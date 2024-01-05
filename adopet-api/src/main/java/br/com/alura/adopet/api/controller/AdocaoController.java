package br.com.alura.adopet.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.adopet.api.dto.AprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.ReprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.SolicitacaoDeAdocaoDto;
import br.com.alura.adopet.api.service.AdocaoService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

@RestController
@RequestMapping("/adocoes")
public class AdocaoController {

	@Autowired
	private AdocaoService adocaoService;

	@PostMapping
	@Transactional
	public ResponseEntity<String> solicitar(@RequestBody @Valid SolicitacaoDeAdocaoDto solicitacao) {
		try {
			this.adocaoService.solicitar(solicitacao);
			return ResponseEntity.ok("Adoção solicitada com sucesso!");
		} catch (ValidationException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@PutMapping("/aprovar")
	@Transactional
	public ResponseEntity<String> aprovar(@RequestBody @Valid AprovacaoAdocaoDto aprovacao) {
		try {
			this.adocaoService.aprovar(aprovacao);
			return ResponseEntity.ok("Adoção aprovada com sucesso!");
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@PutMapping("/reprovar")
	@Transactional
	public ResponseEntity<String> reprovar(@RequestBody @Valid ReprovacaoAdocaoDto reprovacao) {
		try {
			this.adocaoService.reprovar(reprovacao);
			return ResponseEntity.ok("Adoção reprovada com sucesso!");
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}
