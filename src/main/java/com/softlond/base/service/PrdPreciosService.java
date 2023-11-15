package com.softlond.base.service;

import java.math.BigInteger;
import java.util.ArrayList;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Precio;
import com.softlond.base.entity.Producto;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.response.ProductoCliente;

@Service
public class PrdPreciosService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private UsuarioInformacionDao usuarioInformacionDao;

	private static final Logger logger = Logger.getLogger(PrdPreciosService.class);

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerPreciosConsulta(String texto, String idClasificacion, Integer page) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Pageable paging = PageRequest.of(page, 10);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<Precio> precios;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQueryPrecios(query, texto, idClasificacion, idSede);
			
			TypedQuery<Precio> productosInfoQuery = (TypedQuery<Precio>) entityManager.createNativeQuery(query.toString(), Precio.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			productosInfoQuery.setFirstResult(pageNumber * pageSize);
			productosInfoQuery.setMaxResults(pageSize);
			precios = productosInfoQuery.getResultList();
			generarQueryCantidadPrecios(queryCantidad, texto, idClasificacion, idSede);
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<Precio> result = new PageImpl<Precio>(precios, paging, cantidadTotal);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de precios exitosa");
			respuestaDto.setObjetoRespuesta(result);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo precios " + e + "linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo precios");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	private void generarQueryPrecios(StringBuilder query, String texto, String idClasificacion, Integer idSede) {
		//query.append("select * from prd_precios pr join producto p on p.id=pr.nid_producto where pr.b_activo = 1 and pr.id_pyme = " + idSede);
		//query.append("select pre.*, p.* from prd_precios as pre inner join producto as p inner join (select CONCAT(pt.t_tipo, ' ', pr.t_referencia,' ', pp.t_presentacion, ' ', pc.t_color) as productox, pin.* from producto as pin inner join prd_colores as pc inner join prd_tipos as pt inner join prd_referencia as pr inner join prd_presentacion as pp on pin.id_color = pc.id and pin.tipo = pt.id and pin.referencia = pr.id and pin.id_presentacion = pp.id) as n on p.id = pre.nid_producto and p.id = n.id where pre.b_activo = 1 and pre.id_pyme ="+idSede);
		query.append("select pre.*, p.* from prd_precios as pre inner join producto as p inner join (select CONCAT(pt.t_tipo, ' ', pr.t_referencia,' ', pp.t_presentacion, ' ', pc.t_color) as productox, pin.* from producto as pin inner join prd_colores as pc inner join prd_tipos as pt inner join prd_referencia as pr inner join prd_presentacion as pp on pin.id_color = pc.id and pin.tipo = pt.id and pin.referencia = pr.id and pin.id_presentacion = pp.id) as n on p.id = pre.nid_producto and p.id = n.id where pre.id_pyme ="+idSede+" and pre.b_activo = 1");
		if (!texto.equals("null") ) {
			query.append(" and (n.productox like '%" + texto + "%' or p.codigo like '%" + texto + "%')");
		}
		if(!idClasificacion.equals("null")) {
			query.append(" and p.id_clasificacion = " + idClasificacion);
		}
		query.append(" order by n.productox");
	}
	
	private void generarQueryCantidadPrecios(StringBuilder query, String texto, String idClasificacion, Integer idSede) {
		//query.append("select count(*) from prd_precios pr join producto p on p.id=pr.nid_producto where pr.b_activo = 1 and pr.id_pyme = " + idSede);
		query.append("select count(*) from prd_precios as pre inner join producto as p inner join (select CONCAT(pt.t_tipo, ' ', pr.t_referencia,' ', pp.t_presentacion, ' ', pc.t_color) as productox, pin.* from producto as pin inner join prd_colores as pc inner join prd_tipos as pt inner join prd_referencia as pr inner join prd_presentacion as pp on pin.id_color = pc.id and pin.tipo = pt.id and pin.referencia = pr.id and pin.id_presentacion = pp.id) as n on p.id = pre.nid_producto and p.id = n.id where pre.b_activo = 1 and pre.id_pyme ="+idSede);
		if (!texto.equals("null") ) {
			query.append(" and (n.productox like '%" + texto + "%' or p.codigo like '%" + texto + "%')");
		}
		if(!idClasificacion.equals("null")) {
			query.append(" and p.id_clasificacion = " + idClasificacion);
		}
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerPreciosConsultaExport(String texto, String idClasificacion) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<Precio> precios;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQueryPrecios(query, texto, idClasificacion, idSede);
			TypedQuery<Precio> productosInfoQuery = (TypedQuery<Precio>) entityManager.createNativeQuery(query.toString(), Precio.class);
			precios = productosInfoQuery.getResultList();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de precios exitosa");
			respuestaDto.setObjetoRespuesta(precios);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo precios " + e + "linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo precios");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

}