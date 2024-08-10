package com.abubakir.timekeeper.app.controller;

import com.abubakir.timekeeper.app.dto.ClockInDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/batidas")
public class TimeSheetController {

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postClockTime(@RequestBody @Valid ClockInDTO clockInDTO) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<String> getTimeSheet() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
