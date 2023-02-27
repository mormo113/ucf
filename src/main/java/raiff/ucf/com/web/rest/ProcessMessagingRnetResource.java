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
import raiff.ucf.com.domain.ProcessMessagingRnet;
import raiff.ucf.com.repository.ProcessMessagingRnetRepository;
import raiff.ucf.com.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link raiff.ucf.com.domain.ProcessMessagingRnet}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProcessMessagingRnetResource {

    private final Logger log = LoggerFactory.getLogger(ProcessMessagingRnetResource.class);

    private static final String ENTITY_NAME = "ucfProcessMessagingRnet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcessMessagingRnetRepository processMessagingRnetRepository;

    public ProcessMessagingRnetResource(ProcessMessagingRnetRepository processMessagingRnetRepository) {
        this.processMessagingRnetRepository = processMessagingRnetRepository;
    }

    /**
     * {@code POST  /process-messaging-rnets} : Create a new processMessagingRnet.
     *
     * @param processMessagingRnet the processMessagingRnet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new processMessagingRnet, or with status {@code 400 (Bad Request)} if the processMessagingRnet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/process-messaging-rnets")
    public ResponseEntity<ProcessMessagingRnet> createProcessMessagingRnet(@RequestBody ProcessMessagingRnet processMessagingRnet)
        throws URISyntaxException {
        log.debug("REST request to save ProcessMessagingRnet : {}", processMessagingRnet);
        if (processMessagingRnet.getId() != null) {
            throw new BadRequestAlertException("A new processMessagingRnet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProcessMessagingRnet result = processMessagingRnetRepository.save(processMessagingRnet);
        return ResponseEntity
            .created(new URI("/api/process-messaging-rnets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /process-messaging-rnets/:id} : Updates an existing processMessagingRnet.
     *
     * @param id the id of the processMessagingRnet to save.
     * @param processMessagingRnet the processMessagingRnet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processMessagingRnet,
     * or with status {@code 400 (Bad Request)} if the processMessagingRnet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the processMessagingRnet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/process-messaging-rnets/{id}")
    public ResponseEntity<ProcessMessagingRnet> updateProcessMessagingRnet(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProcessMessagingRnet processMessagingRnet
    ) throws URISyntaxException {
        log.debug("REST request to update ProcessMessagingRnet : {}, {}", id, processMessagingRnet);
        if (processMessagingRnet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processMessagingRnet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processMessagingRnetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProcessMessagingRnet result = processMessagingRnetRepository.save(processMessagingRnet);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, processMessagingRnet.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /process-messaging-rnets/:id} : Partial updates given fields of an existing processMessagingRnet, field will ignore if it is null
     *
     * @param id the id of the processMessagingRnet to save.
     * @param processMessagingRnet the processMessagingRnet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processMessagingRnet,
     * or with status {@code 400 (Bad Request)} if the processMessagingRnet is not valid,
     * or with status {@code 404 (Not Found)} if the processMessagingRnet is not found,
     * or with status {@code 500 (Internal Server Error)} if the processMessagingRnet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/process-messaging-rnets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProcessMessagingRnet> partialUpdateProcessMessagingRnet(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProcessMessagingRnet processMessagingRnet
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProcessMessagingRnet partially : {}, {}", id, processMessagingRnet);
        if (processMessagingRnet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processMessagingRnet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processMessagingRnetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProcessMessagingRnet> result = processMessagingRnetRepository
            .findById(processMessagingRnet.getId())
            .map(existingProcessMessagingRnet -> {
                if (processMessagingRnet.getMessageRnetTitle() != null) {
                    existingProcessMessagingRnet.setMessageRnetTitle(processMessagingRnet.getMessageRnetTitle());
                }
                if (processMessagingRnet.getMessageRnetRef() != null) {
                    existingProcessMessagingRnet.setMessageRnetRef(processMessagingRnet.getMessageRnetRef());
                }

                return existingProcessMessagingRnet;
            })
            .map(processMessagingRnetRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, processMessagingRnet.getId().toString())
        );
    }

    /**
     * {@code GET  /process-messaging-rnets} : get all the processMessagingRnets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of processMessagingRnets in body.
     */
    @GetMapping("/process-messaging-rnets")
    public List<ProcessMessagingRnet> getAllProcessMessagingRnets() {
        log.debug("REST request to get all ProcessMessagingRnets");
        return processMessagingRnetRepository.findAll();
    }

    /**
     * {@code GET  /process-messaging-rnets/:id} : get the "id" processMessagingRnet.
     *
     * @param id the id of the processMessagingRnet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the processMessagingRnet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/process-messaging-rnets/{id}")
    public ResponseEntity<ProcessMessagingRnet> getProcessMessagingRnet(@PathVariable Long id) {
        log.debug("REST request to get ProcessMessagingRnet : {}", id);
        Optional<ProcessMessagingRnet> processMessagingRnet = processMessagingRnetRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(processMessagingRnet);
    }

    /**
     * {@code DELETE  /process-messaging-rnets/:id} : delete the "id" processMessagingRnet.
     *
     * @param id the id of the processMessagingRnet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/process-messaging-rnets/{id}")
    public ResponseEntity<Void> deleteProcessMessagingRnet(@PathVariable Long id) {
        log.debug("REST request to delete ProcessMessagingRnet : {}", id);
        processMessagingRnetRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
