package com.softlond.base.repository;

import com.softlond.base.entity.PrefijoEgreso;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PrefijoEgresoDao extends CrudRepository<PrefijoEgreso, Integer> {
	
    @Query(value = "SELECT * FROM prefijo_egreso where id_sede = ?", nativeQuery = true)
    public List<PrefijoEgreso> buscarPrefijosEgreso(Integer idSede);

    @Query(value = "SELECT * FROM prefijo_egreso WHERE tipo_proveedores = :prov AND tipo_gastos = :gastos AND tipo_nomina = :servicios and id_sede =:idSede", nativeQuery = true)
    public List<PrefijoEgreso> buscarPrefijosEgresoPorTipos(
            boolean prov,
            boolean gastos,
            boolean servicios,
            Integer idSede
    );
    
    @Query(value = "select count(*) from prefijo_egreso where inicio is not null and id_sede = ?", nativeQuery = true)
    public Integer validarExistenciaValorInicialPrefijo(Integer idSede);
    
    @Query(value = "select * from prefijo_egreso where inicio is not null and id = ?1 and id_sede = ?2", nativeQuery = true)
    public Optional<PrefijoEgreso> validarExistenciaValorInicialPrefijo(Integer idPrefijo, Integer idSede);
    
    @Query(value = "select * from prefijo_egreso where inicio is not null and id_sede = ?1", nativeQuery = true)
    public Optional<PrefijoEgreso> obtenerPrefijoConValorSede(Integer idSede);
}
