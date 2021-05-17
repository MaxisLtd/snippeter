package com.mbeliakov.snippeter.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mbeliakov.snippeter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LangTypeModelTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LangTypeModel.class);
        LangTypeModel langTypeModel1 = new LangTypeModel();
        langTypeModel1.setId(1L);
        LangTypeModel langTypeModel2 = new LangTypeModel();
        assertThat(langTypeModel1).isNotEqualTo(langTypeModel2);
        langTypeModel2.setId(langTypeModel1.getId());
        assertThat(langTypeModel1).isEqualTo(langTypeModel2);
        langTypeModel2.setId(2L);
        assertThat(langTypeModel1).isNotEqualTo(langTypeModel2);
        langTypeModel1.setId(null);
        assertThat(langTypeModel1).isNotEqualTo(langTypeModel2);
    }
}
