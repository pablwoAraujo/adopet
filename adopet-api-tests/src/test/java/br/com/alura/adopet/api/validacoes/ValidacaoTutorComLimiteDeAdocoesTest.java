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
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;

@ExtendWith(MockitoExtension.class)
class ValidacaoTutorComLimiteDeAdocoesTest {
	@InjectMocks
	private ValidacaoTutorComLimiteDeAdocoes validador;

	@Mock
	private AdocaoRepository adocaoRepository;

	@Mock
	private SolicitacaoAdocaoDto dto;

	@Test
	void deveriaPermitirSolicitacaoDeAdocaoPetSeTutorNaoAtingiuLimiteDe5Adocoes() {
		BDDMockito.given(adocaoRepository.countByTutorIdAndStatus(dto.idTutor(), StatusAdocao.APROVADO)).willReturn(4);

		Assertions.assertDoesNotThrow(() -> validador.validar(dto));
	}

	@Test
	void naoDeveriaPermitirSolicitacaoDeAdocaoPetSeTutorAtingiuLimiteDe5Adocoes() {
		BDDMockito.given(adocaoRepository.countByTutorIdAndStatus(dto.idTutor(), StatusAdocao.APROVADO)).willReturn(5);

		Assertions.assertThrows(ValidacaoException.class, () -> validador.validar(dto));
	}

}
