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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.starking.money.event.RecursoCriadoEvent;
import com.starking.money.model.Categoria;
import com.starking.money.services.CategoriaService;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
	
	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	public List<Categoria> listar() {
		return this.categoriaService.listar();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> criar(@RequestBody @Valid Categoria categoria, HttpServletResponse response) {
		Categoria categoriaSalvar = this.categoriaService.salvar(categoria);
		this.publisher.publishEvent(new RecursoCriadoEvent(this, response, categoria.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalvar);
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<?> buscarPeloCodigo(Categoria categoria) {
		Optional<Categoria> categoriaOptional = this.categoriaService.buscarPorId(categoria);
		return !categoriaOptional.isPresent() ? ResponseEntity.notFound().build() : ResponseEntity.ok(categoriaOptional);
//		if(!categoriaOptional.isPresent()) {
//			return ResponseEntity.notFound().build();
//		}
//		return ResponseEntity.ok(categoriaOptional);
	}
	
	@DeleteMapping("/{codigo}")
	public ResponseEntity<?> deletar(Categoria categoria) {
		this.categoriaService.excluir(categoria);
		return ResponseEntity.noContent().build();
	}	
}