package br.com.alura.adopet.api.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.adopet.api.dto.AprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.ReprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validacoes.ValidacaoPetComAdocaoEmAndamento;
import br.com.alura.adopet.api.validacoes.ValidacaoPetDisponivel;
import br.com.alura.adopet.api.validacoes.ValidacaoSolicitacaoAdocao;

@ExtendWith(MockitoExtension.class)
class AdocaoServiceTest {

	@InjectMocks
	private AdocaoService service;

	@Mock
	private AdocaoRepository adocaoRepository;

	@Mock
	private PetRepository petRepository;

	@Mock
	private TutorRepository tutorRepository;

	@Mock
	private EmailService emailService;

	@Spy
	private List<ValidacaoSolicitacaoAdocao> validacoes = new ArrayList<>();

	@Mock
	private ValidacaoPetDisponivel validacaoPetDisponivel;

	@Mock
	private ValidacaoPetComAdocaoEmAndamento validacaoPetComAdocaoEmAndamento;

	@Mock
	private Pet pet;

	@Mock
	private Tutor tutor;

	@Mock
	private Abrigo abrigo;

	private SolicitacaoAdocaoDto dto;

	@Captor
	private ArgumentCaptor<Adocao> adocaoCaptor;

	@Mock
	private AprovacaoAdocaoDto aprovacaoAdocaoDto;

	@Mock
	private ReprovacaoAdocaoDto reprovacaoAdocaoDto;

	@Spy
	private Adocao adocao;

	@Test
	void deveriaSalvarAdocaoAoSolicitar() {
		// ARRANGE
		this.dto = new SolicitacaoAdocaoDto(10l, 20l, "motivo qualquer");

		BDDMockito.given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
		BDDMockito.given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
		BDDMockito.given(pet.getAbrigo()).willReturn(abrigo);

		// ACT
		service.solicitar(dto);

		// ASERT
		BDDMockito.then(adocaoRepository).should().save(adocaoCaptor.capture());
		Adocao adocaoSalva = adocaoCaptor.getValue();

		Assertions.assertEquals(pet, adocaoSalva.getPet());
		Assertions.assertEquals(tutor, adocaoSalva.getTutor());
		Assertions.assertEquals(dto.motivo(), adocaoSalva.getMotivo());
	}

	@Test
	void deveriaChamarValidadoresAoSolicitar() {
		// ARRANGE
		this.dto = new SolicitacaoAdocaoDto(10l, 20l, "motivo qualquer");

		BDDMockito.given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
		BDDMockito.given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
		BDDMockito.given(pet.getAbrigo()).willReturn(abrigo);

		validacoes.add(validacaoPetDisponivel);
		validacoes.add(validacaoPetComAdocaoEmAndamento);

		// ACT
		service.solicitar(dto);

		// ASERT
		BDDMockito.then(validacaoPetDisponivel).should().validar(dto);
		BDDMockito.then(validacaoPetComAdocaoEmAndamento).should().validar(dto);
	}

	@Test
	void deveriaEnviarEmailAoSolicitarAdocao() {
		// ARRANGE
		SolicitacaoAdocaoDto dto = new SolicitacaoAdocaoDto(10l, 30l, "motivo qualquer");
		BDDMockito.given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
		BDDMockito.given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
		BDDMockito.given(pet.getAbrigo()).willReturn(abrigo);

		// ACT
		service.solicitar(dto);

		// ASSERT
		BDDMockito.then(adocaoRepository).should().save(adocaoCaptor.capture());
		Adocao adocao = adocaoCaptor.getValue();
		BDDMockito.then(emailService).should().enviarEmail(adocao.getPet().getAbrigo().getEmail(),
				"Solicitação de adoção",
				"Olá " + adocao.getPet().getAbrigo().getNome()
						+ "!\n\nUma solicitação de adoção foi registrada hoje para o pet: " + adocao.getPet().getNome()
						+ ". \nFavor avaliar para aprovação ou reprovação.");
	}

