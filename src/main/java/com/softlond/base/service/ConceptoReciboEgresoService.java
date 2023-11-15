package com.softlond.base.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Organizacion;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.AsignacionComprobanteDao;
import com.softlond.base.repository.ComprobanteEgresoDao;
import com.softlond.base.repository.ConceptoReciboEgresoDao;
import com.softlond.base.repository.UsuarioInformacionDao;

@Service
public class ConceptoReciboEgresoService {
	
	private static final Logger logger = Logger.getLogger(ConceptoReciboEgresoService.class);
	
	@Autowired
    private ConceptoReciboEgresoDao conceptoReciboEgresoDao;
    
    @Autowired
	private AsignacionComprobanteDao asignacionComprobanteDao;
    
    @Autowired
    private ComprobanteEgresoDao comprobanteEgresoDao;
    
    @Autowired
	UsuarioInformacionDao usuarioInformacionDao;
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerTotalAbonosConcepto(String numero){
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Organizacion sede = usuarioInformacion.getIdOrganizacion();
		try {
			Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(numero, sede.getId());
        	Integer asignacion = asignacionComprobanteDao.obtenerTotal(numero, sede.getId());
        	Integer descuentos = conceptoReciboEgresoDao.obtenerTotalDescuentos(numero, sede.getId());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de saldo para el concepto exitosa");
			respuestaDto.setObjetoRespuesta(abonos + asignacion + descuentos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		}catch (Exception e) {
			logger.error("Error obteniendo saldo de concepto " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo saldo del concepto");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

}
