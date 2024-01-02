package br.com.alura;

import java.util.Scanner;

public class AdopetConsoleApplication {

	public static void main(String[] args) {
		CommandExecutor executor = new CommandExecutor();
		System.out.println("##### BOAS VINDAS AO SISTEMA ADOPET CONSOLE #####");
		try {
			int opcaoEscolhida = 0;
			while (opcaoEscolhida != 5) {
				showMenu();

				String textoDigitado = new Scanner(System.in).nextLine();
				opcaoEscolhida = Integer.parseInt(textoDigitado);

				switch (opcaoEscolhida) {
					case 1 -> executor.executeCommand(new ListShelterCommand());
					case 2 -> executor.executeCommand(new RegisterSheltersCommand());
					case 3 -> executor.executeCommand(new ListShelterPetsCommand());
					case 4 -> executor.executeCommand(new ImportPetsToTheShelterCommand());
					case 5 -> {
						break;
					}
					default -> {
						System.out.println("NÚMERO INVÁLIDO!");
						opcaoEscolhida = 0;
					}
				}

			}
			System.out.println("Finalizando o programa...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void showMenu() {
		System.out.println("\nDIGITE O NÚMERO DA OPERAÇÃO DESEJADA:");
		System.out.println("1 -> Listar abrigos cadastrados");
		System.out.println("2 -> Cadastrar novo abrigo");
		System.out.println("3 -> Listar pets do abrigo");
		System.out.println("4 -> Importar pets do abrigo");
		System.out.println("5 -> Sair");
	}
}
