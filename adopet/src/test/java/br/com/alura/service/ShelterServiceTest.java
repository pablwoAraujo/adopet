package br.com.alura.service;

import br.com.alura.client.ClientHttpConfiguration;
import br.com.alura.domain.Shelter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.http.HttpResponse;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ShelterServiceTest {

	private ClientHttpConfiguration client = mock(ClientHttpConfiguration.class);
	private ShelterService abrigoService = new ShelterService(client);
	private HttpResponse<String> response = mock(HttpResponse.class);
	private Shelter abrigo = new Shelter("Teste", "61981880392", "abrigo_alura@gmail.com");

	@Test
	public void shouldCheckWhenThereHasShelter() throws IOException, InterruptedException {
		abrigo.setId(0L);
		String expectedAbrigosCadastrados = "Abrigos cadastrados:";
		String expectedIdENome = "0 - Teste";

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(baos);
		System.setOut(printStream);

		when(response.body()).thenReturn("[{" + abrigo.toString() + "}]");
		when(client.triggerGetRequest(anyString())).thenReturn(response);

		abrigoService.listShelters();

		String[] lines = baos.toString().split(System.lineSeparator());
		String actualAbrigosCadastrados = lines[0];
		String actualIdENome = lines[1];

		Assertions.assertEquals(expectedAbrigosCadastrados, actualAbrigosCadastrados);
		Assertions.assertEquals(expectedIdENome, actualIdENome);
	}

	@Test
	public void shouldCheckWhenThereIsNoShelter() throws IOException, InterruptedException {
		abrigo.setId(0L);
		String expected = "Não há abrigos cadastrados";

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(baos);
		System.setOut(printStream);

		when(response.body()).thenReturn("[]");
		when(client.triggerGetRequest(anyString())).thenReturn(response);

		abrigoService.listShelters();

		String[] lines = baos.toString().split(System.lineSeparator());
		String actual = lines[0];

		Assertions.assertEquals(expected, actual);
	}
}