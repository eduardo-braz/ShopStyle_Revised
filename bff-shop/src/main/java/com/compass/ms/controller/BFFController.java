package com.compass.ms.controller;

import com.compass.ms.DTOs.LoginFormDTO;
import com.compass.ms.DTOs.TokenDTO;
import com.compass.ms.DTOs.catalog.ProductDTO;
import com.compass.ms.DTOs.checkout.PaymentDTO;
import com.compass.ms.DTOs.checkout.PurchasesDTO;
import com.compass.ms.DTOs.checkout.PurchasesFormDTO;
import com.compass.ms.DTOs.customer.UserDTO;
import com.compass.ms.DTOs.customer.UserFormDTO;
import com.compass.ms.DTOs.history.HistoricDTO;
import com.compass.ms.services.BFFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class BFFController {

    @Autowired
    private BFFService service;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginFormDTO body){
        return new ResponseEntity<>(this.service.login(body), HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserFormDTO body) {
        return new ResponseEntity<>(this.service.saveUser(body), HttpStatus.CREATED);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id){
        return new ResponseEntity<>(this.service.getUser(id), HttpStatus.OK);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDTO> UpdateUser(@RequestBody UserFormDTO body, @PathVariable Long id){
        return new ResponseEntity<>(this.service.UpdateUser(body, id), HttpStatus.OK);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable String id){
        return new ResponseEntity<>(this.service.getProduct(id), HttpStatus.OK);
    }

    @GetMapping("/categories/{id}/products")
    public ResponseEntity<List<ProductDTO>> getProductsByCategories(@PathVariable String id){
        return new ResponseEntity<>(this.service.getProductsByCategories(id), HttpStatus.OK);
    }

    @GetMapping("/payments")
    public ResponseEntity<List<PaymentDTO>> getPayments(){
        return new ResponseEntity<>(this.service.getPayments(), HttpStatus.OK);
    }

    @PostMapping("/purchases")
    public ResponseEntity<PurchasesDTO> savePurchase(@RequestBody PurchasesFormDTO body){
        return new ResponseEntity<>(this.service.savePurchase(body), HttpStatus.CREATED);
    }

    @GetMapping("/historic/user/{idUser}")
    public ResponseEntity<HistoricDTO> getHistoricUser(@PathVariable Long idUser){
        return new ResponseEntity<>(this.service.getHistoricUser(idUser), HttpStatus.OK);
    }

}
