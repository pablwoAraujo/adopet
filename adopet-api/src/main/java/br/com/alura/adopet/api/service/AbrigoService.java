package br.com.alura.adopet.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class AbrigoService {

	@Autowired
	private AbrigoRepository abrigoRepository;

	public List<Abrigo> listar() {
		return abrigoRepository.findAll();
	}

	public void cadastrar(CadastroAbrigoDto dto) {
		boolean nomeJaCadastrado = abrigoRepository.existsByNome(dto.nome());
		boolean telefoneJaCadastrado = abrigoRepository.existsByTelefone(dto.telefone());
		boolean emailJaCadastrado = abrigoRepository.existsByEmail(dto.email());

		Abrigo abrigo = new Abrigo();
		abrigo.setNome(dto.nome());
		abrigo.setTelefone(dto.telefone());
		abrigo.setEmail(dto.email());

		if (nomeJaCadastrado || telefoneJaCadastrado || emailJaCadastrado) {
			throw new ValidacaoException("Dados já cadastrados para outro abrigo!");
		}

		abrigoRepository.save(abrigo);
	}

	public List<Pet> listarPetsDoAbrigo(String idOuNome) {
		try {
			Long id = Long.parseLong(idOuNome);
			List<Pet> pets = abrigoRepository.getReferenceById(id).getPets();
			return pets;
		} catch (EntityNotFoundException enfe) {
			throw new EntityNotFoundException();
		} catch (NumberFormatException e) {
			try {
				List<Pet> pets = abrigoRepository.findByNome(idOuNome).getPets();
				return pets;
			} catch (EntityNotFoundException enfe) {
				throw new EntityNotFoundException();
			}
		}
	}

	public void cadastrarPet(String idOuNome, CadastroPetDto dto) {
		Pet pet = new Pet();
		pet.setTipo(dto.tipo());
		pet.setNome(dto.nome());
		pet.setRaca(dto.raca());
		pet.setIdade(dto.idade());
		pet.setCor(dto.cor());
		pet.setPeso(dto.peso());

		try {
			Long id = Long.parseLong(idOuNome);
			Abrigo abrigo = abrigoRepository.getReferenceById(id);
			pet.setAbrigo(abrigo);
			pet.setAdotado(false);
			abrigo.getPets().add(pet);
			abrigoRepository.save(abrigo);
		} catch (EntityNotFoundException enfe) {
			throw new EntityNotFoundException();
		} catch (NumberFormatException nfe) {
			try {
				Abrigo abrigo = abrigoRepository.findByNome(idOuNome);
				pet.setAbrigo(abrigo);
				pet.setAdotado(false);
				abrigo.getPets().add(pet);
				abrigoRepository.save(abrigo);
			} catch (EntityNotFoundException enfe) {
				throw new EntityNotFoundException();
			}
		}
	}

}
