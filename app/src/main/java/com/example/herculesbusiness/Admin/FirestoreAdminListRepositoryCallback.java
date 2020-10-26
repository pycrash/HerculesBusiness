package com.example.herculesbusiness.Admin;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static com.example.herculesbusiness.Admin.Constants.ADMIN_COLLECTION;
import static com.example.herculesbusiness.Admin.Constants.LIMIT;
import static com.example.herculesbusiness.Admin.Constants.NAME_PROPERTY;
import static com.google.firebase.firestore.Query.Direction.ASCENDING;

public class FirestoreAdminListRepositoryCallback implements AdminListViewModel.ProductListRepository,
        AdminListLiveData.OnLastVisibleProductCallback, AdminListLiveData.OnLastProductReachedCallback {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference productsRef = firebaseFirestore.collection(ADMIN_COLLECTION);
    private Query query = productsRef.orderBy(NAME_PROPERTY, ASCENDING).limit(LIMIT);
    private DocumentSnapshot lastVisibleProduct;
    private boolean isLastProductReached;

    @Override
    public AdminListLiveData getProductListLiveData() {
        if (isLastProductReached) {
            return null;
        }
        if (lastVisibleProduct != null) {
            query = query.startAfter(lastVisibleProduct);
        }
        return new AdminListLiveData(query, this, this);
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