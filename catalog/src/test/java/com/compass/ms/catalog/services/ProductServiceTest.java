package com.compass.ms.catalog.services;

import com.compass.ms.catalog.DTOs.ProductDTO;
import com.compass.ms.catalog.DTOs.ProductFormDTO;
import com.compass.ms.catalog.models.Category;
import com.compass.ms.catalog.models.Instances;
import com.compass.ms.catalog.models.Product;
import com.compass.ms.catalog.repositories.CategoryRepository;
import com.compass.ms.catalog.repositories.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.compass.ms.catalog.models.Instances.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("Product Service Test")
public class ProductServiceTest {

    @TestConfiguration
    static class ProductServiceTestConfiguration{
        @Bean
        public ProductService productService(){
            return new ProductServiceImpl();
        }

        @Bean
        public ModelMapper modelMapper(){
            return new ModelMapper();
        }
    }

    @MockBean
    CategoryRepository categoryRepository;

    @MockBean
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Test
    public void test(){
        List<String> ids = new ArrayList<>();
        ids.add("624f3fe23b9bc442d1e0b034");
        ids.add("624f3fe23b9bc442d1e0b038");
        when(categoryRepository.findById(ids.get(0))).thenReturn(Optional.of(Instances.category()));
        when(categoryRepository.findById(ids.get(1))).thenReturn(Optional.of(Instances.categoryTwo()));
        assertTrue(true);

    }

    @Test
    @DisplayName("Deve retornar satus 200 ao deletar um produto")
    public void shouldHaveReturnStatusOkWhenDeleteProduct(){
        when(productRepository.findById(anyString())).thenReturn(Optional.of(productOne()));
        HttpStatus status = productService.delete("abcd");
        assertNotNull(status);
        assertEquals(HttpStatus.OK, status);
    }

    @Test
    @DisplayName("Deve retornar satus 404 ao tentar deletar um produto inexistente")
    public void shouldHaveReturnStatusNotFoundWhenTryDeleteInvalidProduct(){
        when(productRepository.findById(anyString())).thenReturn(Optional.empty());
        HttpStatus status = productService.delete("abcd");
        assertNotNull(status);
        assertEquals(HttpStatus.NOT_FOUND, status);
    }

    @Test
    @DisplayName("Deve retornar produto após atualização")
    public void shouldHaveReturnProductDtoWhenUpdateProduct(){
        Product update = productOne();
        update.setName("New name");
        update.setDescription("New description");
        update.setActive(!update.isActive());
        when(productRepository.findById(anyString())).thenReturn(Optional.of(productOne()));
        when(productRepository.save(any(Product.class))).thenReturn(update);
        when(categoryRepository.findById(anyString())).thenReturn(Optional.of(category()));

        Optional<ProductDTO> updated = productService.update(productForm(), "abcd");
        assertTrue(updated.isPresent());
        ProductDTO dto = new ModelMapper().map(productOne(), ProductDTO.class);
        assertNotEquals(dto, updated.get());
        verify(categoryRepository, times(productForm().getCategory_ids().size())).findById(anyString());
    }

