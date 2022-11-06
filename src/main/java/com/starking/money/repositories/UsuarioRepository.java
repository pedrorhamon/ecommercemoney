package com.starking.money.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.starking.money.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	Optional<Usuario> findByEmail(String email);
}
