package com.connect.skilltrade.sample.domain.sub.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubService {

    private final SubReader subReader;
}
