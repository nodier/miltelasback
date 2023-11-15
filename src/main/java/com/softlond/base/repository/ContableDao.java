package com.softlond.base.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

// import com.google.cloud.Date;
import com.softlond.base.entity.Caja;
import com.softlond.base.entity.ContableM;
import com.softlond.base.entity.ContableMD;
import com.softlond.base.entity.Organizacion;

public interface ContableDao extends CrudRepository<ContableMD, Integer> {

	@Query(value = "select * from contablemd c where c.id=?1", nativeQuery = true)
	public ArrayList<ContableMD> BuscarContablePorId(Integer contableId);

	@Query(value = "select * from contablemd c where c.numero LIKE %:contableNumero%", nativeQuery = true)
	public ArrayList<ContableMD> BuscarContablePorNumero(String contableNumero);

	@Query(value = "select * from contablemd c where c.fecha<=CURDATE() and c.is_enviado_mekano=false", nativeQuery = true)
	public ArrayList<ContableMD> BuscarContablePorNoEnviado();

	@Query(value = "select count(*) from contablemd c where c.fecha=:fecha", nativeQuery = true)
	public Integer existeInforme(Date fecha);
}
