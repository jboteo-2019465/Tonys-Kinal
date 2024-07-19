/*
	Juan Carlos Boteo Granados
    2019465
    IN5AM
    Fecha de creación: 28/03/2023
    Fechas de modificaciónes: 28/06/2023, 03/06/2023
    

*/

Drop database if exists DBTonysKinal2023;
create database DBTonysKinal2023;
use DBTonysKinal2023;

Create table Empresas(
	codigoEmpresa int auto_increment not null,
    nombreEmpresa varchar(150) not null,
    direccion varchar(150) not null, 
    telefono varchar(8),
    primary key PK_codigoEmpresa(codigoEmpresa)
);
create table TipoEmpleado(
	codigoTipoEmpleado int not null auto_increment,
    descripcion varchar(50) not null,
    primary key PK_codigoTipoEmpleado (codigoTipoEmpleado)
);
create table TipoPlato(
	codigoTipoPlato int not null auto_increment,
    descripcionTipo varchar(100) not null,
    primary key PK_codigoTipoPlato (codigoTipoPlato)
);

create table Productos(
	codigoProducto int not null auto_increment,
    nombreProducto varchar(150) not null,
    cantidad int not null,
    primary key PK_codigoProducto (codigoProducto)
);

create table Empleados(
	codigoEmpleado int not null auto_increment,
    numeroEmpleado int not null,
    apellidosEmpleado varchar(150) not null,
    nombresEmpleado varchar(150) not null,
    direccionEmpleado varchar(150) not null,
    telefonoContacto varchar(8) not null,
    gradoCocinero varchar(50),
    codigoTipoEmpleado int not null,
    primary key PK_codigoEmpleado (codigoEmpleado),
    constraint FK_Empleados_TipoEmpleado foreign key
		(codigoTipoEmpleado) references TipoEmpleado(codigoTipoEmpleado)
);

create table Servicios(
	codigoServicio int not null auto_increment,
    fechaServicio date not null,
    tipoServicio varchar(150) not null,
    horaServicio time not null,
    lugarServicio varchar(150) not null,
    telefonoContacto varchar(150) not null,
    codigoEmpresa int not null,
    primary key PK_codigoServicio(codigoServicio),
    constraint FK_Servicios_Empresas foreign key (codigoEmpresa)
		references Empresas(codigoEmpresa)
);

create table Presupuestos(
	codigoPresupuesto int not null auto_increment,
    fechaSolicitud date not null,
    cantidadPresupuesto decimal(10,2) not null,
    codigoEmpresa int not null,
    primary key PK_codigoPresupuesto (codigoPresupuesto),
    constraint FK_Presupuestos_Empresas foreign key (codigoEmpresa)
		references Empresas(codigoEmpresa)
    
);

create table Platos(
	codigoPlato int not null auto_increment,
    cantidadPlato int not null,
    nombrePlato varchar(150) not null,
    descripcionPlato varchar(150) not null,
    precioPlato decimal(10,2) not null,
    codigoTipoPlato int not null,
    primary key codigoPlato (codigoPlato),
    constraint FK_Platos_TipoPlato foreign key (codigoTipoPlato)
		references TipoPlato(codigoTipoPlato)
);

create table Productos_has_Platos(
	Productos_codigoProducto int not null,
    codigoPlato int not null,
    codigoProducto int not null,
    primary key PK_Productos_codigoProducto (Productos_codigoProducto),
    constraint FK_Productos_has_Platos_Productos foreign key (codigoProducto)
		references Productos(codigoProducto),
	constraint FK_Productos_has_Platos_Platos foreign key(codigoPlato)
		references Platos(codigoPlato)
);

Create Table Servicios_has_Platos(
	Servicios_codigoServicio int not null,
    codigoPlato int not null,
    codigoServicio int not null,
    primary key PK_Servicios_codigoServicio (Servicios_codigoServicio),
    constraint FK_Servicios_has_Servicios foreign key (codigoServicio)
		references Servicios(codigoServicio),
	constraint FK_Servicios_has_Platos_Platos foreign key (codigoPlato)
		references Platos(codigoPlato)
);

