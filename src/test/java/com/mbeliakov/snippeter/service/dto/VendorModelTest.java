package com.mbeliakov.snippeter.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mbeliakov.snippeter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VendorModelTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VendorModel.class);
        VendorModel vendorModel1 = new VendorModel();
        vendorModel1.setId(1L);
        VendorModel vendorModel2 = new VendorModel();
        assertThat(vendorModel1).isNotEqualTo(vendorModel2);
        vendorModel2.setId(vendorModel1.getId());
        assertThat(vendorModel1).isEqualTo(vendorModel2);
        vendorModel2.setId(2L);
        assertThat(vendorModel1).isNotEqualTo(vendorModel2);
        vendorModel1.setId(null);
        assertThat(vendorModel1).isNotEqualTo(vendorModel2);
    }
}
