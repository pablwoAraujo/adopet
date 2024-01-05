package br.com.alura.adopet.api.dto;

import br.com.alura.adopet.api.model.TipoPet;
import jakarta.validation.constraints.NotNull;

public record CadastroPetDto(@NotNull TipoPet tipo, @NotNull String nome, @NotNull String raca, @NotNull Integer idade,
		@NotNull String cor, @NotNull Float peso) {

}
