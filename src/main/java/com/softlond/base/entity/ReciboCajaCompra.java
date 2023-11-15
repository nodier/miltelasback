package com.softlond.base.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Data
@Entity
@Table(name = "rcb_rbocajacompra")
public class ReciboCajaCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_periodo_contable")
    private PeriodosContables periodoContable;

    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    @Column(name = "fecha_pago")
    private Date fechaPago;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "total_recibo")
    private Double totalRecibo;

    @Column(name = "saldo")
    private Double saldo;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_recibo_caja")
    private List<PagosReciboCaja> pagos;
}
