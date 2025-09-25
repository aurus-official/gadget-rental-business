package com.gadget.rental.account.admin;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.gadget.rental.shared.BaseAccountModel;

@Entity(name = "adminInfo")
@Table(name = "adminInfo")
public class AdminAccountModel extends BaseAccountModel {
    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    private String adminEmail;
}
