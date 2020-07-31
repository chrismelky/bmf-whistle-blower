package tz.or.mkapafoundation.whistleblower.service.mapper;


import tz.or.mkapafoundation.whistleblower.domain.*;
import tz.or.mkapafoundation.whistleblower.service.dto.NotificationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, ComplainMapper.class})
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "complain.id", target = "complainId")
    @Mapping(source = "complain.description", target = "complainDescription")
    NotificationDTO toDto(Notification notification);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "complainId", target = "complain")
    Notification toEntity(NotificationDTO notificationDTO);

    default Notification fromId(Long id) {
        if (id == null) {
            return null;
        }
        Notification notification = new Notification();
        notification.setId(id);
        return notification;
    }

    default String map(byte[] value) {
        return new String(value);
    }
}
