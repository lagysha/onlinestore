package io.teamchallenge.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"address"})
@EqualsAndHashCode(exclude = {"address"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String telephone;

    @Setter(AccessLevel.PRIVATE)
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "user", optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

    /**
     * Sets the address for this user and updates the user reference in the address object.
     *
     * @param address The address to set.
     */
    public void setAddress(Address address) {
        this.address = address;
        address.setUser(this);
    }
}
