package tz.or.mkapafoundation.whistleblower.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import io.github.jhipster.config.cache.PrefixedKeyGenerator;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {
    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, tz.or.mkapafoundation.whistleblower.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, tz.or.mkapafoundation.whistleblower.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, tz.or.mkapafoundation.whistleblower.domain.User.class.getName());
            createCache(cm, tz.or.mkapafoundation.whistleblower.domain.Authority.class.getName());
            createCache(cm, tz.or.mkapafoundation.whistleblower.domain.User.class.getName() + ".authorities");
            createCache(cm, tz.or.mkapafoundation.whistleblower.domain.Category.class.getName());
            createCache(cm, tz.or.mkapafoundation.whistleblower.domain.Complain.class.getName());
            createCache(cm, tz.or.mkapafoundation.whistleblower.domain.Complain.class.getName() + ".witnesses");
            createCache(cm, tz.or.mkapafoundation.whistleblower.domain.Complain.class.getName() + ".suspects");
            createCache(cm, tz.or.mkapafoundation.whistleblower.domain.Complain.class.getName() + ".attachments");
            createCache(cm, tz.or.mkapafoundation.whistleblower.domain.Complain.class.getName() + ".receivers");
            createCache(cm, tz.or.mkapafoundation.whistleblower.domain.Suspect.class.getName());
            createCache(cm, tz.or.mkapafoundation.whistleblower.domain.Witness.class.getName());
            createCache(cm, tz.or.mkapafoundation.whistleblower.domain.Attachment.class.getName());
            createCache(cm, tz.or.mkapafoundation.whistleblower.domain.Notification.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache == null) {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
