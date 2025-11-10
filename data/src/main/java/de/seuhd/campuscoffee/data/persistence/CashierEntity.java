package de.seuhd.campuscoffee.data.persistence;

import jakarta.persistence.*;
import lombok.*;
import de.seuhd.campuscoffee.domain.model.CashierRole;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cashiers")
public class CashierEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cashier_sequence_generator")
    @SequenceGenerator(name = "cashier_sequence_generator", sequenceName = "cashier_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal tipBalance;

    private LocalDateTime lastLogin;

    @Column(nullable = false)
    private boolean active;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CashierRole role;

    @Column
    private LocalDateTime shiftStartTime;

    @Column
    private LocalDateTime shiftEndTime;

    @PrePersist
    protected void onCreate() {
        active = true;
        tipBalance = BigDecimal.ZERO;
    }
}