create table Servicios_has_Empleados(
	Servicios_codigoServicio int not null,
    codigoServicio int not null,
    codigoEmpleado int not null,
    fechaEvento date not null,
    horaEvento time not null,
    lugarEvento varchar(150) not null,
    primary key PK_Servicios_codigoServicio (Servicios_codigoServicio),
    constraint FK_Servicios_has_Empleados_Servicios foreign key (codigoServicio)
		references Servicios(codigoServicio),
	constraint FK_Servicios_has_Empleados_Empleados foreign key (codigoEmpleado)
		references Empleados(codigoEmpleado)
);

create table usuario(
	codigoUsuario int not null auto_increment,
    nombreUsuario varchar(100) not null,
    apellidoUsuario varchar(100) not null,
    usuarioLogin varchar(50) not null,
    contrasena varchar(50) not null,
    primary key PK_codigoUsuario (codigoUsuario)
);

Delimiter //
	create procedure sp_AgregarUsuario(in nombreUsuario varchar(50), in apellidoUsuario varchar(50), 
		in usuarioLogin varchar(50), contrasena varchar(50))
    begin
		Insert into Usuario(nombreUsuario, apellidoUsuario, usuarioLogin, contrasena)
			values(nombreUsuario, apellidoUsuario, usuarioLogin, contrasena);
    end //
Delimiter ;
call sp_AgregarUsuario("Juan", "Boteo", "jboteo", "12345");
call sp_AgregarUsuario("Maynor", "Castillo", "Mcastillo", "12345");
call sp_AgregarUsuario("Maria", "Granados", "Mgranados", "12345");
call sp_AgregarUsuario("Saul", "Gracia", "Sgarcia", "12345");
call sp_AgregarUsuario("Pedro", "Jimenez", "Pjimenez", "12345");
call sp_AgregarUsuario("Pedro", "Armas", "parmas", "parmas");

Delimiter //
	create procedure sp_ListarUsuarios()
    begin
		select U.codigoUsuario, U.nombreUsuario, U.apellidoUsuario, U.usuarioLogin, U.contrasena
			from Usuario U;
    end //
Delimiter ;
call sp_ListarUsuarios();

create table Login(
	usuarioMaster varchar(50) not null,
    passwordLogin varchar(50) not null,
    primary Key PK_usuarioMaster (usuarioMaster)
);


-- --------------------- Procedimientoss Almacenados -----------------------------
-- --------------------- Agregar Empresa -----------------------
Delimiter //
create procedure sp_AgregarEmpresa(in nombreEmpresa varchar(150), 
	in direccion varchar(150), in telefono varchar(8))
    begin
		insert into Empresas(nombreEmpresa, direccion, telefono)
			values(nombreEmpresa, direccion, telefono);
    end //
Delimiter ;
call sp_AgregarEmpresa('Pizza Hut', '12 ave 12 34', '34576523');
call sp_AgregarEmpresa('Club Aurora', '12 ave 12 34', '78654534');
call sp_AgregarEmpresa('Ferreteria la Bendicion', 'zona 1 cc Aurora', '78652234');
call sp_AgregarEmpresa('Pizza Hat', '12 ave 12 34', '34576523');
call sp_AgregarEmpresa('DHL', '12 Calle 5-12', '23398428');

-- ----------------- Editar Empresa -----------------------------
Delimiter //
create procedure sp_EditarEmpresa(in codEmpresa int, in nomEmpresa varchar(150), in direc varchar(150), in tel varchar(8))
	begin
		update Empresas set 
        nombreEmpresa = nomEmpresa, 
        direccion = direc, 
        telefono = tel
			where codigoEmpresa = codEmpresa;
    end //
Delimiter ;
call sp_EditarEmpresa(1, 'Kinal', 'Zona 3 12-89', '23456789');
-- ------------------ Eliminar Empresa -----------------------------
Delimiter //
create procedure sp_EliminarEmpresa(in codEmpresa int)
	begin
		delete from Empresas where codigoEmpresa = codEmpresa;
    end //
Delimiter ;
call sp_EliminarEmpresa(6);
-- ------------- Listar Empresas ---------------------------------
Delimiter //
create procedure sp_ListarEmpresas()
	begin
		select 
        codigoEmpresa, 
        nombreEmpresa, 
        direccion, 
        telefono 
        from Empresas;
    end //
Delimiter ;
call sp_ListarEmpresas();
-- ------------------- Buscar Empresas ---------------------------
Delimiter //
create procedure sp_BuscarEmpresa(in codEmpresa int)
	begin
    select 
        codigoEmpresa, 
        nombreEmpresa, 
        direccion, 
        telefono 
        from Empresas where codigoEmpresa = codEmpresa;
    end //
