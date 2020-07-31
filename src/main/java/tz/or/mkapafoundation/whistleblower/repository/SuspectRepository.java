package tz.or.mkapafoundation.whistleblower.repository;

import tz.or.mkapafoundation.whistleblower.domain.Suspect;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Suspect entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SuspectRepository extends JpaRepository<Suspect, Long> {
}
