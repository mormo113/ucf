package raiff.ucf.com.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
import raiff.ucf.com.domain.File;
import raiff.ucf.com.repository.FileRepository;
import raiff.ucf.com.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link raiff.ucf.com.domain.File}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FileResource {

    private final Logger log = LoggerFactory.getLogger(FileResource.class);

    private static final String ENTITY_NAME = "ucfFile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FileRepository fileRepository;

    public FileResource(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    /**
     * {@code POST  /files} : Create a new file.
     *
     * @param file the file to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new file, or with status {@code 400 (Bad Request)} if the file has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/files")
    public ResponseEntity<File> createFile(@RequestBody File file) throws URISyntaxException {
        log.debug("REST request to save File : {}", file);
        if (file.getId() != null) {
            throw new BadRequestAlertException("A new file cannot already have an ID", ENTITY_NAME, "idexists");
        }
        File result = fileRepository.save(file);
        return ResponseEntity
            .created(new URI("/api/files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /files/:id} : Updates an existing file.
     *
     * @param id the id of the file to save.
     * @param file the file to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated file,
     * or with status {@code 400 (Bad Request)} if the file is not valid,
     * or with status {@code 500 (Internal Server Error)} if the file couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/files/{id}")
    public ResponseEntity<File> updateFile(@PathVariable(value = "id", required = false) final Long id, @RequestBody File file)
        throws URISyntaxException {
        log.debug("REST request to update File : {}, {}", id, file);
        if (file.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, file.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        File result = fileRepository.save(file);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, file.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /files/:id} : Partial updates given fields of an existing file, field will ignore if it is null
     *
     * @param id the id of the file to save.
     * @param file the file to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated file,
     * or with status {@code 400 (Bad Request)} if the file is not valid,
     * or with status {@code 404 (Not Found)} if the file is not found,
     * or with status {@code 500 (Internal Server Error)} if the file couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/files/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<File> partialUpdateFile(@PathVariable(value = "id", required = false) final Long id, @RequestBody File file)
        throws URISyntaxException {
        log.debug("REST request to partial update File partially : {}, {}", id, file);
        if (file.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, file.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<File> result = fileRepository
            .findById(file.getId())
            .map(existingFile -> {
                if (file.getPidNumber() != null) {
                    existingFile.setPidNumber(file.getPidNumber());
                }
                if (file.getType() != null) {
                    existingFile.setType(file.getType());
                }
                if (file.getDocumentType() != null) {
                    existingFile.setDocumentType(file.getDocumentType());
                }
                if (file.getDemandeType() != null) {
                    existingFile.setDemandeType(file.getDemandeType());
                }
                if (file.getReference() != null) {
                    existingFile.setReference(file.getReference());
                }
                if (file.getMessageRnetTitle() != null) {
                    existingFile.setMessageRnetTitle(file.getMessageRnetTitle());
                }
                if (file.getCommunication() != null) {
                    existingFile.setCommunication(file.getCommunication());
                }
                if (file.getClientMessage() != null) {
                    existingFile.setClientMessage(file.getClientMessage());
                }
                if (file.getTtcAmount() != null) {
                    existingFile.setTtcAmount(file.getTtcAmount());
                }
                if (file.getBic() != null) {
                    existingFile.setBic(file.getBic());
                }
                if (file.getIban() != null) {
                    existingFile.setIban(file.getIban());
                }
                if (file.getReceptionDate() != null) {
                    existingFile.setReceptionDate(file.getReceptionDate());
                }
                if (file.getEmissionDate() != null) {
                    existingFile.setEmissionDate(file.getEmissionDate());
                }
                if (file.getExpiryDate() != null) {
                    existingFile.setExpiryDate(file.getExpiryDate());
                }
                if (file.getCreationDate() != null) {
                    existingFile.setCreationDate(file.getCreationDate());
                }
                if (file.getModificationDate() != null) {
                    existingFile.setModificationDate(file.getModificationDate());
                }
                if (file.getStoreGed() != null) {
                    existingFile.setStoreGed(file.getStoreGed());
                }

                return existingFile;
            })
            .map(fileRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, file.getId().toString())
        );
    }

    /**
     * {@code GET  /files} : get all the files.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of files in body.
     */
    @GetMapping("/files")
    public ResponseEntity<List<File>> getAllFiles(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Files");
        Page<File> page = fileRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /files/:id} : get the "id" file.
     *
     * @param id the id of the file to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the file, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/files/{id}")
    public ResponseEntity<File> getFile(@PathVariable Long id) {
        log.debug("REST request to get File : {}", id);
        Optional<File> file = fileRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(file);
    }

    /**
     * {@code DELETE  /files/:id} : delete the "id" file.
     *
     * @param id the id of the file to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/files/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        log.debug("REST request to delete File : {}", id);
        fileRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
