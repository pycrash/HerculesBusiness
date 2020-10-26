package com.example.herculesbusiness.ViewCustomer;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static com.example.herculesbusiness.ViewCustomer.Constants.LIMIT;
import static com.example.herculesbusiness.ViewCustomer.Constants.PRODUCT_NAME_PROPERTY;
import static com.example.herculesbusiness.ViewCustomer.Constants.USERS_COLLECTION;
import static com.google.firebase.firestore.Query.Direction.ASCENDING;

public class FirestoreCustomerListRepositoryCallback implements CustomerListViewModel.ProductListRepository,
        CustomerListLiveData.OnLastVisibleProductCallback, CustomerListLiveData.OnLastProductReachedCallback {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference productsRef = firebaseFirestore.collection(USERS_COLLECTION);
    private Query query = productsRef.orderBy(PRODUCT_NAME_PROPERTY, ASCENDING).limit(LIMIT);
    private DocumentSnapshot lastVisibleProduct;
    private boolean isLastProductReached;

    @Override
    public CustomerListLiveData getProductListLiveData() {
        if (isLastProductReached) {
            return null;
        }
        if (lastVisibleProduct != null) {
            query = query.startAfter(lastVisibleProduct);
        }
        return new CustomerListLiveData(query, this, this);
    }

    @Override
    public void setLastVisibleProduct(DocumentSnapshot lastVisibleProduct) {
        this.lastVisibleProduct = lastVisibleProduct;
    }

    @Override
    public void setLastProductReached(boolean isLastProductReached) {
        this.isLastProductReached = isLastProductReached;
    }
}