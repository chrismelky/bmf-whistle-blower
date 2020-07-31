package tz.or.mkapafoundation.whistleblower.web.rest;

import tz.or.mkapafoundation.whistleblower.WhistleBlowerApp;
import tz.or.mkapafoundation.whistleblower.config.TestSecurityConfiguration;
import tz.or.mkapafoundation.whistleblower.domain.Notification;
import tz.or.mkapafoundation.whistleblower.domain.User;
import tz.or.mkapafoundation.whistleblower.repository.NotificationRepository;
import tz.or.mkapafoundation.whistleblower.service.NotificationService;
import tz.or.mkapafoundation.whistleblower.service.dto.NotificationDTO;
import tz.or.mkapafoundation.whistleblower.service.mapper.NotificationMapper;

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
 * Integration tests for the {@link NotificationResource} REST controller.
 */
@SpringBootTest(classes = { WhistleBlowerApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class NotificationResourceIT {

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_SENT = false;
    private static final Boolean UPDATED_IS_SENT = true;

    private static final Boolean DEFAULT_IGNORE = false;
    private static final Boolean UPDATED_IGNORE = true;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificationMockMvc;

    private Notification notification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notification createEntity(EntityManager em) {
        Notification notification = new Notification()
            .subject(DEFAULT_SUBJECT)
            .email(DEFAULT_EMAIL)
            .isSent(DEFAULT_IS_SENT)
            .ignore(DEFAULT_IGNORE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        notification.setUser(user);
        return notification;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notification createUpdatedEntity(EntityManager em) {
        Notification notification = new Notification()
            .subject(UPDATED_SUBJECT)
            .email(UPDATED_EMAIL)
            .isSent(UPDATED_IS_SENT)
            .ignore(UPDATED_IGNORE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        notification.setUser(user);
        return notification;
    }

    @BeforeEach
    public void initTest() {
        notification = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllNotifications() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList
        restNotificationMockMvc.perform(get("/api/notifications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].isSent").value(hasItem(DEFAULT_IS_SENT.booleanValue())))
            .andExpect(jsonPath("$.[*].ignore").value(hasItem(DEFAULT_IGNORE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get the notification
        restNotificationMockMvc.perform(get("/api/notifications/{id}", notification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notification.getId().intValue()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.isSent").value(DEFAULT_IS_SENT.booleanValue()))
            .andExpect(jsonPath("$.ignore").value(DEFAULT_IGNORE.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingNotification() throws Exception {
        // Get the notification
        restNotificationMockMvc.perform(get("/api/notifications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
}
