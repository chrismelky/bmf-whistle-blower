package tz.or.mkapafoundation.whistleblower.service;

import tz.or.mkapafoundation.whistleblower.domain.Attachment;
import tz.or.mkapafoundation.whistleblower.domain.Complain;
import tz.or.mkapafoundation.whistleblower.domain.User;
import tz.or.mkapafoundation.whistleblower.repository.AttachmentRepository;
import tz.or.mkapafoundation.whistleblower.repository.ComplainRepository;
import tz.or.mkapafoundation.whistleblower.repository.UserRepository;
import tz.or.mkapafoundation.whistleblower.service.dto.ComplainDTO;
import tz.or.mkapafoundation.whistleblower.service.mapper.ComplainMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing {@link Complain}.
 */
@Service
@Transactional
public class ComplainService {

    private final Logger log = LoggerFactory.getLogger(ComplainService.class);

    private final ComplainRepository complainRepository;

    private final UserRepository userRepository;

    private final ComplainMapper complainMapper;

    private final AttachmentRepository attachmentRepository;

    private final NotificationService notificationService;

    public ComplainService(final ComplainRepository complainRepository, final ComplainMapper complainMapper,
            final UserRepository userRepository, AttachmentRepository attachmentRepository, NotificationService notificationService) {
        this.complainRepository = complainRepository;
        this.complainMapper = complainMapper;
        this.userRepository = userRepository;
        this.attachmentRepository = attachmentRepository;
        this.notificationService = notificationService;
    }

    /**
     * Save a complain.
     *
     * @param complainDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public ComplainDTO save(final ComplainDTO complainDTO) {
        log.debug("Request to save Complain : {}", complainDTO);
        Complain complain = complainMapper.toEntity(complainDTO);
        final Complain fComplain = complain;
        // Set complain for witness and suspect to be cascade saved with comliain
        complain.getWitnesses().forEach(w -> w.setComplain(fComplain));
        complain.getSuspects().forEach(s -> s.setComplain(fComplain));

        // Generate control number and set receiver when creating new complian
        if (complain.getId() == null) {
            complain.setControlNumber(Long.toString(Instant.now().toEpochMilli()));
        }

        Set<User> receivers = userRepository.findByAuthorities_Name("ROLE_ADMIN");
        complain.setReceivers(receivers);

        Set<Attachment> attachments = complain.getAttachments();
        complain = complainRepository.save(complain);
        // Upadte attachment to link with complain while creating
        for (final Attachment attachment : attachments) {
            if (attachment.getComplain() == null) {
                attachment.setComplain(complain);
                attachmentRepository.save(attachment);
            }
        }
        notificationService.createFromComplain(complainRepository.getOne(complain.getId()));
        return complainMapper.toDto(complain);
    }

    /**
     * Get all the complains.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ComplainDTO> findAll(final Pageable pageable) {
        log.debug("Request to get all Complains");
        return complainRepository.findAll(pageable).map(complainMapper::toDto);
    }

    /**
     * Get all the complains with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ComplainDTO> findAllWithEagerRelationships(final Pageable pageable) {
        return complainRepository.findAllWithEagerRelationships(pageable).map(complainMapper::toDto);
    }

    /**
     * Get one complain by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ComplainDTO> findOne(final Long id) {
        log.debug("Request to get Complain : {}", id);
        return complainRepository.findOneWithEagerRelationships(id).map(complainMapper::toDto);
    }

    /**
     * Delete the complain by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(final Long id) {
        log.debug("Request to delete Complain : {}", id);
        notificationService.deleteByComplain(id);
        complainRepository.deleteById(id);
    }
}
