package tz.or.mkapafoundation.whistleblower.service;

import tz.or.mkapafoundation.whistleblower.domain.Complain;
import tz.or.mkapafoundation.whistleblower.domain.Notification;
import tz.or.mkapafoundation.whistleblower.domain.User;
import tz.or.mkapafoundation.whistleblower.repository.NotificationRepository;
import tz.or.mkapafoundation.whistleblower.service.dto.ComplainDTO;
import tz.or.mkapafoundation.whistleblower.service.dto.NotificationDTO;
import tz.or.mkapafoundation.whistleblower.service.dto.UserDTO;
import tz.or.mkapafoundation.whistleblower.service.mapper.ComplainMapper;
import tz.or.mkapafoundation.whistleblower.service.mapper.NotificationMapper;
import tz.or.mkapafoundation.whistleblower.service.mapper.UserMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import liquibase.pro.packaged.d;

import java.util.Optional;
import java.util.Set;

import javax.activation.CommandMap;

/**
 * Service Implementation for managing {@link Notification}.
 */
@Service
public class NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    private final UserMapper userMapper;

    private final ComplainMapper complainMapper;

    private final MailService mailService;

    public NotificationService(
            final NotificationRepository notificationRepository, 
            final NotificationMapper notificationMapper,
            UserMapper userMapper,
            ComplainMapper complainMapper,
            final MailService mailService) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.mailService = mailService;
        this.userMapper = userMapper;
        this.complainMapper = complainMapper;
    }

    /**
     * Save a notification.
     *
     * @param notificationDTO the entity to save.
     * @return the persisted entity.
     */
    public NotificationDTO save(final NotificationDTO notificationDTO) {
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
    public Page<NotificationDTO> findAll(final Pageable pageable) {
        log.debug("Request to get all Notifications");
        return notificationRepository.findAll(pageable).map(notificationMapper::toDto);
    }

    /**
     * Get one notification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NotificationDTO> findOne(final Long id) {
        log.debug("Request to get Notification : {}", id);
        return notificationRepository.findById(id).map(notificationMapper::toDto);
    }

    /**
     * Delete the notification by id.
     *
     * @param id the id of the entity.
     */
    public void delete(final Long id) {
        log.debug("Request to delete Notification : {}", id);
        notificationRepository.deleteById(id);
    }

    @Async
    public void createFromComplain(final ComplainDTO dto) {
        final Set<UserDTO> receivers = dto.getReceivers();
        for (final UserDTO user : receivers) {
            final Notification notification = new Notification(complainMapper.toEntity(dto), userMapper.userDTOToUser(user));
            notificationRepository.save(notification);
            mailService.sendNotificationEmail(notification);
        }
    }

    public void deleteByComplain(final Long id) {
        notificationRepository.deleteByComplain_Id(id);
	}

}
