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
import raiff.ucf.com.domain.ProcessInvoiceHomeLoan;
import raiff.ucf.com.repository.ProcessInvoiceHomeLoanRepository;
import raiff.ucf.com.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link raiff.ucf.com.domain.ProcessInvoiceHomeLoan}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProcessInvoiceHomeLoanResource {

    private final Logger log = LoggerFactory.getLogger(ProcessInvoiceHomeLoanResource.class);

    private static final String ENTITY_NAME = "ucfProcessInvoiceHomeLoan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcessInvoiceHomeLoanRepository processInvoiceHomeLoanRepository;

    public ProcessInvoiceHomeLoanResource(ProcessInvoiceHomeLoanRepository processInvoiceHomeLoanRepository) {
        this.processInvoiceHomeLoanRepository = processInvoiceHomeLoanRepository;
    }

    /**
     * {@code POST  /process-invoice-home-loans} : Create a new processInvoiceHomeLoan.
     *
     * @param processInvoiceHomeLoan the processInvoiceHomeLoan to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new processInvoiceHomeLoan, or with status {@code 400 (Bad Request)} if the processInvoiceHomeLoan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/process-invoice-home-loans")
    public ResponseEntity<ProcessInvoiceHomeLoan> createProcessInvoiceHomeLoan(@RequestBody ProcessInvoiceHomeLoan processInvoiceHomeLoan)
        throws URISyntaxException {
        log.debug("REST request to save ProcessInvoiceHomeLoan : {}", processInvoiceHomeLoan);
        if (processInvoiceHomeLoan.getId() != null) {
            throw new BadRequestAlertException("A new processInvoiceHomeLoan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProcessInvoiceHomeLoan result = processInvoiceHomeLoanRepository.save(processInvoiceHomeLoan);
        return ResponseEntity
            .created(new URI("/api/process-invoice-home-loans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /process-invoice-home-loans/:id} : Updates an existing processInvoiceHomeLoan.
     *
     * @param id the id of the processInvoiceHomeLoan to save.
     * @param processInvoiceHomeLoan the processInvoiceHomeLoan to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processInvoiceHomeLoan,
     * or with status {@code 400 (Bad Request)} if the processInvoiceHomeLoan is not valid,
     * or with status {@code 500 (Internal Server Error)} if the processInvoiceHomeLoan couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/process-invoice-home-loans/{id}")
    public ResponseEntity<ProcessInvoiceHomeLoan> updateProcessInvoiceHomeLoan(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProcessInvoiceHomeLoan processInvoiceHomeLoan
    ) throws URISyntaxException {
        log.debug("REST request to update ProcessInvoiceHomeLoan : {}, {}", id, processInvoiceHomeLoan);
        if (processInvoiceHomeLoan.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processInvoiceHomeLoan.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processInvoiceHomeLoanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProcessInvoiceHomeLoan result = processInvoiceHomeLoanRepository.save(processInvoiceHomeLoan);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, processInvoiceHomeLoan.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /process-invoice-home-loans/:id} : Partial updates given fields of an existing processInvoiceHomeLoan, field will ignore if it is null
     *
     * @param id the id of the processInvoiceHomeLoan to save.
     * @param processInvoiceHomeLoan the processInvoiceHomeLoan to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processInvoiceHomeLoan,
     * or with status {@code 400 (Bad Request)} if the processInvoiceHomeLoan is not valid,
     * or with status {@code 404 (Not Found)} if the processInvoiceHomeLoan is not found,
     * or with status {@code 500 (Internal Server Error)} if the processInvoiceHomeLoan couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/process-invoice-home-loans/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProcessInvoiceHomeLoan> partialUpdateProcessInvoiceHomeLoan(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProcessInvoiceHomeLoan processInvoiceHomeLoan
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProcessInvoiceHomeLoan partially : {}, {}", id, processInvoiceHomeLoan);
        if (processInvoiceHomeLoan.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processInvoiceHomeLoan.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processInvoiceHomeLoanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProcessInvoiceHomeLoan> result = processInvoiceHomeLoanRepository
            .findById(processInvoiceHomeLoan.getId())
            .map(existingProcessInvoiceHomeLoan -> {
                if (processInvoiceHomeLoan.getTtcAmount() != null) {
                    existingProcessInvoiceHomeLoan.setTtcAmount(processInvoiceHomeLoan.getTtcAmount());
                }
                if (processInvoiceHomeLoan.getBic() != null) {
                    existingProcessInvoiceHomeLoan.setBic(processInvoiceHomeLoan.getBic());
                }
                if (processInvoiceHomeLoan.getIban() != null) {
                    existingProcessInvoiceHomeLoan.setIban(processInvoiceHomeLoan.getIban());
                }

                return existingProcessInvoiceHomeLoan;
            })
            .map(processInvoiceHomeLoanRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, processInvoiceHomeLoan.getId().toString())
        );
    }

    /**
     * {@code GET  /process-invoice-home-loans} : get all the processInvoiceHomeLoans.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of processInvoiceHomeLoans in body.
     */
    @GetMapping("/process-invoice-home-loans")
    public List<ProcessInvoiceHomeLoan> getAllProcessInvoiceHomeLoans() {
        log.debug("REST request to get all ProcessInvoiceHomeLoans");
        return processInvoiceHomeLoanRepository.findAll();
    }

    /**
     * {@code GET  /process-invoice-home-loans/:id} : get the "id" processInvoiceHomeLoan.
     *
     * @param id the id of the processInvoiceHomeLoan to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the processInvoiceHomeLoan, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/process-invoice-home-loans/{id}")
    public ResponseEntity<ProcessInvoiceHomeLoan> getProcessInvoiceHomeLoan(@PathVariable Long id) {
        log.debug("REST request to get ProcessInvoiceHomeLoan : {}", id);
        Optional<ProcessInvoiceHomeLoan> processInvoiceHomeLoan = processInvoiceHomeLoanRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(processInvoiceHomeLoan);
    }

    /**
     * {@code DELETE  /process-invoice-home-loans/:id} : delete the "id" processInvoiceHomeLoan.
     *
     * @param id the id of the processInvoiceHomeLoan to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/process-invoice-home-loans/{id}")
    public ResponseEntity<Void> deleteProcessInvoiceHomeLoan(@PathVariable Long id) {
        log.debug("REST request to delete ProcessInvoiceHomeLoan : {}", id);
        processInvoiceHomeLoanRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
