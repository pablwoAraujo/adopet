package br.com.alura.adopet.api.controller;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.service.AbrigoService;
import br.com.alura.adopet.api.service.PetService;

@SpringBootTest
@AutoConfigureMockMvc
class AbrigoControllerTest {

	@MockBean
	private AbrigoService abrigoService;

	@MockBean
	private PetService petService;

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private Abrigo abrigo;

	@Test
	void deveriaRetornarCodigo200ParaRequisicaoDeListarAbrigos() throws Exception {
		// ACT
		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/abrigos")).andReturn()
				.getResponse();

		// ASSERT
		Assertions.assertEquals(200, response.getStatus());
	}

	@Test
	void deveriaRetornarCodigo200ParaRequisicaoDeCadastrarAbrigo() throws Exception {
		// ARRANGE
		String json = """
				{
				    "nome": "Abrigo feliz",
				    "telefone": "(99)9999-9999",
				    "email": "email@example.com.br"
				}
				""";

		// ACT
		MockHttpServletResponse response = mockMvc
				.perform(MockMvcRequestBuilders.post("/abrigos").content(json).contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// ASSERT
		Assertions.assertEquals(200, response.getStatus());
	}

	@Test
	void deveriaRetornarCodigo400ParaRequisicaoDeCadastrarAbrigoComDadosFaltando() throws Exception {
		// ARRANGE
		String json = "";

		// ACT
		MockHttpServletResponse response = mockMvc
				.perform(MockMvcRequestBuilders.post("/abrigos").content(json).contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// ASSERT
		Assertions.assertEquals(400, response.getStatus());
	}

	@Test
	void deveriaRetornarCodigo400ParaRequisicaoDeCadastrarAbrigoQuandoAbrigoJaExiste() throws Exception {
		// ARRANGE
		String json = """
				{
				    "nome": "Abrigo feliz",
				    "telefone": "(99)9999-9999",
				    "email": "email@example.com.br"
				}
				""";

		BDDMockito.doThrow(ValidacaoException.class).when(abrigoService).cadatrar(any());

		// ACT
		MockHttpServletResponse response = mockMvc
				.perform(MockMvcRequestBuilders.post("/abrigos").content(json).contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// ASSERT
		Assertions.assertEquals(400, response.getStatus());
	}

	@Test
	void deveriaRetornarCodigo200ParaRequisicaoDeListarPetsDoAbrigoPorNome() throws Exception {
		// Arrange
		String nome = "Abrigo feliz";

		// Act
		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/abrigos/{nome}/pets", nome))
				.andReturn().getResponse();

		// Assert
		Assertions.assertEquals(200, response.getStatus());
	}

	@Test
	void deveriaRetornarCodigo200ParaRequisicaoDeListarPetsDoAbrigoPorId() throws Exception {
		// Arrange
		String id = "1";

		// Act
		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/abrigos/{id}/pets", id))
				.andReturn().getResponse();

		// Assert
		Assertions.assertEquals(200, response.getStatus());
	}

	@Test
	void deveriaRetornarCodigo404ParaRequisicaoDeListarPetsDoAbrigoPorNomeInvalido() throws Exception {
		// Arrange
		String nome = "Abrigo feliz	";
		BDDMockito.given(abrigoService.listarPetsDoAbrigo(nome)).willThrow(ValidacaoException.class);

		// Act
		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/abrigos/{nome}/pets", nome))
				.andReturn().getResponse();

		// Assert
		Assertions.assertEquals(404, response.getStatus());
	}

	@Test
	void deveriaRetornarCodigo200ParaRequisicaoDeCadastrarPet() throws Exception {
		// Arrange
		String json = """
				{
				    "tipo": "GATO",
				    "nome": "Miau",
				    "raca": "padrao",
				    "idade": "5",
				    "cor" : "Parda",
				    "peso": "6.4"
				}
				""";

		String abrigoId = "1";

		// Act
		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
				.post("/abrigos/{abrigoNome}/pets", abrigoId).content(json).contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// Assert
		Assertions.assertEquals(200, response.getStatus());
	}

	@Test
	void deveriaDevolverCodigo404ParaRequisicaoDeCadastrarPetAbrigoNaoEncontrado() throws Exception {
		// Arrange
		String json = """
				{
				    "tipo": "GATO",
				    "nome": "Miau",
				    "raca": "padrao",
				    "idade": "5",
				    "cor" : "Parda",
				    "peso": "6.4"
				}
				""";

		String abrigoNome = "Abrigo Feliz";

		BDDMockito.given(abrigoService.carregarAbrigo(abrigoNome)).willThrow(ValidacaoException.class);

		// Act
		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
				.post("/abrigos/{abrigoId}/pets", abrigoNome).content(json).contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// Assert
		Assertions.assertEquals(404, response.getStatus());
	}

}
