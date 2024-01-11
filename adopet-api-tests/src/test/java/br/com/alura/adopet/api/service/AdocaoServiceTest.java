package br.com.alura.adopet.api.service;

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

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
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

}
