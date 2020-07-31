package tz.or.mkapafoundation.whistleblower.service;

import tz.or.mkapafoundation.whistleblower.domain.Suspect;
import tz.or.mkapafoundation.whistleblower.repository.SuspectRepository;
import tz.or.mkapafoundation.whistleblower.service.dto.SuspectDTO;
import tz.or.mkapafoundation.whistleblower.service.mapper.SuspectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Suspect}.
 */
@Service
@Transactional
public class SuspectService {

    private final Logger log = LoggerFactory.getLogger(SuspectService.class);

    private final SuspectRepository suspectRepository;

    private final SuspectMapper suspectMapper;

    public SuspectService(SuspectRepository suspectRepository, SuspectMapper suspectMapper) {
        this.suspectRepository = suspectRepository;
        this.suspectMapper = suspectMapper;
    }

    /**
     * Save a suspect.
     *
     * @param suspectDTO the entity to save.
     * @return the persisted entity.
     */
    public SuspectDTO save(SuspectDTO suspectDTO) {
        log.debug("Request to save Suspect : {}", suspectDTO);
        Suspect suspect = suspectMapper.toEntity(suspectDTO);
        suspect = suspectRepository.save(suspect);
        return suspectMapper.toDto(suspect);
    }

    /**
     * Get all the suspects.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SuspectDTO> findAll() {
        log.debug("Request to get all Suspects");
        return suspectRepository.findAll().stream()
            .map(suspectMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one suspect by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SuspectDTO> findOne(Long id) {
        log.debug("Request to get Suspect : {}", id);
        return suspectRepository.findById(id)
            .map(suspectMapper::toDto);
    }

    /**
     * Delete the suspect by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Suspect : {}", id);
        suspectRepository.deleteById(id);
    }
}
