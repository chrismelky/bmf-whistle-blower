package tz.or.mkapafoundation.whistleblower.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.content.commons.annotations.ContentId;
import org.springframework.content.commons.annotations.ContentLength;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Attachment.
 */
@Entity
@Table(name = "attachements")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Attachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "content_id")
    @ContentId
    private String contentId;

    @Column(name = "content_length")
    @ContentLength
    private Long contentLength;

    @Column(name = "mime_type")
    private String mimeType;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties(value = "attachments", allowSetters = true)
    private Complain complain;

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

    public Attachment name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentId() {
        return contentId;
    }

    public Attachment contentId(String contentId) {
        this.contentId = contentId;
        return this;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public Attachment contentLength(Long contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Attachment mimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Complain getComplain() {
        return complain;
    }

    public Attachment complain(Complain complain) {
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
        if (!(o instanceof Attachment)) {
            return false;
        }
        return id != null && id.equals(((Attachment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Attachment{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", contentId='" + getContentId() + "'" +
            ", contentLength=" + getContentLength() +
            ", mimeType='" + getMimeType() + "'" +
            "}";
    }
}
