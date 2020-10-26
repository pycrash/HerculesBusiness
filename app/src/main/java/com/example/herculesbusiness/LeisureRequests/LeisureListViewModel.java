package com.example.herculesbusiness.LeisureRequests;

import androidx.lifecycle.ViewModel;

@SuppressWarnings("WeakerAccess")
public class LeisureListViewModel extends ViewModel {
    private ProductListRepository productListRepository = new FirestoreLeisureListRepositoryCallback();

    LeisureListLiveData getProductListLiveData() {
        return productListRepository.getProductListLiveData();
    }

    interface ProductListRepository {
        LeisureListLiveData getProductListLiveData();
    }
}