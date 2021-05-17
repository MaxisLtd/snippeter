package com.mbeliakov.snippeter.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mbeliakov.snippeter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommonPropertyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommonProperty.class);
        CommonProperty commonProperty1 = new CommonProperty();
        commonProperty1.setId(1L);
        CommonProperty commonProperty2 = new CommonProperty();
        commonProperty2.setId(commonProperty1.getId());
        assertThat(commonProperty1).isEqualTo(commonProperty2);
        commonProperty2.setId(2L);
        assertThat(commonProperty1).isNotEqualTo(commonProperty2);
        commonProperty1.setId(null);
        assertThat(commonProperty1).isNotEqualTo(commonProperty2);
    }
}
