package com.mbeliakov.snippeter.repository;

import com.mbeliakov.snippeter.domain.LangType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LangType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LangTypeRepository extends JpaRepository<LangType, Long> {}
