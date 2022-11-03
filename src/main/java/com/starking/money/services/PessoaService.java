package com.starking.money.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.starking.money.model.Pessoa;
import com.starking.money.repositories.PessoaRepository;

@Service
public class PessoaService {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public List<Pessoa> listar() {
		return this.pessoaRepository.findAll();
	}
	
	@Transactional
	public Pessoa salvar(Pessoa pessoa) {
		return this.pessoaRepository.save(pessoa);
	}
	
	public Optional<Pessoa> buscarPorId(Pessoa pessoa) {
		return this.pessoaRepository.findById(pessoa.getCodigo());
	}
	
	@Transactional
	public void excluir(Pessoa pessoa) {
		this.pessoaRepository.deleteById(pessoa.getCodigo());
	}
	
	public Pessoa atualizar(Long codigo, Pessoa pessoa) {
		Pessoa pessoaAtualiza = buscarPessoaPeloCodigo(codigo);
		BeanUtils.copyProperties(pessoa, pessoaAtualiza, "codigo");
		return pessoaRepository.save(pessoaAtualiza);
	}


	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
		Pessoa pessoaAtualiza = buscarPessoaPeloCodigo(codigo);
		pessoaAtualiza.setAtivo(ativo);
		this.pessoaRepository.save(pessoaAtualiza);
	}  
	
	public Pessoa buscarPessoaPeloCodigo(Long codigo) {
		Pessoa pessoaAtualiza = pessoaRepository.findById(codigo)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));
		return pessoaAtualiza;
	}
}