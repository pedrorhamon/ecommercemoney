package com.starking.money.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.starking.money.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>{

}
