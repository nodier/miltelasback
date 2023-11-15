package com.softlond.base.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.Articulo;
import com.softlond.base.entity.ConfigTerClasificaciones;
import com.softlond.base.entity.Cuenta;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Precio;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ClasificacionDao;

@Service
public class ClasificacionService {

	private static final Logger logger = Logger.getLogger(ClasificacionService.class);

	@Autowired
	ClasificacionDao clasificacionDao;

	 @PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
		public ResponseEntity<Object> crearClasificacion(ConfigTerClasificaciones clasificacion) {
			
			ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
			
			try {
				this.clasificacionDao.save(clasificacion);
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			}
			catch (Exception e) {
				logger.error("Error se presentan problemas al guardar clasificación " + e);
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error se presentan problemas al guardar la clasificación "+ e);
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			return respuesta;
		}
	 
	 @PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
		public ResponseEntity<Object> deleteClasificacion(Integer id){
			ResponseEntity<Object> respuesta;
			try {
				this.clasificacionDao.deleteById(id);
				respuesta = ResponseEntity.ok(HttpStatus.OK);
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Caja eliminada exitosamente");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
				return respuesta;
			} catch (Exception e) {
				respuesta = ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
				logger.error("Error eliminando caja " + e.getMessage());
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error eliminando la caja");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			}
				return respuesta;
			}
	 
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public List<ConfigTerClasificaciones> listarTodos() throws Exception {
		List<ConfigTerClasificaciones> clasificaciones = (List<ConfigTerClasificaciones>) clasificacionDao.findAll();
		return clasificaciones;
	}

}
