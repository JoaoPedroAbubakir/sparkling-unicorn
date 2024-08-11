package com.abubakir.timekeeper.app.controller;

import com.abubakir.timekeeper.app.dto.ClockInDTO;
import com.abubakir.timekeeper.service.TimeSheetService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/batidas")
@AllArgsConstructor
public class TimeSheetController {

    TimeSheetService timeSheetService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postClockTime(@RequestBody @Valid ClockInDTO clockInDTO) {
        timeSheetService.processTimeStamp(clockInDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<String> getTimeSheet() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
