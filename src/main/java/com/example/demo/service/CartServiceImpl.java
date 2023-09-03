package com.example.demo.service;

import java.util.Set;
import java.util.HashSet;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CartDTO;
import com.example.demo.dto.MedicineDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.Product;
import com.example.demo.entity.Cart;
import com.example.demo.exception.EpharmacyException;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CartServiceImpl implements CartService{

	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private MedicineService medicineService;
	
	@Override
	public String addMedicinesToCart(CartDTO cart) throws EpharmacyException {
		// TODO Auto-generated method stub
		
		// u will be getting set of product dto's 
		//u have to convert each and every dto into entity		
		// adding in the basket means inserting in the database		
		Set<Product> p = new HashSet<>();   // final product entities will be saved		
		Set<ProductDTO> d = cart.getProducts(); // fetching the product dtos		
		for (ProductDTO productDTO : d) {		
			Product prod = new Product();		
			prod.setQuantity(productDTO.getQuantity());		
			prod.setMedicineId(productDTO.getMedicineDTO().getMedicineId());
					
			// product id is required
					
			productRepository.save(prod);
					
				p.add(prod);			
		}
				
		Optional<Cart> op = cartRepository.findByCustomerEmailId(cart.getCustomerEmailId());
				
		// if cart does not exists
				
		if(!op.isPresent()) {
					
			Cart c = new Cart();
					
			//set the products
					
			c.setProducts(p);
					
			c.setCustomerEmailId(cart.getCustomerEmailId());
					
			//?? insert it into the database
					
			cartRepository.save(c);
					
		}
			
		// we have the cart or basket with us
				
		// we have to add the products to the existing cart
				
		else {
					
			// optional to actual
					
			Cart e = op.get();
					
			// cart can have or cannot have products
					
			// we have few products to add
					
			//if cart has already had the product that we want to add then increase the quantity
					
			// if cart doesnot have the product that we want to add  --- proceed with the normal flow
					
			Set<Product> ex = e.getProducts(); //[ dairymilk - 2, kitkat - 1 , Aasha - 5]
					
			for (Product ref : p) {   // [ dairymilk - 3 ,aasha - 7 , milkybar-1]
						
				//  [ dairymilk - 2, kitkat - 1 , Aasha - 5]  --- already available in the cart    // 1. dairymilk
						
				boolean available = false;
						
				for (Product inCart : ex) {  //[ dairymilk - 2, kitkat - 1 , Aasha - 5]
							
					if(inCart.getMedicineId() .equals( ref.getMedicineId())) {
								
						inCart.setQuantity(ref.getQuantity() + inCart.getQuantity());
								
						productRepository.delete(ref);
								
						available = true;
					}
				}
						
				if(available == false) {
							
					e.getProducts().add(ref);
							
				}
			}
					
		}
		return "Medicine added to cart successfully";
	}

	@Override
	public Set<ProductDTO> getMedicinesFromCart(String email) throws EpharmacyException {
		// TODO Auto-generated method stub
		
		// check for cart existance
		
				Optional<Cart> op = cartRepository.findByCustomerEmailId(email);
				
				Cart c = op.orElseThrow(()-> new EpharmacyException("Cart doesnt exists"));
				
				//check for product existance
				
				if(c.getProducts().isEmpty()) {
					
					throw new EpharmacyException("Products are not available");
				}
				
				 // output set
				
				Set<ProductDTO> response = new HashSet<>();
				
				// input set
				
				Set<Product> s = c.getProducts();
				
				for (Product prod : s) {
					
					ProductDTO d  = new ProductDTO();
					
					d.setProductId(prod.getProductId());
					
					d.setQuantity(prod.getQuantity());
					
					// u r calling medicine service to get the details about a medicine
					
					MedicineDTO m =medicineService.getMedicineById(prod.getMedicineId());
					
					d.setMedicineDTO(m);
					
					response.add(d);
					
					
				}
				
				return response;
	}

	@Override
	public String modifyQuantityOfMedicinesInCart(String email, Integer productId, Integer quantity)
			throws EpharmacyException {
		// TODO Auto-generated method stub
		
		// cart existence??
		
				Optional<Cart> c = cartRepository.findByCustomerEmailId(email);
				
				Cart cr = c.orElseThrow(()->new EpharmacyException("Cart does not exists"));
				
				// products existence??
				
				if(cr.getProducts().isEmpty()) {
					
					throw new EpharmacyException("Products doesnt exists");
				}
				
				Product required = null;
				
				Set<Product> p = cr.getProducts();
				
				for (Product product : p) {
					
					if(product.getProductId().equals(productId)) {
						
						required = product;
						
						break;
					}
				}
				
				if(required == null) {
					
					throw new EpharmacyException("No such product exists");
					
				}
				
				required.setQuantity(quantity);
		
		return "Quantity Modified Successfully";
	}

	@Override
	public String deleteMedicineFromCart(String email, Integer productId) throws EpharmacyException {
		// TODO Auto-generated method stub
		// check for cart existance
		
				Optional<Cart> c = cartRepository.findByCustomerEmailId(email);
				
				Cart cart = c.orElseThrow(()->new EpharmacyException("NO CART FOUND"));
				
				// products availability
				
				// products = set
				if(cart.getProducts().isEmpty()) {
					
					throw new EpharmacyException("Products not available");
				}
				
				Product required = new Product();
				
				Set<Product> p = cart.getProducts(); // check in this set
				
				for (Product product : p) {
					
					if(productId.equals(product.getProductId())) {
						
						required = product;
						break;
						
					}
					
				}
				
				
				if(required == null) {
					
					throw new EpharmacyException("NO PRODUCT EXISTS");
				}
				
				// it is available 
				
				// 1. remove it from the cart
				
				// 2. remove it from the db
				
				cart.getProducts().remove(required);
				
				productRepository.delete(required);
				
				return "Product is deleted";
	}

	@Override
	public String deleteAllMedicinesFromCart(String email) throws EpharmacyException {
		// TODO Auto-generated method stub
		
		Optional<Cart> optional = cartRepository.findByCustomerEmailId(email);
		Cart cart = optional.orElseThrow(()->new EpharmacyException("NO_CART_AVAILABLE"));
		if(cart.getProducts().isEmpty()) {
			throw new EpharmacyException("NO_PRODUCTS_ADDED TO THE CART");
		}
		List<Integer> productIds = new ArrayList<>();
		//this is iterable if needed convert into dto - set then proceed with traditional way
		cart.getProducts().parallelStream().forEach(product ->{
			productIds.add(product.getProductId());
			cart.getProducts().remove(product);
		});
		for (Integer id: productIds) {
			productRepository.deleteById(id);
		} 
		
		
		return "All the products in the cart are deleted";

	}
	
}
