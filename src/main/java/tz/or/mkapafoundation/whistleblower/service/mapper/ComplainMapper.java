package tz.or.mkapafoundation.whistleblower.service.mapper;


import tz.or.mkapafoundation.whistleblower.domain.*;
import tz.or.mkapafoundation.whistleblower.service.dto.ComplainDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Complain} and its DTO {@link ComplainDTO}.
 */
@Mapper(componentModel = "spring", uses = {
    CategoryMapper.class, UserMapper.class, WitnessMapper.class, SuspectMapper.class, AttachmentMapper.class})
public interface ComplainMapper extends EntityMapper<ComplainDTO, Complain> {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(target = "witnesses", source = "witnesses")
    @Mapping(target = "suspects", source = "suspects")
    @Mapping(target = "attachments", source = "attachments")
    @Mapping(target = "receivers", source = "receivers")
    ComplainDTO toDto(Complain complain);

    @Mapping(target = "witnesses", source = "witnesses")
    @Mapping(target = "removeWitnesses", ignore = true)
    @Mapping(target = "suspects", source = "suspects")
    @Mapping(target = "removeSuspects", ignore = true)
    @Mapping(target = "attachments", source = "attachments")
    @Mapping(target = "removeAttachments", ignore = true)
    @Mapping(source = "categoryId", target = "category")
    @Mapping(target = "removeReceivers", ignore = true)
    @Mapping(target = "receivers", source = "receivers")
    Complain toEntity(ComplainDTO complainDTO);

    default Complain fromId(Long id) {
        if (id == null) {
            return null;
        }
        Complain complain = new Complain();
        complain.setId(id);
        return complain;
    }
}
