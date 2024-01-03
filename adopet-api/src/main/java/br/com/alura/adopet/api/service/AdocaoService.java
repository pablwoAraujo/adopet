package br.com.alura.adopet.api.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.adopet.api.dto.AprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.ReprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.SolicitacaoDeAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validations.SolicitacaoDeAdocaoValidation;

@Service
public class AdocaoService {

	@Autowired
	private AdocaoRepository repository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private PetRepository petRepository;

	@Autowired
	private TutorRepository tutorRepository;

	@Autowired
	private List<SolicitacaoDeAdocaoValidation> solicitaoDeAdocaoValidation;

	public void solicitar(SolicitacaoDeAdocaoDto solicitacao) {
		Pet pet = petRepository.getReferenceById(solicitacao.idPet());
		Tutor tutor = tutorRepository.getReferenceById(solicitacao.idTutor());

		solicitaoDeAdocaoValidation.forEach(v -> v.validate(solicitacao));

		Adocao adocao = new Adocao();
		adocao.setPet(pet);
		adocao.setTutor(tutor);
		adocao.setMotivo(solicitacao.motivo());
		adocao.setData(LocalDateTime.now());
		adocao.setStatus(StatusAdocao.AGUARDANDO_AVALIACAO);
		repository.save(adocao);

		String subject = "Solicitação de adoção";
		String recipient = adocao.getPet().getAbrigo().getEmail();
		String message = ("Olá " + adocao.getPet().getAbrigo().getNome()
				+ "!\n\nUma solicitação de adoção foi registrada hoje para o pet: " + adocao.getPet().getNome()
				+ ". \nFavor avaliar para aprovação ou reprovação.");

		emailService.sendEmail(recipient, subject, message);
	}

	public void aprovar(AprovacaoAdocaoDto aprovacao) {
		Adocao adocao = repository.getReferenceById(aprovacao.idAdocao());
		adocao.setStatus(StatusAdocao.APROVADO);

		String subject = "Adoção aprovada";
		String recipient = adocao.getPet().getAbrigo().getEmail();
		String message = ("Parabéns " + adocao.getTutor().getNome() + "!\n\nSua adoção do pet "
				+ adocao.getPet().getNome() + ", solicitada em "
				+ adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
				+ ", foi aprovada.\nFavor entrar em contato com o abrigo " + adocao.getPet().getAbrigo().getNome()
				+ " para agendar a busca do seu pet.");

		emailService.sendEmail(recipient, subject, message);
	}

	public void reprovar(ReprovacaoAdocaoDto reprovacao) {
		Adocao adocao = repository.getReferenceById(reprovacao.idAdocao());
		adocao.setStatus(StatusAdocao.REPROVADO);
		adocao.setJustificativaStatus(reprovacao.justificativa());

		String subject = "Adoção reprovada";
		String recipient = adocao.getPet().getAbrigo().getEmail();
		String message = ("Olá " + adocao.getTutor().getNome() + "!\n\nInfelizmente sua adoção do pet "
				+ adocao.getPet().getNome() + ", solicitada em "
				+ adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
				+ ", foi reprovada pelo abrigo " + adocao.getPet().getAbrigo().getNome()
				+ " com a seguinte justificativa: " + adocao.getJustificativaStatus());

		emailService.sendEmail(recipient, subject, message);
	}
}
