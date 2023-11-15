package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.Clientes;
import com.softlond.base.entity.NotaDebito;
import com.softlond.base.entity.NotaDebitoCliente;
import com.softlond.base.service.NotaDebitoClienteService;

@RestController
@RequestMapping(ApiConstant.NOTA_DEBITO_CLIENTE_API)
public class NotaDebitoClienteCrontroller {

	@Autowired
	public NotaDebitoClienteService notaDebitoClienteService;

	// Crear
	@PostMapping(value = ApiConstant.NOTA_DEBITO_CLIENTE_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crearNotaDebitoCliente(@RequestBody NotaDebitoCliente nuevoNotaDebito)
			throws Exception {
		return notaDebitoClienteService.crearNotaDebitoCliente(nuevoNotaDebito);
	}

	// Actualizar
	@PutMapping(value = ApiConstant.NOTA_DEBITO_CLIENTE_API_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> actualizarNotaDebito(
			@RequestBody NotaDebitoCliente actualizarNotaDebito) {
		return notaDebitoClienteService.actualizarNotaDebito(actualizarNotaDebito);
	}

	// Obtener numero
	@GetMapping(value = ApiConstant.NOTA_DEBITO_CLIENTE_API_OBTENER_NUMERO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerNumero(Integer idSede) {
		return notaDebitoClienteService.obtenerNumero(idSede);
	}

	// busqueda por filtro
	@GetMapping(value = ApiConstant.NOTA_DEBITO_CLIENTE_API_LISTAR_TODOS_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerNDFiltros(String fechaInicial,
			String fechaFinal, String numeroDocumento, String estadoDocumento, Integer page) {
		return notaDebitoClienteService.obtenerNotaDebitoClienteConsulta(fechaInicial, fechaFinal, numeroDocumento,
				estadoDocumento, page);
	}

	@GetMapping(value = ApiConstant.NOTA_DEBITO_CLIENTE_API_BUSCAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerND(String numeroDocumento) {
		return notaDebitoClienteService.obtenerND(numeroDocumento);
	}

	@DeleteMapping(value = ApiConstant.NOTA_DEBITO_CLIENTE_API_ELIMINAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> borrar(@RequestParam Integer idNotaDebito) {
		return notaDebitoClienteService.borrar(idNotaDebito);
	}

	// Listar notas debito del mes
	@GetMapping(value = ApiConstant.NOTA_DEBITO_CLIENTE_API_LISTAR_MES, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarNotasDebitoMes(Integer page) {
		return notaDebitoClienteService.obtenerNotasDebitoMes(page);
	}
}
