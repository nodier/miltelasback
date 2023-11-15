package com.softlond.base.controller;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.PrdColores;
import com.softlond.base.response.ColorCantidadResponse;
import com.softlond.base.service.PrdColorService;
import com.softlond.base.service.ProductoService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(ApiConstant.COLOR_CONTROL_API)
public class PrdColorController {
	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private PrdColorService colorService;
	
	@Autowired
	private ProductoService productoService;

	@GetMapping(value = ApiConstant.COLOR_CONTROL_API_LISTAR_TODOS, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarColores() {
		RespuestaDto respuestaDto;
		try {
			List<ColorCantidadResponse> colores = colorService.listarTodos();
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			respuestaDto.setObjetoRespuesta(colores);
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			String msj = "Error listando colores... " + ex.getMessage();
			logger.error(ex);
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = ApiConstant.COLOR_CONTROL_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity crearColor(@RequestBody PrdColores body) {
		RespuestaDto respuestaDto;
		try {
			PrdColores color = colorService.crearColor(body);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			respuestaDto.setObjetoRespuesta(color);
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			String msj = "Error creando color... " + ex.getMessage();
			logger.error(ex);
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(value = ApiConstant.COLOR_CONTROL_API_EDITAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity editarColor(@RequestBody PrdColores body) {
		RespuestaDto respuestaDto;
		try {
			colorService.editarColor(body);
			//productoService.editarProductoColores(body);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			String msj = "Error editando color... " + ex.getMessage();
			logger.error(ex);
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping(value = ApiConstant.COLOR_CONTROL_API_ELIMINAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity eliminarColor(@RequestParam Integer color) {
		RespuestaDto respuestaDto;
		try {
			colorService.eliminarColor(color);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			String msj = "Error eliminando color... " + ex.getMessage();
			logger.error(ex);
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}

	/* Listar todos los colores */
	@GetMapping(value = ApiConstant.COLOR_CONTROL_API_LISTAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listarColor() {
		return colorService.listarColor();
	}

	@GetMapping(value = ApiConstant.COLOR_CONTROL_API_LISTAR_TODOS_TEXTO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarColoresTexto(String texto) {
		RespuestaDto respuestaDto;
		try {
			List<PrdColores> colores = colorService.listarTodosTexto(texto);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			respuestaDto.setObjetoRespuesta(colores);
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			String msj = "Error listando colores... " + ex.getMessage();
			logger.error(ex);
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}

}
