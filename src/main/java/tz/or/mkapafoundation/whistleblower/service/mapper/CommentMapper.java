package tz.or.mkapafoundation.whistleblower.service.mapper;


import tz.or.mkapafoundation.whistleblower.domain.*;
import tz.or.mkapafoundation.whistleblower.service.dto.CommentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Comment} and its DTO {@link CommentDTO}.
 */
@Mapper(componentModel = "spring", uses = {ComplainMapper.class})
public interface CommentMapper extends EntityMapper<CommentDTO, Comment> {

    @Mapping(source = "complain.id", target = "complainId")
    CommentDTO toDto(Comment comment);

    @Mapping(source = "complainId", target = "complain")
    Comment toEntity(CommentDTO commentDTO);

    default Comment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Comment comment = new Comment();
        comment.setId(id);
        return comment;
    }
}
