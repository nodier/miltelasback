package com.softlond.base.service;

import java.math.BigInteger;
import java.sql.Date;
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
import com.softlond.base.entity.Clientes;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.NotaCredito;
import com.softlond.base.entity.Organizacion;
import com.softlond.base.entity.Prefijo;
import com.softlond.base.entity.Proveedor;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.AsignacionReciboDao;
import com.softlond.base.repository.ClientesDao;
import com.softlond.base.repository.ConceptosReciboCajaDao;
import com.softlond.base.repository.FacturaDao;
import com.softlond.base.repository.UsuarioInformacionDao;

@Service
public class ClientesService {

	private static final Logger logger = Logger.getLogger(UsuarioService.class);

	@Autowired
	private ClientesDao clientesDao;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ConceptosReciboCajaDao conceptosReciboCajaDao;

	@Autowired
	private AsignacionReciboDao asignacionReciboDao;

	@Autowired
	private FacturaDao facturaDao;
	
	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	// listar
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarClientes() {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {

			ArrayList<Clientes> cliente = this.clientesDao.obtenerClientes();

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de prefijos exitosa");
			respuestaDto.setObjetoRespuesta(cliente);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo usuarios " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo usuarios");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Obtener cliente
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> buscarCliente(Integer idCliente) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			Clientes cliente = this.clientesDao.obtenerCliente(idCliente);

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de prefijos exitosa");
			respuestaDto.setObjetoRespuesta(cliente);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo usuarios " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo usuarios");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Crear Cliente
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> crearCliente(Clientes clientes) {
		ResponseEntity<Object> respuesta;
		try {
			Clientes clientesBusqueda = clientesDao.findByNitocc(clientes.getNitocc());
			if (clientesBusqueda != null)
				throw new Exception();
			clientesDao.save(clientes);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "creación del cliente exitoso");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error creando el cliente" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo crear el cliente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Actualizar Cliente
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> actualizarCliente(Clientes clientes) {
		ResponseEntity<Object> respuesta;
		try {
			clientesDao.save(clientes);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Actualización del cliente exitoso");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error actualizando cliente" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo actualizar el cliente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Eliminar Cliente
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> eliminarCliente(Integer idCliente) {
		ResponseEntity<Object> respuesta;
		try {
			clientesDao.deleteById(idCliente);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "eliminación de cliente exitosa");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al eliminar el cliente" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo eliminar un cliente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Listar Todos los Clientes por pagina
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerTodosClientes(Integer page) {
		ResponseEntity<Object> respuesta;
		try {
			Pageable pageConfig = PageRequest.of(page, 10);
			Page<Clientes> clientes = clientesDao.obtenerTodosClientes(pageConfig);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de clientes exitosa");
			respuestaDto.setObjetoRespuesta(clientes);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al listar los clientes" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo obtener los clientes");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Listar todos los clientes
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> ObtenerClientes() {
		ResponseEntity<Object> respuesta;
		try {
			List<Clientes> clientes = (List<Clientes>) clientesDao.findAll();
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de clientes exitosa");
			respuestaDto.setObjetoRespuesta(clientes);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error obteniendo cliente" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo obtener los clientes");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Listar nombres de clientes
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> ObtenerNombreClientes() {
		ResponseEntity<Object> respuesta;
		try {
			List<String> clientes = clientesDao.obtenerNombres();
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de clientes exitosa");
			respuestaDto.setObjetoRespuesta(clientes);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al obtener el cliente" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo obtener los clientes");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Consulta avanzada de clientes
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerClientesConsulta(Integer cliente, Integer ciudad, Integer departamento,
			Integer barrio, Integer clasificacion, String activo, Integer page) {
		ResponseEntity<Object> respuesta;
		Pageable paging = PageRequest.of(page, 10);
		try {
			List<Clientes> clientes;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQuery(query, cliente, ciudad, departamento, barrio, clasificacion, activo);
			TypedQuery<Clientes> clientesInfoQuery = (TypedQuery<Clientes>) entityManager.createNativeQuery(query.toString(), Clientes.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			clientesInfoQuery.setFirstResult(pageNumber * pageSize);
			clientesInfoQuery.setMaxResults(pageSize);
			clientes = clientesInfoQuery.getResultList();
			generarQueryCantidad(queryCantidad, cliente, ciudad, departamento, barrio, clasificacion, activo);
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<Clientes> result = new PageImpl<Clientes>(clientes, paging, cantidadTotal);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de clientes exitosa");
			respuestaDto.setObjetoRespuesta(result);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo clientes " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo clientes");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private void generarQuery(StringBuilder query, Integer cliente, Integer ciudad, Integer departamento,
			Integer barrio, Integer clasificacion, String activo) {
		
		query.append("select * from clientes where");
		if (cliente != 0) {
			query.append(" id= " + cliente);
		}
		if (departamento != 0 && cliente != 0) {
			query.append(" and id_departamento=" + departamento);
		} else if (departamento != 0) {
			query.append(" id_departamento=" + departamento);
		}
		if (ciudad != 0 && (cliente != 0 || departamento != 0)) {
			query.append(" and id_ciudad=" + ciudad);
		} else if (ciudad != 0) {
			query.append(" id_ciudad=" + ciudad);
		}
		if (barrio != 0) {
			query.append(" and barrio=" + barrio);
		}
		if (clasificacion != 0 && (cliente != 0 || departamento != 0)) {
			query.append(" and id_clasificacion=" + clasificacion);
		} else if (clasificacion != 0) {
			query.append(" id_clasificacion=" + clasificacion);
		}
		if (!activo.equals("null") && (cliente != 0 || departamento != 0 || clasificacion != 0)) {
			query.append(" and activo=" + activo);
		} else if (!activo.equals("null")) {
			query.append(" activo=" + activo);
		}
		query.append(" order by id desc");
	}

	private void generarQueryCantidad(StringBuilder query, Integer cliente, Integer ciudad, Integer departamento,
			Integer barrio, Integer clasificacion, String activo) {
		query.append("SELECT count(*) FROM clientes where");
		if (cliente != 0) {
			query.append(" id= " + cliente);
		}
		if (departamento != 0 && cliente != 0) {
			query.append(" and id_departamento=" + departamento);
		} else if (departamento != 0) {
			query.append(" id_departamento=" + departamento);
		}
		if (ciudad != 0 && (cliente != 0 || departamento != 0)) {
			query.append(" and id_ciudad=" + ciudad);
		} else if (ciudad != 0) {
			query.append(" id_ciudad=" + ciudad);
		}
		if (barrio != 0) {
			query.append(" and barrio=" + barrio);
		}
		if (clasificacion != 0 && (cliente != 0 || departamento != 0)) {
			query.append(" and id_clasificacion=" + clasificacion);
		} else if (clasificacion != 0) {
			query.append(" id_clasificacion=" + clasificacion);
		}
		if (!activo.equals("null") && (cliente != 0 || departamento != 0 || clasificacion != 0)) {
			query.append(" and activo=" + activo);
		} else if (!activo.equals("null")) {
			query.append(" activo=" + activo);
		}
	}

	// listar
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarClientesCredito() {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {

			ArrayList<Clientes> cliente = this.clientesDao.obtenerClientesCredito();

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de clientes exitosa");
			respuestaDto.setObjetoRespuesta(cliente);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo usuarios " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo clientes");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	public List<Clientes> listarClientes(Integer idSede) {
		List<Clientes> lista = clientesDao.listarClientes(idSede);
		return lista;
	}

	// Consulta de retenciones por clientes
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerRetencionesClientes(String nombreApellido, Integer page) {
		ResponseEntity<Object> respuesta;
		Pageable paging = PageRequest.of(page, 10);
		try {
			List<Clientes> clientes;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQueryR(query, nombreApellido);
			TypedQuery<Clientes> clientesInfoQuery = (TypedQuery<Clientes>) entityManager.createNativeQuery(query.toString(), Clientes.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			clientesInfoQuery.setFirstResult(pageNumber * pageSize);
			clientesInfoQuery.setMaxResults(pageSize);
			clientes = clientesInfoQuery.getResultList();
			generarQueryCantidadR(queryCantidad, nombreApellido);
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<Clientes> result = new PageImpl<Clientes>(clientes, paging, cantidadTotal);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de retencion por clientes exitosa");
			respuestaDto.setObjetoRespuesta(result);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo retencion por clientes " + e + " Linea error: "
					+ e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo clientes");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private void generarQueryR(StringBuilder query, String nombreApellido) {
		query.append("select * from clientes where");
		if (!nombreApellido.equals("null")) {
			query.append(" concat(nombres,'--',apellidos)=" + "'" + nombreApellido + "'");
		}

		query.append(" order by id desc");
	}

	private void generarQueryCantidadR(StringBuilder query, String nombreApellido) {
		query.append("SELECT count(*) FROM clientes where");
		if (!nombreApellido.equals("null")) {
			query.append(" concat(nombres,'--',apellidos)=" + "'" + nombreApellido + "'");
		}

	}

	// Crear Cliente
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerCupo(Clientes cliente) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			Integer total = facturaDao.obtenerTotalFacturasPorPagar(cliente.getId(), idSede);
			Integer abonos = conceptosReciboCajaDao.totalConceptosClienteFacturas(cliente.getId(), idSede);
			Integer descuentos = conceptosReciboCajaDao.obtenerTotalDescuentoClienteFacturas(cliente.getId(), idSede);
			Integer asignacion = asignacionReciboDao.asignacionFacturaCliente(cliente.getId(), idSede);
			Integer cupo = 0;
			if (cliente.getCupoCredito() != null) {
				cupo = (int) (cliente.getCupoCredito() - total + abonos + descuentos + asignacion);
			}
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtener cupo exitoso");
			respuestaDto.setObjetoRespuesta(cupo);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error obteniendo cupo " + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo cupo");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerSaldo(Integer idCliente) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			Integer total = facturaDao.obtenerTotalFacturasPorPagar(idCliente,idSede);
			Integer abonos = conceptosReciboCajaDao.totalConceptosClienteFacturas(idCliente, idSede);
			Integer descuentos = conceptosReciboCajaDao.obtenerTotalDescuentoClienteFacturas(idCliente, idSede);
			Integer asignacion = asignacionReciboDao.asignacionFacturaCliente(idCliente, idSede);
			Integer saldo = (int) (total - abonos - descuentos - asignacion);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtener cupo exitoso");
			respuestaDto.setObjetoRespuesta(saldo);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error obteniendo cupo " + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo cupo");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarClientesTexto(String texto) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {

			ArrayList<Clientes> cliente = this.clientesDao.obtenerClientestexto(texto);

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de prefijos exitosa");
			respuestaDto.setObjetoRespuesta(cliente);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo usuarios " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo usuarios");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

}
