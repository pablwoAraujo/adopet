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
public class TutorComAdocaoEmAndamento implements SolicitacaoDeAdocaoValidation {

	@Autowired
	private AdocaoRepository repository;

	@Autowired
	private TutorRepository tutorRepository;

	public void validate(SolicitacaoDeAdocaoDto solicitacao) {
		List<Adocao> adocoes = repository.findAll();
		Tutor tutor = tutorRepository.getReferenceById(solicitacao.idTutor());

		for (Adocao a : adocoes) {
			if (a.getTutor() == tutor && a.getStatus() == StatusAdocao.AGUARDANDO_AVALIACAO) {
				throw new ValidacaoException("Tutor já possui outra adoção aguardando avaliação!");
			}
		}
	}
}
