package com.softlond.base.request;

import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Usuario;

import lombok.Data;

@Data
public class AccountRequest {

	Usuario user;
    InformacionUsuario userInfo;
    
    public AccountRequest() {
        super();
    }

    public AccountRequest(Usuario user, InformacionUsuario userInfo) {
        this.user = user;
        this.userInfo = userInfo;
    }
}
