package com.starking.money.controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA')")
	public Page<Pessoa> pesquisar(@RequestParam(required = false, defaultValue = "") String nome, Pageable pageable) {
		return this.pessoaService.listar(nome, pageable);
	}
	
	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and hasAuthority('SCOPE_read')" )
	public ResponseEntity<?> buscarPorId(Pessoa pessoa) {
		Optional<Pessoa> pessoaOptional = this.pessoaService.buscarPorId(pessoa);
		return !pessoaOptional.isPresent() ? ResponseEntity.notFound().build() : ResponseEntity.ok(pessoaOptional);
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and hasAuthority('SCOPE_write')" )
	public ResponseEntity<?> salvar(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {
		Pessoa pessoaSalvar = this.pessoaService.salvar(pessoa);
		this.publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoa.getCodigo()));	
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalvar);
	}
	
	@DeleteMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_REMOVER_PESSOA') and hasAuthority('SCOPE_write')" )
	public ResponseEntity<?> deletar(Pessoa pessoa) {
		this.pessoaService.excluir(pessoa);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/{codigo}")
	public ResponseEntity<?> atualizar(@PathVariable("codigo") Long codigo, @Valid @RequestBody Pessoa pessoa) {
		this.pessoaService.atualizar(codigo, pessoa);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/{codigo}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarParcialAtivo(@PathVariable Long codigo, @RequestBody Boolean ativo) {
		this.pessoaService.atualizarPropriedadeAtivo(codigo, ativo);
	}
}