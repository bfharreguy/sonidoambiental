/* global angular:false, $:false */
/* eslint-env browser */
/*
 * Sistema creado por bfhsoftware para musica ambiental
 * Bernardo harreguy, Derechos reservados
 * and open the template in the editor.
 */
/*var sonidoambiental= angular.module("sonidoambiental",[]);*/
//var app=
//import Vue from 'vue'
//var sha1 = require('sha1');
//var informacionamostrar = "";
//var erroramostrar = "";
var archivosseleccionados ;
var titulodeformulario = "Ingrese sus datos";
var botoningresar = "Ingresar";
var crearusuario =false;
var mostrarrutas = false;
var subiendoarchivos = false;
var porcentajesubido = 0;
var cancionesseleccionadas= 0;
/*function seleccionado(seleccion) {    
    this.seleccion = seleccion;
};*/
//,'ngStorage', 'ngFileUpload'
var app = angular.module('sonidoambiental', ['ngRoute','ngStorage']).
        config(['$routeProvider', function($routeProvider) {
                $routeProvider.when('/', { templateUrl: 'inicio.html', abstract: true, controller: "InicioController"}).
                when('/listado', {templateUrl: 'listado.html', controller: "ListadoController"})./*
      when('/agregar', {templateUrl: 'plantillas/agregar.html', controller: ControladorAgregar}).*/
        otherwise({redirectTo: '/'});
    }]);
//function Listadodecanciones($scope, $http, $location) {
app.controller("ListadoController", function ($scope, $interval, $location) {
    $(document).ready(function() {
        $('#listadomultimedia').DataTable();
    } );
    //    function ListadoController($scope, $interval) {
    /* Carga de datos en el controlador */
    $scope.seleccionartodos = function(){
        for (var i = 0; i < $scope.canciones.length; i++) {
            $scope.canciones[i].seleccionado=true;            
        }
        $scope.cancionesseleccionadas= $scope.canciones.length;  
        $scope.$apply;     
    }
    $scope.desseleccionartodos = function(){
        for (var i = 0; i < $scope.canciones.length; i++) {           
            $scope.canciones[i].seleccionado=false;            
        }
        $scope.cancionesseleccionadas= 0;       
        $scope.$apply;
    }
    $scope.invertirseleccion = function(){
        var cantidad = 0;
        for (var i = 0; i < $scope.canciones.length; i++) {
            switch ($scope.canciones[i].seleccionado){
                case true:
                    $scope.canciones[i].seleccionado=false;            
                    break;
                case false:
                    $scope.canciones[i].seleccionado=true;
                    cantidad +=1;
                    break;
            }            
        }        
        $scope.cancionesseleccionadas= cantidad;
        $scope.$apply;
    }
    $scope.seleccionar = function(cancion){                
        cancion.seleccionado = true;
        var cantidad = 0;
        for (var i = 0; i < $scope.canciones.length; i++) {
            if ($scope.canciones[i].seleccionado)
                cantidad +=1;
        }
        $scope.cancionesseleccionadas= cantidad;
        $scope.$apply;
    }
    
    $scope.desseleccionar = function(cancion){
        cancion.seleccionado = false;
        var cantidad = 0;
        for (var i = 0; i < $scope.canciones.length; i++) {
            if ($scope.canciones[i].seleccionado)
                cantidad +=1;
        }
        $scope.cancionesseleccionadas= cantidad;
        $scope.$apply;
    }
    console.log("ejecutando listado");
    $scope.cargar = function() {
        $.ajax({
            url: '/json/' + encodeURI('lista'),
            dataType: 'json',
            method: 'get',
            success: function(datos) {
                /* Si todo ha ido bien mostramos una alerta con el contenido */
                if (typeof datos.error !== 'undefined' && datos.error === false) {
                    
                    $scope.canciones =  datos.listadecanciones;
                    $scope.$apply();
                    
                } else {
                    console.log("error al recibir canciones");                   
                }
            }
        });        
    };
    $scope.btnmostrarrutas = function(){
        $scope.mostrarrutas = !$scope.mostrarrutas;        
        $scope.$apply;
    }
    $scope.borrarseleccionadas=function(){
        
    }
    /* Control interno para borrar un usuario
  $scope.borrar = function(usuario) {
    borrarUsuario($scope, $http, $location, usuario);
    /* Buscamos el elemento para borrarlo de la vista
    for (var actual in $scope.usuarios) {
      if ($scope.usuarios[actual].id == usuario.id) {
        $scope.usuarios.splice(actual, 1);
      }
    }
  };*/
    /* Función de ordenado de columnas
  $scope.columna = 'usuario';
  $scope.orden = true;
  $scope.ordenar = function(columna) {
    $scope.orden = ($scope.columna === columna) ? !$scope.orden : false;
    $scope.columna = columna;
  };
  /* Cargamos el listado de usuarios */
    $scope.cargar();
});
//class comunicacion {

