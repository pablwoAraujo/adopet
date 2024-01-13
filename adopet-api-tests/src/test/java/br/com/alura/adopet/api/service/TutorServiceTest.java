package br.com.alura.adopet.api.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.adopet.api.dto.AtualizacaoTutorDto;
import br.com.alura.adopet.api.dto.CadastroTutorDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;

@ExtendWith(MockitoExtension.class)
class TutorServiceTest {

	@InjectMocks
	private TutorService tutorService;

	@Mock
	private TutorRepository tutorRepository;

	@Mock
	private CadastroTutorDto dto;

	@Mock
	private Tutor tutor;

	@Mock
	private AtualizacaoTutorDto atualizacaoTutorDto;

	@Test
	void deveriaCadastrarTutor() {
		// Arrange
		BDDMockito.given(tutorRepository.existsByTelefoneOrEmail(dto.telefone(), dto.email())).willReturn(false);

		// Act + Assert
		Assertions.assertDoesNotThrow(() -> tutorService.cadastrar(dto));
		BDDMockito.then(tutorRepository).should().save(new Tutor(dto));
	}

	@Test
	void naodeveriaCadastrarTutorQueJaExiste() {
		// Arrange
		BDDMockito.given(tutorRepository.existsByTelefoneOrEmail(dto.telefone(), dto.email())).willReturn(true);

		// Act + Assert
		Assertions.assertThrows(ValidacaoException.class, () -> tutorService.cadastrar(dto));
	}

	@Test
	void deveriaAtualizarDadosTutor() {
		// Arrange
		BDDMockito.given(tutorRepository.getReferenceById(atualizacaoTutorDto.id())).willReturn(tutor);

		// Act
		tutorService.atualizar(atualizacaoTutorDto);

		// Assert
		BDDMockito.then(tutor).should().atualizarDados(atualizacaoTutorDto);
	}
}
