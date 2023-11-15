package com.softlond.base.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import com.softlond.base.entity.PrdClasificaciones;
import com.softlond.base.entity.Precio;
import com.softlond.base.entity.Producto;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.PrdDescuentosDao;
import com.softlond.base.repository.PrecioDao;
import com.softlond.base.repository.UsuarioInformacionDao;

@Service
public class PrecioProductoService {
	
	private static final Logger logger = Logger.getLogger(PrdDescuentosService.class);

	@Autowired
	private PrecioDao precioDao;
	
	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@PersistenceContext
	private EntityManager entityManager;
	
	
	// Listar todos los proecios por producto por id y sede
			@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
			public ResponseEntity<Object> obtenerPrecioProducto( Integer idProducto) {
				ResponseEntity<Object> respuesta;
				try {
					Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
					Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
					InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
							.buscarPorIdUsuario(usuarioAutenticado.getId());

					int idSede = usuarioInformacion.getIdOrganizacion().getId();
					// int idSede = 11;
					
					Precio precios = this.precioDao.obtenerPrecioProducto(idSede, idProducto);	
					
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion del precio del producto por id y sede exitoso");
					respuestaDto.setObjetoRespuesta(precios);
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
				} catch (Exception e) {
					respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
					logger.error("Error al listar el precio del producto por id y sede" + e.getMessage());
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
							"No se pudo obtener el precio del producto por id y sede");
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
				}
				return respuesta;
			}
	
	
	
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> ActualizarPrecios(@RequestBody Precio precio) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());

		try {
			precio.setSede(usuarioInformacion.getIdOrganizacion());
			//int idSede = usuarioInformacion.getIdOrganizacion().getId();

			Precio guardado = this.precioDao.save(precio);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito actualizando el precio del producto");
			respuestaDto.setObjetoRespuesta(guardado);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error en la actualizacion del precio del producto" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en la actualizacion del precio del producto "+e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}
	
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> CrearPrecios(@RequestBody Precio precio) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());

		try {
			precio.setSede(usuarioInformacion.getIdOrganizacion());
			//int idSede = usuarioInformacion.getIdOrganizacion().getId();

			Precio guardado = this.precioDao.save(precio);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito creando el precio del producto");
			respuestaDto.setObjetoRespuesta(guardado);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error en la creación del precio del producto" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en la creación del precio del producto "+e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

}
