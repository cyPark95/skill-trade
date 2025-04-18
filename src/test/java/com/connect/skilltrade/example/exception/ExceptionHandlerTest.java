package com.connect.skilltrade.example.exception;

import com.connect.skilltrade.common.exception.handler.BusinessExceptionHandler;
import com.connect.skilltrade.common.response.ExceptionResponse;
import com.connect.skilltrade.example.exception.controller.ExceptionController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({BusinessExceptionHandler.class, ExceptionController.class})
public class ExceptionHandlerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Exception Handler 예제")
    @Test
    void sampleExceptionHandle() throws Exception {
        // when
        MvcResult result = mvc.perform(get("/play-ground/exception-handler"))
                .andExpect(status().isInternalServerError())
                .andReturn();

        // then
        ExceptionResponse response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), ExceptionResponse.class);
        assertAll(
                () -> assertThat(response.message()).isEqualTo("Exception: [Test] Exception Message"),
                () -> assertThat(response.code()).isEqualTo(-99)
        );
    }
}
