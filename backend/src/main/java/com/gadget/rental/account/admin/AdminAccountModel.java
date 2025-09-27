package com.gadget.rental.account.admin;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.gadget.rental.shared.BaseAccountModel;

@Entity(name = "adminInfo")
@Table(name = "adminInfo")
public class AdminAccountModel extends BaseAccountModel implements Serializable {
    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    private String adminEmail;
}
