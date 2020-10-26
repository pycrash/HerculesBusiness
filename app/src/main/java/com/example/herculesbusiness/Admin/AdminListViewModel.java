package com.example.herculesbusiness.Admin;

import androidx.lifecycle.ViewModel;

@SuppressWarnings("WeakerAccess")
public class AdminListViewModel extends ViewModel {
    private ProductListRepository productListRepository = new FirestoreAdminListRepositoryCallback();

    AdminListLiveData getProductListLiveData() {
        return productListRepository.getProductListLiveData();
    }

    interface ProductListRepository {
        AdminListLiveData getProductListLiveData();
    }
}