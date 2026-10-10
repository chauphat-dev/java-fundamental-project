package com.learnjavawithcrocsa.store.repositories;

import com.learnjavawithcrocsa.store.dtos.UserSummary;
import com.learnjavawithcrocsa.store.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    @EntityGraph(attributePaths = {"tags","addresses"})
    Optional<User> findByEmail(String email);


    @EntityGraph(attributePaths = "addresses")
    @Query("select u from User u")
    List<User> findAllWithAddresses();

    @Query("SELECT u.id as id, u.email as email FROM User u WHERE u.profile.loyalty_points > :points order by u.email")
    List<UserSummary> findByLoyalUsers(@Param("points") int points);
}
