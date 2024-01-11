package br.com.alura.adopet.api.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.service.AdocaoService;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class AdocaoControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private AdocaoService adocaoService;

	@Autowired
	private JacksonTester<SolicitacaoAdocaoDto> jsonDto;

	@Test
	void deveriaDevolverStatus400ParaSolicitacaoComErros() throws Exception {
		// ARRANGE
		String json = """
				{
					"motivo": "Motivo qualquer"
				}
				""";
		// ACT
		MockHttpServletResponse response = mvc
				.perform(MockMvcRequestBuilders.post("/adocoes").content(json).contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// ASSERT
		Assertions.assertEquals(400, response.getStatus());
	}

	@Test
	void deveriaDevolverStatus200ParaSolicitacaoSemErros() throws Exception {
		// ARRANGE
		SolicitacaoAdocaoDto dto = new SolicitacaoAdocaoDto(1l, 1l, "Motivo qualquer");

		// ACT
		MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.post("/adocoes")
				.content(jsonDto.write(dto).getJson()).contentType(MediaType.APPLICATION_JSON)).andReturn()
				.getResponse();

		// ASSERT
		Assertions.assertEquals(200, response.getStatus());
	}

}