Delimiter ;
call sp_BuscarEmpresa(1);

-- --------------- Agregar Tipo de Empleado ---------------------------
Delimiter //
create procedure sp_AgregarTipoEmpleado(in descripcion varchar(50))
	begin
		insert into TipoEmpleado(descripcion)
			values (descripcion);
    end //
Delimiter ;
call sp_AgregarTipoEmpleado('Cocinero');
call sp_AgregarTipoEmpleado('Cocinero');
call sp_AgregarTipoEmpleado('Mesero');
call sp_AgregarTipoEmpleado('Contador');
call sp_AgregarTipoEmpleado('Publicista');
-- -------------- Editar Tipo de Empleado ------------------------------
Delimiter //
create procedure sp_EditarTipoEmpleado(in codTipoEmpleado int, in descrip varchar(50))
	begin
		update TipoEmpleado set descripcion = descrip
			where codigoTipoEmpleado = codTipoEmpleado;
    end //
Delimiter ;
call sp_EditarTipoEmpleado(1, 'Conductor');
-- ------------- Listar Tipos de Empleados --------------------------
Delimiter //
create procedure sp_ListarTiposEmpleados()
	begin
		select 
			codigoTipoEmpleado,
            descripcion
            from TipoEmpleado;
    end //
Delimiter ;
call sp_ListarTiposEmpleados();
-- ------------ Buscar Tipo de Empleado ------------------------------
Delimiter //
create procedure sp_BuscarTipoEmpleado(in codTipoEmpleado int)
	begin
		select
			codigoTipoEmpleado,
            descripcion
            from TipoEmpleado where codigoTipoEmpleado = codTipoEmpleado;
    end //
Delimiter ;
call sp_BuscarTipoEmpleado(1);
-- ----------- Eliminar Tipo de Empleado ---------------------------
Delimiter //
create procedure sp_EliminarTipoEmpleado(in codTipoEmpleado int)
	begin
		delete from TipoEmpleado where codigoTipoEmpleado = codTipoEmpleado;
    end //
Delimiter ;

-- ------------- Agregar Tipo de Plato ----------------------------
Delimiter //
create procedure sp_AgregarTipoPlato(in descripcionTipo varchar(100))
	begin
		insert into TipoPlato(descripcionTipo)
			values (descripcionTipo);
    end //
Delimiter ;
call sp_AgregarTipoPlato('Ramen');
call sp_AgregarTipoPlato('Carne Asada');
call sp_AgregarTipoPlato('Pizza');
call sp_AgregarTipoPlato('Ensalada');
call sp_AgregarTipoPlato('Estofado');
-- ------------- Editar Tipo de Plato ------------------------------ 
Delimiter //
create procedure sp_EditarTipoPlato(in codTipoPlato int, in descrip varchar(50))
	begin
		update TipoPlato set descripcionTipo = descrip
			where codigoTipoPlato = codTipoPlato;
    end //
Delimiter ;
call sp_EditarTipoPlato(1, 'Espaggueti');
-- ----------- Listar Tipos de Platos ---------------------------
Delimiter //
create procedure sp_ListarTiposPlatos()
	begin
		select 
			codigoTipoPlato,
            descripcionTipo
            from TipoPlato;
    end //
Delimiter ;
call sp_ListarTiposPlatos();
-- ------------- Buscar Tipo de Plato ------------------------------
Delimiter //
create procedure sp_BuscarTipoPlato(in codTipoPlato int)
	begin
		select 
			codigoTipoPlato,
            descripcionTipo
            from TipoPlato where codigoTipoPlato = codTipoPlato;
    end //
Delimiter ;
call sp_BuscarTipoPlato(1);
-- ------------ Eliminar Tipo de Plato ---------------------------
Delimiter //
create procedure sp_EliminarTipoPlato(in codTipoPlato int)
	begin
		delete from TipoPlato where codigoTipoPlato = codTipoPlato;
    end //
Delimiter ;

-- ---------- Agregar Producto ---------------------------
Delimiter //
create procedure sp_AgregarProducto(in nombreProducto varchar(150), in cantidad int)
	begin
		insert into Productos (nombreProducto, cantidad)
			values (nombreProducto, cantidad);
    end //
