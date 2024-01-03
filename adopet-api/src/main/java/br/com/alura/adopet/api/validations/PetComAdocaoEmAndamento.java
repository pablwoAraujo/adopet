package br.com.alura.adopet.api.validations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.alura.adopet.api.dto.SolicitacaoDeAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;

@Component
public class PetComAdocaoEmAndamento implements SolicitacaoDeAdocaoValidation {

	@Autowired
	private AdocaoRepository repository;

	@Autowired
	private PetRepository petRepository;

	public void validate(SolicitacaoDeAdocaoDto solicitacao) {
		List<Adocao> adocoes = repository.findAll();

		Pet pet = petRepository.getReferenceById(solicitacao.idPet());

		for (Adocao a : adocoes) {
			if (a.getPet() == pet && a.getStatus() == StatusAdocao.AGUARDANDO_AVALIACAO) {
				throw new ValidacaoException("Pet já está aguardando avaliação para ser adotado!");
			}
		}
	}
}
