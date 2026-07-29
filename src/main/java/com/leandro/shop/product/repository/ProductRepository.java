package com.leandro.shop.product.repository;

import com.leandro.shop.product.entity.Product;
import com.leandro.shop.user.entity.User;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    @EntityGraph(attributePaths = {"images"})
    Page<Product> findByNameContainingIgnoreCaseAndActiveTrue(String name, Pageable pageable);

    @EntityGraph(attributePaths = {"images"})
    Page<Product> findAllByActiveTrue(Pageable pageable);

    @EntityGraph(attributePaths = {"images"})
    Page<Product> findBySeller(User seller, Pageable pageable);

    @EntityGraph(attributePaths = {"images"})
    Optional<Product> findByIdWithImages(UUID id);

}
