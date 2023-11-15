package com.softlond.base.service;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.FacturaArticulos;
import com.softlond.base.repository.FacturaArticuloDao;
import com.softlond.base.repository.UsuarioInformacionDao;

@Service
public class ProductosPorClienteService {
	
	private static final Logger logger = Logger.getLogger(FacturaService.class);
	
	@Autowired
	private FacturaArticuloDao facturaArticuloDao;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	
	//Consulta avanzada de productos por cliente
			@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
			public ResponseEntity<Object> obtenerProductoPorClienteConsulta(String fechaInicial, String fechaFinal,  
					Integer cliente,  Integer page) {
				ResponseEntity<Object> respuesta;
				Pageable paging = PageRequest.of(page, 10);
				try {
					List<Factura> facturas;
					StringBuilder query = new StringBuilder();
					StringBuilder queryCantidad = new StringBuilder();
					generarQuery(query, fechaInicial, fechaFinal, cliente);
					TypedQuery<Factura> facturasInfoQuery = (TypedQuery<Factura>) entityManager.createNativeQuery(query.toString(), Factura.class);
					int pageNumber =paging.getPageNumber();
				    int pageSize = paging.getPageSize();
				    facturasInfoQuery.setFirstResult(pageNumber*pageSize);
				    facturasInfoQuery.setMaxResults(pageSize);
				    facturas = facturasInfoQuery.getResultList();
				    for (Factura factura : facturas) {
						List<FacturaArticulos> articulos = facturaArticuloDao.findAllBySede(factura.getId());
						factura.setFacArticulos(articulos);
					}
					generarQueryCantidad(queryCantidad, fechaInicial, fechaFinal, cliente);
					Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
					BigInteger countResult = (BigInteger)cantidad.getSingleResult();
				    Integer cantidadTotal = countResult.intValue();
				    Page<Factura> result = new PageImpl<Factura>(facturas,paging,cantidadTotal);
					respuesta = ResponseEntity.ok(HttpStatus.OK);
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de productos por cliente exitosa");
					respuestaDto.setObjetoRespuesta(result);
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
				} catch (Exception e) {
					logger.error("Error obteniendo de productos por cliente " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo de productos por cliente");
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
				}
				return respuesta;
			}
			
			
			private void generarQuery(StringBuilder query, String fechaInicial, String fechaFinal,  
					Integer cliente) {
				query.append("select * from fac_facturas where");
				
				if(!fechaInicial.equals("null") && fechaFinal.equals("null")) {
					query.append(" date_format(date(d_fecha_venta),'%Y-%m-%d')" + "= date('"+fechaInicial+"')");
				}
				else if(fechaInicial.equals("null") && !fechaFinal.equals("null")) {
					query.append(" date_format(date(d_fecha_venta),'%Y-%m-%d')" + "= date('"+fechaFinal+"')");
				}
				else if(!fechaInicial.equals("null") && !fechaFinal.equals("null")) { 
					query.append(" date_format(date(d_fecha_venta),'%Y-%m-%d')" + "between " + "date_format(date('"+fechaInicial+"'),'%Y-%m-%d')" + " and "+ "date_format(date('"+fechaFinal+"'),'%Y-%m-%d')");
				}
				
				if(cliente!=0 && (!fechaInicial.equals("null") && !fechaFinal.equals("null"))){
					query.append(" and nid_cliente=" + cliente);
				}
				
				else if(cliente!=0) {
					query.append(" nid_cliente="+ cliente);
				}
				
			}
			
			
			private void generarQueryCantidad(StringBuilder query, String fechaInicial, String fechaFinal,  
					Integer cliente) {
				query.append("SELECT count(*) FROM fac_facturas where");
				
				if(!fechaInicial.equals("null") && fechaFinal.equals("null")) {
					query.append(" date_format(date(d_fecha_venta),'%Y-%m-%d')" + "= date('"+fechaInicial+"')");
				}
				else if(fechaInicial.equals("null") && !fechaFinal.equals("null")) {
					query.append(" date_format(date(d_fecha_venta),'%Y-%m-%d')" + "= date('"+fechaFinal+"')");
				}
				else if(!fechaInicial.equals("null") && !fechaFinal.equals("null")) { 
					query.append(" date_format(date(d_fecha_venta),'%Y-%m-%d')" + "between " + "date_format(date('"+fechaInicial+"'),'%Y-%m-%d')" + " and "+ "date_format(date('"+fechaFinal+"'),'%Y-%m-%d')");
				}
				
				if(cliente!=0 && (!fechaInicial.equals("null") && !fechaFinal.equals("null"))){
					query.append(" and nid_cliente=" + cliente);
				}
				
				else if(cliente!=0) {
					query.append(" nid_cliente="+ cliente);
				}
				
			}
}
