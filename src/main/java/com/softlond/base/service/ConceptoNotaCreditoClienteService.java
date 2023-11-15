package com.softlond.base.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.Clientes;
import com.softlond.base.entity.ConceptoNotaCredito;
import com.softlond.base.entity.ConceptoNotaCreditoCliente;
import com.softlond.base.entity.DevolucionCompras;
import com.softlond.base.entity.DevolucionVentasCliente;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.NotaCredito;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ConceptoNotaCreditoClienteDao;
import com.softlond.base.repository.ConceptoNotaCreditoDao;
import com.softlond.base.repository.DevolucionComprasDao;
import com.softlond.base.repository.DevolucionVentasClienteDao;
import com.softlond.base.repository.EstadoDocumentoDao;
import com.softlond.base.repository.FacturaCompraDao;
import com.softlond.base.repository.FacturaDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.repository.EstadoDocumentoDao;

@Service
public class ConceptoNotaCreditoClienteService {

	private static final Logger logger = Logger.getLogger(ConceptoNotaCreditoCliente.class);

	@Autowired
	public ConceptoNotaCreditoClienteDao conceptoNotaCreditoClienteDao;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private FacturaDao facturaDao;

	@Autowired
	private EstadoDocumentoDao estadoDocumentoDao;

	@Autowired
	private DevolucionVentasClienteDao devolucionDao;

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> crearConceptoNC(@RequestBody List<ConceptoNotaCreditoCliente> conceptosNC) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			conceptoNotaCreditoClienteDao.eliminarConceptosNotasCreditos(conceptosNC.get(0).getIdNotaCredito().getId());
			for (ConceptoNotaCreditoCliente conceptoNC : conceptosNC) {

				
				if (conceptoNC.getCodigoDocumento().equals("FAC")) {

					Factura factura = this.facturaDao.findByNumeroDocumento(conceptoNC.getNumeroDocumento());
					factura.setCodEstadoCon(this.estadoDocumentoDao.findById(1).orElse(null));
					this.facturaDao.save(factura);
				} else {

					DevolucionVentasCliente devolucion = this.devolucionDao
							.findByNroDevolucion(conceptoNC.getNumeroDocumento());
					devolucion.setCodEstadoCon(this.estadoDocumentoDao.findById(1).get());
					this.devolucionDao.save(devolucion);
				}

				ConceptoNotaCreditoCliente guardado = this.conceptoNotaCreditoClienteDao.save(conceptoNC);
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito creando conceptos NC");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error en la creación de conceptos NC" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en la creación de conceptos NC" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerNcConceptos(Integer idNotaCredito) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {

			ArrayList<ConceptoNotaCreditoCliente> concepto = this.conceptoNotaCreditoClienteDao
					.obtenerConceptoIdNotaCredito(idNotaCredito);

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de conceptos exitosa");
			respuestaDto.setObjetoRespuesta(concepto);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo conceptos " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo conceptos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarNC() {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());

			int idSede = usuarioInformacion.getIdOrganizacion().getId();
			ArrayList<ConceptoNotaCreditoCliente> concepto = this.conceptoNotaCreditoClienteDao
					.obtenerConceptosNotaCredito(idSede);

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de conceptos exitosa");
			respuestaDto.setObjetoRespuesta(concepto);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo conceptos " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo conceptos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}	
}
