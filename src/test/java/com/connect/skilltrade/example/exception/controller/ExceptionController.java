package com.connect.skilltrade.example.exception.controller;

import com.connect.skilltrade.common.exception.BusinessException;
import com.connect.skilltrade.example.exception.domain.SampleExceptionStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionController {

    @GetMapping("/play-ground/exception-handler")
    public void makeException() {
        throw new BusinessException(
                new NullPointerException("This is Null"),
                SampleExceptionStatus.SAMPLE_CODE,
                "Test", "Exception Message"
        );
    }
}
