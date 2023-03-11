package com.daribear.PrefyBackend.Authentication.PasswordReset;

import com.daribear.PrefyBackend.Authentication.Authentication;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PasswordResetToken {

    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = Authentication.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "authentication_id")
    private Authentication authentication;

    private LocalDateTime expiredAt;
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;

    public PasswordResetToken(String token, Authentication authentication, LocalDateTime createdAt, LocalDateTime expiredAt) {
        this.token = token;
        this.authentication = authentication;
        this.expiredAt = expiredAt;
        this.createdAt = createdAt;
    }
}
