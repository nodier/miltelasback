package com.softlond.base.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.response.MovimientoProveedor;
import com.softlond.base.response.Paginacion;
import com.softlond.base.service.ConceptosClienteVisualService;

@RestController
@RequestMapping(ApiConstant.CONCEPTOS_CLIENTE_API)
public class ConceptosVisualReciboCajaController {
	
	@Autowired
	private ConceptosClienteVisualService conceptosService;
	
	private final Logger logger = Logger.getLogger(getClass());

	@GetMapping(value = ApiConstant.CONCEPTOS_CLIENTE_API_LISTA, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity buscarConceptosPendientes(@RequestParam Integer cliente, @RequestParam Integer page) {
        RespuestaDto respuestaDto;
        try {
        	Paginacion pag = conceptosService.obtenerConceptosPaginado(cliente, page);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(pag);
            respuestaDto.setVariable(String.valueOf(conceptosService.getTotal()));
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error buscando conceptos pendientes por cliente... " + ex.getMessage();
            logger.error(ex.getMessage() + ex.getStackTrace()[0].getLineNumber());
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }
	
	@GetMapping(value = ApiConstant.CONCEPTOS_CLIENTE_API_LISTA_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity buscarConceptosPendientesActualizar(@RequestParam Integer cliente, @RequestParam Integer recibo, @RequestParam Integer page) {
        RespuestaDto respuestaDto;
        try {
        	Paginacion pag = conceptosService.obtenerConceptosPaginadoActualizar(cliente, recibo, page);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(pag);
            respuestaDto.setVariable(String.valueOf(conceptosService.getTotal()));
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error buscando conceptos pendientes por cliente... " + ex.getMessage();
            logger.error(ex.getMessage() + ex.getStackTrace()[0].getLineNumber());
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }
}
