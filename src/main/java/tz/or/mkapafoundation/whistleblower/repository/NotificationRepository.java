package tz.or.mkapafoundation.whistleblower.repository;

import tz.or.mkapafoundation.whistleblower.domain.Notification;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Notification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("select notification from Notification notification where notification.user.login = ?#{principal.preferredUsername}")
    List<Notification> findByUserIsCurrentUser();

    void deleteByComplain_Id(Long id);
}
