package com.softlond.base.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.DevolucionArticulosCompra;
import com.softlond.base.entity.DevolucionArticulosVenta;
import com.softlond.base.repository.DevolucionComprasArticulosDao;
import com.softlond.base.repository.DevolucionVentasArticulosDao;

@Service
public class DevolucionVentasArticulosService {

	private static final Logger logger = Logger.getLogger(DevolucionVentasArticulosService.class);

	@Autowired
	public DevolucionVentasArticulosDao devolucionVentasArticulosDao;

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> findBynDevolucion(Integer idDev) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			List<DevolucionArticulosVenta> articulosdev = this.devolucionVentasArticulosDao.findBynDevolucion(idDev);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de artiiculos devolución exitosa");
			respuestaDto.setObjetoRespuesta(articulosdev);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo articulos devolución " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo articulos devolución");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;

	}
}
