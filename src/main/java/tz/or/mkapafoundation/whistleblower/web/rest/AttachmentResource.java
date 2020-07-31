package tz.or.mkapafoundation.whistleblower.web.rest;

import tz.or.mkapafoundation.whistleblower.domain.Attachment;
import tz.or.mkapafoundation.whistleblower.repository.AttachmentRepository;
import tz.or.mkapafoundation.whistleblower.service.AttachmentService;
import tz.or.mkapafoundation.whistleblower.service.AttachmentStore;
import tz.or.mkapafoundation.whistleblower.web.rest.errors.BadRequestAlertException;
import tz.or.mkapafoundation.whistleblower.service.dto.AttachmentDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing
 * {@link tz.or.mkapafoundation.whistleblower.domain.Attachment}.
 */
@RestController
@RequestMapping("/api")
public class AttachmentResource {

    private final Logger log = LoggerFactory.getLogger(AttachmentResource.class);

    private static final String ENTITY_NAME = "attachment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttachmentService attachmentService;
    private final AttachmentRepository attachmentRepository;
    private final AttachmentStore attachmentStore;

    public AttachmentResource(
        AttachmentService attachmentService,  
        AttachmentStore attachmentStore, 
        AttachmentRepository attachmentRepository) {
        this.attachmentService = attachmentService;
        this.attachmentStore = attachmentStore;
        this.attachmentRepository = attachmentRepository;
    }

    @PostMapping("/attachments/upload")
    public ResponseEntity<Attachment> upload(@RequestParam String name,
            @RequestParam("file") final MultipartFile file) throws IOException {
        final Attachment attachment = new Attachment();
        attachment.setName(name);
        attachment.setMimeType(file.getContentType());
        attachmentStore.setContent(attachment, file.getInputStream());
        attachmentRepository.save(attachment);
        return ResponseEntity.ok(attachment);
    }

    @GetMapping("attachments/download/{contentId}")
    public ResponseEntity<?> getContent(@PathVariable("contentId") String contentId, HttpServletResponse response) throws IOException {
        Optional<Attachment> f = attachmentRepository.findByContentId(contentId);
        if (f.isPresent()) {
			InputStreamResource inputStreamResource = new InputStreamResource(attachmentStore.getContent(f.get()));
			HttpHeaders headers = new HttpHeaders();
			headers.setContentLength(f.get().getContentLength());
			headers.set("Content-Type", f.get().getMimeType());
			return new ResponseEntity<Object>(inputStreamResource, headers, HttpStatus.OK);
		}
		return null;
    }

    /**
     * {@code POST  /attachments} : Create a new attachment.
     *
     * @param attachmentDTO the attachmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new attachmentDTO, or with status {@code 400 (Bad Request)}
     *         if the attachment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/attachments")
    public ResponseEntity<AttachmentDTO> createAttachment(@Valid @RequestBody final AttachmentDTO attachmentDTO)
            throws URISyntaxException {
        log.debug("REST request to save Attachment : {}", attachmentDTO);
        if (attachmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new attachment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        final AttachmentDTO result = attachmentService.save(attachmentDTO);
        return ResponseEntity
                .created(new URI("/api/attachments/" + result.getId())).headers(HeaderUtil
                        .createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /attachments} : Updates an existing attachment.
     *
     * @param attachmentDTO the attachmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated attachmentDTO, or with status {@code 400 (Bad Request)}
     *         if the attachmentDTO is not valid, or with status
     *         {@code 500 (Internal Server Error)} if the attachmentDTO couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/attachments")
    public ResponseEntity<AttachmentDTO> updateAttachment(@Valid @RequestBody final AttachmentDTO attachmentDTO)
            throws URISyntaxException {
        log.debug("REST request to update Attachment : {}", attachmentDTO);
        if (attachmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        final AttachmentDTO result = attachmentService.save(attachmentDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                attachmentDTO.getId().toString())).body(result);
    }

    /**
     * {@code GET  /attachments} : get all the attachments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of attachments in body.
     */
    @GetMapping("/attachments")
    public List<AttachmentDTO> getAllAttachments() {
        log.debug("REST request to get all Attachments");
        return attachmentService.findAll();
    }

    /**
     * {@code GET  /attachments/:id} : get the "id" attachment.
     *
     * @param id the id of the attachmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the attachmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/attachments/{id}")
    public ResponseEntity<AttachmentDTO> getAttachment(@PathVariable final Long id) {
        log.debug("REST request to get Attachment : {}", id);
        final Optional<AttachmentDTO> attachmentDTO = attachmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(attachmentDTO);
    }

    /**
     * {@code DELETE  /attachments/:id} : delete the "id" attachment.
     *
     * @param id the id of the attachmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/attachments/{id}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable final Long id) {
        log.debug("REST request to delete Attachment : {}", id);
        attachmentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
