package com.mbeliakov.snippeter.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LangTypeMapperTest {

    private LangTypeMapper langTypeMapper;

    @BeforeEach
    public void setUp() {
        langTypeMapper = new LangTypeMapperImpl();
    }
}
