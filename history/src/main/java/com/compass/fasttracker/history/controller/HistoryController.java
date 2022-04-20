package com.compass.fasttracker.history.controller;

import com.compass.fasttracker.history.DTOs.HistoricDTO;
import com.compass.fasttracker.history.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/historic/user")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @GetMapping("/{idUser}")
    public ResponseEntity<HistoricDTO> getHistoric(@PathVariable Long idUser){
        return new ResponseEntity<>(this.historyService.getHistoric(idUser),
                HttpStatus.OK);
    }
}
