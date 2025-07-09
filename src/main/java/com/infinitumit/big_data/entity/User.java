package com.infinitumit.big_data.entity;

import jakarta.persistence.*; // Spring Boot 3+ için Jakarta Persistence API
import java.util.Set; // Roller için, string seti veya enums

/**
 * Database de ki bir User entity'i temsil eder.
 * Bu class PostgreSQL database'in de ki bir "users" table'ına (veya benzerine) maplenir.
 * User bilgilerinin depolanacağı yapıyı tanımlar.
 */
@Entity // Bu class'ı bir JPA entity si olarak işaretler ve onu bir database table'ına eşler
@Table(name = "users") // Database de ki table name'i specifies
public class User {

    @Id // Bu field'ı primary key olarak mark eder
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Primary key'i database tarafından otomatik arttırmak için configure etmek
    private Long id; // User için id

    @Column(nullable = false, unique = true) // Username'in null olmaması ve aynısı olmaması için
    private String username; // Kullanıcının seçtiği login için username

    @Column(nullable = false) // Şifrenin null olmaması için
    private String password;

    // Simple typeların bir koleksiyonu için @ElementCollection kullanımı (roller için stringler gibi)
    // Bu genellikle roller için ayrı bir join table'ı yapacaktır.
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles; // Kullanıcıya verilen rol set

    /**
     * Default constructor.
     */
    public User() {
    }

    /**
     * Yeni User nesnesi yapmak için constructor
     * @param username Kullanıcı adı.
     * @param password Kullanıcı şifresi.
     * @param roles Kullanıcıya verilen rol seti.
     */
    public User(String username, String password, Set<String> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    // Log ve Debug için okunabilirliği arttırabilen bir method.

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", roles=" + roles +
                '}';
    }
}
