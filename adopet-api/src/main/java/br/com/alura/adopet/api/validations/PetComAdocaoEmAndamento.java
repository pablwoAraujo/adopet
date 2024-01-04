package br.com.alura.adopet.api.validations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.alura.adopet.api.dto.SolicitacaoDeAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;

@Component
public class PetComAdocaoEmAndamento implements SolicitacaoDeAdocaoValidation {

	@Autowired
	private AdocaoRepository repository;

	public void validate(SolicitacaoDeAdocaoDto solicitacao) {
		boolean alreadyExists = repository.existsByPetIdAndStatus(solicitacao.idPet(),
				StatusAdocao.AGUARDANDO_AVALIACAO);

		if (alreadyExists) {
			throw new ValidacaoException("Pet já está aguardando avaliação para ser adotado!");
		}
	}
}
