package com.mbeliakov.snippeter.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mbeliakov.snippeter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommonPropertyModelTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommonPropertyModel.class);
        CommonPropertyModel commonPropertyModel1 = new CommonPropertyModel();
        commonPropertyModel1.setId(1L);
        CommonPropertyModel commonPropertyModel2 = new CommonPropertyModel();
        assertThat(commonPropertyModel1).isNotEqualTo(commonPropertyModel2);
        commonPropertyModel2.setId(commonPropertyModel1.getId());
        assertThat(commonPropertyModel1).isEqualTo(commonPropertyModel2);
        commonPropertyModel2.setId(2L);
        assertThat(commonPropertyModel1).isNotEqualTo(commonPropertyModel2);
        commonPropertyModel1.setId(null);
        assertThat(commonPropertyModel1).isNotEqualTo(commonPropertyModel2);
    }
}
