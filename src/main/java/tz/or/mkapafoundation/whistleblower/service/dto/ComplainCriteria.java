package tz.or.mkapafoundation.whistleblower.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link tz.or.mkapafoundation.whistleblower.domain.Complain} entity. This class is used
 * in {@link tz.or.mkapafoundation.whistleblower.web.rest.ComplainResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /complains?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ComplainCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter position;

    private StringFilter organisation;

    private StringFilter email;

    private StringFilter phoneNumber;

    private StringFilter controlNumber;

    private LongFilter witnessesId;

    private LongFilter suspectsId;

    private LongFilter attachmentsId;

    private LongFilter categoryId;

    private StringFilter receiversId;

    public ComplainCriteria() {
    }

    public ComplainCriteria(ComplainCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.position = other.position == null ? null : other.position.copy();
        this.organisation = other.organisation == null ? null : other.organisation.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.controlNumber = other.controlNumber == null ? null : other.controlNumber.copy();
        this.witnessesId = other.witnessesId == null ? null : other.witnessesId.copy();
        this.suspectsId = other.suspectsId == null ? null : other.suspectsId.copy();
        this.attachmentsId = other.attachmentsId == null ? null : other.attachmentsId.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.receiversId = other.receiversId == null ? null : other.receiversId.copy();
    }

    @Override
    public ComplainCriteria copy() {
        return new ComplainCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getPosition() {
        return position;
    }

    public void setPosition(StringFilter position) {
        this.position = position;
    }

    public StringFilter getOrganisation() {
        return organisation;
    }

    public void setOrganisation(StringFilter organisation) {
        this.organisation = organisation;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public StringFilter getControlNumber() {
        return controlNumber;
    }

    public void setControlNumber(StringFilter controlNumber) {
        this.controlNumber = controlNumber;
    }

    public LongFilter getWitnessesId() {
        return witnessesId;
    }

    public void setWitnessesId(LongFilter witnessesId) {
        this.witnessesId = witnessesId;
    }

    public LongFilter getSuspectsId() {
        return suspectsId;
    }

    public void setSuspectsId(LongFilter suspectsId) {
        this.suspectsId = suspectsId;
    }

    public LongFilter getAttachmentsId() {
        return attachmentsId;
    }

    public void setAttachmentsId(LongFilter attachmentsId) {
        this.attachmentsId = attachmentsId;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public StringFilter getReceiversId() {
        return receiversId;
    }

    public void setReceiversId(StringFilter receiversId) {
        this.receiversId = receiversId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ComplainCriteria that = (ComplainCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(position, that.position) &&
            Objects.equals(organisation, that.organisation) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(controlNumber, that.controlNumber) &&
            Objects.equals(witnessesId, that.witnessesId) &&
            Objects.equals(suspectsId, that.suspectsId) &&
            Objects.equals(attachmentsId, that.attachmentsId) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(receiversId, that.receiversId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        position,
        organisation,
        email,
        phoneNumber,
        controlNumber,
        witnessesId,
        suspectsId,
        attachmentsId,
        categoryId,
        receiversId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ComplainCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (position != null ? "position=" + position + ", " : "") +
                (organisation != null ? "organisation=" + organisation + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
                (controlNumber != null ? "controlNumber=" + controlNumber + ", " : "") +
                (witnessesId != null ? "witnessesId=" + witnessesId + ", " : "") +
                (suspectsId != null ? "suspectsId=" + suspectsId + ", " : "") +
                (attachmentsId != null ? "attachmentsId=" + attachmentsId + ", " : "") +
                (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
                (receiversId != null ? "receiversId=" + receiversId + ", " : "") +
            "}";
    }

}
