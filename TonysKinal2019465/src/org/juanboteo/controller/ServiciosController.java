package org.juanboteo.controller;

import com.jfoenix.controls.JFXTimePicker;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.juanboteo.bean.Empresa;
import org.juanboteo.bean.Servicio;
import org.juanboteo.db.Conexion;
import org.juanboteo.main.Principal;

public class ServiciosController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{GUARDAR, ACTUALIZAR, NINGUNO, ELIMINAR}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Servicio>  listaServicio;
    private ObservableList<Empresa> listaEmpresa;
    private DatePicker fecha;
    
    
    @FXML private TextField txtCodigoServicio;
    @FXML private TextField txtLugarServicio;
    @FXML private TextField txtTipoServicio;
    @FXML private TextField txtTelefono;
    @FXML private JFXTimePicker jfxHora;
    @FXML private GridPane grpFecha;
    @FXML private TableView tblServicios;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colFechaServicio;
    @FXML private TableColumn colHoraServicio;
    @FXML private TableColumn colLugar;
    @FXML private TableColumn colTipo;
    @FXML private TableColumn colTelefono;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private ComboBox cmbCodigoEmpresa;
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
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/juanboteo/resources/TonysKinal.css");
        grpFecha.add(fecha, 1, 1);
        cmbCodigoEmpresa.setItems(getEmpresa());
        fecha.setDisable(true);
        cmbCodigoEmpresa.setDisable(true);
        jfxHora.setDisable(true);
    }
    
    public void cargarDatos(){
        tblServicios.setItems(getServicio());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoServicio"));
        colFechaServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Date>("fechaServicio"));
        colHoraServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("horaServicio"));
        colLugar.setCellValueFactory(new PropertyValueFactory<Servicio, String>("lugarServicio"));
        colTipo.setCellValueFactory(new PropertyValueFactory<Servicio, String>("tipoServicio"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Servicio, String>("telefonoContacto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoEmpresa"));
        
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
                imgNuevo.setImage(new Image("/org/juanboteo/image/save.png"));
                imgEliminar.setImage(new Image("/org/juanboteo/image/eliminar.png"));
                tblServicios.getSelectionModel().clearSelection();
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/juanboteo/image/foldergrey_93178.png"));
                imgEliminar.setImage(new Image("/org/juanboteo/image/delete.png"));
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblServicios.getSelectionModel().getSelectedItem() != null){
                activarControles();
                btnNuevo.setDisable(true);
                btnEliminar.setText("Cancelar");
                btnEditar.setText("Actualizar");
                btnReporte.setDisable(true);
                cmbCodigoEmpresa.setDisable(true);
                imgEditar.setImage(new Image("/org/juanboteo/image/actu.png"));
                imgEliminar.setImage(new Image("/org/juanboteo/image/eliminar.png"));
                tipoDeOperacion = operaciones.ACTUALIZAR;
                
            }else{
                JOptionPane.showMessageDialog(null, "Seleccione un dato");
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
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
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
                imgNuevo.setImage(new Image("/org/juanboteo/image/foldergrey_93178.png"));
                imgEliminar.setImage(new Image("/org/juanboteo/image/delete.png"));
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
                cmbCodigoEmpresa.setDisable(true);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
                
            default:
                if(tblServicios.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Está seguro que quiere eliminar el registro?", "Eliminar Servicio", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarServicio(?)");
                            procedimiento.setInt(1, ((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio());
                            procedimiento.execute();
                            listaServicio.remove(tblServicios.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblServicios.getSelectionModel().clearSelection();
                            procedimiento.execute();
                                    
                        
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        limpiarControles();
                        tblServicios.getSelectionModel().clearSelection();
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Seleccione un dato");
                }
        }
    }
    
    public void guardar(){
            Servicio registro = new Servicio();
            registro.setFechaServicio(fecha.getSelectedDate());
            registro.setHoraServicio(String.valueOf(jfxHora.getValue()));
            registro.setLugarServicio(txtLugarServicio.getText());
            registro.setTelefonoContacto(txtTelefono.getText());
            registro.setTipoServicio(txtTipoServicio.getText());
            registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
                try{
                    PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicios(?,?,?,?,?,?)");
                    procedimiento.setDate(1, new java.sql.Date(registro.getFechaServicio().getTime()));
                    procedimiento.setString(3, registro.getHoraServicio());
                    procedimiento.setString(4, registro.getLugarServicio());
                    procedimiento.setString(5, registro.getTelefonoContacto());
                    procedimiento.setString(2, registro.getTipoServicio());
                    procedimiento.setInt(6, registro.getCodigoEmpresa());
                    procedimiento.execute();
                    listaServicio.add(registro);
                }catch(Exception e){
                    e.printStackTrace();
            }
       
        }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarServicio(?,?,?,?,?)");
            Servicio registro = (Servicio)tblServicios.getSelectionModel().getSelectedItem();
            registro.setHoraServicio(String.valueOf(jfxHora.getValue()));
            registro.setLugarServicio(txtLugarServicio.getText());
            registro.setTelefonoContacto(txtTelefono.getText());
            registro.setTipoServicio(txtTipoServicio.getText());
            registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
            
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaServicio().getTime()));
            procedimiento.setString(3, registro.getHoraServicio());
            procedimiento.setString(4, registro.getLugarServicio());
            procedimiento.setString(5, registro.getTelefonoContacto());
            procedimiento.setString(2, registro.getTipoServicio());
            procedimiento.execute();
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public ObservableList<Servicio> getServicio(){
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicio(resultado.getInt("codigoServicio"),
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
        return listaServicio = FXCollections.observableArrayList(lista);
    }
    
    
    
    
    
    
    public void seleccionarElemento(){
        if(tblServicios.getSelectionModel().getSelectedItem()!= null){
            txtCodigoServicio.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio()));
            txtLugarServicio.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getLugarServicio()));
            txtTipoServicio.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTipoServicio()));
            txtTelefono.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTelefonoContacto()));
            String cadena = String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getHoraServicio()) ;
            jfxHora.setValue(LocalTime.parse(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getHoraServicio()));
            fecha.selectedDateProperty().set(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getFechaServicio());
            cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Servicio)(tblServicios.getSelectionModel().getSelectedItem())).getCodigoEmpresa()));
            
        }
    }
    
    
    public void activarControles(){
        txtCodigoServicio.setEditable(false);
        txtLugarServicio.setEditable(true);
        txtTipoServicio.setEditable(true);
        txtTelefono.setEditable(true);
        jfxHora.setDisable(false);
        fecha.setDisable(false);
        cmbCodigoEmpresa.setDisable(false);
    }
    public void desactivarControles(){
        txtCodigoServicio.setEditable(false);
        txtLugarServicio.setEditable(false);
        txtTipoServicio.setEditable(false);
        txtTelefono.setEditable(false);
        jfxHora.setDisable(true);
        fecha.setDisable(true);
        cmbCodigoEmpresa.setDisable(true);
    }
    public void limpiarControles(){
        txtCodigoServicio.setText("");
        txtLugarServicio.setText("");
        txtTipoServicio.setText("");
        txtTelefono.setText("");
        jfxHora.setValue(null);
        fecha.setSelectedDate(null);
        cmbCodigoEmpresa.setValue(null);
    }
    
    
    
    public Empresa buscarEmpresa(int codigoEmpresa){
        Empresa resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpresa(?)");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empresa(registro.getInt("codigoEmpresa"),
                    registro.getString("nombreEmpresa"),
                    registro.getString("direccion"),
                    registro.getString("telefono"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
        
    }
    public ObservableList<Empresa> getEmpresa(){
            ArrayList<Empresa> lista = new ArrayList<Empresa>();
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpresas");
                ResultSet resultado = procedimiento.executeQuery();
                while(resultado.next()){
                    lista.add(new Empresa(resultado.getInt("codigoEmpresa"),
                            resultado.getString("nombreEmpresa"),
                            resultado.getString("direccion"),
                            resultado.getString("telefono")));
                }
            
            }catch(Exception e){
                e.printStackTrace();
            }
            
            return listaEmpresa = FXCollections.observableArrayList(lista); 
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
    public void ventanaEmpresa(){
        escenarioPrincipal.ventanaEmpresa();
    }
    
}
