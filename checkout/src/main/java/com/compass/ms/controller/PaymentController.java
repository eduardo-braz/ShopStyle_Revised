package com.compass.ms.controller;

import com.compass.ms.DTO.PaymentDTO;
import com.compass.ms.DTO.PaymentFormDTO;
import com.compass.ms.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/payments")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping
    @Transactional
    public ResponseEntity<PaymentDTO> save(@RequestBody @Valid PaymentFormDTO body){
        return new ResponseEntity<>(this.paymentService.save(body), HttpStatus.CREATED);
    }

    @GetMapping
    @Transactional
    public ResponseEntity<List<PaymentDTO>> getAll(){
        return new ResponseEntity<>(this.paymentService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<PaymentDTO> getById(@PathVariable Long id){
        Optional<PaymentDTO> found = this.paymentService.getById(id);
        if (found.isPresent())
            return new ResponseEntity<>(found.get(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<PaymentDTO> save(@RequestBody @Valid PaymentFormDTO body, @PathVariable Long id){
        Optional<PaymentDTO> updated = this.paymentService.update(body, id);
        if (updated.isPresent())
            return new ResponseEntity<>(updated.get(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id){
        return new ResponseEntity<>(this.paymentService.delete(id));
    }

}
