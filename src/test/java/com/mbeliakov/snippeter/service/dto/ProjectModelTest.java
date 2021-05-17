package com.mbeliakov.snippeter.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mbeliakov.snippeter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProjectModelTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectModel.class);
        ProjectModel projectModel1 = new ProjectModel();
        projectModel1.setId(1L);
        ProjectModel projectModel2 = new ProjectModel();
        assertThat(projectModel1).isNotEqualTo(projectModel2);
        projectModel2.setId(projectModel1.getId());
        assertThat(projectModel1).isEqualTo(projectModel2);
        projectModel2.setId(2L);
        assertThat(projectModel1).isNotEqualTo(projectModel2);
        projectModel1.setId(null);
        assertThat(projectModel1).isNotEqualTo(projectModel2);
    }
}
