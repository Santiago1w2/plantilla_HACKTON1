package org.test.week06lab01.Tropel.tropelRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.test.week06lab01.Tropel.Tropel;

public interface TropelRepository extends JpaRepository<Tropel, Long>, JpaSpecificationExecutor<Tropel> {}
