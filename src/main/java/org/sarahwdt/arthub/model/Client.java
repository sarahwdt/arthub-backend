package org.sarahwdt.arthub.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Entity
@Table(name = "clients")
@NoArgsConstructor
@Getter
@Setter
public class Client extends AbstractAuditable<Integer> {
    @Size(max = 64)
    @NotBlank
    @NonNull
    @Column(nullable = false, length = 64)
    private String firstName;

    @Size(max = 128)
    @NotBlank
    @NonNull
    @Column(nullable = false, length = 128)
    private String lastName;

    @Size(max = 64)
    @Column(length = 64)
    private String patronymic;

    @Size(max = 320)
    @Email
    @Column(length = 320)
    private String email;

    @Size(max = 30)
    @Column(length = 30)
    private String phone;
}
