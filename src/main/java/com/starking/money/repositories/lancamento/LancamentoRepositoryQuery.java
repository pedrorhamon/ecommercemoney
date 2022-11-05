package com.starking.money.repositories.lancamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.starking.money.model.Lancamento;
import com.starking.money.repositories.filter.LancamentoFilter;

public interface LancamentoRepositoryQuery {
	
	Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
}
