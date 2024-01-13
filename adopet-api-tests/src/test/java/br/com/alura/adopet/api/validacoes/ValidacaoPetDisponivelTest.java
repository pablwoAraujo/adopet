package br.com.alura.adopet.api.validacoes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.PetRepository;

@ExtendWith(MockitoExtension.class)
class ValidacaoPetDisponivelTest {

	@Mock
	private PetRepository petRepository;

	@InjectMocks
	private ValidacaoPetDisponivel validacao;

	@Mock
	private Pet pet;

	@Mock
	private SolicitacaoAdocaoDto dto;

	@Test
	void deveriaPermitirSolicitacaoDeAdocaoPet() {
		BDDMockito.given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
		BDDMockito.given(pet.getAdotado()).willReturn(false);

		Assertions.assertDoesNotThrow(() -> validacao.validar(dto));
	}

	@Test
	void naodeveriaPermitirSolicitacaoDeAdocaoPet() {
		BDDMockito.given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
		BDDMockito.given(pet.getAdotado()).willReturn(true);

		Assertions.assertThrows(ValidacaoException.class, () -> validacao.validar(dto));
	}

}
