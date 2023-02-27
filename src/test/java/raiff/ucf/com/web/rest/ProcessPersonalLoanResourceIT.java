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
import raiff.ucf.com.domain.ProcessPersonalLoan;
import raiff.ucf.com.repository.ProcessPersonalLoanRepository;

/**
 * Integration tests for the {@link ProcessPersonalLoanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProcessPersonalLoanResourceIT {

    private static final String DEFAULT_REQUEST_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/process-personal-loans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProcessPersonalLoanRepository processPersonalLoanRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProcessPersonalLoanMockMvc;

    private ProcessPersonalLoan processPersonalLoan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcessPersonalLoan createEntity(EntityManager em) {
        ProcessPersonalLoan processPersonalLoan = new ProcessPersonalLoan()
            .requestType(DEFAULT_REQUEST_TYPE)
            .reference(DEFAULT_REFERENCE)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME);
        return processPersonalLoan;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcessPersonalLoan createUpdatedEntity(EntityManager em) {
        ProcessPersonalLoan processPersonalLoan = new ProcessPersonalLoan()
            .requestType(UPDATED_REQUEST_TYPE)
            .reference(UPDATED_REFERENCE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME);
        return processPersonalLoan;
    }

    @BeforeEach
    public void initTest() {
        processPersonalLoan = createEntity(em);
    }

    @Test
    @Transactional
    void createProcessPersonalLoan() throws Exception {
        int databaseSizeBeforeCreate = processPersonalLoanRepository.findAll().size();
        // Create the ProcessPersonalLoan
        restProcessPersonalLoanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(processPersonalLoan))
            )
            .andExpect(status().isCreated());

        // Validate the ProcessPersonalLoan in the database
        List<ProcessPersonalLoan> processPersonalLoanList = processPersonalLoanRepository.findAll();
        assertThat(processPersonalLoanList).hasSize(databaseSizeBeforeCreate + 1);
        ProcessPersonalLoan testProcessPersonalLoan = processPersonalLoanList.get(processPersonalLoanList.size() - 1);
        assertThat(testProcessPersonalLoan.getRequestType()).isEqualTo(DEFAULT_REQUEST_TYPE);
        assertThat(testProcessPersonalLoan.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testProcessPersonalLoan.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testProcessPersonalLoan.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void createProcessPersonalLoanWithExistingId() throws Exception {
        // Create the ProcessPersonalLoan with an existing ID
        processPersonalLoan.setId(1L);

        int databaseSizeBeforeCreate = processPersonalLoanRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcessPersonalLoanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(processPersonalLoan))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessPersonalLoan in the database
        List<ProcessPersonalLoan> processPersonalLoanList = processPersonalLoanRepository.findAll();
        assertThat(processPersonalLoanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProcessPersonalLoans() throws Exception {
        // Initialize the database
        processPersonalLoanRepository.saveAndFlush(processPersonalLoan);

        // Get all the processPersonalLoanList
        restProcessPersonalLoanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processPersonalLoan.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestType").value(hasItem(DEFAULT_REQUEST_TYPE)))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)));
    }

    @Test
    @Transactional
    void getProcessPersonalLoan() throws Exception {
        // Initialize the database
        processPersonalLoanRepository.saveAndFlush(processPersonalLoan);

        // Get the processPersonalLoan
        restProcessPersonalLoanMockMvc
            .perform(get(ENTITY_API_URL_ID, processPersonalLoan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(processPersonalLoan.getId().intValue()))
            .andExpect(jsonPath("$.requestType").value(DEFAULT_REQUEST_TYPE))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME));
    }

    @Test
    @Transactional
    void getNonExistingProcessPersonalLoan() throws Exception {
        // Get the processPersonalLoan
        restProcessPersonalLoanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProcessPersonalLoan() throws Exception {
        // Initialize the database
        processPersonalLoanRepository.saveAndFlush(processPersonalLoan);

        int databaseSizeBeforeUpdate = processPersonalLoanRepository.findAll().size();

        // Update the processPersonalLoan
        ProcessPersonalLoan updatedProcessPersonalLoan = processPersonalLoanRepository.findById(processPersonalLoan.getId()).get();
        // Disconnect from session so that the updates on updatedProcessPersonalLoan are not directly saved in db
        em.detach(updatedProcessPersonalLoan);
        updatedProcessPersonalLoan
            .requestType(UPDATED_REQUEST_TYPE)
            .reference(UPDATED_REFERENCE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME);

        restProcessPersonalLoanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProcessPersonalLoan.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProcessPersonalLoan))
            )
            .andExpect(status().isOk());

        // Validate the ProcessPersonalLoan in the database
        List<ProcessPersonalLoan> processPersonalLoanList = processPersonalLoanRepository.findAll();
        assertThat(processPersonalLoanList).hasSize(databaseSizeBeforeUpdate);
        ProcessPersonalLoan testProcessPersonalLoan = processPersonalLoanList.get(processPersonalLoanList.size() - 1);
        assertThat(testProcessPersonalLoan.getRequestType()).isEqualTo(UPDATED_REQUEST_TYPE);
        assertThat(testProcessPersonalLoan.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testProcessPersonalLoan.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testProcessPersonalLoan.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void putNonExistingProcessPersonalLoan() throws Exception {
        int databaseSizeBeforeUpdate = processPersonalLoanRepository.findAll().size();
        processPersonalLoan.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessPersonalLoanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, processPersonalLoan.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processPersonalLoan))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessPersonalLoan in the database
        List<ProcessPersonalLoan> processPersonalLoanList = processPersonalLoanRepository.findAll();
        assertThat(processPersonalLoanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProcessPersonalLoan() throws Exception {
        int databaseSizeBeforeUpdate = processPersonalLoanRepository.findAll().size();
        processPersonalLoan.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessPersonalLoanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processPersonalLoan))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessPersonalLoan in the database
        List<ProcessPersonalLoan> processPersonalLoanList = processPersonalLoanRepository.findAll();
        assertThat(processPersonalLoanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProcessPersonalLoan() throws Exception {
        int databaseSizeBeforeUpdate = processPersonalLoanRepository.findAll().size();
        processPersonalLoan.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessPersonalLoanMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(processPersonalLoan))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProcessPersonalLoan in the database
        List<ProcessPersonalLoan> processPersonalLoanList = processPersonalLoanRepository.findAll();
        assertThat(processPersonalLoanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProcessPersonalLoanWithPatch() throws Exception {
        // Initialize the database
        processPersonalLoanRepository.saveAndFlush(processPersonalLoan);

        int databaseSizeBeforeUpdate = processPersonalLoanRepository.findAll().size();

        // Update the processPersonalLoan using partial update
        ProcessPersonalLoan partialUpdatedProcessPersonalLoan = new ProcessPersonalLoan();
        partialUpdatedProcessPersonalLoan.setId(processPersonalLoan.getId());

        partialUpdatedProcessPersonalLoan.reference(UPDATED_REFERENCE).firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME);

        restProcessPersonalLoanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcessPersonalLoan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcessPersonalLoan))
            )
            .andExpect(status().isOk());

        // Validate the ProcessPersonalLoan in the database
        List<ProcessPersonalLoan> processPersonalLoanList = processPersonalLoanRepository.findAll();
        assertThat(processPersonalLoanList).hasSize(databaseSizeBeforeUpdate);
        ProcessPersonalLoan testProcessPersonalLoan = processPersonalLoanList.get(processPersonalLoanList.size() - 1);
        assertThat(testProcessPersonalLoan.getRequestType()).isEqualTo(DEFAULT_REQUEST_TYPE);
        assertThat(testProcessPersonalLoan.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testProcessPersonalLoan.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testProcessPersonalLoan.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void fullUpdateProcessPersonalLoanWithPatch() throws Exception {
        // Initialize the database
        processPersonalLoanRepository.saveAndFlush(processPersonalLoan);

        int databaseSizeBeforeUpdate = processPersonalLoanRepository.findAll().size();

        // Update the processPersonalLoan using partial update
        ProcessPersonalLoan partialUpdatedProcessPersonalLoan = new ProcessPersonalLoan();
        partialUpdatedProcessPersonalLoan.setId(processPersonalLoan.getId());

        partialUpdatedProcessPersonalLoan
            .requestType(UPDATED_REQUEST_TYPE)
            .reference(UPDATED_REFERENCE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME);

        restProcessPersonalLoanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcessPersonalLoan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcessPersonalLoan))
            )
            .andExpect(status().isOk());

        // Validate the ProcessPersonalLoan in the database
        List<ProcessPersonalLoan> processPersonalLoanList = processPersonalLoanRepository.findAll();
        assertThat(processPersonalLoanList).hasSize(databaseSizeBeforeUpdate);
        ProcessPersonalLoan testProcessPersonalLoan = processPersonalLoanList.get(processPersonalLoanList.size() - 1);
        assertThat(testProcessPersonalLoan.getRequestType()).isEqualTo(UPDATED_REQUEST_TYPE);
        assertThat(testProcessPersonalLoan.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testProcessPersonalLoan.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testProcessPersonalLoan.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingProcessPersonalLoan() throws Exception {
        int databaseSizeBeforeUpdate = processPersonalLoanRepository.findAll().size();
        processPersonalLoan.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessPersonalLoanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, processPersonalLoan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processPersonalLoan))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessPersonalLoan in the database
        List<ProcessPersonalLoan> processPersonalLoanList = processPersonalLoanRepository.findAll();
        assertThat(processPersonalLoanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProcessPersonalLoan() throws Exception {
        int databaseSizeBeforeUpdate = processPersonalLoanRepository.findAll().size();
        processPersonalLoan.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessPersonalLoanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processPersonalLoan))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessPersonalLoan in the database
        List<ProcessPersonalLoan> processPersonalLoanList = processPersonalLoanRepository.findAll();
        assertThat(processPersonalLoanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProcessPersonalLoan() throws Exception {
        int databaseSizeBeforeUpdate = processPersonalLoanRepository.findAll().size();
        processPersonalLoan.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessPersonalLoanMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processPersonalLoan))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProcessPersonalLoan in the database
        List<ProcessPersonalLoan> processPersonalLoanList = processPersonalLoanRepository.findAll();
        assertThat(processPersonalLoanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProcessPersonalLoan() throws Exception {
        // Initialize the database
        processPersonalLoanRepository.saveAndFlush(processPersonalLoan);

        int databaseSizeBeforeDelete = processPersonalLoanRepository.findAll().size();

        // Delete the processPersonalLoan
        restProcessPersonalLoanMockMvc
            .perform(delete(ENTITY_API_URL_ID, processPersonalLoan.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProcessPersonalLoan> processPersonalLoanList = processPersonalLoanRepository.findAll();
        assertThat(processPersonalLoanList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
