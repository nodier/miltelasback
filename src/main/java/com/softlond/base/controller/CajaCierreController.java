package com.softlond.base.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.Caja;
import com.softlond.base.entity.CajaCierre;
import com.softlond.base.service.CajaCierreService;

@RestController
@RequestMapping(ApiConstant.CIERRE_CAJAS_API)
public class CajaCierreController {
	
	@Autowired
	private CajaCierreService cajaCierreService;
	
	@GetMapping(value = ApiConstant.CIERRE_CAJAS_API_CIERRE_POR_CAJA, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> cierreCaja(@RequestParam Integer idCaja){
		return this.cajaCierreService.cierreCaja(idCaja);
	}
	
	@PostMapping(value = ApiConstant.CIERRE_CAJAS_API_REGISTRAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> registrarCierre(@RequestBody CajaCierre cierre){
		return this.cajaCierreService.registrarCierre(cierre);
	}

}
