package br.com.alura;

import java.io.IOException;

import br.com.alura.client.ClientHttpConfiguration;
import br.com.alura.service.PetService;

public class ListShelterPetsCommand implements Command {

	@Override
	public void execute() {
		try {
			ClientHttpConfiguration clientHttp = new ClientHttpConfiguration();
			PetService petService = new PetService(clientHttp);
			petService.listShelterPets();
		} catch (IOException | InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

}
