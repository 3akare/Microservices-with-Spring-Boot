package com.microservice.accounts.repository;

import com.microservice.accounts.entity.Accounts;
import com.microservice.accounts.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {
    Optional<Accounts> findByCustomer(Customer customer);
    Optional<Accounts> findByAccountNumber(Long accountNumber);
}
