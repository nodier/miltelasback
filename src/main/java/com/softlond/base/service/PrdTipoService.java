package com.softlond.base.service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.PrdColores;
import com.softlond.base.entity.PrdPresentacion;
import com.softlond.base.entity.PrdTipos;
import com.softlond.base.entity.Producto;
import com.softlond.base.repository.PrdTipoDao;
import com.softlond.base.repository.ProductoDao;
import com.softlond.base.response.TiposCantidadResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PrdTipoService {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private PrdTipoDao tipoDao;
    
    @Autowired
   	private ProductoDao productoDao;


    public List<TiposCantidadResponse> listarTodos() throws Exception {
        List<PrdTipos> tipos = (List<PrdTipos>) tipoDao.findAll();
        List<TiposCantidadResponse> nuevosTipos = new ArrayList<TiposCantidadResponse>();
        for (PrdTipos tipo : tipos) {
        	TiposCantidadResponse nuevoTipo = new TiposCantidadResponse();
        	nuevoTipo.setTipo(tipo);
        	Integer cantidad = tipoDao.cantidadProductos(tipo.getId());
        	nuevoTipo.setCantidad(cantidad);
        	nuevosTipos.add(nuevoTipo);
		}
        return nuevosTipos;
    }

    public PrdTipos crearTipo(PrdTipos body) throws Exception {
        // TODO: validar body
    	    	
    	Integer numeroId = tipoDao.obtenerTidTipo();
    	body.setTidTipo(String.valueOf(numeroId + 1));
    	String NombreTipo = body.getTTipo();
    	 List<PrdTipos> tipos = (List<PrdTipos>) tipoDao.findAll();
    	for (PrdTipos tipo : tipos) {
    		   
    		  String NombreListaTipos = tipo.getTTipo();
    		   
    		 if (Objects.equals(NombreTipo, NombreListaTipos))  {
    			throw new Exception("El nombre del tipo ya existe");
    		}
    	}
    	
        PrdTipos nuevoTipo = tipoDao.save(body);
        return nuevoTipo;
    }

    public void editarTipo(PrdTipos body) throws Exception {
        // TODO: validar body
       
	    tipoDao.save(body);	
    	
    }

    public void eliminarTipo(Integer idTipo) throws Exception {
        PrdTipos tipo = tipoDao.findById(idTipo).get();
        if (tipo == null) {
            throw new Exception("No existe un tipo de producto con este ID");
        }
        tipoDao.delete(tipo);
    }
    
 // listar todos los colores
    @PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
   	public ResponseEntity<Object> listarTipo() {

   		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

   		try {

   			ArrayList<PrdTipos> tipo = this.tipoDao.obtenerTipo();

   			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de productos exitosa");
   			respuestaDto.setObjetoRespuesta(tipo);
   			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
   		} catch (Exception e) {
   			logger.error("Error obteniendo presentaciones " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
   			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo presentaciones");
   			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
   		}
   		return respuesta;
   	}
    public List<PrdTipos> listarTodosSelector() throws Exception {
        List<PrdTipos> tipos = (List<PrdTipos>) tipoDao.findAll();
        return tipos;
    }
    
    public List<PrdTipos> listarTodosFiltroTexto(String texto) throws Exception {
        List<PrdTipos> tipos = (List<PrdTipos>) tipoDao.obtenerTiposTexto(texto);
        return tipos;
    }
}
