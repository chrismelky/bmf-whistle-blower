package tz.or.mkapafoundation.whistleblower.web.rest;

import tz.or.mkapafoundation.whistleblower.WhistleBlowerApp;
import tz.or.mkapafoundation.whistleblower.config.TestSecurityConfiguration;
import tz.or.mkapafoundation.whistleblower.domain.Suspect;
import tz.or.mkapafoundation.whistleblower.domain.Complain;
import tz.or.mkapafoundation.whistleblower.repository.SuspectRepository;
import tz.or.mkapafoundation.whistleblower.service.SuspectService;
import tz.or.mkapafoundation.whistleblower.service.dto.SuspectDTO;
import tz.or.mkapafoundation.whistleblower.service.mapper.SuspectMapper;

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
 * Integration tests for the {@link SuspectResource} REST controller.
 */
@SpringBootTest(classes = { WhistleBlowerApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class SuspectResourceIT {

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
    private SuspectRepository suspectRepository;

    @Autowired
    private SuspectMapper suspectMapper;

    @Autowired
    private SuspectService suspectService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSuspectMockMvc;

    private Suspect suspect;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Suspect createEntity(EntityManager em) {
        Suspect suspect = new Suspect()
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
        suspect.setComplain(complain);
        return suspect;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Suspect createUpdatedEntity(EntityManager em) {
        Suspect suspect = new Suspect()
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
        suspect.setComplain(complain);
        return suspect;
    }

    @BeforeEach
    public void initTest() {
        suspect = createEntity(em);
    }

    @Test
    @Transactional
    public void createSuspect() throws Exception {
        int databaseSizeBeforeCreate = suspectRepository.findAll().size();
        // Create the Suspect
        SuspectDTO suspectDTO = suspectMapper.toDto(suspect);
        restSuspectMockMvc.perform(post("/api/suspects").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(suspectDTO)))
            .andExpect(status().isCreated());

        // Validate the Suspect in the database
        List<Suspect> suspectList = suspectRepository.findAll();
        assertThat(suspectList).hasSize(databaseSizeBeforeCreate + 1);
        Suspect testSuspect = suspectList.get(suspectList.size() - 1);
        assertThat(testSuspect.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSuspect.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testSuspect.getOrganisation()).isEqualTo(DEFAULT_ORGANISATION);
        assertThat(testSuspect.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testSuspect.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void createSuspectWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = suspectRepository.findAll().size();

        // Create the Suspect with an existing ID
        suspect.setId(1L);
        SuspectDTO suspectDTO = suspectMapper.toDto(suspect);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSuspectMockMvc.perform(post("/api/suspects").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(suspectDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Suspect in the database
        List<Suspect> suspectList = suspectRepository.findAll();
        assertThat(suspectList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = suspectRepository.findAll().size();
        // set the field null
        suspect.setName(null);

        // Create the Suspect, which fails.
        SuspectDTO suspectDTO = suspectMapper.toDto(suspect);


        restSuspectMockMvc.perform(post("/api/suspects").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(suspectDTO)))
            .andExpect(status().isBadRequest());

        List<Suspect> suspectList = suspectRepository.findAll();
        assertThat(suspectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSuspects() throws Exception {
        // Initialize the database
        suspectRepository.saveAndFlush(suspect);

        // Get all the suspectList
        restSuspectMockMvc.perform(get("/api/suspects?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(suspect.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].organisation").value(hasItem(DEFAULT_ORGANISATION)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }
    
    @Test
    @Transactional
    public void getSuspect() throws Exception {
        // Initialize the database
        suspectRepository.saveAndFlush(suspect);

        // Get the suspect
        restSuspectMockMvc.perform(get("/api/suspects/{id}", suspect.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(suspect.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION))
            .andExpect(jsonPath("$.organisation").value(DEFAULT_ORGANISATION))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }
    @Test
    @Transactional
    public void getNonExistingSuspect() throws Exception {
        // Get the suspect
        restSuspectMockMvc.perform(get("/api/suspects/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSuspect() throws Exception {
        // Initialize the database
        suspectRepository.saveAndFlush(suspect);

        int databaseSizeBeforeUpdate = suspectRepository.findAll().size();

        // Update the suspect
        Suspect updatedSuspect = suspectRepository.findById(suspect.getId()).get();
        // Disconnect from session so that the updates on updatedSuspect are not directly saved in db
        em.detach(updatedSuspect);
        updatedSuspect
            .name(UPDATED_NAME)
            .position(UPDATED_POSITION)
            .organisation(UPDATED_ORGANISATION)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        SuspectDTO suspectDTO = suspectMapper.toDto(updatedSuspect);

        restSuspectMockMvc.perform(put("/api/suspects").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(suspectDTO)))
            .andExpect(status().isOk());

        // Validate the Suspect in the database
        List<Suspect> suspectList = suspectRepository.findAll();
        assertThat(suspectList).hasSize(databaseSizeBeforeUpdate);
        Suspect testSuspect = suspectList.get(suspectList.size() - 1);
        assertThat(testSuspect.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSuspect.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testSuspect.getOrganisation()).isEqualTo(UPDATED_ORGANISATION);
        assertThat(testSuspect.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSuspect.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingSuspect() throws Exception {
        int databaseSizeBeforeUpdate = suspectRepository.findAll().size();

        // Create the Suspect
        SuspectDTO suspectDTO = suspectMapper.toDto(suspect);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuspectMockMvc.perform(put("/api/suspects").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(suspectDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Suspect in the database
        List<Suspect> suspectList = suspectRepository.findAll();
        assertThat(suspectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSuspect() throws Exception {
        // Initialize the database
        suspectRepository.saveAndFlush(suspect);

        int databaseSizeBeforeDelete = suspectRepository.findAll().size();

        // Delete the suspect
        restSuspectMockMvc.perform(delete("/api/suspects/{id}", suspect.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Suspect> suspectList = suspectRepository.findAll();
        assertThat(suspectList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
