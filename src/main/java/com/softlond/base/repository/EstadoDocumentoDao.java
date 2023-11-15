package com.softlond.base.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.EstadoDocumento;

public interface EstadoDocumentoDao extends CrudRepository<EstadoDocumento, Integer> {

	@Query(value = "SELECT * FROM config_doc_estados", nativeQuery = true)
	public ArrayList<EstadoDocumento> obtenerEstadosDocumento();

	@Query(value = "select t_estado from config_doc_estados", nativeQuery = true)
	public List<String> obtenerNombresEstados();

	@Query(value = "SELECT * FROM config_doc_estados where t_moc in (6,7,8)", nativeQuery = true)
	public ArrayList<EstadoDocumento> obtenerEstadosDocumentoTraslado();

	@Query(value = "SELECT * from config_doc_estados where t_moc = 1 ", nativeQuery = true)
	public EstadoDocumento estadoAsignado();
}
