package com.mbeliakov.snippeter.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProjectChapterMapperTest {

    private ProjectChapterMapper projectChapterMapper;

    @BeforeEach
    public void setUp() {
        projectChapterMapper = new ProjectChapterMapperImpl();
    }
}
