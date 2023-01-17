package com.auction.auto_auction.repository;

import com.auction.auto_auction.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount,Integer> {
}
