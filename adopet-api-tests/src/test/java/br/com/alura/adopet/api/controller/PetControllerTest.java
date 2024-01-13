package br.com.alura.adopet.api.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import br.com.alura.adopet.api.service.PetService;

@SpringBootTest
@AutoConfigureMockMvc
class PetControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PetService service;

	@Test
	void deveriaDevolverCodigo200ParaRequisicaoDeListarTodosPetsDisponiveis() throws Exception {
		// Act
		MockHttpServletResponse response = mockMvc
				.perform(MockMvcRequestBuilders.get("/pets").contentType(MediaType.APPLICATION_JSON)).andReturn()
				.getResponse();

		// Assert
		Assertions.assertEquals(200, response.getStatus());
	}

}
