package com.compass.ms.clientFeign;

import com.compass.ms.DTOs.history.HistoricDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("history")
public interface HistoryClient {

    @RequestMapping(value = "/v1/historic/user/{idUser}", method = RequestMethod.GET)
    HistoricDTO getHistoric(@PathVariable Long idUser);
}
