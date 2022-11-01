package com.starking.money.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.starking.money.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

}
