package org.juanboteo.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.juanboteo.main.Principal;


public class MenuPrincipalController implements Initializable {
    private Principal escenarioPrincipal;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
 
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void ventanaProgramador(){
        escenarioPrincipal.ventanaProgramador();
        
    }
    public void ventanaEmpresa(){
        escenarioPrincipal.ventanaEmpresa();
    }
    public void ventanaEmpleados(){
        escenarioPrincipal.ventanaEmpleados();
    }
    public void ventanaPlato(){
        escenarioPrincipal.ventanaPlato();
    }
    public void ventanaPresupuesto(){
        escenarioPrincipal.ventanaPresupuesto();
    }
    public void ventanaProducto(){
        escenarioPrincipal.ventanaProducto();
    }
    public void ventanaProductos_has_Platos(){
        escenarioPrincipal.ventanaProductos_has_Platos();
    }
    public void ventanaServicios(){
        escenarioPrincipal.ventanaServicios();
    }
    public void ventanaServicios_has_Empleados(){
        escenarioPrincipal.ventanaServicios_has_Empleados();
    }
    public void ventanaServiciosHasPlatos(){
        escenarioPrincipal.ventanaServiciosHasPlatos();
    }
    public void ventanaTipoEmpleado(){
        escenarioPrincipal.ventanaTipoEmpleado();
    }
    public void ventanaTipoPlato(){
        escenarioPrincipal.ventanaTipoPlato();
    }
    public void login(){
        escenarioPrincipal.login();
    }
    public void ventanaUsuario(){
        escenarioPrincipal.ventanaUsuario();
    }
    
}
