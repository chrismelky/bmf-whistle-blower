package tz.or.mkapafoundation.whistleblower.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link tz.or.mkapafoundation.whistleblower.domain.Attachment} entity.
 */
public class AttachmentDTO implements Serializable {
    
    private Long id;

    private String name;

    private String contentId;

    private Long contentLength;

    private String mimeType;


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

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
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
        if (!(o instanceof AttachmentDTO)) {
            return false;
        }

        return id != null && id.equals(((AttachmentDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttachmentDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", contentId='" + getContentId() + "'" +
            ", contentLength=" + getContentLength() +
            ", mimeType='" + getMimeType() + "'" +
            ", complainId=" + getComplainId() +
            "}";
    }
}
