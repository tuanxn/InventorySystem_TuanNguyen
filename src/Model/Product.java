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
public class Product {
    protected ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int Id;
    private String Name;
    private double Price;
    private int Stock;
    private int Min;
    private int Max;
    
    public Product(int id, String name, double price, int stock, int min, int max) {
        setId(id);
        setName(name);
        setPrice(price);
        setStock(stock);
        setMin(min);
        setMax(max);
    }
    
    public void setId(int id) {
        this.Id = id;
    }
    
    public void setName(String name) {
        this.Name = name;
    }
    
    public void setPrice(double price) {
        this.Price = price;
    }
    
    public void setStock(int stock) {
        this.Stock = stock;
    }
    
    public void setMin(int min) {
        this.Min = min;
    }
    
    public void setMax(int max) {
        this.Max = max;
    }
    
    public int getId() {
        return Id;
    }
    
    public String getName() {
        return Name;
    }
    
    public double getPrice() {
        return Price;
    }
    
    public int getStock() {
        return Stock;
    }
    
    public int getMin() {
        return Min;
    }
    
    public int getMax() {
        return Max;
    }
    
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }
    
    public void deleteAssociatedPart(Part associatedPart) {
        associatedParts.remove(associatedPart);
    }
    
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}
