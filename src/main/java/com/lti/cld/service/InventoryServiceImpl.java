package com.lti.cld.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.lti.cld.dao.InventoryDao;
import com.lti.cld.dto.ProductDTO;
import com.lti.cld.entity.Factory;
import com.lti.cld.entity.Product;

@Service
public class InventoryServiceImpl implements InventoryService {

	@Autowired
	InventoryDao dao;

	@Autowired
	BlobContainerClient blobContainerClient;

	public Factory addOrUpdateFactory(Factory factory) {
		return dao.addOrUpdateFactory(factory);
	}

	@Override
	public boolean removeFactory(int factoryId) {
		dao.deleteFactory(factoryId);
		return true;
	}

	@Override
	public List<Factory> viewAllFactories() {

		return dao.viewAllFactories();
	}

	@Override
	public Product addOrUpdateProduct(Product product,MultipartFile image) {
		if(image!=null) {
			String fileExtension=image.getContentType().split("/")[1];
			String imageName;
			Product existingProduct=dao.getProductById(product.getProductId());
			if(product.getProductId()>0 && existingProduct.getImageName()!=null) {
				imageName=existingProduct.getImageName();//if product already exists
			}
			else {
				imageName="Product_image_"+UUID.randomUUID().toString()+"."+fileExtension;				
			}
			
			product.setImageName(imageName);
			
		
		try {
			
			BlobClient blobClient= blobContainerClient.getBlobClient(imageName);
			Map<String,String> metadata = new HashMap<>();
			
			blobClient.upload(image.getInputStream(),image.getSize(),true);
			product.setImageURL(blobClient.getBlobUrl());
			
			blobClient.setMetadata(metadata);
			
			System.out.println("Photo uploaded to blob");

			
		} catch (IOException e) {
			e.printStackTrace();
		}
		Product addedProduct=dao.addOrUpdateProduct(product);
		return addedProduct ;
		}
		else {
			if(product.getProductId()>0) {
				Product temp=dao.getProductById(product.getProductId());
				product.setImageName(temp.getImageName());
				product.setImageURL(temp.getImageURL());
				return dao.addOrUpdateProduct(product);
			}
			return dao.addOrUpdateProduct(product);
			
		}
		
	}

	@Override
	public boolean removeProduct(int productId) {
		BlobClient blobClient=blobContainerClient.getBlobClient(dao.getProductById(productId).getImageName());
		blobClient.delete();
		return dao.deleteProduct(productId);
	}

	@Override
	public List<Product> viewAllProducts() {
		return dao.viewAllProducts();
	}

	@Override
	public List<ProductDTO> viewProductsByFactory(int factoryId) {
		List<ProductDTO> updatedProducts=new ArrayList<>();
		List<Product> products= dao.viewAllProductsbyFactory(factoryId);
		for(Product p:products) {
			ProductDTO temp=new ProductDTO();
			temp.setImageUrl(p.getImageURL());
			temp.setDescription(p.getDescription());
			temp.setFactoryId(factoryId);
			temp.setProductId(p.getProductId());
			temp.setProductName(p.getProductName());
			temp.setQuantity(p.getQuantity());
			
			updatedProducts.add(temp);
		}
		
		return updatedProducts;
	}

	@Override
	public Factory getFactoryById(int factoryId) {

		return dao.getFactoryById(factoryId);
	}

	@Override
	public Product getProductById(int productId) {
		return dao.getProductById(productId);
	}

}
