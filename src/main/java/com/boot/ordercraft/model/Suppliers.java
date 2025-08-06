package com.boot.ordercraft.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ORDER_CRAFT_SUPPLIERS")
public class Suppliers {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
   private Long supplier_id;
   private String supplier_name;
   private String contact_name;
   private String contact_email;
   private String phone;
   
   @OneToOne(cascade = CascadeType.ALL)
   @JoinColumn(name = "address_id", referencedColumnName = "addressId") // foreign key in OrderManagementUsers table
   private Address address;
   
   @ManyToOne
   @JoinColumn(name = "user_id", referencedColumnName = "user_id") // foreign key in OrderManagementUsers table
   private User user;

public Suppliers() {
	super();
	// TODO Auto-generated constructor stub
}

public Suppliers(Long supplier_id, String supplier_name, String contact_name, String contact_email, String phone,
		Address address, User user) {
	super();
	this.supplier_id = supplier_id;
	this.supplier_name = supplier_name;
	this.contact_name = contact_name;
	this.contact_email = contact_email;
	this.phone = phone;
	this.address = address;
	this.user = user;
}

public Long getSupplier_id() {
	return supplier_id;
}

public void setSupplier_id(Long supplier_id) {
	this.supplier_id = supplier_id;
}

public String getSupplier_name() {
	return supplier_name;
}

public void setSupplier_name(String supplier_name) {
	this.supplier_name = supplier_name;
}

public String getContact_name() {
	return contact_name;
}

public void setContact_name(String contact_name) {
	this.contact_name = contact_name;
}

public String getContact_email() {
	return contact_email;
}

public void setContact_email(String contact_email) {
	this.contact_email = contact_email;
}

public String getPhone() {
	return phone;
}

public void setPhone(String phone) {
	this.phone = phone;
}

public Address getAddress() {
	return address;
}

public void setAddress(Address address) {
	this.address = address;
}

public User getUser() {
	return user;
}

public void setUser(User user) {
	this.user = user;
}

@Override
public String toString() {
	return "Suppliers [supplier_id=" + supplier_id + ", supplier_name=" + supplier_name + ", contact_name="
			+ contact_name + ", contact_email=" + contact_email + ", phone=" + phone + ", address=" + address
			+ ", user=" + user + "]";
}
   
   
}
