package com.softlond.base.controller;


import com.softlond.base.constant.ApiConstant;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.response.Etiqueta;
import com.softlond.base.service.EtiquetaService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(ApiConstant.ETIQUETA_CONTROL_API)
public class EtiquetaController {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private EtiquetaService etiquetaService;

    @GetMapping(value = ApiConstant.ETIQUETA_CONTROL_API_PRODUCTO, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity etiquetasPorProducto(@RequestParam String codigo) {
    	
        RespuestaDto respuestaDto;
        try {
            List<Etiqueta> etiquetas = etiquetaService.articulosPorCodigoProducto(codigo);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(etiquetas);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error buscando información para etiquetas... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = ApiConstant.ETIQUETA_CONTROL_API_REMISION, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity etiquetasPorRemision(@RequestParam String remision) {
        RespuestaDto respuestaDto;
        try {
            List<Etiqueta> etiquetas = etiquetaService.articulosPorRemision(remision);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(etiquetas);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            String msj = "Error buscando información para etiquetas... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = ApiConstant.ETIQUETA_CONTROL_API_SECTOR, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity etiquetasPorSector(@RequestParam Integer sector) {
        RespuestaDto respuestaDto;
        try {
            List<Etiqueta> etiquetas = etiquetaService.articulosPorSector(sector);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(etiquetas);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error buscando información para etiquetas... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = ApiConstant.ETIQUETA_CONTROL_API_ARTICULO, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity etiquetasPorArticulo(@RequestParam String codigo) {
        RespuestaDto respuestaDto;
        try {
            List<Etiqueta> etiquetas = etiquetaService.articulosPorCodigoArticulo(codigo);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(etiquetas);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error buscando información para etiquetas... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }
}
