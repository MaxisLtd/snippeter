package com.mbeliakov.snippeter.repository;

import com.mbeliakov.snippeter.domain.Snippet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Snippet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SnippetRepository extends JpaRepository<Snippet, Long> {}
