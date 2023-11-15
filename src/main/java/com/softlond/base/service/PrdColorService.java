package com.softlond.base.service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.PrdColores;
import com.softlond.base.entity.Producto;
import com.softlond.base.entity.PrdPresentacion;
import com.softlond.base.entity.PrdReferencia;
import com.softlond.base.repository.PrdColorDao;
import com.softlond.base.repository.ProductoDao;
import com.softlond.base.response.ColorCantidadResponse;
import com.softlond.base.repository.ProductoDao;

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
public class PrdColorService {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private PrdColorDao colorDao;
    
    @Autowired
	private ProductoDao productoDao;
    

    public List<ColorCantidadResponse> listarTodos() throws Exception {
    	List<ColorCantidadResponse> coloresNuevos = new ArrayList<ColorCantidadResponse>();
    	
        List<PrdColores> colores = (List<PrdColores>) colorDao.findAll();
        for (PrdColores color : colores) {
        	ColorCantidadResponse colorNuevo = new ColorCantidadResponse();
        	colorNuevo.setColor(color);
        	colorNuevo.setCantidad(colorDao.cantidadProductos(color.getId()));
        	coloresNuevos.add(colorNuevo);
		}
        return coloresNuevos;
    }

    public PrdColores crearColor(PrdColores body) throws Exception {
        // TODO: Validar body
    	Integer numeroId = colorDao.IdColor();
    	body.setTidColor(String.valueOf(numeroId + 1));
    	String NombreColor = body.getTColor();
    	 List<PrdColores> colores = (List<PrdColores>) colorDao.findAll();
     	for (PrdColores color : colores) {
   		   
   		  String NombreListaColor = color.getTColor();
   		   
   		 if (Objects.equals(NombreColor, NombreListaColor))  {
   			throw new Exception("El nombre de la referencia ya existe");
   		 	}
     	}
     	
     	String rgb = body.getTRGB();
     	
     	for (PrdColores color : colores) {
    		   
     		  String NombreRGBListaColor = color.getTRGB();
     		   
     		 if (Objects.equals(rgb, NombreRGBListaColor))  {
     			throw new Exception("El RGBColor ya existe");
     		 	}
       	}
     	
        PrdColores nuevoColor = colorDao.save(body);
        return nuevoColor;
    }

    public void editarColor(PrdColores body) throws Exception {
        // TODO: Validar body
    	//int tamano = colorDao.cantidadProductos(body.getId());	
    	colorDao.save(body);
    }

    public void eliminarColor(Integer idColor) throws Exception {
        PrdColores color = colorDao.findById(idColor).get();
        if (color == null) {
            throw new Exception("No existe un color con este ID");
        }
        colorDao.delete(color);
    }
    
   

    
    
 // listar todos los colores
  	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
  	public ResponseEntity<Object> listarColor() {

  		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

  		try {

  			ArrayList<PrdColores> color = this.colorDao.obtenerColores();

  			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de productos exitosa");
  			respuestaDto.setObjetoRespuesta(color);
  			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
  		} catch (Exception e) {
  			logger.error("Error obteniendo presentaciones " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
  			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo presentaciones");
  			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
  		}
  		return respuesta;
  	}
    public List<PrdColores> listarTodosTexto(String texto) throws Exception {
        List<PrdColores> colores = colorDao.listaColoresTexto(texto);
        return colores;
    }
}
