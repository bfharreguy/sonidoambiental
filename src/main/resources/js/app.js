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
var archivosseleccionados;
var tipodedato = "canciones";
var titulodeformulario = "Ingrese sus datos";
var botoningresar = "Ingresar";
var crearusuario = false;
var mostrarrutas = false;
var subiendoarchivos = false;
var porcentajesubido = 0;
var cancionesseleccionadas = 0;
/*function seleccionado(seleccion) {    
    this.seleccion = seleccion;
};*/
//,'ngStorage', 'ngFileUpload'
var app = angular.module('sonidoambiental', ['ngRoute', 'ngStorage']).config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'inicio.html',
        abstract: true,
        controller: "InicioController"
    }).when('/listado', {
        templateUrl: 'listado.html',
        controller: "ListadoController"
    }).when('/albums', {
        templateUrl: 'albums.html',
        controller: "AlbumsController"
    }).when('/subir', {
        templateUrl: 'subirarchivos.html',
        controller: "SubirArchivos"
    }).when('/reproduccion', {
        templateUrl: 'reproduccion.html',
        controller: "ReproduccionController"
    }).otherwise({redirectTo: '/'});
}]);
app.controller("ReproduccionController", function ($scope) {
    if (!$scope.sesion()) {
        $location.path('/').replace();
    }
    $scope.datosdereproduccion = [];
    $scope.agregaralbumareproduccion =function (reproduccion, album) {
        $.ajax({
            url: '/json/' + encodeURI('agregaralbum:' + reproduccion + ":"+ album),
            dataType: 'json',
            method: 'get',
            success: function (datos) {
                if (typeof datos.error !== 'undefined' && datos.error === false) {
                    $scope.cargar();
                }
            }
        });
    };
    $scope.deshabilitar=function (nombre) {
        $.ajax({
            url: '/json/' + encodeURI('deshabilitarreproduccion:' + nombre),
            dataType: 'json',
            method: 'get',
            success: function (datos) {
                if (typeof datos.error !== 'undefined' && datos.error === false) {
                    $scope.cargar();
                }
            }
        });
    };
    $scope.haceralbummaestro = function (nombre) {
        $.ajax({
            url: '/json/' + encodeURI('hacerreproduccionmaestra:' + nombre),
            dataType: 'json',
            method: 'get',
            success: function (datos) {
                if (typeof datos.error !== 'undefined' && datos.error === false) {
                    $scope.cargar();
                }
            }
        });
    };
    $scope.deshaceralbummaestro = function (nombre) {
        $.ajax({
            url: '/json/' + encodeURI('deshacerreproduccionmaestra:' + nombre),
            dataType: 'json',
            method: 'get',
            success: function (datos) {
                if (typeof datos.error !== 'undefined' && datos.error === false) {
                    $scope.cargar();
                }
            }
        });
    };
    $scope.eliminarreproduccion = function (nombre) {
        $.ajax({
            url: '/json/' + encodeURI('eliminarreproduccion:' + nombre),
            dataType: 'json',
            method: 'get',
            success: function (datos) {
                if (typeof datos.error !== 'undefined' && datos.error === false) {
                    $scope.cargar();
                }
            }
        });
    };
    $scope.habilitar=function (nombre) {
        $.ajax({
            url: '/json/' + encodeURI('habilitarreproduccion:' + nombre),
            dataType: 'json',
            method: 'get',
            success: function (datos) {
                if (typeof datos.error !== 'undefined' && datos.error === false) {
                    $scope.cargar();
                }
            }
        });
    };
    $scope.quitaralbumde=function (reproduccion, album) {
        $.ajax({
            url: '/json/' + encodeURI('quitaralbum:' + reproduccion + ":"+ album),
            dataType: 'json',
            method: 'get',
            success: function (datos) {
                if (typeof datos.error !== 'undefined' && datos.error === false) {
                $scope.cargar();
                } else {
                    console.log("error al recibir canciones");
                }
            }
        });

    };
    $scope.cargar = function () {
        $.ajax({
            url: '/json/' + encodeURI('reproduccion'),
            dataType: 'json',
            method: 'get',
            success: function (datos) {
                if (typeof datos.error !== 'undefined' && datos.error === false) {
                    $scope.datosdereproduccion = [];
                    $scope.datosdereproduccion = datos.datosdereproduccion;
                    $scope.$apply();
                    /*$(document).ready(function () {
                        $('#listadereproduccion').DataTable();
                    });*/
                } else {
                    console.log("error al recibir canciones");
                }
            }
        });

    };
    $scope.crearreproduccion=function (nombredereproduccionnueva) {
        console.log('nuevareproduccion:' + nombredereproduccionnueva)
        $.ajax({
            url: '/json/' + encodeURI('nuevareproduccion:' + nombredereproduccionnueva),
            dataType: 'json',
            method: 'get',
            success: function (datos) {
                if (typeof datos.error !== 'undefined' && datos.error === false) {
                $scope.cargar();
                } else {
                    console.log("error al crear nueva reproduccion");
                }
            }
        });
    };

    $scope.cargar();
});
app.controller("barraprincipal", function ($scope, $interval, $location) {
    $scope.inicio = function () {
        $location.path('/').replace();
    };
    $scope.albums = function () {
        $location.path('/albums').replace();
    };
    $scope.subir = function () {
        $location.path('/subir').replace();
    };
    $scope.listado = function () {
        $location.path('/listado').replace();
    }
    $scope.reproduccion = function () {
        $location.path('/reproduccion').replace();
    }
});
/*app.controller("MensajeamostrarController", function ($scope) {
    $scope.erroramostrarmensaje = "";
    $scope.error = true;
    $scope.tituloerroramostrarmensaje = "Ha ocurrido un error";
    $scope.mandaerror=function (errormensaje) {
        $scope.error = true;
        $scope.erroramostrarmensaje = errormensaje;
        $('#mensajeamostrar').modal('show')
        $scope.$apply()
    }
    $scope.mandaerror=function (errormensaje, titulomensaje) {
        $scope.error = true;
        $scope.erroramostrarmensaje = errormensaje;
        $scope.tituloerroramostrarmensaje  = titulomensaje;
        $('#mensajeamostrar').modal('show')
        $scope.$apply()
    }
});*/
//function Listadodecanciones($scope, $http, $location) {
app.controller("AlbumsController", function ($scope, $interval, $location) {
    $scope.titulo = "Álbumes";
    $scope.cantidadseleccionadas = 0;
    $scope.albumes = [];
    $scope.editaroagregar = "";
    $scope.tituloerroramostrarmensaje = "Ha ocurrido un error";
    $scope.mensaje = function (mostrarmensaje) {
        $scope.error = false;
        $scope.erroramostrarmensaje = mostrarmensaje;
        $('#mensajeamostrar').modal('show');
        $scope.$apply()
    };
    $scope.mensaje = function (mostrarmensaje, eserror) {
        $scope.error = eserror;
        $scope.erroramostrarmensaje = mostrarmensaje;
        $('#mensajeamostrar').modal('show');
        $scope.$apply()
    };
    $scope.mensaje = function (mostrarmensaje, eserror, titulo) {
        $scope.tituloerroramostrarmensaje = titulo;
        $scope.error = eserror;
        $scope.erroramostrarmensaje = mostrarmensaje;
        $('#mensajeamostrar').modal('show');
        $scope.$apply()
    };

    $scope.cargar = function () {
        $.ajax({
            url: '/json/' + encodeURI('album'),
            dataType: 'json',
            method: 'get',
            success: function (datos) {
                if (typeof datos.error !== 'undefined' && datos.error === false) {
                    $scope.albumes = datos.mensajes;
                    $scope.$apply();
                    console.log("cargar albumes");
                } else {
                    console.log("error al recibir albumes");
                }
            }
        });
        $scope.cantidadseleccionadas = 0;
        //$scope.$apply();
    };
    $scope.editaroagregaralbum = function (nombre, editaroagregar, nombrenuevo) {
        if (editaroagregar === "Agregar album") {
            $.ajax({
                url: '/json/' + encodeURI('crearalbum:' + nombre),
                dataType: 'json',
                method: 'get',
                success: function (datos) {
                    if (typeof datos.error !== 'undefined' && datos.error === false) {
                        if (datos.mensaje == "existente") {
                            $scope.mensaje("Existe un album con ese nombre");
                        } else
                            $('#editaroagregaralbum').collapse('hide');
                    } else {
                        $scope.mensaje("Error al intentar editar el nombre del album");
                    }
                    $scope.cargar();
                }
            });
        } else {
            $.ajax({
                url: '/json/' + encodeURI('editaralbum:' + nombrenuevo + ":" + nombre),
                dataType: 'json',
                method: 'get',
                success: function (datos) {
                    if (typeof datos.error !== 'undefined' && datos.error === false) {
                        if (datos.mensaje == "existente") {
                            $scope.mensaje("Existe un album con ese nombre");
                            //$scope.erroramostrarmensaje("Existe un album con ese nombre");
                        } else
                            $('#editaroagregaralbum').collapse('hide');
                    } else {
                        $scope.mensaje("Error al intentar editar el nombre del album");
                        //$scope.erroramostrarmensaje("Error al intentar editar el nombre del album");
                    }
                    $scope.cargar();
                }
            });
        }

    };
    $scope.nombredealbumeseleccionado = function () {
        for (var i = 0; i < $scope.albumes.length; i++) {
            if ($scope.albumes[i].seleccionado)
                return $scope.albumes[i].dato;
        }
        return "";
    }
    $scope.eliminaralbum = function () {
        for (var i = 0; i < $scope.albumes.length; i++) {
            if ($scope.albumes[i].seleccionado) {
                $.ajax({
                    url: '/json/' + encodeURI('eliminaralbum:' + $scope.albumes[i].dato),
                    dataType: 'json',
                    method: 'get',
                    success: function (datos) {
                        if (typeof datos.error !== 'undefined' && datos.error === false) {
                            if (datos.mensaje == "existente") {
                                $scope.mensaje("Existe un album con ese nombre");
                            }
                        } else {
                            $scope.mensaje("Error al intentar eliminar el album");
                        }
                        $scope.cargar();
                    }
                });
            }
        }
    }
    $scope.verificarcantidad = function () {
        var cantidad = 0;
        for (var i = 0; i < $scope.albumes.length; i++) {
            if ($scope.albumes[i].seleccionado)
                cantidad += 1;
        }
        $scope.cantidadseleccionadas = cantidad;
    };
    $scope.seleccionar = function (albumes) {
        albumes.seleccionado = true;
        $scope.verificarcantidad();
        $scope.$apply;
    }
    $scope.desseleccionar = function (albumes) {
        albumes.seleccionado = false;
        $scope.verificarcantidad();
        $scope.$apply;
    }
    $scope.cargar();
});
app.controller("ListadoController", function ($scope, $interval, $location, $http) {
    if (!$scope.sesion()) {
        $location.path('/').replace();
    }
    $scope.titulo = "Listado de Archivos Multimedia";
    $scope.canciones = [];
    $scope.albumes = [];
    $scope.albumseleccionado = '';
    $scope.cancionesseleccionadas = 0;
    $scope.mostrarporalbum = function (nombredealbum) {
        $scope.albumseleccionado = nombredealbum;
    };
    $scope.seleccionartodos = function () {
        for (var i = 0; i < $scope.canciones.length; i++) {
            $scope.canciones[i].seleccionado = true;
        }
        $scope.cancionesseleccionadas = $scope.canciones.length;
        $scope.$apply;
    }
    $scope.desseleccionartodos = function () {
        for (var i = 0; i < $scope.canciones.length; i++) {
            $scope.canciones[i].seleccionado = false;
        }
        $scope.cancionesseleccionadas = 0;
        $scope.$apply;
    }
    $scope.invertirseleccion = function () {
        var cantidad = 0;
        for (var i = 0; i < $scope.canciones.length; i++) {
            switch ($scope.canciones[i].seleccionado) {
                case true:
                    $scope.canciones[i].seleccionado = false;
                    break;
                case false:
                    $scope.canciones[i].seleccionado = true;
                    cantidad += 1;
                    break;
            }
        }
        $scope.cancionesseleccionadas = cantidad;
        $scope.$apply;
    }
    $scope.seleccionar = function (cancion) {
        cancion.seleccionado = true;
        var cantidad = 0;
        for (var i = 0; i < $scope.canciones.length; i++) {
            if ($scope.canciones[i].seleccionado)
                cantidad += 1;
        }
        $scope.cancionesseleccionadas = cantidad;
        $scope.$apply;
    }

    $scope.desseleccionar = function (cancion) {
        cancion.seleccionado = false;
        var cantidad = 0;
        for (var i = 0; i < $scope.canciones.length; i++) {
            if ($scope.canciones[i].seleccionado)
                cantidad += 1;
        }
        $scope.cancionesseleccionadas = cantidad;
        $scope.$apply;
    }
    $scope.habilitarseleccionados = function () {
        for (var i = 0; i < $scope.canciones.length; i++) {
            if ($scope.canciones[i].seleccionado) {
                $.ajax({
                    url: '/json/' + encodeURI('habilitar:' + $scope.canciones[i].nombre),
                    dataType: 'json',
                    method: 'get',
                });
                console.log('habilitar');
            }
        }
        $scope.cargar;
        $scope.$apply;
    }
    $scope.anularseleccionados = function () {
        for (var i = 0; i < $scope.canciones.length; i++) {
            if ($scope.canciones[i].seleccionado) {
                $.ajax({
                    url: '/json/' + encodeURI('anular:' + $scope.canciones[i].nombre),
                    dataType: 'json',
                    method: 'get',
                });
                console.log('anular');
            }
        }
        $scope.cargar;
        $scope.$apply;
    }
    $scope.borrarseleccionados = function () {
        for (var i = 0; i < $scope.canciones.length; i++) {
            if ($scope.canciones[i].seleccionado) {
                $.ajax({
                    url: '/json/' + encodeURI('borrar:' + $scope.canciones[i].nombre),
                    dataType: 'json',
                    method: 'get',
                });
            }
        }
        $scope.cargar;
        $scope.$apply;
    }
    //console.log("
    $scope.cambiaralbum = function (albumnombre) {
        for (var i = 0; i < $scope.canciones.length; i++) {
            if ($scope.canciones[i].seleccionado) {
                $.ajax({
                    url: '/json/' + encodeURI('cambiaralbum:' + $scope.canciones[i].nombre + ':' + albumnombre),
                    dataType: 'json',
                    method: 'get',
                });
            }
        }
        $scope.cargar;
        $scope.$apply;
    }
    console.log("ejecutando listado");
    $scope.cargar = function () {
        /*$http({
            method: 'GET',
            url: '/json/' + encodeURI('lista')
        }).then(function (datos){
            console.log("respuesta");
            $scope.canciones = datos.listadecanciones;
            $scope.$apply();
            $(document).ready(function () {
                $('#listadomultimedia').DataTable();
            });
        });*/
        $.ajax({
            url: '/json/' + encodeURI('album'),
            dataType: 'json',
            method: 'get',
            success: function (datos) {
                if (typeof datos.error !== 'undefined' && datos.error === false) {
                    $scope.albumes = datos.mensajes;
                    $scope.$apply();
                    console.log("cargar albumes");
                } else {
                    console.log("error al recibir albumes");
                }
            }
        });
        $.ajax({
            url: '/json/' + encodeURI('lista'),
            dataType: 'json',
            method: 'get',
            success: function (datos) {
                if (typeof datos.error !== 'undefined' && datos.error === false) {
                    $scope.canciones = [];
                    $scope.canciones = datos.listadecanciones;
                    $scope.$apply();
                    $(document).ready(function () {
                        $('#listadomultimedia').DataTable();
                    });

                   /* if ( $.fn.dataTable.isDataTable( '#example' ) ) {
                        table = $('#listadomultimedia').DataTable();
                    }
                    else {
                        table = $('#listadomultimedia').DataTable( {
                            paging: false
                        } );
                    }*/
                    //$('#listadomultimedia').DataTable( {
                    //    autoFill: true
                    //} );
                } else {
                    console.log("error al recibir canciones");
                }
            }
        });
    };
    $scope.btnmostrarrutas = function () {
        $scope.mostrarrutas = !$scope.mostrarrutas;
        $scope.$apply;
    }
    $scope.borrarseleccionadas = function () {

    }

    $scope.cargar();
});

