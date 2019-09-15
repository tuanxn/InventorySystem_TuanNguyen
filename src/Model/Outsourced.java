/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Model.Part;

/**
 *
 * @author tuanxn
 */
public class Outsourced extends Part {
    private String CompanyName;
    
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, max, min);
        setCompanyName(companyName);
    }
    
    public void setCompanyName(String companyName) {
        this.CompanyName = companyName;
    }
    
    public String getCompanyName() {
        return CompanyName;
    }
    
}
