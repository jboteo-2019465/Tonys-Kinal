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
import org.juanboteo.bean.Plato;
import org.juanboteo.bean.Servicio;
import org.juanboteo.bean.ServiciosHasPlatos;
import org.juanboteo.db.Conexion;
import org.juanboteo.main.Principal;


public class Servicios_has_PlatosController implements Initializable{
    private enum operaciones {GUARDAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<ServiciosHasPlatos>listaServiciosHasPlatos;
    private ObservableList<Plato>listaPlato;
    private ObservableList<Servicio>listaServicio;
    
    @FXML private TextField txtCodigo;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private TableView tblServiciosHasPlatos;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableColumn colCodigoServicio;
    @FXML private Button btnNuevo;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgReporte;
        
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoPlato.setItems(getPlato());
        cmbCodigoServicio.setItems(getServicio());
        cmbCodigoPlato.setDisable(true);
        cmbCodigoServicio.setDisable(true);
    }
    
    public void cargarDatos(){
        tblServiciosHasPlatos.setItems(getServiciosHasPlatos());
        colCodigo.setCellValueFactory(new PropertyValueFactory <ServiciosHasPlatos, Integer>("Servicios_codigoServicio"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory <ServiciosHasPlatos, Integer>("codigoPlato"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory <ServiciosHasPlatos, Integer>("codigoServicio"));
    }
    
    public void seleccionarElemento(){
        if(tblServiciosHasPlatos.getSelectionModel().getSelectedItem() !=null){
            txtCodigo.setText(String.valueOf(((ServiciosHasPlatos)tblServiciosHasPlatos.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
            cmbCodigoPlato.getSelectionModel().select(buscarPlato(((ServiciosHasPlatos)tblServiciosHasPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
            cmbCodigoServicio.getSelectionModel().select(buscarServicio(((ServiciosHasPlatos)tblServiciosHasPlatos.getSelectionModel().getSelectedItem()).getCodigoServicio()));

        }
    }
    
    public Plato buscarPlato(int codigoPlato){
        Plato resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarPlato(?)");
            procedimiento.setInt(1, codigoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Plato (registro.getInt("codigoPlato"),
                        registro.getInt("cantidadPlato"),
                        registro.getString("nombrePlato"),
                        registro.getString("descripcionPlato"),
                        registro.getDouble("precioPlato"),
                        registro.getInt("codigoTipoPlato"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public Servicio buscarServicio (int codigoServicio){
        Servicio resultado =  null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarServicio(?)");
            procedimiento.setInt(1, codigoServicio);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado =  new Servicio (registro.getInt("codigoServicio"),
                        registro.getDate("fechaServicio"),
                        registro.getString("tipoServicio"),
                        registro.getString("horaServicio"),
                        registro.getString("lugarServicio"),
                        registro.getString("telefonoContacto"),
                        registro.getInt("codigoEmpresa"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }

    public ObservableList<ServiciosHasPlatos> getServiciosHasPlatos(){
        ArrayList<ServiciosHasPlatos> lista = new ArrayList<ServiciosHasPlatos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios_has_Platos");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new ServiciosHasPlatos(resultado.getInt("Servicios_codigoServicio"),
                        resultado.getInt("codigoPlato"),
                        resultado.getInt("codigoServicio")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaServiciosHasPlatos = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Plato> getPlato(){
        ArrayList<Plato> lista = new ArrayList<Plato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarPlatos");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Plato(resultado.getInt("codigoPlato"),
                        resultado.getInt("cantidadPlato"),
                        resultado.getString("nombrePlato"),
                        resultado.getString("descripcionPlato"),
                        resultado.getDouble("precioPlato"),
                        resultado.getInt("codigoTipoPlato")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listaPlato = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList <Servicio> getServicio(){
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicio (resultado.getInt("codigoServicio"),
                        resultado.getDate("fechaServicio"),
                        resultado.getString("tipoServicio"),
                        resultado.getString("horaServicio"),
                        resultado.getString("lugarServicio"),
                        resultado.getString("telefonoContacto"),
                        resultado.getInt("codigoEmpresa")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaServicio =  FXCollections.observableArrayList(lista);
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnReporte.setText("Cancelar");
                imgNuevo.setImage(new Image ("/org/juanboteo/image/save.png"));
                imgReporte.setImage(new Image ("/org/juanboteo/image/eliminar.png"));
                tblServiciosHasPlatos.getSelectionModel().clearSelection();
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnReporte.setText("Reporte");
                imgNuevo.setImage(new Image ("/org/juanboteo/image/foldergrey_93178.png"));
                imgReporte.setImage(new Image("/org/juanboteo/image/reporte.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void reporte(){
        switch (tipoDeOperacion) {
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnReporte.setText("Reporte");
                imgNuevo.setImage(new Image ("/org/juanboteo/image/foldergrey_93178.png"));
                imgReporte.setImage(new Image("/org/juanboteo/image/reporte.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void guardar(){
        ServiciosHasPlatos registro = new ServiciosHasPlatos();
        String text = txtCodigo.getText();
        text.replaceAll(" ", "");
        if(text.length() == 0||cmbCodigoPlato.getSelectionModel() == null || cmbCodigoServicio.getSelectionModel() == null){
            JOptionPane.showMessageDialog(null, "Faltan datos");
        }else {
            registro.setServicios_codigoServicio(Integer.parseInt(txtCodigo.getText()));
            registro.setCodigoPlato(((Plato)cmbCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
            registro.setCodigoServicio(((Servicio)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicio_has_Plato(?,?,?)");
                procedimiento.setInt(1, registro.getServicios_codigoServicio());
                procedimiento.setInt(2, registro.getCodigoPlato());
                procedimiento.setInt(3, registro.getCodigoServicio());
                procedimiento.execute();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public void desactivarControles(){
        txtCodigo.setEditable(false);
        cmbCodigoPlato.setDisable(true);
        cmbCodigoServicio.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigo.setEditable(true);
        cmbCodigoPlato.setDisable(false);
        cmbCodigoServicio.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigo.setText("");
        cmbCodigoPlato.setValue(null);
        cmbCodigoServicio.setValue(null);
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