Delimiter ;
call sp_AgregarProducto('Pasta', 12);
call sp_AgregarProducto('Pasta', 20);
call sp_AgregarProducto('Salsa de Tomate', 50);
call sp_AgregarProducto('Lechuga', 15);
call sp_AgregarProducto('Pollo Completo', 10);

-- ----------- Editar Producto -----------------------
Delimiter //
create procedure sp_EditarProductos(in codProducto int, in nomProducto varchar(150), in cant int)
	begin
		update Productos set codigoProducto = codProducto, nombreProducto = nomProducto, cantidad = cant
			where codigoProducto = codProducto;
    end //
Delimiter ;
call sp_EditarProductos(2, "Carne de res", 16);
-- ---------- Listar Productos -----------------------
Delimiter //
create procedure sp_ListarProductos()
	begin
		select 
			codigoProducto,
            nombreProducto,
            cantidad
            from Productos;
    end //
Delimiter ;
call sp_ListarProductos();
-- ------------------- Buscar Producto -----------------------
Delimiter //
create procedure sp_BuscarProducto(in codProducto int)
	begin
		select 
			codigoProducto,
            nombreProducto,
            cantidad
            from Productos where codigoProducto = codProducto;
    end //
Delimiter ;
call sp_BuscarProducto(1);
-- ------------------ Eliminar Producto --------------------
Delimiter //
create procedure sp_EliminarProducto(in codProducto int)
	begin
		delete from Productos where codigoProducto = codProducto;
    end //
Delimiter ;

-- ----------------- Agregar Empleado ---------------------
Delimiter //
create procedure sp_AgregarEmpleado(in numeroEmpleado int, in apellidosEmpleado varchar(150), 
	in nombresEmpleado varchar(150), in direccionEmpleado varchar(150), in telefonoContacto varchar(8), 
    in gradoCocinero varchar(50), in codigoTipoEmpleado int)
    begin
		insert into Empleados(numeroEmpleado, apellidosEmpleado, nombresEmpleado, direccionEmpleado, 
			telefonoContacto, gradoCocinero, codigoTipoEmpleado)
            values(numeroEmpleado, apellidosEmpleado, nombresEmpleado, direccionEmpleado, telefonoContacto, 
            gradoCocinero, codigoTipoEmpleado);
    end //
Delimiter ;
call sp_AgregarEmpleado(1, 'Arevalo Garcia', 'Pablo Escobar', 'zona 9', '08764537', '1', '1');
call sp_AgregarEmpleado(2, 'Morales Garcia', 'Carlos Alberto', 'Zona 7', '98765432', '2', '2');
call sp_AgregarEmpleado(3, 'Garcia Jimenez', 'Juan Carlos', 'zona 3 el gallito', '23568965', '3', '3');
call sp_AgregarEmpleado(4, 'Arevalo Garcia', 'Pablo Escobar', 'zona 14', '76543210', '4', '4');
call sp_AgregarEmpleado(5, 'Boteo Granados', 'Dulce Maria', 'zona 22', '56473829', '5', '5');
-- --------------- Editar Empleado -------------------------------
Delimiter //
create procedure sp_EditarEmpleado(in codEmpleado int, in numEmpleado int, in apelEmpleado varchar(150), 
	in nomEmpleado varchar(150), in direcEmpleado varchar(150), in telContacto varchar(8), 
    in gradCocinero varchar(50))
    begin 
		update Empleados set numeroEmpleado = numEmpleado, apellidosEmpleado = apelEmpleado, 
			nombresEmpleado = nomEmpleado, direccionEmpleado = direcEmpleado, 
			telefonoContacto = telContacto, gradoCocinero = gradCocinero
            where codigoEmpleado = codEmpleado;
    end //
Delimiter ;
call sp_EditarEmpleado(1, 1, 'Boteo', 'Maynor', 'zona 20', '35764500', '1');
-- ----------------- Listar Empleados --------------------------------
Delimiter //
create procedure sp_ListarEmpleados()
	begin 
		select 
			codigoEmpleado, 
			numeroEmpleado, 
            apellidosEmpleado, 
            nombresEmpleado, 
            direccionEmpleado, 
            telefonoContacto, 
            gradoCocinero, 
            codigoTipoEmpleado
            from Empleados;
    end //
