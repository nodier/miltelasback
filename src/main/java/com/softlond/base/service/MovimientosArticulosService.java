package com.softlond.base.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softlond.base.entity.ArticuloMovimientos;
import com.softlond.base.entity.ArticulosRemisionCompra;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.FacturaArticulos;
import com.softlond.base.entity.ReciboCajaVenta;
import com.softlond.base.entity.Traslado;
import com.softlond.base.repository.ArticuloMovimientoDao;
import com.softlond.base.repository.FacturaArticuloDao;
import com.softlond.base.repository.FacturaDao;
import com.softlond.base.repository.ReciboCajaVentaDao;
import com.softlond.base.repository.RemisionCompraDao;
import com.softlond.base.repository.RemisionVentaDao;
import com.softlond.base.repository.TrasladosDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.response.MovimientosArticulos;
import com.softlond.base.response.Paginacion;

@Service
public class MovimientosArticulosService {

	private static final Logger logger = Logger.getLogger(MovimientosArticulosService.class);

	final int ITEMS_POR_PAGINA = 10;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private FacturaArticuloDao facturaArticuloDao;
	
	@Autowired
	private FacturaDao facturaDao;
	
	@Autowired
	private FacturaArticuloService facturaArticuloService;
	
	@Autowired
	private RemisionVentaDao remisionVentaDao;
	
	@Autowired
	private RemisionCompraDao remisionCompraDao;
	
	@Autowired
	private ArticulosRemisionCompraService articulosRemisionCompraService;
	
	@Autowired
	private ReciboCajaVentaService reciboCajaVentaService;
	
	@Autowired
	ReciboCajaVentaDao reciboCajaDao;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@Autowired
	private TrasladosDao trasladosDao;
	
	@Autowired
	private ArticuloMovimientoDao articulosMovimientosDao;
	
	
	public List<MovimientosArticulos> obtenerMovimientosPorArticulo(int idSede, int idArticulo) {
		List<MovimientosArticulos> movs = new ArrayList<>();

		//Obtener facturas
		List<Factura> facturas = facturaDao.buscarPorIdArticulo(idSede, idArticulo);
		for (Factura f : facturas) {
			List<FacturaArticulos> facArticulos = facturaArticuloDao.obtenerFactArticuloCodigoYFactura(f.getId(), idArticulo);
			for (FacturaArticulos facArticulo : facArticulos) {
				MovimientosArticulos movimiento = MovimientosArticulos.convertirFactura(facArticulo,f);
				movs.add(movimiento);
			}
		}

		// Obtener Remisión Compra
		List<ArticulosRemisionCompra> remisionCompra = articulosRemisionCompraService.buscarPorIdArticulo(idSede, idArticulo);
		for (ArticulosRemisionCompra rc : remisionCompra) {
			MovimientosArticulos movimiento = MovimientosArticulos.convertirRemCompra(rc);
			movs.add(movimiento);
		}
		
		//obtener traslados
		List<Traslado> traslados = trasladosDao.obtenerTrasladoArticulo(idArticulo, idSede);
		for (Traslado traslado : traslados) {
			List<ArticuloMovimientos> movimientos = articulosMovimientosDao.obtenerMovimientosTrasladoArticulo(traslado.getId(), idArticulo);
			for (ArticuloMovimientos movimiento : movimientos) {
				MovimientosArticulos mv = MovimientosArticulos.convertirTraslado(traslado,movimiento);
				movs.add(mv);
			}
		}
		
		// Obtener Remisión Venta
		/*List<ReciboCajaVenta> reciboCajaVenta = reciboCajaVentaService.buscarPorIdArticulo2(idSede, idArticulo);
		for (ReciboCajaVenta rv : reciboCajaVenta) {
			FacturaArticulos factArticulos = facturaArticuloDao.buscarporIdRecibocaja(rv.getId());
			/*MovimientosArticulos movimiento = MovimientosArticulos.convertirRemVenta(rv, factArticulos);
			/*Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(movimiento.getNumero());
			if (abonos != null)
				movimiento.setSaldo(movimiento.getTotal() - abonos);
			movs.add(movimiento);
		}
		}*/
		return movs;
	}

	public Paginacion obtenerMovimientosPorArticuloPaginado(int idSede, int idArticulo, int pagina) {
		List<MovimientosArticulos> movs = obtenerMovimientosPorArticulo(idSede, idArticulo);
		return Paginacion.paginar(movs, ITEMS_POR_PAGINA, pagina);
	}

}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
					
	// Consulta avanzada de obtención de movimiento de articulos
	/*@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER')")
	public ResponseEntity<Object> obtenerArticuloMovimientoConsulta(Integer page, Integer local) {
		ResponseEntity<Object> respuesta;
		Pageable paging = PageRequest.of(page, 10);
		try {
			List<FacturaArticulos> sector;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQueryFiltros(query, local);			
			TypedQuery<FacturaArticulos> descuentosInfoQuery = (TypedQuery<FacturaArticulos>) entityManager.createNativeQuery(query.toString(), FacturaArticulos.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			descuentosInfoQuery.setFirstResult(pageNumber * pageSize);
			descuentosInfoQuery.setMaxResults(pageSize);
			sector = descuentosInfoQuery.getResultList();
			generarQueryFiltrosCantidad(queryCantidad, local);
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<FacturaArticulos> result = new PageImpl<FacturaArticulos>(sector, paging, cantidadTotal);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de descuento exitosa");
			respuestaDto.setObjetoRespuesta(result);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo descuento " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo descuento");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private void generarQueryFiltros(StringBuilder query, Integer local) {
		query.append("select * from inv_sector where");
		if (local != 0) {
			query.append(" id_local=" + local);
		}
	}

	private void generarQueryFiltrosCantidad(StringBuilder query, Integer local) {
		query.append("select count(*) from inv_sector where");
		if (local != 0) {
			query.append(" id_local=" + local);
		}
	}
}*/