package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.api.client.http.HttpHeaders;
import com.google.gson.Gson;
import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.FacturaM;
import com.softlond.base.entity.NotaDebito;
import com.softlond.base.entity.Prefijo;
import com.softlond.base.service.NotaDebitoService;
import java.util.Date;

// import org.apache.http.HttpEntity;
import org.springframework.http.HttpEntity;
import org.apache.log4j.Logger;

@RestController
@RequestMapping(ApiConstant.NOTA_DEBITO_API)
public class NotaDebitoCrontroller {

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	public NotaDebitoService notaDebitoService;

	// Crear
	@PostMapping(value = ApiConstant.NOTA_DEBITO_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crearNotaDebito(@RequestBody NotaDebito nuevoNotaDebito) {
		return notaDebitoService.crearNotaDebito(nuevoNotaDebito);
	}

	// Actualizar
	@PostMapping(value = ApiConstant.NOTA_DEBITO_API_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> actualizarNotaDebito(@RequestBody NotaDebito actualizarNotaDebito) {
		return notaDebitoService.actualizarNotaDebito(actualizarNotaDebito);
	}

	// Obtener numero
	@GetMapping(value = ApiConstant.NOTA_DEBITO_API_OBTENER_NUMERO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerNumero(Integer idSede) {
		return notaDebitoService.obtenerNumero(idSede);
	}

	// busqueda por filtro
	@GetMapping(value = ApiConstant.NOTA_DEBITO_API_BUSCAR_FILTRO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerNDFiltros(String numeroDocumento, String fechaInicial,
			String fechaFinal, String estadoDocumento, Integer page) throws Exception {
		return notaDebitoService.obtenerNDFiltros(numeroDocumento, fechaInicial, fechaFinal, estadoDocumento, page);
	}

	@GetMapping(value = ApiConstant.NOTA_DEBITO_API_BUSCAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerND(String numeroDocumento) {
		return notaDebitoService.obtenerND(numeroDocumento);
	}

	@DeleteMapping(value = ApiConstant.NOTA_DEBITO_API_ELIMINAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> borrar(@RequestParam Integer idNotaDebito) {
		return notaDebitoService.borrar(idNotaDebito);
	}

	// Listar notas debito del mes
	@GetMapping(value = ApiConstant.NOTA_DEBITO_API_LISTAR_MES, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarNotasDebitoMes(Integer page) {
		return notaDebitoService.obtenerNotasDebitoMes(page);
	}

	@GetMapping(value = ApiConstant.NOTA_DEBITO_API_LISTAR_ANULACION, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarNotasDebitoAnular(String numero) {
		return notaDebitoService.obtenerNotasDebitoAnular(numero);
	}

	@PostMapping(value = ApiConstant.NOTA_DEBITO_API_ANULAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> anularNotaDebito(@RequestBody NotaDebito notaDebito) {
		return notaDebitoService.anularNotaDebito(notaDebito);
	}
}
