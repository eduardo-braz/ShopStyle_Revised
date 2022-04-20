package com.compass.ms.catalog.services;

import com.compass.ms.catalog.DTOs.ProductDTO;
import com.compass.ms.catalog.DTOs.VariationDTO;
import com.compass.ms.catalog.DTOs.VariationFormDTO;
import com.compass.ms.catalog.exceptions.InvalidOperationException;
import com.compass.ms.catalog.exceptions.NotFoundException;
import com.compass.ms.catalog.models.Product;
import com.compass.ms.catalog.models.Variation;
import com.compass.ms.catalog.repositories.ProductRepository;
import com.compass.ms.catalog.repositories.VariationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VariationServiceImpl implements VariationService{

    @Autowired
    private VariationRepository variationRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Optional<VariationDTO> save(VariationFormDTO form) throws InvalidOperationException {
        Optional<Product> product = this.productRepository.findById(form.getProduct_id());
        if (product.isPresent()) {
            Variation map = modelMapper.map(form, Variation.class);
            Variation variation = this.variationRepository.save(map);
            product.get().getVariations().add(variation);
            this.productRepository.save(product.get());
            return Optional.of(
                    modelMapper.map(variation, VariationDTO.class));
        }
        throw new InvalidOperationException("Produto não encontrado.", "product_id",
                form.getProduct_id(), HttpStatus.NOT_FOUND);
    }

    @Override
    public VariationDTO update(VariationFormDTO form, String id) throws InvalidOperationException {
        Optional<Variation> variation = this.variationRepository.findById(id);
        if (variation.isPresent()){
            Optional<Product> product = this.productRepository.findById(form.getProduct_id());
            if (product.isPresent()){
                variation.get().setColor(form.getColor());
                variation.get().setPrice(form.getPrice());
                variation.get().setQuantity(form.getQuantity());
                variation.get().setSize(form.getSize());
                Variation variationSaved = this.variationRepository.save(variation.get());
                // Updating variation in product
                product.get().getVariations().forEach(var -> {
                    if (var.getId().equals(variationSaved.getId()))
                        product.get().getVariations().set(
                            product.get().getVariations().indexOf(var), var);
                });
                this.productRepository.save(product.get());
                return modelMapper.map(variationSaved, VariationDTO.class);
            }
            throw new InvalidOperationException("Produto não encontrado.", "product_id",
                    form.getProduct_id(), HttpStatus.NOT_FOUND);
        }
        throw new InvalidOperationException("Categoria não encontrada.", "id",
                form.getProduct_id(), HttpStatus.NOT_FOUND);
    }


    @Override
    public HttpStatus delete(String id){
        Optional<Variation> variation = this.variationRepository.findById(id);
        if (variation.isPresent()){
            this.variationRepository.delete(variation.get());
            return HttpStatus.OK;
        }
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public ProductDTO findById(String id) throws NotFoundException {
        Optional<Variation> variation = this.variationRepository.findById(id);
        if (variation.isPresent()){
            Optional<Product> productFound = this.productRepository.findByVariationsIdEquals(id);
            return modelMapper.map(productFound.get(), ProductDTO.class);
        }
        throw new NotFoundException();
    }
}
