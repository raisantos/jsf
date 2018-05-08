package br.com.caelum.livraria.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
	//carrega a configuração persistence.xml. Cria uma entidade baseada na unidade de persistencia do xml. retorna uma Entity Manager
	// ajuda na inicialização do JPA, garantindo que exista apenas uma EntityManagerFactory
	private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("livraria");
	
	public EntityManager getEntityManager() {
		return entityManagerFactory.createEntityManager(); //possui os principais metodos jpa
	}
}