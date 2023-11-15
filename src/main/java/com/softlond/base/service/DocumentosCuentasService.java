package com.softlond.base.service;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.DocumentosCuentas;
import com.softlond.base.entity.Organizacion;
import com.softlond.base.entity.Prefijo;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.DocumentosCuentasDao;
import com.softlond.base.repository.OrganizacionDao;

@Service 
public class DocumentosCuentasService {

	private static final Logger logger = Logger.getLogger(DocumentosCuentas.class);
	
	@Autowired
	private DocumentosCuentasDao documentosCuentasDao;
	
	@Autowired
	private OrganizacionDao organizacionDao;
	
	@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerDocumentosCuentasSede(){
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		
		try {
			Organizacion sede = organizacionDao.obtenerSede(usuarioAutenticado.getId()); 
			ArrayList<DocumentosCuentas> documentos = this.documentosCuentasDao.obtenerDocumentosCuentas(sede.getId());	
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de documentos exitosa");
			respuestaDto.setObjetoRespuesta(documentos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo documentos " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo documentos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
}
