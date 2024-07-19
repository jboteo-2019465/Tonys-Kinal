package org.juanboteo.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.juanboteo.bean.Usuario;
import org.juanboteo.db.Conexion;
import org.juanboteo.main.Principal;

public class UsuarioController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{GUARDAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    @FXML private TextField txtCodigoUsuario;
    @FXML private TextField txtNombreUsuario;
    @FXML private TextField txtApellidoUsuario;
    @FXML private TextField txtUsuario;
    @FXML private TextField txtPassword;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                imgNuevo.setImage(new Image("/org/juanboteo/image/save.png"));
                imgEliminar.setImage(new Image("/org/juanboteo/image/eliminar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                imgNuevo.setImage(new Image("/org/juanboteo/image/foldergrey_93178.png"));
                imgEliminar.setImage(new Image("/org/juanboteo/image/delete.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                login();
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
                imgNuevo.setImage(new Image("/org/juanboteo/image/foldergrey_93178.png"));
                imgEliminar.setImage(new Image("/org/juanboteo/image/delete.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
                
        }
    }
    
    public void guardar(){
        String text = txtNombreUsuario.getText();
        String text2 = txtApellidoUsuario.getText();
        String text3 = txtUsuario.getText();
        String text4 = txtPassword.getText();
        text = text.replaceAll(" ", "");
        text2 = text2.replaceAll(" ", "");
        text3 = text3.replaceAll(" ", "");
        text4 = text4.replaceAll(" ", "");
        if(text.length() == 0 || text2.length() == 0 || text3.length() == 0 || text4.length() == 0){
            JOptionPane.showMessageDialog(null, "Faltan datos por rellenar");
       
        }else{  
             Usuario registro = new Usuario();
            registro.setNombreUsuario(txtNombreUsuario.getText());
            registro.setApellidoUsuario(txtApellidoUsuario.getText());
            registro.setUsuarioLogin(txtUsuario.getText());
            registro.setContrasena(txtPassword.getText());
                try{
                    PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarUsuario(?,?,?,?)");
                    procedimiento.setString(1, registro.getNombreUsuario());
                    procedimiento.setString(2, registro.getApellidoUsuario());
                    procedimiento.setString(3, registro.getUsuarioLogin());
                    procedimiento.setString(4, registro.getContrasena());
                    procedimiento.execute();
   
                }catch(Exception e){
                    e.printStackTrace();
            }
            }
    }
    
    
    public void limpiarControles(){
        txtCodigoUsuario.setText("");
        txtUsuario.setText("");
        txtNombreUsuario.setText("");
        txtApellidoUsuario.setText("");
        txtPassword.setText("");
    }
    public void desactivarControles(){
        txtCodigoUsuario.setEditable(false);
        txtUsuario.setEditable(false);
        txtNombreUsuario.setEditable(false);
        txtApellidoUsuario.setEditable(false);
        txtPassword.setEditable(false);
    }
    public void activarControles(){
        txtCodigoUsuario.setEditable(false);
        txtUsuario.setEditable(true);
        txtNombreUsuario.setEditable(true);
        txtApellidoUsuario.setEditable(true);
        txtPassword.setEditable(true);
    }
    
        public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void login(){
        escenarioPrincipal.login();
    }
    
}
