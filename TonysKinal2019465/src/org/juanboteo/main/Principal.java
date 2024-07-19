/*
 * Juan Carlos Boteo Granadods
    2019465
    IN5AM
    fecha de creacion: 28/03/2023
    fechas de modificaciones: 11/04/2023, 17/04/2023, 18/04/2023, 19/04/2023, 20/04/2023, 
        21/04/2023, 22/04/2023, 23/04/2023, 26/04/2023, 29/04/2023, 02/05/2023, 18/05/2023,
        22/05/2023, 26/05/2023, 27/05/2023, 28/05/2023, 30/05/2023, 31/05/2023, 02/06/2023, 05/06/2023, 06/06/2023
 */
package org.juanboteo.main;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.juanboteo.controller.EmpleadosController;
import org.juanboteo.controller.EmpresaController;
import org.juanboteo.controller.LoginController;
import org.juanboteo.controller.MenuPrincipalController;
import org.juanboteo.controller.PlatoController;
import org.juanboteo.controller.PresupuestoController;
import org.juanboteo.controller.ProductoController;
import org.juanboteo.controller.Productos_has_PlatosController;
import org.juanboteo.controller.ProgramadorController;
import org.juanboteo.controller.ServiciosController;
import org.juanboteo.controller.Servicios_has_EmpleadosController;
import org.juanboteo.controller.Servicios_has_PlatosController;
import org.juanboteo.controller.TipoEmpleadocontroller;
import org.juanboteo.controller.TipoPlatoController;
import org.juanboteo.controller.UsuarioController;


public class Principal extends Application {
    private final String PAQUETE_VISTA  = "/org/juanboteo/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
    
    @Override
    public void start(Stage escenarioPrincipal) throws IOException {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Tony's Kinal 2023");
        escenarioPrincipal.getIcons().add(new Image("/org/juanboteo/image/LogoTonysKinal2.png"));
        //Parent root = FXMLLoader.load(getClass().getResource("/org/juanboteo/view/MenuPrincipalView.fxml"));
        //Scene escena = new Scene(root);
        //escenarioPrincipal.setScene(escena);
        login();
        escenarioPrincipal.show();
        
    }
    
    public void login(){
         try{
            LoginController login = (LoginController)cambiarEscena("LoginView.fxml", 720, 573); 
            login.setEscenarioPrincipal(this);
            escenarioPrincipal.setResizable(false);
        }catch(Exception e){
            e.printStackTrace();
        
        }
        
    }
    
    public void ventanaUsuario(){
        try{
            UsuarioController usuario = (UsuarioController)cambiarEscena("UsuarioView.fxml", 739, 415); 
            usuario.setEscenarioPrincipal(this);
            escenarioPrincipal.setResizable(false);
        }catch(Exception e){
            e.printStackTrace();
        
        }
    }
    public void menuPrincipal(){
        try{
            MenuPrincipalController menu = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml", 458, 433); 
            menu.setEscenarioPrincipal(this);
            escenarioPrincipal.setResizable(false);
        }catch(Exception e){
            e.printStackTrace();
        
        }
    }
    
    public void ventanaProgramador(){
        try{
            ProgramadorController programador = (ProgramadorController)cambiarEscena("ProgrmadorView.fxml", 600, 313);
            programador.setEscenarioPrincipal(this);
            escenarioPrincipal.setResizable(false);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaEmpleados(){
        try{
            EmpleadosController empleado = (EmpleadosController)cambiarEscena("EmpleadosView.fxml", 1387, 546);
            empleado.setEscenarioPrincipal(this);
            escenarioPrincipal.setResizable(false);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaEmpresa(){
        try{
            EmpresaController empresa = (EmpresaController)cambiarEscena("EmpresaView.fxml", 1017, 546);
            empresa.setEscenarioPrincipal(this);
            escenarioPrincipal.setResizable(false);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaPlato(){
        try{
            PlatoController plato = (PlatoController)cambiarEscena("PlatoView.fxml", 1154, 546);
            plato.setEscenarioPrincipal(this);
            escenarioPrincipal.setResizable(false);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaPresupuesto(){
        try{
            PresupuestoController presupuesto = (PresupuestoController)cambiarEscena("PresupuestoView.fxml", 1017, 546);
            presupuesto.setEscenarioPrincipal(this);
            escenarioPrincipal.setResizable(false);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaProducto(){
        try{
            ProductoController producto = (ProductoController)cambiarEscena("ProductoView.fxml", 1017, 546);
            producto.setEscenarioPrincipal(this);
            escenarioPrincipal.setResizable(false);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaProductos_has_Platos(){
        try{
            Productos_has_PlatosController productos_has_platos = (Productos_has_PlatosController)cambiarEscena("Productos_has_PlatosView.fxml", 1017, 546);
            productos_has_platos.setEscenarioPrincipal(this);
            escenarioPrincipal.setResizable(false);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaServicios(){
        try{
            ServiciosController servicios = (ServiciosController)cambiarEscena("ServiciosView.fxml", 1158, 546);
            servicios.setEscenarioPrincipal(this);
            escenarioPrincipal.setResizable(false);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaServicios_has_Empleados(){
        try{
            Servicios_has_EmpleadosController servicios_has_empleados = (Servicios_has_EmpleadosController)cambiarEscena("Servicios_has_Empleados.fxml", 1017, 546);
            servicios_has_empleados.setEscenarioPrincipal(this);
            escenarioPrincipal.setResizable(false);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
        public void ventanaServiciosHasPlatos(){
        try{
            Servicios_has_PlatosController has = (Servicios_has_PlatosController) cambiarEscena ("Servicios_has_PlatosView.fxml", 1017, 546);
            has.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaTipoEmpleado(){
        try{
            TipoEmpleadocontroller tipoempleado = (TipoEmpleadocontroller)cambiarEscena("TipoEmpleadoView.fxml", 1017, 546);
            tipoempleado.setEscenarioPrincipal(this);
            escenarioPrincipal.setResizable(false);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaTipoPlato(){
        try{
            TipoPlatoController tipoplato = (TipoPlatoController)cambiarEscena("TipoPlatoView.fxml", 1017, 546);
            tipoplato.setEscenarioprincipal(this);
            escenarioPrincipal.setResizable(false);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    
    public static void main(String[] args) {
        launch(args);
    }
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        
        return resultado;
             
            
        }
    
}
