package br.com.mejias.devopsmaturity.web.rest;

import br.com.mejias.devopsmaturity.DevOpsMaturityApp;
import br.com.mejias.devopsmaturity.domain.Tools;
import br.com.mejias.devopsmaturity.repository.ToolsRepository;
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
 * Integration tests for the {@link ToolsResource} REST controller.
 */
@SpringBootTest(classes = DevOpsMaturityApp.class)
public class ToolsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ToolsRepository toolsRepository;

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

    private MockMvc restToolsMockMvc;

    private Tools tools;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ToolsResource toolsResource = new ToolsResource(toolsRepository);
        this.restToolsMockMvc = MockMvcBuilders.standaloneSetup(toolsResource)
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
    public static Tools createEntity(EntityManager em) {
        Tools tools = new Tools()
            .name(DEFAULT_NAME);
        return tools;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tools createUpdatedEntity(EntityManager em) {
        Tools tools = new Tools()
            .name(UPDATED_NAME);
        return tools;
    }

    @BeforeEach
    public void initTest() {
        tools = createEntity(em);
    }

    @Test
    @Transactional
    public void createTools() throws Exception {
        int databaseSizeBeforeCreate = toolsRepository.findAll().size();

        // Create the Tools
        restToolsMockMvc.perform(post("/api/tools")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tools)))
            .andExpect(status().isCreated());

        // Validate the Tools in the database
        List<Tools> toolsList = toolsRepository.findAll();
        assertThat(toolsList).hasSize(databaseSizeBeforeCreate + 1);
        Tools testTools = toolsList.get(toolsList.size() - 1);
        assertThat(testTools.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createToolsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = toolsRepository.findAll().size();

        // Create the Tools with an existing ID
        tools.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restToolsMockMvc.perform(post("/api/tools")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tools)))
            .andExpect(status().isBadRequest());

        // Validate the Tools in the database
        List<Tools> toolsList = toolsRepository.findAll();
        assertThat(toolsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTools() throws Exception {
        // Initialize the database
        toolsRepository.saveAndFlush(tools);

        // Get all the toolsList
        restToolsMockMvc.perform(get("/api/tools?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tools.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getTools() throws Exception {
        // Initialize the database
        toolsRepository.saveAndFlush(tools);

        // Get the tools
        restToolsMockMvc.perform(get("/api/tools/{id}", tools.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tools.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingTools() throws Exception {
        // Get the tools
        restToolsMockMvc.perform(get("/api/tools/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTools() throws Exception {
        // Initialize the database
        toolsRepository.saveAndFlush(tools);

        int databaseSizeBeforeUpdate = toolsRepository.findAll().size();

        // Update the tools
        Tools updatedTools = toolsRepository.findById(tools.getId()).get();
        // Disconnect from session so that the updates on updatedTools are not directly saved in db
        em.detach(updatedTools);
        updatedTools
            .name(UPDATED_NAME);

        restToolsMockMvc.perform(put("/api/tools")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTools)))
            .andExpect(status().isOk());

        // Validate the Tools in the database
        List<Tools> toolsList = toolsRepository.findAll();
        assertThat(toolsList).hasSize(databaseSizeBeforeUpdate);
        Tools testTools = toolsList.get(toolsList.size() - 1);
        assertThat(testTools.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingTools() throws Exception {
        int databaseSizeBeforeUpdate = toolsRepository.findAll().size();

        // Create the Tools

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restToolsMockMvc.perform(put("/api/tools")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tools)))
            .andExpect(status().isBadRequest());

        // Validate the Tools in the database
        List<Tools> toolsList = toolsRepository.findAll();
        assertThat(toolsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTools() throws Exception {
        // Initialize the database
        toolsRepository.saveAndFlush(tools);

        int databaseSizeBeforeDelete = toolsRepository.findAll().size();

        // Delete the tools
        restToolsMockMvc.perform(delete("/api/tools/{id}", tools.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tools> toolsList = toolsRepository.findAll();
        assertThat(toolsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
