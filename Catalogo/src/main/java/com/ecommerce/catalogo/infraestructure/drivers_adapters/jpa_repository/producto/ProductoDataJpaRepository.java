package com.ecommerce.catalogo.infraestructure.drivers_adapters.jpa_repository.producto;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductoDataJpaRepository extends JpaRepository<ProductoData, Long>{}
