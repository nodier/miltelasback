package com.softlond.base.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "pagos_comprobante_egreso")
public class PagoComprobanteEgreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "valor")
    private Integer valor;

    @ManyToOne
    @JoinColumn(name = "id_forma_pago")
    private FormasPago formaPago;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "cuenta")
    private Cuenta cuenta;

    @Column(name = "numero_aprobacion",length = 20)
    private String numeroAprobacion;

    @Column(name = "banco_cheque",length = 25)
    private String bancoCheque;

    @CreationTimestamp
    @Column(name = "fecha")
    private Date fecha;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "id_creador")
    private Usuario creador;
}
