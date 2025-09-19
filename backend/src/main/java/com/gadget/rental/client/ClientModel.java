package com.gadget.rental.client;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "clientInfo")
@Table(name = "clientInfo")
public class ClientModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String password;
    private String email;
    @Convert(converter = ClientRoleConverter.class)
    private List<ClientRole> clientRoleList = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addRole(ClientRole clientRole) {
        this.clientRoleList.add(clientRole);
    }

    public void removeRole(ClientRole clientRole) {
        this.clientRoleList.remove(clientRole);
    }

    public void resetAllRoles() {
        this.clientRoleList.clear();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ClientRole> getClientRoleList() {
        return clientRoleList;
    }

    public void setClientRoleList(List<ClientRole> clientRoleList) {
        this.clientRoleList = clientRoleList;
    }
}
