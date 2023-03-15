package com.lti.cld.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.lti.cld.dto.ProductDTO;
import com.lti.cld.entity.Factory;
import com.lti.cld.entity.Product;

public interface InventoryService {
	
	Factory addOrUpdateFactory(Factory factory);
	boolean removeFactory(int factoryId);
	List<Factory> viewAllFactories();
	Factory getFactoryById(int factoryId);
	
	Product addOrUpdateProduct(Product product,MultipartFile image);
	boolean removeProduct(int productId);
	ProductDTO getProductById(int productId);
	List<Product> viewAllProducts();
	List<ProductDTO> viewProductsByFactory(int factoryId);

}
