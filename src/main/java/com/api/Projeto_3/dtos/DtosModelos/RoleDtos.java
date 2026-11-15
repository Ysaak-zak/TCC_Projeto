package com.api.Projeto_3.dtos.DtosModelos;

import com.api.Projeto_3.model.RolesModel;

public class RoleDtos {
    private Long id;
    private String authority;


    public RoleDtos(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    public RoleDtos(RolesModel roles) {
    if (roles != null) {
        this.id = roles.getId();
        this.authority = roles.getAuthority();
    }
}

    public RoleDtos(Long id) {
        this.id = id;

    }

    


    public RoleDtos() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return "RoleDtos [id=" + id + ", authority=" + authority + "]";
    }

    

    

}