	@Test
	void deveriaAprovarUmaAdocao() {
		// Arrange
		BDDMockito.given(adocaoRepository.getReferenceById(aprovacaoAdocaoDto.idAdocao())).willReturn(adocao);
		BDDMockito.given(adocao.getPet()).willReturn(pet);
		BDDMockito.given(pet.getAbrigo()).willReturn(abrigo);
		BDDMockito.given(abrigo.getEmail()).willReturn("email@example.com");
		BDDMockito.given(adocao.getTutor()).willReturn(tutor);
		BDDMockito.given(tutor.getNome()).willReturn("Rodrigo");
		BDDMockito.given(adocao.getData()).willReturn(LocalDateTime.now());

		// Act
		service.aprovar(aprovacaoAdocaoDto);

		// Assert
		BDDMockito.then(adocao).should().marcarComoAprovada();
		Assertions.assertEquals(StatusAdocao.APROVADO, adocao.getStatus());
	}

	@Test
	void deveriaEnviarEmailAoAprovarUmaAdocao() {
		// Arrange
		BDDMockito.given(adocaoRepository.getReferenceById(aprovacaoAdocaoDto.idAdocao())).willReturn(adocao);
		BDDMockito.given(adocao.getPet()).willReturn(pet);
		BDDMockito.given(pet.getAbrigo()).willReturn(abrigo);
		BDDMockito.given(abrigo.getEmail()).willReturn("email@example.com");
		BDDMockito.given(adocao.getTutor()).willReturn(tutor);
		BDDMockito.given(tutor.getNome()).willReturn("Rodrigo");
		BDDMockito.given(adocao.getData()).willReturn(LocalDateTime.now());

		// Act
		service.aprovar(aprovacaoAdocaoDto);

		// Assert

		BDDMockito.then(emailService).should().enviarEmail(adocao.getPet().getAbrigo().getEmail(), "Adoção aprovada",
				"Parabéns " + adocao.getTutor().getNome() + "!\n\nSua adoção do pet " + adocao.getPet().getNome()
						+ ", solicitada em "
						+ adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
						+ ", foi aprovada.\nFavor entrar em contato com o abrigo "
						+ adocao.getPet().getAbrigo().getNome() + " para agendar a busca do seu pet.");
	}

	@Test
	void deveriaReprovarUmaAdocao() {
		// Arrange
		BDDMockito.given(adocaoRepository.getReferenceById(reprovacaoAdocaoDto.idAdocao())).willReturn(adocao);
		BDDMockito.given(adocao.getPet()).willReturn(pet);
		BDDMockito.given(pet.getAbrigo()).willReturn(abrigo);
		BDDMockito.given(abrigo.getEmail()).willReturn("email@example.com");
		BDDMockito.given(adocao.getTutor()).willReturn(tutor);
		BDDMockito.given(tutor.getNome()).willReturn("Rodrigo");
		BDDMockito.given(adocao.getData()).willReturn(LocalDateTime.now());

		// Act
		service.reprovar(reprovacaoAdocaoDto);

		// Assert
		BDDMockito.then(adocao).should().marcarComoReprovada(reprovacaoAdocaoDto.justificativa());
		Assertions.assertEquals(StatusAdocao.REPROVADO, adocao.getStatus());
	}

	@Test
	void deveriaEnviarEmailAoReprovarUmaAdocao() {
		// Arrange
		BDDMockito.given(adocaoRepository.getReferenceById(aprovacaoAdocaoDto.idAdocao())).willReturn(adocao);
		BDDMockito.given(adocao.getPet()).willReturn(pet);
		BDDMockito.given(pet.getAbrigo()).willReturn(abrigo);
		BDDMockito.given(abrigo.getEmail()).willReturn("email@example.com");
		BDDMockito.given(adocao.getTutor()).willReturn(tutor);
		BDDMockito.given(tutor.getNome()).willReturn("Rodrigo");
		BDDMockito.given(adocao.getData()).willReturn(LocalDateTime.now());

		// Act
		service.reprovar(reprovacaoAdocaoDto);

		// Assert

		BDDMockito.then(emailService).should().enviarEmail(adocao.getPet().getAbrigo().getEmail(),
				"Solicitação de adoção",
				"Olá " + adocao.getTutor().getNome() + "!\n\nInfelizmente sua adoção do pet "
						+ adocao.getPet().getNome() + ", solicitada em "
						+ adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
						+ ", foi reprovada pelo abrigo " + adocao.getPet().getAbrigo().getNome()
						+ " com a seguinte justificativa: " + adocao.getJustificativaStatus());
	}
}
