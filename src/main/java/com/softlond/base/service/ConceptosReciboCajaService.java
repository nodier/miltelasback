package com.softlond.base.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.ConceptosReciboCaja;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.Organizacion;
import com.softlond.base.entity.PlazoCredito;
import com.softlond.base.entity.Prefijo;
import com.softlond.base.entity.ReciboCajaVenta;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ConceptosReciboCajaDao;
import com.softlond.base.repository.FacturaDao;

@Service
public class ConceptosReciboCajaService {

	private static final Logger logger = Logger.getLogger(PrefijoService.class);
	

	@Autowired
	private ConceptosReciboCajaDao conceptosDao;
	
	
	@PersistenceContext
	private EntityManager entityManager;


	
	
			
			// Actualizar abono credito
			@PreAuthorize("hasAuthority('USER') or hasAuthority('VENDEDOR')")
			public ResponseEntity<Object> actualizarAbono(@RequestBody ConceptosReciboCaja abonoActualizado) {
				
				ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
								
				ConceptosReciboCaja concepto = this.conceptosDao.findById(abonoActualizado.getId()).get();
				
				try {
					
					if (concepto.getId().equals(abonoActualizado.getId())) {
							concepto.setValorAbono(abonoActualizado.getValorAbono());
							concepto.setValorDescuento(abonoActualizado.getValorDescuento());
							concepto.setSaldoFinal(abonoActualizado.getSaldoFinal());
						
							ConceptosReciboCaja actualizada = this.conceptosDao.save(concepto);
							
							RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Abono registrado");
							respuestaDto.setObjetoRespuesta(actualizada);
							respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
									
					}
			
				} 
				catch (Exception e) {
					logger.error("Error en el registro del abono");
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el registro del abono");
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
				}
				
				return respuesta;
			}
			
			private boolean existeConcepto(ConceptosReciboCaja conceptoBuscar) {
				
				boolean flag = false;
				
				if (this.conceptosDao.findByConceptos(conceptoBuscar.getConceptos()) != null) {
					flag = true;
				} 
				
				return flag;
			}
			
			
}
