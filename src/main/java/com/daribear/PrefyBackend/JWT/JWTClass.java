package com.daribear.PrefyBackend.JWT;

import com.daribear.PrefyBackend.Authentication.Authentication;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Jwt Entity that represents a JWT token.
 */
@Data
@Entity
@Table(name = "JWToken",uniqueConstraints=
@UniqueConstraint(columnNames = {"token"}))
@NoArgsConstructor
public class JWTClass {
    @SequenceGenerator(
            name = "JWToken_Sequence",
            sequenceName = "JWToken_Sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "JWToken_Sequence"
    )

    @Id
    private Long Id;
    private String token;
    private LocalDateTime issueDate;
    private LocalDateTime expiryDate;
    private LocalDateTime banDate;
    //@OneToOne(cascade = CascadeType.ALL)
    @OneToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Authentication authentication;


    public JWTClass(String token, LocalDateTime issueDate, LocalDateTime expiryDate, LocalDateTime banDate, Authentication authentication) {
        this.token = token;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.banDate = banDate;
        this.authentication = authentication;
    }
}
