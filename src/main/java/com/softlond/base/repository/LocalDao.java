package com.softlond.base.repository;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;


import com.softlond.base.entity.InveLocal;

@Transactional
public interface LocalDao extends CrudRepository<InveLocal, Integer> {
	
	@Query(value = "SELECT * FROM inve_local", nativeQuery = true)
	public List<InveLocal> listarLocaless(Integer idSede);
	
    @Query(value = "SELECT * FROM inve_local WHERE id_sede =?1", nativeQuery = true)
    public Page<InveLocal> listarLocales(Integer idSede, Pageable page);
    
	@Query(value = "SELECT * FROM inve_local WHERE id_sede =?1", nativeQuery = true)
	public List<InveLocal> listarLocal(Integer idSede);
    
}
