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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.juanboteo.bean.TipoPlato;
import org.juanboteo.db.Conexion;
import org.juanboteo.main.Principal;

public class TipoPlatoController  implements Initializable{
    private enum operaciones{GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones TipoDeOperaciones = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<TipoPlato> listaTipoPlato;
    
    @FXML private TextField txtCodigoTipoPlato;
    @FXML private TextField txtDescripcion;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    @FXML private Button btnEditar;
    @FXML private TableView tblTipoPlato;
    @FXML private TableColumn colCodigoTipoPlato;
    @FXML private TableColumn colDescripcion;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private ImageView imgX;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        
    }
    public void cargarDatos(){
        tblTipoPlato.setItems(getTipoPlato());
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato, Integer>("codigoTipoPlato"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoPlato, String>("descripcionTipo"));
    }
    public ObservableList<TipoPlato>getTipoPlato(){
        ArrayList <TipoPlato>lista = new ArrayList<TipoPlato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTiposPlatos");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TipoPlato(resultado.getInt("codigoTipoPlato"),
                    resultado.getString("descripcionTipo")));
            }
        
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTipoPlato = FXCollections.observableList(lista);
        
                
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
                tblTipoPlato.getSelectionModel().clearSelection();
                TipoDeOperaciones = operaciones.GUARDAR;
                break;
            case GUARDAR:
               String text = txtDescripcion.getText();
               text = text.replaceAll(" ", "");
               if(text.length() == 0){
                   JOptionPane.showMessageDialog(null, "No hay datos para ingresar");
               
               }else{
                    guardar();
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
                    cargarDatos();
                    break;
               }
                
        }
    }
    
        public void editar(){
        switch(TipoDeOperaciones){
            case NINGUNO:
                if(tblTipoPlato.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnNuevo.setDisable(true);
                     btnEliminar.setText("Cancelar");
                    btnEditar.setText("Actualizar");
                    btnReporte.setDisable(true);
                    imgEditar.setImage(new Image("/org/juanboteo/image/actu.png"));
                    imgEliminar.setImage(new Image("/org/juanboteo/image/eliminar.png"));
                    TipoDeOperaciones = operaciones.ACTUALIZAR;
                    break;
                }else{
                    JOptionPane.showMessageDialog(null, "No ha seleccionado un elemento");
                }
            case ACTUALIZAR:
                String text = txtDescripcion.getText();
                text = text.replaceAll(" ", "");
                if(text.length() == 0){
                    JOptionPane.showMessageDialog(null, "No hay datos para actualizar.");
                }else{
                actualizar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setText("Eliminar");
                btnEditar.setText("Editar");
                btnReporte.setDisable(false);
                imgEditar.setImage(new Image("/org/juanboteo/image/edit.png"));
                imgEliminar.setImage(new Image("/org/juanboteo/image/delete.png"));
                TipoDeOperaciones = operaciones.NINGUNO;
                imgX.setVisible(false);
                cargarDatos();
                break;
                }
               }
    }
        
    public void eliminar(){
        switch(TipoDeOperaciones){
            case GUARDAR:
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/juanboteo/image/foldergrey_93178.png"));
                imgEliminar.setImage(new Image("/org/juanboteo/image/delete.png"));
                TipoDeOperaciones = operaciones.NINGUNO;
                imgX.setVisible(false);
                break;
            case ACTUALIZAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setText("Eliminar");
                btnEditar.setText("Editar");
                btnReporte.setDisable(false);
                imgEditar.setImage(new Image("/org/juanboteo/image/edit.png"));
                imgEliminar.setImage(new Image("/org/juanboteo/image/delete.png"));
                TipoDeOperaciones = operaciones.NINGUNO;
                imgX.setVisible(false);
                tblTipoPlato.getSelectionModel().clearSelection();
                break;
            default:
                if(tblTipoPlato.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro que quiere eliminar el dato?", "Eliminar Tipo de Plato", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarTipoPlato(?)");
                        procedimiento.setInt(1, ((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
                        procedimiento.execute();
                        listaTipoPlato.remove(tblTipoPlato.getSelectionModel().getSelectedItem());
                        limpiarControles();
                        tblTipoPlato.getSelectionModel().clearSelection();
                        procedimiento.execute();
                        
                            
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        limpiarControles();
                        tblTipoPlato.getSelectionModel().clearSelection();
                    }
                }
            
            
                
                
        }
    }
    public void guardar(){
        TipoPlato registro = new TipoPlato();
        registro.setDescripcionTipo(txtDescripcion.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarTipoPlato(?)");
            procedimiento.setString(1, registro.getDescripcionTipo());
            procedimiento.execute();
            listaTipoPlato.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarTipoPlato(?, ?);");
            TipoPlato registro = (TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem();
            registro.setDescripcionTipo(txtDescripcion.getText());
            procedimiento.setInt(1, registro.getCodigoTipoPlato());
            procedimiento.setString(2, registro.getDescripcionTipo());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
            
            
    public void limpiarControles(){
        txtCodigoTipoPlato.setText("");
        txtDescripcion.setText("");
    }
    public void desactivarControles(){
        txtCodigoTipoPlato.setEditable(false);
        txtDescripcion.setEditable(false);
    }
    public void activarControles(){
        txtCodigoTipoPlato.setEditable(false);
        txtDescripcion.setEditable(true);
    }
    
    public void seleccionarElemento(){
        if(tblTipoPlato.getSelectionModel().getSelectedItem() != null){
        txtCodigoTipoPlato.setText(String.valueOf(((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
        txtDescripcion.setText(((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getDescripcionTipo());
        }else{
            JOptionPane.showMessageDialog(null, "Seleccione un dato");
        }
    }
    
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    public void setEscenarioprincipal(Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    public void ventanaPlato(){
         escenarioPrincipal.ventanaPlato();
    
    }
}
