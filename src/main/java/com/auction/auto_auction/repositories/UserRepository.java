package com.auction.auto_auction.repositories;

import com.auction.auto_auction.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
}
