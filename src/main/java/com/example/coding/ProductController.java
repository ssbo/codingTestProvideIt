package com.example.coding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping(value="/", produces="application/json")
    @ResponseBody
    public ResponseEntity<ProductListingResponse> getProductListing(
            @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "8") int pageSize
    ) {
        try {
            Page<Product> productsPage = productService.getProductsByFilters(minPrice, maxPrice, category, page, pageSize);

            List<Product> products = removeDescriptions(productsPage.getContent());

            boolean hasNextPage = productsPage.hasNext();

            ProductListingResponse response = new ProductListingResponse(products, hasNextPage);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private List<Product> removeDescriptions(List<Product> products) {
        List<Product> lightProducts = new ArrayList<>();
        for (Product p : products) {
            Product lightProduct = new Product();
            lightProduct.setImage(p.getImage());
            lightProduct.setTitle(p.getTitle());
            lightProduct.setPrice(p.getPrice());
            lightProduct.setCategory(p.getCategory());
            lightProducts.add(lightProduct);
            lightProduct.setId(p.getId());
        }
        return lightProducts;
    }

    @GetMapping("/{productId}")
    public ProductDetails getProductDetails(@PathVariable Long productId) {
        Optional<Product> product = productService.getProductById(productId);
        if (product.isPresent()) {
            ProductDetails pd = new ProductDetails();
            pd.setDescription(product.get().getDescription());
            pd.setId(product.get().getId());
            pd.setCategory(product.get().getCategory());
            pd.setImage(product.get().getImage());
            pd.setPrice(product.get().getPrice());
            pd.setTitle(product.get().getTitle());
            return pd;
        }
        return null;
    }
}
