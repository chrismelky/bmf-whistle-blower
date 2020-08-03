package tz.or.mkapafoundation.whistleblower.web.rest;

import tz.or.mkapafoundation.whistleblower.WhistleBlowerApp;
import tz.or.mkapafoundation.whistleblower.config.TestSecurityConfiguration;
import tz.or.mkapafoundation.whistleblower.domain.Complain;
import tz.or.mkapafoundation.whistleblower.domain.Witness;
import tz.or.mkapafoundation.whistleblower.domain.Suspect;
import tz.or.mkapafoundation.whistleblower.domain.Attachment;
import tz.or.mkapafoundation.whistleblower.domain.Category;
import tz.or.mkapafoundation.whistleblower.domain.User;
import tz.or.mkapafoundation.whistleblower.repository.ComplainRepository;
import tz.or.mkapafoundation.whistleblower.service.ComplainService;
import tz.or.mkapafoundation.whistleblower.service.dto.ComplainDTO;
import tz.or.mkapafoundation.whistleblower.service.mapper.ComplainMapper;
import tz.or.mkapafoundation.whistleblower.service.dto.ComplainCriteria;
import tz.or.mkapafoundation.whistleblower.service.ComplainQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ComplainResource} REST controller.
 */
@SpringBootTest(classes = { WhistleBlowerApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ComplainResourceIT {

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

    private static final String DEFAULT_CONTROL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CONTROL_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ComplainRepository complainRepository;

    @Mock
    private ComplainRepository complainRepositoryMock;

    @Autowired
    private ComplainMapper complainMapper;

    @Mock
    private ComplainService complainServiceMock;

    @Autowired
    private ComplainService complainService;

    @Autowired
    private ComplainQueryService complainQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restComplainMockMvc;

    private Complain complain;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Complain createEntity(EntityManager em) {
        Complain complain = new Complain()
            .name(DEFAULT_NAME)
            .position(DEFAULT_POSITION)
            .organisation(DEFAULT_ORGANISATION)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .controlNumber(DEFAULT_CONTROL_NUMBER)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        complain.setCategory(category);
        return complain;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Complain createUpdatedEntity(EntityManager em) {
        Complain complain = new Complain()
            .name(UPDATED_NAME)
            .position(UPDATED_POSITION)
            .organisation(UPDATED_ORGANISATION)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .controlNumber(UPDATED_CONTROL_NUMBER)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createUpdatedEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        complain.setCategory(category);
        return complain;
    }

    @BeforeEach
    public void initTest() {
        complain = createEntity(em);
    }

    // @Test
    // @Transactional
    // public void createComplain() throws Exception {
    //     int databaseSizeBeforeCreate = complainRepository.findAll().size();
    //     // Create the Complain
    //     ComplainDTO complainDTO = complainMapper.toDto(complain);
    //     restComplainMockMvc.perform(post("/api/complains").with(csrf())
    //         .contentType(MediaType.APPLICATION_JSON)
    //         .content(TestUtil.convertObjectToJsonBytes(complainDTO)))
    //         .andExpect(status().isCreated());

    //     // Validate the Complain in the database
    //     List<Complain> complainList = complainRepository.findAll();
    //     assertThat(complainList).hasSize(databaseSizeBeforeCreate + 1);
    //     Complain testComplain = complainList.get(complainList.size() - 1);
    //     assertThat(testComplain.getName()).isEqualTo(DEFAULT_NAME);
    //     assertThat(testComplain.getPosition()).isEqualTo(DEFAULT_POSITION);
    //     assertThat(testComplain.getOrganisation()).isEqualTo(DEFAULT_ORGANISATION);
    //     assertThat(testComplain.getEmail()).isEqualTo(DEFAULT_EMAIL);
    //     assertThat(testComplain.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
    //     assertThat(testComplain.getControlNumber()).isEqualTo(DEFAULT_CONTROL_NUMBER);
    //     assertThat(testComplain.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    // }

    @Test
    @Transactional
    public void createComplainWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = complainRepository.findAll().size();

        // Create the Complain with an existing ID
        complain.setId(1L);
        ComplainDTO complainDTO = complainMapper.toDto(complain);

        // An entity with an existing ID cannot be created, so this API call must fail
        restComplainMockMvc.perform(post("/api/complains").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(complainDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Complain in the database
        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllComplains() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList
        restComplainMockMvc.perform(get("/api/complains?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(complain.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].organisation").value(hasItem(DEFAULT_ORGANISATION)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].controlNumber").value(hasItem(DEFAULT_CONTROL_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllComplainsWithEagerRelationshipsIsEnabled() throws Exception {
        when(complainServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restComplainMockMvc.perform(get("/api/complains?eagerload=true"))
            .andExpect(status().isOk());

        verify(complainServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllComplainsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(complainServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restComplainMockMvc.perform(get("/api/complains?eagerload=true"))
            .andExpect(status().isOk());

        verify(complainServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getComplain() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get the complain
        restComplainMockMvc.perform(get("/api/complains/{id}", complain.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(complain.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION))
            .andExpect(jsonPath("$.organisation").value(DEFAULT_ORGANISATION))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.controlNumber").value(DEFAULT_CONTROL_NUMBER))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }


    @Test
    @Transactional
    public void getComplainsByIdFiltering() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        Long id = complain.getId();

        defaultComplainShouldBeFound("id.equals=" + id);
        defaultComplainShouldNotBeFound("id.notEquals=" + id);

        defaultComplainShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultComplainShouldNotBeFound("id.greaterThan=" + id);

        defaultComplainShouldBeFound("id.lessThanOrEqual=" + id);
        defaultComplainShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllComplainsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where name equals to DEFAULT_NAME
        defaultComplainShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the complainList where name equals to UPDATED_NAME
        defaultComplainShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllComplainsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where name not equals to DEFAULT_NAME
        defaultComplainShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the complainList where name not equals to UPDATED_NAME
        defaultComplainShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllComplainsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where name in DEFAULT_NAME or UPDATED_NAME
        defaultComplainShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the complainList where name equals to UPDATED_NAME
        defaultComplainShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllComplainsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where name is not null
        defaultComplainShouldBeFound("name.specified=true");

        // Get all the complainList where name is null
        defaultComplainShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllComplainsByNameContainsSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where name contains DEFAULT_NAME
        defaultComplainShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the complainList where name contains UPDATED_NAME
        defaultComplainShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllComplainsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where name does not contain DEFAULT_NAME
        defaultComplainShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the complainList where name does not contain UPDATED_NAME
        defaultComplainShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllComplainsByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where position equals to DEFAULT_POSITION
        defaultComplainShouldBeFound("position.equals=" + DEFAULT_POSITION);

        // Get all the complainList where position equals to UPDATED_POSITION
        defaultComplainShouldNotBeFound("position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    public void getAllComplainsByPositionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where position not equals to DEFAULT_POSITION
        defaultComplainShouldNotBeFound("position.notEquals=" + DEFAULT_POSITION);

        // Get all the complainList where position not equals to UPDATED_POSITION
        defaultComplainShouldBeFound("position.notEquals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    public void getAllComplainsByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where position in DEFAULT_POSITION or UPDATED_POSITION
        defaultComplainShouldBeFound("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION);

        // Get all the complainList where position equals to UPDATED_POSITION
        defaultComplainShouldNotBeFound("position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    public void getAllComplainsByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where position is not null
        defaultComplainShouldBeFound("position.specified=true");

        // Get all the complainList where position is null
        defaultComplainShouldNotBeFound("position.specified=false");
    }
                @Test
    @Transactional
    public void getAllComplainsByPositionContainsSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where position contains DEFAULT_POSITION
        defaultComplainShouldBeFound("position.contains=" + DEFAULT_POSITION);

        // Get all the complainList where position contains UPDATED_POSITION
        defaultComplainShouldNotBeFound("position.contains=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    public void getAllComplainsByPositionNotContainsSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where position does not contain DEFAULT_POSITION
        defaultComplainShouldNotBeFound("position.doesNotContain=" + DEFAULT_POSITION);

        // Get all the complainList where position does not contain UPDATED_POSITION
        defaultComplainShouldBeFound("position.doesNotContain=" + UPDATED_POSITION);
    }


    @Test
    @Transactional
    public void getAllComplainsByOrganisationIsEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where organisation equals to DEFAULT_ORGANISATION
        defaultComplainShouldBeFound("organisation.equals=" + DEFAULT_ORGANISATION);

        // Get all the complainList where organisation equals to UPDATED_ORGANISATION
        defaultComplainShouldNotBeFound("organisation.equals=" + UPDATED_ORGANISATION);
    }

    @Test
    @Transactional
    public void getAllComplainsByOrganisationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where organisation not equals to DEFAULT_ORGANISATION
        defaultComplainShouldNotBeFound("organisation.notEquals=" + DEFAULT_ORGANISATION);

        // Get all the complainList where organisation not equals to UPDATED_ORGANISATION
        defaultComplainShouldBeFound("organisation.notEquals=" + UPDATED_ORGANISATION);
    }

    @Test
    @Transactional
    public void getAllComplainsByOrganisationIsInShouldWork() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where organisation in DEFAULT_ORGANISATION or UPDATED_ORGANISATION
        defaultComplainShouldBeFound("organisation.in=" + DEFAULT_ORGANISATION + "," + UPDATED_ORGANISATION);

        // Get all the complainList where organisation equals to UPDATED_ORGANISATION
        defaultComplainShouldNotBeFound("organisation.in=" + UPDATED_ORGANISATION);
    }

    @Test
    @Transactional
    public void getAllComplainsByOrganisationIsNullOrNotNull() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where organisation is not null
        defaultComplainShouldBeFound("organisation.specified=true");

        // Get all the complainList where organisation is null
        defaultComplainShouldNotBeFound("organisation.specified=false");
    }
                @Test
    @Transactional
    public void getAllComplainsByOrganisationContainsSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where organisation contains DEFAULT_ORGANISATION
        defaultComplainShouldBeFound("organisation.contains=" + DEFAULT_ORGANISATION);

        // Get all the complainList where organisation contains UPDATED_ORGANISATION
        defaultComplainShouldNotBeFound("organisation.contains=" + UPDATED_ORGANISATION);
    }

    @Test
    @Transactional
    public void getAllComplainsByOrganisationNotContainsSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where organisation does not contain DEFAULT_ORGANISATION
        defaultComplainShouldNotBeFound("organisation.doesNotContain=" + DEFAULT_ORGANISATION);

        // Get all the complainList where organisation does not contain UPDATED_ORGANISATION
        defaultComplainShouldBeFound("organisation.doesNotContain=" + UPDATED_ORGANISATION);
    }


    @Test
    @Transactional
    public void getAllComplainsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where email equals to DEFAULT_EMAIL
        defaultComplainShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the complainList where email equals to UPDATED_EMAIL
        defaultComplainShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllComplainsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where email not equals to DEFAULT_EMAIL
        defaultComplainShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the complainList where email not equals to UPDATED_EMAIL
        defaultComplainShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllComplainsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultComplainShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the complainList where email equals to UPDATED_EMAIL
        defaultComplainShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllComplainsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where email is not null
        defaultComplainShouldBeFound("email.specified=true");

        // Get all the complainList where email is null
        defaultComplainShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllComplainsByEmailContainsSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where email contains DEFAULT_EMAIL
        defaultComplainShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the complainList where email contains UPDATED_EMAIL
        defaultComplainShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllComplainsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where email does not contain DEFAULT_EMAIL
        defaultComplainShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the complainList where email does not contain UPDATED_EMAIL
        defaultComplainShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllComplainsByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultComplainShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the complainList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultComplainShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllComplainsByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultComplainShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the complainList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultComplainShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllComplainsByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultComplainShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the complainList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultComplainShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllComplainsByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where phoneNumber is not null
        defaultComplainShouldBeFound("phoneNumber.specified=true");

        // Get all the complainList where phoneNumber is null
        defaultComplainShouldNotBeFound("phoneNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllComplainsByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultComplainShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the complainList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultComplainShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllComplainsByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultComplainShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the complainList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultComplainShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }


    @Test
    @Transactional
    public void getAllComplainsByControlNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where controlNumber equals to DEFAULT_CONTROL_NUMBER
        defaultComplainShouldBeFound("controlNumber.equals=" + DEFAULT_CONTROL_NUMBER);

        // Get all the complainList where controlNumber equals to UPDATED_CONTROL_NUMBER
        defaultComplainShouldNotBeFound("controlNumber.equals=" + UPDATED_CONTROL_NUMBER);
    }

    @Test
    @Transactional
    public void getAllComplainsByControlNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where controlNumber not equals to DEFAULT_CONTROL_NUMBER
        defaultComplainShouldNotBeFound("controlNumber.notEquals=" + DEFAULT_CONTROL_NUMBER);

        // Get all the complainList where controlNumber not equals to UPDATED_CONTROL_NUMBER
        defaultComplainShouldBeFound("controlNumber.notEquals=" + UPDATED_CONTROL_NUMBER);
    }

    @Test
    @Transactional
    public void getAllComplainsByControlNumberIsInShouldWork() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where controlNumber in DEFAULT_CONTROL_NUMBER or UPDATED_CONTROL_NUMBER
        defaultComplainShouldBeFound("controlNumber.in=" + DEFAULT_CONTROL_NUMBER + "," + UPDATED_CONTROL_NUMBER);

        // Get all the complainList where controlNumber equals to UPDATED_CONTROL_NUMBER
        defaultComplainShouldNotBeFound("controlNumber.in=" + UPDATED_CONTROL_NUMBER);
    }

    @Test
    @Transactional
    public void getAllComplainsByControlNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where controlNumber is not null
        defaultComplainShouldBeFound("controlNumber.specified=true");

        // Get all the complainList where controlNumber is null
        defaultComplainShouldNotBeFound("controlNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllComplainsByControlNumberContainsSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where controlNumber contains DEFAULT_CONTROL_NUMBER
        defaultComplainShouldBeFound("controlNumber.contains=" + DEFAULT_CONTROL_NUMBER);

        // Get all the complainList where controlNumber contains UPDATED_CONTROL_NUMBER
        defaultComplainShouldNotBeFound("controlNumber.contains=" + UPDATED_CONTROL_NUMBER);
    }

    @Test
    @Transactional
    public void getAllComplainsByControlNumberNotContainsSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where controlNumber does not contain DEFAULT_CONTROL_NUMBER
        defaultComplainShouldNotBeFound("controlNumber.doesNotContain=" + DEFAULT_CONTROL_NUMBER);

        // Get all the complainList where controlNumber does not contain UPDATED_CONTROL_NUMBER
        defaultComplainShouldBeFound("controlNumber.doesNotContain=" + UPDATED_CONTROL_NUMBER);
    }


    @Test
    @Transactional
    public void getAllComplainsByWitnessesIsEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);
        Witness witnesses = WitnessResourceIT.createEntity(em);
        em.persist(witnesses);
        em.flush();
        complain.addWitnesses(witnesses);
        complainRepository.saveAndFlush(complain);
        Long witnessesId = witnesses.getId();

        // Get all the complainList where witnesses equals to witnessesId
        defaultComplainShouldBeFound("witnessesId.equals=" + witnessesId);

        // Get all the complainList where witnesses equals to witnessesId + 1
        defaultComplainShouldNotBeFound("witnessesId.equals=" + (witnessesId + 1));
    }


    @Test
    @Transactional
    public void getAllComplainsBySuspectsIsEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);
        Suspect suspects = SuspectResourceIT.createEntity(em);
        em.persist(suspects);
        em.flush();
        complain.addSuspects(suspects);
        complainRepository.saveAndFlush(complain);
        Long suspectsId = suspects.getId();

        // Get all the complainList where suspects equals to suspectsId
        defaultComplainShouldBeFound("suspectsId.equals=" + suspectsId);

        // Get all the complainList where suspects equals to suspectsId + 1
        defaultComplainShouldNotBeFound("suspectsId.equals=" + (suspectsId + 1));
    }


    @Test
    @Transactional
    public void getAllComplainsByAttachmentsIsEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);
        Attachment attachments = AttachmentResourceIT.createEntity(em);
        em.persist(attachments);
        em.flush();
        complain.addAttachments(attachments);
        complainRepository.saveAndFlush(complain);
        Long attachmentsId = attachments.getId();

        // Get all the complainList where attachments equals to attachmentsId
        defaultComplainShouldBeFound("attachmentsId.equals=" + attachmentsId);

        // Get all the complainList where attachments equals to attachmentsId + 1
        defaultComplainShouldNotBeFound("attachmentsId.equals=" + (attachmentsId + 1));
    }


    @Test
    @Transactional
    public void getAllComplainsByCategoryIsEqualToSomething() throws Exception {
        // Get already existing entity
        Category category = complain.getCategory();
        complainRepository.saveAndFlush(complain);
        Long categoryId = category.getId();

        // Get all the complainList where category equals to categoryId
        defaultComplainShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the complainList where category equals to categoryId + 1
        defaultComplainShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }


    @Test
    @Transactional
    public void getAllComplainsByReceiversIsEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);
        User receivers = UserResourceIT.createEntity(em);
        em.persist(receivers);
        em.flush();
        complain.addReceivers(receivers);
        complainRepository.saveAndFlush(complain);
        String receiversId = receivers.getId();

        // Get all the complainList where receivers equals to receiversId
        defaultComplainShouldBeFound("receiversId.equals=" + receiversId);

        // Get all the complainList where receivers equals to receiversId + 1
        defaultComplainShouldNotBeFound("receiversId.equals=" + (receiversId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultComplainShouldBeFound(String filter) throws Exception {
        restComplainMockMvc.perform(get("/api/complains?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(complain.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].organisation").value(hasItem(DEFAULT_ORGANISATION)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].controlNumber").value(hasItem(DEFAULT_CONTROL_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restComplainMockMvc.perform(get("/api/complains/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultComplainShouldNotBeFound(String filter) throws Exception {
        restComplainMockMvc.perform(get("/api/complains?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restComplainMockMvc.perform(get("/api/complains/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingComplain() throws Exception {
        // Get the complain
        restComplainMockMvc.perform(get("/api/complains/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateComplain() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        int databaseSizeBeforeUpdate = complainRepository.findAll().size();

        // Update the complain
        Complain updatedComplain = complainRepository.findById(complain.getId()).get();
        // Disconnect from session so that the updates on updatedComplain are not directly saved in db
        em.detach(updatedComplain);
        updatedComplain
            .name(UPDATED_NAME)
            .position(UPDATED_POSITION)
            .organisation(UPDATED_ORGANISATION)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .controlNumber(UPDATED_CONTROL_NUMBER)
            .description(UPDATED_DESCRIPTION);
        ComplainDTO complainDTO = complainMapper.toDto(updatedComplain);

        restComplainMockMvc.perform(put("/api/complains").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(complainDTO)))
            .andExpect(status().isOk());

        // Validate the Complain in the database
        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeUpdate);
        Complain testComplain = complainList.get(complainList.size() - 1);
        assertThat(testComplain.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testComplain.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testComplain.getOrganisation()).isEqualTo(UPDATED_ORGANISATION);
        assertThat(testComplain.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testComplain.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testComplain.getControlNumber()).isEqualTo(UPDATED_CONTROL_NUMBER);
        assertThat(testComplain.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingComplain() throws Exception {
        int databaseSizeBeforeUpdate = complainRepository.findAll().size();

        // Create the Complain
        ComplainDTO complainDTO = complainMapper.toDto(complain);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComplainMockMvc.perform(put("/api/complains").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(complainDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Complain in the database
        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteComplain() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        int databaseSizeBeforeDelete = complainRepository.findAll().size();

        // Delete the complain
        restComplainMockMvc.perform(delete("/api/complains/{id}", complain.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
