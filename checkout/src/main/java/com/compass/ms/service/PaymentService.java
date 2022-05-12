package com.compass.ms.service;

import com.compass.ms.DTO.PaymentDTO;
import com.compass.ms.DTO.PaymentFormDTO;
import org.springframework.http.HttpStatus;

import java.net.ConnectException;
import java.util.List;
import java.util.Optional;


public interface PaymentService {

    PaymentDTO save(PaymentFormDTO form);

    List<PaymentDTO> getAll();

    Optional<PaymentDTO> getById(Long id);

    Optional<PaymentDTO> update(PaymentFormDTO form, Long id);

    HttpStatus delete(Long id);


}
