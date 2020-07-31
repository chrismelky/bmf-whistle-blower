package tz.or.mkapafoundation.whistleblower.web.rest;

import tz.or.mkapafoundation.whistleblower.WhistleBlowerApp;
import tz.or.mkapafoundation.whistleblower.config.TestSecurityConfiguration;
import tz.or.mkapafoundation.whistleblower.domain.Witness;
import tz.or.mkapafoundation.whistleblower.domain.Complain;
import tz.or.mkapafoundation.whistleblower.repository.WitnessRepository;
import tz.or.mkapafoundation.whistleblower.service.WitnessService;
import tz.or.mkapafoundation.whistleblower.service.dto.WitnessDTO;
import tz.or.mkapafoundation.whistleblower.service.mapper.WitnessMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link WitnessResource} REST controller.
 */
@SpringBootTest(classes = { WhistleBlowerApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class WitnessResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final String DEFAULT_ORGANISATION = "AAAAAAAAAA";
    private static final String UPDATED_ORGANISATION = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    @Autowired
    private WitnessRepository witnessRepository;

    @Autowired
    private WitnessMapper witnessMapper;

    @Autowired
    private WitnessService witnessService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWitnessMockMvc;

    private Witness witness;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Witness createEntity(EntityManager em) {
        Witness witness = new Witness()
            .name(DEFAULT_NAME)
            .position(DEFAULT_POSITION)
            .organisation(DEFAULT_ORGANISATION)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER);
        // Add required entity
        Complain complain;
        if (TestUtil.findAll(em, Complain.class).isEmpty()) {
            complain = ComplainResourceIT.createEntity(em);
            em.persist(complain);
            em.flush();
        } else {
            complain = TestUtil.findAll(em, Complain.class).get(0);
        }
        witness.setComplain(complain);
        return witness;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Witness createUpdatedEntity(EntityManager em) {
        Witness witness = new Witness()
            .name(UPDATED_NAME)
            .position(UPDATED_POSITION)
            .organisation(UPDATED_ORGANISATION)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        // Add required entity
        Complain complain;
        if (TestUtil.findAll(em, Complain.class).isEmpty()) {
            complain = ComplainResourceIT.createUpdatedEntity(em);
            em.persist(complain);
            em.flush();
        } else {
            complain = TestUtil.findAll(em, Complain.class).get(0);
        }
        witness.setComplain(complain);
        return witness;
    }

    @BeforeEach
    public void initTest() {
        witness = createEntity(em);
    }

    @Test
    @Transactional
    public void createWitness() throws Exception {
        int databaseSizeBeforeCreate = witnessRepository.findAll().size();
        // Create the Witness
        WitnessDTO witnessDTO = witnessMapper.toDto(witness);
        restWitnessMockMvc.perform(post("/api/witnesses").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(witnessDTO)))
            .andExpect(status().isCreated());

        // Validate the Witness in the database
        List<Witness> witnessList = witnessRepository.findAll();
        assertThat(witnessList).hasSize(databaseSizeBeforeCreate + 1);
        Witness testWitness = witnessList.get(witnessList.size() - 1);
        assertThat(testWitness.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWitness.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testWitness.getOrganisation()).isEqualTo(DEFAULT_ORGANISATION);
        assertThat(testWitness.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testWitness.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void createWitnessWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = witnessRepository.findAll().size();

        // Create the Witness with an existing ID
        witness.setId(1L);
        WitnessDTO witnessDTO = witnessMapper.toDto(witness);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWitnessMockMvc.perform(post("/api/witnesses").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(witnessDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Witness in the database
        List<Witness> witnessList = witnessRepository.findAll();
        assertThat(witnessList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = witnessRepository.findAll().size();
        // set the field null
        witness.setName(null);

        // Create the Witness, which fails.
        WitnessDTO witnessDTO = witnessMapper.toDto(witness);


        restWitnessMockMvc.perform(post("/api/witnesses").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(witnessDTO)))
            .andExpect(status().isBadRequest());

        List<Witness> witnessList = witnessRepository.findAll();
        assertThat(witnessList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWitnesses() throws Exception {
        // Initialize the database
        witnessRepository.saveAndFlush(witness);

        // Get all the witnessList
        restWitnessMockMvc.perform(get("/api/witnesses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(witness.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].organisation").value(hasItem(DEFAULT_ORGANISATION)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }
    
    @Test
    @Transactional
    public void getWitness() throws Exception {
        // Initialize the database
        witnessRepository.saveAndFlush(witness);

        // Get the witness
        restWitnessMockMvc.perform(get("/api/witnesses/{id}", witness.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(witness.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION))
            .andExpect(jsonPath("$.organisation").value(DEFAULT_ORGANISATION))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }
    @Test
    @Transactional
    public void getNonExistingWitness() throws Exception {
        // Get the witness
        restWitnessMockMvc.perform(get("/api/witnesses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWitness() throws Exception {
        // Initialize the database
        witnessRepository.saveAndFlush(witness);

        int databaseSizeBeforeUpdate = witnessRepository.findAll().size();

        // Update the witness
        Witness updatedWitness = witnessRepository.findById(witness.getId()).get();
        // Disconnect from session so that the updates on updatedWitness are not directly saved in db
        em.detach(updatedWitness);
        updatedWitness
            .name(UPDATED_NAME)
            .position(UPDATED_POSITION)
            .organisation(UPDATED_ORGANISATION)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        WitnessDTO witnessDTO = witnessMapper.toDto(updatedWitness);

        restWitnessMockMvc.perform(put("/api/witnesses").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(witnessDTO)))
            .andExpect(status().isOk());

        // Validate the Witness in the database
        List<Witness> witnessList = witnessRepository.findAll();
        assertThat(witnessList).hasSize(databaseSizeBeforeUpdate);
        Witness testWitness = witnessList.get(witnessList.size() - 1);
        assertThat(testWitness.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWitness.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testWitness.getOrganisation()).isEqualTo(UPDATED_ORGANISATION);
        assertThat(testWitness.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testWitness.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingWitness() throws Exception {
        int databaseSizeBeforeUpdate = witnessRepository.findAll().size();

        // Create the Witness
        WitnessDTO witnessDTO = witnessMapper.toDto(witness);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWitnessMockMvc.perform(put("/api/witnesses").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(witnessDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Witness in the database
        List<Witness> witnessList = witnessRepository.findAll();
        assertThat(witnessList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWitness() throws Exception {
        // Initialize the database
        witnessRepository.saveAndFlush(witness);

        int databaseSizeBeforeDelete = witnessRepository.findAll().size();

        // Delete the witness
        restWitnessMockMvc.perform(delete("/api/witnesses/{id}", witness.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Witness> witnessList = witnessRepository.findAll();
        assertThat(witnessList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
