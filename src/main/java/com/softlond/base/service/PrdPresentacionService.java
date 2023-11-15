package com.softlond.base.service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.PrdClasificaciones;
import com.softlond.base.entity.PrdColores;
import com.softlond.base.entity.PrdPresentacion;
import com.softlond.base.entity.Producto;
import com.softlond.base.repository.PrdPresentacionDao;
import com.softlond.base.repository.ProductoDao;
import com.softlond.base.response.PresentacionCantidadResponse;

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
public class PrdPresentacionService {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private PrdPresentacionDao presentacionDao;

    @Autowired
   	private ProductoDao productoDao;

    public List<PresentacionCantidadResponse> listarTodos() throws Exception {
        List<PrdPresentacion> presentaciones = (List<PrdPresentacion>) presentacionDao.findAll();
        List<PresentacionCantidadResponse> presentacionesNuevo = new ArrayList<PresentacionCantidadResponse>();
        for (PrdPresentacion presentacion : presentaciones) {
        	PresentacionCantidadResponse presentacionNuevo = new PresentacionCantidadResponse();
        	presentacionNuevo.setPresentacion(presentacion);
        	presentacionNuevo.setCantidad(presentacionDao.cantidadProductos(presentacion.getId()));
        	presentacionesNuevo.add(presentacionNuevo);
		}
        return presentacionesNuevo;
    }

    public PrdPresentacion crearPresentacion(PrdPresentacion body) throws Exception {
        // TODO: Validar body    	
    	List<String> ListadoPresentaciones = new ArrayList<>();
    	List<PrdPresentacion> lista = presentacionDao.obtenerPresentaciones();
    	    
    	String NombrePresentacion = body.getTPresentacion();
    	for (PrdPresentacion presentaciones : lista) {
    		   
    		  String NombreListaPresentaciones = presentaciones.getTPresentacion();
    		   
    		   
    		 if (Objects.equals(NombrePresentacion, NombreListaPresentaciones))  {
    			throw new Exception("El nombre de la presentacion ya existe");
    		}
    	}
    	
    	Integer numeroId = presentacionDao.IdPresentacion();
    	body.setId(numeroId + 1);
    	
    	int tamano = lista.size();
    	   	
    	ListadoPresentaciones.add("1");
    	ListadoPresentaciones.add("2");
    	ListadoPresentaciones.add("3");
    	ListadoPresentaciones.add("4");
    	ListadoPresentaciones.add("5");
    	ListadoPresentaciones.add("6");
    	ListadoPresentaciones.add("7");
    	ListadoPresentaciones.add("8");
    	ListadoPresentaciones.add("9");
    	ListadoPresentaciones.add("A");
    	ListadoPresentaciones.add("B");
    	ListadoPresentaciones.add("C");
    	ListadoPresentaciones.add("D");
    	ListadoPresentaciones.add("E");
    	ListadoPresentaciones.add("F");
    	ListadoPresentaciones.add("G");
    	ListadoPresentaciones.add("H");
    	ListadoPresentaciones.add("I");
    	ListadoPresentaciones.add("J");
    	ListadoPresentaciones.add("K");
    	ListadoPresentaciones.add("L");
    	ListadoPresentaciones.add("M");
    	ListadoPresentaciones.add("N");
    	ListadoPresentaciones.add("Ñ");
    	ListadoPresentaciones.add("O");
    	ListadoPresentaciones.add("P");
    	ListadoPresentaciones.add("Q");
    	ListadoPresentaciones.add("R");
    	ListadoPresentaciones.add("S");
    	ListadoPresentaciones.add("T");
    	ListadoPresentaciones.add("U");
    	ListadoPresentaciones.add("V");
    	ListadoPresentaciones.add("W");
    	ListadoPresentaciones.add("Y");
    	ListadoPresentaciones.add("Z");
    	
    	 boolean bandera;
         int ContadorNo;
         String nuevaPresentacion = "";
    		
    			for (int i=0 ; i<ListadoPresentaciones.size() ; i++) {       
    				    String presentacionBase = ListadoPresentaciones.get(i);
    				     bandera = false;
    				     ContadorNo =0;
    				for (PrdPresentacion presentacion : lista) {
    					    
    					    String presentacionLista = presentacion.getTidPresentacion();
    				if(presentacionLista != null) {
    				   if (Objects.equals(presentacionBase, presentacionLista)) {
    					        
    					        bandera = true;
    					        
    				   	}else {
    				   		ContadorNo = ContadorNo + 1;
    				   	}
    				}
    			}
    				if (bandera == false && ContadorNo == tamano) {
    					nuevaPresentacion = presentacionBase;    				 
    				    i = ListadoPresentaciones.size();   
    				}			
    	}		
    			body.setTidPresentacion(nuevaPresentacion);
    			
        PrdPresentacion PresentacionNew = presentacionDao.save(body);
        return PresentacionNew;			
    }

    public void editarPresentacion(PrdPresentacion body) throws Exception {
        // TODO: Validar body

	    presentacionDao.save(body);
    	
    }

    public void eliminarPresentacion(Integer idPresentacion) throws Exception {
        PrdPresentacion presentacion = presentacionDao.findById(idPresentacion).get();
        if (presentacion == null) {
            throw new Exception("No existe una presentacion con ese ID");
        }
        presentacionDao.delete(presentacion);
    }
    
    
 // listar todas las presentaciones
    @PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
 	public ResponseEntity<Object> listarPresentaciones() {

 		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

 		try {

 			ArrayList<PrdPresentacion> presentacion = this.presentacionDao.obtenerPresentaciones();

 			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de productos exitosa");
 			respuestaDto.setObjetoRespuesta(presentacion);
 			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
 		} catch (Exception e) {
 			logger.error("Error obteniendo presentaciones " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
 			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo presentaciones");
 			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
 		}
 		return respuesta;
 	}
 	
 	
 // Listar todos las presentaciones por id
 	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
 	public ResponseEntity<Object> obtenerPresentacion( Integer idPresentacion) {
 		ResponseEntity<Object> respuesta;
 		try {
 			
 			
 			List<PrdPresentacion> clasificaciones = this.presentacionDao.obtenerPresentacion(idPresentacion);	
 			
 			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de la presentacion por id exitoso");
 			respuestaDto.setObjetoRespuesta(clasificaciones);
 			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
 		} catch (Exception e) {
 			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
 			logger.error("Error al listar la presentacion por id" + e.getMessage());
 			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
 					"No se pudo obtener la presentacion por id");
 			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
 		}
 		return respuesta;
 	}
    
    
    public List<PrdPresentacion> obtenerPresentacionesSelector(){
    	List<PrdPresentacion> presentaciones = (List<PrdPresentacion>) presentacionDao.findAll();
    	return presentaciones;
    }
    
    public List<PrdPresentacion> obtenerPresentacionesTexto(String texto){
    	List<PrdPresentacion> presentaciones = presentacionDao.listarPresentacionTexto(texto);
    	return presentaciones;
    }
}



