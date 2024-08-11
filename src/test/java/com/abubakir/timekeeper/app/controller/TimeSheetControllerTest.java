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
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Import(TestBeanConfiguration.class)
@ContextConfiguration(classes = TestBeanConfiguration.class)
@DirtiesContext
@TestPropertySource(properties = {
        "spring.kafka.bootstrap-servers.host=localhost",
        "spring.kafka.bootstrap-servers.port=9092",
        "spring.kafka.custom.topic=test-topic"
})
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
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
    public void shouldReturnDuplicateRecord() {
        String json = "{\"dataHora\": \"2024/10/09T10:00:00\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/batidas", entity, String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(responseEntity.getBody()).contains("{\"mensagem\":\"Horários já registrado\"}");
    }

    @Test
    public void shouldReturnInvalidWorkingDay() {
        String json = "{\"dataHora\": \"2024/08/11T09:00:00\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/batidas", entity, String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(responseEntity.getBody()).contains("{\"mensagem\":\"Sábado e domingo não são permitidos como dia de trabalho\"}");
    }

    @Test
    public void shouldReturnLunchTimeError() {
        String json = "{\"dataHora\": \"2024/10/09T12:50:00\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/batidas", entity, String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(responseEntity.getBody()).contains("{\"mensagem\":\"Deve haver no mínimo 1 hora de almoço\"}");
    }

    @Test
    public void shouldReturnMaximumRecordsAllowed() {
        String json = "{\"dataHora\": \"2024/10/08T19:00:00\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/batidas", entity, String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(responseEntity.getBody()).contains("{\"mensagem\":\"Apenas 4 horários podem ser registrados por dia\"}");
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