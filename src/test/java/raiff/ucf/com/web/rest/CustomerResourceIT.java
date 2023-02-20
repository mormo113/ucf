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
import raiff.ucf.com.domain.Customer;
import raiff.ucf.com.repository.CustomerRepository;

/**
 * Integration tests for the {@link CustomerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerResourceIT {

    private static final String DEFAULT_IDUN = "AAAAAAAAAA";
    private static final String UPDATED_IDUN = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME_RNET = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME_RNET = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME_RNET = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME_RNET = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME_PID = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME_PID = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME_PID = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME_PID = "BBBBBBBBBB";

    private static final String DEFAULT_NATIONALITY = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerMockMvc;

    private Customer customer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createEntity(EntityManager em) {
        Customer customer = new Customer()
            .idun(DEFAULT_IDUN)
            .firstNameRNET(DEFAULT_FIRST_NAME_RNET)
            .lastNameRNET(DEFAULT_LAST_NAME_RNET)
            .firstNamePID(DEFAULT_FIRST_NAME_PID)
            .lastNamePID(DEFAULT_LAST_NAME_PID)
            .nationality(DEFAULT_NATIONALITY);
        return customer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createUpdatedEntity(EntityManager em) {
        Customer customer = new Customer()
            .idun(UPDATED_IDUN)
            .firstNameRNET(UPDATED_FIRST_NAME_RNET)
            .lastNameRNET(UPDATED_LAST_NAME_RNET)
            .firstNamePID(UPDATED_FIRST_NAME_PID)
            .lastNamePID(UPDATED_LAST_NAME_PID)
            .nationality(UPDATED_NATIONALITY);
        return customer;
    }

    @BeforeEach
    public void initTest() {
        customer = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();
        // Create the Customer
        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isCreated());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getIdun()).isEqualTo(DEFAULT_IDUN);
        assertThat(testCustomer.getFirstNameRNET()).isEqualTo(DEFAULT_FIRST_NAME_RNET);
        assertThat(testCustomer.getLastNameRNET()).isEqualTo(DEFAULT_LAST_NAME_RNET);
        assertThat(testCustomer.getFirstNamePID()).isEqualTo(DEFAULT_FIRST_NAME_PID);
        assertThat(testCustomer.getLastNamePID()).isEqualTo(DEFAULT_LAST_NAME_PID);
        assertThat(testCustomer.getNationality()).isEqualTo(DEFAULT_NATIONALITY);
    }

    @Test
    @Transactional
    void createCustomerWithExistingId() throws Exception {
        // Create the Customer with an existing ID
        customer.setId(1L);

        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCustomers() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].idun").value(hasItem(DEFAULT_IDUN)))
            .andExpect(jsonPath("$.[*].firstNameRNET").value(hasItem(DEFAULT_FIRST_NAME_RNET)))
            .andExpect(jsonPath("$.[*].lastNameRNET").value(hasItem(DEFAULT_LAST_NAME_RNET)))
            .andExpect(jsonPath("$.[*].firstNamePID").value(hasItem(DEFAULT_FIRST_NAME_PID)))
            .andExpect(jsonPath("$.[*].lastNamePID").value(hasItem(DEFAULT_LAST_NAME_PID)))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY)));
    }

    @Test
    @Transactional
    void getCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get the customer
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL_ID, customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customer.getId().intValue()))
            .andExpect(jsonPath("$.idun").value(DEFAULT_IDUN))
            .andExpect(jsonPath("$.firstNameRNET").value(DEFAULT_FIRST_NAME_RNET))
            .andExpect(jsonPath("$.lastNameRNET").value(DEFAULT_LAST_NAME_RNET))
            .andExpect(jsonPath("$.firstNamePID").value(DEFAULT_FIRST_NAME_PID))
            .andExpect(jsonPath("$.lastNamePID").value(DEFAULT_LAST_NAME_PID))
            .andExpect(jsonPath("$.nationality").value(DEFAULT_NATIONALITY));
    }

    @Test
    @Transactional
    void getNonExistingCustomer() throws Exception {
        // Get the customer
        restCustomerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer
        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        // Disconnect from session so that the updates on updatedCustomer are not directly saved in db
        em.detach(updatedCustomer);
        updatedCustomer
            .idun(UPDATED_IDUN)
            .firstNameRNET(UPDATED_FIRST_NAME_RNET)
            .lastNameRNET(UPDATED_LAST_NAME_RNET)
            .firstNamePID(UPDATED_FIRST_NAME_PID)
            .lastNamePID(UPDATED_LAST_NAME_PID)
            .nationality(UPDATED_NATIONALITY);

        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCustomer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCustomer))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getIdun()).isEqualTo(UPDATED_IDUN);
        assertThat(testCustomer.getFirstNameRNET()).isEqualTo(UPDATED_FIRST_NAME_RNET);
        assertThat(testCustomer.getLastNameRNET()).isEqualTo(UPDATED_LAST_NAME_RNET);
        assertThat(testCustomer.getFirstNamePID()).isEqualTo(UPDATED_FIRST_NAME_PID);
        assertThat(testCustomer.getLastNamePID()).isEqualTo(UPDATED_LAST_NAME_PID);
        assertThat(testCustomer.getNationality()).isEqualTo(UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void putNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerWithPatch() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer using partial update
        Customer partialUpdatedCustomer = new Customer();
        partialUpdatedCustomer.setId(customer.getId());

        partialUpdatedCustomer.idun(UPDATED_IDUN).lastNamePID(UPDATED_LAST_NAME_PID);

        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomer))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getIdun()).isEqualTo(UPDATED_IDUN);
        assertThat(testCustomer.getFirstNameRNET()).isEqualTo(DEFAULT_FIRST_NAME_RNET);
        assertThat(testCustomer.getLastNameRNET()).isEqualTo(DEFAULT_LAST_NAME_RNET);
        assertThat(testCustomer.getFirstNamePID()).isEqualTo(DEFAULT_FIRST_NAME_PID);
        assertThat(testCustomer.getLastNamePID()).isEqualTo(UPDATED_LAST_NAME_PID);
        assertThat(testCustomer.getNationality()).isEqualTo(DEFAULT_NATIONALITY);
    }

    @Test
    @Transactional
    void fullUpdateCustomerWithPatch() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer using partial update
        Customer partialUpdatedCustomer = new Customer();
        partialUpdatedCustomer.setId(customer.getId());

        partialUpdatedCustomer
            .idun(UPDATED_IDUN)
            .firstNameRNET(UPDATED_FIRST_NAME_RNET)
            .lastNameRNET(UPDATED_LAST_NAME_RNET)
            .firstNamePID(UPDATED_FIRST_NAME_PID)
            .lastNamePID(UPDATED_LAST_NAME_PID)
            .nationality(UPDATED_NATIONALITY);

        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomer))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getIdun()).isEqualTo(UPDATED_IDUN);
        assertThat(testCustomer.getFirstNameRNET()).isEqualTo(UPDATED_FIRST_NAME_RNET);
        assertThat(testCustomer.getLastNameRNET()).isEqualTo(UPDATED_LAST_NAME_RNET);
        assertThat(testCustomer.getFirstNamePID()).isEqualTo(UPDATED_FIRST_NAME_PID);
        assertThat(testCustomer.getLastNamePID()).isEqualTo(UPDATED_LAST_NAME_PID);
        assertThat(testCustomer.getNationality()).isEqualTo(UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void patchNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeDelete = customerRepository.findAll().size();

        // Delete the customer
        restCustomerMockMvc
            .perform(delete(ENTITY_API_URL_ID, customer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
