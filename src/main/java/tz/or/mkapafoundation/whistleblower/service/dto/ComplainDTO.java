package tz.or.mkapafoundation.whistleblower.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Lob;

/**
 * A DTO for the {@link tz.or.mkapafoundation.whistleblower.domain.Complain}
 * entity.
 */
public class ComplainDTO implements Serializable {

    private Long id;

    private String name;

    private String position;

    private String organisation;

    private String email;

    private String phoneNumber;

    private String controlNumber;

    @Lob
    private String description;

    private Long categoryId;

    private String categoryName;
    private Set<UserDTO> receivers = new HashSet<>();
    private Set<SuspectDTO> suspects = new HashSet<>();
    private Set<WitnessDTO> witnesses = new HashSet<>();
    private Set<AttachmentDTO> attachments = new HashSet<>();

    public Long getId() {
        return id;
    }

    public Set<AttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<AttachmentDTO> attachments) {
        this.attachments = attachments;
    }

    public Set<WitnessDTO> getWitnesses() {
        return witnesses;
    }

    public void setWitnesses(Set<WitnessDTO> witnesses) {
        this.witnesses = witnesses;
    }

    public Set<SuspectDTO> getSuspects() {
        return suspects;
    }

    public void setSuspects(Set<SuspectDTO> suspects) {
        this.suspects = suspects;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getControlNumber() {
        return controlNumber;
    }

    public void setControlNumber(String controlNumber) {
        this.controlNumber = controlNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Set<UserDTO> getReceivers() {
        return receivers;
    }

    public void setReceivers(Set<UserDTO> users) {
        this.receivers = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ComplainDTO)) {
            return false;
        }

        return id != null && id.equals(((ComplainDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ComplainDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", position='" + getPosition() + "'" +
            ", organisation='" + getOrganisation() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", controlNumber='" + getControlNumber() + "'" +
            ", description='" + getDescription() + "'" +
            ", categoryId=" + getCategoryId() +
            ", categoryName='" + getCategoryName() + "'" +
            ", receivers='" + getReceivers() + "'" +
            "}";
    }
}
