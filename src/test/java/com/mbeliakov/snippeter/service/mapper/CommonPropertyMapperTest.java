package com.mbeliakov.snippeter.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommonPropertyMapperTest {

    private CommonPropertyMapper commonPropertyMapper;

    @BeforeEach
    public void setUp() {
        commonPropertyMapper = new CommonPropertyMapperImpl();
    }
}
