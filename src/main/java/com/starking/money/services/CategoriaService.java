package com.starking.money.services;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starking.money.model.Categoria;
import com.starking.money.repositories.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public List<Categoria> listar() {
		return this.categoriaRepository.findAll();
	}
	
	@Transactional
	public Categoria salvar(Categoria categoria, HttpServletResponse response) {
		Categoria categoriaSalvar = this.categoriaRepository.save(categoria);
		return categoriaSalvar;
	}
	
	public Optional<Categoria> buscarPorId(Categoria categoria) {
		return this.categoriaRepository.findById(categoria.getCodigo());
	}
}
