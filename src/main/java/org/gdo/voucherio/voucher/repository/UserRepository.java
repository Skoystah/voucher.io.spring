package org.gdo.voucherio.voucher.repository;

import java.util.Optional;

import org.gdo.voucherio.voucher.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // @NativeQuery("SELECT * from USER WHERE name = ?1")
    public Optional<User> findByUsername(String username);
}
