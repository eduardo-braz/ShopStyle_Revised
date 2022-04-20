package com.compass.fasttracker.history.service;

import com.compass.fasttracker.history.DTOs.HistoricDTO;

public interface HistoryService {

    public HistoricDTO getHistoric(Long idUser);
}
