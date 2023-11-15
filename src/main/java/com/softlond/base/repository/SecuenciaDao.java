package com.softlond.base.repository;

import com.softlond.base.entity.Secuencia;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SecuenciaDao extends CrudRepository<Secuencia, Integer> {
    @Query(value = "SELECT * FROM _secuencias WHERE tipo_relacion = 'SEDE' AND id_relacion = :sede LIMIT 1", nativeQuery = true)
    public Secuencia buscarPorSede(Integer sede);
}
