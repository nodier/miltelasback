package com.softlond.base.repository;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.ConceptoNotaDebito;
import com.softlond.base.entity.ConceptoNotaDebitoCliente;

@Transactional
public interface ConceptoNotaDebitoClienteDao extends CrudRepository<ConceptoNotaDebitoCliente, Integer> {

	// ------------------
	@Query(value = "SELECT * FROM concepto_nota_debito_cliente WHERE id_nota_debito=?1", nativeQuery = true)
	public ArrayList<ConceptoNotaDebitoCliente> obtenerConceptoIdNotaDebito(Integer idNotaDebito);
	
	@Modifying
	@Query(value = "DELETE FROM concepto_nota_debito_cliente WHERE id_nota_debito=?1", nativeQuery = true)
	public void eliminarConceptosNotasDebito(Integer idNotaDebito);
	
}
