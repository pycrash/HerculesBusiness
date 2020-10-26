package com.example.herculesbusiness.ViewCustomer;

import androidx.annotation.Nullable;

@SuppressWarnings("WeakerAccess")
public class Customer {
    public String name, phone, email;
    public String password, mailingName,address, pincode, state;
    public String contactName, contactNumber, gstin;
    public int discount;


    public Customer(String name, String phone, String email, String password, String mailingName, String address,
                    String pincode, String state, String contactName, String contactNumber, String gstin, int discount) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.mailingName = mailingName;
        this.address = address;
        this.pincode = pincode;
        this.state = state;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.gstin = gstin;
        this.discount = discount;
    }

    public Customer() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMailingName() {
        return mailingName;
    }

    public void setMailingName(String mailingName) {
        this.mailingName = mailingName;
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

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
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

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}