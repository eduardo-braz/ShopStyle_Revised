package com.compass.ms.service;

import com.compass.ms.DTO.PaymentDTO;
import com.compass.ms.DTO.PaymentFormDTO;
import com.compass.ms.entity.Payment;
import com.compass.ms.repository.PaymentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService{

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public PaymentDTO save(PaymentFormDTO form) {
        try{
            Payment saved = this.paymentRepository.save(modelMapper.map(form, Payment.class));
            System.out.println(saved.toString());
            return modelMapper.map(saved, PaymentDTO.class);
        } catch (Exception e){
            throw new RuntimeException();       // Mudar exceção
        }
    }

    @Override
    public List<PaymentDTO> getAll() {
        List<Payment> paymentList = this.paymentRepository.findAll();
        List<PaymentDTO> listDto = new ArrayList<>();
        paymentList.forEach(payment -> {
            listDto.add(modelMapper.map(payment, PaymentDTO.class));
        });
        return listDto;
    }

    @Override
    public Optional<PaymentDTO> getById(Long id) {
        Optional<Payment> found = this.paymentRepository.findById(id);
        if (found.isPresent())
            return Optional.of(modelMapper.map(found.get(), PaymentDTO.class));
        return Optional.empty();
    }

    @Override
    public Optional<PaymentDTO> update(PaymentFormDTO form, Long id) {
        Optional<Payment> found = this.paymentRepository.findById(id);
        if (found.isPresent()) {
            found.get().setDiscount(form.getDiscount());
           // found.get().setType(form.getType());
            found.get().setStatus(form.isStatus());
            Payment updated = this.paymentRepository.save(found.get());
            return Optional.of(modelMapper.map(updated, PaymentDTO.class));
        }
        return Optional.empty();
    }

    @Override
    public HttpStatus delete(Long id) {
        Optional<Payment> payment = this.paymentRepository.findById(id);
        if (payment.isPresent()) {
            this.paymentRepository.delete(payment.get());
            return HttpStatus.OK;
        }
        return HttpStatus.NOT_FOUND;
    }
}
