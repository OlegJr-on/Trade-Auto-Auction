package com.auction.auto_auction.repository;

import com.auction.auto_auction.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<List<User>> findByFirstName(String firstName);

    Optional<User> findByFirstNameAndLastName(String firstName,String lastName);

    Optional<User> findByEmail(String email);
}
