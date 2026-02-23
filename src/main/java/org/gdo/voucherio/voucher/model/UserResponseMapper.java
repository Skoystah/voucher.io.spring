package org.gdo.voucherio.voucher.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {

    @Mapping(source = "username", target = "name")
    @Mapping(source = "isAdmin", target = "is_admin")
    UserResponse from(User user);

}
