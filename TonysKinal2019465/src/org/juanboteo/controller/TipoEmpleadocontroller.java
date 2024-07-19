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
import org.juanboteo.bean.TipoEpleado;
import org.juanboteo.db.Conexion;
import org.juanboteo.main.Principal;

public class TipoEmpleadocontroller implements Initializable{
    private enum operaciones{GUARDAR, EDITAR, ACTUALIZAR, ELIMINAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
   
    private Principal escenarioPrincipal;
    private ObservableList<TipoEpleado> listaTipoEmpleado;
    
    @FXML private TextField txtCodigoTipoEmpleado;
    @FXML private TextField txtDescripcion;
    @FXML private TableView tblTipoEmpleado;
    @FXML private TableColumn colCodigoTipoEmpleado;
    @FXML private TableColumn colDescripcion;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
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
            tblTipoEmpleado.setItems(getTipoEmpleado());
            colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEpleado, Integer>("codigoTipoEmpleado"));
            colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoEpleado, String>("descripcion"));
            
        }
        
        public ObservableList<TipoEpleado> getTipoEmpleado(){
            ArrayList<TipoEpleado> lista = new ArrayList<TipoEpleado>();
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTiposEmpleados");
                ResultSet resultado = procedimiento.executeQuery();
                while(resultado.next()){
                    lista.add(new TipoEpleado(resultado.getInt("codigoTipoEmpleado"),
                            resultado.getString("descripcion")));
     
                }
            
            }catch(Exception e){
                e.printStackTrace();
            }
            
            return listaTipoEmpleado = FXCollections.observableArrayList(lista); 
        }
        
        public void nuevo(){
            switch(tipoDeOperaciones){
                case NINGUNO:
                    limpiarControles();
                    activarControles();
                    btnNuevo.setText("Guardar");
                    btnEliminar.setText("Cancelar");
                    btnEditar.setDisable(true);
                    btnReporte.setDisable(true);
                    imgNuevo.setImage(new Image("/org/juanboteo/image/save.png"));
                    imgEliminar.setImage(new Image("/org/juanboteo/image/eliminar.png"));
                    tblTipoEmpleado.getSelectionModel().clearSelection();
                    tipoDeOperaciones = operaciones.GUARDAR;
                    break;
                case GUARDAR:
                    String text = txtDescripcion.getText();
                    text = text.replaceAll(" ", "");
                   if(text.length() == 0){
                       JOptionPane.showMessageDialog(null, "No hay datos para ingresar.");
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
                        tipoDeOperaciones = operaciones.NINGUNO;
                        cargarDatos();
                        break;
                   }
                    
                    
                    
            }
        }
        
        public void editar(){
            switch(tipoDeOperaciones){
                case NINGUNO:
                    if(tblTipoEmpleado.getSelectionModel().getSelectedItem() !=null){
                        btnNuevo.setDisable(true);
                        btnReporte.setDisable(true);
                        btnEditar.setText("Actualizar");
                        btnEliminar.setText("Cancelar");
                        imgEditar.setImage(new Image("/org/juanboteo/image/actu.png"));
                        imgEliminar.setImage(new Image("/org/juanboteo/image/eliminar.png"));
                        tipoDeOperaciones = operaciones.ACTUALIZAR;
                        activarControles();
                        break;
    
                    }else{
                        JOptionPane.showMessageDialog(null, "No ha seleccionado un eleménto");
                    }
                case ACTUALIZAR:
                    String text = txtDescripcion.getText();
                    text = text.replaceAll(" ", "");
                    if (text.length() == 0){
                        JOptionPane.showMessageDialog(null, "No hay datos para actualizar.");
                    }else{
                    actualizar();
                    limpiarControles();
                    desactivarControles();
                    btnEditar.setText("Editar");
                    btnEliminar.setText("Eliminar");
                    btnNuevo.setDisable(false);
                    btnReporte.setDisable(false);
                    imgEditar.setImage(new Image("/org/juanboteo/image/edit.png"));
                    imgEliminar.setImage(new Image("/org/juanboteo/image/delete.png"));
                    desactivarControles();
                    tipoDeOperaciones = operaciones.NINGUNO;
                    cargarDatos();
                    break;
                    }
            }
        }
        
        public void eliminar(){
            switch(tipoDeOperaciones){
                case GUARDAR:
                    limpiarControles();
                    desactivarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    imgNuevo.setImage(new Image("/org/juanboteo/image/foldergrey_93178.png"));
                    imgEliminar.setImage(new Image("/org/juanboteo/image/delete.png"));
                    tipoDeOperaciones = operaciones.NINGUNO;
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
                    tipoDeOperaciones = operaciones.NINGUNO;
                    tblTipoEmpleado.getSelectionModel().clearSelection();
                    break;
                    
                default:
                    if(tblTipoEmpleado.getSelectionModel().getSelectedItem() != null){
                        int respuesta = JOptionPane.showConfirmDialog(null, "Está seguro que quiere eliminar el registro?", "Eliminar Tipo de Empleado", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                            try{
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarTipoEmpleado(?)");
                                procedimiento.setInt(1, ((TipoEpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
                                procedimiento.execute();
                                listaTipoEmpleado.remove(tblTipoEmpleado.getSelectionModel().getSelectedItem());
                                limpiarControles();
                                tblTipoEmpleado.getSelectionModel().clearSelection();
                                procedimiento.execute();
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                         }else{
                            limpiarControles();
                            tblTipoEmpleado.getSelectionModel().clearSelection();
                        }
                            
                        
                    }else{
                        JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                    }
            }
        }
        
        public void actualizar(){
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarTipoEmpleado(?, ?)");
                TipoEpleado registro = (TipoEpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem();
                registro.setDescripcion(txtDescripcion.getText());
                procedimiento.setInt(1, registro.getCodigoTipoEmpleado());
                procedimiento.setString(2, registro.getDescripcion());
                procedimiento.execute();
            
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        
        
        public void guardar(){
            
            TipoEpleado registro = new TipoEpleado();
            registro.setDescripcion(txtDescripcion.getText());
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarTipoEmpleado(?)");
                procedimiento.setString(1, registro.getDescripcion());
                procedimiento.execute();
                listaTipoEmpleado.add(registro);
            
            }catch(Exception e){
                e.printStackTrace();
            
            }
        }
        
        public void seleccionarElemento(){
            if(tblTipoEmpleado.getSelectionModel().getSelectedItem() != null){
            txtCodigoTipoEmpleado.setText(String.valueOf(((TipoEpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
            txtDescripcion.setText(((TipoEpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getDescripcion());
            }else{
                JOptionPane.showMessageDialog(null, "Seleccione un dato");
            }
        }
        
        
        public void desactivarControles(){
            txtCodigoTipoEmpleado.setEditable(false);
            txtDescripcion.setEditable(false);
        }
        public void activarControles(){
            txtCodigoTipoEmpleado.setEditable(false);
            txtDescripcion.setEditable(true);
        }
        public void limpiarControles(){
            txtCodigoTipoEmpleado.setText("");
            txtDescripcion.setText("");
        }
        
    
    
    
    
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    public void setEscenarioPrincipal(Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    public void ventanaEmpleados(){
        escenarioPrincipal.ventanaEmpleados();
    }
    
}
