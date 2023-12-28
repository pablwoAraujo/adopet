package br.com.alura.service;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

		JsonArray jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();
		System.out.println("Abrigos cadastrados:");
		for (JsonElement element : jsonArray) {
			JsonObject jsonObject = element.getAsJsonObject();
			long id = jsonObject.get("id").getAsLong();
			String nome = jsonObject.get("nome").getAsString();
			System.out.println(id + " - " + nome);
		}
	}

	public void registerShelters() throws IOException, InterruptedException {
		System.out.println("Digite o nome do abrigo:");
		Scanner scanner = new Scanner(System.in);

		String name = scanner.nextLine();
		System.out.println("Digite o telefone do abrigo:");
		String phone = scanner.nextLine();
		System.out.println("Digite o email do abrigo:");
		String email = scanner.nextLine();

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

		scanner.close();
	}
}