Delimiter ;
call sp_ListarEmpleados();
-- ----------------- Buscar Empleado -------------------
Delimiter //
create procedure sp_BuscarEmpleado(in codEmpleado int)
	begin
		select 
			codigoEmpleado, 
			numeroEmpleado, 
            apellidosEmpleado, 
            nombresEmpleado, 
            direccionEmpleado, 
            telefonoContacto, 
            gradoCocinero, 
            codigoTipoEmpleado
            from Empleados where codigoEmpleado = codEmpleado;
    end //
Delimiter ;
call sp_BuscarEmpleado(1);
-- -------------------- Eliminar Empleado ---------------------------
Delimiter //
create procedure sp_EliminarEmpleado(in codEmpleado int)
	begin
		delete from Empleados where codigoEmpleado = codEmpleado;
    end //
Delimiter ;
-- --------------------- Agregar Servicio ----------------------
Delimiter //
create procedure sp_AgregarServicios(in fechaServicio date, in tipoServicio varchar(150), 
	in horaServicio time,in lugarServicio varchar(150),in telefonoContacto varchar(150),in codigoEmpresa int)
    Begin 
		insert into Servicios(fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, 
			codigoEmpresa)
            values(fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa);
    end //
Delimiter ;
call sp_AgregarServicios('2023-03-28', 'Buffet', '1:00', 'zona 12', '23456789', 1);
call sp_AgregarServicios('2023-05-29', 'Buffet', '14:00', 'zona 22', '34567890', 2);
call sp_AgregarServicios('2023-09-02', 'Buffet', '19:00', 'zona 14', '12345678', 3);
call sp_AgregarServicios('2023-07-30', 'Buffet', '13:00', 'zona 9', '10923874', 4);
call sp_AgregarServicios('2023-12-05', 'Buffet', '9:00', 'zona 1', '34567890', 5);
-- -------------------- Editar Servicio ---------------------------
-- drop procedure sp_EditarServicio;
Delimiter //
create procedure sp_EditarServicio(in codServicio int, in feServicio date, in tipServicio varchar(150), in horServicio time,
	in luServicio varchar(150),in telContacto varchar(150),in codEmpresa int)
    begin
		Update Servicios set fechaServicio = feServicio, tipoServicio = tipServicio, 
			horaServicio = horServicio, lugarServicio = luServicio, telefonoContacto = telContacto, 
			codigoEmpresa = codEmpresa where codigoServicio = codServicio;
    end //
Delimiter ;
call sp_EditarServicio(1, '2023-04-25', 'Buffet', '2:00', 'zona 15', '23216789', 1);
-- -------------------- Listar Servicios --------------------------------
Delimiter //
create procedure sp_ListarServicios()
	begin
		select 
			codigoServicio, 
            fechaServicio, 
            tipoServicio, 
            horaServicio, 
            lugarServicio, 
            telefonoContacto, 
            codigoEmpresa
            from Servicios;
    end //
Delimiter ;
call sp_ListarServicios();
-- -------------------- Buscar Servicio ---------------------------
Delimiter //
create procedure sp_BuscarServicio(in codServicio int)
	begin
		select 
			codigoServicio, 
            fechaServicio, 
            tipoServicio, 
            horaServicio, 
            lugarServicio, 
            telefonoContacto, 
            codigoEmpresa
            from Servicios where codigoServicio = codServicio;
    end //
Delimiter ;
call sp_BuscarServicio(1);
-- -------------------- Eliminar Servicio -----------------------
Delimiter //
create procedure sp_EliminarServicio(in codServicio int)
	begin
		delete from Servicios where codigoServicio = codServicio;
    end //
Delimiter ;
-- -------------------- Agregar Presupuesto ----------------------
Delimiter //
create procedure sp_AgregarPresupuesto(in feSolicitud date,in cantPresupuesto int, in codEmpresa int)
	begin
		insert Into Presupuestos(fechaSolicitud, cantidadPresupuesto, codigoEmpresa)
			values(feSolicitud, cantPresupuesto, codEmpresa);
    end //
