/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Inventory;
import Model.InHouse;
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
import javafx.scene.control.Alert.AlertType;
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
public class InhousePartScreenController implements Initializable {

    @FXML
    private Label PartScreenType;
    @FXML
    private RadioButton InhousePart;
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
    private TextField PartMachineID;
    @FXML
    private AnchorPane InhousePartScreen;
    
    Alert errorAlert = new Alert(AlertType.ERROR);
    Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
    
    InHouse existingPart;
    Outsourced oldPart;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void inhousePartHandler(ActionEvent event) {
    }

    @FXML
    private void outsourcedPartHandler(ActionEvent event) throws IOException{
        
        Outsourced changeToOutsourced;
        
        if (PartScreenType.getText().equals("Add Part")) {
            int nextPartId = 0;
            for (Part p: Inventory.allParts) {
                if (p.getId() > nextPartId) {
                    nextPartId = p.getId();
                }
            }
            nextPartId++;
            
            changeToOutsourced = new Outsourced(
            nextPartId,
            PartName.getText(),
            Double.parseDouble(PartPriceCost.getText()),                
            Integer.parseInt(PartInv.getText()),
            Integer.parseInt(PartMax.getText()),
            Integer.parseInt(PartMin.getText()),
            ""
            );            
        }else {

            if (oldPart == null) {
                changeToOutsourced = new Outsourced(
                    Integer.parseInt(PartID.getText()),
                    PartName.getText(),
                    Double.parseDouble(PartPriceCost.getText()),                
                    Integer.parseInt(PartInv.getText()),
                    Integer.parseInt(PartMax.getText()),
                    Integer.parseInt(PartMin.getText()),
                    ""
                );        
            }else {
                changeToOutsourced = oldPart;
            }
        }
        
        Stage stage;
        Parent root;
        stage=(Stage) OutsourcedPart.getScene().getWindow();
        FXMLLoader loader=new FXMLLoader(getClass().getResource("OutsourcedPartScreen.fxml"));
        root = loader.load(); 
        OutsourcedPartScreenController controller = loader.getController();
        
        // Stay on the correct add/modify screen label
        if (PartScreenType.getText().equals("Modify Part")) {
            controller.setPart(existingPart, changeToOutsourced, "Modify Part");            
        }else {
            controller.setPart(changeToOutsourced, "Add Part");  
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();         
        
    }

    @FXML
    private void partSaveHandler(ActionEvent event) throws IOException{
        
        // Determine next available Part Id
        int nextPartId = 0;
        if (PartScreenType.getText().equals("Add Part")) { 
            for (Part p: Inventory.allParts) {
                if (p.getId() > nextPartId) {
                    nextPartId = p.getId();
                }
            }
            nextPartId++;
        }else {
            nextPartId = existingPart.getId();
        }
        
        // Grab entered info for new Part
        String Name = PartName.getText();
        String Stock = PartInv.getText();
        String Price = PartPriceCost.getText();
        String Max = PartMax.getText();
        String Min = PartMin.getText();
        String MachineId = PartMachineID.getText();
        
        if(Integer.parseInt(Stock) < Integer.parseInt(Min) || Integer.parseInt(Stock) > Integer.parseInt(Max)) {
            errorAlert.setContentText("Please set inventory between minimum and maximum amounts.");
            errorAlert.showAndWait();
        }else{

                //Delete Inhouse part we're modifying
                Inventory.allParts.remove(existingPart);

                //Delete Outsource part we're converting to Inhouse
                Inventory.allParts.remove(oldPart);

                //Add new or modified Inhouse part
                Inventory.allParts.add(new InHouse(nextPartId,
                    Name,
                    Double.parseDouble(Price),
                    Integer.parseInt(Stock),
                    Integer.parseInt(Max),
                    Integer.parseInt(Min),
                    Integer.parseInt(MachineId)
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
    
    public void setPart(InHouse part, String label) {
        
        existingPart = part;
        
        PartScreenType.setText(label);
        PartID.setText(new Integer(part.getId()).toString());
        PartName.setText(part.getName());
        PartInv.setText(new Integer(part.getStock()).toString());
        PartPriceCost.setText(new Double(part.getPrice()).toString());
        PartMax.setText(new Integer(part.getMax()).toString());
        PartMin.setText(new Integer(part.getMin()).toString());
        PartMachineID.setText(new Integer(part.getMachineId()).toString());
    }
    
    public void setPart(Outsourced oldpart, InHouse part, String label) {
        
        oldPart = oldpart;
        existingPart = part;
        
        PartScreenType.setText(label);
        PartID.setText(new Integer(part.getId()).toString());
        PartName.setText(part.getName());
        PartInv.setText(new Integer(part.getStock()).toString());
        PartPriceCost.setText(new Double(part.getPrice()).toString());
        PartMax.setText(new Integer(part.getMax()).toString());
        PartMin.setText(new Integer(part.getMin()).toString());
        PartMachineID.setText(new Integer(part.getMachineId()).toString());
    }    
    
}
