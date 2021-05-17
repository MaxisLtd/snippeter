package com.mbeliakov.snippeter.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mbeliakov.snippeter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SnippetModelTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SnippetModel.class);
        SnippetModel snippetModel1 = new SnippetModel();
        snippetModel1.setId(1L);
        SnippetModel snippetModel2 = new SnippetModel();
        assertThat(snippetModel1).isNotEqualTo(snippetModel2);
        snippetModel2.setId(snippetModel1.getId());
        assertThat(snippetModel1).isEqualTo(snippetModel2);
        snippetModel2.setId(2L);
        assertThat(snippetModel1).isNotEqualTo(snippetModel2);
        snippetModel1.setId(null);
        assertThat(snippetModel1).isNotEqualTo(snippetModel2);
    }
}
