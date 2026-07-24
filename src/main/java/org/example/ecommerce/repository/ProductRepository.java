package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<List<Product>> findByCategoryId(Long id, Pageable pageable);
    Optional<List<Product>> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
