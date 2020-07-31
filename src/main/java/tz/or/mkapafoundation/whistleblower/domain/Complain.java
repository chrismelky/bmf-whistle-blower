package tz.or.mkapafoundation.whistleblower.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Complain.
 */
@Entity
@Table(name = "complains")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Complain implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "position")
    private String position;

    @Column(name = "organisation")
    private String organisation;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "control_number")
    private String controlNumber;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "complain",cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Witness> witnesses = new HashSet<>();

    @OneToMany(mappedBy = "complain", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Suspect> suspects = new HashSet<>();

    /**
     * Attachment are not cascase saved when saving complain because they 
     * are created before compian and attached to compian when saving the complain
     */
    @OneToMany(mappedBy = "complain", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Attachment> attachments = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "complains", allowSetters = true)
    private Category category;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "complains_receivers",
               joinColumns = @JoinColumn(name = "complain_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "receivers_id", referencedColumnName = "id"))
    private Set<User> receivers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Complain name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public Complain position(String position) {
        this.position = position;
        return this;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getOrganisation() {
        return organisation;
    }

    public Complain organisation(String organisation) {
        this.organisation = organisation;
        return this;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getEmail() {
        return email;
    }

    public Complain email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Complain phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getControlNumber() {
        return controlNumber;
    }

    public Complain controlNumber(String controlNumber) {
        this.controlNumber = controlNumber;
        return this;
    }

    public void setControlNumber(String controlNumber) {
        this.controlNumber = controlNumber;
    }

    public String getDescription() {
        return description;
    }

    public Complain description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Witness> getWitnesses() {
        return witnesses;
    }

    public Complain witnesses(Set<Witness> witnesses) {
        this.witnesses = witnesses;
        return this;
    }

    public Complain addWitnesses(Witness witness) {
        this.witnesses.add(witness);
        witness.setComplain(this);
        return this;
    }

    public Complain removeWitnesses(Witness witness) {
        this.witnesses.remove(witness);
        witness.setComplain(null);
        return this;
    }

    public void setWitnesses(Set<Witness> witnesses) {
        this.witnesses = witnesses;
    }

    public Set<Suspect> getSuspects() {
        return suspects;
    }

    public Complain suspects(Set<Suspect> suspects) {
        this.suspects = suspects;
        return this;
    }

    public Complain addSuspects(Suspect suspect) {
        this.suspects.add(suspect);
        suspect.setComplain(this);
        return this;
    }

    public Complain removeSuspects(Suspect suspect) {
        this.suspects.remove(suspect);
        suspect.setComplain(null);
        return this;
    }

    public void setSuspects(Set<Suspect> suspects) {
        this.suspects = suspects;
    }

    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public Complain attachments(Set<Attachment> attachments) {
        this.attachments = attachments;
        return this;
    }

    public Complain addAttachments(Attachment attachment) {
        this.attachments.add(attachment);
        attachment.setComplain(this);
        return this;
    }

    public Complain removeAttachments(Attachment attachment) {
        this.attachments.remove(attachment);
        attachment.setComplain(null);
        return this;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Category getCategory() {
        return category;
    }

    public Complain category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<User> getReceivers() {
        return receivers;
    }

    public Complain receivers(Set<User> users) {
        this.receivers = users;
        return this;
    }

    public Complain addReceivers(User user) {
        this.receivers.add(user);
        return this;
    }

    public Complain removeReceivers(User user) {
        this.receivers.remove(user);
        return this;
    }

    public void setReceivers(Set<User> users) {
        this.receivers = users;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Complain)) {
            return false;
        }
        return id != null && id.equals(((Complain) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Complain{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", position='" + getPosition() + "'" +
            ", organisation='" + getOrganisation() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", controlNumber='" + getControlNumber() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
