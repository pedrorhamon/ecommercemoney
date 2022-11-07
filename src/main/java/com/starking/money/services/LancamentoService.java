package com.starking.money.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.starking.money.exception.PessoaInexistenteOuInativoException;
import com.starking.money.model.Lancamento;
import com.starking.money.model.Pessoa;
import com.starking.money.model.dto.LancamentoDTO;
import com.starking.money.repositories.LancamentoRepository;
import com.starking.money.repositories.PessoaRepository;
import com.starking.money.repositories.filter.LancamentoFilter;

@Service
public class LancamentoService {

	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private PessoaService pessoaService;
	
	@Autowired
	private PessoaRepository pessoaRepository;

	public Page<Lancamento> buscar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		return this.lancamentoRepository.filtrar(lancamentoFilter, pageable);
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
	
	public Page<LancamentoDTO> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
		return this.lancamentoRepository.resumir(lancamentoFilter, pageable);
	}

	public Lancamento atualizar(Long codigo, Lancamento lancamento) {
		Lancamento lancamentoSalvo = buscarLancamentoExistente(codigo);
		if (!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) {
			validarPessoa(lancamento);
		}

		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");

		return lancamentoRepository.save(lancamentoSalvo);
	}

	private void validarPessoa(Lancamento lancamento) {
		Optional<Pessoa> pessoa = null;
		if (lancamento.getPessoa().getCodigo() != null) {
			pessoa = this.pessoaRepository.findById(lancamento.getPessoa().getCodigo());
		}

		if (pessoa == null || pessoa.get().isInativo()) {
			throw new PessoaInexistenteOuInativoException();
		}
	}

	private Lancamento buscarLancamentoExistente(Long codigo) {
/* 		Optional<Lancamento> lancamentoSalvo = lancamentoRepository.findById(codigo);
		if (lancamentoSalvo.isEmpty()) {
			throw new IllegalArgumentException();
		} */
		return lancamentoRepository.findById(codigo).orElseThrow(() -> new IllegalArgumentException());
	}	
}
