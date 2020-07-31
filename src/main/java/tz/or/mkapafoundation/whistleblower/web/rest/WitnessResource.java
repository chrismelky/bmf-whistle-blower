package tz.or.mkapafoundation.whistleblower.web.rest;

import tz.or.mkapafoundation.whistleblower.service.WitnessService;
import tz.or.mkapafoundation.whistleblower.web.rest.errors.BadRequestAlertException;
import tz.or.mkapafoundation.whistleblower.service.dto.WitnessDTO;

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
 * REST controller for managing {@link tz.or.mkapafoundation.whistleblower.domain.Witness}.
 */
@RestController
@RequestMapping("/api")
public class WitnessResource {

    private final Logger log = LoggerFactory.getLogger(WitnessResource.class);

    private static final String ENTITY_NAME = "witness";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WitnessService witnessService;

    public WitnessResource(WitnessService witnessService) {
        this.witnessService = witnessService;
    }

    /**
     * {@code POST  /witnesses} : Create a new witness.
     *
     * @param witnessDTO the witnessDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new witnessDTO, or with status {@code 400 (Bad Request)} if the witness has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/witnesses")
    public ResponseEntity<WitnessDTO> createWitness(@Valid @RequestBody WitnessDTO witnessDTO) throws URISyntaxException {
        log.debug("REST request to save Witness : {}", witnessDTO);
        if (witnessDTO.getId() != null) {
            throw new BadRequestAlertException("A new witness cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WitnessDTO result = witnessService.save(witnessDTO);
        return ResponseEntity.created(new URI("/api/witnesses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /witnesses} : Updates an existing witness.
     *
     * @param witnessDTO the witnessDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated witnessDTO,
     * or with status {@code 400 (Bad Request)} if the witnessDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the witnessDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/witnesses")
    public ResponseEntity<WitnessDTO> updateWitness(@Valid @RequestBody WitnessDTO witnessDTO) throws URISyntaxException {
        log.debug("REST request to update Witness : {}", witnessDTO);
        if (witnessDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WitnessDTO result = witnessService.save(witnessDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, witnessDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /witnesses} : get all the witnesses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of witnesses in body.
     */
    @GetMapping("/witnesses")
    public List<WitnessDTO> getAllWitnesses() {
        log.debug("REST request to get all Witnesses");
        return witnessService.findAll();
    }

    /**
     * {@code GET  /witnesses/:id} : get the "id" witness.
     *
     * @param id the id of the witnessDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the witnessDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/witnesses/{id}")
    public ResponseEntity<WitnessDTO> getWitness(@PathVariable Long id) {
        log.debug("REST request to get Witness : {}", id);
        Optional<WitnessDTO> witnessDTO = witnessService.findOne(id);
        return ResponseUtil.wrapOrNotFound(witnessDTO);
    }

    /**
     * {@code DELETE  /witnesses/:id} : delete the "id" witness.
     *
     * @param id the id of the witnessDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/witnesses/{id}")
    public ResponseEntity<Void> deleteWitness(@PathVariable Long id) {
        log.debug("REST request to delete Witness : {}", id);
        witnessService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
