package com.example.herculesbusiness.Models;


import java.io.Serializable;
import java.util.List;

public class OrderModel implements Serializable {
    private String orderID, date, name, phone, email, contactName, mailingName, contactNumber, gstin, discount, address, pincode, state, status;
    private String newTotal, total, notes;
    private List<Product> cart;
    boolean cancelled;

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public OrderModel(String orderID, String date, String name, String phone, String email, String contactName, String mailingName,
                      String contactNumber, String gstin, String discount, String address, String pincode, String state,
                      String status, String newTotal, String total, List<Product> cart, String notes, boolean cancelled) {
        this.orderID = orderID;
        this.date = date;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.contactName = contactName;
        this.mailingName = mailingName;
        this.contactNumber = contactNumber;
        this.gstin = gstin;
        this.discount = discount;
        this.address = address;
        this.pincode = pincode;
        this.state = state;
        this.status = status;
        this.newTotal = newTotal;
        this.total = total;
        this.cart = cart;
        this.notes = notes;
        this.cancelled = cancelled;
    }

    public OrderModel() {
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getMailingName() {
        return mailingName;
    }

    public void setMailingName(String mailingName) {
        this.mailingName = mailingName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNewTotal() {
        return newTotal;
    }

    public void setNewTotal(String newTotal) {
        this.newTotal = newTotal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Product> getCart() {
        return cart;
    }

    public void setCart(List<Product> cart) {
        this.cart = cart;
    }
}


