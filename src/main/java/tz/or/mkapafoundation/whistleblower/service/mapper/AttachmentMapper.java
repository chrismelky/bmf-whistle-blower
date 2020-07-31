package tz.or.mkapafoundation.whistleblower.service.mapper;


import tz.or.mkapafoundation.whistleblower.domain.*;
import tz.or.mkapafoundation.whistleblower.service.dto.AttachmentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Attachment} and its DTO {@link AttachmentDTO}.
 */
@Mapper(componentModel = "spring", uses = {ComplainMapper.class})
public interface AttachmentMapper extends EntityMapper<AttachmentDTO, Attachment> {

    @Mapping(source = "complain.id", target = "complainId")
    AttachmentDTO toDto(Attachment attachment);

    @Mapping(source = "complainId", target = "complain")
    Attachment toEntity(AttachmentDTO attachmentDTO);

    default Attachment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Attachment attachment = new Attachment();
        attachment.setId(id);
        return attachment;
    }
}
