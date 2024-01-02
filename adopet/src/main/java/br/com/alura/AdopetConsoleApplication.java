package br.com.alura;

import java.util.Scanner;

import br.com.alura.client.ClientHttpConfiguration;
import br.com.alura.service.PetService;
import br.com.alura.service.ShelterService;

public class AdopetConsoleApplication {

	public static void main(String[] args) {
		CommandExecutor executor = new CommandExecutor();

		System.out.println("##### BOAS VINDAS AO SISTEMA ADOPET CONSOLE #####");
		try {
			int opcaoEscolhida = 0;
			while (opcaoEscolhida != 5) {
				System.out.println("\nDIGITE O NÚMERO DA OPERAÇÃO DESEJADA:");
				System.out.println("1 -> Listar abrigos cadastrados");
				System.out.println("2 -> Cadastrar novo abrigo");
				System.out.println("3 -> Listar pets do abrigo");
				System.out.println("4 -> Importar pets do abrigo");
				System.out.println("5 -> Sair");

				String textoDigitado = new Scanner(System.in).nextLine();
				opcaoEscolhida = Integer.parseInt(textoDigitado);

				if (opcaoEscolhida == 1) {
					executor.executeCommand(new ListShelterCommand());
				} else if (opcaoEscolhida == 2) {
					executor.executeCommand(new RegisterSheltersCommand());
				} else if (opcaoEscolhida == 3) {
					executor.executeCommand(new ListShelterPetsCommand());
				} else if (opcaoEscolhida == 4) {
					executor.executeCommand(new ImportPetsToTheShelterCommand());
				} else if (opcaoEscolhida == 5) {
					break;
				} else {
					System.out.println("NÚMERO INVÁLIDO!");
					opcaoEscolhida = 0;
				}
			}
			System.out.println("Finalizando o programa...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
