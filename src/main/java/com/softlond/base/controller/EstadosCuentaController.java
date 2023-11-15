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
import com.softlond.base.service.EstadosCuentasService;

@Controller
@RequestMapping(ApiConstant.PROVEEDORES_API_ESTADOS_CUENTA)
public class EstadosCuentaController {
	
	private final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private EstadosCuentasService estadosCuentaService;
	
	@GetMapping(value = ApiConstant.PROVEEDORES_API_ESTADOS_CUENTA_BUSCAR, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity buscarMovimientosPaginacion(int idProveedor,@PathVariable int page) {
        RespuestaDto respuestaDto;
        try {
            Paginacion pag = estadosCuentaService.obtenerEstadosCuentaPaginado(idProveedor, page);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(pag);
            respuestaDto.setVariable(String.valueOf(estadosCuentaService.sumar(idProveedor)));
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            String msj = "Error buscando estados de cuentas por proveedor... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }

}
