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
import org.springframework.web.bind.annotation.RequestBody;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.InvSector;
import com.softlond.base.entity.InveLocal;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.LocalDao;
import com.softlond.base.repository.SectorDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.response.Locales;
import com.softlond.base.response.Paginacion;

@Service
public class LocalService {

	private static final Logger logger = Logger.getLogger(LocalService.class);

	final int ITEMS_POR_PAGINA = 10;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private LocalDao localDao;

	@Autowired
	private SectorDao sectorDao;

	@PersistenceContext
	private EntityManager entityManager;

	// listar
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarLocaless() {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		try {

			List<InveLocal> locales = this.localDao.listarLocaless(usuarioInformacion.getIdOrganizacion().getId());
			for (InveLocal local : locales) {
				List<InvSector> sectores = sectorDao.obtenerSectorLocal(local.getId());
				local.setSectores(sectores);
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de locales exitoso");
			respuestaDto.setObjetoRespuesta(locales);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo locales " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo locales");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// listar todos los locales por sede
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarLocales(Integer page) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());

			Pageable pageConfig = PageRequest.of(page, 10);

			int idSede = usuarioInformacion.getIdOrganizacion().getId();			
			Page<InveLocal> facturas = localDao.listarLocales(idSede, pageConfig);			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de facturas exitosa");

			respuestaDto.setObjetoRespuesta(facturas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener las facturas" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,"Error al obtener las facturas " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> crearLocal(@RequestBody InveLocal local) {
	    
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());

		try {
			local.setSede(usuarioInformacion.getIdOrganizacion());
			// int idSede = usuarioInformacion.getIdOrganizacion().getId();

			InveLocal guardado = this.localDao.save(local);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito creando el local");
			respuestaDto.setObjetoRespuesta(guardado);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error en la creación del local" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en la creación del local " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> actualizarLocal(@RequestBody InveLocal local) {
	    
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		InformacionUsuario usuarioInformacionUpdate = this.usuarioInformacionDao.buscarPorId(local.getResponsable().getId());
		List<InvSector> sectores = this.sectorDao.obtenerSectorLocal(local.getId());
		
		for(InvSector guardarSector : sectores) {
		    guardarSector.setIdLocal(local);
		}
		
		local.setSectores(sectores);
		local.setResponsable(usuarioInformacionUpdate);

		try {
			local.setSede(usuarioInformacion.getIdOrganizacion());
			InveLocal guardado = this.localDao.save(local);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito actualizando el local");
			respuestaDto.setObjetoRespuesta(guardado);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error en la actualizacion del local" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en la actualizacion del local " + e.getMessage());
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	// Eliminar Local
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> eliminarLocal(Integer idLocal) {
		ResponseEntity<Object> respuesta;
		try {
			localDao.deleteById(idLocal);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "eliminación del local exitoso");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al eliminar el local " + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo eliminar un local " + e.getMessage());
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Forma de eliminar Santiago

	// @PreAuthorize("hasAuthority('SUPER') or hasAuthority('TECNICO') or
	// hasAuthority('COORDINADOR') or hasAuthority('USUARIO')")
	public ResponseEntity<Object> eliminarLocalSi(Integer id) {
		ResponseEntity<Object> respuesta;
		try {
			localDao.deleteById(id);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "eliminación de la actividad exitosa");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al eliminar la actividad" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo eliminar la actividad");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	public List<Locales> listarLocales(int idSede) {
		List<Locales> vecom = new ArrayList<>();

		// Obtener locales
		List<InveLocal> locales = localDao.listarLocal(idSede);
	
		for (InveLocal l : locales) {
			Locales local = Locales.convertirLocales(l);
			Integer sectores = sectorDao.cantidadSectores(l.getId());

			local.setSectores(sectores);
			vecom.add(local);
		}
		return vecom;
	}

	public Paginacion listarLocalesPaginado(int idSede, int pagina) {
		List<Locales> vecom = listarLocales(idSede);
		return Paginacion.paginar(vecom, ITEMS_POR_PAGINA, pagina);
	}

	// Busqueda avanzada locales
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerLocal(Integer idLocal, Integer page) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		Pageable paging = PageRequest.of(page, 10);
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		try {
			List<InveLocal> productos;
			List<Locales> Pnoexistentes = new ArrayList<Locales>();
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQueryProductosNoExistentesFiltros(usuarioInformacion.getIdOrganizacion().getId(), idLocal, query);
			TypedQuery<InveLocal> productosInfoQuery = (TypedQuery<InveLocal>) entityManager.createNativeQuery(query.toString(), InveLocal.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			productosInfoQuery.setFirstResult(pageNumber * pageSize);
			productosInfoQuery.setMaxResults(pageSize);
			productos = productosInfoQuery.getResultList();
			generarQueryCantidadProductosFiltros(usuarioInformacion.getIdOrganizacion().getId(), idLocal,
					queryCantidad);
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<InveLocal> result = new PageImpl<InveLocal>(productos, paging, cantidadTotal);
			for (InveLocal producto : result) {
				Locales noexistentes = Locales.convertirLocales(producto);
				Integer sectores = sectorDao.cantidadSectores(producto.getId());
				noexistentes.setSectores(sectores);
				Pnoexistentes.add(noexistentes);
			}
			Page<Locales> resultProductos = new PageImpl<Locales>(Pnoexistentes, paging, cantidadTotal);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de productos exitosa");
			respuestaDto.setObjetoRespuesta(resultProductos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo productos " + e + "linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo productos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	public void generarQueryProductosNoExistentesFiltros(Integer idSede, Integer idLocal, StringBuilder query) {
		query.append("select * from inve_local where id_sede=");
		query.append("" + idSede);

		if (idLocal != 0) {
			query.append(" and id = " + idLocal);
		}
	}

	public void generarQueryCantidadProductosFiltros(Integer idSede, Integer idLocal, StringBuilder query) {
		query.append("select count(*) from inve_local where id_sede=");

		query.append("" + idSede);

		if (idLocal != 0) {
			query.append(" and id = " + idLocal);
		}
	}

	// Busqueda avanzada locales
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerLocal2(Integer page) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		Pageable paging = PageRequest.of(page, 10);
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		try {
			List<InveLocal> productos;
			List<Locales> Pnoexistentes = new ArrayList<Locales>();
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQueryProductosNoExistentesFiltros2(usuarioInformacion.getIdOrganizacion().getId(), query);
			TypedQuery<InveLocal> productosInfoQuery = (TypedQuery<InveLocal>) entityManager
					.createNativeQuery(query.toString(), InveLocal.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			productosInfoQuery.setFirstResult(pageNumber * pageSize);
			productosInfoQuery.setMaxResults(pageSize);
			productos = productosInfoQuery.getResultList();
			generarQueryCantidadProductosFiltros2(usuarioInformacion.getIdOrganizacion().getId(), queryCantidad);
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<InveLocal> result = new PageImpl<InveLocal>(productos, paging, cantidadTotal);
			for (InveLocal producto : result) {
				Locales noexistentes = Locales.convertirLocales(producto);
				Integer sectores = sectorDao.cantidadSectores(producto.getId());

				noexistentes.setSectores(sectores);

				Pnoexistentes.add(noexistentes);

			}
			Page<Locales> resultProductos = new PageImpl<Locales>(Pnoexistentes, paging, cantidadTotal);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de productos exitosa");
			respuestaDto.setObjetoRespuesta(resultProductos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo productos " + e + "linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo productos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	public void generarQueryProductosNoExistentesFiltros2(Integer idSede, StringBuilder query) {
		query.append("select * from inve_local where id_sede=");
		query.append("" + idSede);
	}

	public void generarQueryCantidadProductosFiltros2(Integer idSede, StringBuilder query) {
		query.append("select count(*) from inve_local where id_sede=");

		query.append("" + idSede);
	}

	// listar todos los locales por sede
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarLocalesSede() {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());
			int idSede = usuarioInformacion.getIdOrganizacion().getId();
			List<InveLocal> locales = localDao.listarLocal(idSede);
			for (InveLocal local : locales) {
				List<InvSector> sectores = sectorDao.obtenerSectorLocal(local.getId());
				local.setSectores(sectores);
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de locales exitosa");
			respuestaDto.setObjetoRespuesta(locales);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener los locales" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener los locales " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

}