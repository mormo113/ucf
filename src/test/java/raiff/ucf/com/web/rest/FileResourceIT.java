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
import raiff.ucf.com.domain.File;
import raiff.ucf.com.domain.enumeration.TypeUC;
import raiff.ucf.com.repository.FileRepository;

/**
 * Integration tests for the {@link FileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FileResourceIT {

    private static final String DEFAULT_PID_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PID_NUMBER = "BBBBBBBBBB";

    private static final TypeUC DEFAULT_TYPE = TypeUC.UC1;
    private static final TypeUC UPDATED_TYPE = TypeUC.UC2;

    private static final String DEFAULT_DOCUMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DEMANDE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DEMANDE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE_RNET_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE_RNET_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_COMMUNICATION = "AAAAAAAAAA";
    private static final String UPDATED_COMMUNICATION = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_TTC_AMOUNT = "AAAAAAAAAA";
    private static final String UPDATED_TTC_AMOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_BIC = "AAAAAAAAAA";
    private static final String UPDATED_BIC = "BBBBBBBBBB";

    private static final String DEFAULT_IBAN = "AAAAAAAAAA";
    private static final String UPDATED_IBAN = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_RECEPTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RECEPTION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_EMISSION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EMISSION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_EXPIRY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_MODIFICATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFICATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_STORE_GED = false;
    private static final Boolean UPDATED_STORE_GED = true;

    private static final String ENTITY_API_URL = "/api/files";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFileMockMvc;

    private File file;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static File createEntity(EntityManager em) {
        File file = new File()
            .pidNumber(DEFAULT_PID_NUMBER)
            .type(DEFAULT_TYPE)
            .documentType(DEFAULT_DOCUMENT_TYPE)
            .demandeType(DEFAULT_DEMANDE_TYPE)
            .reference(DEFAULT_REFERENCE)
            .messageRnetTitle(DEFAULT_MESSAGE_RNET_TITLE)
            .communication(DEFAULT_COMMUNICATION)
            .clientMessage(DEFAULT_CLIENT_MESSAGE)
            .ttcAmount(DEFAULT_TTC_AMOUNT)
            .bic(DEFAULT_BIC)
            .iban(DEFAULT_IBAN)
            .receptionDate(DEFAULT_RECEPTION_DATE)
            .emissionDate(DEFAULT_EMISSION_DATE)
            .expiryDate(DEFAULT_EXPIRY_DATE)
            .creationDate(DEFAULT_CREATION_DATE)
            .modificationDate(DEFAULT_MODIFICATION_DATE)
            .storeGed(DEFAULT_STORE_GED);
        return file;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static File createUpdatedEntity(EntityManager em) {
        File file = new File()
            .pidNumber(UPDATED_PID_NUMBER)
            .type(UPDATED_TYPE)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .demandeType(UPDATED_DEMANDE_TYPE)
            .reference(UPDATED_REFERENCE)
            .messageRnetTitle(UPDATED_MESSAGE_RNET_TITLE)
            .communication(UPDATED_COMMUNICATION)
            .clientMessage(UPDATED_CLIENT_MESSAGE)
            .ttcAmount(UPDATED_TTC_AMOUNT)
            .bic(UPDATED_BIC)
            .iban(UPDATED_IBAN)
            .receptionDate(UPDATED_RECEPTION_DATE)
            .emissionDate(UPDATED_EMISSION_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .creationDate(UPDATED_CREATION_DATE)
            .modificationDate(UPDATED_MODIFICATION_DATE)
            .storeGed(UPDATED_STORE_GED);
        return file;
    }

    @BeforeEach
    public void initTest() {
        file = createEntity(em);
    }

    @Test
    @Transactional
    void createFile() throws Exception {
        int databaseSizeBeforeCreate = fileRepository.findAll().size();
        // Create the File
        restFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(file)))
            .andExpect(status().isCreated());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeCreate + 1);
        File testFile = fileList.get(fileList.size() - 1);
        assertThat(testFile.getPidNumber()).isEqualTo(DEFAULT_PID_NUMBER);
        assertThat(testFile.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testFile.getDocumentType()).isEqualTo(DEFAULT_DOCUMENT_TYPE);
        assertThat(testFile.getDemandeType()).isEqualTo(DEFAULT_DEMANDE_TYPE);
        assertThat(testFile.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testFile.getMessageRnetTitle()).isEqualTo(DEFAULT_MESSAGE_RNET_TITLE);
        assertThat(testFile.getCommunication()).isEqualTo(DEFAULT_COMMUNICATION);
        assertThat(testFile.getClientMessage()).isEqualTo(DEFAULT_CLIENT_MESSAGE);
        assertThat(testFile.getTtcAmount()).isEqualTo(DEFAULT_TTC_AMOUNT);
        assertThat(testFile.getBic()).isEqualTo(DEFAULT_BIC);
        assertThat(testFile.getIban()).isEqualTo(DEFAULT_IBAN);
        assertThat(testFile.getReceptionDate()).isEqualTo(DEFAULT_RECEPTION_DATE);
        assertThat(testFile.getEmissionDate()).isEqualTo(DEFAULT_EMISSION_DATE);
        assertThat(testFile.getExpiryDate()).isEqualTo(DEFAULT_EXPIRY_DATE);
        assertThat(testFile.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testFile.getModificationDate()).isEqualTo(DEFAULT_MODIFICATION_DATE);
        assertThat(testFile.getStoreGed()).isEqualTo(DEFAULT_STORE_GED);
    }

    @Test
    @Transactional
    void createFileWithExistingId() throws Exception {
        // Create the File with an existing ID
        file.setId(1L);

        int databaseSizeBeforeCreate = fileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(file)))
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFiles() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        // Get all the fileList
        restFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(file.getId().intValue())))
            .andExpect(jsonPath("$.[*].pidNumber").value(hasItem(DEFAULT_PID_NUMBER)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE)))
            .andExpect(jsonPath("$.[*].demandeType").value(hasItem(DEFAULT_DEMANDE_TYPE)))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].messageRnetTitle").value(hasItem(DEFAULT_MESSAGE_RNET_TITLE)))
            .andExpect(jsonPath("$.[*].communication").value(hasItem(DEFAULT_COMMUNICATION)))
            .andExpect(jsonPath("$.[*].clientMessage").value(hasItem(DEFAULT_CLIENT_MESSAGE)))
            .andExpect(jsonPath("$.[*].ttcAmount").value(hasItem(DEFAULT_TTC_AMOUNT)))
            .andExpect(jsonPath("$.[*].bic").value(hasItem(DEFAULT_BIC)))
            .andExpect(jsonPath("$.[*].iban").value(hasItem(DEFAULT_IBAN)))
            .andExpect(jsonPath("$.[*].receptionDate").value(hasItem(DEFAULT_RECEPTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].emissionDate").value(hasItem(DEFAULT_EMISSION_DATE.toString())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modificationDate").value(hasItem(DEFAULT_MODIFICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].storeGed").value(hasItem(DEFAULT_STORE_GED.booleanValue())));
    }

    @Test
    @Transactional
    void getFile() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        // Get the file
        restFileMockMvc
            .perform(get(ENTITY_API_URL_ID, file.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(file.getId().intValue()))
            .andExpect(jsonPath("$.pidNumber").value(DEFAULT_PID_NUMBER))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.documentType").value(DEFAULT_DOCUMENT_TYPE))
            .andExpect(jsonPath("$.demandeType").value(DEFAULT_DEMANDE_TYPE))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE))
            .andExpect(jsonPath("$.messageRnetTitle").value(DEFAULT_MESSAGE_RNET_TITLE))
            .andExpect(jsonPath("$.communication").value(DEFAULT_COMMUNICATION))
            .andExpect(jsonPath("$.clientMessage").value(DEFAULT_CLIENT_MESSAGE))
            .andExpect(jsonPath("$.ttcAmount").value(DEFAULT_TTC_AMOUNT))
            .andExpect(jsonPath("$.bic").value(DEFAULT_BIC))
            .andExpect(jsonPath("$.iban").value(DEFAULT_IBAN))
            .andExpect(jsonPath("$.receptionDate").value(DEFAULT_RECEPTION_DATE.toString()))
            .andExpect(jsonPath("$.emissionDate").value(DEFAULT_EMISSION_DATE.toString()))
            .andExpect(jsonPath("$.expiryDate").value(DEFAULT_EXPIRY_DATE.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.modificationDate").value(DEFAULT_MODIFICATION_DATE.toString()))
            .andExpect(jsonPath("$.storeGed").value(DEFAULT_STORE_GED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingFile() throws Exception {
        // Get the file
        restFileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFile() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        int databaseSizeBeforeUpdate = fileRepository.findAll().size();

        // Update the file
        File updatedFile = fileRepository.findById(file.getId()).get();
        // Disconnect from session so that the updates on updatedFile are not directly saved in db
        em.detach(updatedFile);
        updatedFile
            .pidNumber(UPDATED_PID_NUMBER)
            .type(UPDATED_TYPE)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .demandeType(UPDATED_DEMANDE_TYPE)
            .reference(UPDATED_REFERENCE)
            .messageRnetTitle(UPDATED_MESSAGE_RNET_TITLE)
            .communication(UPDATED_COMMUNICATION)
            .clientMessage(UPDATED_CLIENT_MESSAGE)
            .ttcAmount(UPDATED_TTC_AMOUNT)
            .bic(UPDATED_BIC)
            .iban(UPDATED_IBAN)
            .receptionDate(UPDATED_RECEPTION_DATE)
            .emissionDate(UPDATED_EMISSION_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .creationDate(UPDATED_CREATION_DATE)
            .modificationDate(UPDATED_MODIFICATION_DATE)
            .storeGed(UPDATED_STORE_GED);

        restFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFile.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFile))
            )
            .andExpect(status().isOk());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
        File testFile = fileList.get(fileList.size() - 1);
        assertThat(testFile.getPidNumber()).isEqualTo(UPDATED_PID_NUMBER);
        assertThat(testFile.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testFile.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
        assertThat(testFile.getDemandeType()).isEqualTo(UPDATED_DEMANDE_TYPE);
        assertThat(testFile.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testFile.getMessageRnetTitle()).isEqualTo(UPDATED_MESSAGE_RNET_TITLE);
        assertThat(testFile.getCommunication()).isEqualTo(UPDATED_COMMUNICATION);
        assertThat(testFile.getClientMessage()).isEqualTo(UPDATED_CLIENT_MESSAGE);
        assertThat(testFile.getTtcAmount()).isEqualTo(UPDATED_TTC_AMOUNT);
        assertThat(testFile.getBic()).isEqualTo(UPDATED_BIC);
        assertThat(testFile.getIban()).isEqualTo(UPDATED_IBAN);
        assertThat(testFile.getReceptionDate()).isEqualTo(UPDATED_RECEPTION_DATE);
        assertThat(testFile.getEmissionDate()).isEqualTo(UPDATED_EMISSION_DATE);
        assertThat(testFile.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
        assertThat(testFile.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testFile.getModificationDate()).isEqualTo(UPDATED_MODIFICATION_DATE);
        assertThat(testFile.getStoreGed()).isEqualTo(UPDATED_STORE_GED);
    }

    @Test
    @Transactional
    void putNonExistingFile() throws Exception {
        int databaseSizeBeforeUpdate = fileRepository.findAll().size();
        file.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, file.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(file))
            )
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFile() throws Exception {
        int databaseSizeBeforeUpdate = fileRepository.findAll().size();
        file.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(file))
            )
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFile() throws Exception {
        int databaseSizeBeforeUpdate = fileRepository.findAll().size();
        file.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(file)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFileWithPatch() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        int databaseSizeBeforeUpdate = fileRepository.findAll().size();

        // Update the file using partial update
        File partialUpdatedFile = new File();
        partialUpdatedFile.setId(file.getId());

        partialUpdatedFile
            .pidNumber(UPDATED_PID_NUMBER)
            .messageRnetTitle(UPDATED_MESSAGE_RNET_TITLE)
            .communication(UPDATED_COMMUNICATION)
            .bic(UPDATED_BIC)
            .iban(UPDATED_IBAN)
            .modificationDate(UPDATED_MODIFICATION_DATE);

        restFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFile))
            )
            .andExpect(status().isOk());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
        File testFile = fileList.get(fileList.size() - 1);
        assertThat(testFile.getPidNumber()).isEqualTo(UPDATED_PID_NUMBER);
        assertThat(testFile.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testFile.getDocumentType()).isEqualTo(DEFAULT_DOCUMENT_TYPE);
        assertThat(testFile.getDemandeType()).isEqualTo(DEFAULT_DEMANDE_TYPE);
        assertThat(testFile.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testFile.getMessageRnetTitle()).isEqualTo(UPDATED_MESSAGE_RNET_TITLE);
        assertThat(testFile.getCommunication()).isEqualTo(UPDATED_COMMUNICATION);
        assertThat(testFile.getClientMessage()).isEqualTo(DEFAULT_CLIENT_MESSAGE);
        assertThat(testFile.getTtcAmount()).isEqualTo(DEFAULT_TTC_AMOUNT);
        assertThat(testFile.getBic()).isEqualTo(UPDATED_BIC);
        assertThat(testFile.getIban()).isEqualTo(UPDATED_IBAN);
        assertThat(testFile.getReceptionDate()).isEqualTo(DEFAULT_RECEPTION_DATE);
        assertThat(testFile.getEmissionDate()).isEqualTo(DEFAULT_EMISSION_DATE);
        assertThat(testFile.getExpiryDate()).isEqualTo(DEFAULT_EXPIRY_DATE);
        assertThat(testFile.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testFile.getModificationDate()).isEqualTo(UPDATED_MODIFICATION_DATE);
        assertThat(testFile.getStoreGed()).isEqualTo(DEFAULT_STORE_GED);
    }

    @Test
    @Transactional
    void fullUpdateFileWithPatch() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        int databaseSizeBeforeUpdate = fileRepository.findAll().size();

        // Update the file using partial update
        File partialUpdatedFile = new File();
        partialUpdatedFile.setId(file.getId());

        partialUpdatedFile
            .pidNumber(UPDATED_PID_NUMBER)
            .type(UPDATED_TYPE)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .demandeType(UPDATED_DEMANDE_TYPE)
            .reference(UPDATED_REFERENCE)
            .messageRnetTitle(UPDATED_MESSAGE_RNET_TITLE)
            .communication(UPDATED_COMMUNICATION)
            .clientMessage(UPDATED_CLIENT_MESSAGE)
            .ttcAmount(UPDATED_TTC_AMOUNT)
            .bic(UPDATED_BIC)
            .iban(UPDATED_IBAN)
            .receptionDate(UPDATED_RECEPTION_DATE)
            .emissionDate(UPDATED_EMISSION_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .creationDate(UPDATED_CREATION_DATE)
            .modificationDate(UPDATED_MODIFICATION_DATE)
            .storeGed(UPDATED_STORE_GED);

        restFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFile))
            )
            .andExpect(status().isOk());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
        File testFile = fileList.get(fileList.size() - 1);
        assertThat(testFile.getPidNumber()).isEqualTo(UPDATED_PID_NUMBER);
        assertThat(testFile.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testFile.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
        assertThat(testFile.getDemandeType()).isEqualTo(UPDATED_DEMANDE_TYPE);
        assertThat(testFile.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testFile.getMessageRnetTitle()).isEqualTo(UPDATED_MESSAGE_RNET_TITLE);
        assertThat(testFile.getCommunication()).isEqualTo(UPDATED_COMMUNICATION);
        assertThat(testFile.getClientMessage()).isEqualTo(UPDATED_CLIENT_MESSAGE);
        assertThat(testFile.getTtcAmount()).isEqualTo(UPDATED_TTC_AMOUNT);
        assertThat(testFile.getBic()).isEqualTo(UPDATED_BIC);
        assertThat(testFile.getIban()).isEqualTo(UPDATED_IBAN);
        assertThat(testFile.getReceptionDate()).isEqualTo(UPDATED_RECEPTION_DATE);
        assertThat(testFile.getEmissionDate()).isEqualTo(UPDATED_EMISSION_DATE);
        assertThat(testFile.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
        assertThat(testFile.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testFile.getModificationDate()).isEqualTo(UPDATED_MODIFICATION_DATE);
        assertThat(testFile.getStoreGed()).isEqualTo(UPDATED_STORE_GED);
    }

    @Test
    @Transactional
    void patchNonExistingFile() throws Exception {
        int databaseSizeBeforeUpdate = fileRepository.findAll().size();
        file.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, file.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(file))
            )
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFile() throws Exception {
        int databaseSizeBeforeUpdate = fileRepository.findAll().size();
        file.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(file))
            )
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFile() throws Exception {
        int databaseSizeBeforeUpdate = fileRepository.findAll().size();
        file.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(file)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFile() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        int databaseSizeBeforeDelete = fileRepository.findAll().size();

        // Delete the file
        restFileMockMvc
            .perform(delete(ENTITY_API_URL_ID, file.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
