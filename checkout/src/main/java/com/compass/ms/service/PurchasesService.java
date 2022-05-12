package com.compass.ms.service;

import com.compass.ms.DTO.PurchasesDTO;
import com.compass.ms.DTO.PurchasesFormDTO;

public interface PurchasesService {

    PurchasesDTO save(PurchasesFormDTO body);

}
