/* global angular:false, $:false */
/* eslint-env browser */
/* 
 * Sistema creado por bfhsoftware para musica ambiental
 * Bernardo harreguy, Derechos reservados
 * and open the template in the editor.
 */
/*var sonidoambiental= angular.module("sonidoambiental",[]);*/
//var app=
 var app = angular.module('sonidoambiental', ['ngRoute']).
  config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/', { templateUrl: 'inicio.html', abstract: true, controller: "Inicio"}).
      /*when('/editar/:id', {templateUrl: 'plantillas/editar.html', controller: ControladorEditar}).
      when('/agregar', {templateUrl: 'plantillas/agregar.html', controller: ControladorAgregar}).*/
      otherwise({redirectTo: '/'});
}]);

function preguntar(pregunta){
    var retorna;
    $.ajax({            
          url: '/json/' + encodeURI(pregunta),
          dataType: 'json',
          method: 'get',
          success: function(datos) {
            /* Si todo ha ido bien mostramos una alerta con el contenido */
            if (typeof datos.error !== 'undefined' && datos.error === false) { 
               // console.log(datos.mensaje);
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
app.controller("Inicio", function ($scope, $interval) {
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
            /* Si todo ha ido bien mostramos una alerta con el contenido */
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
            /* Si todo ha ido bien mostramos una alerta con el contenido */
            if (typeof datos.error !== 'undefined' && datos.error === false) {    
                $scope.estado = datos.mensaje;
              //alert("Mensaje recibido: " + datos.mensaje);
            } else {
              //alert("Error en la consulta");
            }
          }
        });
       }, 500);
   
    };
    $scope.detenerreproductor=function(){
        console.log("!");
        preguntar('pausar-reproducir');   
        yaesta=true;
    };
$scope.StartTimer();    
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