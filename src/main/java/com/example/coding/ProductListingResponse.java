package com.example.coding;

import java.io.Serializable;
import java.util.List;

public class ProductListingResponse implements Serializable {
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    private List<Product> products;
    private boolean hasNextPage;

    public ProductListingResponse(List<Product> products, boolean hasNextPage) {
        this.products = products;
        this.hasNextPage = hasNextPage;
    }

}
