package org.gdo.voucherio.voucher.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VoucherResponseMapper {

    @Mapping(source = "code", target = "code")
    @Mapping(target = "duration", expression = "java(voucher.getDuration().toStr())")
    @Mapping(source = "used", target = "used")
    VoucherResponse from(Voucher voucher);

}
