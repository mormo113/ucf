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
import raiff.ucf.com.domain.AttachedFile;
import raiff.ucf.com.repository.AttachedFileRepository;
import raiff.ucf.com.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link raiff.ucf.com.domain.AttachedFile}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AttachedFileResource {

    private final Logger log = LoggerFactory.getLogger(AttachedFileResource.class);

    private static final String ENTITY_NAME = "ucfAttachedFile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttachedFileRepository attachedFileRepository;

    public AttachedFileResource(AttachedFileRepository attachedFileRepository) {
        this.attachedFileRepository = attachedFileRepository;
    }

    /**
     * {@code POST  /attached-files} : Create a new attachedFile.
     *
     * @param attachedFile the attachedFile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attachedFile, or with status {@code 400 (Bad Request)} if the attachedFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/attached-files")
    public ResponseEntity<AttachedFile> createAttachedFile(@RequestBody AttachedFile attachedFile) throws URISyntaxException {
        log.debug("REST request to save AttachedFile : {}", attachedFile);
        if (attachedFile.getId() != null) {
            throw new BadRequestAlertException("A new attachedFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AttachedFile result = attachedFileRepository.save(attachedFile);
        return ResponseEntity
            .created(new URI("/api/attached-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /attached-files/:id} : Updates an existing attachedFile.
     *
     * @param id the id of the attachedFile to save.
     * @param attachedFile the attachedFile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attachedFile,
     * or with status {@code 400 (Bad Request)} if the attachedFile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attachedFile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/attached-files/{id}")
    public ResponseEntity<AttachedFile> updateAttachedFile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AttachedFile attachedFile
    ) throws URISyntaxException {
        log.debug("REST request to update AttachedFile : {}, {}", id, attachedFile);
        if (attachedFile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attachedFile.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attachedFileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AttachedFile result = attachedFileRepository.save(attachedFile);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, attachedFile.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /attached-files/:id} : Partial updates given fields of an existing attachedFile, field will ignore if it is null
     *
     * @param id the id of the attachedFile to save.
     * @param attachedFile the attachedFile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attachedFile,
     * or with status {@code 400 (Bad Request)} if the attachedFile is not valid,
     * or with status {@code 404 (Not Found)} if the attachedFile is not found,
     * or with status {@code 500 (Internal Server Error)} if the attachedFile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/attached-files/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AttachedFile> partialUpdateAttachedFile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AttachedFile attachedFile
    ) throws URISyntaxException {
        log.debug("REST request to partial update AttachedFile partially : {}, {}", id, attachedFile);
        if (attachedFile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attachedFile.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attachedFileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AttachedFile> result = attachedFileRepository
            .findById(attachedFile.getId())
            .map(existingAttachedFile -> {
                if (attachedFile.getFileName() != null) {
                    existingAttachedFile.setFileName(attachedFile.getFileName());
                }
                if (attachedFile.getFilePath() != null) {
                    existingAttachedFile.setFilePath(attachedFile.getFilePath());
                }
                if (attachedFile.getDocumentType() != null) {
                    existingAttachedFile.setDocumentType(attachedFile.getDocumentType());
                }
                if (attachedFile.getOcrRaw() != null) {
                    existingAttachedFile.setOcrRaw(attachedFile.getOcrRaw());
                }
                if (attachedFile.getStoreGed() != null) {
                    existingAttachedFile.setStoreGed(attachedFile.getStoreGed());
                }

                return existingAttachedFile;
            })
            .map(attachedFileRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, attachedFile.getId().toString())
        );
    }

    /**
     * {@code GET  /attached-files} : get all the attachedFiles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attachedFiles in body.
     */
    @GetMapping("/attached-files")
    public List<AttachedFile> getAllAttachedFiles() {
        log.debug("REST request to get all AttachedFiles");
        return attachedFileRepository.findAll();
    }

    /**
     * {@code GET  /attached-files/:id} : get the "id" attachedFile.
     *
     * @param id the id of the attachedFile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attachedFile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/attached-files/{id}")
    public ResponseEntity<AttachedFile> getAttachedFile(@PathVariable Long id) {
        log.debug("REST request to get AttachedFile : {}", id);
        Optional<AttachedFile> attachedFile = attachedFileRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(attachedFile);
    }

    /**
     * {@code DELETE  /attached-files/:id} : delete the "id" attachedFile.
     *
     * @param id the id of the attachedFile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/attached-files/{id}")
    public ResponseEntity<Void> deleteAttachedFile(@PathVariable Long id) {
        log.debug("REST request to delete AttachedFile : {}", id);
        attachedFileRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