app.controller('sesion', function ($location, $scope, $localStorage, $sessionStorage, $window) {
    $scope.iniciarsesion = function (nombredeusuario) {
        $localStorage.LocalMessage = "iniciada";
        $sessionStorage.SessionMessage = nombredeusuario;
    }
    $scope.cerrarsesion = function () {
        $localStorage.LocalMessage = "";
        $sessionStorage.SessionMessage = "";
        $location.path('/').replace();
    }
    $scope.cargar = function () {
        return $sessionStorage.SessionMessage;
        //$window.alert($localStorage.LocalMessage );
        //+ "\n" + $sessionStorage.SessionMessage);
    }
    $scope.sesion = function () {
        var regresa = ($localStorage.LocalMessage === "iniciada" && $sessionStorage.SessionMessage !== 'undefined');
        return regresa;
    }
    $scope.sesioniniciada = function () {
        console.log($sessionStorage.SessionMessage);
        return $sessionStorage.SessionMessage;
    }
    $scope.obtenerstring = function (enviardatos) {
        $.ajax({
            url: '/json/' + encodeURI(enviardatos),
            dataType: 'json',
            method: 'get',
            success: function (datos) {
                if (typeof datos.error !== 'undefined' && datos.error === false) {
                    return datos.mensaje;
                }
            }
        });
        return null;
    }
});
app.controller("InicioController", function ($scope, $interval, $location) {
    $scope.titulo = "Inicio";

    function usuario(usuario, sha1) {
        this.usuario = usuario;
        this.sha1 = sha1;
    };
    $scope.primerinicio = function () {
        $scope.titulodeformulario = titulodeformulario;
        $scope.botoningresar = botoningresar;
        $scope.crearusuario = crearusuario;
        $.ajax({
            url: '/json/' + encodeURI('sinusuarios'),
            dataType: 'json',
            method: 'get',
            success: function (datos) {
                if (typeof datos.error !== 'undefined' && datos.error === false) {
                    if (datos.mensaje === ' ') {
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

    $scope.Timer = null;
    $scope.yaesta = false;
    $scope.StartTimer = function () {
        $scope.Timer = $interval(function () {
            //console.log('corriendo');
            // $scope.mensaje1 = preguntar('musica');
            // $scope.tiempo = preguntar('tiempo');
            $.ajax({
                url: '/json/' + encodeURI('musica'),
                dataType: 'json',
                method: 'get',
                success: function (datos) {
                    if (typeof datos.error !== 'undefined' && datos.error === false) {
                        $scope.mensaje1 = datos.mensaje;
                    }
                }
            });
            $.ajax({
                url: '/json/' + encodeURI('estado'),
                dataType: 'json',
                method: 'get',
                success: function (datos) {
                    if (typeof datos.error !== 'undefined' && datos.error === false) {
                        $scope.estado = datos.mensaje;
                    }
                }
            });
        }, 1500);

    };
    $scope.loguear = function () {
        var usuario = document.getElementsByName("Usuario")[0].value;
        var contrasena = document.getElementsByName("Contrasena")[0].value;
        if (usuario != null) {
            if (usuario.indexOf(':') > -1) {
                $scope.erroramostrar = "No se acepta : como carácter para crear usuario ";
                return null;
            }
        } else {
            $scope.erroramostrar = "Debe rellenar el formulario: Falta usuario";
            return null;
        }
        if (contrasena != null) {

        } else {
            $scope.erroramostrar = "Debe rellenar el formulario: Falta Contraseña";
            return null;
        }
        $scope.erroramostrar = "";
        if ($scope.titulodeformulario == "Ingrese sus datos") {
            console.log("logueo");
            $.ajax({
                url: '/json/' + encodeURI('LoGuEo:' + usuario + ':' + Sha1.hash(usuario + contrasena)),
                dataType: 'json',
                method: 'get',
                success: function (datos) {
                    if (typeof datos.error !== 'undefined' && datos.error === false) {
                        console.log(datos.mensaje)
                        switch (datos.mensaje) {
                            case 'loguea':
                                $scope.informacionamostrar = "El usuario " + usuario + " se logueo ";
                                $scope.erroramostrar = "";
                                $scope.iniciarsesion(usuario);
                                console.log(sessionStorage.SessionMessage);
                                $location.path('/listado').replace();
                                $scope.$apply();
                                return "logueado";
                                break;
                            default:
                                $scope.erroramostrar = "No se encuentran registros de la información ingresada";
                                $scope.informacionamostrar = "";
                                $scope.$apply();
                                return "error";
                                break;
                        }
                    } else {
                        $scope.erroramostrar = "Error de comunicacion!";
                        $scope.informacionamostrar = "";
                        $scope.$apply();
                        return "error";
                    }
                }
            });
        }
        else {
            console.log("crear usuario")
            console.log('CrEaR:' + usuario + ':' + Sha1.hash(usuario + contrasena));
            $.ajax({
                url: '/json/' + encodeURI('CrEaR:' + usuario + ':' + Sha1.hash(usuario + contrasena)),
                dataType: 'json',
                method: 'get',
                success: function (datos) {
                    if (typeof datos.error !== 'undefined' && datos.error === false) {
                        if (datos.mensaje == "existente") {
                            $scope.erroramostrar = "El usuario " + usuario + " que esta intentando crear ya existe ";
                            $scope.informacionamostrar = "";
                        } else {
                            $scope.informacionamostrar = "Se ha creado la cuenta de " + usuario + " satisfactoriamente";
                            $scope.erroramostrar = "";
                            titulodeformulario = "Ingrese sus datos";
                            crearusuario = true;
                            botoningresar = "Ingresar";
                            $scope.titulodeformulario = titulodeformulario;
                            $scope.botoningresar = botoningresar;
                            $scope.crearusuario = crearusuario;
                        }
                    } else {
                        $scope.erroramostrar = "Hubo errores en la creacion de la cuenta  " + usuario;
                        $scope.informacionamostrar = "";
                    }
                    $scope.$apply
                }
            });
        }
        //alert("loguea:"+ usuarioqueloguea);
    };
    $scope.detenerreproductor = function () {
        console.log("!");
        preguntar('pausar-reproducir');
        yaesta = true;
    };
    $scope.StartTimer();
    // $scope.mensaje1 = "¡¡Hola Mundo!!";
});
var nombre = document.getElementById('nombre');


app.controller('SubirArchivos', function ($scope, $http, $parse, $location) {
    $scope.titulo = "Subir Archivos";
    $scope.copalsa = true;
    $scope.albumes = [];
    $scope.obteneralbumseleccionado = function () {
        return $scope.aalbumasubir;
    }

    $scope.cargaralbums = function () {
        $.ajax({
            url: '/json/' + encodeURI('album'),
            dataType: 'json',
            method: 'get',
            success: function (datos) {
                if (typeof datos.error !== 'undefined' && datos.error === false) {
                    $scope.albumes = datos.mensajes;
                    $scope.$apply();
                    console.log("cargar albumes");
                } else {
                    console.log("error al recibir albumes");
                }
            }
        });
        $scope.cantidadseleccionadas = 0;
        //$scope.$apply();
    };
    $scope.borrarseleccionado = function (file) {
        var i = $scope.files.indexOf(file);
        if (i !== -1) {
            $scope.files.splice(i, 1);
        }
    }

    if (!$scope.sesion()) {
        $location.path('/').replace();
    }
    $scope.name = 'World';
    $scope.files = [];
    $scope.volver = function () {
        $location.path('/listado').replace();
        //$scope.$apply();
    };
    $scope.upload = function (element) {
        var cantidad = 0;
        $scope.subiendoarchivos = true;
        $scope.porcentajesubido = 0;
        $.ajax({
            url: '/json/' + encodeURI('subirarchivos:' + $scope.aalbumasubir),
            dataType: 'json',
            method: 'get',
            success: function (datos) {
                if (typeof datos.error !== 'undefined' && datos.error === false) {
                    $scope.$apply;
                    console.log("intentando subir");
                    console.log($scope.files.length);
                    angular.forEach($scope.files, function (item) {
                        var fd = new FormData();
                        fd.append('file', item);
                        $http.post("/upload/", fd, {
                            transformRequest: angular.identity,
                            headers: {'Content-Type': undefined}
                        }).then(function (success) {
                            cantidad += 1;
                            $scope.porcentajesubido = Math.round((cantidad * 100) / $scope.files.length);
                            if ($scope.porcentajesubido == 100) {
                                $scope.subiendoarchivos = false;
                                $scope.files.length = 0;
                            }
                            $scope.$apply;
                            console.log("subido :D");
                            return success.data.data;
                        });
                    });


                } else {
                    console.log("error al intentar comenzar la subida de archivos");
                }
            }
        });
    };
    $scope.cargaralbums();
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
                    if (item.name.endsWith("mp3")) {
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
                $('#listadearchivosasubir').DataTable();
            })
        }
    }
}]);
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