package com.example.demo5;

import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.*;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
      //  FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        TableView tableView = new TableView();


        ObservableList<Product> list= FXCollections.observableArrayList();
        FileInputStream fin=new FileInputStream("data.ser");
        ObjectInputStream oin=new ObjectInputStream(fin);
        try {
            Product p=(Product) oin.readObject();
            list.add(p);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        list.add(new Product("product 1",200));
        TableColumn<Product, String> nameColumn=new TableColumn<>("Product Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<Product,String>("name"));

        TableColumn<Product,Double> priceColumn=new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<Product,Double>("price"));
        tableView.setItems(list);
        tableView.getColumns().addAll(nameColumn,priceColumn);

        TextField nameField=new TextField();
        TextField priceField=new TextField();
        Button saveButton=new Button("Save");
        saveButton.setPrefWidth(50);
        HBox hbox=new HBox();
        hbox.getChildren().addAll(nameField,priceField,saveButton);
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(nameField.getText().length()>=1) {
                    list.add(new Product(nameField.getText(), Double.parseDouble(priceField.getText())));
                    try {
                        FileOutputStream fout=new FileOutputStream("data.ser");
                        ObjectOutputStream oout=new ObjectOutputStream(fout);
                        oout.writeObject(new Product(nameField.getText(), Double.parseDouble(priceField.getText())));
                        oout.close();
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                nameField.setText("");
                priceField.setText("");
            }
        });
        GridPane grid=new GridPane();
        grid.add(hbox,0,1,2,1);
        grid.add(tableView,0,0,2,1);
        Scene scene = new Scene(grid, 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}