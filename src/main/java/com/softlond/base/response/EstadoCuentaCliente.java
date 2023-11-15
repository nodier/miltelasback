package com.softlond.base.response;
import java.util.Date;

import com.softlond.base.entity.Clientes;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.NotaCreditoCliente;
import com.softlond.base.entity.NotaDebitoCliente;

import lombok.Data; 
	 
	@Data 
	public class EstadoCuentaCliente { 

		String empresa;
	    String documento; 
	    String numero; 
	    Integer total; 
	    Integer saldo; 
	    Integer diasVencidos; 
	    Date fecha; 
	    Date fechaVencimiento; 
	    String concepto; 
	    Double descuento; 
	    String clientes;
	  
	    public static EstadoCuentaCliente convertirNotaCred(NotaCreditoCliente nc) { 
	    	EstadoCuentaCliente ec = new EstadoCuentaCliente(); 
	
	        ec.empresa = nc.getIdSede().getIdUnico();
	        ec.documento = nc.getIdCliente().getNitocc();
	        ec.numero = nc.getNumeroDocumento();
	        ec.total = nc.getTotal(); 
	        ec.saldo = nc.getTotal(); 
	        ec.diasVencidos = 0; 
	        ec.fecha = nc.getFechaDocumento(); 
	        ec.fechaVencimiento = null;
	        ec.concepto = "NC" + " " + nc.getObservaciones();
	        ec.clientes = nc.getIdCliente().getNombres() + " " + nc.getIdCliente().getApellidos();
	        return ec; 
	    } 
	    
		 
	    public static EstadoCuentaCliente convertirNotaDeb(NotaDebitoCliente nd) { 
	    	EstadoCuentaCliente ec = new EstadoCuentaCliente(); 
	        ec.empresa = nd.getIdSede().getIdUnico();
	        ec.documento = nd.getIdCliente().getNitocc();
	        ec.numero = nd.getNumeroDocumento();
	        ec.total = nd.getTotal(); 
	        ec.saldo = nd.getTotal(); 
	        ec.diasVencidos = 0; 
	        ec.fecha = nd.getFechaDocumento(); 
	        ec.fechaVencimiento = null;
	        ec.concepto = "ND" + " " + nd.getObservaciones();
	        ec.clientes = nd.getIdCliente().getNombres() + " " + nd.getIdCliente().getApellidos();
	        return ec; 
	    } 
	 
	    public static EstadoCuentaCliente convertirFactura(Factura fc) { 
	    	EstadoCuentaCliente ec = new EstadoCuentaCliente(); 
	     
	        ec.empresa = fc.getIdSede().getIdUnico(); 
	        ec.documento = fc.getIdCliente().getNitocc();
	        ec.numero = String.valueOf(fc.getNroFactura());
	        ec.total = fc.getTotal(); 
	        ec.saldo = fc.getTotal(); 
	        ec.diasVencidos = diasVencidos(fc.getFechaVencimientoCr()); 
	        ec.fecha = new Date(fc.getFechaVenta().getTime()); 
	        ec.fechaVencimiento = new Date(fc.getFechaVencimientoCr().getTime()); 
	        ec.concepto = fc.getIdConcepto()!=null ? fc.getIdConcepto().getCodDocumento():""; 
	        ec.clientes = fc.getIdCliente().getNombres() + " " + fc.getIdCliente().getApellidos();
	        		
	        return ec; 
	    } 
	 
	    private static int diasVencidos(Date fechaVencimiento) { 
	     long tiempoActual = new Date().getTime(); 
	        long tiempoFechaVencimiento = fechaVencimiento.getTime(); 
	        long diff = tiempoFechaVencimiento - tiempoActual; 
	        return (int) diff / (1000 * 60 * 60 * 24); 
	    } 
	}



