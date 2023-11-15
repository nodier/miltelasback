package com.softlond.base.repository;

import com.softlond.base.entity.Ciudad;
import com.softlond.base.entity.PrdReferencia;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PrdReferenciaDao extends CrudRepository<PrdReferencia, Integer> {
	
	@Query(value = "select * from prd_referencia where id_tipo = ?1", nativeQuery = true)
	public Page<PrdReferencia> referenciasPorTipo(Integer idTipo, Pageable page);
	
	@Query(value = "select ifnull(count(*),0) from producto where referencia = ?1 and tipo = ?2", nativeQuery = true)
	Integer cantidadProductos(Integer idReferencia, Integer idtipo);
	
	@Query(value = "select * from prd_referencia where id_tipo = :idTipo order by t_referencia ", nativeQuery = true)
	ArrayList<PrdReferencia> buscarPorTipo(@Param ("idTipo") Integer idTipo);
	@Query(value = "select * from prd_referencia where t_referencia like %?1%", nativeQuery = true)
	public List<PrdReferencia> referenciasPorTipo(String texto);
	
	@Query(value="select max(convert(tid_referencia, UNSIGNED INTEGER)) from prd_referencia", nativeQuery = true)
	public Integer numerodReferencia();
}
