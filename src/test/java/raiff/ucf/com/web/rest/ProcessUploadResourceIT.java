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
import raiff.ucf.com.domain.ProcessUpload;
import raiff.ucf.com.repository.ProcessUploadRepository;

/**
 * Integration tests for the {@link ProcessUploadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProcessUploadResourceIT {

    private static final String DEFAULT_PROCESS_STATE = "AAAAAAAAAA";
    private static final String UPDATED_PROCESS_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_ID = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_ID = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_LAST_UPDATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_UPDATE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_RECEPTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RECEPTION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/process-uploads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProcessUploadRepository processUploadRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProcessUploadMockMvc;

    private ProcessUpload processUpload;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcessUpload createEntity(EntityManager em) {
        ProcessUpload processUpload = new ProcessUpload()
            .processState(DEFAULT_PROCESS_STATE)
            .customerId(DEFAULT_CUSTOMER_ID)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdateDate(DEFAULT_LAST_UPDATE_DATE)
            .receptionDate(DEFAULT_RECEPTION_DATE)
            .notes(DEFAULT_NOTES);
        return processUpload;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcessUpload createUpdatedEntity(EntityManager em) {
        ProcessUpload processUpload = new ProcessUpload()
            .processState(UPDATED_PROCESS_STATE)
            .customerId(UPDATED_CUSTOMER_ID)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdateDate(UPDATED_LAST_UPDATE_DATE)
            .receptionDate(UPDATED_RECEPTION_DATE)
            .notes(UPDATED_NOTES);
        return processUpload;
    }

    @BeforeEach
    public void initTest() {
        processUpload = createEntity(em);
    }

    @Test
    @Transactional
    void createProcessUpload() throws Exception {
        int databaseSizeBeforeCreate = processUploadRepository.findAll().size();
        // Create the ProcessUpload
        restProcessUploadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(processUpload)))
            .andExpect(status().isCreated());

        // Validate the ProcessUpload in the database
        List<ProcessUpload> processUploadList = processUploadRepository.findAll();
        assertThat(processUploadList).hasSize(databaseSizeBeforeCreate + 1);
        ProcessUpload testProcessUpload = processUploadList.get(processUploadList.size() - 1);
        assertThat(testProcessUpload.getProcessState()).isEqualTo(DEFAULT_PROCESS_STATE);
        assertThat(testProcessUpload.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testProcessUpload.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testProcessUpload.getLastUpdateDate()).isEqualTo(DEFAULT_LAST_UPDATE_DATE);
        assertThat(testProcessUpload.getReceptionDate()).isEqualTo(DEFAULT_RECEPTION_DATE);
        assertThat(testProcessUpload.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void createProcessUploadWithExistingId() throws Exception {
        // Create the ProcessUpload with an existing ID
        processUpload.setId(1L);

        int databaseSizeBeforeCreate = processUploadRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcessUploadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(processUpload)))
            .andExpect(status().isBadRequest());

        // Validate the ProcessUpload in the database
        List<ProcessUpload> processUploadList = processUploadRepository.findAll();
        assertThat(processUploadList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProcessUploads() throws Exception {
        // Initialize the database
        processUploadRepository.saveAndFlush(processUpload);

        // Get all the processUploadList
        restProcessUploadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processUpload.getId().intValue())))
            .andExpect(jsonPath("$.[*].processState").value(hasItem(DEFAULT_PROCESS_STATE)))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdateDate").value(hasItem(DEFAULT_LAST_UPDATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].receptionDate").value(hasItem(DEFAULT_RECEPTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @Test
    @Transactional
    void getProcessUpload() throws Exception {
        // Initialize the database
        processUploadRepository.saveAndFlush(processUpload);

        // Get the processUpload
        restProcessUploadMockMvc
            .perform(get(ENTITY_API_URL_ID, processUpload.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(processUpload.getId().intValue()))
            .andExpect(jsonPath("$.processState").value(DEFAULT_PROCESS_STATE))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdateDate").value(DEFAULT_LAST_UPDATE_DATE.toString()))
            .andExpect(jsonPath("$.receptionDate").value(DEFAULT_RECEPTION_DATE.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getNonExistingProcessUpload() throws Exception {
        // Get the processUpload
        restProcessUploadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProcessUpload() throws Exception {
        // Initialize the database
        processUploadRepository.saveAndFlush(processUpload);

        int databaseSizeBeforeUpdate = processUploadRepository.findAll().size();

        // Update the processUpload
        ProcessUpload updatedProcessUpload = processUploadRepository.findById(processUpload.getId()).get();
        // Disconnect from session so that the updates on updatedProcessUpload are not directly saved in db
        em.detach(updatedProcessUpload);
        updatedProcessUpload
            .processState(UPDATED_PROCESS_STATE)
            .customerId(UPDATED_CUSTOMER_ID)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdateDate(UPDATED_LAST_UPDATE_DATE)
            .receptionDate(UPDATED_RECEPTION_DATE)
            .notes(UPDATED_NOTES);

        restProcessUploadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProcessUpload.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProcessUpload))
            )
            .andExpect(status().isOk());

        // Validate the ProcessUpload in the database
        List<ProcessUpload> processUploadList = processUploadRepository.findAll();
        assertThat(processUploadList).hasSize(databaseSizeBeforeUpdate);
        ProcessUpload testProcessUpload = processUploadList.get(processUploadList.size() - 1);
        assertThat(testProcessUpload.getProcessState()).isEqualTo(UPDATED_PROCESS_STATE);
        assertThat(testProcessUpload.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testProcessUpload.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testProcessUpload.getLastUpdateDate()).isEqualTo(UPDATED_LAST_UPDATE_DATE);
        assertThat(testProcessUpload.getReceptionDate()).isEqualTo(UPDATED_RECEPTION_DATE);
        assertThat(testProcessUpload.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void putNonExistingProcessUpload() throws Exception {
        int databaseSizeBeforeUpdate = processUploadRepository.findAll().size();
        processUpload.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessUploadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, processUpload.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processUpload))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessUpload in the database
        List<ProcessUpload> processUploadList = processUploadRepository.findAll();
        assertThat(processUploadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProcessUpload() throws Exception {
        int databaseSizeBeforeUpdate = processUploadRepository.findAll().size();
        processUpload.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessUploadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processUpload))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessUpload in the database
        List<ProcessUpload> processUploadList = processUploadRepository.findAll();
        assertThat(processUploadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProcessUpload() throws Exception {
        int databaseSizeBeforeUpdate = processUploadRepository.findAll().size();
        processUpload.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessUploadMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(processUpload)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProcessUpload in the database
        List<ProcessUpload> processUploadList = processUploadRepository.findAll();
        assertThat(processUploadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProcessUploadWithPatch() throws Exception {
        // Initialize the database
        processUploadRepository.saveAndFlush(processUpload);

        int databaseSizeBeforeUpdate = processUploadRepository.findAll().size();

        // Update the processUpload using partial update
        ProcessUpload partialUpdatedProcessUpload = new ProcessUpload();
        partialUpdatedProcessUpload.setId(processUpload.getId());

        partialUpdatedProcessUpload.creationDate(UPDATED_CREATION_DATE).lastUpdateDate(UPDATED_LAST_UPDATE_DATE).notes(UPDATED_NOTES);

        restProcessUploadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcessUpload.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcessUpload))
            )
            .andExpect(status().isOk());

        // Validate the ProcessUpload in the database
        List<ProcessUpload> processUploadList = processUploadRepository.findAll();
        assertThat(processUploadList).hasSize(databaseSizeBeforeUpdate);
        ProcessUpload testProcessUpload = processUploadList.get(processUploadList.size() - 1);
        assertThat(testProcessUpload.getProcessState()).isEqualTo(DEFAULT_PROCESS_STATE);
        assertThat(testProcessUpload.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testProcessUpload.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testProcessUpload.getLastUpdateDate()).isEqualTo(UPDATED_LAST_UPDATE_DATE);
        assertThat(testProcessUpload.getReceptionDate()).isEqualTo(DEFAULT_RECEPTION_DATE);
        assertThat(testProcessUpload.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void fullUpdateProcessUploadWithPatch() throws Exception {
        // Initialize the database
        processUploadRepository.saveAndFlush(processUpload);

        int databaseSizeBeforeUpdate = processUploadRepository.findAll().size();

        // Update the processUpload using partial update
        ProcessUpload partialUpdatedProcessUpload = new ProcessUpload();
        partialUpdatedProcessUpload.setId(processUpload.getId());

        partialUpdatedProcessUpload
            .processState(UPDATED_PROCESS_STATE)
            .customerId(UPDATED_CUSTOMER_ID)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdateDate(UPDATED_LAST_UPDATE_DATE)
            .receptionDate(UPDATED_RECEPTION_DATE)
            .notes(UPDATED_NOTES);

        restProcessUploadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcessUpload.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcessUpload))
            )
            .andExpect(status().isOk());

        // Validate the ProcessUpload in the database
        List<ProcessUpload> processUploadList = processUploadRepository.findAll();
        assertThat(processUploadList).hasSize(databaseSizeBeforeUpdate);
        ProcessUpload testProcessUpload = processUploadList.get(processUploadList.size() - 1);
        assertThat(testProcessUpload.getProcessState()).isEqualTo(UPDATED_PROCESS_STATE);
        assertThat(testProcessUpload.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testProcessUpload.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testProcessUpload.getLastUpdateDate()).isEqualTo(UPDATED_LAST_UPDATE_DATE);
        assertThat(testProcessUpload.getReceptionDate()).isEqualTo(UPDATED_RECEPTION_DATE);
        assertThat(testProcessUpload.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void patchNonExistingProcessUpload() throws Exception {
        int databaseSizeBeforeUpdate = processUploadRepository.findAll().size();
        processUpload.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessUploadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, processUpload.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processUpload))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessUpload in the database
        List<ProcessUpload> processUploadList = processUploadRepository.findAll();
        assertThat(processUploadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProcessUpload() throws Exception {
        int databaseSizeBeforeUpdate = processUploadRepository.findAll().size();
        processUpload.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessUploadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processUpload))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessUpload in the database
        List<ProcessUpload> processUploadList = processUploadRepository.findAll();
        assertThat(processUploadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProcessUpload() throws Exception {
        int databaseSizeBeforeUpdate = processUploadRepository.findAll().size();
        processUpload.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessUploadMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(processUpload))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProcessUpload in the database
        List<ProcessUpload> processUploadList = processUploadRepository.findAll();
        assertThat(processUploadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProcessUpload() throws Exception {
        // Initialize the database
        processUploadRepository.saveAndFlush(processUpload);

        int databaseSizeBeforeDelete = processUploadRepository.findAll().size();

        // Delete the processUpload
        restProcessUploadMockMvc
            .perform(delete(ENTITY_API_URL_ID, processUpload.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProcessUpload> processUploadList = processUploadRepository.findAll();
        assertThat(processUploadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
