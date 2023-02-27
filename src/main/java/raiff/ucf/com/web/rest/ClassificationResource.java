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
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import raiff.ucf.com.domain.Classification;
import raiff.ucf.com.repository.ClassificationRepository;
import raiff.ucf.com.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link raiff.ucf.com.domain.Classification}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ClassificationResource {

    private final Logger log = LoggerFactory.getLogger(ClassificationResource.class);

    private static final String ENTITY_NAME = "ucfClassification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassificationRepository classificationRepository;

    public ClassificationResource(ClassificationRepository classificationRepository) {
        this.classificationRepository = classificationRepository;
    }

    /**
     * {@code POST  /classifications} : Create a new classification.
     *
     * @param classification the classification to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new classification, or with status {@code 400 (Bad Request)} if the classification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/classifications")
    public ResponseEntity<Classification> createClassification(@RequestBody Classification classification) throws URISyntaxException {
        log.debug("REST request to save Classification : {}", classification);
        if (classification.getId() != null) {
            throw new BadRequestAlertException("A new classification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Classification result = classificationRepository.save(classification);
        return ResponseEntity
            .created(new URI("/api/classifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /classifications/:id} : Updates an existing classification.
     *
     * @param id the id of the classification to save.
     * @param classification the classification to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classification,
     * or with status {@code 400 (Bad Request)} if the classification is not valid,
     * or with status {@code 500 (Internal Server Error)} if the classification couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/classifications/{id}")
    public ResponseEntity<Classification> updateClassification(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Classification classification
    ) throws URISyntaxException {
        log.debug("REST request to update Classification : {}, {}", id, classification);
        if (classification.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classification.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!classificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Classification result = classificationRepository.save(classification);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, classification.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /classifications/:id} : Partial updates given fields of an existing classification, field will ignore if it is null
     *
     * @param id the id of the classification to save.
     * @param classification the classification to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classification,
     * or with status {@code 400 (Bad Request)} if the classification is not valid,
     * or with status {@code 404 (Not Found)} if the classification is not found,
     * or with status {@code 500 (Internal Server Error)} if the classification couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/classifications/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Classification> partialUpdateClassification(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Classification classification
    ) throws URISyntaxException {
        log.debug("REST request to partial update Classification partially : {}, {}", id, classification);
        if (classification.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classification.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!classificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Classification> result = classificationRepository
            .findById(classification.getId())
            .map(existingClassification -> {
                if (classification.getDocumentSeriesName() != null) {
                    existingClassification.setDocumentSeriesName(classification.getDocumentSeriesName());
                }
                if (classification.getClassificationFileNet() != null) {
                    existingClassification.setClassificationFileNet(classification.getClassificationFileNet());
                }
                if (classification.getnTreesCategory() != null) {
                    existingClassification.setnTreesCategory(classification.getnTreesCategory());
                }
                if (classification.getnTwoCategory() != null) {
                    existingClassification.setnTwoCategory(classification.getnTwoCategory());
                }

                return existingClassification;
            })
            .map(classificationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, classification.getId().toString())
        );
    }

    /**
     * {@code GET  /classifications} : get all the classifications.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of classifications in body.
     */
    @GetMapping("/classifications")
    public List<Classification> getAllClassifications(@RequestParam(required = false) String filter) {
        if ("attachedfile-is-null".equals(filter)) {
            log.debug("REST request to get all Classifications where attachedFile is null");
            return StreamSupport
                .stream(classificationRepository.findAll().spliterator(), false)
                .filter(classification -> classification.getAttachedFile() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Classifications");
        return classificationRepository.findAll();
    }

    /**
     * {@code GET  /classifications/:id} : get the "id" classification.
     *
     * @param id the id of the classification to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the classification, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/classifications/{id}")
    public ResponseEntity<Classification> getClassification(@PathVariable Long id) {
        log.debug("REST request to get Classification : {}", id);
        Optional<Classification> classification = classificationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(classification);
    }

    /**
     * {@code DELETE  /classifications/:id} : delete the "id" classification.
     *
     * @param id the id of the classification to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/classifications/{id}")
    public ResponseEntity<Void> deleteClassification(@PathVariable Long id) {
        log.debug("REST request to delete Classification : {}", id);
        classificationRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
