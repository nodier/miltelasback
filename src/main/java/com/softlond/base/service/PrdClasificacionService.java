package com.softlond.base.service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.PrdClasificaciones;
import com.softlond.base.entity.PrdColores;
import com.softlond.base.entity.Producto;
import com.softlond.base.repository.PrdClasificacionDao;
import com.softlond.base.response.ClasificacionCantidadResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrdClasificacionService {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private PrdClasificacionDao clasificacionDao;

    public List<ClasificacionCantidadResponse> listarTodos() throws Exception {
    	List<ClasificacionCantidadResponse> clasificacionesNuevos = new ArrayList<ClasificacionCantidadResponse>();
        List<PrdClasificaciones> clasificaciones = (List<PrdClasificaciones>) clasificacionDao.findAll();
        for (PrdClasificaciones clasificacion : clasificaciones) {
        	ClasificacionCantidadResponse clasificacionNuevo = new ClasificacionCantidadResponse();
        	clasificacionNuevo.setClasificacion(clasificacion);
        	clasificacionNuevo.setCantidad(clasificacionDao.cantidadProductos(clasificacion.getId()));
        	clasificacionesNuevos.add(clasificacionNuevo);
		}
        return clasificacionesNuevos;
    }

    public PrdClasificaciones crearClasificacion(PrdClasificaciones body) throws Exception {
        // TODO: Validar body
        PrdClasificaciones nuevaClasificacion = clasificacionDao.save(body);
        return nuevaClasificacion;
    }

    public void editarClasificacion(PrdClasificaciones body) throws Exception {
        // TODO: Validar body
        clasificacionDao.save(body);
    }

    public void eliminarClasificacion(Integer idClasificacion) throws Exception {
        PrdClasificaciones clasificacion = clasificacionDao.findById(idClasificacion).get();
        if (clasificacion == null) {
            throw new Exception("No existe clasificación con este ID");
        }
        clasificacionDao.delete(clasificacion);
    }   
    
    
    
  //listar
	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarClasificacion(){
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		try {
			
			ArrayList<PrdClasificaciones> clasificacion = this.clasificacionDao.obtenerClasificaciones();	
			
			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de clasificación exitosa");
			respuestaDto.setObjetoRespuesta(clasificacion);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo clasificaciones " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo clasificaciones");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
}
