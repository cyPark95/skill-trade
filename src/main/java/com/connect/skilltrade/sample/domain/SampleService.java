package com.connect.skilltrade.sample.domain;

import com.connect.skilltrade.sample.domain.sub.domain.SubService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SampleService {

    private final SampleReader sampleReader;
    private final SampleStore sampleStore;
    private final SubService subService;
}
