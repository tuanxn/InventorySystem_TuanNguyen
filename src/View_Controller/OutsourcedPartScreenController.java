/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author tuanxn
 */
public class OutsourcedPartScreenController implements Initializable {

    @FXML
    private Label PartScreenType;
    @FXML
    private RadioButton OutsourcedPart;
    @FXML
    private Button PartSave;
    @FXML
    private Button PartCancel;
    @FXML
    private TextField PartID;
    @FXML
    private TextField PartName;
    @FXML
    private TextField PartInv;
    @FXML
    private TextField PartPriceCost;
    @FXML
    private TextField PartMax;
    @FXML
    private TextField PartMin;
    @FXML
    private RadioButton InhousePart;
    @FXML
    private TextField PartCompanyName;
    @FXML
    private AnchorPane OutsourcedPartScreen;
    
    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);    
    
    //Variable to hold existing Outsourced part if modifying
    Outsourced existingPart;
    //Variable to hold reference to existing InHouse part if converting from existing InHouse to Outsourced
    InHouse oldPart;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    


    @FXML
    private void outsourcedPartHandler(ActionEvent event) {
    }

    @FXML
    private void partSaveHandler(ActionEvent event) throws IOException{
        
        // Determine next available Part Id if adding part
        int nextPartId = 0;
        if (PartScreenType.getText().equals("Add Part")) { 
            for (Part p: Inventory.allParts) {
                if (p.getId() > nextPartId) {
                    nextPartId = p.getId();
                }
            }
            nextPartId++;
        }else {
            // If not adding part, then use the ID from the existing part
            nextPartId = existingPart.getId();
        }

        
        // Grab entered text info for part
        String Name = PartName.getText();
        String Stock = PartInv.getText();
        String Price = PartPriceCost.getText();
        String Max = PartMax.getText();
        String Min = PartMin.getText();
        String CompanyName = PartCompanyName.getText();
        
        if(Integer.parseInt(Stock) < Integer.parseInt(Min) || Integer.parseInt(Stock) > Integer.parseInt(Max)) {
            errorAlert.setContentText("Please set inventory between minimum and maximum amounts.");
            errorAlert.showAndWait();
        }else{
                
                //Delete Outsourced part we're modifying (if applicable)
                Inventory.allParts.remove(existingPart);

                //Delete InHouse part we're converting to Outsourced (if applicable)
                Inventory.allParts.remove(oldPart);
                
                //Add new or modified outsource part
                //This means when we modify an existing part, we are deleting the old existing part and adding a new one in its place
                Inventory.allParts.add(new Outsourced(nextPartId,
                    Name,
                    Double.parseDouble(Price),
                    Integer.parseInt(Stock),
                    Integer.parseInt(Max),
                    Integer.parseInt(Min),
                    CompanyName
                ));

            Parent part_screen_parent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene part_screen_scene = new Scene(part_screen_parent);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.hide();
            app_stage.setScene(part_screen_scene);
            app_stage.show();
        }        
        
    }

    @FXML
    private void partCancelHandler(ActionEvent event) throws IOException{
        confirmAlert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Parent part_screen_parent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene part_screen_scene = new Scene(part_screen_parent);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.hide();
            app_stage.setScene(part_screen_scene);
            app_stage.show();            
        }
    }

    @FXML
    private void inhousePartHandler(ActionEvent event) throws IOException{
        
        //Create reference to InHouse part we are going to conver to
        InHouse changeToInhouse;
        
        if (PartScreenType.getText().equals("Add Part")) {
            // If we're adding a part, then get the next ID and grab any values we've already input
            int nextPartId = 0;
            for (Part p: Inventory.allParts) {
                if (p.getId() > nextPartId) {
                    nextPartId = p.getId();
                }
            }
            nextPartId++;
            
            changeToInhouse = new InHouse(
                nextPartId,
                PartName.getText(),
                Double.parseDouble(PartPriceCost.getText()),                
                Integer.parseInt(PartInv.getText()),
                Integer.parseInt(PartMax.getText()),
                Integer.parseInt(PartMin.getText()),
                0
            );            
        }else {        
            // If we are modifying and this is the first time we jumped to the InHouse screen
            if (oldPart == null) {
                System.out.println("test");
                changeToInhouse = new InHouse(
                Integer.parseInt(PartID.getText()),
                PartName.getText(),
                Double.parseDouble(PartPriceCost.getText()),                
                Integer.parseInt(PartInv.getText()),
                Integer.parseInt(PartMax.getText()),
                Integer.parseInt(PartMin.getText()),
                0
                );
            }else {
            // If we are modifying and we:
            // 1. Started with an InHouse part
            // 2. Converted to an Outsourced part
            // 3. Now jumping back to the InHouse part
                changeToInhouse = oldPart;
            }
            // If modifying existing part from inhouse to outsourced
            // Create new outsource object and bring over inhouse attributes

        }    
        
        Stage stage;
        Parent root;
        stage=(Stage) InhousePart.getScene().getWindow();
        FXMLLoader loader=new FXMLLoader(getClass().getResource("InhousePartScreen.fxml"));
        root = loader.load(); 
        InhousePartScreenController controller = loader.getController();
        
        // Stay on the correct add/modify screen label
        if (PartScreenType.getText().equals("Modify Part")) {
            controller.setPart(existingPart, changeToInhouse, "Modify Part");            
        }else {
            controller.setPart(changeToInhouse, "Add Part");  
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();         
    }
    
    public void setPart(Outsourced part, String label) {
        // Method to jump to modify part screen from Main Screen
        existingPart = part;
        
        PartScreenType.setText(label);
        PartID.setText(new Integer(part.getId()).toString());
        PartName.setText(part.getName());
        PartInv.setText(new Integer(part.getStock()).toString());
        PartPriceCost.setText(new Double(part.getPrice()).toString());
        PartMax.setText(new Integer(part.getMax()).toString());
        PartMin.setText(new Integer(part.getMin()).toString());
        PartCompanyName.setText(part.getCompanyName());
    }    
    
    public void setPart(InHouse oldpart, Outsourced part, String label) {
        // Method if you are converting an existing InHouse part to an Outsourced part
        // This is the method that is triggered when you click the Outsource radio button
        // When you are modifying an existing InHouse part
        oldPart = oldpart;
        existingPart = part;
        
        PartScreenType.setText(label);
        PartID.setText(new Integer(part.getId()).toString());
        PartName.setText(part.getName());
        PartInv.setText(new Integer(part.getStock()).toString());
        PartPriceCost.setText(new Double(part.getPrice()).toString());
        PartMax.setText(new Integer(part.getMax()).toString());
        PartMin.setText(new Integer(part.getMin()).toString());
        PartCompanyName.setText(part.getCompanyName());
    }        
    
}
