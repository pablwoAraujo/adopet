package br.com.alura.adopet.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.adopet.api.dto.AbrigoRespostaDto;
import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.dto.PetRespostaDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class AbrigoService {

	@Autowired
	private AbrigoRepository abrigoRepository;

	public List<AbrigoRespostaDto> listar() {
		return abrigoRepository.findAll().stream().map(AbrigoRespostaDto::new).toList();
	}

	public void cadastrar(CadastroAbrigoDto dto) {
		boolean abrigoExiste = abrigoRepository.existsByNomeOrTelefoneOrEmail(dto.nome(), dto.telefone(), dto.email());
		if (abrigoExiste) {
			throw new ValidacaoException("Dados já cadastrados para outro abrigo!");
		}

		Abrigo abrigo = new Abrigo(dto.nome(), dto.telefone(), dto.email());
		abrigoRepository.save(abrigo);
	}

	public List<PetRespostaDto> listarPetsDoAbrigo(String idOuNome) {
		try {
			Abrigo abrigo = encontrarAbrigo(idOuNome);
			return abrigo.getPets().stream().map(PetRespostaDto::new).toList();
		} catch (EntityNotFoundException enfe) {
			throw new EntityNotFoundException();
		}
	}

	public void cadastrarPet(String idOuNome, CadastroPetDto dto) {
		try {
			Abrigo abrigo = encontrarAbrigo(idOuNome);
			Pet pet = new Pet(dto, abrigo);

			abrigo.getPets().add(pet);
			abrigoRepository.save(abrigo);
		} catch (EntityNotFoundException enfe) {
			throw new EntityNotFoundException();
		}
	}

	private Abrigo encontrarAbrigo(String idOuNome) {
		Optional<Abrigo> optional;

		try {
			Long id = Long.parseLong(idOuNome);
			optional = abrigoRepository.findById(id);
		} catch (NumberFormatException exception) {
			optional = abrigoRepository.findByNome(idOuNome);
		}

		return optional.orElseThrow(() -> new ValidacaoException("Abrigo não encontrado"));
	}

}
