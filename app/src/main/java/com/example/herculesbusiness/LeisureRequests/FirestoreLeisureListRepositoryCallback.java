package com.example.herculesbusiness.LeisureRequests;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static com.example.herculesbusiness.LeisureRequests.LeisureConstants.LEISURE_COLLECTION;
import static com.example.herculesbusiness.ViewCustomer.Constants.LIMIT;
import static com.example.herculesbusiness.LeisureRequests.LeisureConstants.PRODUCT_NAME_PROPERTY;
import static com.google.firebase.firestore.Query.Direction.ASCENDING;

public class FirestoreLeisureListRepositoryCallback implements LeisureListViewModel.ProductListRepository,
        LeisureListLiveData.OnLastVisibleProductCallback, LeisureListLiveData.OnLastProductReachedCallback {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference productsRef = firebaseFirestore.collection(LEISURE_COLLECTION);
    private Query query = productsRef.orderBy(PRODUCT_NAME_PROPERTY, ASCENDING).limit(LIMIT);
    private DocumentSnapshot lastVisibleProduct;
    private boolean isLastProductReached;

    @Override
    public LeisureListLiveData getProductListLiveData() {
        if (isLastProductReached) {
            return null;
        }
        if (lastVisibleProduct != null) {
            query = query.startAfter(lastVisibleProduct);
        }
        return new LeisureListLiveData(query, this, this);
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