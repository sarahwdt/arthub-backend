package org.sarahwdt.arthub.model.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@Getter
@Setter
public class Role {
    @Size(max = 32)
    @Pattern(regexp = "[A-Z]+")
    @NotNull
    @Id
    @Column(unique = true, nullable = false, length = 32)
    private String name;

    @NotNull
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Fetch(FetchMode.JOIN)
    @Column(name = "authority", nullable = false, length = 32)
    private Set<Privilege> authorities = new HashSet<>();
}
