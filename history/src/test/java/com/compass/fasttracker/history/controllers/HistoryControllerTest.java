package com.compass.fasttracker.history.controllers;

import com.compass.fasttracker.history.DTOs.HistoricDTO;
import com.compass.fasttracker.history.Instance.Instances;
import com.compass.fasttracker.history.controller.HistoryController;
import com.compass.fasttracker.history.service.HistoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@DisplayName("History Controller Test")
public class HistoryControllerTest {

    @MockBean
    HistoryService historyService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    HistoryController historyController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(historyController).build();
    }

    @Test
    @DisplayName("Deve retornar status 200 e variation ao atualizar")
    public void shouldHaveReturnStatusOkWhenUpdateVariation() throws Exception {
        when(historyService.getHistoric(any(Long.class))).thenReturn(Instances.historicDTO());
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/v1/historic/user/{idUser}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect( result -> {
                    HistoricDTO historicDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                            HistoricDTO.class);
                    assertEquals(Instances.historicDTO(), historicDTO);
                } );
    }

}
