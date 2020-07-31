package tz.or.mkapafoundation.whistleblower.service;

import tz.or.mkapafoundation.whistleblower.domain.Witness;
import tz.or.mkapafoundation.whistleblower.repository.WitnessRepository;
import tz.or.mkapafoundation.whistleblower.service.dto.WitnessDTO;
import tz.or.mkapafoundation.whistleblower.service.mapper.WitnessMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Witness}.
 */
@Service
@Transactional
public class WitnessService {

    private final Logger log = LoggerFactory.getLogger(WitnessService.class);

    private final WitnessRepository witnessRepository;

    private final WitnessMapper witnessMapper;

    public WitnessService(WitnessRepository witnessRepository, WitnessMapper witnessMapper) {
        this.witnessRepository = witnessRepository;
        this.witnessMapper = witnessMapper;
    }

    /**
     * Save a witness.
     *
     * @param witnessDTO the entity to save.
     * @return the persisted entity.
     */
    public WitnessDTO save(WitnessDTO witnessDTO) {
        log.debug("Request to save Witness : {}", witnessDTO);
        Witness witness = witnessMapper.toEntity(witnessDTO);
        witness = witnessRepository.save(witness);
        return witnessMapper.toDto(witness);
    }

    /**
     * Get all the witnesses.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WitnessDTO> findAll() {
        log.debug("Request to get all Witnesses");
        return witnessRepository.findAll().stream()
            .map(witnessMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one witness by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WitnessDTO> findOne(Long id) {
        log.debug("Request to get Witness : {}", id);
        return witnessRepository.findById(id)
            .map(witnessMapper::toDto);
    }

    /**
     * Delete the witness by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Witness : {}", id);
        witnessRepository.deleteById(id);
    }
}
