package com.mbeliakov.snippeter.repository;

import com.mbeliakov.snippeter.domain.CommonProperty;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CommonProperty entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommonPropertyRepository extends JpaRepository<CommonProperty, Long> {}
