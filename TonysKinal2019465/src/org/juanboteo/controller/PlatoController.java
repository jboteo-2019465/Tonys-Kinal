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
import org.juanboteo.bean.TipoPlato;
import org.juanboteo.db.Conexion;
import org.juanboteo.main.Principal;

public class PlatoController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones{GUARDAR, ACTUALIZAR, NINGUNO, ELIMINAR}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Plato> listaPlato;
    private ObservableList<TipoPlato> listaTipoPlato;
    
    @FXML private TextField txtCodigoPlato;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtNombrePlato;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtPrecioPlato;
    @FXML private TableView tblPlatos;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableColumn colNombrePlato;
    @FXML private TableColumn colDescripcionPlato;
    @FXML private TableColumn colPrecio;
    @FXML private TableColumn colCantidad;
    @FXML private TableColumn colCodigoTipoPlato;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private ComboBox cmbCodigoTipoPlato;
    
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoTipoPlato.setItems(getTipoPlato());
        cmbCodigoTipoPlato.setDisable(true);

    }
    public void cargarDatos(){
        tblPlatos.setItems(getPlato());
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("codigoPlato"));
        colNombrePlato.setCellValueFactory(new PropertyValueFactory<Plato, String>("nombrePlato"));
        colDescripcionPlato.setCellValueFactory(new PropertyValueFactory<Plato, String>("descripcionPlato"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<Plato, Double>("precioPlato"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("cantidadPlato"));
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("codigoTipoPlato"));
    }
    
  
    public void seleccionarElemento(){
        if(tblPlatos.getSelectionModel().getSelectedItem() !=null){
            txtCodigoPlato.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
            txtCantidad.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCantidadPlato()));
            txtNombrePlato.setText(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getNombrePlato());
            txtDescripcion.setText(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getDescripcionPlato());
            txtPrecioPlato.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getPrecioPlato()));
            cmbCodigoTipoPlato.getSelectionModel().select(buscarTipoPlato(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
        }
    }
    
    public TipoPlato buscarTipoPlato(int codigoTipoPlato){
        TipoPlato resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarTipoPlato(?)");
            procedimiento.setInt(1, codigoTipoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TipoPlato (registro.getInt("codigoTipoPlato"),
                        registro.getString("descripcionTipo"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList<Plato>getPlato(){
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
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPlato = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<TipoPlato> getTipoPlato(){
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
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
        return listaTipoPlato = FXCollections.observableArrayList(lista);
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
                tblPlatos.getSelectionModel().clearSelection();
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/juanboteo/image/foldergrey_93178.png"));
                imgEliminar.setImage(new Image("/org/juanboteo/image/delete.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                tblPlatos.getSelectionModel().clearSelection();
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
                tblPlatos.getSelectionModel().clearSelection();
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
                tblPlatos.getSelectionModel().clearSelection();
            default:
                if(tblPlatos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Desea eliminar el registro?", "Eliminar Plato", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarPlato(?)");
                            procedimiento.setInt(1, ((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato());
                            listaPlato.remove(tblPlatos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            procedimiento.execute();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else if(respuesta == JOptionPane.NO_OPTION){
                        limpiarControles();
                        desactivarControles();
                        tblPlatos.getSelectionModel().clearSelection();
                    }
                }else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblPlatos.getSelectionModel().getSelectedItem() !=null){
                    activarControles();
                    btnNuevo.setDisable(true);
                    btnEliminar.setText("Cancelar");
                    btnEditar.setText("Actualizar");
                    btnReporte.setDisable(true);
                    imgEditar.setImage(new Image("/org/juanboteo/image/actu.png"));
                    imgEliminar.setImage(new Image("/org/juanboteo/image/eliminar.png"));  
                    cmbCodigoTipoPlato.setDisable(true);
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
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                tblPlatos.getSelectionModel().clearSelection();
                break;
        }
    }
    
    public void guardar(){
        Plato registro = new Plato();
            registro.setCantidadPlato(Integer.parseInt(txtCantidad.getText()));
            registro.setNombrePlato(txtNombrePlato.getText());
            registro.setDescripcionPlato(txtDescripcion.getText());
            registro.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText()));
            registro.setCodigoTipoPlato(((TipoPlato)cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarPlato(?,?,?,?,?)");
                procedimiento.setInt(1, registro.getCantidadPlato());
                procedimiento.setString(2, registro.getNombrePlato());
                procedimiento.setString(3, registro.getDescripcionPlato());
                procedimiento.setDouble(4, registro.getPrecioPlato());
                procedimiento.setInt(5, registro.getCodigoTipoPlato());
                procedimiento.execute();
                listaPlato.add(registro);
            }catch(Exception e){
                e.printStackTrace();
            }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarPlato(?,?,?,?)");
            Plato registro = (Plato)tblPlatos.getSelectionModel().getSelectedItem();
            registro.setCantidadPlato(Integer.parseInt(txtCantidad.getText()));
            registro.setNombrePlato(txtNombrePlato.getText());
            registro.setDescripcionPlato(txtDescripcion.getText());
            registro.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText()));
            procedimiento.setInt(1, registro.getCantidadPlato());
            procedimiento.setString(2, registro.getNombrePlato());
            procedimiento.setString(3, registro.getDescripcionPlato());
            procedimiento.setDouble(4, registro.getPrecioPlato());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void desactivarControles(){
        txtCodigoPlato.setEditable(false);
        txtCantidad.setEditable(false);
        txtNombrePlato.setEditable(false);
        txtDescripcion.setEditable(false);
        txtPrecioPlato.setEditable(false);
        cmbCodigoTipoPlato.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoPlato.setEditable(false);
        txtCantidad.setEditable(true);
        txtNombrePlato.setEditable(true);
        txtDescripcion.setEditable(true);
        txtPrecioPlato.setEditable(true);
        cmbCodigoTipoPlato.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoPlato.setText("");
        txtCantidad.setText("");
        txtNombrePlato.setText("");
        txtDescripcion.setText("");
        txtPrecioPlato.setText("");
        cmbCodigoTipoPlato.setValue(null);
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
    public void ventanaTipoPlato(){
        escenarioPrincipal.ventanaTipoPlato();
    }
    
}
