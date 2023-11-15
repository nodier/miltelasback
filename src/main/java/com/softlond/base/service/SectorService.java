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
import org.springframework.web.bind.annotation.RequestBody;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.InvSector;
import com.softlond.base.repository.SectorDao;
import com.softlond.base.repository.UsuarioInformacionDao;

@Service
public class SectorService {

	private static final Logger logger = Logger.getLogger(SectorService.class);

	final int ITEMS_POR_PAGINA = 10;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private SectorDao sectorDao;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	
	 @PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	    public  ResponseEntity<Object> obtenerSectorPorLocal(Integer idLocal, Integer page) {
	    	ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
	    	Pageable pageConfig = PageRequest.of(page, 10);
	        try {
	        	Page<InvSector> sectores = sectorDao.buscarSectorPorLocal(idLocal, pageConfig);
	            RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito obteniendo sectores");
				respuestaDto.setObjetoRespuesta(sectores);
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);			
	        } catch (Exception ex) {
	        	RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo sectores");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    	return respuesta;
	    }
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> crearSector(@RequestBody InvSector sector) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {

			InvSector guardado = this.sectorDao.save(sector);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito creando el sector");
			respuestaDto.setObjetoRespuesta(guardado);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error en la creación del sector" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en la creación del sector "+e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> actualizarSector(@RequestBody InvSector sector) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			this.sectorDao.save(sector);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito creando el sector");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error en la creación del sector" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en la creación del sector "+e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}
	
	// Eliminar Sector
		@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
		public ResponseEntity<Object> eliminarSector(Integer idSector) {
			ResponseEntity<Object> respuesta;
			try {
				sectorDao.deleteById(idSector);
				respuesta = ResponseEntity.ok(HttpStatus.OK);
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "eliminación del sector exitoso");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

			} catch (Exception e) {
				respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
				logger.error("Error al eliminar el sector" + e.getMessage());
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
						"No se pudo eliminar un sector");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return respuesta;
		}
					
	// Consulta avanzada de sector por local
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerSectorConsulta(Integer page, Integer local) {
		ResponseEntity<Object> respuesta;
		Pageable paging = PageRequest.of(page, 10);
		try {
			List<InvSector> sector;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQueryFiltros(query, local);
	
			TypedQuery<InvSector> descuentosInfoQuery = (TypedQuery<InvSector>) entityManager.createNativeQuery(query.toString(), InvSector.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			descuentosInfoQuery.setFirstResult(pageNumber * pageSize);
			descuentosInfoQuery.setMaxResults(pageSize);
			sector = descuentosInfoQuery.getResultList();
			generarQueryFiltrosCantidad(queryCantidad, local);		
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<InvSector> result = new PageImpl<InvSector>(sector, paging, cantidadTotal);
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
}