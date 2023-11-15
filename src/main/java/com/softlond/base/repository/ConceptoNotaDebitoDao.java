package com.softlond.base.repository;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.ConceptoNotaDebito;

@Transactional
public interface ConceptoNotaDebitoDao extends CrudRepository<ConceptoNotaDebito, Integer> {

	// ------------------
	@Query(value = "SELECT * FROM concepto_nota_debito WHERE id_nota_debito=?1", nativeQuery = true)
	public ArrayList<ConceptoNotaDebito> obtenerConceptoIdNotaDebito(Integer idNotaDebito);
	
	@Modifying
	@Query(value = "DELETE FROM concepto_nota_debito WHERE id_nota_debito=?1", nativeQuery = true)
	public void eliminarConceptosNotasDebito(Integer idNotaDebito);

}
