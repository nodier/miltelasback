package com.softlond.base.repository;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.DocumentosCuentas;

@Transactional
public interface DocumentosCuentasDao extends CrudRepository <DocumentosCuentas, Integer> {

	@Query(value = "SELECT *  FROM documentos_cuenta WHERE id_sede =?", nativeQuery = true)
	public ArrayList<DocumentosCuentas> obtenerDocumentosCuentas(Integer idSede);
}
