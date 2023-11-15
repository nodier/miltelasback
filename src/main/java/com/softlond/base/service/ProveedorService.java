package com.softlond.base.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
// import org.hibernate.annotations.common.util.impl.Log_.logger;
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
import org.springframework.web.client.RestTemplate;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Proveedor;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.AsignacionComprobanteDao;
import com.softlond.base.repository.ComprobanteEgresoDao;
import com.softlond.base.repository.ConceptoReciboEgresoDao;
import com.softlond.base.repository.FacturaCompraDao;
import com.softlond.base.repository.NotaCreditoDao;
import com.softlond.base.repository.NotaDebitoDao;
import com.softlond.base.repository.ProveedorDao;
import com.softlond.base.repository.UsuarioInformacionDao;

@Service
public class ProveedorService {

	private static final Logger logger = Logger.getLogger(ProveedorService.class);

	@Autowired
	private ProveedorDao proveedorDao;

	public ProveedorDao getProveedorDao() {
		return proveedorDao;
	}

	public void setProveedorDao(ProveedorDao proveedorDao) {
		this.proveedorDao = proveedorDao;
	}

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ComprobanteEgresoDao comprobanteEgresoDao;

	@Autowired
	private AsignacionComprobanteDao asignacionComprobanteDao;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private NotaDebitoDao ndDao;
	@Autowired
	private NotaCreditoDao ncDao;

