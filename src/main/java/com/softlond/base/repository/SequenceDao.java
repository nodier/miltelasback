package com.softlond.base.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.Sequence;
import org.springframework.data.jpa.repository.Modifying;

@Transactional
public interface SequenceDao extends CrudRepository<Sequence, Integer> {

	@Query(nativeQuery = true, value = "select getNextSeq(?1, ?2)")
	public Integer sequencia(Integer idSede, Integer idPrefijo);

	@Query(nativeQuery = true, value = "select getnextSecuencia(?1, ?2, ?3)")
	public Integer sequenciaNotaCredito(String nombrePrefijo, Integer idSede, Integer idPrefijo);

	public Optional<Sequence> findByIdSedeAndValorSequenciaAndIdPrefijo(Integer idSede, Integer valorSequencia,
			Integer idPrefijo);

	@Query(nativeQuery = true, value = "select * from _sequence where seq_prefijo = ?2 and seq_sede = ?1 and seq_val = (select max(seq_val) from _sequence where seq_prefijo = ?2 and seq_sede = ?1)")
	public Optional<Sequence> findByIdSedeAndIdPrefijo(Integer idSede, Integer idPrefijo);

	@Query(nativeQuery = true, value = "select * from _sequence s join prefijo p on p.id=seq_prefijo where seq_val = (select max(seq_val) from _sequence s join prefijo p on p.id=seq_prefijo where seq_sede = ? and tipo_documento='Remision venta') and tipo_documento='Remision venta' limit 1")
	public Optional<Sequence> SequenciaRemision(Integer idSede);

	@Query(nativeQuery = true, value = "select getnextSecuencia(?1, ?2, ?3)")
	public Integer sequenciaNueva(String nombrePrefijo, Integer idSede, Integer idPrefijo);

	@Query(nativeQuery = true, value = "select seq_val from sequencia where seq_name = ?1 and id_sede = ?2 and id_prefijo = ?3")
	public Integer sequenciaNuevaInforme(String nombrePrefijo, Integer idSede,
			Integer idPrefijo);

	@Query(nativeQuery = true, value = "select seq_val from sequencia where id_prefijo = ?2 and id_sede = ?1")
	public Integer obtenerUltimaSecuencia(Integer idSede, Integer idPrefijo);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "insert into sequencia(seq_name, seq_val, id_prefijo, id_sede)\n"
			+ "   values (?1, ?2, ?3, ?4);")
	public void ObtenerSequencia2(String Nombre, Integer Secuencia, Integer idSede, Integer Prefijo);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "delete from sequencia where id = ?1")
	public void EliminarSequencia(Integer id);

	// @Query(nativeQuery = true, value = "select seq_val from sequencia where
	// id_sede =?1 and seq_name= ?2")
	// public Integer ObtenerSequencia(Integer idSede, String Prefijo);

	@Query(nativeQuery = true, value = "select seq_val from sequencia where id_sede =?1 and seq_name= ?2 and id_prefijo= ?3")
	public Integer ObtenerSequencia(Integer idSede, String Prefijo, Integer idPrefijo);

	@Query(nativeQuery = true, value = "select id from sequencia where id_sede =?1 and seq_name= ?2 and id_prefijo= ?3 ")
	public Integer ObtenerSequenciaId(Integer idSede, String Prefijo, Integer idPrefijo);

}
