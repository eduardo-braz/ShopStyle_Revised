package com.compass.ms.catalog.services;

import com.compass.ms.catalog.DTOs.ProductDTO;
import com.compass.ms.catalog.DTOs.VariationDTO;
import com.compass.ms.catalog.exceptions.InvalidOperationException;
import com.compass.ms.catalog.exceptions.NotFoundException;
import com.compass.ms.catalog.models.Variation;
import com.compass.ms.catalog.repositories.ProductRepository;
import com.compass.ms.catalog.repositories.VariationRepository;
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

import java.util.Optional;

import static com.compass.ms.catalog.models.Instances.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("Variations Service Test")
public class VariationServiceTest {

    @TestConfiguration
    static class VariationServiceTestConfiguration{
        @Bean
        public VariationService variationService(){
            return new VariationServiceImpl();
        }
        @Bean
        public ModelMapper modelMapper(){
            return new ModelMapper();
        }
    }

    @MockBean
    VariationRepository variationRepository;

    @MockBean
    ProductRepository productRepository;

    @Autowired
    VariationService variationService;

    @Test
    @DisplayName("Deve retornar o Produto ao buscar por variation Id")
    public void shouldHaveReturnProductWhenFindProductByVariationId() throws NotFoundException {
        when(variationRepository.findById(anyString())).thenReturn(Optional.of(variationOne()));
        when(productRepository.findByVariationsIdEquals(anyString())).thenReturn(Optional.of(productOne()));
        ProductDTO found = variationService.findById("abcd");
        assertNotNull(found);
        assertEquals(productDTO(), found);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao buscar por variation Id inválido")
    public void shouldHaveThrowNotFoundExceptionWhenFindProductByInvalidVariationId() throws NotFoundException {
        when(variationRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, ()-> {
            ProductDTO found = variationService.findById("abcd");
            assertNull(found);
        });
        verify(productRepository, times(0)).findByVariationsIdEquals(anyString());
    }

    @Test
    @DisplayName("Deve lançar status OK ao deletar variation")
    public void shouldHaveReturnStatusOkWhenDeleteVariation() throws NotFoundException {
        when(variationRepository.findById(anyString())).thenReturn(Optional.of(variationOne()));
        HttpStatus status = variationService.delete("abcd");
        assertNotNull(status);
        assertEquals(HttpStatus.OK, status);
        verify(variationRepository, times(1)).delete(any());
    }

    @Test
    @DisplayName("Deve lançar status Not Found ao tentar deletar variation inexistente")
    public void shouldHaveReturnStatusNotFoundWhenDeleteInvalidVariation() {
        when(variationRepository.findById(anyString())).thenReturn(Optional.empty());
        HttpStatus status = variationService.delete("abcd");
        assertNotNull(status);
        assertEquals(HttpStatus.NOT_FOUND, status);
        verify(variationRepository, times(0)).delete(any());
    }

    @Test
    @DisplayName("Deve retornar VariationDto ao atualizar variation")
    public void shouldHaveReturnVariationDtoWhenUpdateVariation() throws InvalidOperationException {
        Variation newVariation = variationTwo();
        newVariation.setColor("new color");
        newVariation.setQuantity(100);
        newVariation.setSize("new size");
        when(variationRepository.findById(anyString())).thenReturn(Optional.of(variationOne()));
        when(productRepository.findById(any())).thenReturn(Optional.of(productOne()));
        when(variationRepository.save(any())).thenReturn(newVariation);

        VariationDTO updated = variationService.update(variationForm(), "abcd");
        assertNotNull(updated);
        assertNotEquals(variationDTO(), updated);

        verify(variationRepository, times(1)).save(any());
        verify(productRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve lançar InvalidOperationException ao tentar atualizar variation inexistente")
    public void shouldHaveThrowInvalidOperationExceptionWhenTryUpdateVariationNotFound() throws NotFoundException, InvalidOperationException {
        when(variationRepository.findById(anyString())).thenReturn(Optional.empty());
        InvalidOperationException exception = assertThrows(InvalidOperationException.class, () -> {
            VariationDTO updated = variationService.update(variationForm(), "abcd");
            assertNull(updated);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Variação não encontrada.", exception.getMessage());
        assertEquals("id", exception.getFieldName());
        verify(productRepository, times(0)).findById(anyString());
        verify(variationRepository, times(0)).save(any());
        verify(productRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Deve lançar InvalidOperationException ao tentar atualizar variation que produto não foi encontrado")
    public void shouldHaveThrowInvalidOperationExceptionWhenTryUpdateVariationWithProductNotFound() throws InvalidOperationException {
        when(variationRepository.findById(anyString())).thenReturn(Optional.of(variationOne()));
        when(productRepository.findById(any())).thenReturn(Optional.empty());
        InvalidOperationException exception = assertThrows(InvalidOperationException.class, () -> {
            VariationDTO updated = variationService.update(variationForm(), "abcd");
            assertNull(updated);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Produto não encontrado.", exception.getMessage());
        assertEquals("product_id", exception.getFieldName());
        verify(productRepository, times(1)).findById(anyString());
        verify(variationRepository, times(0)).save(any());
        verify(productRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Deve lançar InvalidOperationException ao tentar salvar variation que produto não foi encontrado")
    public void shouldHaveThrowInvalidOperationExceptionWhenTrySaveVariationWithProductNotFound() throws InvalidOperationException {
        when(productRepository.findById(any())).thenReturn(Optional.empty());
        InvalidOperationException exception = assertThrows(InvalidOperationException.class, () -> {
            VariationDTO saved = variationService.save(variationForm());
            assertNull(saved);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Produto não encontrado.", exception.getMessage());
        assertEquals("product_id", exception.getFieldName());
        verify(variationRepository, times(0)).save(any());
        verify(productRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Deve retornar objeto VariationDto ao salvar variation")
    public void shouldHaveReturnVariationDtoWhenSaveVariation() throws InvalidOperationException {
        when(productRepository.findById(any())).thenReturn(Optional.of(productOne()));
        when(variationRepository.save(any())).thenReturn(variationOne());
        VariationDTO saved = variationService.save(variationForm());
        assertNotNull(saved);
        assertEquals(variationDTO(), saved);

    }

}
