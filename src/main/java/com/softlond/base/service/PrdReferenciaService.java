package com.softlond.base.service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.PrdColores;
import com.softlond.base.entity.PrdReferencia;
import com.softlond.base.entity.PrdTipos;
import com.softlond.base.entity.Producto;
import com.softlond.base.repository.PrdReferenciaDao;
import com.softlond.base.repository.ProductoDao;
import com.softlond.base.response.ReferenciaConCantidad;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PrdReferenciaService {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private PrdReferenciaDao refDao;
    
    @Autowired
	private ProductoDao productoDao;

    public List<PrdReferencia> listarTodos() throws Exception {
        List<PrdReferencia> referencias = (List<PrdReferencia>) refDao.findAll();
        return referencias;
    }

    public PrdReferencia crearReferencia(PrdReferencia body) throws Exception {
        // TODO: Validar body  
    	
     	String NombreReferencia = body.getTreferencia();
     	List<PrdReferencia> referencias = (List<PrdReferencia>) refDao.findAll();
     	for (PrdReferencia referencia : referencias) {
   		   
   		  String NombreListaReferencia = referencia.getTreferencia();
   		   
   		 if (Objects.equals(NombreReferencia, NombreListaReferencia))  {
   			throw new Exception("El nombre de la referencia ya existe");
   		 	}
     	}
        PrdReferencia nuevaRef = refDao.save(body);
        Integer numeroId  = nuevaRef.getId();
        if (numeroId <= 9999) {
        	String Cadena = String.valueOf(numeroId);
        	String referenciaCadena = "0";
            String d = referenciaCadena.concat(Cadena);          
        	body.setTIdReferencia(d);
        	}else {
        		String Cadena2 = String.valueOf(numeroId);
        		body.setTIdReferencia(Cadena2);
        }
          refDao.save(body);
        
        return nuevaRef;
    }

    
    
    public void editarReferencia(PrdReferencia body) throws Exception {
        // TODO: Validar body
   
	    refDao.save(body);
    	
    	
    }

    public void eliminarReferencia(Integer idRef) throws Exception {
        PrdReferencia referencia = refDao.findById(idRef).get();
        if (referencia == null) {
            throw new Exception("No existe una referencia con este ID");
        }
        refDao.delete(referencia);
    }
    
    public Page<ReferenciaConCantidad> listarTodosPorTipo(Integer idTipo, Integer page) throws Exception {
    	Pageable paging = PageRequest.of(page, 10);
    	List<ReferenciaConCantidad> referenciasCantidad = new ArrayList<ReferenciaConCantidad>();
    	Page<PrdReferencia> referencias = (Page<PrdReferencia>) refDao.referenciasPorTipo(idTipo, paging);
    	for (PrdReferencia referencia : referencias) {
    		ReferenciaConCantidad nuevaReferencia = new ReferenciaConCantidad();
    		nuevaReferencia.setReferencia(referencia);
    		Integer cantidad = refDao.cantidadProductos(referencia.getId(), referencia.getTipo().getId());
    		nuevaReferencia.setCantidad(cantidad);
    		referenciasCantidad.add(nuevaReferencia);
		}
    	Page<ReferenciaConCantidad> result = new PageImpl<ReferenciaConCantidad>(referenciasCantidad, paging, referencias.getTotalElements());
        return result;
    }
    
    
    @PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerPorTipo(Integer idTipo) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		try {		
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito listando todas las referencias");
			respuestaDto.setObjetoRespuesta(this.refDao.buscarPorTipo(idTipo));
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			logger.error("Error se presentan problemas al cargar las ciudades por departamento");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error se presentan problemas al cargar las ciudades por departamento");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
    public List<PrdReferencia> listarTodosTexto(String texto) throws Exception {
        List<PrdReferencia> referencias = (List<PrdReferencia>) refDao.referenciasPorTipo(texto);
        return referencias;
    }
}
