package org.gdo.voucherio.voucher.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// this creates a bean from the mapper
@Mapper(componentModel = "spring")
public interface VoucherRequestMapper {

    @Mapping(source = "code", target = "code")
    @Mapping(target = "duration", expression = "java(VoucherDuration.fromCode(voucherRequest.getDuration()))")
    Voucher from(VoucherRequest voucherRequest);

}
