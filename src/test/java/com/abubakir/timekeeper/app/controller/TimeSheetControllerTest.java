package com.abubakir.timekeeper.app.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TimeSheetControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreate() {
        String json = "{\"dataHora\": \"2024/10/08T15:30:20\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/batidas", entity, String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void shouldReturnBadRequestWithBlankParameterMessage() {
        String invalidBlankJson = "{}";
        String invalidBlankDateTime = "{\"dataHora\": \"\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/batidas", new HttpEntity<>(invalidBlankJson, headers), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).contains("{\"mensagem\":\"Campo obrigatório não informado\"}");
        headers.setContentType(MediaType.APPLICATION_JSON);
        responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/batidas", new HttpEntity<>(invalidBlankDateTime, headers), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).contains("{\"mensagem\":\"Campo obrigatório não informado\"}");
    }

    @Test
    void shouldReturnBadRequestWhenInvalidFormatIsProvided() {
        String invalidBecauseMissingT = "{\"dataHora\": \"2024/08/10 14:30:20\"}";
        String invalidBecauseMonthDoesNotExist = "{\"dataHora\": \"2024/20/10 14:30:20\"}";
        String invalidBecauseDayDoesNotExist = "{\"dataHora\": \"2024/08/54 14:30:20\"}";
        String invalidBecauseTimeDoesNotExist = "{\"dataHora\": \"2024/08/10 34:30:20\"}";
        String invalidBecauseLetterMixedUp = "{\"dataHora\": \"2024/0i/10 14:30:20\"}";
        String invalidBecauseWrongFormatYearMissplaced =  "{\"dataHora\": \"10/08/2024 14:30:20\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/batidas", new HttpEntity<>(invalidBecauseMissingT, headers), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).contains("{\"mensagem\":\"Data e hora em formato inválido, confira se o formato segue: dd/MM/yyyy'T'HH:mm:ss\"}");
        responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/batidas", new HttpEntity<>(invalidBecauseMonthDoesNotExist, headers), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).contains("{\"mensagem\":\"Data e hora em formato inválido, confira se o formato segue: dd/MM/yyyy'T'HH:mm:ss\"}");
        responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/batidas", new HttpEntity<>(invalidBecauseDayDoesNotExist, headers), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).contains("{\"mensagem\":\"Data e hora em formato inválido, confira se o formato segue: dd/MM/yyyy'T'HH:mm:ss\"}");
        responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/batidas", new HttpEntity<>(invalidBecauseTimeDoesNotExist, headers), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).contains("{\"mensagem\":\"Data e hora em formato inválido, confira se o formato segue: dd/MM/yyyy'T'HH:mm:ss\"}");
        responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/batidas", new HttpEntity<>(invalidBecauseLetterMixedUp, headers), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).contains("{\"mensagem\":\"Data e hora em formato inválido, confira se o formato segue: dd/MM/yyyy'T'HH:mm:ss\"}");
        responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/batidas", new HttpEntity<>(invalidBecauseWrongFormatYearMissplaced, headers), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).contains("{\"mensagem\":\"Data e hora em formato inválido, confira se o formato segue: dd/MM/yyyy'T'HH:mm:ss\"}");
    }

}