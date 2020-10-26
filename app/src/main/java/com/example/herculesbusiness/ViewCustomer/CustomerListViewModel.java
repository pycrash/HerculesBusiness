package com.example.herculesbusiness.ViewCustomer;

import androidx.lifecycle.ViewModel;

@SuppressWarnings("WeakerAccess")
public class CustomerListViewModel extends ViewModel {
    private ProductListRepository productListRepository = new FirestoreCustomerListRepositoryCallback();

    CustomerListLiveData getProductListLiveData() {
        return productListRepository.getProductListLiveData();
    }

    interface ProductListRepository {
        CustomerListLiveData getProductListLiveData();
    }
}