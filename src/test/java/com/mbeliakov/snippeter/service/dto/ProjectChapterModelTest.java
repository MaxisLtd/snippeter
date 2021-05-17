package com.mbeliakov.snippeter.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mbeliakov.snippeter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProjectChapterModelTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectChapterModel.class);
        ProjectChapterModel projectChapterModel1 = new ProjectChapterModel();
        projectChapterModel1.setId(1L);
        ProjectChapterModel projectChapterModel2 = new ProjectChapterModel();
        assertThat(projectChapterModel1).isNotEqualTo(projectChapterModel2);
        projectChapterModel2.setId(projectChapterModel1.getId());
        assertThat(projectChapterModel1).isEqualTo(projectChapterModel2);
        projectChapterModel2.setId(2L);
        assertThat(projectChapterModel1).isNotEqualTo(projectChapterModel2);
        projectChapterModel1.setId(null);
        assertThat(projectChapterModel1).isNotEqualTo(projectChapterModel2);
    }
}
