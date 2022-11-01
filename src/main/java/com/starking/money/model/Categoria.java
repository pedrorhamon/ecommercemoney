package com.starking.money.model;

import javax.persistence.*;

import lombok.*;

@Entity
@Table(name = "categoria")
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long codigo;
	
	private String nome;

}
