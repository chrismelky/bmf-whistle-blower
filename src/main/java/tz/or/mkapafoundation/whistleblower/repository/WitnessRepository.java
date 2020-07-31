package tz.or.mkapafoundation.whistleblower.repository;

import tz.or.mkapafoundation.whistleblower.domain.Witness;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Witness entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WitnessRepository extends JpaRepository<Witness, Long> {
}
