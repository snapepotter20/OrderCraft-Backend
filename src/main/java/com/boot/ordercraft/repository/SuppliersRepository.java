package com.boot.ordercraft.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boot.ordercraft.model.Suppliers;

public interface SuppliersRepository extends JpaRepository<Suppliers, Long> {

}
