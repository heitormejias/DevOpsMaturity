package br.com.mejias.devopsmaturity.web.rest;

import br.com.mejias.devopsmaturity.DevOpsMaturityApp;
import br.com.mejias.devopsmaturity.domain.Infra;
import br.com.mejias.devopsmaturity.repository.InfraRepository;
import br.com.mejias.devopsmaturity.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static br.com.mejias.devopsmaturity.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link InfraResource} REST controller.
 */
@SpringBootTest(classes = DevOpsMaturityApp.class)
public class InfraResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private InfraRepository infraRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restInfraMockMvc;

    private Infra infra;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InfraResource infraResource = new InfraResource(infraRepository);
        this.restInfraMockMvc = MockMvcBuilders.standaloneSetup(infraResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Infra createEntity(EntityManager em) {
        Infra infra = new Infra()
            .name(DEFAULT_NAME);
        return infra;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Infra createUpdatedEntity(EntityManager em) {
        Infra infra = new Infra()
            .name(UPDATED_NAME);
        return infra;
    }

    @BeforeEach
    public void initTest() {
        infra = createEntity(em);
    }

    @Test
    @Transactional
    public void createInfra() throws Exception {
        int databaseSizeBeforeCreate = infraRepository.findAll().size();

        // Create the Infra
        restInfraMockMvc.perform(post("/api/infras")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(infra)))
            .andExpect(status().isCreated());

        // Validate the Infra in the database
        List<Infra> infraList = infraRepository.findAll();
        assertThat(infraList).hasSize(databaseSizeBeforeCreate + 1);
        Infra testInfra = infraList.get(infraList.size() - 1);
        assertThat(testInfra.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createInfraWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = infraRepository.findAll().size();

        // Create the Infra with an existing ID
        infra.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInfraMockMvc.perform(post("/api/infras")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(infra)))
            .andExpect(status().isBadRequest());

        // Validate the Infra in the database
        List<Infra> infraList = infraRepository.findAll();
        assertThat(infraList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllInfras() throws Exception {
        // Initialize the database
        infraRepository.saveAndFlush(infra);

        // Get all the infraList
        restInfraMockMvc.perform(get("/api/infras?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(infra.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getInfra() throws Exception {
        // Initialize the database
        infraRepository.saveAndFlush(infra);

        // Get the infra
        restInfraMockMvc.perform(get("/api/infras/{id}", infra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(infra.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingInfra() throws Exception {
        // Get the infra
        restInfraMockMvc.perform(get("/api/infras/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInfra() throws Exception {
        // Initialize the database
        infraRepository.saveAndFlush(infra);

        int databaseSizeBeforeUpdate = infraRepository.findAll().size();

        // Update the infra
        Infra updatedInfra = infraRepository.findById(infra.getId()).get();
        // Disconnect from session so that the updates on updatedInfra are not directly saved in db
        em.detach(updatedInfra);
        updatedInfra
            .name(UPDATED_NAME);

        restInfraMockMvc.perform(put("/api/infras")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedInfra)))
            .andExpect(status().isOk());

        // Validate the Infra in the database
        List<Infra> infraList = infraRepository.findAll();
        assertThat(infraList).hasSize(databaseSizeBeforeUpdate);
        Infra testInfra = infraList.get(infraList.size() - 1);
        assertThat(testInfra.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingInfra() throws Exception {
        int databaseSizeBeforeUpdate = infraRepository.findAll().size();

        // Create the Infra

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInfraMockMvc.perform(put("/api/infras")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(infra)))
            .andExpect(status().isBadRequest());

        // Validate the Infra in the database
        List<Infra> infraList = infraRepository.findAll();
        assertThat(infraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInfra() throws Exception {
        // Initialize the database
        infraRepository.saveAndFlush(infra);

        int databaseSizeBeforeDelete = infraRepository.findAll().size();

        // Delete the infra
        restInfraMockMvc.perform(delete("/api/infras/{id}", infra.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Infra> infraList = infraRepository.findAll();
        assertThat(infraList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
