package com.connect.skilltrade.example.docs.controller;

import com.connect.skilltrade.example.docs.controller.request.SpringRestDocsRequest;
import com.connect.skilltrade.example.docs.controller.response.SpringRestDocsResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringRestDocsController {

    @GetMapping("/play-ground/spring-rest-docs")
    public SpringRestDocsResponse checkRestDocs(@RequestBody SpringRestDocsRequest request) {
        return new SpringRestDocsResponse(
                true,
                String.format("Value: %s / Number: %d", request.field(), request.number())
        );
    }
}
