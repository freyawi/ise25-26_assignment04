package de.seuhd.campuscoffee.data.persistence;

import jakarta.persistence.*;
import lombok.*;
import de.seuhd.campuscoffee.domain.model.PaymentMethod;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refunds")
public class RefundEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refund_sequence_generator")
    @SequenceGenerator(name = "refund_sequence_generator", sequenceName = "refund_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private Long originalSaleId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod refundMethod;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private Long cashierId;

    @Column(nullable = false)
    private String cashierName;

    private String reason;

    @Column(nullable = false, unique = true)
    private String receiptNumber;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}