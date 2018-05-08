package br.com.caelum.livraria.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import br.com.caelum.livraria.modelo.Autor;
import br.com.caelum.livraria.modelo.Livro;

//gerenciamento da persistência no banco
public class DAO<T> {

	private final Class<T> classe;
		
	public DAO(Class<T> classe) {
		this.classe = classe;
	}
	
	public Class<T> getClasse() {
		return classe;
	}
	
	public void adiciona(T t) {
		//responsavel pela persistencia de entidades em uma transação
		EntityManager em = new JPAUtil().getEntityManager();
		//abre transação
		em.getTransaction().begin();
		//persiste o objeto
		em.persist(t);
		//commita a a transação
		em.getTransaction().commit();
		//fecha E.M
		em.close();
	}
	
	public void atualiza(T t) {
		EntityManager em = new JPAUtil().getEntityManager();
		em.getTransaction().begin();
		em.merge(t);
		em.getTransaction().commit();
		em.close();
	}
	
	public void remove(T t) {
		EntityManager em = new JPAUtil().getEntityManager();
		em.getTransaction().begin();
		em.remove(em.merge(t));
		em.getTransaction().commit();
		em.close();
	}

	public List<Autor> listaTodos() {
		EntityManager em = new JPAUtil().getEntityManager();
		em.getTransaction().begin();
		
		Query query =  em.createQuery("select a from Autor a");
		@SuppressWarnings("unchecked")
		List<Autor> autores = query.getResultList();
		
		em.close();
		
		return autores;
	}

	public List<Livro> listaLivros() {
		EntityManager em = new JPAUtil().getEntityManager();
		em.getTransaction().begin();
		
		Query query =  em.createQuery("select l from Livro l");
		@SuppressWarnings("unchecked")
		List<Livro> livros = query.getResultList();
		
		em.close();
		
		return livros;
	}
	
	public Autor buscaPorId(Integer autorId) {
		
		EntityManager em = new JPAUtil().getEntityManager();
		em.getTransaction().begin();
		
		Query query =  em.createQuery("select a from Autor a where a.id=" + autorId); //----colocar outra forma de query------
		Autor autor = (Autor) query.getSingleResult();
		
		em.close();
		
		return autor;
	}
}
