package tz.or.mkapafoundation.whistleblower.service.mapper;


import tz.or.mkapafoundation.whistleblower.domain.*;
import tz.or.mkapafoundation.whistleblower.service.dto.SuspectDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Suspect} and its DTO {@link SuspectDTO}.
 */
@Mapper(componentModel = "spring", uses = {ComplainMapper.class})
public interface SuspectMapper extends EntityMapper<SuspectDTO, Suspect> {

    @Mapping(source = "complain.id", target = "complainId")
    SuspectDTO toDto(Suspect suspect);

    @Mapping(source = "complainId", target = "complain")
    Suspect toEntity(SuspectDTO suspectDTO);

    default Suspect fromId(Long id) {
        if (id == null) {
            return null;
        }
        Suspect suspect = new Suspect();
        suspect.setId(id);
        return suspect;
    }
}
