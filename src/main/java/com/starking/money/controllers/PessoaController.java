package com.starking.money.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.starking.money.model.Pessoa;
import com.starking.money.services.PessoaService;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {
	
	@Autowired
	private PessoaService pessoaService;
	
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
		URI uri = uriParaUrl(response, pessoaSalvar);
		return ResponseEntity.created(uri).body(pessoaSalvar);
	}
	
	private URI uriParaUrl(HttpServletResponse response, Pessoa pessoaSalvar) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
				.buildAndExpand(pessoaSalvar.getCodigo()).toUri();
		response.setHeader("Location", uri.toASCIIString());
		return uri;
	}
}
