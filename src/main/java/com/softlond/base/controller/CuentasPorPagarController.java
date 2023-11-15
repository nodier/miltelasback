package com.softlond.base.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.request.BusquedaMovProveedor;
import com.softlond.base.response.Paginacion;
import com.softlond.base.service.CuentasPorPagarService;

@Controller
@RequestMapping(ApiConstant.PROVEEDORES_API_CUENTAS_PAGAR)
public class CuentasPorPagarController {
	
	private final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private CuentasPorPagarService cuentasPagarService;
	
	@GetMapping(value = ApiConstant.PROVEEDORES_API_CUENTAS_PAGAR_BUSCAR, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity buscarMovimientosPaginacion(int idProveedor,int pagina) {
        RespuestaDto respuestaDto;
        try {
            Paginacion pag = cuentasPagarService.obtenerCuentasPorPagarPorProveedorPaginado(idProveedor, pagina);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(pag);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            String msj = "Error buscando cuentas por pagar por proveedor... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }
	
	@GetMapping(value = ApiConstant.PROVEEDORES_API_CUENTAS_PAGAR_TOTAL, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity Total(int idProveedor) {
        RespuestaDto respuestaDto;
        try {
            Integer total = cuentasPagarService.sumar(idProveedor);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(total);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            String msj = "Error onteniento total... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }
	
	@GetMapping(value = ApiConstant.PROVEEDORES_API_CUENTAS_PAGAR_BUSCAR_ALMACEN, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity CuentasPorPagarAlmacen(int pagina) {
        RespuestaDto respuestaDto;
        try {
            Paginacion pag = cuentasPagarService.obtenerCuentasPorPagarPorProveedorPaginadoAlmacen(pagina);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(pag);
            respuestaDto.setVariable(String.valueOf(cuentasPagarService.sumarAlmacen()));
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            String msj = "Error buscando cuentas por pagar por proveedor... " + ex.getMessage();
            logger.error("Error: " + ex.getMessage() + " " + ex.getStackTrace()[0].getLineNumber() + ex.getStackTrace()[0].getClassName());
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }

}
