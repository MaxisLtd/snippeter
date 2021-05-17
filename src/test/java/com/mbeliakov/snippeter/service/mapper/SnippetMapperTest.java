package com.mbeliakov.snippeter.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SnippetMapperTest {

    private SnippetMapper snippetMapper;

    @BeforeEach
    public void setUp() {
        snippetMapper = new SnippetMapperImpl();
    }
}
