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
import raiff.ucf.com.domain.AttachedFile;
import raiff.ucf.com.repository.AttachedFileRepository;

/**
 * Integration tests for the {@link AttachedFileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AttachedFileResourceIT {

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_OCR_RAW = "AAAAAAAAAA";
    private static final String UPDATED_OCR_RAW = "BBBBBBBBBB";

    private static final String DEFAULT_STORE_GED = "AAAAAAAAAA";
    private static final String UPDATED_STORE_GED = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/attached-files";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AttachedFileRepository attachedFileRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAttachedFileMockMvc;

    private AttachedFile attachedFile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttachedFile createEntity(EntityManager em) {
        AttachedFile attachedFile = new AttachedFile()
            .fileName(DEFAULT_FILE_NAME)
            .filePath(DEFAULT_FILE_PATH)
            .documentType(DEFAULT_DOCUMENT_TYPE)
            .ocrRaw(DEFAULT_OCR_RAW)
            .storeGed(DEFAULT_STORE_GED);
        return attachedFile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttachedFile createUpdatedEntity(EntityManager em) {
        AttachedFile attachedFile = new AttachedFile()
            .fileName(UPDATED_FILE_NAME)
            .filePath(UPDATED_FILE_PATH)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .ocrRaw(UPDATED_OCR_RAW)
            .storeGed(UPDATED_STORE_GED);
        return attachedFile;
    }

    @BeforeEach
    public void initTest() {
        attachedFile = createEntity(em);
    }

    @Test
    @Transactional
    void createAttachedFile() throws Exception {
        int databaseSizeBeforeCreate = attachedFileRepository.findAll().size();
        // Create the AttachedFile
        restAttachedFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attachedFile)))
            .andExpect(status().isCreated());

        // Validate the AttachedFile in the database
        List<AttachedFile> attachedFileList = attachedFileRepository.findAll();
        assertThat(attachedFileList).hasSize(databaseSizeBeforeCreate + 1);
        AttachedFile testAttachedFile = attachedFileList.get(attachedFileList.size() - 1);
        assertThat(testAttachedFile.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testAttachedFile.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testAttachedFile.getDocumentType()).isEqualTo(DEFAULT_DOCUMENT_TYPE);
        assertThat(testAttachedFile.getOcrRaw()).isEqualTo(DEFAULT_OCR_RAW);
        assertThat(testAttachedFile.getStoreGed()).isEqualTo(DEFAULT_STORE_GED);
    }

    @Test
    @Transactional
    void createAttachedFileWithExistingId() throws Exception {
        // Create the AttachedFile with an existing ID
        attachedFile.setId(1L);

        int databaseSizeBeforeCreate = attachedFileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttachedFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attachedFile)))
            .andExpect(status().isBadRequest());

        // Validate the AttachedFile in the database
        List<AttachedFile> attachedFileList = attachedFileRepository.findAll();
        assertThat(attachedFileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAttachedFiles() throws Exception {
        // Initialize the database
        attachedFileRepository.saveAndFlush(attachedFile);

        // Get all the attachedFileList
        restAttachedFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attachedFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE)))
            .andExpect(jsonPath("$.[*].ocrRaw").value(hasItem(DEFAULT_OCR_RAW)))
            .andExpect(jsonPath("$.[*].storeGed").value(hasItem(DEFAULT_STORE_GED)));
    }

    @Test
    @Transactional
    void getAttachedFile() throws Exception {
        // Initialize the database
        attachedFileRepository.saveAndFlush(attachedFile);

        // Get the attachedFile
        restAttachedFileMockMvc
            .perform(get(ENTITY_API_URL_ID, attachedFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attachedFile.getId().intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH))
            .andExpect(jsonPath("$.documentType").value(DEFAULT_DOCUMENT_TYPE))
            .andExpect(jsonPath("$.ocrRaw").value(DEFAULT_OCR_RAW))
            .andExpect(jsonPath("$.storeGed").value(DEFAULT_STORE_GED));
    }

    @Test
    @Transactional
    void getNonExistingAttachedFile() throws Exception {
        // Get the attachedFile
        restAttachedFileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAttachedFile() throws Exception {
        // Initialize the database
        attachedFileRepository.saveAndFlush(attachedFile);

        int databaseSizeBeforeUpdate = attachedFileRepository.findAll().size();

        // Update the attachedFile
        AttachedFile updatedAttachedFile = attachedFileRepository.findById(attachedFile.getId()).get();
        // Disconnect from session so that the updates on updatedAttachedFile are not directly saved in db
        em.detach(updatedAttachedFile);
        updatedAttachedFile
            .fileName(UPDATED_FILE_NAME)
            .filePath(UPDATED_FILE_PATH)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .ocrRaw(UPDATED_OCR_RAW)
            .storeGed(UPDATED_STORE_GED);

        restAttachedFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAttachedFile.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAttachedFile))
            )
            .andExpect(status().isOk());

        // Validate the AttachedFile in the database
        List<AttachedFile> attachedFileList = attachedFileRepository.findAll();
        assertThat(attachedFileList).hasSize(databaseSizeBeforeUpdate);
        AttachedFile testAttachedFile = attachedFileList.get(attachedFileList.size() - 1);
        assertThat(testAttachedFile.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testAttachedFile.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testAttachedFile.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
        assertThat(testAttachedFile.getOcrRaw()).isEqualTo(UPDATED_OCR_RAW);
        assertThat(testAttachedFile.getStoreGed()).isEqualTo(UPDATED_STORE_GED);
    }

    @Test
    @Transactional
    void putNonExistingAttachedFile() throws Exception {
        int databaseSizeBeforeUpdate = attachedFileRepository.findAll().size();
        attachedFile.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttachedFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attachedFile.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachedFile))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttachedFile in the database
        List<AttachedFile> attachedFileList = attachedFileRepository.findAll();
        assertThat(attachedFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAttachedFile() throws Exception {
        int databaseSizeBeforeUpdate = attachedFileRepository.findAll().size();
        attachedFile.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachedFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachedFile))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttachedFile in the database
        List<AttachedFile> attachedFileList = attachedFileRepository.findAll();
        assertThat(attachedFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAttachedFile() throws Exception {
        int databaseSizeBeforeUpdate = attachedFileRepository.findAll().size();
        attachedFile.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachedFileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attachedFile)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttachedFile in the database
        List<AttachedFile> attachedFileList = attachedFileRepository.findAll();
        assertThat(attachedFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAttachedFileWithPatch() throws Exception {
        // Initialize the database
        attachedFileRepository.saveAndFlush(attachedFile);

        int databaseSizeBeforeUpdate = attachedFileRepository.findAll().size();

        // Update the attachedFile using partial update
        AttachedFile partialUpdatedAttachedFile = new AttachedFile();
        partialUpdatedAttachedFile.setId(attachedFile.getId());

        partialUpdatedAttachedFile
            .fileName(UPDATED_FILE_NAME)
            .filePath(UPDATED_FILE_PATH)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .ocrRaw(UPDATED_OCR_RAW)
            .storeGed(UPDATED_STORE_GED);

        restAttachedFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttachedFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttachedFile))
            )
            .andExpect(status().isOk());

        // Validate the AttachedFile in the database
        List<AttachedFile> attachedFileList = attachedFileRepository.findAll();
        assertThat(attachedFileList).hasSize(databaseSizeBeforeUpdate);
        AttachedFile testAttachedFile = attachedFileList.get(attachedFileList.size() - 1);
        assertThat(testAttachedFile.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testAttachedFile.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testAttachedFile.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
        assertThat(testAttachedFile.getOcrRaw()).isEqualTo(UPDATED_OCR_RAW);
        assertThat(testAttachedFile.getStoreGed()).isEqualTo(UPDATED_STORE_GED);
    }

    @Test
    @Transactional
    void fullUpdateAttachedFileWithPatch() throws Exception {
        // Initialize the database
        attachedFileRepository.saveAndFlush(attachedFile);

        int databaseSizeBeforeUpdate = attachedFileRepository.findAll().size();

        // Update the attachedFile using partial update
        AttachedFile partialUpdatedAttachedFile = new AttachedFile();
        partialUpdatedAttachedFile.setId(attachedFile.getId());

        partialUpdatedAttachedFile
            .fileName(UPDATED_FILE_NAME)
            .filePath(UPDATED_FILE_PATH)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .ocrRaw(UPDATED_OCR_RAW)
            .storeGed(UPDATED_STORE_GED);

        restAttachedFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttachedFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttachedFile))
            )
            .andExpect(status().isOk());

        // Validate the AttachedFile in the database
        List<AttachedFile> attachedFileList = attachedFileRepository.findAll();
        assertThat(attachedFileList).hasSize(databaseSizeBeforeUpdate);
        AttachedFile testAttachedFile = attachedFileList.get(attachedFileList.size() - 1);
        assertThat(testAttachedFile.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testAttachedFile.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testAttachedFile.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
        assertThat(testAttachedFile.getOcrRaw()).isEqualTo(UPDATED_OCR_RAW);
        assertThat(testAttachedFile.getStoreGed()).isEqualTo(UPDATED_STORE_GED);
    }

    @Test
    @Transactional
    void patchNonExistingAttachedFile() throws Exception {
        int databaseSizeBeforeUpdate = attachedFileRepository.findAll().size();
        attachedFile.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttachedFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, attachedFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attachedFile))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttachedFile in the database
        List<AttachedFile> attachedFileList = attachedFileRepository.findAll();
        assertThat(attachedFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAttachedFile() throws Exception {
        int databaseSizeBeforeUpdate = attachedFileRepository.findAll().size();
        attachedFile.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachedFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attachedFile))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttachedFile in the database
        List<AttachedFile> attachedFileList = attachedFileRepository.findAll();
        assertThat(attachedFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAttachedFile() throws Exception {
        int databaseSizeBeforeUpdate = attachedFileRepository.findAll().size();
        attachedFile.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachedFileMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(attachedFile))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttachedFile in the database
        List<AttachedFile> attachedFileList = attachedFileRepository.findAll();
        assertThat(attachedFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAttachedFile() throws Exception {
        // Initialize the database
        attachedFileRepository.saveAndFlush(attachedFile);

        int databaseSizeBeforeDelete = attachedFileRepository.findAll().size();

        // Delete the attachedFile
        restAttachedFileMockMvc
            .perform(delete(ENTITY_API_URL_ID, attachedFile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AttachedFile> attachedFileList = attachedFileRepository.findAll();
        assertThat(attachedFileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
