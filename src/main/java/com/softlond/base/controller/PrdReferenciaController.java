package com.softlond.base.controller;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.PrdReferencia;
import com.softlond.base.response.ReferenciaConCantidad;
import com.softlond.base.service.PrdReferenciaService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.websocket.server.PathParam;

@Controller
@RequestMapping(ApiConstant.REFERENCIA_CONTROL_API)
public class PrdReferenciaController {
    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private PrdReferenciaService referenciaService;

    @GetMapping(value = ApiConstant.REFERENCIA_CONTROL_API_LISTAR_TODOS, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity listarReferencias() {
        RespuestaDto respuestaDto;
        try {
            List<PrdReferencia> referencias = referenciaService.listarTodos();
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(referencias);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error listando referencias... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = ApiConstant.REFERENCIA_CONTROL_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity crearReferencia(@RequestBody PrdReferencia body) {
        RespuestaDto respuestaDto;
        try {
            PrdReferencia referencia = referenciaService.crearReferencia(body);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(referencia);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error creando referencias... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = ApiConstant.REFERENCIA_CONTROL_API_EDITAR, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity editarReferencia(@RequestBody PrdReferencia body) {
        RespuestaDto respuestaDto;
        try {
            referenciaService.editarReferencia(body);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error editando referencias... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = ApiConstant.REFERENCIA_CONTROL_API_ELIMINAR, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity eliminarReferencia(@RequestParam Integer referencia) {
        RespuestaDto respuestaDto;
        try {
            referenciaService.eliminarReferencia(referencia);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error eliminando referencias... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping(value = ApiConstant.REFERENCIA_CONTROL_API_LISTAR_TODOS_TIPO, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity listarReferenciasPorTipo(Integer tipo, @PathVariable Integer page) {
        RespuestaDto respuestaDto;
        try {
            Page<ReferenciaConCantidad> referencias = referenciaService.listarTodosPorTipo(tipo, page);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(referencias);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error listando referencias... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }
    
    
    @GetMapping(value = ApiConstant.REFERENCIA_CONTROL_API_OBTENER_POR_TIPO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerPorTipo(Integer idTipo) {
		return referenciaService.obtenerPorTipo(idTipo);
	}
    @GetMapping(value = ApiConstant.REFERENCIA_CONTROL_API_LISTAR_TODOS_TEXTO, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity listarReferenciasTexto(String texto) {
        RespuestaDto respuestaDto;
        try {
            List<PrdReferencia> referencias = referenciaService.listarTodosTexto(texto);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(referencias);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error listando referencias... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }
}
