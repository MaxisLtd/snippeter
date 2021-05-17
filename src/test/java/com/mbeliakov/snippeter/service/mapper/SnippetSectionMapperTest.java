package com.mbeliakov.snippeter.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SnippetSectionMapperTest {

    private SnippetSectionMapper snippetSectionMapper;

    @BeforeEach
    public void setUp() {
        snippetSectionMapper = new SnippetSectionMapperImpl();
    }
}
