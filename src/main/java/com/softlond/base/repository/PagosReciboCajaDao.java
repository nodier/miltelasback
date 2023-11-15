package com.softlond.base.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.google.cloud.Date;
import com.softlond.base.entity.FormasPago;
import com.softlond.base.entity.PagosReciboCaja;

@Transactional
public interface PagosReciboCajaDao extends CrudRepository  <PagosReciboCaja, Integer>{
	
	@Query(value="SELECT * FROM pagos_recibo_caja WHERE id_recibo_caja=?",nativeQuery = true)
	public ArrayList<PagosReciboCaja> findPagos(Integer idReciboCaja);
	
	@Query(value="DELETE FROM pagos_recibo_caja WHERE fecha=?",nativeQuery = true)
	public void deleteByFecha ( java.sql.Date fecha);
	
	@Query(value="SELECT fecha FROM pagos_recibo_caja WHERE id_recibo_caja=?",nativeQuery = true)
	public Date buscarFecha(Integer id);
	
	@Query(value="SELECT * FROM pagos_recibo_caja AS p JOIN rcb_rbocajaventa AS r ON "
			+ "r.id = p.id_recibo_caja INNER JOIN fac_facturas AS f ON r.id = f.id_recibo_caja AND f.nid_factura =?", nativeQuery = true)
	public ArrayList<PagosReciboCaja> pagosCliente(Integer idFactura);

	@Query(value = "select * from pagos_recibo_caja pag join rcb_rbocajaventa rcb on rcb.id = pag.id_recibo_caja\r\n"
			+ "where id_caja=?1 and date_format(date(fecha_pago),'%Y-%m-%d') = date(?2)", nativeQuery = true)
	List<PagosReciboCaja> obtenerPagosInformeCaja(Integer idCaja, String fecha);
	
	@Query(value = "select * from pagos_recibo_caja pag join rcb_rbocajaventa rcb on rcb.id = pag.id_recibo_caja\r\n"
			+ "where id_sede=?1 and date_format(date(fecha_pago),'%Y-%m-%d') = date(?2)", nativeQuery = true)
	List<PagosReciboCaja> obtenerPagosInformeSede(Integer idSede, String fecha);
	
	@Modifying
	@Query(value="DELETE FROM pagos_recibo_caja WHERE id_recibo_caja=?",nativeQuery = true)
	public void borrarPagosRecibo (Integer idRecibo);
}
