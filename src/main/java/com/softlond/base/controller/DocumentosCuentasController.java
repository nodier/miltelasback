package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.service.DocumentosCuentasService;

@RestController
@RequestMapping(ApiConstant.DOCUMENTO_CUENTA_API)
public class DocumentosCuentasController {
	
	@Autowired
	private DocumentosCuentasService documentosCuentasService;
	
	@GetMapping(value = ApiConstant.DOCUMENTO_CUENTA_API_LISTAR_SEDE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerPrefijoSede() {
		return documentosCuentasService.obtenerDocumentosCuentasSede();
	}
}
