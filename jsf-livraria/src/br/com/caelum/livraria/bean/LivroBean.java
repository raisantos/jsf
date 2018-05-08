package br.com.caelum.livraria.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
//depreciado
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import br.com.caelum.livraria.dao.DAO;
import br.com.caelum.livraria.modelo.Autor;
import br.com.caelum.livraria.modelo.Livro;;

//intermediário do modelo livro com a view xhtml livro
@SuppressWarnings("deprecation")
@ManagedBean
@ViewScoped
public class LivroBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private Livro livro = new Livro();
	private Integer autorId;

	public Integer getAutorId() {
		return autorId;
	}

	public void setAutorId(Integer autorId) {
		this.autorId = autorId;
	}

	public Livro getLivro() {
		return livro;
	}
	
	public void gravarAutor() {
		Autor autor = new DAO<Autor>(Autor.class).buscaPorId(this.autorId);
		this.livro.adicionaAutor(autor);
		System.out.println("Livro escrito por : " + autor.getNome());
	}
	
	//gravar livro no banco validando se tem autor
	public void gravar() {
		System.out.println("Gravando Livro " + this.livro.getTitulo());
		
		if(livro.getAutores().isEmpty()) {
			//throw new RuntimeException("livro deve ter pelo menos um autor");
			FacesContext.getCurrentInstance().addMessage("autor", new FacesMessage("Livro deve ter pelo menos um autor"));
			return;
		}
		new DAO<Livro>(Livro.class).adiciona(this.livro);
		
		this.livro = new Livro();
	}
	
	public List<Autor> getAutores(){
		return new DAO<Autor>(Autor.class).listaTodos();
	}
	
	public List<Autor> getAutoresDoLivro(){
		return this.livro.getAutores();
	}
	
	public List<Livro> getLivros(){
		return new DAO<Livro>(Livro.class).listaLivros();
	}
	
	//validador ISBN
	public void comecaComDigitoUm(FacesContext fc, UIComponent component, Object value) throws ValidatorException {
		String valor = value.toString();
		if(!valor.startsWith("1")) {
			throw new ValidatorException(new FacesMessage("ISBN deveria começar com 1"));
		}
	}
	
	public String formAutor() {
		System.out.println("Chamando o formulário do autor");
		return "autor?faces-redirect=true"; //redirecionar pelo navegador
	}
}
