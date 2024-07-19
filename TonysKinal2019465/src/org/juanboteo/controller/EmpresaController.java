package org.juanboteo.controller;


import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import org.juanboteo.bean.Empresa;
import org.juanboteo.db.Conexion;
import org.juanboteo.main.Principal;
import org.juanboteo.report.GenerarReporte;

public class EmpresaController  implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Empresa> listaEmpresa;
    
        @FXML private TextField txtCodigoEmpresa;
        @FXML private TextField txtNombreEmpresa;
        @FXML private TextField txtDireccionEmpresa;
        @FXML private TextField txtTelefonoEmpresa;
        @FXML private TableView tblEmpresas;
        @FXML private TableColumn colCodigoEmpresa;
        @FXML private TableColumn colNombreEmpresa;
        @FXML private TableColumn colDireccionEmpresa;
        @FXML private TableColumn colTelefonoEmpresa;
        @FXML private Button btnNuevo;
        @FXML private Button btnEliminar;
        @FXML private Button btnEditar;
        @FXML private Button btnReporte;
        @FXML private ImageView imgNuevo;
        @FXML private ImageView imgEliminar;
        @FXML private ImageView imgEditar;
        @FXML private ImageView imgReporte;
        @FXML private ImageView imgX;
        @FXML private ImageView imgX2;
        @FXML private ImageView imgX3;
        
        
        
        @Override
        public void initialize(URL location, ResourceBundle resources) {
              cargarDatos();
          }
        
        public void cargarDatos(){
            tblEmpresas.setItems(getEmpresa());
            colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, Integer>("codigoEmpresa"));
            colNombreEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("nombreEmpresa"));
            colDireccionEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("direccion"));
            colTelefonoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("telefono"));
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
                    tblEmpresas.getSelectionModel().clearSelection();
                    tipoDeOperacion = operaciones.GUARDAR;
                    break;
                case GUARDAR:
                   String text = txtNombreEmpresa.getText();
                   String text2 = txtDireccionEmpresa.getText();
                   String text3 = txtTelefonoEmpresa.getText();
                   text = text.replaceAll(" ", "");
                   text2 = text2.replaceAll(" ", "");
                   text3 = text3.replaceAll(" ", "");
                   if(text.length() == 0 || text2.length() == 0 || text3.length() == 0){
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
                        tipoDeOperacion = operaciones.NINGUNO;
                        cargarDatos();
                        break;
                   }
                
                    
            }
        }
        
        public void editar(){
            switch(tipoDeOperacion){
                case NINGUNO:
                    if(tblEmpresas.getSelectionModel().getSelectedItem() != null){
                    btnNuevo.setDisable(true);
                    btnReporte.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnEliminar.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/juanboteo/image/actu.png"));
                    imgEliminar.setImage(new Image("/org/juanboteo/image/eliminar.png"));
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    activarControles();
                    break;    
            }else{
                    JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento"); 
            }
                    break;
                case ACTUALIZAR:
                    String text = txtNombreEmpresa.getText();
                    String text2 = txtDireccionEmpresa.getText();
                    String text3 = txtTelefonoEmpresa.getText();
                    text = text.replaceAll(" ", "");
                    text2 = text2.replaceAll(" ","");
                    text3 = text3.replaceAll(" ", "");
                    if(text.length() == 0 || text2.length() == 0 || text3.length() == 0){
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
                        tipoDeOperacion = operaciones.NINGUNO;
                        cargarDatos();
                        break;
                    }
                    
        }
        }
        public void actualizar(){
            if(txtTelefonoEmpresa.getText().length() > 8){
                    JOptionPane.showMessageDialog(null, "El teléfono tiene que tener 8 dígitos");
                }else{
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarEmpresa(?, ?, ?, ?)");
                Empresa registro = (Empresa)tblEmpresas.getSelectionModel().getSelectedItem();
                registro.setNombreEmpresa(txtNombreEmpresa.getText());
                registro.setDireccion(txtDireccionEmpresa.getText());
                registro.setTelefono(txtTelefonoEmpresa.getText());
                
                procedimiento.setInt(1, registro.getCodigoEmpresa());
                procedimiento.setString(2, registro.getNombreEmpresa());
                procedimiento.setString(3, registro.getDireccion());
                procedimiento.setString(4, registro.getTelefono());
                procedimiento.execute();
            }catch(Exception e){
                e.printStackTrace();
            
            }
            
        }
        }

        public void seleccionarElemento(){
            if(tblEmpresas.getSelectionModel().getSelectedItem() != null){
            txtCodigoEmpresa.setText(String.valueOf(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
            txtNombreEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getNombreEmpresa());
            txtDireccionEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getDireccion());
            txtTelefonoEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getTelefono());
            }else{
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
                    imgX.setVisible(false);
                    imgX2.setVisible(false);
                    imgX3.setVisible(false);
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
                    tipoDeOperacion = operaciones.NINGUNO;
                    tblEmpresas.getSelectionModel().clearSelection();
                    imgX.setVisible(false);
                    imgX2.setVisible(false);
                    imgX3.setVisible(false);
                    break;
                default:
                        if(tblEmpresas.getSelectionModel().getSelectedItem() !=null){
                            
                            int respuesta = JOptionPane.showConfirmDialog(null, "Está seguro de eliminar el registro?", "Eliminar Empresa", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                            
                            if(respuesta == JOptionPane.YES_OPTION){
                                try{
                                    PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarEmpresa(?)");
                                    procedimiento.setInt(1, ((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
                                    procedimiento.execute();
                                    listaEmpresa.remove(tblEmpresas.getSelectionModel().getSelectedIndex());
                                    limpiarControles();
                                    tblEmpresas.getSelectionModel().clearSelection();
                                    procedimiento.execute();
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                                
                            }else{
                                limpiarControles();
                                tblEmpresas.getSelectionModel().clearSelection();
                                
                            }
                            
                        
                            
                        }else{
                            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                        }
                
            }
        }
        public void guardar(){       
            Empresa registro = new Empresa();
            //registro.setCodigoEmpresa(Integer.parseInt(txtCodigoEmpresa.getText()));
            registro.setNombreEmpresa(txtNombreEmpresa.getText());
            registro.setDireccion(txtDireccionEmpresa.getText());
            registro.setTelefono(txtTelefonoEmpresa.getText());
            if(txtTelefonoEmpresa.getText().length() > 8){
                    JOptionPane.showMessageDialog(null, "El teléfono tiene que tener 8 dígitos ");
                }else{
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarEmpresa(?, ?, ?);");
                procedimiento.setString(1, registro.getNombreEmpresa());
                procedimiento.setString(2, registro.getDireccion());
                procedimiento.setString(3, registro.getTelefono());
                
                procedimiento.execute();
                listaEmpresa.add(registro);
            }catch(Exception e){
                e.printStackTrace();
            }
 
        }
        }
        
        public void generarReporte(){
            switch(tipoDeOperacion){
                case NINGUNO:
                    imprimirReporte();
                    break;
            }
        }
        
        public void imprimirReporte(){
            Map parametros = new HashMap();
            parametros.put("codigoEmpresa", null);
            parametros.put("imagen", GenerarReporte.class.getResourceAsStream("/org/juanboteo/image/a.png"));
            parametros.put("imagen2", GenerarReporte.class.getResourceAsStream("/org/juanboteo/image/report.png"));
            GenerarReporte.mostrarReporte("ReporteEmpresas.jasper", "Reporte de Empresas", parametros);
        }

        
        public void desactivarControles(){
            txtCodigoEmpresa.setEditable(false);
            txtNombreEmpresa.setEditable(false);
            txtDireccionEmpresa.setEditable(false);
            txtTelefonoEmpresa.setEditable(false);
        }
        public void activarControles(){
            txtCodigoEmpresa.setEditable(false);
            txtNombreEmpresa.setEditable(true);
            txtDireccionEmpresa.setEditable(true);
            txtTelefonoEmpresa.setEditable(true);           
        }
        public void limpiarControles(){
            txtCodigoEmpresa.setText("");
            txtNombreEmpresa.setText("");
            txtDireccionEmpresa.setText("");
            txtTelefonoEmpresa.setText("");
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
    public void ventanaPresupuesto(){
        escenarioPrincipal.ventanaPresupuesto();
        
    }
    public void ventanaServicio(){
        escenarioPrincipal.ventanaServicios();
    }
    

}
