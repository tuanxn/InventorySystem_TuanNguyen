/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Model.Part;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author tuanxn
 */
public class Inventory {
    
    public static ObservableList<Part> allParts = FXCollections.observableArrayList();
    public static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    
    public void addPart(Part newPart) {
        allParts.add(newPart);
    }
    
    public void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }
    
    /* Set result to null and result to an actual part/product only if found
    Loop through the respective lists and check to see if it matches the Id provided
    If it does, then assign that part/product to result and return it.
    */
    
    public Part lookupPart(int partId) {
        Part result = null;
        for (Part p: allParts) {
            if(p.getId() == partId) {
                System.out.println("Found part " + p.getName());
                result = p;
            }
        }
        return result;
    }
    
    public Product lookupProduct(int productId) {
        Product result = null;
        for (Product p: allProducts) {
            if(p.getId() == productId) {
                System.out.println("Found product " + p.getName());
                result = p;
            }
        }
        return result;
    }
    
    public ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> searchResults = FXCollections.observableArrayList();
        for (Part p: allParts) {
            if(p.getName().toLowerCase().contains(partName.toLowerCase())) {
                searchResults.add(p);
            }
        }
        return searchResults;
    }
    
    public ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> searchResults = FXCollections.observableArrayList();
        for (Product p: allProducts) {
            if(p.getName().toLowerCase().contains(productName.toLowerCase())) {
                searchResults.add(p);
            }
        }
        return searchResults;
    }
    
    public void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }
    
    public void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }
    
    public void deletePart(Part selectedPart) {
        allParts.remove(selectedPart);
    }
    
    public void deleteProduct(Product selectedProduct) {
        allProducts.remove(selectedProduct);
    }
    
    public ObservableList<Part> getAllParts() {
        return allParts;
    }
    
    public ObservableList<Product> getAllProducts() {
        return allProducts;
    }
    
}
