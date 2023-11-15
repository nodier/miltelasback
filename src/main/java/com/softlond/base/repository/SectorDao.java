package com.softlond.base.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.softlond.base.entity.InvSector;

public interface SectorDao extends CrudRepository<InvSector, Integer> {
	
	 @Query(value = "SELECT * FROM inv_sector WHERE id_local =?1", nativeQuery = true)
	    public Page<InvSector> buscarSectorPorLocal(Integer idLocal, Pageable page);
    
	  @Query(value = "select count(*) from inv_sector where id_local= ?1", nativeQuery = true)
		public Integer cantidadSectores(Integer id);
	  
	  @Query(value="SELECT * FROM inv_sector WHERE id_local = ?", nativeQuery = true)
		public List<InvSector> obtenerLocalPorId(Integer idLocal);
	  
		@Query(value="SELECT * FROM inv_sector where id_local = ?1", nativeQuery = true)
		public List<InvSector> obtenerSectorLocal(Integer idLocal);
}