//    angular.module('sonidoambiental.services',[]).value ('version', '0.1')
//  .factory('comunicacion',['$http', 'API_KEY', function($http,API_KEY) {
/*     app.run(function($rootScope){
          $rootScope.obtenerstring = function(pregunta){
            var retorna;
            $.ajax({
                url: '/json/' + encodeURI(pregunta),
                dataType: 'json',
                method: 'get',
                success: function(datos) {

                    if (typeof datos.error !== 'undefined' && datos.error === false) {
                        console.log("p"+datos.mensaje+"p");
                        retorna= datos.mensaje;
                        //return datos.mensaje;
                        //alert("Mensaje recibido: " + datos.mensaje);
                    } else {
                        retorna = null;
                        //return null;
                    }
                }
            });
            return retorna;
        }
    });*/
function usuario(usuario, sha1) {
    this.usuario = usuario;
    this.sha1 = sha1;
};
//app.

app.controller('sesion', function ($scope, $localStorage, $sessionStorage, $window) {
    $scope.iniciarsesion = function (nombredeusuario) {
        $localStorage.LocalMessage = "iniciada";
        $sessionStorage.SessionMessage = nombredeusuario;
    }
    $scope.cerrarsesion = function () {
        $localStorage.LocalMessage = "";
        $sessionStorage.SessionMessage = "";
    }
    $scope.cargar = function () {
        return $sessionStorage.SessionMessage;
        //$window.alert($localStorage.LocalMessage );
        //+ "\n" + $sessionStorage.SessionMessage);
    }
    $scope.sesion = function(){
        var regresa = ($localStorage.LocalMessage === "iniciada");
        return regresa;
    }
    $scope.sesioniniciada = function(){
        console.log ($sessionStorage.SessionMessage);
        return $sessionStorage.SessionMessage;
    }
    $scope.obtenerstring = function(enviardatos) {
        $.ajax({
            url: '/json/' + encodeURI(enviardatos),
            dataType: 'json',
            method: 'get',
            success: function(datos) {
                if (typeof datos.error !== 'undefined' && datos.error === false) {
                    return datos.mensaje;
                }                
            }
        });
        return null;
    }
});
app.controller("InicioController", function ($scope, $interval, $location) {
    function usuario(usuario, sha1) {
        this.usuario = usuario;
        this.sha1 = sha1;
    };
    $scope.primerinicio=function(){
        $scope.titulodeformulario = titulodeformulario;
        $scope.botoningresar = botoningresar;
        $scope.crearusuario = crearusuario;
        $.ajax({
            url: '/json/' + encodeURI('sinusuarios'),
            dataType: 'json',
            method: 'get',
            success: function(datos) {
                if (typeof datos.error !== 'undefined' && datos.error === false) {
                    if (datos.mensaje === ' '){
                        titulodeformulario = "No hay usuario creado por favor ingrese los datos correspondientes";
                        crearusuario = true;
                        botoningresar = "Crear usuario";
                        $scope.titulodeformulario = titulodeformulario;
                        $scope.botoningresar = botoningresar;
                        $scope.crearusuario = crearusuario;
                        $scope.$apply();
                    }
                    
                }
            }
        });
    };
    
    $scope.Timer=null;
    $scope.yaesta = false;
    $scope.StartTimer = function () {
        $scope.Timer = $interval( function () {
            //console.log('corriendo');
            // $scope.mensaje1 = preguntar('musica');
            // $scope.tiempo = preguntar('tiempo');
            $.ajax({
                url: '/json/' + encodeURI('musica'),
                dataType: 'json',
                method: 'get',
                success: function(datos) {
                    if (typeof datos.error !== 'undefined' && datos.error === false) {
                        $scope.InicioController.mensaje1 = datos.mensaje;
                    } 
                }
            });
            $.ajax({
                url: '/json/' + encodeURI('estado'),
                dataType: 'json',
                method: 'get',
                success: function(datos) {
                    if (typeof datos.error !== 'undefined' && datos.error === false) {
                        $scope.InicioController.estado = datos.mensaje;                                    
                    } 
                }
            });
        }, 1500);
        
    };
    $scope.loguear=function(){
        var usuario = document.getElementsByName("Usuario")[0].value;
        var contrasena = document.getElementsByName("Contrasena")[0].value;
        if (usuario != null){
            if (usuario.indexOf(':') > -1){
                $scope.erroramostrar ="No se acepta : como caracter para crear usuario " ;
                return null ;
            }
        }else{
            $scope.erroramostrar ="Debe rellenar el formulario: Falta usuario" ;
            return null;
        }
        if (contrasena != null){
            
        }else{
            $scope.erroramostrar ="Debe rellenar el formulario: Falta Contraseña" ;
            return null;
        }
        $scope.erroramostrar ="";
        if ($scope.titulodeformulario == "Ingrese sus datos") {
            console.log("logueo");
            var usuarioqueloguea="";
            $.ajax({
                url: '/json/' + encodeURI('LoGuEo:'+usuario+ ':' + Sha1.hash(usuario + contrasena)),
                dataType: 'json',
                method: 'get',
                success: function(datos) {
                    if (typeof datos.error !== 'undefined' && datos.error === false) {
                        console.log(datos.mensaje)
                        switch(datos.mensaje) {
                            case 'loguea':
                                $scope.informacionamostrar ="El usuario " + usuario + " se logueo ";
                                $scope.erroramostrar ="";
                                $scope.iniciarsesion(usuario);                                            
                                console.log(sessionStorage.SessionMessage);                                             
                                $location.path('/listado').replace();   
                                $scope.$apply();                     
                                return "logueado";
                                break;
                            default:
                                $scope.erroramostrar ="No se encuentran registros de la información ingresada";
                                $scope.informacionamostrar ="";
                                $scope.$apply();
                                return "error";
                                break;
                        }
                    } else {
                        $scope.erroramostrar ="Error de comunicacion!";
                        $scope.informacionamostrar ="";
                        $scope.$apply();
                        return "error";
                    }
                }
            });
        }
        else
        {
            console.log("crear usuario")
            console.log('CrEaR:' + usuario + ':' + Sha1.hash(usuario + contrasena));
            $.ajax({
                url: '/json/' + encodeURI('CrEaR:' + usuario + ':' + Sha1.hash(usuario + contrasena)),
                dataType: 'json',
                method: 'get',
                success: function(datos) {
                    if (typeof datos.error !== 'undefined' && datos.error === false) {
                        if (datos.mensaje =="existente") {
                            $scope.erroramostrar ="El usuario " + usuario + " que esta intentando crear ya existe ";
                            $scope.informacionamostrar ="";
                        }else{
                            $scope.informacionamostrar ="Se ha creado la cuenta de " + usuario + " satisfactoriamente";
                            $scope.erroramostrar ="" ;
                            titulodeformulario = "Ingrese sus datos";
                            crearusuario = true;
                            botoningresar = "Ingresar";
                            $scope.titulodeformulario = titulodeformulario;
                            $scope.botoningresar = botoningresar;
                            $scope.crearusuario = crearusuario;
                        }
                    } else {
                        $scope.erroramostrar ="Hubo errores en la creacion de la cuenta  " + usuario;
                        $scope.informacionamostrar ="";
                    }
                    $scope.$apply
                }
            });
        }
        //alert("loguea:"+ usuarioqueloguea);
    };
    $scope.detenerreproductor=function(){
        console.log("!");
        preguntar('pausar-reproducir');
        yaesta=true;
    };
    //$scope.StartTimer();
    // $scope.mensaje1 = "¡¡Hola Mundo!!";
});
var nombre = document.getElementById('nombre');

