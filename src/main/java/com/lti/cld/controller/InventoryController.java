package com.lti.cld.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lti.cld.dto.ProductDTO;
import com.lti.cld.entity.Factory;
import com.lti.cld.entity.Product;
import com.lti.cld.service.InventoryService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/manage")
public class InventoryController {
	@Autowired
	InventoryService inventoryService;

	// Factories API
	@PostMapping("/add/factory")
	Factory addFactory(@RequestBody Factory factory) {
		return inventoryService.addOrUpdateFactory(factory);
	}

	@PutMapping("/update/factory")
	Factory updateFactory(@RequestBody Factory factory) {
		return inventoryService.addOrUpdateFactory(factory);
	}

	@DeleteMapping("/delete/factory/{factoryId}")
	ResponseEntity<Map<String,String>> deleteFactory(@PathVariable int factoryId) {
		boolean deleted = inventoryService.removeFactory(factoryId);
		
		if (deleted)
			return  ResponseEntity.status(HttpStatus.OK).body(Map.of("message","Deleted Successfully " + factoryId));
		return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message","Not Deleted Successfully " + factoryId));
	}

	@GetMapping("/viewAll/factory")
	List<Factory> viewAllfactories() {
		return inventoryService.viewAllFactories();
	}
	
	@GetMapping("/factory/{factoryId}")
	Factory viewFactory(@PathVariable int factoryId) {
		return inventoryService.getFactoryById(factoryId);
	}

	// Products API
	@PostMapping("/add/product")
	Product addProduct(@RequestBody ProductDTO productdto) {
		Product product = new Product();
		product.setFactory(inventoryService.getFactoryById(productdto.getFactoryId()));
		product.setProductName(productdto.getProductName());
		product.setDescription(productdto.getDescription());
		product.setQuantity(productdto.getQuantity());
		return inventoryService.addOrUpdateProduct(product);
	}

	@PutMapping("/update/product")
	Product updateProduct(@RequestBody ProductDTO productdto) {
		Product product = new Product();
		product.setFactory(inventoryService.getFactoryById(productdto.getFactoryId()));
		product.setProductName(productdto.getProductName());
		product.setDescription(productdto.getDescription());
		product.setQuantity(productdto.getQuantity());
		System.out.println(productdto.getQuantity());
		product.setProductId(productdto.getProductId());
		return inventoryService.addOrUpdateProduct(product);
	}

	@DeleteMapping("/delete/product/{productId}")
	ResponseEntity<Map<String,String>> deleteProduct(@PathVariable int productId) {
		boolean deleted = inventoryService.removeProduct(productId);
		if (deleted)
			return  ResponseEntity.status(HttpStatus.OK).body(Map.of("message","Deleted Successfully " + productId));
		return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message","Not Deleted Successfully " + productId));
	}

	@GetMapping("/viewAll/products/{factoryId}")
	List<Product> viewALlProducts(@PathVariable int factoryId) {
		return inventoryService.viewProductsByFactory(factoryId);
	}
}
