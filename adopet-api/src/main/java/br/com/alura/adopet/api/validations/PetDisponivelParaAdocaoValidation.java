package br.com.alura.adopet.api.validations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.alura.adopet.api.dto.SolicitacaoDeAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.PetRepository;

@Component
public class PetDisponivelParaAdocaoValidation implements SolicitacaoDeAdocaoValidation {

	@Autowired
	private PetRepository petRepository;

	public void validate(SolicitacaoDeAdocaoDto solicitacao) {
		Pet pet = petRepository.getReferenceById(solicitacao.idPet());

		if (pet.getAdotado()) {
			throw new ValidacaoException("Pet já foi adotado!");
		}
	}
}
