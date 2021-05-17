package com.mbeliakov.snippeter.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mbeliakov.snippeter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SnippetSectionModelTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SnippetSectionModel.class);
        SnippetSectionModel snippetSectionModel1 = new SnippetSectionModel();
        snippetSectionModel1.setId(1L);
        SnippetSectionModel snippetSectionModel2 = new SnippetSectionModel();
        assertThat(snippetSectionModel1).isNotEqualTo(snippetSectionModel2);
        snippetSectionModel2.setId(snippetSectionModel1.getId());
        assertThat(snippetSectionModel1).isEqualTo(snippetSectionModel2);
        snippetSectionModel2.setId(2L);
        assertThat(snippetSectionModel1).isNotEqualTo(snippetSectionModel2);
        snippetSectionModel1.setId(null);
        assertThat(snippetSectionModel1).isNotEqualTo(snippetSectionModel2);
    }
}
