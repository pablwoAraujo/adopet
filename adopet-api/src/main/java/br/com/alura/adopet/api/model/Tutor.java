package br.com.alura.adopet.api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.com.alura.adopet.api.dto.AtualizacaoTutorDto;
import br.com.alura.adopet.api.dto.CadastroTutorDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tutores")
public class Tutor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String nome;

	@NotBlank
	@Pattern(regexp = "\\(?\\d{2}\\)?\\d?\\d{4}-?\\d{4}")
	private String telefone;

	@NotBlank
	@Email
	private String email;

	@OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY)
	@JsonManagedReference("tutor_adocoes")
	private List<Adocao> adocoes;

	public Tutor() {
	}

	public Tutor(CadastroTutorDto dto) {
		this.nome = dto.nome();
		this.telefone = dto.telefone();
		this.email = dto.email();
		this.adocoes = new ArrayList<>();
	}

	public void atualizarDados(AtualizacaoTutorDto dto) {
		if (dto.nome() != null && !dto.nome().isEmpty()) {
			this.nome = dto.nome();
		}

		if (dto.telefone() != null && !dto.telefone().isEmpty()) {
			this.telefone = dto.telefone();
		}

		if (dto.email() != null && !dto.email().isEmpty()) {
			this.email = dto.email();
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Tutor tutor = (Tutor) o;
		return Objects.equals(id, tutor.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public String getEmail() {
		return email;
	}

	public List<Adocao> getAdocoes() {
		return adocoes;
	}

}
