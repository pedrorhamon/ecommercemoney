package com.starking.money.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.starking.money.model.Lancamento;
import com.starking.money.repositories.lancamento.LancamentoRepositoryQuery;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery {

}
