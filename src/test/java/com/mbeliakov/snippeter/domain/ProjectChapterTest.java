package com.mbeliakov.snippeter.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mbeliakov.snippeter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProjectChapterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectChapter.class);
        ProjectChapter projectChapter1 = new ProjectChapter();
        projectChapter1.setId(1L);
        ProjectChapter projectChapter2 = new ProjectChapter();
        projectChapter2.setId(projectChapter1.getId());
        assertThat(projectChapter1).isEqualTo(projectChapter2);
        projectChapter2.setId(2L);
        assertThat(projectChapter1).isNotEqualTo(projectChapter2);
        projectChapter1.setId(null);
        assertThat(projectChapter1).isNotEqualTo(projectChapter2);
    }
}
