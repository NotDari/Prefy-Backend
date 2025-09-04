package com.daribear.PrefyBackend.Authentication.Registration.RegistrationToken;

import com.daribear.PrefyBackend.Authentication.Authentication;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * data entity representing the registrationconfirmationToken.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class RegistrationConfirmationToken {

    @Id
    @SequenceGenerator(
            name = "RegistrationConfirmation_Sequence",
            sequenceName = "RegistrationConfirmation_Sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "RegistrationConfirmation_Sequence"
    )
    private Long id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime expiredAt;
    private LocalDateTime confirmedAt;
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "authentication_id"
    )
    private Authentication authentication;


    public RegistrationConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiredAt,Authentication authentication) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.authentication = authentication;
    }
}
