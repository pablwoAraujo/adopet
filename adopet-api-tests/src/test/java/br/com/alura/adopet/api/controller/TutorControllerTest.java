package br.com.alura.adopet.api.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import br.com.alura.adopet.api.dto.AtualizacaoTutorDto;
import br.com.alura.adopet.api.dto.CadastroTutorDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.service.TutorService;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class TutorControllerTest {

	@MockBean
	private TutorService service;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JacksonTester<CadastroTutorDto> jsonCadastro;

	@Autowired
	private JacksonTester<AtualizacaoTutorDto> jsonAtualizacao;

	@Test
	void deveriaRetornarCodigo200ParaRequisicaoDeCadastrarTutor() throws Exception {
		// Arrange
		CadastroTutorDto dto = new CadastroTutorDto("Pablwo", "83999999999", "email@example.com.br");

		// Act
		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/tutores")
				.content(jsonCadastro.write(dto).getJson()).contentType(MediaType.APPLICATION_JSON)).andReturn()
				.getResponse();

		// Assert
		Assertions.assertEquals(200, response.getStatus());
	}

	@Test
	void deveriaRetornarCodigo400ParaRequisicaoDeCadastrarTutorInvalido() throws Exception {
		// Arrange
		CadastroTutorDto dto = new CadastroTutorDto("Pablwo", "83999999999", "email@example.com.br");
		BDDMockito.doThrow(ValidacaoException.class).when(service).cadastrar(dto);

		// Act
		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/tutores")
				.content(jsonCadastro.write(dto).getJson()).contentType(MediaType.APPLICATION_JSON)).andReturn()
				.getResponse();

		// Assert
		Assertions.assertEquals(400, response.getStatus());
	}

	@Test
	void deveriaRetornarCodigo200ParaRequisicaoDeAtualizarTutor() throws Exception {
		// Arrange
		AtualizacaoTutorDto dto = new AtualizacaoTutorDto(1L, "Pablwo", "83999999999", "email@example.com.br");

		// Act
		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.put("/tutores")
				.content(jsonAtualizacao.write(dto).getJson()).contentType(MediaType.APPLICATION_JSON)).andReturn()
				.getResponse();

		// Assert
		Assertions.assertEquals(200, response.getStatus());
	}

	@Test
	void deveriaRetornarCodigo400ParaRequisicaoDeAtualizarTutorInvalida() throws Exception {
		// Arrange
		AtualizacaoTutorDto dto = new AtualizacaoTutorDto(1L, "Pablwo", "83999999999", "email@example.com.br");
		BDDMockito.doThrow(ValidacaoException.class).when(service).atualizar(dto);

		// Act
		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.put("/tutores")
				.content(jsonAtualizacao.write(dto).getJson()).contentType(MediaType.APPLICATION_JSON)).andReturn()
				.getResponse();

		// Assert
		Assertions.assertEquals(400, response.getStatus());
	}

}
