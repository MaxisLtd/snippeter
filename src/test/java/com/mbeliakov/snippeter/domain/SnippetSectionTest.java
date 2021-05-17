package com.mbeliakov.snippeter.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mbeliakov.snippeter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SnippetSectionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SnippetSection.class);
        SnippetSection snippetSection1 = new SnippetSection();
        snippetSection1.setId(1L);
        SnippetSection snippetSection2 = new SnippetSection();
        snippetSection2.setId(snippetSection1.getId());
        assertThat(snippetSection1).isEqualTo(snippetSection2);
        snippetSection2.setId(2L);
        assertThat(snippetSection1).isNotEqualTo(snippetSection2);
        snippetSection1.setId(null);
        assertThat(snippetSection1).isNotEqualTo(snippetSection2);
    }
}
