package tz.or.mkapafoundation.whistleblower.repository;

import tz.or.mkapafoundation.whistleblower.domain.Complain;
import tz.or.mkapafoundation.whistleblower.domain.ComplainStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Complain entity.
 */
@Repository
public interface ComplainRepository extends JpaRepository<Complain, Long>, JpaSpecificationExecutor<Complain> {

    @Query(value = "select distinct complain from Complain complain left join fetch complain.receivers",
        countQuery = "select count(distinct complain) from Complain complain")
    Page<Complain> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct complain from Complain complain left join fetch complain.receivers")
    List<Complain> findAllWithEagerRelationships();

    @Query("select complain from Complain complain left join fetch complain.receivers where complain.id =:id")
    Optional<Complain> findOneWithEagerRelationships(@Param("id") Long id);

    Optional<Complain> findFirstByControlNumber(String controlNumber);

    @Modifying
    @Query("UPDATE Complain co SET co.status=:status WHERE co.id=:id")
    int updateStatus(@Param("id")Long id, @Param("status") ComplainStatus status);
}
