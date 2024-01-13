package br.com.alura.adopet.api.service;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import br.com.alura.adopet.api.repository.PetRepository;

@ExtendWith(MockitoExtension.class)
class AbrigoServiceTest {

	@InjectMocks
	private AbrigoService abrigoService;

	@Mock
	private AbrigoRepository abrigoRepository;

	@Mock
	private PetRepository petRepository;

	@Mock
	private Abrigo abrigo;

	private CadastroAbrigoDto dto;

	@Test
	void deveChamarOMetodoFindAllAoListarOsAbrigos() {
		// Act
		abrigoService.listar();

		// Assert
		BDDMockito.then(abrigoRepository).should().findAll();
	}

	@Test
	void deveriaChamarListaDePetsDoAbrigoAtravesDoNome() {
		// Arrange
		String nome = "Abrigo";
		BDDMockito.given(abrigoRepository.findByNome(nome)).willReturn(Optional.of(abrigo));

		// Act
		abrigoService.listarPetsDoAbrigo(nome);

		// Assert
		BDDMockito.then(petRepository).should().findByAbrigo(abrigo);
	}

	@Test
	void deveCadastrarUmAbrigoNovo() {
		// Arrange
		this.dto = new CadastroAbrigoDto("Abrigo Feliz", "83999999999", "abrigoFeliz@gmail.com");

		// ACT
		abrigoService.cadatrar(dto);

		// ASSERT
		BDDMockito.then(abrigoRepository).should().save(new Abrigo(dto));
	}

	@Test
	void deveLancarUmaExceptionAoCadastrarAbrigoJaExistente() {
		// Arrange
		this.dto = new CadastroAbrigoDto("Abrigo Feliz", "83999999999", "abrigoFeliz@gmail.com");
		BDDMockito.given(abrigoRepository.existsByNomeOrTelefoneOrEmail(dto.nome(), dto.telefone(), dto.email()))
				.willReturn(true);

		// ASSERT
		Assertions.assertThrows(ValidacaoException.class, () -> abrigoService.cadatrar(dto));
	}

	@Test
	void deveRetornarOAbrigoAtravesDoId() {
		// Arrange
		Long id = 1L;
		BDDMockito.given(abrigoRepository.findById(id)).willReturn(Optional.of(abrigo));

		// Act
		abrigoService.carregarAbrigo("1");

		// Assert
		BDDMockito.then(abrigoRepository).should().findById(id);
	}

	@Test
	void deveRetornarOAbrigoAtravesDoNome() {
		// Arrange
		String nome = "Abrigo feliz";
		BDDMockito.given(abrigoRepository.findByNome(nome)).willReturn(Optional.of(abrigo));

		// Act
		abrigoService.carregarAbrigo(nome);

		// Assert
		BDDMockito.then(abrigoRepository).should().findByNome(nome);
	}

	@Test
	void deveLancarUmaExceptionAoPassarUmNomeOuIdInexistente() {
		// Arrange
		String nome = "Abrigo inexistente";

		// Assert
		Assertions.assertThrows(ValidacaoException.class, () -> abrigoService.carregarAbrigo(nome));
	}

}
