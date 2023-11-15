package com.softlond.base.repository;

import com.softlond.base.entity.ConsecutivoEgresoSede;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ConsecutivoEgresoSedeDao extends CrudRepository<ConsecutivoEgresoSede, Integer> {
    @Query(value = "SELECT * FROM consecutivo_egreso_por_sede WHERE id_sede = :idSede and valor_actual = (select max(valor_actual) from consecutivo_egreso_por_sede where id_sede = :idSede) LIMIT 1", nativeQuery = true)
    public ConsecutivoEgresoSede buscarConsecutivoPorSede(Integer idSede);
    
    @Query(value = "select max(valor_actual) from consecutivo_egreso_por_sede where id_sede = ?1", nativeQuery = true)
    public Integer obtenerNumeroMaximo(Integer idSede);
}
