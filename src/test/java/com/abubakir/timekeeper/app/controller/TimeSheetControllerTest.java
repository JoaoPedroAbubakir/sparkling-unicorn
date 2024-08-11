package com.abubakir.timekeeper.app.controller;


import com.abubakir.timekeeper.configuration.TestBeanConfiguration;
import com.abubakir.timekeeper.service.repository.TimeSheetSimulatedDatabase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Import(TestBeanConfiguration.class)
@ContextConfiguration(classes = TestBeanConfiguration.class)
public class TimeSheetControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;


    @Test
    public void shouldCreate() {
        String json = "{\"dataHora\": \"2024/08/12T09:00:00\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/batidas", entity, String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void shouldReturnBadRequestWithBlankParameterMessage() {
        String invalidBlankJson = "{}";
        String invalidBlankDateTime = "{\"dataHora\": \"\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/batidas", new HttpEntity<>(invalidBlankJson, headers), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).contains("{\"mensagem\":\"Campo obrigatório não informado\"}");
        headers.setContentType(MediaType.APPLICATION_JSON);
        responseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/batidas", new HttpEntity<>(invalidBlankDateTime, headers), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).contains("{\"mensagem\":\"Campo obrigatório não informado\"}");
    }

    @Test
    public void shouldReturnBadRequestWhenInvalidFormatIsProvided() {
        String invalidBecauseMissingT = "{\"dataHora\": \"2024/08/10 14:30:20\"}";
        String invalidBecauseMonthDoesNotExist = "{\"dataHora\": \"2024/20/10 14:30:20\"}";
        String invalidBecauseDayDoesNotExist = "{\"dataHora\": \"2024/08/54 14:30:20\"}";
        String invalidBecauseTimeDoesNotExist = "{\"dataHora\": \"2024/08/10 34:30:20\"}";
        String invalidBecauseLetterMixedUp = "{\"dataHora\": \"2024/0i/10 14:30:20\"}";
        String invalidBecauseWrongFormatYearMissplaced = "{\"dataHora\": \"10/08/2024 14:30:20\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/batidas", new HttpEntity<>(invalidBecauseMissingT, headers), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).contains("{\"mensagem\":\"Data e hora em formato inválido, confira se o formato segue: dd/MM/yyyy'T'HH:mm:ss\"}");
        responseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/batidas", new HttpEntity<>(invalidBecauseMonthDoesNotExist, headers), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).contains("{\"mensagem\":\"Data e hora em formato inválido, confira se o formato segue: dd/MM/yyyy'T'HH:mm:ss\"}");
        responseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/batidas", new HttpEntity<>(invalidBecauseDayDoesNotExist, headers), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).contains("{\"mensagem\":\"Data e hora em formato inválido, confira se o formato segue: dd/MM/yyyy'T'HH:mm:ss\"}");
        responseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/batidas", new HttpEntity<>(invalidBecauseTimeDoesNotExist, headers), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).contains("{\"mensagem\":\"Data e hora em formato inválido, confira se o formato segue: dd/MM/yyyy'T'HH:mm:ss\"}");
        responseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/batidas", new HttpEntity<>(invalidBecauseLetterMixedUp, headers), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).contains("{\"mensagem\":\"Data e hora em formato inválido, confira se o formato segue: dd/MM/yyyy'T'HH:mm:ss\"}");
        responseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/batidas", new HttpEntity<>(invalidBecauseWrongFormatYearMissplaced, headers), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).contains("{\"mensagem\":\"Data e hora em formato inválido, confira se o formato segue: dd/MM/yyyy'T'HH:mm:ss\"}");
    }

}