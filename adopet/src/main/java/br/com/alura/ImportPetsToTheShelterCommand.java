package br.com.alura;

import java.io.IOException;

import br.com.alura.client.ClientHttpConfiguration;
import br.com.alura.service.PetService;

public class ImportPetsToTheShelterCommand implements Command {

	@Override
	public void execute() {
		try {
			ClientHttpConfiguration clientHttp = new ClientHttpConfiguration();
			PetService petService = new PetService(clientHttp);
			petService.importPetsToTheShelter();
		} catch (IOException | InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

}
