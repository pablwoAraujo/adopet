package br.com.alura.adopet.api.dto;

import br.com.alura.adopet.api.model.TipoPet;

public record CadastroPetDto(TipoPet tipo, String nome, String raca, Integer idade, String cor, Float peso) {

}
