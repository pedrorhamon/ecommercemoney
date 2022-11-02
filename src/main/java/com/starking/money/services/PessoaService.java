package com.starking.money.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
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
}