    @Test
    @DisplayName("Deve retornar Optional vazio ao tentar atualizar produto inexistente")
    public void shouldHaveReturnEmptyOptionalWhenTryUpdateProductWithInvalidId(){
        when(productRepository.findById(anyString())).thenReturn(Optional.empty());
        Optional<ProductDTO> updated = productService.update(productForm(), "abcd");
        assertFalse(updated.isPresent());
        verify(categoryRepository, times(0)).findById(anyString());
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao tentar atualizar um produto com categoria inativa")
    public void shouldHaveThrowIllegalArgumentExceptionWhenTryUpdateProductWithCategoryInactive(){
        when(productRepository.findById(anyString())).thenReturn(Optional.of(productOne()));
        Category category = category();
        category.setActive(false);
        when(categoryRepository.findById(anyString())).thenReturn(Optional.of(category));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Optional<ProductDTO> updated = productService.update(productForm(), "abcd");
            assertNull(updated);
        });
        assertEquals("Categoria(s) não está(ão) ativa(s) ou não encontrada(s)!", exception.getMessage());
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao tentar atualizar um produto com categoria inexistente")
    public void shouldHaveThrowIllegalArgumentExceptionWhenTryUpdateProductWithCategoryInvalid(){
        when(productRepository.findById(anyString())).thenReturn(Optional.of(productOne()));
        when(categoryRepository.findById(anyString())).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Optional<ProductDTO> updated = productService.update(productForm(), "abcd");
            assertNull(updated);
        });
        assertEquals("Categoria(s) não está(ão) ativa(s) ou não encontrada(s)!", exception.getMessage());
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    @DisplayName("Deve retornar Optional com Produto ao buscar produto por Id")
    public void shouldHaveReturnProductDtoWhenFindProductById(){
        when(productRepository.findById(anyString())).thenReturn(Optional.of(productOne()));
        Optional<ProductDTO> found = productService.findById("abcd");
        assertTrue(found.isPresent());
        assertEquals(productDTO(), found.get());
    }

    @Test
    @DisplayName("Deve retornar Optional vazio ao buscar produto por Id inexistente")
    public void shouldHaveReturnEmptyOptionalWhenFindProductByIdBecauseInvalidId(){
        when(productRepository.findById(anyString())).thenReturn(Optional.empty());
        Optional<ProductDTO> found = productService.findById("abcd");
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Deve retornar lista com Produtos ao buscar todos produto")
    public void shouldHaveReturnListOfProductDtoWhenGetAllProducts(){
        List<Product> products = new ArrayList<>();
        products.add(productOne());
        products.add(productTwo());
        when(productRepository.findAll()).thenReturn(products);
        List<ProductDTO> productsDTO = productService.findAll();
        assertNotNull(productsDTO);
        ProductDTO oneDTO = new ModelMapper().map(productOne(), ProductDTO.class);
        ProductDTO twoDTO = new ModelMapper().map(productTwo(), ProductDTO.class);
        assertEquals(oneDTO, productsDTO.get(0));
        assertEquals(twoDTO, productsDTO.get(1));
    }

    @Test
    @DisplayName("Deve retornar ProdutoDTO ao salvar produto")
    public void shouldHaveReturnProductDtoWhenSaveProduct(){
        ProductFormDTO form = productForm();
        form.getCategory_ids().add("624f3fe23b9bc442d1e0b038");
        when(productRepository.save(any())).thenReturn(productOne());
        when(categoryRepository.findById(form.getCategory_ids().get(0))).thenReturn(Optional.of(category()));
        when(categoryRepository.findById(form.getCategory_ids().get(1))).thenReturn(Optional.of(categoryTwo()));
        ProductDTO saved = productService.save(form);
        assertNotNull(saved);
        assertEquals(productDTO(), saved);
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao tentar salvar produto com categoria inexistente")
    public void shouldHaveThrowIllegalArgumentExceptionWhenSaveProductWithInvalidCategory(){
        ProductFormDTO form = productForm();
        when(categoryRepository.findById(form.getCategory_ids().get(0))).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ProductDTO saved = productService.save(form);
            assertNull(saved);
        });

        assertEquals("Categoria(s) não encontrada(s)!", exception.getMessage());
        verify(productRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao tentar salvar produto com categoria inativa")
    public void shouldHaveThrowIllegalArgumentExceptionWhenSaveProductWithInactiveCategory(){
        ProductFormDTO form = productForm();
        Category category = category();
        category.setActive(false);
        when(categoryRepository.findById(form.getCategory_ids().get(0))).thenReturn(Optional.of(category));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ProductDTO saved = productService.save(form);
            assertNull(saved);
        });

        assertEquals("Categoria(s) não está(ão) ativa(s)!", exception.getMessage());
        verify(productRepository, times(0)).save(any());
        verify(categoryRepository, atLeastOnce()).findById(anyString());
    }

}
