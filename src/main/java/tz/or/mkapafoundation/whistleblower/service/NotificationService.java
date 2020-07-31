package tz.or.mkapafoundation.whistleblower.service;

import tz.or.mkapafoundation.whistleblower.domain.Complain;
import tz.or.mkapafoundation.whistleblower.domain.Notification;
import tz.or.mkapafoundation.whistleblower.domain.User;
import tz.or.mkapafoundation.whistleblower.repository.NotificationRepository;
import tz.or.mkapafoundation.whistleblower.service.dto.NotificationDTO;
import tz.or.mkapafoundation.whistleblower.service.mapper.NotificationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing {@link Notification}.
 */
@Service
@Transactional
public class NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    private final MailService mailService;

    public NotificationService(
        NotificationRepository notificationRepository, NotificationMapper notificationMapper,MailService mailService) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.mailService = mailService;
    }

    /**
     * Save a notification.
     *
     * @param notificationDTO the entity to save.
     * @return the persisted entity.
     */
    public NotificationDTO save(NotificationDTO notificationDTO) {
        log.debug("Request to save Notification : {}", notificationDTO);
        Notification notification = notificationMapper.toEntity(notificationDTO);
        notification = notificationRepository.save(notification);
        return notificationMapper.toDto(notification);
    }

    /**
     * Get all the notifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NotificationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Notifications");
        return notificationRepository.findAll(pageable)
            .map(notificationMapper::toDto);
    }


    /**
     * Get one notification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NotificationDTO> findOne(Long id) {
        log.debug("Request to get Notification : {}", id);
        return notificationRepository.findById(id)
            .map(notificationMapper::toDto);
    }

    /**
     * Delete the notification by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Notification : {}", id);
        notificationRepository.deleteById(id);
    }

    @Async
    public void createFromComplain(Complain complain) {
        Set<User> receivers = complain.getReceivers();
        for (User user : receivers) {
            Notification notification = new Notification(complain, user);
            notificationRepository.save(notification);
            mailService.sendNotificationEmail(notification);
        }
    }

	public void deleteByComplain(Long id) {
        notificationRepository.deleteByComplain_Id(id);
	}

}
