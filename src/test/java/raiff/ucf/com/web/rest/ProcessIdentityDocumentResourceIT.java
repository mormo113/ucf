package raiff.ucf.com.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
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
import raiff.ucf.com.domain.ProcessIdentityDocument;
import raiff.ucf.com.repository.ProcessIdentityDocumentRepository;

/**
 * Integration tests for the {@link ProcessIdentityDocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProcessIdentityDocumentResourceIT {

    private static final String DEFAULT_LAST_NAME_PID = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME_PID = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME_PID = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME_PID = "BBBBBBBBBB";

    private static final String DEFAULT_NATIONALITY_PID = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITY_PID = "BBBBBBBBBB";

    private static final String DEFAULT_NUMBER_PID = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER_PID = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EXPIRATION_DATE_PID = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRATION_DATE_PID = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ISSUE_DATE_PID = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ISSUE_DATE_PID = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/process-identity-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProcessIdentityDocumentRepository processIdentityDocumentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProcessIdentityDocumentMockMvc;

    private ProcessIdentityDocument processIdentityDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcessIdentityDocument createEntity(EntityManager em) {
        ProcessIdentityDocument processIdentityDocument = new ProcessIdentityDocument()
            .lastNamePid(DEFAULT_LAST_NAME_PID)
            .firstNamePid(DEFAULT_FIRST_NAME_PID)
            .nationalityPid(DEFAULT_NATIONALITY_PID)
            .numberPid(DEFAULT_NUMBER_PID)
            .expirationDatePid(DEFAULT_EXPIRATION_DATE_PID)
            .issueDatePid(DEFAULT_ISSUE_DATE_PID);
        return processIdentityDocument;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcessIdentityDocument createUpdatedEntity(EntityManager em) {
        ProcessIdentityDocument processIdentityDocument = new ProcessIdentityDocument()
            .lastNamePid(UPDATED_LAST_NAME_PID)
            .firstNamePid(UPDATED_FIRST_NAME_PID)
            .nationalityPid(UPDATED_NATIONALITY_PID)
            .numberPid(UPDATED_NUMBER_PID)
            .expirationDatePid(UPDATED_EXPIRATION_DATE_PID)
            .issueDatePid(UPDATED_ISSUE_DATE_PID);
        return processIdentityDocument;
    }

    @BeforeEach
    public void initTest() {
        processIdentityDocument = createEntity(em);
    }

    @Test
    @Transactional
    void createProcessIdentityDocument() throws Exception {
        int databaseSizeBeforeCreate = processIdentityDocumentRepository.findAll().size();
        // Create the ProcessIdentityDocument
        restProcessIdentityDocumentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processIdentityDocument))
            )
            .andExpect(status().isCreated());

        // Validate the ProcessIdentityDocument in the database
        List<ProcessIdentityDocument> processIdentityDocumentList = processIdentityDocumentRepository.findAll();
        assertThat(processIdentityDocumentList).hasSize(databaseSizeBeforeCreate + 1);
        ProcessIdentityDocument testProcessIdentityDocument = processIdentityDocumentList.get(processIdentityDocumentList.size() - 1);
        assertThat(testProcessIdentityDocument.getLastNamePid()).isEqualTo(DEFAULT_LAST_NAME_PID);
        assertThat(testProcessIdentityDocument.getFirstNamePid()).isEqualTo(DEFAULT_FIRST_NAME_PID);
        assertThat(testProcessIdentityDocument.getNationalityPid()).isEqualTo(DEFAULT_NATIONALITY_PID);
        assertThat(testProcessIdentityDocument.getNumberPid()).isEqualTo(DEFAULT_NUMBER_PID);
        assertThat(testProcessIdentityDocument.getExpirationDatePid()).isEqualTo(DEFAULT_EXPIRATION_DATE_PID);
        assertThat(testProcessIdentityDocument.getIssueDatePid()).isEqualTo(DEFAULT_ISSUE_DATE_PID);
    }

    @Test
    @Transactional
    void createProcessIdentityDocumentWithExistingId() throws Exception {
        // Create the ProcessIdentityDocument with an existing ID
        processIdentityDocument.setId(1L);

        int databaseSizeBeforeCreate = processIdentityDocumentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcessIdentityDocumentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processIdentityDocument))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessIdentityDocument in the database
        List<ProcessIdentityDocument> processIdentityDocumentList = processIdentityDocumentRepository.findAll();
        assertThat(processIdentityDocumentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProcessIdentityDocuments() throws Exception {
        // Initialize the database
        processIdentityDocumentRepository.saveAndFlush(processIdentityDocument);

        // Get all the processIdentityDocumentList
        restProcessIdentityDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processIdentityDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].lastNamePid").value(hasItem(DEFAULT_LAST_NAME_PID)))
            .andExpect(jsonPath("$.[*].firstNamePid").value(hasItem(DEFAULT_FIRST_NAME_PID)))
            .andExpect(jsonPath("$.[*].nationalityPid").value(hasItem(DEFAULT_NATIONALITY_PID)))
            .andExpect(jsonPath("$.[*].numberPid").value(hasItem(DEFAULT_NUMBER_PID)))
            .andExpect(jsonPath("$.[*].expirationDatePid").value(hasItem(DEFAULT_EXPIRATION_DATE_PID.toString())))
            .andExpect(jsonPath("$.[*].issueDatePid").value(hasItem(DEFAULT_ISSUE_DATE_PID.toString())));
    }

    @Test
    @Transactional
    void getProcessIdentityDocument() throws Exception {
        // Initialize the database
        processIdentityDocumentRepository.saveAndFlush(processIdentityDocument);

        // Get the processIdentityDocument
        restProcessIdentityDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, processIdentityDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(processIdentityDocument.getId().intValue()))
            .andExpect(jsonPath("$.lastNamePid").value(DEFAULT_LAST_NAME_PID))
            .andExpect(jsonPath("$.firstNamePid").value(DEFAULT_FIRST_NAME_PID))
            .andExpect(jsonPath("$.nationalityPid").value(DEFAULT_NATIONALITY_PID))
            .andExpect(jsonPath("$.numberPid").value(DEFAULT_NUMBER_PID))
            .andExpect(jsonPath("$.expirationDatePid").value(DEFAULT_EXPIRATION_DATE_PID.toString()))
            .andExpect(jsonPath("$.issueDatePid").value(DEFAULT_ISSUE_DATE_PID.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProcessIdentityDocument() throws Exception {
        // Get the processIdentityDocument
        restProcessIdentityDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProcessIdentityDocument() throws Exception {
        // Initialize the database
        processIdentityDocumentRepository.saveAndFlush(processIdentityDocument);

        int databaseSizeBeforeUpdate = processIdentityDocumentRepository.findAll().size();

        // Update the processIdentityDocument
        ProcessIdentityDocument updatedProcessIdentityDocument = processIdentityDocumentRepository
            .findById(processIdentityDocument.getId())
            .get();
        // Disconnect from session so that the updates on updatedProcessIdentityDocument are not directly saved in db
        em.detach(updatedProcessIdentityDocument);
        updatedProcessIdentityDocument
            .lastNamePid(UPDATED_LAST_NAME_PID)
            .firstNamePid(UPDATED_FIRST_NAME_PID)
            .nationalityPid(UPDATED_NATIONALITY_PID)
            .numberPid(UPDATED_NUMBER_PID)
            .expirationDatePid(UPDATED_EXPIRATION_DATE_PID)
            .issueDatePid(UPDATED_ISSUE_DATE_PID);

        restProcessIdentityDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProcessIdentityDocument.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProcessIdentityDocument))
            )
            .andExpect(status().isOk());

        // Validate the ProcessIdentityDocument in the database
        List<ProcessIdentityDocument> processIdentityDocumentList = processIdentityDocumentRepository.findAll();
        assertThat(processIdentityDocumentList).hasSize(databaseSizeBeforeUpdate);
        ProcessIdentityDocument testProcessIdentityDocument = processIdentityDocumentList.get(processIdentityDocumentList.size() - 1);
        assertThat(testProcessIdentityDocument.getLastNamePid()).isEqualTo(UPDATED_LAST_NAME_PID);
        assertThat(testProcessIdentityDocument.getFirstNamePid()).isEqualTo(UPDATED_FIRST_NAME_PID);
        assertThat(testProcessIdentityDocument.getNationalityPid()).isEqualTo(UPDATED_NATIONALITY_PID);
        assertThat(testProcessIdentityDocument.getNumberPid()).isEqualTo(UPDATED_NUMBER_PID);
        assertThat(testProcessIdentityDocument.getExpirationDatePid()).isEqualTo(UPDATED_EXPIRATION_DATE_PID);
        assertThat(testProcessIdentityDocument.getIssueDatePid()).isEqualTo(UPDATED_ISSUE_DATE_PID);
    }

    @Test
    @Transactional
    void putNonExistingProcessIdentityDocument() throws Exception {
        int databaseSizeBeforeUpdate = processIdentityDocumentRepository.findAll().size();
        processIdentityDocument.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessIdentityDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, processIdentityDocument.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processIdentityDocument))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessIdentityDocument in the database
        List<ProcessIdentityDocument> processIdentityDocumentList = processIdentityDocumentRepository.findAll();
        assertThat(processIdentityDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProcessIdentityDocument() throws Exception {
        int databaseSizeBeforeUpdate = processIdentityDocumentRepository.findAll().size();
        processIdentityDocument.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessIdentityDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processIdentityDocument))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessIdentityDocument in the database
        List<ProcessIdentityDocument> processIdentityDocumentList = processIdentityDocumentRepository.findAll();
        assertThat(processIdentityDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProcessIdentityDocument() throws Exception {
        int databaseSizeBeforeUpdate = processIdentityDocumentRepository.findAll().size();
        processIdentityDocument.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessIdentityDocumentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processIdentityDocument))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProcessIdentityDocument in the database
        List<ProcessIdentityDocument> processIdentityDocumentList = processIdentityDocumentRepository.findAll();
        assertThat(processIdentityDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProcessIdentityDocumentWithPatch() throws Exception {
        // Initialize the database
        processIdentityDocumentRepository.saveAndFlush(processIdentityDocument);

        int databaseSizeBeforeUpdate = processIdentityDocumentRepository.findAll().size();

        // Update the processIdentityDocument using partial update
        ProcessIdentityDocument partialUpdatedProcessIdentityDocument = new ProcessIdentityDocument();
        partialUpdatedProcessIdentityDocument.setId(processIdentityDocument.getId());

        partialUpdatedProcessIdentityDocument
            .lastNamePid(UPDATED_LAST_NAME_PID)
            .nationalityPid(UPDATED_NATIONALITY_PID)
            .expirationDatePid(UPDATED_EXPIRATION_DATE_PID)
            .issueDatePid(UPDATED_ISSUE_DATE_PID);

        restProcessIdentityDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcessIdentityDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcessIdentityDocument))
            )
            .andExpect(status().isOk());

        // Validate the ProcessIdentityDocument in the database
        List<ProcessIdentityDocument> processIdentityDocumentList = processIdentityDocumentRepository.findAll();
        assertThat(processIdentityDocumentList).hasSize(databaseSizeBeforeUpdate);
        ProcessIdentityDocument testProcessIdentityDocument = processIdentityDocumentList.get(processIdentityDocumentList.size() - 1);
        assertThat(testProcessIdentityDocument.getLastNamePid()).isEqualTo(UPDATED_LAST_NAME_PID);
        assertThat(testProcessIdentityDocument.getFirstNamePid()).isEqualTo(DEFAULT_FIRST_NAME_PID);
        assertThat(testProcessIdentityDocument.getNationalityPid()).isEqualTo(UPDATED_NATIONALITY_PID);
        assertThat(testProcessIdentityDocument.getNumberPid()).isEqualTo(DEFAULT_NUMBER_PID);
        assertThat(testProcessIdentityDocument.getExpirationDatePid()).isEqualTo(UPDATED_EXPIRATION_DATE_PID);
        assertThat(testProcessIdentityDocument.getIssueDatePid()).isEqualTo(UPDATED_ISSUE_DATE_PID);
    }

    @Test
    @Transactional
    void fullUpdateProcessIdentityDocumentWithPatch() throws Exception {
        // Initialize the database
        processIdentityDocumentRepository.saveAndFlush(processIdentityDocument);

        int databaseSizeBeforeUpdate = processIdentityDocumentRepository.findAll().size();

        // Update the processIdentityDocument using partial update
        ProcessIdentityDocument partialUpdatedProcessIdentityDocument = new ProcessIdentityDocument();
        partialUpdatedProcessIdentityDocument.setId(processIdentityDocument.getId());

        partialUpdatedProcessIdentityDocument
            .lastNamePid(UPDATED_LAST_NAME_PID)
            .firstNamePid(UPDATED_FIRST_NAME_PID)
            .nationalityPid(UPDATED_NATIONALITY_PID)
            .numberPid(UPDATED_NUMBER_PID)
            .expirationDatePid(UPDATED_EXPIRATION_DATE_PID)
            .issueDatePid(UPDATED_ISSUE_DATE_PID);

        restProcessIdentityDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcessIdentityDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcessIdentityDocument))
            )
            .andExpect(status().isOk());

        // Validate the ProcessIdentityDocument in the database
        List<ProcessIdentityDocument> processIdentityDocumentList = processIdentityDocumentRepository.findAll();
        assertThat(processIdentityDocumentList).hasSize(databaseSizeBeforeUpdate);
        ProcessIdentityDocument testProcessIdentityDocument = processIdentityDocumentList.get(processIdentityDocumentList.size() - 1);
        assertThat(testProcessIdentityDocument.getLastNamePid()).isEqualTo(UPDATED_LAST_NAME_PID);
        assertThat(testProcessIdentityDocument.getFirstNamePid()).isEqualTo(UPDATED_FIRST_NAME_PID);
        assertThat(testProcessIdentityDocument.getNationalityPid()).isEqualTo(UPDATED_NATIONALITY_PID);
        assertThat(testProcessIdentityDocument.getNumberPid()).isEqualTo(UPDATED_NUMBER_PID);
        assertThat(testProcessIdentityDocument.getExpirationDatePid()).isEqualTo(UPDATED_EXPIRATION_DATE_PID);
        assertThat(testProcessIdentityDocument.getIssueDatePid()).isEqualTo(UPDATED_ISSUE_DATE_PID);
    }

    @Test
    @Transactional
    void patchNonExistingProcessIdentityDocument() throws Exception {
        int databaseSizeBeforeUpdate = processIdentityDocumentRepository.findAll().size();
        processIdentityDocument.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessIdentityDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, processIdentityDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processIdentityDocument))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessIdentityDocument in the database
        List<ProcessIdentityDocument> processIdentityDocumentList = processIdentityDocumentRepository.findAll();
        assertThat(processIdentityDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProcessIdentityDocument() throws Exception {
        int databaseSizeBeforeUpdate = processIdentityDocumentRepository.findAll().size();
        processIdentityDocument.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessIdentityDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processIdentityDocument))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessIdentityDocument in the database
        List<ProcessIdentityDocument> processIdentityDocumentList = processIdentityDocumentRepository.findAll();
        assertThat(processIdentityDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProcessIdentityDocument() throws Exception {
        int databaseSizeBeforeUpdate = processIdentityDocumentRepository.findAll().size();
        processIdentityDocument.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessIdentityDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processIdentityDocument))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProcessIdentityDocument in the database
        List<ProcessIdentityDocument> processIdentityDocumentList = processIdentityDocumentRepository.findAll();
        assertThat(processIdentityDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProcessIdentityDocument() throws Exception {
        // Initialize the database
        processIdentityDocumentRepository.saveAndFlush(processIdentityDocument);

        int databaseSizeBeforeDelete = processIdentityDocumentRepository.findAll().size();

        // Delete the processIdentityDocument
        restProcessIdentityDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, processIdentityDocument.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProcessIdentityDocument> processIdentityDocumentList = processIdentityDocumentRepository.findAll();
        assertThat(processIdentityDocumentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
