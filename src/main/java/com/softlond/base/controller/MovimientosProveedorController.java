package com.softlond.base.controller;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.request.BusquedaMovProveedor;
import com.softlond.base.response.MovimientoProveedor;
import com.softlond.base.response.Paginacion;
import com.softlond.base.service.MovimientosProveedorService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(ApiConstant.PROVEEDORES_API_MOVIMENTOS)
public class MovimientosProveedorController {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private MovimientosProveedorService servicioMovimientos;

    @PostMapping(value = ApiConstant.PROVEEDORES_API_MOVIMENTOS_BUSCAR, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity buscarMovimientosPaginacion(@RequestBody BusquedaMovProveedor body,
            @RequestParam int pagina) {
        RespuestaDto respuestaDto;
        try {
            Paginacion pag = servicioMovimientos.obtenerMovimientosPorProveedorPaginado(body.getIdProveedor(),
                    body.getFechaInicial(), body.getFechaFinal(), pagina);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(pag);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            String msj = "Error buscando movimientos por proveedor... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = ApiConstant.PROVEEDORES_API_MOVIMENTOS_BUSCAR_TODOS, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity buscarMovimientos(@RequestBody BusquedaMovProveedor body) {
        RespuestaDto respuestaDto;
        try {
            List<MovimientoProveedor> movs = servicioMovimientos.obtenerMovimientosPorProveedor(body.getIdProveedor(),
                    body.getFechaInicial(), body.getFechaFinal());
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(movs);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            String msj = "Error buscando movimientos por proveedor... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = ApiConstant.PROVEEDORES_API_MOVIMENTOS_BUSCAR_PENDIENTES, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity buscarMovimientosPendientes(@RequestParam Integer proveedor) {
        RespuestaDto respuestaDto;
        try {
            List<MovimientoProveedor> movs = servicioMovimientos.obtenerPendientes(proveedor);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(movs);
            respuestaDto.setVariable(String.valueOf(servicioMovimientos.obtenerSaldoTotal(movs)));
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            String msj = "Error buscando movimientos pendientes por proveedor... " + ex;
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = ApiConstant.PROVEEDORES_API_MOVIMENTOS_BUSCAR_PENDIENTES_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity buscarMovimientosPendientesActualizar(@RequestParam Integer proveedor,
            @RequestParam Integer comprobante) {
        RespuestaDto respuestaDto;
        try {
            List<MovimientoProveedor> movs = servicioMovimientos.obtenerPendientesActualizar(proveedor, comprobante);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(movs);
            respuestaDto.setVariable(String.valueOf(servicioMovimientos.obtenerSaldoTotal(movs)));
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            String msj = "Error buscando movimientos pendientes por proveedor... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }
}
