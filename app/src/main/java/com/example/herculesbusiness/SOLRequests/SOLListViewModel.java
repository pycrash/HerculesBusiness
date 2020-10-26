package com.example.herculesbusiness.SOLRequests;

import androidx.lifecycle.ViewModel;

import com.example.herculesbusiness.ViewCustomer.CustomerListLiveData;

@SuppressWarnings("WeakerAccess")
public class SOLListViewModel extends ViewModel {
    private ProductListRepository productListRepository = new FirestoreSOLListRepositoryCallback();

    SolListLiveData getProductListLiveData() {
        return productListRepository.getProductListLiveData();
    }

    interface ProductListRepository {
        SolListLiveData getProductListLiveData();
    }
}