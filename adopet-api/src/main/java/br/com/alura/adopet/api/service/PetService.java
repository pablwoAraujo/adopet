package br.com.alura.adopet.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.adopet.api.dto.PetRespostaDto;
import br.com.alura.adopet.api.repository.PetRepository;

@Service
public class PetService {

	@Autowired
	private PetRepository repository;

	public List<PetRespostaDto> listarTodosDisponiveis() {
		List<PetRespostaDto> disponiveis = repository
				.findAllByAdotado(false)
				.stream()
				.map(PetRespostaDto::new)
				.toList();

		return disponiveis;
	}
}
