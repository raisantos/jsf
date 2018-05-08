package br.com.caelum.livraria.modelo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

//modelo do livro
@Entity
public class Livro {
	
	@Id @GeneratedValue
	private Integer id;
	
	private String titulo;
	private String isbn;
	private double preco;
	//private String dataDeLancamento;
	@Temporal(TemporalType.DATE)
	private Calendar dataDeLancamento = Calendar.getInstance();
	
	@ManyToMany
	private List<Autor> autores = new ArrayList<Autor>();
	
	public Livro() {
	}

	public List<Autor> getAutores(){
		return autores;
	}
	
	public void adicionaAutor(Autor autor) {
		this.autores.add(autor);
	}
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}

	public Calendar getDataDeLancamento() {
		return dataDeLancamento;
	}

	public void setDataDeLancamento(Calendar dataDeLancamento) {
		this.dataDeLancamento = dataDeLancamento;
	}
	
	@Override
	public int hashCode() {
		final int primo = 31;
		int result = 1;
		result = primo * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		return false;
	}
}