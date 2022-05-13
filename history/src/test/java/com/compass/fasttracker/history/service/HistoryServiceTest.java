package com.compass.fasttracker.history.service;

import com.compass.fasttracker.history.DTOs.HistoricDTO;
import com.compass.fasttracker.history.Instance.Instances;
import com.compass.fasttracker.history.models.Historic;
import com.compass.fasttracker.history.repository.HistoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("History Controller Test")
public class HistoryServiceTest {

    @TestConfiguration
    public static class HistoryServiceTestConfiguration{
        @Bean
        public HistoryService historyService(){
            return new HistoryServiceImpl();
        }
        @Bean
        public ModelMapper mapper(){
            return new ModelMapper();
        }
    }

    @Autowired
    HistoryService historyService;

    @MockBean
    HistoryRepository historyRepository;

    @Test
    public void shouldHaveReturnHistoricDtoWhenGetHistoricByIdUser(){
        List<Historic> historics = new ArrayList<>();
        historics.add(Instances.historic());
        when(historyRepository.findByUserId(anyLong())).thenReturn(historics);
        HistoricDTO dto = historyService.getHistoric(1L);
        assertNotNull(dto);
        assertEquals(Instances.historicDTO(), dto);
    }

    @Test
    public void shouldThrowResponseStatusExceptionWhenGetHistoricWithInvalidIdUser(){
        when(historyRepository.findByUserId(anyLong())).thenReturn(Collections.EMPTY_LIST);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            HistoricDTO dto = historyService.getHistoric(1L);
            assertNull(dto);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }





}
