/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Inventory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import Model.Part;
import Model.Product;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author tuanxn
 */
public class ProductScreenController implements Initializable {

    @FXML
    private Label ProductScreenType;
    @FXML
    private Button SearchProduct;
    @FXML
    private TextField ProductID;
    @FXML
    private TextField ProductName;
    @FXML
    private TextField ProductInv;
    @FXML
    private TextField ProductPrice;
    @FXML
    private TextField ProductMax;
    @FXML
    private TextField ProductMin;
    @FXML
    private TableColumn<Part, Integer> PartInv;
    @FXML
    private TableColumn<Part, Integer> PartID;
    @FXML
    private TableColumn<Part, String> PartName;
    @FXML
    private TableColumn<Part, Double> PartPrice;
    @FXML
    private TableColumn<Part, Integer> ProductPartInv;
    @FXML
    private TableColumn<Part, Integer> ProductPartID;
    @FXML
    private TableColumn<Part, String> ProductPartName;
    @FXML
    private TableColumn<Part, Double> ProductPartPrice;
    @FXML
    private Button AddPart;
    @FXML
    private Button DeletePart;
    @FXML
    private Button CancelProduct;
    @FXML
    private Button SaveProduct;
    @FXML
    private AnchorPane ProductScreen;
    @FXML
    private TableView<Part> PartTable;
    @FXML
    private TableView<Part> ProductTable;
    @FXML
    private TextField searchPartText;
    
    Alert errorAlert = new Alert(Alert.AlertType.ERROR);       
    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);    
    
    Product product = new Product(0,"name",0,0,0,0);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PartID.setCellValueFactory(new PropertyValueFactory<>("Id"));
        PartName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        PartInv.setCellValueFactory(new PropertyValueFactory<>("Stock"));
        PartPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        PartTable.setItems(Inventory.allParts);
        
        ProductPartID.setCellValueFactory(new PropertyValueFactory<>("Id"));
        ProductPartName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        ProductPartInv.setCellValueFactory(new PropertyValueFactory<>("Stock"));
        ProductPartPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        ProductTable.setItems(product.getAllAssociatedParts());
        
    }    

    @FXML
    private void searchProductHandler(ActionEvent event) {
        ObservableList<Part> partSearchResults = FXCollections.observableArrayList();
        String partSearch = searchPartText.getText();
        for (Part p: Inventory.allParts) {
            if(Integer.toString(p.getId()).equals(partSearch) || p.getName().toLowerCase().contains(partSearch.toLowerCase())) {
                partSearchResults.add(p);
            }
        }
        // If search text is empty, then return full list of parts
        if (partSearch == null) {
            PartTable.setItems(Inventory.allParts);
        }else{
            PartTable.setItems(partSearchResults);
        }        
    }

    @FXML
    private void addPartHandler(ActionEvent event) {
        Part selectedPart=PartTable.getSelectionModel().getSelectedItem();
        product.addAssociatedPart(selectedPart);
        //TODO           
    }

    @FXML
    private void deletePartHandler(ActionEvent event) {
        Part selectedPart=ProductTable.getSelectionModel().getSelectedItem();
        
        confirmAlert.setContentText("Are you sure you want to delete this part from the product?");
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.get() == ButtonType.OK) {
            product.deleteAssociatedPart(selectedPart);       
        }
    }

    @FXML
    private void cancelProductHandler(ActionEvent event) throws IOException{
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
    private void saveProductHandler(ActionEvent event) throws IOException{
        // Determine next available Product Id
        int nextProductId = 0;
        for (Product p: Inventory.allProducts) {
            if (p.getId() > nextProductId) {
                nextProductId = p.getId();
            }
        }
        nextProductId++;
        
        Double productCost = 0.0;
        
        for(Part p: product.getAllAssociatedParts()) {
            productCost += (p.getPrice());
        }
        
        if (ProductName.getText().isEmpty() || ProductPrice.getText().isEmpty() || ProductInv.getText().isEmpty()){
            errorAlert.setContentText("Product must have a name, price, and inventory level");
            errorAlert.showAndWait();
            if (ProductInv.getText().isEmpty()) {
                ProductInv.setText("0");
            }
        }else if(Integer.parseInt(ProductInv.getText()) < Integer.parseInt(ProductMin.getText()) || Integer.parseInt(ProductInv.getText()) > Integer.parseInt(ProductMax.getText())) {
            errorAlert.setContentText("Please set inventory between minimum and maximum amounts.");
            errorAlert.showAndWait();        
        }else if(Double.parseDouble(ProductPrice.getText()) < productCost) {
            errorAlert.setContentText("Product price must be at least the cost of the parts: " + productCost.toString());
            errorAlert.showAndWait();
        }else {
            // Grab entered info for new Part
            product.setName(ProductName.getText());
            product.setStock(Integer.parseInt(ProductInv.getText()));
            product.setPrice(Double.parseDouble((ProductPrice.getText())));
            product.setMax(Integer.parseInt((ProductMax.getText())));
            product.setMin(Integer.parseInt(ProductMin.getText()));

            if(ProductScreenType.getText().equalsIgnoreCase("Add Product")) {
                System.out.println("this worked");
                product.setId(nextProductId);
                            System.out.println("this worked");
                Inventory.allProducts.add(product);  
                            System.out.println("this worked");
            }

            Parent part_screen_parent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene part_screen_scene = new Scene(part_screen_parent);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.hide();
            app_stage.setScene(part_screen_scene);
            app_stage.show();
        }
    }
    
    public void setProduct(Product existingProduct, String label) {
        
        product = existingProduct;
        
        ProductScreenType.setText(label);
        ProductID.setText(new Integer(product.getId()).toString());
        ProductName.setText(product.getName());
        ProductInv.setText(new Integer(product.getStock()).toString());
        ProductPrice.setText(new Double(product.getPrice()).toString());
        ProductMax.setText(new Integer(product.getMax()).toString());
        ProductMin.setText(new Integer(product.getMin()).toString());
        
        try {
            ProductPartID.setCellValueFactory(new PropertyValueFactory<>("Id"));
            ProductPartName.setCellValueFactory(new PropertyValueFactory<>("Name"));
            ProductPartInv.setCellValueFactory(new PropertyValueFactory<>("Stock"));
            ProductPartPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
            ProductTable.setItems(product.getAllAssociatedParts());
        }catch (Exception e) {
            System.out.println("No part list for this product");
        }    
        
        
    }
    
}
