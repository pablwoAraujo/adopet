package br.com.alura.adopet.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.adopet.api.dto.AtualizacaoTutorDto;
import br.com.alura.adopet.api.dto.CadastroTutorDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class TutorService {

	@Autowired
	private TutorRepository repository;

	public void cadastrar(CadastroTutorDto dto) {
		boolean tutorTemCadastro = repository.existsByTelefoneOrEmail(dto.telefone(), dto.email());
		if (tutorTemCadastro) {
			throw new ValidacaoException("Dados já cadastrados para outro tutor!");
		}

		Tutor tutor = new Tutor(dto);
		repository.save(tutor);
	}

	public void atualizar(AtualizacaoTutorDto dto) {
		boolean tutorExiste = repository.existsById(dto.id());
		if (!tutorExiste) {
			throw new EntityNotFoundException();
		}

		Tutor tutor = repository.getReferenceById(dto.id());

		boolean dadosJaCadastrados = repository.existsByTelefoneOrEmail(dto.telefone(), dto.email());
		if (dadosJaCadastrados) {
			throw new ValidacaoException("Dados já cadastrados para outro tutor!");
		}

		tutor.atualizarDados(dto);
	}
}
