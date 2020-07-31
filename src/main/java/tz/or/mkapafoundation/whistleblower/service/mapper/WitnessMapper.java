package tz.or.mkapafoundation.whistleblower.service.mapper;


import tz.or.mkapafoundation.whistleblower.domain.*;
import tz.or.mkapafoundation.whistleblower.service.dto.WitnessDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Witness} and its DTO {@link WitnessDTO}.
 */
@Mapper(componentModel = "spring", uses = {ComplainMapper.class})
public interface WitnessMapper extends EntityMapper<WitnessDTO, Witness> {

    @Mapping(source = "complain.id", target = "complainId")
    WitnessDTO toDto(Witness witness);

    @Mapping(source = "complainId", target = "complain")
    Witness toEntity(WitnessDTO witnessDTO);

    default Witness fromId(Long id) {
        if (id == null) {
            return null;
        }
        Witness witness = new Witness();
        witness.setId(id);
        return witness;
    }
}
