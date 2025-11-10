package de.seuhd.campuscoffee.data.persistence;

import jakarta.persistence.*;
import lombok.*;
import de.seuhd.campuscoffee.domain.model.PaymentMethod;
import de.seuhd.campuscoffee.domain.model.SaleStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sales")
public class SaleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sale_sequence_generator")
    @SequenceGenerator(name = "sale_sequence_generator", sequenceName = "sale_seq", allocationSize = 1)
    private Long id;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItemEntity> items = new ArrayList<>();

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(precision = 10, scale = 2)
    private BigDecimal discount;

    @Column(precision = 10, scale = 2)
    private BigDecimal tip;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false, unique = true)
    private String receiptNumber;

    @Column(nullable = false)
    private Long cashierId;

    @Column(nullable = false)
    private String cashierName;

    @Column(precision = 10, scale = 2)
    private BigDecimal cashReceived;

    @Column(precision = 10, scale = 2)
    private BigDecimal changeGiven;

    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SaleStatus status;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}