function enviar_post() {
    $.ajax({
        url: '/json/',
        dataType: 'json',
        method: 'post',
        data: JSON.stringify({
            mensaje: nombre.value,
        }),
        contentType: 'application/json; charset=utf-8',
        success: function(datos) {
            /* Si todo ha ido bien mostramos una alerta con el contenido */
            if (typeof datos.error !== 'undefined' && datos.error === false) {
                //$scope.$root.PruebaController= datos.mensaje;
                alert("Mensaje recibido: " + datos.mensaje);
            } else {
                alert("Error en la consulta");
            }
        }
    });
}
function enviar_plano_url() {
    $.ajax({
        url: '/texto/' + encodeURI(nombre.value),
        dataType: 'text',
        method: 'get',
        success: function(datos) {
            alert("Mensaje recibido: " + datos);
        }
    });
}
function enviar_plano_post() {
    $.ajax({
        url: '/texto/',
        dataType: 'text',
        method: 'post',
        data: nombre.value,
        success: function(datos) {
            alert("Mensaje recibido: " + datos);
        }
    });
}
app.controller('SubirArchivos', function($scope, $http, $parse) {
    
    $scope.name = 'World';
    $scope.files = []; 
    $scope.upload=function(element) {
        var cantidad = 0;
        $scope.subiendoarchivos = true;
        $scope.porcentajesubido = 0;                
        $scope.$apply;
        //console.log(document.getElementsById("directorioseleccionado"));                
        console.log("intentando subir");
        console.log($scope.files.length);              
        angular.forEach($scope.files, function (item) {                                 
            var fd = new FormData();
            fd.append('file', item);
            $http.post("/upload/", fd, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            }).then(function(success){                        
                cantidad +=1;
                $scope.porcentajesubido = Math.round((cantidad*100)/$scope.files.length);                        
                if ($scope.porcentajesubido  ==100){
                    $scope.subiendoarchivos = false;            
                    $scope.files.length = 0;                    
                }              
                $scope.$apply;  
                console.log("subido :D");
                return success.data.data;
            });                
        });          
        
    };            
});
app.directive('ngFileModel', ['$parse', function ($parse) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var model = $parse(attrs.ngFileModel);
                var isMultiple = attrs.multiple;
                var modelSetter = model.assign;                    
                element.bind('change', function () {
                    var values = [];                       
                    angular.forEach(element[0].files, function (item) {                            
                        if (item.name.endsWith("mp3")){
                            //console.log(document.getElementById('directorioseleccionado').value);
                            
                            //var tmppath = URL.createObjectURL(item);
                            //console.log($("img").fadeIn("fast").attr('src',tmppath));
                            
                            
                            //                                console.log(item.val());
                            //console.log(item.webkitRelativePath);
                            //document.getElementsByName("Contrasena")[0].value
                            /*var value = {           
                                name: item.name,
                                size: item.size,
                                url: URL.createObjectURL(item),
                                _file: item
                            };*/
                            values.push(item);
                        }
                    });
                    scope.$apply(function () {
                        if (isMultiple) {        
                            modelSetter(scope, values);
                        } else {
                            modelSetter(scope, values[0]);
                        }
                    });
                })
            }
        }}]);
/* app.controller('SubirArchivos', function($scope, $http) {
            $scope.name = 'World';
            $scope.files = []; 
            $scope.upload = function () {

        app.controller('myCtrl', ['$scope', 'fileUpload', function($scope, fileUpload){

            $scope.uploadFile = function(){
                var file = $scope.myFile;
                console.log('file is ' );
                console.dir(file);
                var uploadUrl = "/upload/";
                fileUpload.uploadFileToUrl(file, uploadUrl);
            };

        }]);*/

/*        function cambiarFile(){
            const input = document.getElementById('inputFileServer');
            if(input.files && input.files[0])
                console.log("File Seleccionado : ", input.files[0]);

        }
        console.log("Sin Archivo Seleccionado " + document.getElementById('inputFileServer').files[0]);*/