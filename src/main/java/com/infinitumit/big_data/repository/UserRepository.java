package com.infinitumit.big_data.repository;

import com.infinitumit.big_data.entity.User;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User entities için repository interface.
 * JpaRepository ile yapma, okuma, güncelleme ve silme
 */
@Repository // Bu interface'i Spring Data JPA repository component
@EnableJpaRepositories(basePackages = "com.infinitumit.big_data.repository")
@EntityScan(basePackages = "com.infinitumit.big_data.entity")
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Username ile User bulmak
     * @param username Aratılacak username
     * @return Bulunursa içinde User olan, bulunmazsa empty Optional.
     */
    Optional<User> findByUsername(String username);
}
