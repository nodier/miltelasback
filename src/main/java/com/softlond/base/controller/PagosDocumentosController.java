package com.softlond.base.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import com.softlond.base.entity.ComprobanteEgreso;
import com.softlond.base.response.MovimientoProveedor;
import com.softlond.base.response.Paginacion;
import com.softlond.base.response.PagoDocumento;
import com.softlond.base.service.ComprobanteEgresoService;
import com.softlond.base.service.PagoDocumentoService;
import com.softlond.base.service.ReciboCajaVentaService;

@RestController
@RequestMapping(ApiConstant.PAGOS_DOCUMENTOS_API)
public class PagosDocumentosController {

	@Autowired
	private PagoDocumentoService pagoDocumentoService;
	
	@Autowired	
	private ComprobanteEgresoService comprobanteService;
	
	@Autowired	
	private ReciboCajaVentaService reciboCajaDao;
	
	private final Logger logger = Logger.getLogger(getClass());
	
	@GetMapping(value = ApiConstant.PAGOS_DOCUMENTOS_API_OBTENER, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity PagosDocumentos(@RequestParam String numero, @RequestParam String tipoPago, @RequestParam String tipoDocumento) {
        RespuestaDto respuestaDto;
        try {
        	List<PagoDocumento> pagos = pagoDocumentoService.listarPagos(numero, tipoPago, tipoDocumento);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(pagos);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            String msj = "Error buscando movimientos pendientes por proveedor... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }
	
	@GetMapping(value = ApiConstant.PAGOS_DOCUMENTOS_API_OBTENER_COMPROBANTES, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity Comprobantes(@RequestParam String numero, @RequestParam String tipoPago, @RequestParam String tipoDocumento,
    		@RequestParam Integer page) {
		ResponseEntity respuesta = null;
		if(tipoPago.equals("Proveedores")) {
			switch (tipoDocumento) {
			case "Facturas de Compra":
				respuesta = comprobanteService.listarComprobantesPagosDocumentos(numero, page);
				break;
			case "Notas Débito":
				respuesta = comprobanteService.listarComprobantesPagosDocumentosNotasCredito(numero, page);
				break;
			default:
				break;
			}
		}
		else if(tipoPago.equals("Clientes")) {
			switch (tipoDocumento) {
			case "Facturas de Venta":
				respuesta = reciboCajaDao.obtenerReciboFacturas(numero, page);
				break;
			case "Notas Débito":
				respuesta = reciboCajaDao.obtenerReciboFacturas(numero, page);
				break;
			default:
				break;
			}
		}
		else {
			respuesta = comprobanteService.obtenerComprobantesGastosServicios(numero, page);
		}
		return respuesta;
    }
}
