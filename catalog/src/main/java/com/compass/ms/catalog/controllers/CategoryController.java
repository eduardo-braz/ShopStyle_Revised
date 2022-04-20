package com.compass.ms.catalog.controllers;


import com.compass.ms.catalog.DTOs.CategoryDTO;
import com.compass.ms.catalog.DTOs.CategoryFormDTO;
import com.compass.ms.catalog.DTOs.ProductDTO;
import com.compass.ms.catalog.exceptions.NotFoundException;
import com.compass.ms.catalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDTO> save(@RequestBody @Valid CategoryFormDTO body){
        CategoryDTO response = categoryService.save(body);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        return new ResponseEntity<>(this.categoryService.delete(id));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAll(){
        return new ResponseEntity<>(
                this.categoryService.findAll(),
                HttpStatus.OK);
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductDTO>> getProductsOfCategory(@PathVariable String id) throws NotFoundException {
        return new ResponseEntity<>(
                this.categoryService.findAllProductsByCategpryId(id),
                HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> update(@RequestBody @Valid CategoryFormDTO form, @PathVariable String id)
            throws NotFoundException {
        return new ResponseEntity<>(
                this.categoryService.update(form, id),
                HttpStatus.OK);
    }

}
