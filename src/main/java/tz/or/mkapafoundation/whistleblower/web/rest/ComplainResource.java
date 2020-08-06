package tz.or.mkapafoundation.whistleblower.web.rest;

import tz.or.mkapafoundation.whistleblower.service.ComplainService;
import tz.or.mkapafoundation.whistleblower.service.NotificationService;
import tz.or.mkapafoundation.whistleblower.web.rest.errors.BadRequestAlertException;
import tz.or.mkapafoundation.whistleblower.service.dto.ComplainDTO;
import tz.or.mkapafoundation.whistleblower.service.dto.ComplainCriteria;
import tz.or.mkapafoundation.whistleblower.repository.ComplainRepository;
import tz.or.mkapafoundation.whistleblower.repository.UserRepository;
import tz.or.mkapafoundation.whistleblower.service.ComplainQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link tz.or.mkapafoundation.whistleblower.domain.Complain}.
 */
@RestController
@RequestMapping("/api")
public class ComplainResource {

    private final Logger log = LoggerFactory.getLogger(ComplainResource.class);

    private static final String ENTITY_NAME = "complain";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComplainService complainService;

    private final NotificationService notificationService;

    private final ComplainRepository complainRepository;


    private final ComplainQueryService complainQueryService;

    public ComplainResource(final ComplainRepository complainRepository,ComplainService complainService, ComplainQueryService complainQueryService,
    final UserRepository userRepository, NotificationService notificationService) {
        this.complainService = complainService;
        this.complainQueryService = complainQueryService;
        this.notificationService = notificationService;
        this.complainRepository = complainRepository;

    }

    /**
     * {@code POST  /complains} : Create a new complain.
     *
     * @param complainDTO the complainDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new complainDTO, or with status {@code 400 (Bad Request)} if the complain has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/complains")
    public ResponseEntity<ComplainDTO> createComplain(@Valid @RequestBody ComplainDTO complainDTO) throws URISyntaxException {
        log.debug("REST request to save Complain : {}", complainDTO);
        if (complainDTO.getId() != null) {
            throw new BadRequestAlertException("A new complain cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ComplainDTO result = complainService.save(complainDTO);
        notificationService.createFromComplain(result);

        return ResponseEntity.created(new URI("/api/complains/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /complains} : Updates an existing complain.
     *
     * @param complainDTO the complainDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated complainDTO,
     * or with status {@code 400 (Bad Request)} if the complainDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the complainDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/complains")
    public ResponseEntity<ComplainDTO> updateComplain(@Valid @RequestBody ComplainDTO complainDTO) throws URISyntaxException {
        log.debug("REST request to update Complain : {}", complainDTO);
        if (complainDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ComplainDTO result = complainService.save(complainDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, complainDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /complains} : get all the complains.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of complains in body.
     */
    @GetMapping("/complains")
    public ResponseEntity<List<ComplainDTO>> getAllComplains(ComplainCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Complains by criteria: {}", criteria);
        Page<ComplainDTO> page = complainQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /complains/count} : count all the complains.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/complains/count")
    public ResponseEntity<Long> countComplains(ComplainCriteria criteria) {
        log.debug("REST request to count Complains by criteria: {}", criteria);
        return ResponseEntity.ok().body(complainQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /complains/:id} : get the "id" complain.
     *
     * @param id the id of the complainDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the complainDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/complains/{id}")
    public ResponseEntity<ComplainDTO> getComplain(@PathVariable Long id) {
        log.debug("REST request to get Complain : {}", id);
        Optional<ComplainDTO> complainDTO = complainService.findOne(id);
        return ResponseUtil.wrapOrNotFound(complainDTO);
    }

    /**
     * {@code DELETE  /complains/:id} : delete the "id" complain.
     *
     * @param id the id of the complainDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/complains/{id}")
    public ResponseEntity<Void> deleteComplain(@PathVariable Long id) {
        log.debug("REST request to delete Complain : {}", id);
        complainService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
