package com.mbeliakov.snippeter.repository;

import com.mbeliakov.snippeter.domain.ProjectChapter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProjectChapter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectChapterRepository extends JpaRepository<ProjectChapter, Long> {}
