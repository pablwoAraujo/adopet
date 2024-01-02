package br.com.alura.service;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alura.client.ClientHttpConfiguration;
import br.com.alura.domain.Shelter;

public class ShelterService {

	private ClientHttpConfiguration clientHttp;

	public ShelterService(ClientHttpConfiguration clientHttp) {
		this.clientHttp = clientHttp;
	}

	public void listShelters() throws IOException, InterruptedException {
		String uri = "http://localhost:8080/abrigos";

		HttpResponse<String> response = clientHttp.triggerGetRequest(uri);
		String responseBody = response.body();

		Shelter[] shelters = new ObjectMapper().readValue(responseBody, Shelter[].class);
		// List<Shelter> shelterList = Arrays.stream(shelters).toList();

		if (shelters.length == 0) {
			System.out.println("Não há abrigos cadastrados");
		} else {
			System.out.println("Abrigos cadastrados:");
			for (Shelter shelter : shelters) {
				long id = shelter.getId();
				String name = shelter.getNome();
				System.out.println(id + " - " + name);
			}
		}
	}

	public void registerShelters() throws IOException, InterruptedException {
		System.out.println("Digite o nome do abrigo:");
		// Scanner scanner = new Scanner(System.in);

		String name = new Scanner(System.in).nextLine();
		System.out.println("Digite o telefone do abrigo:");
		String phone = new Scanner(System.in).nextLine();
		System.out.println("Digite o email do abrigo:");
		String email = new Scanner(System.in).nextLine();

		Shelter shelter = new Shelter(name, phone, email);

		String uri = "http://localhost:8080/abrigos";
		HttpResponse<String> response = clientHttp.triggerPostRequest(uri, shelter);

		int statusCode = response.statusCode();
		String responseBody = response.body();
		if (statusCode == 200) {
			System.out.println("Abrigo cadastrado com sucesso!");
			System.out.println(responseBody);
		} else if (statusCode == 400 || statusCode == 500) {
			System.out.println("Erro ao cadastrar o abrigo:");
			System.out.println(responseBody);
		}

		// scanner.close();
	}
}
