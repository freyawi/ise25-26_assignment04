package de.seuhd.campuscoffee.data.persistence;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_sequence_generator")
    @SequenceGenerator(name = "product_sequence_generator", sequenceName = "product_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, unique = true)
    private String barcode;

    private String category;
    
    @Column(nullable = false)
    private boolean available;
}