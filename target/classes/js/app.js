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
var app = angular.module('sonidoambiental', ['ngRoute']).
        config(['$routeProvider', function($routeProvider) {
                $routeProvider.when('/', { templateUrl: 'inicio.html', abstract: true, controller: "InicioController"}).
                when('/listado', {templateUrl: 'listado.html', controller: "ListadoController"})./*
      when('/agregar', {templateUrl: 'plantillas/agregar.html', controller: ControladorAgregar}).*/
        otherwise({redirectTo: '/'});
    }]);
//function Listadodecanciones($scope, $http, $location) {
app.controller("ListadoController", function ($scope, $interval) {
    /* Carga de datos en el controlador */
    console.log("ejecutando listado");
    $scope.cargar = function() {
        $.ajax({
            url: '/json/' + encodeURI('lista'),
            dataType: 'json',
            method: 'get',
            success: function(datos) {
                /* Si todo ha ido bien mostramos una alerta con el contenido */
                if (typeof datos.error !== 'undefined' && datos.error === false) {
                    $scope.canciones = datos.listadecanciones;
                    // console.log(datos.listadecanciones.toString());
                    //alert("Mensaje recibido: " + datos.mensaje);
                } else {
                    console.log("error en la consola");
                    //alert("Error en la consulta");
                }
            }
            
        });
    };
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
function preguntar(pregunta){
    var retorna;
    $.ajax({
        url: '/json/' + encodeURI(pregunta),
        dataType: 'json',
        method: 'get',
        success: function(datos) {
            /* Si todo ha ido bien mostramos una alerta con el contenido */
            if (typeof datos.error !== 'undefined' && datos.error === false) {
                console.log(datos.mensaje);
                retorna= datos.mensaje;
                return retorna;
                //alert("Mensaje recibido: " + datos.mensaje);
            } else {
                return null;
            }
        }
    });
};


//app.
app.controller("InicioController", function ($scope, $interval) {
    $scope.crearusuario = false;
    $scope.primerinicio=function(){
        $.ajax({
            url: '/json/' + encodeURI('sinusuarios'),
            dataType: 'json',
            method: 'get',
            success: function(datos) {
                if (typeof datos.error !== 'undefined' && datos.error === false) {
                    console.log("hay respuesta"+datos.mensaje+"si");
                    if (datos.mensaje === ''){                        
                        $scope.titulodeformulario = "Ingrese sus datos";    
                        $scope.botoningresar = "Ingresar";                          
                    }
                    else
                    {
                        console.log("que pasa");                        
                        $scope.titulodeformulario = "No hay usuario creado por favor ingrese los datos correspondientes";                                       
                        $scope.crearusuario = true;
                        $scope.botoningresar = "Crear usuario";    
                        
                    }
                }
            }
        });
        
    };
    
        //descomentar lo siguiente para mostrar el estado del servidor
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
                            $scope.mensaje1 = datos.mensaje;
                            //alert("Mensaje recibido: " + datos.mensaje);
                        } else {
                            //alert("Error en la consulta");
                        }
                    }
                });
                $.ajax({
                    url: '/json/' + encodeURI('estado'),
                    dataType: 'json',
                    method: 'get',
                    success: function(datos) {
                        if (typeof datos.error !== 'undefined' && datos.error === false) {
                            $scope.estado = datos.mensaje;
                            //alert("Mensaje recibido: " + datos.mensaje);
                        } else {
                            //alert("Error en la consulta");
                        }
                    }
                });
            }, 1500);

        };
        $scope.loguear=function(){
            var usuarioqueloguea="";
            console.log("!");
            //alert("anda");
            var usuario = document.getElementById('Usuario');
            var contrasena = document.getElementById('contrasena');
            //alert("anda");
            //alert();
            $.ajax({
                url: '/json/' + encodeURI('LoGuEo:' + Sha1.hash(usuario + contrasena)),
                dataType: 'json',
                method: 'get',
                success: function(datos) {               
                    if (typeof datos.error !== 'undefined' && datos.error === false) {
                        console.log(datos.mensaje);
                        usuarioqueloguea = datos.mensaje;
                    } else {                
                        $scope.mensaje1 ="";
                    }
                }
            });
            alert("loguea:"+ usuarioqueloguea);
        };
        $scope.detenerreproductor=function(){
            console.log("!");
            preguntar('pausar-reproducir');
            yaesta=true;
        };   
        //$scope.StartTimer();
        $scope.mensaje1 = "¡¡Hola Mundo!!";
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

