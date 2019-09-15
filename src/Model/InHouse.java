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
public class InHouse extends Part {
    private int MachineId;
    
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, max, min);
        setMachineId(machineId);
    }
    
    public void setMachineId(int machineId) {
        this.MachineId = machineId;
    }
    
    public int getMachineId() {
        return MachineId;
    }
    
}
