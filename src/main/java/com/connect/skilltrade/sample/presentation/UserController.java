package com.connect.skilltrade.sample.presentation;

import com.connect.skilltrade.sample.application.SampleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final SampleUseCase sampleUseCase;
}
