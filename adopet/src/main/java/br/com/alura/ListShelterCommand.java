package br.com.alura;

import java.io.IOException;

import br.com.alura.client.ClientHttpConfiguration;
import br.com.alura.service.ShelterService;

public class ListShelterCommand implements Command {

	@Override
	public void execute() {
		try {
			ClientHttpConfiguration clientHttp = new ClientHttpConfiguration();
			ShelterService shelterService = new ShelterService(clientHttp);
			shelterService.listShelters();
		} catch (IOException | InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

}
