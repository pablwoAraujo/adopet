package br.com.alura.adopet.api.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.ProbabilidadeAdocao;
import br.com.alura.adopet.api.model.TipoPet;

public class CalculadoraProbabilidadeAdocaoTest {

	@Test
	@DisplayName("Probabilidade alta para gatos com idade baixa e peso baixo")
	void deveriaRetornarProbabilidadeAlta() {
		CalculadoraProbabilidadeAdocao calculadora = new CalculadoraProbabilidadeAdocao();

		Abrigo abrigo = new Abrigo(new CadastroAbrigoDto("Abrigo feliz", "83900000001", "abrigofeliz@gmail.com"));
		Pet pet = new Pet(new CadastroPetDto(TipoPet.GATO, "Miau", "Siames", 4, "Cinza", 4.0f), abrigo);

		ProbabilidadeAdocao probabilidade = calculadora.calcular(pet);
		Assertions.assertEquals(ProbabilidadeAdocao.ALTA, probabilidade);
	}

	@Test
	@DisplayName("Probabilidade média para gatos com idade alta e peso baixo")
	void deveriaRetornarProbabilidadeMedia() {
		CalculadoraProbabilidadeAdocao calculadora = new CalculadoraProbabilidadeAdocao();

		Abrigo abrigo = new Abrigo(new CadastroAbrigoDto("Abrigo feliz", "83900000001", "abrigofeliz@gmail.com"));
		Pet pet = new Pet(new CadastroPetDto(TipoPet.GATO, "Miau", "Siames", 15, "Cinza", 4.0f), abrigo);

		ProbabilidadeAdocao probabilidade = calculadora.calcular(pet);
		Assertions.assertEquals(ProbabilidadeAdocao.MEDIA, probabilidade);
	}

	@Test
	@DisplayName("Probabilidade baixa para gatos com idade alta e peso alto")
	void deveriaRetornarProbabilidadeBaixa() {
		CalculadoraProbabilidadeAdocao calculadora = new CalculadoraProbabilidadeAdocao();

		Abrigo abrigo = new Abrigo(new CadastroAbrigoDto("Abrigo feliz", "83900000001", "abrigofeliz@gmail.com"));
		Pet pet = new Pet(new CadastroPetDto(TipoPet.GATO, "Miau", "Siames", 15, "Cinza", 15.0f), abrigo);

		ProbabilidadeAdocao probabilidade = calculadora.calcular(pet);
		Assertions.assertEquals(ProbabilidadeAdocao.BAIXA, probabilidade);
	}

}
