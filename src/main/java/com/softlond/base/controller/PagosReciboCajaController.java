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

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.PagosReciboCaja;
import com.softlond.base.service.PagosReciboCajaService;

@RestController
@RequestMapping(ApiConstant.PAGOS_RECIBO_CAJA)
class PagosReciboCajaController {
	
	@Autowired
	public PagosReciboCajaService  pagoReciboCajaService;
	
	// Listar pagos
			@GetMapping(value = ApiConstant.PAGOS_RECIBO_CAJA_LISTAR_PAGO_CAJA, produces = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody ResponseEntity<Object> obtenerTodos(@RequestParam Integer idReciboCaja) {
				return pagoReciboCajaService.obtenerTodos(idReciboCaja);
			}
			
			// Crear pagos
			@PostMapping(value = ApiConstant.PAGOS_RECIBO_CAJA_ADICIONAR, produces = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody ResponseEntity<Object> crerPagoReciboCaja(@RequestBody PagosReciboCaja pagoNuevo) {
				return pagoReciboCajaService.crearPagoReciboCaja(pagoNuevo);
			}
			
			// Deshacer ultimo pago registrado
			@DeleteMapping(value = ApiConstant.PAGOS_RECIBO_CAJA_DESHACER_PAGO, produces = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody ResponseEntity<Object> borrarPago(@RequestParam Integer idPago) {
				return pagoReciboCajaService.borrarPago(idPago);
			}
			
			//Obtener pagos por cliente
			@GetMapping(value = ApiConstant. PAGOS_RECIBO_CAJA_PAGOS_FACTURA, produces = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody ResponseEntity<Object> obtenerPagosCliente(@RequestParam Integer idFactura) {
				return pagoReciboCajaService.obtenerPagosCliente(idFactura);
			}
			
			@GetMapping(value = ApiConstant.PAGOS_RECIBO_CAJA_INFORME_CAJA, produces = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody ResponseEntity<Object> obtenerFormaPago(Integer idCaja, String fecha) {
				return this.pagoReciboCajaService.obtenerPagosCaja(idCaja, fecha);
			}
			
			@GetMapping(value = ApiConstant.PAGOS_RECIBO_CAJA_INFORME_SEDE, produces = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody ResponseEntity<Object> obtenerFormaPagoSede(Integer idSede, String fecha) {
				return this.pagoReciboCajaService.obtenerPagosSede(idSede, fecha);
			}

}
