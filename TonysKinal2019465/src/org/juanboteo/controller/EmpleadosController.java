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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.juanboteo.bean.Empleado;
import org.juanboteo.bean.TipoEpleado;
import org.juanboteo.db.Conexion;
import org.juanboteo.main.Principal;


public class EmpleadosController implements Initializable{
    private enum operaciones {GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Empleado>listaEmpleado;
    private ObservableList<TipoEpleado>listaTipoEmpleado;
    
    @FXML private TextField txtCodigoEmpleado;
    @FXML private TextField txtNumeroEmpleado;
    @FXML private TextField txtApellido;
    @FXML private TextField txtNombre;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtGradoCocinero;
    @FXML private ComboBox cmbCodigoTipoEmpleado;
    @FXML private TableView tblEmpleados;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colNumeroEmpleado;
    @FXML private TableColumn colApellidosEmpleado;
    @FXML private TableColumn colNombresEmpleado;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colTelefono;
    @FXML private TableColumn colGradoCocinero;
    @FXML private TableColumn colCodigoTipoEmpleado;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoTipoEmpleado.setItems(getTipoEmpleado());
        cmbCodigoTipoEmpleado.setDisable(true);
    }
    
    public void cargarDatos(){
        tblEmpleados.setItems(getEmpleado());
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoEmpleado"));
        colNumeroEmpleado.setCellValueFactory(new PropertyValueFactory <Empleado, Integer>("numeroEmpleado"));
        colApellidosEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidosEmpleado"));
        colNombresEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado,String>("nombresEmpleado"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Empleado, String>("direccionEmpleado"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Empleado, String>("telefonoContacto"));
        colGradoCocinero.setCellValueFactory(new PropertyValueFactory<Empleado, String>("gradoCocinero"));
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoTipoEmpleado"));
    }
    public void seleccionarElemento(){
       if(tblEmpleados.getSelectionModel().getSelectedItem() !=null){
           txtCodigoEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
           txtNumeroEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
           txtApellido.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getApellidosEmpleado());
           txtNombre.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNombresEmpleado());
           txtDireccion.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getDireccionEmpleado());
           txtTelefono.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getTelefonoContacto());
           txtGradoCocinero.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getGradoCocinero());
           cmbCodigoTipoEmpleado.getSelectionModel().select(buscarTipoEmpleado(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
       }
    }
    
    public TipoEpleado buscarTipoEmpleado(int codigoTipoEmpleado){
        TipoEpleado resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarTipoEmpleado(?)");
            procedimiento.setInt(1, codigoTipoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TipoEpleado (registro.getInt("codigoTipoEmpleado"),
                    registro.getString("descripcion"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList<Empleado> getEmpleado(){
        ArrayList <Empleado> lista = new ArrayList<Empleado>();
        try{
            PreparedStatement procedimiento =  Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpleados");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empleado (resultado.getInt("codigoEmpleado"),
                        resultado.getInt("numeroEmpleado"),
                        resultado.getString("apellidosEmpleado"),
                        resultado.getString("nombresEmpleado"),
                        resultado.getString("direccionEmpleado"),
                        resultado.getString("telefonoContacto"),
                        resultado.getString("gradoCocinero"),
                        resultado.getInt("codigoTipoEmpleado")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpleado = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<TipoEpleado> getTipoEmpleado(){
        ArrayList<TipoEpleado> lista =  new ArrayList<TipoEpleado>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareStatement("call sp_ListarTiposEmpleados");
            ResultSet resultado =  procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new TipoEpleado(resultado.getInt("codigoTipoEmpleado"),
                        resultado.getString("descripcion")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTipoEmpleado = FXCollections.observableArrayList(lista);
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image ("/org/juanboteo/image/save.png"));
                imgEliminar.setImage(new Image("/org/juanboteo/image/eliminar.png"));
                tblEmpleados.getSelectionModel().clearSelection();
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText(("Nuevo"));
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image ("/org/juanboteo/image/foldergrey_93178.png"));
                imgEliminar.setImage(new Image ("/org/juanboteo/image/eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image ("/org/juanboteo/image/foldergrey_93178.png"));
                imgEliminar.setImage(new Image ("/org/juanboteo/image/delete.png"));
                tipoDeOperacion = operaciones.NINGUNO;
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
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblEmpleados.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Empleado", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarEmpleado(?)");
                            procedimiento.setInt(1, ((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                            listaEmpleado.remove(tblEmpleados.getSelectionModel().getFocusedIndex());
                            limpiarControles();
                            procedimiento.execute();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else if(respuesta == JOptionPane.NO_OPTION){
                        limpiarControles();
                        desactivarControles();
                        tblEmpleados.getSelectionModel().clearSelection();
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblEmpleados.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnNuevo.setDisable(true);
                    btnEliminar.setText("Cancelar");
                    btnEditar.setText("Actualizar");
                    btnReporte.setDisable(true);
                    imgEditar.setImage((new Image ("/org/juanboteo/image/actu.png")));
                    imgEliminar.setImage(new Image ("/org/juanboteo/image/eliminar.png"));
                    cmbCodigoTipoEmpleado.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setText("Eliminar");
                btnEditar.setText("Editar");
                btnReporte.setDisable(false);
                imgEditar.setImage(new Image("/org/juanboteo/image/edit.png"));
                imgEliminar.setImage(new Image("/org/juanboteo/image/delete.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        String text2 = txtNumeroEmpleado.getText();
        String text3 = txtApellido.getText();
        String text4 = txtNombre.getText();
        String text5 = txtDireccion.getText();
        String text6 = txtTelefono.getText();
        String text7 = txtGradoCocinero.getText();
        if(text2.length() == 0 || text3.length() == 0 || text4.length() == 0 || text5.length() == 0 || 
                text6.length() == 0 || text7.length() == 0 ){
           JOptionPane.showMessageDialog(null, "Faltan datos");
        }else{
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarEmpleado(?,?,?,?,?,?,?)");
            Empleado registro = (Empleado)tblEmpleados.getSelectionModel().getSelectedItem();
            registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
            registro.setApellidosEmpleado(txtApellido.getText());
            registro.setNombresEmpleado(txtNombre.getText());
            registro.setDireccionEmpleado(txtDireccion.getText());
            registro.setTelefonoContacto(txtTelefono.getText());
            registro.setGradoCocinero(txtGradoCocinero.getText());
            
            procedimiento.setInt(1, registro.getCodigoEmpleado());
            procedimiento.setInt(2, registro.getNumeroEmpleado());
            procedimiento.setString(3, registro.getApellidosEmpleado());
            procedimiento.setString(4, registro.getNombresEmpleado());
            procedimiento.setString(5, registro.getDireccionEmpleado());
            procedimiento.setString(6, registro.getTelefonoContacto());
            procedimiento.setString(7, registro.getGradoCocinero());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    }
    
    public void guardar(){
        Empleado registro = new Empleado();
        String text2 = txtNumeroEmpleado.getText();
        String text3 = txtApellido.getText();
        String text4 = txtNombre.getText();
        String text5 = txtDireccion.getText();
        String text6 = txtTelefono.getText();
        String text7 = txtGradoCocinero.getText();
        if(text2.length() == 0 || text3.length() == 0 || text4.length() == 0 || text5.length() == 0 || 
                text6.length() == 0 || text7.length() == 0 ||cmbCodigoTipoEmpleado.getSelectionModel() == null){
           JOptionPane.showMessageDialog(null, "Faltan datos");
        }else{
            registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
            registro.setApellidosEmpleado(txtApellido.getText());
            registro.setNombresEmpleado(txtNombre.getText());
            registro.setDireccionEmpleado(txtDireccion.getText());
            registro.setTelefonoContacto(txtTelefono.getText());
            registro.setGradoCocinero(txtGradoCocinero.getText());
            registro.setCodigoTipoEmpleado(((TipoEpleado)cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarEmpleado(?,?,?,?,?,?,?)");
                procedimiento.setInt(1, registro.getNumeroEmpleado());
                procedimiento.setString(2, registro.getApellidosEmpleado());
                procedimiento.setString(3, registro.getNombresEmpleado());
                procedimiento.setString(4, registro.getDireccionEmpleado());
                procedimiento.setString(5, registro.getTelefonoContacto());
                procedimiento.setString(6, registro.getGradoCocinero());
                procedimiento.setInt(7, registro.getCodigoTipoEmpleado());
                procedimiento.execute();
                listaEmpleado.add(registro);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
   public void desactivarControles(){
       txtCodigoEmpleado.setEditable(false);
       txtNumeroEmpleado.setEditable(false);
       txtApellido.setEditable(false);
       txtNombre.setEditable(false);
       txtDireccion.setEditable(false);
       txtTelefono.setEditable(false);
       txtGradoCocinero.setEditable(false);
       cmbCodigoTipoEmpleado.setDisable(true);
   } 
   
   public void activarControles(){
       txtCodigoEmpleado.setEditable(false);
       txtNumeroEmpleado.setEditable(true);
       txtApellido.setEditable(true);
       txtNombre.setEditable(true);
       txtDireccion.setEditable(true);
       txtTelefono.setEditable(true);
       txtGradoCocinero.setEditable(true);
       cmbCodigoTipoEmpleado.setDisable(false);
   }
   
   public void limpiarControles(){
       txtCodigoEmpleado.clear();
       txtNumeroEmpleado.clear();
       txtApellido.setText("");
       txtNombre.setText("");
       txtDireccion.setText("");
       txtTelefono.setText("");
       txtGradoCocinero.clear();
       cmbCodigoTipoEmpleado.setValue(null);
   }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void ventanaTipoEmpleado(){
        escenarioPrincipal.ventanaTipoEmpleado();
    }
}
