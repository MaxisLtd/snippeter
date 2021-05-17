package com.mbeliakov.snippeter.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mbeliakov.snippeter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LangTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LangType.class);
        LangType langType1 = new LangType();
        langType1.setId(1L);
        LangType langType2 = new LangType();
        langType2.setId(langType1.getId());
        assertThat(langType1).isEqualTo(langType2);
        langType2.setId(2L);
        assertThat(langType1).isNotEqualTo(langType2);
        langType1.setId(null);
        assertThat(langType1).isNotEqualTo(langType2);
    }
}
