package tz.or.mkapafoundation.whistleblower.web.rest;

import tz.or.mkapafoundation.whistleblower.service.SuspectService;
import tz.or.mkapafoundation.whistleblower.web.rest.errors.BadRequestAlertException;
import tz.or.mkapafoundation.whistleblower.service.dto.SuspectDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link tz.or.mkapafoundation.whistleblower.domain.Suspect}.
 */
@RestController
@RequestMapping("/api")
public class SuspectResource {

    private final Logger log = LoggerFactory.getLogger(SuspectResource.class);

    private static final String ENTITY_NAME = "suspect";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SuspectService suspectService;

    public SuspectResource(SuspectService suspectService) {
        this.suspectService = suspectService;
    }

    /**
     * {@code POST  /suspects} : Create a new suspect.
     *
     * @param suspectDTO the suspectDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new suspectDTO, or with status {@code 400 (Bad Request)} if the suspect has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/suspects")
    public ResponseEntity<SuspectDTO> createSuspect(@Valid @RequestBody SuspectDTO suspectDTO) throws URISyntaxException {
        log.debug("REST request to save Suspect : {}", suspectDTO);
        if (suspectDTO.getId() != null) {
            throw new BadRequestAlertException("A new suspect cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SuspectDTO result = suspectService.save(suspectDTO);
        return ResponseEntity.created(new URI("/api/suspects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /suspects} : Updates an existing suspect.
     *
     * @param suspectDTO the suspectDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated suspectDTO,
     * or with status {@code 400 (Bad Request)} if the suspectDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the suspectDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/suspects")
    public ResponseEntity<SuspectDTO> updateSuspect(@Valid @RequestBody SuspectDTO suspectDTO) throws URISyntaxException {
        log.debug("REST request to update Suspect : {}", suspectDTO);
        if (suspectDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SuspectDTO result = suspectService.save(suspectDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, suspectDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /suspects} : get all the suspects.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of suspects in body.
     */
    @GetMapping("/suspects")
    public List<SuspectDTO> getAllSuspects() {
        log.debug("REST request to get all Suspects");
        return suspectService.findAll();
    }

    /**
     * {@code GET  /suspects/:id} : get the "id" suspect.
     *
     * @param id the id of the suspectDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the suspectDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/suspects/{id}")
    public ResponseEntity<SuspectDTO> getSuspect(@PathVariable Long id) {
        log.debug("REST request to get Suspect : {}", id);
        Optional<SuspectDTO> suspectDTO = suspectService.findOne(id);
        return ResponseUtil.wrapOrNotFound(suspectDTO);
    }

    /**
     * {@code DELETE  /suspects/:id} : delete the "id" suspect.
     *
     * @param id the id of the suspectDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/suspects/{id}")
    public ResponseEntity<Void> deleteSuspect(@PathVariable Long id) {
        log.debug("REST request to delete Suspect : {}", id);
        suspectService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
