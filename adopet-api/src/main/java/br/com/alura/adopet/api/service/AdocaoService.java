package br.com.alura.adopet.api.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;

@Service
public class AdocaoService {

	@Autowired
	private AdocaoRepository repository;

	@Autowired
	private EmailService emailService;

	public void solicitar(Adocao adocao) {
		if (adocao.getPet().getAdotado() == true) {
			throw new ValidacaoException("Pet já foi adotado!");
		} else {
			List<Adocao> adocoes = repository.findAll();
			for (Adocao a : adocoes) {
				if (a.getTutor() == adocao.getTutor() && a.getStatus() == StatusAdocao.AGUARDANDO_AVALIACAO) {
					throw new ValidacaoException("Tutor já possui outra adoção aguardando avaliação!");
				}
			}
			for (Adocao a : adocoes) {
				if (a.getPet() == adocao.getPet() && a.getStatus() == StatusAdocao.AGUARDANDO_AVALIACAO) {
					throw new ValidacaoException("Pet já está aguardando avaliação para ser adotado!");
				}
			}
			for (Adocao a : adocoes) {
				int contador = 0;
				if (a.getTutor() == adocao.getTutor() && a.getStatus() == StatusAdocao.APROVADO) {
					contador = contador + 1;
				}
				if (contador == 5) {
					throw new ValidacaoException("Tutor chegou ao limite máximo de 5 adoções!");
				}
			}
		}
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

	public void aprovar(Adocao adocao) {
		adocao.setStatus(StatusAdocao.APROVADO);
		repository.save(adocao);

		String subject = "Adoção aprovada";
		String recipient = adocao.getPet().getAbrigo().getEmail();
		String message = ("Parabéns " + adocao.getTutor().getNome() + "!\n\nSua adoção do pet "
				+ adocao.getPet().getNome() + ", solicitada em "
				+ adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
				+ ", foi aprovada.\nFavor entrar em contato com o abrigo " + adocao.getPet().getAbrigo().getNome()
				+ " para agendar a busca do seu pet.");

		emailService.sendEmail(recipient, subject, message);
	}

	public void reprovar(Adocao adocao) {
		adocao.setStatus(StatusAdocao.REPROVADO);
		repository.save(adocao);

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