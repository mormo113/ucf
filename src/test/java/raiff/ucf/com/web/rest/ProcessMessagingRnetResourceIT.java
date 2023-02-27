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
import raiff.ucf.com.domain.ProcessMessagingRnet;
import raiff.ucf.com.repository.ProcessMessagingRnetRepository;

/**
 * Integration tests for the {@link ProcessMessagingRnetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProcessMessagingRnetResourceIT {

    private static final String DEFAULT_MESSAGE_RNET_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE_RNET_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE_RNET_REF = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE_RNET_REF = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/process-messaging-rnets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProcessMessagingRnetRepository processMessagingRnetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProcessMessagingRnetMockMvc;

    private ProcessMessagingRnet processMessagingRnet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcessMessagingRnet createEntity(EntityManager em) {
        ProcessMessagingRnet processMessagingRnet = new ProcessMessagingRnet()
            .messageRnetTitle(DEFAULT_MESSAGE_RNET_TITLE)
            .messageRnetRef(DEFAULT_MESSAGE_RNET_REF);
        return processMessagingRnet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcessMessagingRnet createUpdatedEntity(EntityManager em) {
        ProcessMessagingRnet processMessagingRnet = new ProcessMessagingRnet()
            .messageRnetTitle(UPDATED_MESSAGE_RNET_TITLE)
            .messageRnetRef(UPDATED_MESSAGE_RNET_REF);
        return processMessagingRnet;
    }

    @BeforeEach
    public void initTest() {
        processMessagingRnet = createEntity(em);
    }

    @Test
    @Transactional
    void createProcessMessagingRnet() throws Exception {
        int databaseSizeBeforeCreate = processMessagingRnetRepository.findAll().size();
        // Create the ProcessMessagingRnet
        restProcessMessagingRnetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processMessagingRnet))
            )
            .andExpect(status().isCreated());

        // Validate the ProcessMessagingRnet in the database
        List<ProcessMessagingRnet> processMessagingRnetList = processMessagingRnetRepository.findAll();
        assertThat(processMessagingRnetList).hasSize(databaseSizeBeforeCreate + 1);
        ProcessMessagingRnet testProcessMessagingRnet = processMessagingRnetList.get(processMessagingRnetList.size() - 1);
        assertThat(testProcessMessagingRnet.getMessageRnetTitle()).isEqualTo(DEFAULT_MESSAGE_RNET_TITLE);
        assertThat(testProcessMessagingRnet.getMessageRnetRef()).isEqualTo(DEFAULT_MESSAGE_RNET_REF);
    }

    @Test
    @Transactional
    void createProcessMessagingRnetWithExistingId() throws Exception {
        // Create the ProcessMessagingRnet with an existing ID
        processMessagingRnet.setId(1L);

        int databaseSizeBeforeCreate = processMessagingRnetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcessMessagingRnetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processMessagingRnet))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessMessagingRnet in the database
        List<ProcessMessagingRnet> processMessagingRnetList = processMessagingRnetRepository.findAll();
        assertThat(processMessagingRnetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProcessMessagingRnets() throws Exception {
        // Initialize the database
        processMessagingRnetRepository.saveAndFlush(processMessagingRnet);

        // Get all the processMessagingRnetList
        restProcessMessagingRnetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processMessagingRnet.getId().intValue())))
            .andExpect(jsonPath("$.[*].messageRnetTitle").value(hasItem(DEFAULT_MESSAGE_RNET_TITLE)))
            .andExpect(jsonPath("$.[*].messageRnetRef").value(hasItem(DEFAULT_MESSAGE_RNET_REF)));
    }

    @Test
    @Transactional
    void getProcessMessagingRnet() throws Exception {
        // Initialize the database
        processMessagingRnetRepository.saveAndFlush(processMessagingRnet);

        // Get the processMessagingRnet
        restProcessMessagingRnetMockMvc
            .perform(get(ENTITY_API_URL_ID, processMessagingRnet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(processMessagingRnet.getId().intValue()))
            .andExpect(jsonPath("$.messageRnetTitle").value(DEFAULT_MESSAGE_RNET_TITLE))
            .andExpect(jsonPath("$.messageRnetRef").value(DEFAULT_MESSAGE_RNET_REF));
    }

    @Test
    @Transactional
    void getNonExistingProcessMessagingRnet() throws Exception {
        // Get the processMessagingRnet
        restProcessMessagingRnetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProcessMessagingRnet() throws Exception {
        // Initialize the database
        processMessagingRnetRepository.saveAndFlush(processMessagingRnet);

        int databaseSizeBeforeUpdate = processMessagingRnetRepository.findAll().size();

        // Update the processMessagingRnet
        ProcessMessagingRnet updatedProcessMessagingRnet = processMessagingRnetRepository.findById(processMessagingRnet.getId()).get();
        // Disconnect from session so that the updates on updatedProcessMessagingRnet are not directly saved in db
        em.detach(updatedProcessMessagingRnet);
        updatedProcessMessagingRnet.messageRnetTitle(UPDATED_MESSAGE_RNET_TITLE).messageRnetRef(UPDATED_MESSAGE_RNET_REF);

        restProcessMessagingRnetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProcessMessagingRnet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProcessMessagingRnet))
            )
            .andExpect(status().isOk());

        // Validate the ProcessMessagingRnet in the database
        List<ProcessMessagingRnet> processMessagingRnetList = processMessagingRnetRepository.findAll();
        assertThat(processMessagingRnetList).hasSize(databaseSizeBeforeUpdate);
        ProcessMessagingRnet testProcessMessagingRnet = processMessagingRnetList.get(processMessagingRnetList.size() - 1);
        assertThat(testProcessMessagingRnet.getMessageRnetTitle()).isEqualTo(UPDATED_MESSAGE_RNET_TITLE);
        assertThat(testProcessMessagingRnet.getMessageRnetRef()).isEqualTo(UPDATED_MESSAGE_RNET_REF);
    }

    @Test
    @Transactional
    void putNonExistingProcessMessagingRnet() throws Exception {
        int databaseSizeBeforeUpdate = processMessagingRnetRepository.findAll().size();
        processMessagingRnet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessMessagingRnetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, processMessagingRnet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processMessagingRnet))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessMessagingRnet in the database
        List<ProcessMessagingRnet> processMessagingRnetList = processMessagingRnetRepository.findAll();
        assertThat(processMessagingRnetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProcessMessagingRnet() throws Exception {
        int databaseSizeBeforeUpdate = processMessagingRnetRepository.findAll().size();
        processMessagingRnet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessMessagingRnetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processMessagingRnet))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessMessagingRnet in the database
        List<ProcessMessagingRnet> processMessagingRnetList = processMessagingRnetRepository.findAll();
        assertThat(processMessagingRnetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProcessMessagingRnet() throws Exception {
        int databaseSizeBeforeUpdate = processMessagingRnetRepository.findAll().size();
        processMessagingRnet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessMessagingRnetMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(processMessagingRnet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProcessMessagingRnet in the database
        List<ProcessMessagingRnet> processMessagingRnetList = processMessagingRnetRepository.findAll();
        assertThat(processMessagingRnetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProcessMessagingRnetWithPatch() throws Exception {
        // Initialize the database
        processMessagingRnetRepository.saveAndFlush(processMessagingRnet);

        int databaseSizeBeforeUpdate = processMessagingRnetRepository.findAll().size();

        // Update the processMessagingRnet using partial update
        ProcessMessagingRnet partialUpdatedProcessMessagingRnet = new ProcessMessagingRnet();
        partialUpdatedProcessMessagingRnet.setId(processMessagingRnet.getId());

        restProcessMessagingRnetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcessMessagingRnet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcessMessagingRnet))
            )
            .andExpect(status().isOk());

        // Validate the ProcessMessagingRnet in the database
        List<ProcessMessagingRnet> processMessagingRnetList = processMessagingRnetRepository.findAll();
        assertThat(processMessagingRnetList).hasSize(databaseSizeBeforeUpdate);
        ProcessMessagingRnet testProcessMessagingRnet = processMessagingRnetList.get(processMessagingRnetList.size() - 1);
        assertThat(testProcessMessagingRnet.getMessageRnetTitle()).isEqualTo(DEFAULT_MESSAGE_RNET_TITLE);
        assertThat(testProcessMessagingRnet.getMessageRnetRef()).isEqualTo(DEFAULT_MESSAGE_RNET_REF);
    }

    @Test
    @Transactional
    void fullUpdateProcessMessagingRnetWithPatch() throws Exception {
        // Initialize the database
        processMessagingRnetRepository.saveAndFlush(processMessagingRnet);

        int databaseSizeBeforeUpdate = processMessagingRnetRepository.findAll().size();

        // Update the processMessagingRnet using partial update
        ProcessMessagingRnet partialUpdatedProcessMessagingRnet = new ProcessMessagingRnet();
        partialUpdatedProcessMessagingRnet.setId(processMessagingRnet.getId());

        partialUpdatedProcessMessagingRnet.messageRnetTitle(UPDATED_MESSAGE_RNET_TITLE).messageRnetRef(UPDATED_MESSAGE_RNET_REF);

        restProcessMessagingRnetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcessMessagingRnet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcessMessagingRnet))
            )
            .andExpect(status().isOk());

        // Validate the ProcessMessagingRnet in the database
        List<ProcessMessagingRnet> processMessagingRnetList = processMessagingRnetRepository.findAll();
        assertThat(processMessagingRnetList).hasSize(databaseSizeBeforeUpdate);
        ProcessMessagingRnet testProcessMessagingRnet = processMessagingRnetList.get(processMessagingRnetList.size() - 1);
        assertThat(testProcessMessagingRnet.getMessageRnetTitle()).isEqualTo(UPDATED_MESSAGE_RNET_TITLE);
        assertThat(testProcessMessagingRnet.getMessageRnetRef()).isEqualTo(UPDATED_MESSAGE_RNET_REF);
    }

    @Test
    @Transactional
    void patchNonExistingProcessMessagingRnet() throws Exception {
        int databaseSizeBeforeUpdate = processMessagingRnetRepository.findAll().size();
        processMessagingRnet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessMessagingRnetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, processMessagingRnet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processMessagingRnet))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessMessagingRnet in the database
        List<ProcessMessagingRnet> processMessagingRnetList = processMessagingRnetRepository.findAll();
        assertThat(processMessagingRnetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProcessMessagingRnet() throws Exception {
        int databaseSizeBeforeUpdate = processMessagingRnetRepository.findAll().size();
        processMessagingRnet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessMessagingRnetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processMessagingRnet))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessMessagingRnet in the database
        List<ProcessMessagingRnet> processMessagingRnetList = processMessagingRnetRepository.findAll();
        assertThat(processMessagingRnetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProcessMessagingRnet() throws Exception {
        int databaseSizeBeforeUpdate = processMessagingRnetRepository.findAll().size();
        processMessagingRnet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessMessagingRnetMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processMessagingRnet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProcessMessagingRnet in the database
        List<ProcessMessagingRnet> processMessagingRnetList = processMessagingRnetRepository.findAll();
        assertThat(processMessagingRnetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProcessMessagingRnet() throws Exception {
        // Initialize the database
        processMessagingRnetRepository.saveAndFlush(processMessagingRnet);

        int databaseSizeBeforeDelete = processMessagingRnetRepository.findAll().size();

        // Delete the processMessagingRnet
        restProcessMessagingRnetMockMvc
            .perform(delete(ENTITY_API_URL_ID, processMessagingRnet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProcessMessagingRnet> processMessagingRnetList = processMessagingRnetRepository.findAll();
        assertThat(processMessagingRnetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
