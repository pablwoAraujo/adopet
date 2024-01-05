package br.com.alura.adopet.api.dto;

import br.com.alura.adopet.api.model.Abrigo;

public record AbrigoRespostaDto(Long id, String nome) {

	public AbrigoRespostaDto(Abrigo abrigo) {
		this(abrigo.getId(), abrigo.getNome());
	}
}
