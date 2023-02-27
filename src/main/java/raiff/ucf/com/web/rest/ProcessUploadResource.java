package raiff.ucf.com.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import raiff.ucf.com.domain.ProcessUpload;
import raiff.ucf.com.repository.ProcessUploadRepository;
import raiff.ucf.com.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link raiff.ucf.com.domain.ProcessUpload}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProcessUploadResource {

    private final Logger log = LoggerFactory.getLogger(ProcessUploadResource.class);

    private static final String ENTITY_NAME = "ucfProcessUpload";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcessUploadRepository processUploadRepository;

    public ProcessUploadResource(ProcessUploadRepository processUploadRepository) {
        this.processUploadRepository = processUploadRepository;
    }

    /**
     * {@code POST  /process-uploads} : Create a new processUpload.
     *
     * @param processUpload the processUpload to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new processUpload, or with status {@code 400 (Bad Request)} if the processUpload has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/process-uploads")
    public ResponseEntity<ProcessUpload> createProcessUpload(@RequestBody ProcessUpload processUpload) throws URISyntaxException {
        log.debug("REST request to save ProcessUpload : {}", processUpload);
        if (processUpload.getId() != null) {
            throw new BadRequestAlertException("A new processUpload cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProcessUpload result = processUploadRepository.save(processUpload);
        return ResponseEntity
            .created(new URI("/api/process-uploads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /process-uploads/:id} : Updates an existing processUpload.
     *
     * @param id the id of the processUpload to save.
     * @param processUpload the processUpload to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processUpload,
     * or with status {@code 400 (Bad Request)} if the processUpload is not valid,
     * or with status {@code 500 (Internal Server Error)} if the processUpload couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/process-uploads/{id}")
    public ResponseEntity<ProcessUpload> updateProcessUpload(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProcessUpload processUpload
    ) throws URISyntaxException {
        log.debug("REST request to update ProcessUpload : {}, {}", id, processUpload);
        if (processUpload.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processUpload.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processUploadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProcessUpload result = processUploadRepository.save(processUpload);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, processUpload.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /process-uploads/:id} : Partial updates given fields of an existing processUpload, field will ignore if it is null
     *
     * @param id the id of the processUpload to save.
     * @param processUpload the processUpload to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processUpload,
     * or with status {@code 400 (Bad Request)} if the processUpload is not valid,
     * or with status {@code 404 (Not Found)} if the processUpload is not found,
     * or with status {@code 500 (Internal Server Error)} if the processUpload couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/process-uploads/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProcessUpload> partialUpdateProcessUpload(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProcessUpload processUpload
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProcessUpload partially : {}, {}", id, processUpload);
        if (processUpload.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processUpload.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processUploadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProcessUpload> result = processUploadRepository
            .findById(processUpload.getId())
            .map(existingProcessUpload -> {
                if (processUpload.getProcessState() != null) {
                    existingProcessUpload.setProcessState(processUpload.getProcessState());
                }
                if (processUpload.getCustomerId() != null) {
                    existingProcessUpload.setCustomerId(processUpload.getCustomerId());
                }
                if (processUpload.getCreationDate() != null) {
                    existingProcessUpload.setCreationDate(processUpload.getCreationDate());
                }
                if (processUpload.getLastUpdateDate() != null) {
                    existingProcessUpload.setLastUpdateDate(processUpload.getLastUpdateDate());
                }
                if (processUpload.getReceptionDate() != null) {
                    existingProcessUpload.setReceptionDate(processUpload.getReceptionDate());
                }
                if (processUpload.getNotes() != null) {
                    existingProcessUpload.setNotes(processUpload.getNotes());
                }

                return existingProcessUpload;
            })
            .map(processUploadRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, processUpload.getId().toString())
        );
    }

    /**
     * {@code GET  /process-uploads} : get all the processUploads.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of processUploads in body.
     */
    @GetMapping("/process-uploads")
    public ResponseEntity<List<ProcessUpload>> getAllProcessUploads(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("processmessagingrnet-is-null".equals(filter)) {
            log.debug("REST request to get all ProcessUploads where processMessagingRnet is null");
            return new ResponseEntity<>(
                StreamSupport
                    .stream(processUploadRepository.findAll().spliterator(), false)
                    .filter(processUpload -> processUpload.getProcessMessagingRnet() == null)
                    .collect(Collectors.toList()),
                HttpStatus.OK
            );
        }

        if ("processidentitydocument-is-null".equals(filter)) {
            log.debug("REST request to get all ProcessUploads where processIdentityDocument is null");
            return new ResponseEntity<>(
                StreamSupport
                    .stream(processUploadRepository.findAll().spliterator(), false)
                    .filter(processUpload -> processUpload.getProcessIdentityDocument() == null)
                    .collect(Collectors.toList()),
                HttpStatus.OK
            );
        }

        if ("processinvoicehomeloan-is-null".equals(filter)) {
            log.debug("REST request to get all ProcessUploads where processInvoiceHomeLoan is null");
            return new ResponseEntity<>(
                StreamSupport
                    .stream(processUploadRepository.findAll().spliterator(), false)
                    .filter(processUpload -> processUpload.getProcessInvoiceHomeLoan() == null)
                    .collect(Collectors.toList()),
                HttpStatus.OK
            );
        }

        if ("processpersonalloan-is-null".equals(filter)) {
            log.debug("REST request to get all ProcessUploads where processPersonalLoan is null");
            return new ResponseEntity<>(
                StreamSupport
                    .stream(processUploadRepository.findAll().spliterator(), false)
                    .filter(processUpload -> processUpload.getProcessPersonalLoan() == null)
                    .collect(Collectors.toList()),
                HttpStatus.OK
            );
        }
        log.debug("REST request to get a page of ProcessUploads");
        Page<ProcessUpload> page = processUploadRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /process-uploads/:id} : get the "id" processUpload.
     *
     * @param id the id of the processUpload to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the processUpload, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/process-uploads/{id}")
    public ResponseEntity<ProcessUpload> getProcessUpload(@PathVariable Long id) {
        log.debug("REST request to get ProcessUpload : {}", id);
        Optional<ProcessUpload> processUpload = processUploadRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(processUpload);
    }

    /**
     * {@code DELETE  /process-uploads/:id} : delete the "id" processUpload.
     *
     * @param id the id of the processUpload to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/process-uploads/{id}")
    public ResponseEntity<Void> deleteProcessUpload(@PathVariable Long id) {
        log.debug("REST request to delete ProcessUpload : {}", id);
        processUploadRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
