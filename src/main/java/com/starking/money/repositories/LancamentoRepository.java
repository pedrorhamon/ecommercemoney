package com.starking.money.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.starking.money.model.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}
