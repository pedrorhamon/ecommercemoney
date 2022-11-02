package com.starking.money.event;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

public class RecursoCriadoEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	@Getter
	private HttpServletResponse response;
	
	@Getter
	private Long codigo;

	public RecursoCriadoEvent(Object source, HttpServletResponse response, Long codigo) {
		super(source);
		this.response = response;
		this.codigo = codigo;
	}
}