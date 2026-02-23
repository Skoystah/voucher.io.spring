package org.gdo.voucherio.voucher.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.gdo.voucherio.voucher.model.Voucher;
import org.gdo.voucherio.voucher.model.VoucherRequest;
import org.gdo.voucherio.voucher.model.VoucherRequestMapper;
import org.gdo.voucherio.voucher.model.VoucherResponse;
import org.gdo.voucherio.voucher.model.VoucherResponseMapper;
import org.gdo.voucherio.voucher.exception.NoVoucherExistsException;
import org.gdo.voucherio.voucher.exception.VoucherAlreadyExistsException;
import org.gdo.voucherio.voucher.repository.VoucherRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;
    private final VoucherRequestMapper voucherRequestMapper;
    private final VoucherResponseMapper voucherResponseMapper;

    public VoucherServiceImpl(VoucherRepository voucherRepository,
            VoucherRequestMapper voucherRequestMapper,
            VoucherResponseMapper voucherResponseMapper) {
        this.voucherRepository = voucherRepository;
        this.voucherRequestMapper = voucherRequestMapper;
        this.voucherResponseMapper = voucherResponseMapper;
    }

    public List<VoucherResponse> findAll(VoucherSearchCriteria voucherSearchCriteria) {

        Example<Voucher> queryVoucher = Example.of(voucherSearchCriteria.toVoucher());

        return this.voucherRepository.findAll(queryVoucher)
                .stream()
                .map(voucherResponseMapper::from)
                .collect(Collectors.toList());

    }

    public VoucherResponse findByCode(String code) {
        Optional<Voucher> voucher = this.voucherRepository.findById(code);

        if (!voucher.isPresent()) {
            throw new NoVoucherExistsException(
                    String.format("Voucher with code %s does not exist", code));
        }
        return voucherResponseMapper.from(voucher.get());
    }

    @Override
    public VoucherResponse addVoucher(VoucherRequest voucherRequest) {
        Optional<Voucher> savedVoucher = this.voucherRepository.findById(voucherRequest.getCode());

        if (savedVoucher.isPresent()) {
            throw new VoucherAlreadyExistsException(
                    String.format("Voucher with code %s already exists", voucherRequest.getCode()));
        }

        Voucher newVoucher = voucherRequestMapper.from(voucherRequest);
        newVoucher.setUsed(false);

        return voucherResponseMapper.from(this.voucherRepository.save(newVoucher));
    }

    public void delete(String code) {
        this.voucherRepository.findById(code)
                .orElseThrow(
                        () -> new NoVoucherExistsException(String.format("Voucher with code %s does not exist", code)));

        this.voucherRepository.deleteById(code);
    }

    public void useVoucher(String code) {
        Voucher voucher = this.voucherRepository.findById(code)
                .orElseThrow(
                        () -> new NoVoucherExistsException(String.format("Voucher with code %s does not exist", code)));

        voucher.setUsed(true);
        this.voucherRepository.save(voucher);
    }
}
