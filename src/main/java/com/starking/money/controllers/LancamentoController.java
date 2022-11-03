package com.starking.money.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.starking.money.event.RecursoCriadoEvent;
import com.starking.money.exception.MoneyExceptionHandle.Erro;
import com.starking.money.exception.PessoaInexistenteOuInativoException;
import com.starking.money.model.Lancamento;
import com.starking.money.services.LancamentoService;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	public List<Lancamento> buscar() {
		return this.lancamentoService.buscar();
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<?> buscarPorId(Lancamento lancamento) {
		Optional<Lancamento> lancamentoOptional = this.lancamentoService.buscarPorId(lancamento);
		return !lancamentoOptional.isPresent() ? ResponseEntity.notFound().build() : ResponseEntity.ok(lancamentoOptional);
	}
	
	@PostMapping
	public ResponseEntity<?> salvar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
		Lancamento lancamentoSalvar = this.lancamentoService.salvar(lancamento);
		this.publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamento.getCodigo()));	
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvar);	
	}	
}
