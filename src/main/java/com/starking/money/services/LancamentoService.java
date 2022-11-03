package com.starking.money.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starking.money.model.Lancamento;
import com.starking.money.repositories.LancamentoRepository;

@Service
public class LancamentoService {

	@Autowired
	private LancamentoRepository lancamentoRepository;

	public List<Lancamento> buscar() {
		return this.lancamentoRepository.findAll();
	}

	public Optional<Lancamento> buscarPorId(Lancamento lancamento) {
		return this.lancamentoRepository.findById(lancamento.getCodigo());
	}
	
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		return this.lancamentoRepository.save(lancamento);
	}
}
