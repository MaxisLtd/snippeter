package com.mbeliakov.snippeter.repository;

import com.mbeliakov.snippeter.domain.SnippetSection;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SnippetSection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SnippetSectionRepository extends JpaRepository<SnippetSection, Long> {}
