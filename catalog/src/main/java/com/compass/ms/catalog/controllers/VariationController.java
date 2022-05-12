package com.compass.ms.catalog.controllers;

import com.compass.ms.catalog.DTOs.ProductDTO;
import com.compass.ms.catalog.DTOs.VariationDTO;
import com.compass.ms.catalog.DTOs.VariationFormDTO;
import com.compass.ms.catalog.exceptions.InvalidOperationException;
import com.compass.ms.catalog.exceptions.NotFoundException;
import com.compass.ms.catalog.services.VariationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/v1/variations")
public class VariationController {

    @Autowired
    private VariationService variationService;

    @PostMapping
    public ResponseEntity<VariationDTO> save(@RequestBody @Valid VariationFormDTO form)
            throws InvalidOperationException {
        return new ResponseEntity<>(this.variationService.save(form), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<VariationDTO> update(@RequestBody @Valid VariationFormDTO form, @PathVariable String id)
            throws InvalidOperationException {
        return new ResponseEntity<>(this.variationService.update(form, id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<VariationDTO> delete(@PathVariable String id) {
        return new ResponseEntity<>(this.variationService.delete(id));
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductDTO> findProductByIdVariation(@PathVariable String id) throws NotFoundException {
        return new ResponseEntity<>(this.variationService.findById(id),
                HttpStatus.OK);
    }



}
