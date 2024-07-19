package org.juanboteo.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.juanboteo.bean.Producto;
import org.juanboteo.db.Conexion;
import org.juanboteo.main.Principal;

public class ProductoController implements Initializable{
    private enum operaciones{GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones TipoDeOperaciones = operaciones.NINGUNO;
    
    private Principal escenarioPrincipal;
    private ObservableList<Producto> listaProducto;
    
    @FXML private TextField txtCodigoProducto;
    @FXML private TextField txtNombreProducto;
    @FXML private TextField txtCantidad;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    @FXML private Button btnEditar;
    @FXML private TableView tblProducto;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colNombreProducto;
    @FXML private TableColumn colCantidad;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private ImageView imgX;
    @FXML private ImageView imgX2;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblProducto.setItems(getProducto());
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("codigoProducto"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombreProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("cantidad"));
    }
    
    public ObservableList<Producto>getProducto(){
        ArrayList <Producto> lista = new ArrayList<Producto>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProductos");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Producto(resultado.getInt("codigoProducto"),
                            resultado.getString("nombreProducto"),
                            resultado.getInt("cantidad")));
            
            }
        
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaProducto = FXCollections.observableList(lista);
    }
    
    public void nuevo(){
        switch(TipoDeOperaciones){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/juanboteo/image/save.png"));
                imgEliminar.setImage(new Image("/org/juanboteo/image/eliminar.png"));
                tblProducto.getSelectionModel().clearSelection();
                TipoDeOperaciones = operaciones.GUARDAR;
                break;
            case GUARDAR:
               String text = txtNombreProducto.getText();
               String text2 = txtCantidad.getText();
               text2 = text2.replaceAll(" ", "");
               text = text.replaceAll(" ", "");
               if(text.length() == 0 || text2.length() == 0){
                   JOptionPane.showMessageDialog(null, "No  hay datos para ingresar");
               }else{
                    guardar();
                    desactivarControles();
                    limpiarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    imgNuevo.setImage(new Image("/org/juanboteo/image/foldergrey_93178.png"));
                    imgEliminar.setImage(new Image("/org/juanboteo/image/delete.png"));
                    TipoDeOperaciones = operaciones.NINGUNO;
                    cargarDatos();
                    imgX.setVisible(false);
                   imgX2.setVisible(false);
                    break;
               }
                
                
        }
        
    }
    public void editar(){
        switch(TipoDeOperaciones){
            case NINGUNO:
                if(tblProducto.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnNuevo.setDisable(true);
                    btnReporte.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnEliminar.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/juanboteo/image/actu.png"));
                    imgEliminar.setImage(new Image("/org/juanboteo/image/eliminar.png"));
                    TipoDeOperaciones = operaciones.ACTUALIZAR;
                    break;
                }else{
                    JOptionPane.showMessageDialog(null, "No ha seleccionado un elemento");
                }
            case ACTUALIZAR:
                String text = txtNombreProducto.getText();
                String text2 = txtCantidad.getText();
                text = text.replaceAll(" ", "");
                text2 = text2.replaceAll(" ", "");
                if(text.length() == 0 || text2.length() == 0){
                    JOptionPane.showMessageDialog(null, "No hay datos para actualizar.");
                }else{
                actualizar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnReporte.setDisable(false);
                btnEditar.setText("Editar");
                btnEliminar.setText("Eliminar");
                imgEditar.setImage(new Image("/org/juanboteo/image/edit.png"));
                imgEliminar.setImage(new Image("/org/juanboteo/image/delete.png"));
                TipoDeOperaciones = operaciones.NINGUNO;
                imgX2.setVisible(false);
                imgX.setVisible(false);
                cargarDatos();
                break;
                }
        }
    }
    
    public void eliminar(){
        switch(TipoDeOperaciones){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/juanboteo/image/foldergrey_93178.png"));
                imgEliminar.setImage(new Image("/org/juanboteo/image/delete.png"));
                TipoDeOperaciones = operaciones.NINGUNO;
                imgX.setVisible(false);
                imgX2.setVisible(false);
                break;
            case ACTUALIZAR:
                limpiarControles();
                desactivarControles();
                btnEditar.setText("Editar");
                btnEliminar.setText("Eliminar");
                btnNuevo.setDisable(false);
                btnReporte.setDisable(false);
                imgEditar.setImage(new Image("/org/juanboteo/image/edit.png"));
                imgEliminar.setImage(new Image("/org/juanboteo/image/delete.png"));
                TipoDeOperaciones = operaciones.NINGUNO;
                imgX2.setVisible(false);
                imgX.setVisible(false);
                tblProducto.getSelectionModel().clearSelection();
                break;
            default:
                if(tblProducto.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Está seguro que quiere eliminar el registro?", "Eliminar Producto", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarProducto(?)");
                            procedimiento.setInt(1, ((Producto)tblProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
                            procedimiento.execute();
                            listaProducto.remove(tblProducto.getSelectionModel().getSelectedItem());
                            limpiarControles();
                            tblProducto.getSelectionModel().clearSelection();
                            procedimiento.execute();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        limpiarControles();
                        tblProducto.getSelectionModel().clearSelection();
                    }
                    
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                
        }
    }
    
    public void guardar(){
        Producto registro = new Producto();
        registro.setNombreProducto(txtNombreProducto.getText());
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        
        //if(txtCantidad.getText().matches("-?([0-9]*)?")){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarProducto(?, ?)");
            procedimiento.setString(1, registro.getNombreProducto());
            procedimiento.setInt(2, registro.getCantidad());
            procedimiento.execute();
            listaProducto.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
        //return txtCantidad;
        
    
    }
   // }
    //public static boolean validarCantidad(int cantidad){
        
    //}
    
    
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarProductos(?,?,?)");
            Producto registro = (Producto)tblProducto.getSelectionModel().getSelectedItem();
            registro.setNombreProducto(txtNombreProducto.getText());
            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            procedimiento.setInt(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getNombreProducto());
            procedimiento.setInt(3, registro.getCantidad());
            procedimiento.execute();
        
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    public void seleccionarElemento(){
        if(tblProducto.getSelectionModel().getSelectedItem() != null){
        txtCodigoProducto.setText(String.valueOf(((Producto)tblProducto.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        txtNombreProducto.setText(((Producto)tblProducto.getSelectionModel().getSelectedItem()).getNombreProducto());
        txtCantidad.setText(String.valueOf(((Producto)tblProducto.getSelectionModel().getSelectedItem()).getCantidad()));
        }
        }
        
        
    
    public void desactivarControles(){
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(false);
        txtCantidad.setEditable(false);
    }
    public void limpiarControles(){
        txtCodigoProducto.setText("");
        txtNombreProducto.setText("");
        txtCantidad.setText("");
    }
    public void activarControles(){
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(true);
        txtCantidad.setEditable(true);
    }
    
    
    
    
    
    
    
    
    
    
    
    

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}
