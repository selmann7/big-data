package com.infinitumit.big_data.repository;

import com.infinitumit.big_data.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * UserRepository için entegrasyon testleri.
 * - JPA repository işlemlerini test etmek için bellek içi (in-memory) H2 veritabanı kullanır.
 * - @DataJpaTest, JPA testleri için bir Spring uygulama bağlamı (application context) yapılandırır.
 */
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private PasswordEncoder passwordEncoder;

    /**
     * Her testten önce parola şifreleyiciyi (encoder) başlatır.
     */
    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Yeni bir kullanıcının kaydedilmesini test eder.
     */
    @Test
    void whenSaveUser_thenUserIsPersisted() {
        // roles set'inin mutable olması için HashSet
        User newUser = new User("testuser", passwordEncoder.encode("password123"), new HashSet<>(Set.of("ROLE_USER")));

        User savedUser = userRepository.save(newUser);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("testuser");
        assertThat(passwordEncoder.matches("password123", savedUser.getPassword())).isTrue();
        assertThat(savedUser.getRoles()).containsExactlyInAnyOrder("ROLE_USER");
    }

    /**
     * Bir kullanıcının kimliğine (ID) göre bulunmasını test eder.
     */
    @Test
    void whenFindById_thenUserIsFound() {
        User user = new User("findbyiduser", passwordEncoder.encode("securepass"), Set.of("ROLE_ADMIN"));
        entityManager.persistAndFlush(user);

        Optional<User> foundUser = userRepository.findById(user.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("findbyiduser");
        assertThat(foundUser.get().getRoles()).containsExactlyInAnyOrder("ROLE_ADMIN");
    }

    /**
     * Kullanıcı adını kullanarak kullanıcı bulmayı test eder (custom method).
     */
    @Test
    void whenFindByUsername_thenUserIsFound() {
        User user = new User("uniqueuser", passwordEncoder.encode("mypas$word"), Set.of("ROLE_USER"));
        entityManager.persistAndFlush(user);

        Optional<User> foundUser = userRepository.findByUsername("uniqueuser");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("uniqueuser");
    }

    /**
     * findByUsername methodunun, var olmayan bir kullanıcı için empty döndüğünü test eder.
     */
    @Test
    void whenFindByUsername_thenEmptyOptionalForNonExistentUser() {
        Optional<User> foundUser = userRepository.findByUsername("nonexistent");
        assertThat(foundUser).isEmpty();
    }

    /**
     * Mevcut bir kullanıcının güncellenmesini test eder.
     */
    @Test
    void whenUpdateUser_thenChangesArePersisted() {
        // roles set'inin mutable olması için HashSet
        User user = new User("oldname", passwordEncoder.encode("pass"), new HashSet<>(Set.of("ROLE_USER")));
        entityManager.persistAndFlush(user);

        User userToUpdate = userRepository.findById(user.getId()).get();
        userToUpdate.setUsername("newname");
        // Güncellenmiş roller için yeni HashSet
        userToUpdate.setRoles(new HashSet<>(Set.of("ROLE_USER", "ROLE_EDITOR")));
        userToUpdate.setPassword(passwordEncoder.encode("newpass")); // Update password as well

        User updatedUser = userRepository.save(userToUpdate);

        assertThat(updatedUser.getUsername()).isEqualTo("newname");
        assertThat(updatedUser.getRoles()).containsExactlyInAnyOrder("ROLE_USER", "ROLE_EDITOR");
        assertThat(passwordEncoder.matches("newpass", updatedUser.getPassword())).isTrue();
    }

    /**
     * Bir kullanıcının silinmesini test eder.
     */
    @Test
    void whenDeleteUser_thenUserIsRemoved() {
        User user = new User("todelete", passwordEncoder.encode("deletepass"), Set.of("ROLE_USER"));
        entityManager.persistAndFlush(user);

        userRepository.delete(user);
        entityManager.flush();

        Optional<User> deletedUser = userRepository.findById(user.getId());
        assertThat(deletedUser).isEmpty();
    }
}
