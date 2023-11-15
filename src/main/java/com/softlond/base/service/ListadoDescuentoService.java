package com.softlond.base.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.Clientes;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.NotaCredito;
import com.softlond.base.entity.NotaDebito;
import com.softlond.base.entity.Producto;
import com.softlond.base.entity.ProveedorCondicionesComerciales;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ClientesDao;
import com.softlond.base.repository.FacturaCompraDao;
import com.softlond.base.repository.NotaCreditoDao;
import com.softlond.base.repository.NotaDebitoDao;
import com.softlond.base.repository.ProductoDao;
import com.softlond.base.repository.ProveedorCondicionesComercialesDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.response.EstadosCuenta;
import com.softlond.base.response.InfromeFacturasVencidas;
import com.softlond.base.response.ListadoDescuentos;
import com.softlond.base.response.Paginacion;

@Service
public class ListadoDescuentoService {
	
	private static final Logger logger = Logger.getLogger(ListadoDescuentoService.class);

final int ITEMS_POR_PAGINA = 10;
	
	@Autowired
    private ClientesDao clientesDao;
    @Autowired
    private ProductoDao prDao;
    
	@PersistenceContext
    private EntityManager entityManager;
	
	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;
	
		
	
}

