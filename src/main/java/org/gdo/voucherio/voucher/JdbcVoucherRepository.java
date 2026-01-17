package org.gdo.voucherio.voucher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.gdo.voucherio.voucher.exception.NoVoucherExistsException;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class JdbcVoucherRepository implements VoucherRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcVoucherRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void delete(String code) {
        // TODO Auto-generated method stub

    }

    @Override
    public Optional<Voucher> findByCode(String code) {
        List<Voucher> results = jdbcTemplate.query(
                "select code, duration, used from voucher where code =?",
                this::mapRowToVoucher,
                code);

        return results.size() == 0 ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<Voucher> findAll() {
        return jdbcTemplate.query(
                "select code, duration, used from voucher",
                this::mapRowToVoucher);
    }

    @Override
    public Voucher save(Voucher voucher) {
        jdbcTemplate.update(
                "insert into voucher (code, duration, used) values (?, ?, ?)",
                voucher.getCode(),
                voucher.getDuration().getDuration(),
                voucher.getUsed());
        return voucher;
    }

    @Override
    public void useVoucher(String code) {
        jdbcTemplate.update(
                "update voucher set used=true where code=?",
                code);
    }

    private Voucher mapRowToVoucher(ResultSet row, int rowNum) throws SQLException {
        return new Voucher(
                row.getString("code"),
                VoucherDuration.fromCode(row.getString("duration")),
                row.getBoolean("used"));

    }

}
