package br.com.alura.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alura.client.ClientHttpConfiguration;
import br.com.alura.domain.Pet;

public class PetService {

	private ClientHttpConfiguration clientHttp;

	public PetService(ClientHttpConfiguration clientHttp) {
		this.clientHttp = clientHttp;
	}

	public void listShelterPets() throws IOException, InterruptedException {
		System.out.println("Digite o id ou nome do abrigo:");
		String idOuNome = new Scanner(System.in).nextLine();

		String uri = "http://localhost:8080/abrigos/" + idOuNome + "/pets";
		HttpResponse<String> response = clientHttp.triggerGetRequest(uri);

		int statusCode = response.statusCode();
		if (statusCode == 404 || statusCode == 500) {
			System.out.println("ID ou nome não cadastrado!");
			return;
		}
		String responseBody = response.body();

		Pet[] pets = new ObjectMapper().readValue(responseBody, Pet[].class);
		// List<Pet> petList = Arrays.stream(pets).toList();

		System.out.println("Pets cadastrados:");
		for (Pet pet : pets) {
			long id = pet.getId();
			String kind = pet.getTipo();
			String name = pet.getNome();
			String breed = pet.getRaca();
			int age = pet.getIdade();
			System.out.println(id + " - " + kind + " - " + name + " - " + breed + " - " + age + " ano(s)");
		}
	}

	public void importPetsToTheShelter() throws NumberFormatException, IOException, InterruptedException {
		System.out.println("Digite o id ou nome do abrigo:");
		String idOuNome = new Scanner(System.in).nextLine();

		System.out.println("Digite o nome do arquivo CSV:");
		String nomeArquivo = new Scanner(System.in).nextLine();

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(nomeArquivo));
		} catch (IOException e) {
			System.out.println("Erro ao carregar o arquivo: " + nomeArquivo);
			return;
		}
		String line;
		while ((line = reader.readLine()) != null) {
			String[] campos = line.split(",");

			String kind = campos[0];
			String name = campos[1];
			String breed = campos[2];
			int age = Integer.parseInt(campos[3]);
			String color = campos[4];
			Float weight = Float.parseFloat(campos[5]);

			Pet pet = new Pet(kind.toUpperCase(), name, breed, age, color, weight);

			String uri = "http://localhost:8080/abrigos/" + idOuNome + "/pets";
			HttpResponse<String> response = clientHttp.triggerPostRequest(uri, pet);

			int statusCode = response.statusCode();
			String responseBody = response.body();
			if (statusCode == 200) {
				System.out.println("Pet cadastrado com sucesso: " + name);
			} else if (statusCode == 404) {
				System.out.println("Id ou nome do abrigo não encontado!");
				break;
			} else if (statusCode == 400 || statusCode == 500) {
				System.out.println("Erro ao cadastrar o pet: " + name);
				System.out.println(responseBody);
				break;
			}
		}
		reader.close();
	}
}
