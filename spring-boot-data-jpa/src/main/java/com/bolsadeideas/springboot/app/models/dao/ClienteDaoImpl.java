package com.bolsadeideas.springboot.app.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

@Repository("clienteDaoJPA")
public class ClienteDaoImpl {
/*
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Cliente> findAll() {

		return entityManager.createQuery("from Cliente").getResultList();
	}

	
	@Override
	public void save(Cliente cliente) { 
		if (cliente.getId() != null && cliente.getId() > 0) {
			entityManager.merge(cliente); //actualiza los datos de cliente
		}else {
			entityManager.persist(cliente);//persiste el objeto cliente en bbdd
		}
	}

	
	@Override
	public Cliente findOne(Long id) {
		return entityManager.find(Cliente.class, id);	
	}

	@Override
	public void delete(Long id) {
		//Cliente cliente = findOne(id); // buscamos el id
		//entityManager.remove(cliente);
		entityManager.remove(findOne(id));
		
	}
*/
}
