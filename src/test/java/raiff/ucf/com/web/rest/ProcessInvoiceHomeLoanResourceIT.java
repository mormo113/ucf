package raiff.ucf.com.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import raiff.ucf.com.IntegrationTest;
import raiff.ucf.com.domain.ProcessInvoiceHomeLoan;
import raiff.ucf.com.repository.ProcessInvoiceHomeLoanRepository;

/**
 * Integration tests for the {@link ProcessInvoiceHomeLoanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProcessInvoiceHomeLoanResourceIT {

    private static final String DEFAULT_TTC_AMOUNT = "AAAAAAAAAA";
    private static final String UPDATED_TTC_AMOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_BIC = "AAAAAAAAAA";
    private static final String UPDATED_BIC = "BBBBBBBBBB";

    private static final String DEFAULT_IBAN = "AAAAAAAAAA";
    private static final String UPDATED_IBAN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/process-invoice-home-loans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProcessInvoiceHomeLoanRepository processInvoiceHomeLoanRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProcessInvoiceHomeLoanMockMvc;

    private ProcessInvoiceHomeLoan processInvoiceHomeLoan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcessInvoiceHomeLoan createEntity(EntityManager em) {
        ProcessInvoiceHomeLoan processInvoiceHomeLoan = new ProcessInvoiceHomeLoan()
            .ttcAmount(DEFAULT_TTC_AMOUNT)
            .bic(DEFAULT_BIC)
            .iban(DEFAULT_IBAN);
        return processInvoiceHomeLoan;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcessInvoiceHomeLoan createUpdatedEntity(EntityManager em) {
        ProcessInvoiceHomeLoan processInvoiceHomeLoan = new ProcessInvoiceHomeLoan()
            .ttcAmount(UPDATED_TTC_AMOUNT)
            .bic(UPDATED_BIC)
            .iban(UPDATED_IBAN);
        return processInvoiceHomeLoan;
    }

    @BeforeEach
    public void initTest() {
        processInvoiceHomeLoan = createEntity(em);
    }

    @Test
    @Transactional
    void createProcessInvoiceHomeLoan() throws Exception {
        int databaseSizeBeforeCreate = processInvoiceHomeLoanRepository.findAll().size();
        // Create the ProcessInvoiceHomeLoan
        restProcessInvoiceHomeLoanMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processInvoiceHomeLoan))
            )
            .andExpect(status().isCreated());

        // Validate the ProcessInvoiceHomeLoan in the database
        List<ProcessInvoiceHomeLoan> processInvoiceHomeLoanList = processInvoiceHomeLoanRepository.findAll();
        assertThat(processInvoiceHomeLoanList).hasSize(databaseSizeBeforeCreate + 1);
        ProcessInvoiceHomeLoan testProcessInvoiceHomeLoan = processInvoiceHomeLoanList.get(processInvoiceHomeLoanList.size() - 1);
        assertThat(testProcessInvoiceHomeLoan.getTtcAmount()).isEqualTo(DEFAULT_TTC_AMOUNT);
        assertThat(testProcessInvoiceHomeLoan.getBic()).isEqualTo(DEFAULT_BIC);
        assertThat(testProcessInvoiceHomeLoan.getIban()).isEqualTo(DEFAULT_IBAN);
    }

    @Test
    @Transactional
    void createProcessInvoiceHomeLoanWithExistingId() throws Exception {
        // Create the ProcessInvoiceHomeLoan with an existing ID
        processInvoiceHomeLoan.setId(1L);

        int databaseSizeBeforeCreate = processInvoiceHomeLoanRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcessInvoiceHomeLoanMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processInvoiceHomeLoan))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessInvoiceHomeLoan in the database
        List<ProcessInvoiceHomeLoan> processInvoiceHomeLoanList = processInvoiceHomeLoanRepository.findAll();
        assertThat(processInvoiceHomeLoanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProcessInvoiceHomeLoans() throws Exception {
        // Initialize the database
        processInvoiceHomeLoanRepository.saveAndFlush(processInvoiceHomeLoan);

        // Get all the processInvoiceHomeLoanList
        restProcessInvoiceHomeLoanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processInvoiceHomeLoan.getId().intValue())))
            .andExpect(jsonPath("$.[*].ttcAmount").value(hasItem(DEFAULT_TTC_AMOUNT)))
            .andExpect(jsonPath("$.[*].bic").value(hasItem(DEFAULT_BIC)))
            .andExpect(jsonPath("$.[*].iban").value(hasItem(DEFAULT_IBAN)));
    }

    @Test
    @Transactional
    void getProcessInvoiceHomeLoan() throws Exception {
        // Initialize the database
        processInvoiceHomeLoanRepository.saveAndFlush(processInvoiceHomeLoan);

        // Get the processInvoiceHomeLoan
        restProcessInvoiceHomeLoanMockMvc
            .perform(get(ENTITY_API_URL_ID, processInvoiceHomeLoan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(processInvoiceHomeLoan.getId().intValue()))
            .andExpect(jsonPath("$.ttcAmount").value(DEFAULT_TTC_AMOUNT))
            .andExpect(jsonPath("$.bic").value(DEFAULT_BIC))
            .andExpect(jsonPath("$.iban").value(DEFAULT_IBAN));
    }

    @Test
    @Transactional
    void getNonExistingProcessInvoiceHomeLoan() throws Exception {
        // Get the processInvoiceHomeLoan
        restProcessInvoiceHomeLoanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProcessInvoiceHomeLoan() throws Exception {
        // Initialize the database
        processInvoiceHomeLoanRepository.saveAndFlush(processInvoiceHomeLoan);

        int databaseSizeBeforeUpdate = processInvoiceHomeLoanRepository.findAll().size();

        // Update the processInvoiceHomeLoan
        ProcessInvoiceHomeLoan updatedProcessInvoiceHomeLoan = processInvoiceHomeLoanRepository
            .findById(processInvoiceHomeLoan.getId())
            .get();
        // Disconnect from session so that the updates on updatedProcessInvoiceHomeLoan are not directly saved in db
        em.detach(updatedProcessInvoiceHomeLoan);
        updatedProcessInvoiceHomeLoan.ttcAmount(UPDATED_TTC_AMOUNT).bic(UPDATED_BIC).iban(UPDATED_IBAN);

        restProcessInvoiceHomeLoanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProcessInvoiceHomeLoan.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProcessInvoiceHomeLoan))
            )
            .andExpect(status().isOk());

        // Validate the ProcessInvoiceHomeLoan in the database
        List<ProcessInvoiceHomeLoan> processInvoiceHomeLoanList = processInvoiceHomeLoanRepository.findAll();
        assertThat(processInvoiceHomeLoanList).hasSize(databaseSizeBeforeUpdate);
        ProcessInvoiceHomeLoan testProcessInvoiceHomeLoan = processInvoiceHomeLoanList.get(processInvoiceHomeLoanList.size() - 1);
        assertThat(testProcessInvoiceHomeLoan.getTtcAmount()).isEqualTo(UPDATED_TTC_AMOUNT);
        assertThat(testProcessInvoiceHomeLoan.getBic()).isEqualTo(UPDATED_BIC);
        assertThat(testProcessInvoiceHomeLoan.getIban()).isEqualTo(UPDATED_IBAN);
    }

    @Test
    @Transactional
    void putNonExistingProcessInvoiceHomeLoan() throws Exception {
        int databaseSizeBeforeUpdate = processInvoiceHomeLoanRepository.findAll().size();
        processInvoiceHomeLoan.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessInvoiceHomeLoanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, processInvoiceHomeLoan.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processInvoiceHomeLoan))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessInvoiceHomeLoan in the database
        List<ProcessInvoiceHomeLoan> processInvoiceHomeLoanList = processInvoiceHomeLoanRepository.findAll();
        assertThat(processInvoiceHomeLoanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProcessInvoiceHomeLoan() throws Exception {
        int databaseSizeBeforeUpdate = processInvoiceHomeLoanRepository.findAll().size();
        processInvoiceHomeLoan.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessInvoiceHomeLoanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processInvoiceHomeLoan))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessInvoiceHomeLoan in the database
        List<ProcessInvoiceHomeLoan> processInvoiceHomeLoanList = processInvoiceHomeLoanRepository.findAll();
        assertThat(processInvoiceHomeLoanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProcessInvoiceHomeLoan() throws Exception {
        int databaseSizeBeforeUpdate = processInvoiceHomeLoanRepository.findAll().size();
        processInvoiceHomeLoan.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessInvoiceHomeLoanMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processInvoiceHomeLoan))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProcessInvoiceHomeLoan in the database
        List<ProcessInvoiceHomeLoan> processInvoiceHomeLoanList = processInvoiceHomeLoanRepository.findAll();
        assertThat(processInvoiceHomeLoanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProcessInvoiceHomeLoanWithPatch() throws Exception {
        // Initialize the database
        processInvoiceHomeLoanRepository.saveAndFlush(processInvoiceHomeLoan);

        int databaseSizeBeforeUpdate = processInvoiceHomeLoanRepository.findAll().size();

        // Update the processInvoiceHomeLoan using partial update
        ProcessInvoiceHomeLoan partialUpdatedProcessInvoiceHomeLoan = new ProcessInvoiceHomeLoan();
        partialUpdatedProcessInvoiceHomeLoan.setId(processInvoiceHomeLoan.getId());

        partialUpdatedProcessInvoiceHomeLoan.ttcAmount(UPDATED_TTC_AMOUNT);

        restProcessInvoiceHomeLoanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcessInvoiceHomeLoan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcessInvoiceHomeLoan))
            )
            .andExpect(status().isOk());

        // Validate the ProcessInvoiceHomeLoan in the database
        List<ProcessInvoiceHomeLoan> processInvoiceHomeLoanList = processInvoiceHomeLoanRepository.findAll();
        assertThat(processInvoiceHomeLoanList).hasSize(databaseSizeBeforeUpdate);
        ProcessInvoiceHomeLoan testProcessInvoiceHomeLoan = processInvoiceHomeLoanList.get(processInvoiceHomeLoanList.size() - 1);
        assertThat(testProcessInvoiceHomeLoan.getTtcAmount()).isEqualTo(UPDATED_TTC_AMOUNT);
        assertThat(testProcessInvoiceHomeLoan.getBic()).isEqualTo(DEFAULT_BIC);
        assertThat(testProcessInvoiceHomeLoan.getIban()).isEqualTo(DEFAULT_IBAN);
    }

    @Test
    @Transactional
    void fullUpdateProcessInvoiceHomeLoanWithPatch() throws Exception {
        // Initialize the database
        processInvoiceHomeLoanRepository.saveAndFlush(processInvoiceHomeLoan);

        int databaseSizeBeforeUpdate = processInvoiceHomeLoanRepository.findAll().size();

        // Update the processInvoiceHomeLoan using partial update
        ProcessInvoiceHomeLoan partialUpdatedProcessInvoiceHomeLoan = new ProcessInvoiceHomeLoan();
        partialUpdatedProcessInvoiceHomeLoan.setId(processInvoiceHomeLoan.getId());

        partialUpdatedProcessInvoiceHomeLoan.ttcAmount(UPDATED_TTC_AMOUNT).bic(UPDATED_BIC).iban(UPDATED_IBAN);

        restProcessInvoiceHomeLoanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcessInvoiceHomeLoan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcessInvoiceHomeLoan))
            )
            .andExpect(status().isOk());

        // Validate the ProcessInvoiceHomeLoan in the database
        List<ProcessInvoiceHomeLoan> processInvoiceHomeLoanList = processInvoiceHomeLoanRepository.findAll();
        assertThat(processInvoiceHomeLoanList).hasSize(databaseSizeBeforeUpdate);
        ProcessInvoiceHomeLoan testProcessInvoiceHomeLoan = processInvoiceHomeLoanList.get(processInvoiceHomeLoanList.size() - 1);
        assertThat(testProcessInvoiceHomeLoan.getTtcAmount()).isEqualTo(UPDATED_TTC_AMOUNT);
        assertThat(testProcessInvoiceHomeLoan.getBic()).isEqualTo(UPDATED_BIC);
        assertThat(testProcessInvoiceHomeLoan.getIban()).isEqualTo(UPDATED_IBAN);
    }

    @Test
    @Transactional
    void patchNonExistingProcessInvoiceHomeLoan() throws Exception {
        int databaseSizeBeforeUpdate = processInvoiceHomeLoanRepository.findAll().size();
        processInvoiceHomeLoan.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessInvoiceHomeLoanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, processInvoiceHomeLoan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processInvoiceHomeLoan))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessInvoiceHomeLoan in the database
        List<ProcessInvoiceHomeLoan> processInvoiceHomeLoanList = processInvoiceHomeLoanRepository.findAll();
        assertThat(processInvoiceHomeLoanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProcessInvoiceHomeLoan() throws Exception {
        int databaseSizeBeforeUpdate = processInvoiceHomeLoanRepository.findAll().size();
        processInvoiceHomeLoan.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessInvoiceHomeLoanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processInvoiceHomeLoan))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessInvoiceHomeLoan in the database
        List<ProcessInvoiceHomeLoan> processInvoiceHomeLoanList = processInvoiceHomeLoanRepository.findAll();
        assertThat(processInvoiceHomeLoanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProcessInvoiceHomeLoan() throws Exception {
        int databaseSizeBeforeUpdate = processInvoiceHomeLoanRepository.findAll().size();
        processInvoiceHomeLoan.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessInvoiceHomeLoanMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processInvoiceHomeLoan))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProcessInvoiceHomeLoan in the database
        List<ProcessInvoiceHomeLoan> processInvoiceHomeLoanList = processInvoiceHomeLoanRepository.findAll();
        assertThat(processInvoiceHomeLoanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProcessInvoiceHomeLoan() throws Exception {
        // Initialize the database
        processInvoiceHomeLoanRepository.saveAndFlush(processInvoiceHomeLoan);

        int databaseSizeBeforeDelete = processInvoiceHomeLoanRepository.findAll().size();

        // Delete the processInvoiceHomeLoan
        restProcessInvoiceHomeLoanMockMvc
            .perform(delete(ENTITY_API_URL_ID, processInvoiceHomeLoan.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProcessInvoiceHomeLoan> processInvoiceHomeLoanList = processInvoiceHomeLoanRepository.findAll();
        assertThat(processInvoiceHomeLoanList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
