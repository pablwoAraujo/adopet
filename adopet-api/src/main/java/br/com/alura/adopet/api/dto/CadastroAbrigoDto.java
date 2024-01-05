package br.com.alura.adopet.api.dto;

import jakarta.validation.constraints.NotNull;

public record CadastroAbrigoDto(@NotNull String nome, @NotNull String telefone, @NotNull String email) {

}