Delimiter ;
call sp_AgregarPresupuesto('2023-04-26', 23000, 1);
call sp_AgregarPresupuesto('2023-05-29', 44000, 2);
call sp_AgregarPresupuesto('2023-09-02', 50000, 3);
call sp_AgregarPresupuesto('2023-07-30', 10000, 4);
call sp_AgregarPresupuesto('2023-12-05', 90000, 5);
-- ------------------- Editar Presupuesto ------------------------
Delimiter //
create procedure sp_EditarPresupuesto(in codPresupuesto int, in feSolicitud date,in cantPresupuesto int)
	begin
		update Presupuestos set fechaSolicitud = feSolicitud, cantidadPresupuesto = cantPresupuesto
			 where codigoPresupuesto = codPresupuesto;
    end //	
Delimiter ;
call sp_EditarPresupuesto(1, '2023-09-26', 2400);
-- ------------------- Listar Presupuestos -------------------------
Delimiter //
create procedure sp_ListarPresupuestos()
	begin
		select codigoPresupuesto, fechaSolicitud, cantidadPresupuesto, codigoEmpresa
			from Presupuestos;
    end //
Delimiter ;
call sp_ListarPresupuestos();
-- --------------------- Buscar Presupuesto --------------------
Delimiter //
create procedure sp_BuscarPresupuesto(in codPresupuesto int)
	begin 
		select codigoPresupuesto, fechaSolicitud, cantidadPresupuesto, codigoEmpresa
			from Presupuestos where codigoPresupuesto = codPresupuesto;
    end //
Delimiter ;
call sp_BuscarPresupuesto(1);
-- --------------------- Eliminar Presupuesto ----------------------
Delimiter //
create procedure sp_EliminarPresupuesto(in codPresupuesto int)
	begin
		delete from Presupuestos where codigoPresupuesto = codPresupuesto;
    end //
Delimiter ;
-- call sp_EliminarPresupuesto(1);
-- ----------------- Agregar Plato --------------------------
Delimiter //
create procedure sp_AgregarPlato(in cantidadPlato int, in nombrePlato varchar(150), 
	in descripcionPlato varchar(150),in precioPlato decimal(10,2),in codigoTipoPlato int)
    begin
		insert into Platos (cantidadPlato, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato)
			values(cantidadPlato, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato);
    end //
