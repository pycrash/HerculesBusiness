package com.example.herculesbusiness.LeisureRequests;

import com.example.herculesbusiness.ViewCustomer.Customer;

class Operation {
    Customer customer;
    int type;

    Operation(Customer customer, int type) {
        this.customer = customer;
        this.type = type;
    }
}