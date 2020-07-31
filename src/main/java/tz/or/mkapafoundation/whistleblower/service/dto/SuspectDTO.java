package tz.or.mkapafoundation.whistleblower.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link tz.or.mkapafoundation.whistleblower.domain.Suspect} entity.
 */
public class SuspectDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String name;

    private String position;

    private String organisation;

    private String email;

    private String phoneNumber;


    private Long complainId;
    
    public Long getId() {
        return id;
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

    public Long getComplainId() {
        return complainId;
    }

    public void setComplainId(Long complainId) {
        this.complainId = complainId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SuspectDTO)) {
            return false;
        }

        return id != null && id.equals(((SuspectDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SuspectDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", position='" + getPosition() + "'" +
            ", organisation='" + getOrganisation() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", complainId=" + getComplainId() +
            "}";
    }
}
