package com.starking.money.repositories.lancamento;

import java.util.List;

import com.starking.money.model.Lancamento;
import com.starking.money.repositories.filter.LancamentoFilter;

public interface LancamentoRepositoryQuery {
	
	public List<Lancamento> filtrar(LancamentoFilter lancamentoFilter);
}
