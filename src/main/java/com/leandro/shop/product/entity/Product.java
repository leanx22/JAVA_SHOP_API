package com.leandro.shop.product.entity;

import com.leandro.shop.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(
            name = "name",
            nullable = false,
            length = 250
    )
    private String name;

    @Column(
            name = "description",
            nullable = false,
            length = 250
    )
    private String description;

    @Column(
            name = "price",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "seller_id",
        nullable = false
    )
    private User seller;

    @Column(
            name = "active",
            nullable = false
    )
    private Boolean active;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
