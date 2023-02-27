package raiff.ucf.com.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import raiff.ucf.com.domain.ProcessIdentityDocument;
import raiff.ucf.com.repository.ProcessIdentityDocumentRepository;
import raiff.ucf.com.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link raiff.ucf.com.domain.ProcessIdentityDocument}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProcessIdentityDocumentResource {

    private final Logger log = LoggerFactory.getLogger(ProcessIdentityDocumentResource.class);

    private static final String ENTITY_NAME = "ucfProcessIdentityDocument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcessIdentityDocumentRepository processIdentityDocumentRepository;

    public ProcessIdentityDocumentResource(ProcessIdentityDocumentRepository processIdentityDocumentRepository) {
        this.processIdentityDocumentRepository = processIdentityDocumentRepository;
    }

    /**
     * {@code POST  /process-identity-documents} : Create a new processIdentityDocument.
     *
     * @param processIdentityDocument the processIdentityDocument to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new processIdentityDocument, or with status {@code 400 (Bad Request)} if the processIdentityDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/process-identity-documents")
    public ResponseEntity<ProcessIdentityDocument> createProcessIdentityDocument(
        @RequestBody ProcessIdentityDocument processIdentityDocument
    ) throws URISyntaxException {
        log.debug("REST request to save ProcessIdentityDocument : {}", processIdentityDocument);
        if (processIdentityDocument.getId() != null) {
            throw new BadRequestAlertException("A new processIdentityDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProcessIdentityDocument result = processIdentityDocumentRepository.save(processIdentityDocument);
        return ResponseEntity
            .created(new URI("/api/process-identity-documents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /process-identity-documents/:id} : Updates an existing processIdentityDocument.
     *
     * @param id the id of the processIdentityDocument to save.
     * @param processIdentityDocument the processIdentityDocument to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processIdentityDocument,
     * or with status {@code 400 (Bad Request)} if the processIdentityDocument is not valid,
     * or with status {@code 500 (Internal Server Error)} if the processIdentityDocument couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/process-identity-documents/{id}")
    public ResponseEntity<ProcessIdentityDocument> updateProcessIdentityDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProcessIdentityDocument processIdentityDocument
    ) throws URISyntaxException {
        log.debug("REST request to update ProcessIdentityDocument : {}, {}", id, processIdentityDocument);
        if (processIdentityDocument.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processIdentityDocument.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processIdentityDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProcessIdentityDocument result = processIdentityDocumentRepository.save(processIdentityDocument);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, processIdentityDocument.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /process-identity-documents/:id} : Partial updates given fields of an existing processIdentityDocument, field will ignore if it is null
     *
     * @param id the id of the processIdentityDocument to save.
     * @param processIdentityDocument the processIdentityDocument to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processIdentityDocument,
     * or with status {@code 400 (Bad Request)} if the processIdentityDocument is not valid,
     * or with status {@code 404 (Not Found)} if the processIdentityDocument is not found,
     * or with status {@code 500 (Internal Server Error)} if the processIdentityDocument couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/process-identity-documents/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProcessIdentityDocument> partialUpdateProcessIdentityDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProcessIdentityDocument processIdentityDocument
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProcessIdentityDocument partially : {}, {}", id, processIdentityDocument);
        if (processIdentityDocument.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processIdentityDocument.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processIdentityDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProcessIdentityDocument> result = processIdentityDocumentRepository
            .findById(processIdentityDocument.getId())
            .map(existingProcessIdentityDocument -> {
                if (processIdentityDocument.getLastNamePid() != null) {
                    existingProcessIdentityDocument.setLastNamePid(processIdentityDocument.getLastNamePid());
                }
                if (processIdentityDocument.getFirstNamePid() != null) {
                    existingProcessIdentityDocument.setFirstNamePid(processIdentityDocument.getFirstNamePid());
                }
                if (processIdentityDocument.getNationalityPid() != null) {
                    existingProcessIdentityDocument.setNationalityPid(processIdentityDocument.getNationalityPid());
                }
                if (processIdentityDocument.getNumberPid() != null) {
                    existingProcessIdentityDocument.setNumberPid(processIdentityDocument.getNumberPid());
                }
                if (processIdentityDocument.getExpirationDatePid() != null) {
                    existingProcessIdentityDocument.setExpirationDatePid(processIdentityDocument.getExpirationDatePid());
                }
                if (processIdentityDocument.getIssueDatePid() != null) {
                    existingProcessIdentityDocument.setIssueDatePid(processIdentityDocument.getIssueDatePid());
                }

                return existingProcessIdentityDocument;
            })
            .map(processIdentityDocumentRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, processIdentityDocument.getId().toString())
        );
    }

    /**
     * {@code GET  /process-identity-documents} : get all the processIdentityDocuments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of processIdentityDocuments in body.
     */
    @GetMapping("/process-identity-documents")
    public List<ProcessIdentityDocument> getAllProcessIdentityDocuments() {
        log.debug("REST request to get all ProcessIdentityDocuments");
        return processIdentityDocumentRepository.findAll();
    }

    /**
     * {@code GET  /process-identity-documents/:id} : get the "id" processIdentityDocument.
     *
     * @param id the id of the processIdentityDocument to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the processIdentityDocument, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/process-identity-documents/{id}")
    public ResponseEntity<ProcessIdentityDocument> getProcessIdentityDocument(@PathVariable Long id) {
        log.debug("REST request to get ProcessIdentityDocument : {}", id);
        Optional<ProcessIdentityDocument> processIdentityDocument = processIdentityDocumentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(processIdentityDocument);
    }

    /**
     * {@code DELETE  /process-identity-documents/:id} : delete the "id" processIdentityDocument.
     *
     * @param id the id of the processIdentityDocument to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/process-identity-documents/{id}")
    public ResponseEntity<Void> deleteProcessIdentityDocument(@PathVariable Long id) {
        log.debug("REST request to delete ProcessIdentityDocument : {}", id);
        processIdentityDocumentRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
