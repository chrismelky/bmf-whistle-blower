package tz.or.mkapafoundation.whistleblower.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Notification.
 */
@Entity
@Table(name = "notifications")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "subject")
    private String subject;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "is_sent")
    private Boolean isSent = false;

    @Column(name = "ignore")
    private Boolean ignore = false;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "notifications", allowSetters = true)
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = "notifications", allowSetters = true)
    private Complain complain;

    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "log")
    private String log;

    public Notification(){}
    public Notification(Complain complain, User user) {
        this.complain = complain;
        this.user = user;
        this.email = user.getEmail();
        this.subject = "COMPLAIN";
	}

	// jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public Notification subject(String subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmail() {
        return email;
    }

    public Notification email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean isIsSent() {
        return isSent;
    }

    public Notification isSent(Boolean isSent) {
        this.isSent = isSent;
        return this;
    }

    public void setIsSent(Boolean isSent) {
        this.isSent = isSent;
    }

    public Boolean isIgnore() {
        return ignore;
    }

    public Notification ignore(Boolean ignore) {
        this.ignore = ignore;
        return this;
    }

    public void setIgnore(Boolean ignore) {
        this.ignore = ignore;
    }

    public User getUser() {
        return user;
    }

    public Notification user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Complain getComplain() {
        return complain;
    }

    public Notification complain(Complain complain) {
        this.complain = complain;
        return this;
    }

    public void setComplain(Complain complain) {
        this.complain = complain;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return id != null && id.equals(((Notification) o).id);
    }
    

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", subject='" + getSubject() + "'" +
            ", email='" + getEmail() + "'" +
            ", isSent='" + isIsSent() + "'" +
            ", ignore='" + isIgnore() + "'" +
            "}";
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
