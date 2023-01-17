package com.auction.auto_auction.repository;

import com.auction.auto_auction.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
}
