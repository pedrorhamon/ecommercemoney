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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.starking.money.event.RecursoCriadoEvent;
import com.starking.money.model.Lancamento;
import com.starking.money.repositories.filter.LancamentoFilter;
import com.starking.money.services.LancamentoService;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and hasAuthority('SCOPE_read')" )
	public Page<Lancamento> buscar(LancamentoFilter lanlcamentoFilter, Pageable pageable) {
		return this.lancamentoService.buscar(lanlcamentoFilter, pageable);
	}
	
	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and hasAuthority('SCOPE_read')" )
	public ResponseEntity<?> buscarPorId(Lancamento lancamento) {
		Optional<Lancamento> lancamentoOptional = this.lancamentoService.buscarPorId(lancamento);
		return !lancamentoOptional.isPresent() ? ResponseEntity.notFound().build() : ResponseEntity.ok(lancamentoOptional);
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and hasAuthority('SCOPE_write')" )
	public ResponseEntity<?> salvar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
		Lancamento lancamentoSalvar = this.lancamentoService.salvar(lancamento);
		this.publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamento.getCodigo()));	
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvar);	
	}
	
	@DeleteMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO') and hasAuthority('SCOPE_write')" )
	public ResponseEntity<?> deletar(Lancamento lancamento) {
		this.lancamentoService.deletar(lancamento);
		return ResponseEntity.noContent().build();		
	}
}
