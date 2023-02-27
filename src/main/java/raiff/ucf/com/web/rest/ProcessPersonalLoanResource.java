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
import raiff.ucf.com.domain.ProcessPersonalLoan;
import raiff.ucf.com.repository.ProcessPersonalLoanRepository;
import raiff.ucf.com.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link raiff.ucf.com.domain.ProcessPersonalLoan}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProcessPersonalLoanResource {

    private final Logger log = LoggerFactory.getLogger(ProcessPersonalLoanResource.class);

    private static final String ENTITY_NAME = "ucfProcessPersonalLoan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcessPersonalLoanRepository processPersonalLoanRepository;

    public ProcessPersonalLoanResource(ProcessPersonalLoanRepository processPersonalLoanRepository) {
        this.processPersonalLoanRepository = processPersonalLoanRepository;
    }

    /**
     * {@code POST  /process-personal-loans} : Create a new processPersonalLoan.
     *
     * @param processPersonalLoan the processPersonalLoan to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new processPersonalLoan, or with status {@code 400 (Bad Request)} if the processPersonalLoan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/process-personal-loans")
    public ResponseEntity<ProcessPersonalLoan> createProcessPersonalLoan(@RequestBody ProcessPersonalLoan processPersonalLoan)
        throws URISyntaxException {
        log.debug("REST request to save ProcessPersonalLoan : {}", processPersonalLoan);
        if (processPersonalLoan.getId() != null) {
            throw new BadRequestAlertException("A new processPersonalLoan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProcessPersonalLoan result = processPersonalLoanRepository.save(processPersonalLoan);
        return ResponseEntity
            .created(new URI("/api/process-personal-loans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /process-personal-loans/:id} : Updates an existing processPersonalLoan.
     *
     * @param id the id of the processPersonalLoan to save.
     * @param processPersonalLoan the processPersonalLoan to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processPersonalLoan,
     * or with status {@code 400 (Bad Request)} if the processPersonalLoan is not valid,
     * or with status {@code 500 (Internal Server Error)} if the processPersonalLoan couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/process-personal-loans/{id}")
    public ResponseEntity<ProcessPersonalLoan> updateProcessPersonalLoan(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProcessPersonalLoan processPersonalLoan
    ) throws URISyntaxException {
        log.debug("REST request to update ProcessPersonalLoan : {}, {}", id, processPersonalLoan);
        if (processPersonalLoan.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processPersonalLoan.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processPersonalLoanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProcessPersonalLoan result = processPersonalLoanRepository.save(processPersonalLoan);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, processPersonalLoan.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /process-personal-loans/:id} : Partial updates given fields of an existing processPersonalLoan, field will ignore if it is null
     *
     * @param id the id of the processPersonalLoan to save.
     * @param processPersonalLoan the processPersonalLoan to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processPersonalLoan,
     * or with status {@code 400 (Bad Request)} if the processPersonalLoan is not valid,
     * or with status {@code 404 (Not Found)} if the processPersonalLoan is not found,
     * or with status {@code 500 (Internal Server Error)} if the processPersonalLoan couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/process-personal-loans/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProcessPersonalLoan> partialUpdateProcessPersonalLoan(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProcessPersonalLoan processPersonalLoan
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProcessPersonalLoan partially : {}, {}", id, processPersonalLoan);
        if (processPersonalLoan.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processPersonalLoan.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processPersonalLoanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProcessPersonalLoan> result = processPersonalLoanRepository
            .findById(processPersonalLoan.getId())
            .map(existingProcessPersonalLoan -> {
                if (processPersonalLoan.getRequestType() != null) {
                    existingProcessPersonalLoan.setRequestType(processPersonalLoan.getRequestType());
                }
                if (processPersonalLoan.getReference() != null) {
                    existingProcessPersonalLoan.setReference(processPersonalLoan.getReference());
                }
                if (processPersonalLoan.getFirstName() != null) {
                    existingProcessPersonalLoan.setFirstName(processPersonalLoan.getFirstName());
                }
                if (processPersonalLoan.getLastName() != null) {
                    existingProcessPersonalLoan.setLastName(processPersonalLoan.getLastName());
                }

                return existingProcessPersonalLoan;
            })
            .map(processPersonalLoanRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, processPersonalLoan.getId().toString())
        );
    }

    /**
     * {@code GET  /process-personal-loans} : get all the processPersonalLoans.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of processPersonalLoans in body.
     */
    @GetMapping("/process-personal-loans")
    public List<ProcessPersonalLoan> getAllProcessPersonalLoans() {
        log.debug("REST request to get all ProcessPersonalLoans");
        return processPersonalLoanRepository.findAll();
    }

    /**
     * {@code GET  /process-personal-loans/:id} : get the "id" processPersonalLoan.
     *
     * @param id the id of the processPersonalLoan to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the processPersonalLoan, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/process-personal-loans/{id}")
    public ResponseEntity<ProcessPersonalLoan> getProcessPersonalLoan(@PathVariable Long id) {
        log.debug("REST request to get ProcessPersonalLoan : {}", id);
        Optional<ProcessPersonalLoan> processPersonalLoan = processPersonalLoanRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(processPersonalLoan);
    }

    /**
     * {@code DELETE  /process-personal-loans/:id} : delete the "id" processPersonalLoan.
     *
     * @param id the id of the processPersonalLoan to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/process-personal-loans/{id}")
    public ResponseEntity<Void> deleteProcessPersonalLoan(@PathVariable Long id) {
        log.debug("REST request to delete ProcessPersonalLoan : {}", id);
        processPersonalLoanRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
