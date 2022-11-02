package com.starking.money.controllers;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.starking.money.event.RecursoCriadoEvent;
import com.starking.money.model.Pessoa;
import com.starking.money.services.PessoaService;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {
	
	@Autowired
	private PessoaService pessoaService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	public List<Pessoa> listar() {
		return this.pessoaService.listar();
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<?> buscarPorId(Pessoa pessoa) {
		Optional<Pessoa> pessoaOptional = this.pessoaService.buscarPorId(pessoa);
		return !pessoaOptional.isPresent() ? ResponseEntity.notFound().build() : ResponseEntity.ok(pessoaOptional);
	}
	
	@PostMapping
	public ResponseEntity<?> salvar(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {
		Pessoa pessoaSalvar = this.pessoaService.salvar(pessoa);
		this.publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoa.getCodigo()));	
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalvar);
	}
	
	@DeleteMapping("/{codigo}")
	public ResponseEntity<?> deletar(Pessoa pessoa) {
		this.pessoaService.excluir(pessoa);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/{codigo}")
	public ResponseEntity<?> atualizar(@PathVariable("codigo") Long codigo, @Valid @RequestBody Pessoa pessoa) {
		this.pessoaService.atualizar(codigo, pessoa);
		return ResponseEntity.noContent().build();
	}
}