Delimiter ;
call sp_AgregarPlato(3, 'Boloñesa Italiana', 'Gran cantida de fideos bañados en una salsa de tomate italiana
	con albóndigas', 55.00, 1); 
call sp_AgregarPlato(15, 'Carne asada con Tomate', 'Carne asada al punto bañada con jugo de tomates asados', 65.00, 2);
call sp_AgregarPlato(10, 'Pizza Margarita', 'Pizza con diferentes vegetales y carnes', 50.00, 3);  
call sp_AgregarPlato(15, 'Ensalada Mar y Tierra', 'Ensalada con pollo y mariscos', 60.00, 4);
call sp_AgregarPlato(20, 'Estofado Chileno', 'Clasico estofado con pollo y verduras', 70.00, 5);  
-- ---------------- Editar Plato ----------------------------
-- drop procedure sp_EditarPlato;
Delimiter //
create procedure sp_EditarPlato(in codPlato int, in cantPla int, in nomPlato varchar(150), in desPlato varchar(150),
	in prePlato decimal(10,2),in codTipoPlato int)
    begin
		update Platos set cantidadPlato = cantPla, nombrePlato = nomPlato, descripcionPlato = desPlato, 
			precioPlato = prePlato, codigoTipoPlato = codTipoPlato where codigoPlato = codPlato;
    end //
Delimiter ;
call sp_EditarPlato(10, 15, 'Fideos Blancos con Pollo', 'Gran cantida de fideos bañados en una salsa blanca con
	pedazitos de pollo cocido con sal', 65.00, 1);  
-- ---------------- Listar Platos ------------------------
Delimiter //
create procedure sp_ListarPlatos()
	begin
		select 
			codigoPlato, 
            cantidadPlato, 
            nombrePlato, 
            descripcionPlato, 
            precioPlato, 
            codigoTipoPlato
            from Platos;
    end //
Delimiter ;
call sp_ListarPlatos();
-- ----------------- Buscar Plato ------------------
Delimiter //
create procedure sp_BuscarPlato(in codPlato int)
	begin
		select 
			codigoPlato, 
            cantidadPlato, 
            nombrePlato, 
            descripcionPlato, 
            precioPlato, 
            codigoTipoPlato
            from Platos where codigoPlato = codPlato;
    end //
Delimiter ;
call sp_BuscarPlato(1);
-- ------------------- Eliminar Plato -----------------
Delimiter //
create procedure sp_EliminarPlato(in codPlato int)
	begin
		delete from Platos where codigoPlato = codPlato;
    end //
Delimiter ;

-- ---------------- Agregar Productos_has_Platos -----------------
Delimiter //
create procedure sp_AgregarProductos_has_Platos(in Productos_codigoProducto int, in codigoPlato int, 
	in codigoProducto int)
	begin
		 insert into Productos_has_Platos(Productos_codigoProducto, codigoPlato, codigoProducto)
			values(Productos_codigoProducto, codigoPlato, codigoProducto);
    end //
Delimiter ;
call sp_AgregarProductos_has_Platos(1,1,1);
call sp_AgregarProductos_has_Platos(2,2,2);
call sp_AgregarProductos_has_Platos(3,3,3);
call sp_AgregarProductos_has_Platos(4,4,4);
call sp_AgregarProductos_has_Platos(5,5,5);
-- --------------- Editar Productos_has_Platos ------------------------
-- ---------------- Listar Productos_has_Platos ----------------------
Delimiter //
create procedure sp_ListarProductos_has_Platos()
	begin
    select Productos_codigoProducto, codigoPlato, codigoProducto from Productos_has_Platos;
    end //
Delimiter ;	
call sp_ListarProductos_has_Platos();
-- --------------- Buscar Producto_has_Plato -------------------------
Delimiter //
create procedure sp_BuscarProducto_has_Platos(in Productos_codProducto int)
	begin
    select Productos_codigoProducto, codigoPlato, codigoProducto from Productos_has_Platos
		where Productos_codigoProducto = Productos_codProducto;
    end //
Delimiter ;
call sp_BuscarProducto_has_Platos(1);
-- ---------------- Eliminar Producto_has_Plato ---------------------------
Delimiter //
create procedure sp_EliminarProducto_has_Plato(in Productos_codProducto int)
	begin
		delete from Productos_has_Platos where Productos_codigoProducto = Productos_codProducto;
    end //
Delimiter ;

-- ---------------- Agregar Servicio_has_Plato ---------------------------
Delimiter //
create procedure sp_AgregarServicio_has_Plato(in Servicios_codigoServicio int,in codigoPlato int, 
	in codigoServicio int)
    begin
		insert into Servicios_has_Platos(Servicios_codigoServicio, codigoPlato, codigoServicio)
			values(Servicios_codigoServicio, codigoPlato, codigoServicio);
    end //
Delimiter ;
call sp_AgregarServicio_has_Plato(1, 1, 1);
call sp_AgregarServicio_has_Plato(2, 2, 2);
call sp_AgregarServicio_has_Plato(3, 3, 3);
call sp_AgregarServicio_has_Plato(4, 4, 4);
call sp_AgregarServicio_has_Plato(5, 5, 5);
-- --------------- Editar Servicio_has_Plato -------------------------------
-- ------------- Listar Servicios_has_Platos ----------------------------
Delimiter //
create procedure sp_ListarServicios_has_Platos()
	begin
		select Servicios_codigoServicio, codigoPlato, codigoServicio
        from Servicios_has_Platos;
    end //
Delimiter ;
call sp_ListarServicios_has_Platos();
-- --------------- Buscar Servicio_has_Platos -------------------------
Delimiter //
create procedure sp_BuscarServicio_has_Plato(in Servicios_codServicio int)
	begin
		select Servicios_codigoServicio, codigoPlato, codigoServicio
        from Servicios_has_Platos where Servicios_codigoServicio = Servicios_codServicio;
    end //
Delimiter ;
call sp_BuscarServicio_has_Plato(1);
-- --------------- Eliminar Servicio_has_Plato ---------------------------
Delimiter //
create procedure sp_EliminarServicio_has_Plato(in Servicios_codServicio int)
	begin
		delete from Servicios_has_Platos where Servicios_codigoServicio = Servicios_codServicio;
    end //
Delimiter ;
-- ------------- Agregar Servicio_has_Empleado ---------------------------
Delimiter //
create procedure sp_AgregarServicio_has_Empleado(in Servicios_codigoServicio int,in codigoServicio int, 
	in codigoEmpleado int,in fechaEvento date,in horaEvento time,in lugarEvento varchar(150))
    begin
		insert into Servicios_has_Empleados(Servicios_codigoServicio, codigoServicio, codigoEmpleado, 
			fechaEvento, horaEvento, lugarEvento)
            values(Servicios_codigoServicio, codigoServicio, codigoEmpleado, fechaEvento, horaEvento, 
				lugarEvento);
    end //
Delimiter ;
call sp_AgregarServicio_has_Empleado(1, 1, 1, '2023-08-8', '10:00', 'zona 22');
call sp_AgregarServicio_has_Empleado(2, 2, 2, '2023-05-29', '14:00', 'zona 22');
call sp_AgregarServicio_has_Empleado(3, 3, 3, '2023-09-02', '19:00', 'zona 14');
call sp_AgregarServicio_has_Empleado(4, 4, 4, '2023-07-30', '13:00', 'zona 9');
call sp_AgregarServicio_has_Empleado(5, 5, 5, '2023-12-05', '09:00', 'zona 1');
call sp_ListarEmpleados();
-- -------------- Editar Servicio_has_Empleado --------------------------
-- drop procedure sp_EditarServicio_has_Empleado;
Delimiter //
create procedure sp_EditarServicio_has_Empleado(in Ser_codigoServicio int,
	in feEvento date,in horEvento time,in luEvento varchar(150))
    begin
		update Servicios_has_Empleados set fechaEvento = feEvento, horaEvento = horEvento, lugarEvento = luEvento
            where Ser_codigoServicio = Servicios_codigoServicio;
    end //
Delimiter ;
call sp_EditarServicio_has_Empleado(1, '2024-09-8', '11:00', 'zona 12');
-- --------------------- Listar Servicios_has_Empleados ----------------------
Delimiter //
create procedure sp_ListarServicios_has_Empleados()
	begin
		select 
			Servicios_codigoServicio, 
            codigoServicio, 
			codigoEmpleado, 
			fechaEvento, 
            horaEvento, 
            lugarEvento
            from Servicios_has_Empleados;
    end //
Delimiter ;	
call sp_ListarServicios_has_Empleados();
-- ----------------- Buscar Servicio_has_Empleado ------------------------------------
Delimiter //
create procedure sp_BuscarServicio_has_Empleado(in Servicios_codServicio int)
	begin
		select 
			Servicios_codigoServicio, 
            codigoServicio, 
			codigoEmpleado, 
			fechaEvento, 
            horaEvento, 
            lugarEvento
            from Servicios_has_Empleados 
				where Servicios_codigoServicio = Servicios_codServicio;
    end //
Delimiter ;
call sp_BuscarServicio_has_Empleado(1);
-- -------------------- Eliminar Servicios_has_Empleado -----------------------------
Delimiter //
create procedure sp_EliminarServicio_has_Empleado(in Servicios_codServicio int)
	begin
		delete from Servicios_has_Empleados where Servicios_codigoServicio = Servicios_codServicio;
    end //
Delimiter ;

-- ----------------------------------------------------------------------------------------
Delimiter //
create procedure sp_ServicioEmpresa(in codEmpresa int)
begin 
select EM.codigoEmpresa, EM.nombreEmpresa, EM.direccion, EM.telefono, S.codigoServicio, S.tipoServicio, S.lugarServicio, S.horaServicio, SE.fechaEvento  , E.nombresEmpleado, E.apellidosEmpleado, TE.descripcion, P.nombrePlato, P.cantidadPlato, PRE.cantidadPresupuesto , PR.nombreProducto, PR.cantidad
		from Empresas EM inner join Servicios S on EM.codigoEmpresa = S.codigoEmpresa
			inner join servicios_has_empleados SE on  S.codigoServicio = SE.codigoServicio
				 inner join Empleados E on SE.codigoEmpleado = E.codigoEmpleado
					inner join TipoEmpleado TE on E.codigoTipoEmpleado = TE.codigoTipoEmpleado
						 inner join servicios_has_platos SP on S.codigoServicio = SP.codigoServicio
							 inner join Platos P on SP.codigoPlato = P.codigoPlato
								 inner join productos_has_platos PP on P.codigoPlato = PP.codigoPlato
									inner join Productos PR on PP.codigoProducto = PR.codigoProducto
										inner join Presupuestos PRE on EM.codigoEmpresa = PRE.codigoEmpresa
					where EM.codigoEmpresa = codEmpresa;
	
end //
Delimiter ;
call sp_ServicioEmpresa(2);
------------------------------------------------------------------------

