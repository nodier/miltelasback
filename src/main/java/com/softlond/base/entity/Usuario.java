package com.softlond.base.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.security.core.userdetails.UserDetails;

import com.softlond.base.model.security.Authority;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuario", uniqueConstraints = { @UniqueConstraint(columnNames = { "nombre_usuario" }) })
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails, Serializable {


	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre_usuario")
    private String nombreUsuario;

    @Column(name = "contrasena", unique = true)
    private String contrasena;

    @Column(name = "cuenta_expirada")
    private boolean cuentaExpirada;

    @Column(name = "cuenta_bloqueada")
    private boolean cuentaBloqueada;

    @Column(name = "credenciales_expiradas")
    private boolean credencialesExpiradas;
    
    @Column(name = "activo")
    private boolean activo;

    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "autoridades_usuario", joinColumns = @JoinColumn(name = "id_usuario", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "id_autoridad", referencedColumnName = "id"))
    @OrderBy
    private Collection<Authority> autoridades;
    
    @Override
    public boolean isAccountNonExpired() {
        return !isAccountExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isAccountLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !isCredentialsExpired();
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return nombreUsuario;
	}

	public void setUsername(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getPassword() {
		return contrasena;
	}

	public void setPassword(String contrasena) {
		this.contrasena = contrasena;
	}

	public boolean isAccountExpired() {
		return cuentaExpirada;
	}

	public void setAccountExpired(boolean cuentaExpirada) {
		this.cuentaExpirada = cuentaExpirada;
	}

	public boolean isAccountLocked() {
		return cuentaBloqueada;
	}

	public void setAccountLocked(boolean cuentaBloqueada) {
		this.cuentaBloqueada = cuentaBloqueada;
	}

	public boolean isCredentialsExpired() {
		return credencialesExpiradas;
	}

	public void setCredentialsExpired(boolean credencialesExpiradas) {
		this.credencialesExpiradas = credencialesExpiradas;
	}

	public boolean isEnabled() {
		return activo;
	}

	public void setEnabled(boolean activo) {
		this.activo = activo;
	}

	public Collection<Authority> getAuthorities() {
		return autoridades;
	}

	public void setAuthorities(Collection<Authority> autoridades) {
		this.autoridades = autoridades;
	}

	public Usuario() {
		super();
	}

	public Usuario(Integer id, String nombreUsuario, String contrasena, boolean cuentaExpirada, boolean cuentaBloqueada,
			boolean credencialesExpiradas, boolean activo, Collection<Authority> autoridades) {
		super();
		this.id = id;
		this.nombreUsuario = nombreUsuario;
		this.contrasena = contrasena;
		this.cuentaExpirada = cuentaExpirada;
		this.cuentaBloqueada = cuentaBloqueada;
		this.credencialesExpiradas = credencialesExpiradas;
		this.activo = activo;
		this.autoridades = autoridades;
	}
	
}
