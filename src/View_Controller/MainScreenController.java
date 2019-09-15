/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import Model.Inventory;
import Model.InHouse;
import Model.Part;
import Model.Outsourced;
import Model.Product;
import java.io.IOException;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author tuanxn
 */
public class MainScreenController implements Initializable {

    @FXML
    private Button SearchPart;
    @FXML
    private Button ModifyPart;
    @FXML
    private Button AddPart;
    @FXML
    private Button DeletePart;
    @FXML
    private Button SearchProduct;
    @FXML
    private Button ModifyProduct;
    @FXML
    private Button AddProduct;
    @FXML
    private Button DeleteProduct;
    @FXML
    private Button Exit;
    @FXML
    private AnchorPane MainScreen;
    @FXML
    private TextField SearchPartText;
    @FXML
    private TableView<Part> PartTable;
    @FXML
    private TableColumn<Part, Integer> PartIdColumn;
    @FXML
    private TableColumn<Part, String> PartNameColumn;
    @FXML
    private TableColumn<Part, Integer> PartInvColumn;
    @FXML
    private TableColumn<Part, Double> PartPriceColumn;
    @FXML
    private TextField SearchProductText;
    @FXML
    private TableColumn<Product, Integer> ProductIdColumn;
    @FXML
    private TableColumn<Product, String> ProductNameColumn;
    @FXML
    private TableColumn<Product, Integer> ProductInvColumn;
    @FXML
    private TableColumn<Product, Double> ProductPriceColumn;
    @FXML
    private TableView<Product> ProductTable;
    
    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);    
    
    static boolean entered;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(!entered) {
            
            //If not initialized previously, input sample data
            Inventory.allParts.add(new InHouse(1, "leg", 2.50, 4, 100, 1, 100));
            Inventory.allParts.add(new InHouse(2, "top", 4.00, 2, 50, 1, 200));
            Inventory.allParts.add(new Outsourced(3, "vinyl sticker", 8.00, 3, 25, 1, "stickeria"));
            
            Inventory.allProducts.add(new Product(1, "Coffee table", 25.00, 1, 1, 10));
            Inventory.allProducts.add(new Product(2, "Standing desk", 100.99, 2, 1, 20));
            
            //Set flag for initialized to true
            entered=true;
        }
        
        //Map part table to part sample data
        PartIdColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        PartNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        PartInvColumn.setCellValueFactory(new PropertyValueFactory<>("Stock"));
        PartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("Price"));
        PartTable.setItems(Inventory.allParts);
        
        //Map product table to product sample data
        ProductIdColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        ProductNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        ProductInvColumn.setCellValueFactory(new PropertyValueFactory<>("Stock"));
        ProductPriceColumn.setCellValueFactory(new PropertyValueFactory<>("Price"));
        ProductTable.setItems(Inventory.allProducts);
    }    

    @FXML
    private void searchPartHandler(ActionEvent event) {
        
        //Search for part by ID number or text
        ObservableList<Part> partSearchResults = FXCollections.observableArrayList();
        String partSearch = SearchPartText.getText();
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
    private void modifyPartHandler(ActionEvent event) throws IOException{
        
        Part selectedPart=PartTable.getSelectionModel().getSelectedItem();
        
        //Pass selected product to correct InHouse or Outsourced screen
        if(selectedPart instanceof InHouse) {
            System.out.println("inhouse");
            InHouse part = (InHouse) selectedPart;
            Stage stage;
            Parent root;
            stage=(Stage) ModifyPart.getScene().getWindow();
            FXMLLoader loader=new FXMLLoader(getClass().getResource("InhousePartScreen.fxml"));
            root = loader.load();   
            InhousePartScreenController controller = loader.getController();
            controller.setPart(part, "Modify Part");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
        }else if(selectedPart instanceof Outsourced){
            System.out.println("outsourced");
            Outsourced part = (Outsourced) selectedPart;
            Stage stage;
            Parent root;
            stage=(Stage) ModifyPart.getScene().getWindow();
            FXMLLoader loader=new FXMLLoader(getClass().getResource("OutsourcedPartScreen.fxml"));
            root = loader.load(); 
            OutsourcedPartScreenController controller = loader.getController();
            controller.setPart(part, "Modify Part");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();            
            
        }else {
            return;
        }     
    }

    @FXML
    private void addPartHandler(ActionEvent event) throws IOException {
        Parent part_screen_parent = FXMLLoader.load(getClass().getResource("InhousePartScreen.fxml"));
        Scene part_screen_scene = new Scene(part_screen_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.hide();
        app_stage.setScene(part_screen_scene);
        app_stage.show();
    }

    @FXML
    private void deletePartHandler(ActionEvent event) {
        Part selectedPart=PartTable.getSelectionModel().getSelectedItem();
        
        confirmAlert.setContentText("Are you sure you want to delete this part?");
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Inventory.allParts.remove(selectedPart);        
        }          
    }

    @FXML
    private void searchProductHandler(ActionEvent event) {
        ObservableList<Product> productSearchResults = FXCollections.observableArrayList();
        String productSearch = SearchProductText.getText();
        for (Product p: Inventory.allProducts) {
            if(Integer.toString(p.getId()).equals(productSearch) || p.getName().toLowerCase().contains(productSearch.toLowerCase())) {
                productSearchResults.add(p);
            }
        }
        // If search text is empty, then return full list of parts
        if (productSearch == null) {
            ProductTable.setItems(Inventory.allProducts);
        }else{
            ProductTable.setItems(productSearchResults);
        }        
    }

    @FXML
    private void modifyProductHandler(ActionEvent event) throws IOException{
        Product selectedProduct=ProductTable.getSelectionModel().getSelectedItem();

        Stage stage;
        Parent root;
        stage=(Stage) ModifyProduct.getScene().getWindow();
        FXMLLoader loader=new FXMLLoader(getClass().getResource("ProductScreen.fxml"));
        root = loader.load();   
        ProductScreenController controller = loader.getController();
        controller.setProduct(selectedProduct, "Modify Product");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();       
    }

    @FXML
    private void addProductHandler(ActionEvent event) throws IOException{
        Parent part_screen_parent = FXMLLoader.load(getClass().getResource("ProductScreen.fxml"));
        Scene part_screen_scene = new Scene(part_screen_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.hide();
        app_stage.setScene(part_screen_scene);
        app_stage.show();      
    }

    @FXML
    private void deleteProductHandler(ActionEvent event) {   
        Product selectedPart=ProductTable.getSelectionModel().getSelectedItem();
        
        confirmAlert.setContentText("Are you sure you want to delete this product?");
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Inventory.allProducts.remove(selectedPart);         
        }      
    }

    @FXML
    private void exitHandler(ActionEvent event) {
        System.exit(0);
    }
    
}
