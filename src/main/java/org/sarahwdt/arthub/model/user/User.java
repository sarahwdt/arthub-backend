package org.sarahwdt.arthub.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sarahwdt.arthub.model.AbstractAuditable;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User extends AbstractAuditable<Integer> {
    @Column(unique = true, nullable = false, length = 32)
    private String username;

    @Column(unique = true, nullable = false, length = 320)
    private String email;

    @JsonIgnore
    @OneToOne(mappedBy = "user", optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
    private Credentials credentials;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<RefreshToken> refreshTokens = new HashSet<>();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Role role;
}
