package com.compass.ms.repository;

import com.compass.ms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById (Long id);

    Optional<User> findByEmail(String email);

    @Query(value = "from User u where u.email = ?1 and not u.id = ?2")
    Optional<User> findByEmailAndId(String email, Long id);
}
