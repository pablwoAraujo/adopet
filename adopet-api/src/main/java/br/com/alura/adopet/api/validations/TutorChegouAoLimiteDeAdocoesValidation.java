package br.com.alura.adopet.api.validations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.alura.adopet.api.dto.SolicitacaoDeAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.TutorRepository;

@Component
public class TutorChegouAoLimiteDeAdocoesValidation implements SolicitacaoDeAdocaoValidation {

	@Autowired
	private AdocaoRepository repository;

	@Autowired
	private TutorRepository tutorRepository;

	public void validate(SolicitacaoDeAdocaoDto solicitacao) {
		List<Adocao> adocoes = repository.findAll();
		Tutor tutor = tutorRepository.getReferenceById(solicitacao.idTutor());

		for (Adocao a : adocoes) {
			int contador = 0;
			if (a.getTutor() == tutor && a.getStatus() == StatusAdocao.APROVADO) {
				contador = contador + 1;
			}
			if (contador == 5) {
				throw new ValidacaoException("Tutor chegou ao limite máximo de 5 adoções!");
			}
		}
	}
}
