package com.gadget.rental.account.client;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.gadget.rental.shared.BaseAccountModel;

@Entity(name = "clientInfo")
@Table(name = "clientInfo")
public class ClientAccountModel extends BaseAccountModel {
}
