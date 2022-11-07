package com.starking.money.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.starking.money.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>{
	
	Page<Pessoa> findByNomeContaining(String nome, Pageable pageable);

}
