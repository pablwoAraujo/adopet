package br.com.alura;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AdopetConsoleApplication {

	public static void main(String[] args) {
		System.out.println("##### BOAS VINDAS AO SISTEMA ADOPET CONSOLE #####");
		try {
			int opcaoEscolhida = 0;
			Scanner scanner = new Scanner(System.in);
			while (opcaoEscolhida != 5) {
				System.out.println("\nDIGITE O NÚMERO DA OPERAÇÃO DESEJADA:");
				System.out.println("1 -> Listar abrigos cadastrados");
				System.out.println("2 -> Cadastrar novo abrigo");
				System.out.println("3 -> Listar pets do abrigo");
				System.out.println("4 -> Importar pets do abrigo");
				System.out.println("5 -> Sair");

				String textoDigitado = scanner.nextLine();
				opcaoEscolhida = Integer.parseInt(textoDigitado);

				if (opcaoEscolhida == 1) {
					listShelters();
				} else if (opcaoEscolhida == 2) {
					registerShelters();
				} else if (opcaoEscolhida == 3) {
					listShelterPets();
				} else if (opcaoEscolhida == 4) {
					importPetsToTheShelter();
				} else if (opcaoEscolhida == 5) {
					break;
				} else {
					System.out.println("NÚMERO INVÁLIDO!");
					opcaoEscolhida = 0;
				}
			}
			scanner.close();
			System.out.println("Finalizando o programa...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void listShelters() throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
		String uri = "http://localhost:8080/abrigos";

		HttpResponse<String> response = triggerGetRequest(client, uri);
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

	public static void registerShelters() throws IOException, InterruptedException {
		System.out.println("Digite o nome do abrigo:");
		Scanner scanner = new Scanner(System.in);

		String nome = scanner.nextLine();
		System.out.println("Digite o telefone do abrigo:");
		String telefone = scanner.nextLine();
		System.out.println("Digite o email do abrigo:");
		String email = scanner.nextLine();

		JsonObject json = new JsonObject();
		json.addProperty("nome", nome);
		json.addProperty("telefone", telefone);
		json.addProperty("email", email);

		HttpClient client = HttpClient.newHttpClient();
		String uri = "http://localhost:8080/abrigos";
		HttpResponse<String> response = triggerPostRequest(client, uri, json);

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

	public static void listShelterPets() throws IOException, InterruptedException {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Digite o id ou nome do abrigo:");
		String idOuNome = scanner.nextLine();

		scanner.close();

		HttpClient client = HttpClient.newHttpClient();
		String uri = "http://localhost:8080/abrigos/" + idOuNome + "/pets";
		HttpResponse<String> response = triggerGetRequest(client, uri);

		int statusCode = response.statusCode();
		if (statusCode == 404 || statusCode == 500) {
			System.out.println("ID ou nome não cadastrado!");
			return;
		}
		String responseBody = response.body();
		JsonArray jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();
		System.out.println("Pets cadastrados:");
		for (JsonElement element : jsonArray) {
			JsonObject jsonObject = element.getAsJsonObject();
			long id = jsonObject.get("id").getAsLong();
			String tipo = jsonObject.get("tipo").getAsString();
			String nome = jsonObject.get("nome").getAsString();
			String raca = jsonObject.get("raca").getAsString();
			int idade = jsonObject.get("idade").getAsInt();
			System.out.println(id + " - " + tipo + " - " + nome + " - " + raca + " - " + idade + " ano(s)");
		}
	}

	public static void importPetsToTheShelter() throws NumberFormatException, IOException, InterruptedException {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Digite o id ou nome do abrigo:");
		String idOuNome = scanner.nextLine();

		System.out.println("Digite o nome do arquivo CSV:");
		String nomeArquivo = scanner.nextLine();

		scanner.close();
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
			String tipo = campos[0];
			String nome = campos[1];
			String raca = campos[2];
			int idade = Integer.parseInt(campos[3]);
			String cor = campos[4];
			Float peso = Float.parseFloat(campos[5]);

			JsonObject json = new JsonObject();
			json.addProperty("tipo", tipo.toUpperCase());
			json.addProperty("nome", nome);
			json.addProperty("raca", raca);
			json.addProperty("idade", idade);
			json.addProperty("cor", cor);
			json.addProperty("peso", peso);

			HttpClient client = HttpClient.newHttpClient();
			String uri = "http://localhost:8080/abrigos/" + idOuNome + "/pets";
			HttpResponse<String> response = triggerPostRequest(client, uri, json);

			int statusCode = response.statusCode();
			String responseBody = response.body();
			if (statusCode == 200) {
				System.out.println("Pet cadastrado com sucesso: " + nome);
			} else if (statusCode == 404) {
				System.out.println("Id ou nome do abrigo não encontado!");
				break;
			} else if (statusCode == 400 || statusCode == 500) {
				System.out.println("Erro ao cadastrar o pet: " + nome);
				System.out.println(responseBody);
				break;
			}
		}
		reader.close();
	}

	public static HttpResponse<String> triggerGetRequest(HttpClient client, String uri)
			throws IOException, InterruptedException {
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri))
				.method("GET", HttpRequest.BodyPublishers.noBody()).build();

		return client.send(request, HttpResponse.BodyHandlers.ofString());
	}

	public static HttpResponse<String> triggerPostRequest(HttpClient client, String uri, JsonObject json)
			throws IOException, InterruptedException {
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).header("Content-Type", "application/json")
				.method("POST", HttpRequest.BodyPublishers.ofString(json.toString())).build();

		return client.send(request, HttpResponse.BodyHandlers.ofString());
	}
}
