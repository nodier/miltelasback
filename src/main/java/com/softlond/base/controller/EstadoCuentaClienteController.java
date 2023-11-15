package com.softlond.base.controller;


import org.apache.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.http.HttpStatus; 
import org.springframework.http.MediaType; 
import org.springframework.http.ResponseEntity; 
import org.springframework.stereotype.Controller; 
import org.springframework.web.bind.annotation.GetMapping; 
import org.springframework.web.bind.annotation.PathVariable; 
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.ResponseBody; 
 
import com.softlond.base.constant.ApiConstant; 
import com.softlond.base.dto.RespuestaDto; 
import com.softlond.base.response.Paginacion;
import com.softlond.base.service.EstadoCuentaClienteService; 
 
	
@Controller 
@RequestMapping(ApiConstant.CLIENTES_API_ESTADOS_CUENTA) 
public class EstadoCuentaClienteController { 
  
 private final Logger logger = Logger.getLogger(getClass()); 
  
 @Autowired 
 private EstadoCuentaClienteService estadocuentaClienteService; 
  
 @GetMapping(value = ApiConstant.CLIENTES_API_ESTADOS_CUENTA_BUSCAR, produces = MediaType.APPLICATION_JSON_VALUE) 
    public @ResponseBody ResponseEntity buscarMovimientosPaginacion(int idCliente, @PathVariable int page) { 
        RespuestaDto respuestaDto; 
        try { 
            Paginacion pag = estadocuentaClienteService.obtenerEstadosCuentaPaginado(idCliente, page); 
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok"); 
            respuestaDto.setObjetoRespuesta(pag); 
            respuestaDto.setVariable(String.valueOf(estadocuentaClienteService.sumar(idCliente))); 
            return new ResponseEntity(respuestaDto, HttpStatus.OK); 
        } catch (Exception ex) { 
            ex.printStackTrace(); 
            String msj = "Error buscando estados de cuentas por cliente... " + ex.getMessage(); 
            logger.error(ex); 
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj); 
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST); 
        } 
    } 
 
}
