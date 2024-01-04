package br.com.alura.adopet.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.com.alura.adopet.api.dto.CadastroPetDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
@Table(name = "pets")
public class Pet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@NotNull
	private TipoPet tipo;

	@NotBlank
	private String nome;

	@NotBlank
	private String raca;

	@NotNull
	private Integer idade;

	@NotBlank
	private String cor;

	@NotNull
	private Float peso;

	private Boolean adotado;

	@ManyToOne
	@JsonBackReference("abrigo_pets")
	@JoinColumn(name = "abrigo_id")
	private Abrigo abrigo;

	@OneToOne(mappedBy = "pet")
	@JsonBackReference("adocao_pets")
	private Adocao adocao;

	public Pet() {
	}

	public Pet(CadastroPetDto dto, Abrigo abrigo) {
		this.tipo = dto.tipo();
		this.nome = dto.nome();
		this.raca = dto.raca();
		this.idade = dto.idade();
		this.cor = dto.cor();
		this.peso = dto.peso();
		this.adotado = false;
		this.abrigo = abrigo;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Pet pet = (Pet) o;
		return Objects.equals(id, pet.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public Long getId() {
		return id;
	}

	public TipoPet getTipo() {
		return tipo;
	}

	public String getNome() {
		return nome;
	}

	public String getRaca() {
		return raca;
	}

	public Integer getIdade() {
		return idade;
	}

	public String getCor() {
		return cor;
	}

	public Float getPeso() {
		return peso;
	}

	public Boolean getAdotado() {
		return adotado;
	}

	public Abrigo getAbrigo() {
		return abrigo;
	}

	public Adocao getAdocao() {
		return adocao;
	}

}
