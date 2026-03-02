package org.gdo.voucherio.service;

import org.gdo.voucherio.voucher.model.VoucherResponse;
import org.gdo.voucherio.voucher.exception.NoVoucherExistsException;
import org.gdo.voucherio.voucher.exception.VoucherAlreadyExistsException;
import org.gdo.voucherio.voucher.model.Voucher;
import org.gdo.voucherio.voucher.model.VoucherDuration;
import org.gdo.voucherio.voucher.model.VoucherRequest;
import org.gdo.voucherio.voucher.model.VoucherRequestMapper;
import org.gdo.voucherio.voucher.model.VoucherResponseMapper;
import org.gdo.voucherio.voucher.repository.VoucherRepository;
import org.gdo.voucherio.voucher.service.VoucherService;
import org.gdo.voucherio.voucher.service.VoucherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class VoucherServiceTest {

    @Mock
    private VoucherRepository voucherRepository;

    private VoucherResponseMapper voucherResponseMapper = Mappers.getMapper(VoucherResponseMapper.class);
    private VoucherRequestMapper voucherRequestMapper = Mappers.getMapper(VoucherRequestMapper.class);
    private VoucherService voucherService;

    @BeforeEach
    void createService() {
        voucherService = new VoucherServiceImpl(voucherRepository, voucherRequestMapper, voucherResponseMapper);
    }

    @Test
    public void testGetVoucherByCode() {

        // test object
        final String voucherCode = "LEU123";
        final VoucherResponse expectedVoucher = new VoucherResponse(voucherCode, "1h", false);
        final Voucher voucher = new Voucher(voucherCode, VoucherDuration.ONE_HOUR, false);

        // conditions
        when(voucherRepository.findById(voucherCode)).thenReturn(Optional.of(voucher));

        assertEquals(expectedVoucher, voucherService.findByCode(voucherCode));
    }

    @Test
    public void testGetVoucherByCodeNotFound() {

        // test object
        final String voucherCode = "LEU123";
        //
        // conditions
        when(voucherRepository.findById(voucherCode)).thenReturn(Optional.empty());

        assertThrows(NoVoucherExistsException.class, () -> voucherService.findByCode(voucherCode));
    }

    @Test
    public void testAddVoucher() {

        final String voucherCode = "LEU123";
        final VoucherRequest request = new VoucherRequest(voucherCode, "1h");
        final VoucherResponse expectedVoucher = new VoucherResponse(voucherCode, "1h", false);
        final Voucher voucher = new Voucher(voucherCode, VoucherDuration.ONE_HOUR, false);

        when(voucherRepository.findById(voucherCode)).thenReturn(Optional.empty());
        when(voucherRepository.save(voucher))
                .thenReturn(voucher);

        assertEquals(expectedVoucher, voucherService.addVoucher(request));
    }

    @Test
    public void testAddVoucherDuplicate() {

        final String voucherCode = "LEU123";
        final VoucherRequest request = new VoucherRequest(voucherCode, "1h");
        final Voucher voucher = new Voucher(voucherCode, VoucherDuration.ONE_HOUR, false);

        when(voucherRepository.findById(voucherCode)).thenReturn(Optional.of(voucher));

        assertThrows(VoucherAlreadyExistsException.class, () -> voucherService.addVoucher(request));
    }

    @Test
    public void testAddVoucherTwiceDuplicate() {

        final String voucherCode = "LEU123";
        final VoucherRequest request = new VoucherRequest(voucherCode, "1h");
        final VoucherResponse expectedVoucher = new VoucherResponse(voucherCode, "1h", false);
        final Voucher voucher = new Voucher(voucherCode, VoucherDuration.ONE_HOUR, false);

        when(voucherRepository.findById(voucherCode)).thenReturn(Optional.empty());
        when(voucherRepository.save(voucher))
                .thenReturn(voucher)
                .thenThrow(new VoucherAlreadyExistsException("voucher already exists"));

        assertEquals(expectedVoucher, voucherService.addVoucher(request));

        assertThrows(VoucherAlreadyExistsException.class, () -> voucherService.addVoucher(request));
    }
}
