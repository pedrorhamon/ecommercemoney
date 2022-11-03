package com.starking.money.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starking.money.exception.PessoaInexistenteOuInativoException;
import com.starking.money.model.Lancamento;
import com.starking.money.model.Pessoa;
import com.starking.money.repositories.LancamentoRepository;

@Service
public class LancamentoService {

	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private PessoaService pessoaService;

	public List<Lancamento> buscar() {
		return this.lancamentoRepository.findAll();
	}

	public Optional<Lancamento> buscarPorId(Lancamento lancamento) {
		return this.lancamentoRepository.findById(lancamento.getCodigo());
	}
	
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		Pessoa pessoa = this.pessoaService.buscarPessoaPeloCodigo(lancamento.getPessoa().getCodigo());
		if (pessoa == null || pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativoException();
		}
		return this.lancamentoRepository.save(lancamento);
	}
	
	@Transactional
	public void deletar(Lancamento lancamento) {
		this.lancamentoRepository.deleteById(lancamento.getCodigo());
	}
}