	@Autowired
	private FacturaCompraDao facturaDao;

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> crearProveedor(Proveedor proveedor) {
		logger.info(proveedor.getProveedor());
		ResponseEntity<Object> respuesta;
		try {
			Proveedor proveedorBusqueda = proveedorDao.findByNitAndDigito(proveedor.getNit(), proveedor.getDigito());
			// if (proveedorBusqueda != null) {
			// logger.info("el proveedorBusqueda no es nulo");
			// logger.info(proveedorBusqueda);
			// throw new Exception();
			// }
			proveedorDao.save(proveedor);
			// String url =
			// "http://190.145.10.42:8080/api/v1/TApoloRestInterface/executequery";
			// RestTemplate rest = new RestTemplate();
			// Object[] o = rest.getForObject(url, Object[].class);
			// logger.info(o);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "creación de proveedore exitosa");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error creando proveedor" + e.getMessage() + " " + e.getStackTrace()[0].getLineNumber() + " "
					+ e.getStackTrace()[0].getClassName());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo crar un proveedor");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// @PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or
	// hasAuthority('USER') or hasAuthority('VENDEDOR')")
	// public ResponseEntity<Object> crearProveedorTercero(Proveedor proveedor) {
	// logger.info(proveedor);
	// ResponseEntity<Object> respuesta;
	// try {
	// Proveedor proveedorBusqueda =
	// proveedorDao.findByNitAndDigito(proveedor.getNit(), proveedor.getDigito());
	// // if (proveedorBusqueda != null) {
	// // logger.info("el proveedorBusqueda no es nulo");
	// // logger.info(proveedorBusqueda);
	// // throw new Exception();
	// // }
	// proveedorDao.save(proveedor);
	// String url =
	// "http://190.145.10.42:8080/api/v1/TApoloRestInterface/executequery";
	// RestTemplate rest = new RestTemplate();
	// Object[] o = rest.getForObject(url, Object[].class);
	// // logger.info(o);
	// respuesta = ResponseEntity.ok(HttpStatus.OK);
	// RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "creación de
	// proveedore exitosa");
	// respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
	// } catch (Exception e) {
	// respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
	// logger.error("Error creando proveedor" + e.getMessage() + " " +
	// e.getStackTrace()[0].getLineNumber() + " "
	// + e.getStackTrace()[0].getClassName());
	// RespuestaDto respuestaDto = new
	// RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
	// "No se pudo crar un proveedor");
	// respuesta = new ResponseEntity<>(respuestaDto,
	// HttpStatus.INTERNAL_SERVER_ERROR);
	// }
	// return respuesta;
	// }

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> actualizarProveedor(Proveedor proveedor) {
		ResponseEntity<Object> respuesta;
		try {
			proveedorDao.save(proveedor);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "creación de proveedore exitosa");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error creando proveedor" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo crar un proveedor");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> eliminarProveedor(Integer idProveedor) {
		ResponseEntity<Object> respuesta;
		try {
			proveedorDao.deleteById(idProveedor);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "eliminación de proveedore exitosa");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error creando proveedor" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo eliminar un proveedor");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> ObtenerProveedores(Integer page) {
		ResponseEntity<Object> respuesta;
		try {
			Pageable pageConfig = PageRequest.of(page, 10);
			Page<Proveedor> proveedores = proveedorDao.obtenerTodosProveedores(pageConfig);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de proveedores exitosa");
			respuestaDto.setObjetoRespuesta(proveedores);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error creando proveedor" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo obteneer los proveedores");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> ObtenerTodosProveedores() {
		ResponseEntity<Object> respuesta;
		try {
			List<Proveedor> proveedores = (List<Proveedor>) proveedorDao.findAll();
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de proveedores exitosa");
			respuestaDto.setObjetoRespuesta(proveedores);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error obteniendo proveedor" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo obteneer los proveedores");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> ObtenerNombreProveedores() {
		ResponseEntity<Object> respuesta;
		try {
			List<String> proveedores = proveedorDao.obtenerNombres();
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de proveedores exitosa");
			respuestaDto.setObjetoRespuesta(proveedores);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error creando proveedor" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo obteneer los proveedores");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerProveedoresConsulta(String nitNombre, Integer ciudad, Integer departamento,
			Integer barrio, Integer page) {
		ResponseEntity<Object> respuesta;
		Pageable paging = PageRequest.of(page, 10);
		try {
			List<Proveedor> proveedores;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQuery(query, nitNombre, ciudad, departamento, barrio);
			TypedQuery<Proveedor> proveedoresInfoQuery = (TypedQuery<Proveedor>) entityManager
					.createNativeQuery(query.toString(), Proveedor.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			proveedoresInfoQuery.setFirstResult(pageNumber * pageSize);
			proveedoresInfoQuery.setMaxResults(pageSize);
			proveedores = proveedoresInfoQuery.getResultList();
			generarQueryCantidad(queryCantidad, nitNombre, ciudad, departamento, barrio);
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<Proveedor> result = new PageImpl<Proveedor>(proveedores, paging, cantidadTotal);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de proveedores exitosa");
			respuestaDto.setObjetoRespuesta(result);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo proveedores " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo proveedores");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private void generarQuery(StringBuilder query, String nitNombre, Integer ciudad, Integer departamento,
			Integer barrio) {

		query.append(
				"select * from ter_proveedores p left join fc_factura_compra fc " + "on p.nid_proveedor = fc.nid_proveedor ");
		if (!nitNombre.equals("null")) {
			query.append("where concat(p.tnitocc,'--',p.t_proveedor)=" + "'" + nitNombre + "'");
		}
		if (departamento != 0 && !nitNombre.equals("null")) {
			query.append(" and p.nid_depto=" + departamento);
		} else if (departamento != 0) {
			query.append("where p.nid_depto=" + departamento);
		}
		if (ciudad != 0 && (!nitNombre.equals("null") || departamento != 0)) {
			query.append(" and p.nid_ciudad=" + ciudad);
		} else if (ciudad != 0) {
			query.append("where p.nid_ciudad=" + ciudad);
		}
		if (barrio != 0) {
			query.append(" and p.barrio=" + barrio);
		}
		query.append(" group by p.nid_proveedor order by p.t_proveedor");

	}

	private void generarQueryCantidad(StringBuilder query, String nitNombre, Integer ciudad, Integer departamento,
			Integer barrio) {
		query.append("SELECT count(*) FROM ter_proveedores ");
		if (!nitNombre.equals("null")) {
			query.append("where concat(tnitocc,'--',t_proveedor)=" + "'" + nitNombre + "'");
		}
		if (departamento != 0 && !nitNombre.equals("null")) {
			query.append(" and nid_depto=" + departamento);
		} else if (departamento != 0) {
			query.append("where nid_depto=" + departamento);
		}
		if (ciudad != 0 && (!nitNombre.equals("null") || departamento != 0)) {
			query.append(" and nid_ciudad=" + ciudad);
		} else if (ciudad != 0) {
			query.append("where nid_ciudad=" + ciudad);
		}
		if (barrio != 0) {
			query.append(" and barrio=" + barrio);
		}

	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerProveedoresRemision() {
		ResponseEntity<Object> respuesta;
		try {

			ArrayList<Proveedor> proveedores = this.proveedorDao.obtenerProveedores();
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de proveedores exitosa");
			respuestaDto.setObjetoRespuesta(proveedores);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error creando proveedor" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo obteneer los proveedores");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	/***
	 * Obtiene el valor de las facturas que estan en estado de credito para el
	 * proveedor
	 * 
	 * @return
	 */
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> ObtenerValorFacturasPorProveedor(Integer idProveedor) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			double totalFacturas = (facturaDao.obtenerTotalFacturasPorPagar(idProveedor, idSede) != null)
					? facturaDao.obtenerTotalFacturasPorPagar(idProveedor, idSede)
					: 0;
			double totalNotasCredito = (ncDao.obtenerTotalNotasCreditosPorPagar(idProveedor, idSede) != null)
					? ncDao.obtenerTotalNotasCreditosPorPagar(idProveedor, idSede)
					: 0;
			double totalNotasDebito = (ndDao.obtenerTotalNotasDebitosPorPagar(idProveedor, idSede) != null)
					? ndDao.obtenerTotalNotasDebitosPorPagar(idProveedor, idSede)
					: 0;
			Integer totalAbonos = comprobanteEgresoDao.obtenerValorAbonosProveedor(idProveedor, idSede)
					- comprobanteEgresoDao.obtenerValorAbonosProveedorFacturasPagadas(idProveedor, idSede);
			Integer totalDescuentos = comprobanteEgresoDao.obtenerValorDescuentosProveedor(idProveedor, idSede)
					- comprobanteEgresoDao.obtenerValorDescuentosProveedorFacturasPagadas(idProveedor, idSede);
			Integer totalAsignacion = asignacionComprobanteDao.obtenerTotalProveedor(idProveedor, idSede);
			double total = totalFacturas - totalNotasCredito + totalNotasDebito - totalAbonos - totalDescuentos
					- totalAsignacion;
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de proveedores exitosa");
			respuestaDto.setObjetoRespuesta(total);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error obteniendo valor " + e.getMessage() + " linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo obteneer los proveedores");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	public List<Proveedor> buscarPorPalabraClave(String palabra) {
		List<Proveedor> proveedores = proveedorDao.buscarPorPalabraClave(palabra.toLowerCase());
		return proveedores;
	}
}
