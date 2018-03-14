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
app.controller("ReproduccionController", function ($scope, $http) {
    if (!$scope.sesion()) {
        $location.path('/').replace();
    }
    $scope.agregarprogramacion = false;
    $scope.verporalbum = true;
    $scope.verporprogramacion = false;
    $scope.abriralbum = function (reproduccion) {
        $('#agregarprogramacion').modal('show');
        $scope.ver = reproduccion;
        $scope.horahasta = new Date(1970, 0, 1, 23, 59, 0);
        $scope.horadesde = new Date(1970, 0, 1, 0, 0, 0);
    };
    $scope.dia=function (diadesemana) {
        switch (diadesemana) {
            case 1:
                return 'Lunes';
                break;
            case 2:
                return 'Martes';
                break;
            case 3:
                return 'Miércoles';
                break;
            case 4:
                return 'Jueves';
                break;
            case 5:
                return 'Viernes';
                break;
            case 6:
                return 'Sábado';
                break;
            case 7:
                return 'Domingo';
                break;
            default :
                return '';
        }
    };
    $scope.rango = function(min, max, step) {
        step = step || 1;
        var input = [];
        for (var i = min; i <= max; i += step) {
            input.push(i);
        }
        return input;
    };
    $scope.enviar=function (nombre, lunes, martes, miercoles, jueves, viernes, sabado, domingo, horadesde, horahasta) {
        var reproduccion = {
            nombre: nombre,
            lunes: lunes,
            martes: martes,
            miercoles: miercoles,
            jueves: jueves,
            viernes: viernes,
            sabado: sabado,
            domingo: domingo,
            horadesde: horadesde,
            horahasta: horahasta
        };
        $http({
            url: '/json/',
            dataType: 'json',
            method: 'post',
            data: JSON.stringify(reproduccion),
            contentType: 'application/json; charset=utf-8'}).then(function () {
            $scope.cargar();
        });
    };
    $scope.verificardia = function (lista, dia) {
        for (var i = 0; i < lista.length; i++) {
            if (lista[i] == dia)
                return true;
        }
        return false;
    };
    $scope.poralbum = function () {
        $scope.verporalbum = true;
        $scope.verporprogramacion = false;
    };
    $scope.porprogramacion = function () {
        $scope.verporalbum = false;
        $scope.verporprogramacion = true;
    };
    $scope.datosdereproduccion = [];
    $scope.agregaralbumareproduccion = function (reproduccion, album) {
        $http.get('/json/' + encodeURI('agregaralbum:' + reproduccion + ":" + album)).then(function (response) {
            $scope.cargar();
        });
        /*$.ajax({
            url: '/json/' + encodeURI('agregaralbum:' + reproduccion + ":"+ album),
            dataType: 'json',
            method: 'get',
            success: function (datos) {
                if (typeof datos.error !== 'undefined' && datos.error === false) {
                    $scope.cargar();
                }
            }
        });*/
    };
    $scope.deshabilitar = function (nombre) {
        $http.get('/json/' + encodeURI('deshabilitarreproduccion:' + nombre)).then(function (response) {
            $scope.cargar();
        });
    };
    $scope.haceralbummaestro = function (nombre) {

        $http.get('/json/' + encodeURI('hacerreproduccionmaestra:' + nombre)).then(function (response) {
            $scope.cargar();
        });
    };
    $scope.deshaceralbummaestro = function (nombre) {
        $http.get('/json/' + encodeURI('deshacerreproduccionmaestra:' + nombre)).then(function (response) {
            $scope.cargar();
        });
    };
    $scope.eliminarreproduccion = function (nombre) {
        $http.get('/json/' + encodeURI('eliminarreproduccion:' + nombre)).then(function (response) {
            $scope.cargar();
        });
    };
    $scope.habilitar = function (nombre) {
        $http.get('/json/' + encodeURI('habilitarreproduccion:' + nombre)).then(function (response) {
            $scope.cargar();
        });
    };
    $scope.quitaralbumde = function (reproduccion, album) {
        $http.get('/json/' + encodeURI('quitaralbum:' + reproduccion + ":" + album)).then(function (response) {
            $scope.cargar();
        });
    };
    $scope.cargar = function () {
        $http.get('/json/' + encodeURI('reproduccion')).then(function (response) {
            $scope.datosdereproduccion = [];
            $scope.datosdereproduccion = response.data.datosdereproduccion;
        });
    };
    $scope.crearreproduccion = function (nombredereproduccionnueva) {
        $http.get('/json/' + encodeURI('nuevareproduccion:' + nombredereproduccionnueva)).then(function (response) {
            $scope.cargar();
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
/*app.directive('verificadordefechasdesde', function () {
    return {
        require: ['ngModel', '^form'],
        link: function(scope, element, attrs, ctrls) {
        console.log(ctrls);
        function myValidation(value) {
            console.log(value);
        }
        if (value.getTime() < (ctrls.horahasta.getTime())) {
            ctrls.horadesde.$setValidity(true);
        } else {
            ctrls.horadesde.$setValidity(false);
        }
        return value;
    }
    ctrls.horadesde.$parsers.push(myValidation);

    }*/
     /*   require: 'ngModel',
    link: function (scope, element, attr, mCtrl) {
        console.log(mCtrl);
        function myValidation(value) {
            if (value.getTime() < (horahasta.getTime())) {
                mCtrl.$setValidity(true);
            } else {
                mCtrl.$setValidity(false);
            }
            return value;
        }
        mCtrl.$parsers.push(myValidation);
    }
}

})
;*/
/*app.directive('Verificadordefechashasta', function () {
    return {
        require: 'ngModel',
        link: function (scope, element, attr, m) {

        }
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
app.controller("AlbumsController", function ($http, $scope, $interval, $location) {
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
        $http.get('/json/' + encodeURI('album')).then(function (response) {
            $scope.albumes = response.data.mensajes;
        });
        $scope.cantidadseleccionadas = 0;
    };
    $scope.editaroagregaralbum = function (nombre, editaroagregar, nombrenuevo) {
        if (editaroagregar === "Agregar album") {
            $http.get('/json/' + encodeURI('crearalbum:' + nombre)).then(function (response) {
                if (response.data.mensaje == "existente") {
                    $scope.mensaje("Existe un album con ese nombre");
                } else
                    $('#editaroagregaralbum').collapse('hide');
                $scope.cargar();
            });
        } else {
            $http.get('/json/' + encodeURI('editaralbum:' + nombrenuevo + ":" + nombre)).then(function (response) {
                if (response.data.mensaje == "existente") {
                    $scope.mensaje("Existe un album con ese nombre");
                } else
                    $('#editaroagregaralbum').collapse('hide');
                $scope.cargar();
            });
        }
        ;
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
                $http.get('/json/' + encodeURI('eliminaralbum:' + $scope.albumes[i].dato)).then(function (response) {
                    $scope.cargar();
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
    };
    $scope.desseleccionar = function (albumes) {
        albumes.seleccionado = false;
        $scope.verificarcantidad();
        $scope.$apply;
    };
    $scope.cargar();
})
;
app.controller("ListadoController", ['$scope', '$http', '$interval', '$location', function ($scope, $http, $interval, $location) {
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
                $http.get('/json/' + encodeURI('habilitar:' + $scope.canciones[i].nombre));
                console.log('habilitar');
            }
        }
        $scope.cargar;
    }
    $scope.anularseleccionados = function () {
        for (var i = 0; i < $scope.canciones.length; i++) {
            if ($scope.canciones[i].seleccionado) {
                $http.get('/json/' + encodeURI('anular:' + $scope.canciones[i].nombre));
                console.log('anular');
            }
        }
        $scope.cargar;
    }
    $scope.borrarseleccionados = function () {
        for (var i = 0; i < $scope.canciones.length; i++) {
            if ($scope.canciones[i].seleccionado) {
                $http.get('/json/' + encodeURI('borrar:' + $scope.canciones[i].nombre));
            }
        }
        $scope.cargar;
    }
    $scope.cambiaralbum = function (albumnombre) {
        for (var i = 0; i < $scope.canciones.length; i++) {
            if ($scope.canciones[i].seleccionado) {
                $http.get('/json/' + encodeURI('cambiaralbum:' + $scope.canciones[i].nombre + ':' + albumnombre));
            }
        }
        $scope.cargar;
    };
    $scope.cargar = function () {
        console.log("ejecutando listado");
        $http.get('/json/' + encodeURI('album')).then(function (response) {
            $scope.albumes = response.data.mensajes;
        });
        $http.get('/json/' + encodeURI('lista')).then(function (response) {
            $scope.canciones = [];
            $scope.canciones = response.data.listadecanciones;
            $(document).ready(function () {
                $('#listadomultimedia').DataTable();
            });
        });
    };
    $scope.btnmostrarrutas = function () {
        $scope.mostrarrutas = !$scope.mostrarrutas;
        $scope.$apply;
    };
    $scope.borrarseleccionadas = function () {

    };

    $scope.cargar();
}]);
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
        //console.log($sessionStorage.SessionMessage);
        if (typeof $sessionStorage.SessionMessage !== 'undefined')
            $sessionStorage.SessionMessage = '';
        return $sessionStorage.SessionMessage;
    }
    $scope.obtenerstring = function (enviardatos) {
        $http.get('/json/' + encodeURI(enviardatos)).then(function (response) {
            return response.data.mensaje;
        });
        return null;
    }
});
app.controller("InicioController", function ($http, $scope, $interval, $location) {
    $scope.titulo = "Inicio";

    function usuario(usuario, sha1) {
        this.usuario = usuario;
        this.sha1 = sha1;
    };
    $scope.primerinicio = function () {
        $scope.titulodeformulario = titulodeformulario;
        $scope.botoningresar = botoningresar;
        $scope.crearusuario = crearusuario;
        $http.get('/json/' + encodeURI('sinusuarios')).then(function (response) {
            if (response.data.mensaje === ' ') {
                titulodeformulario = "No hay usuario creado por favor ingrese los datos correspondientes";
                crearusuario = true;
                botoningresar = "Crear usuario";
                $scope.titulodeformulario = titulodeformulario;
                $scope.botoningresar = botoningresar;
                $scope.crearusuario = crearusuario;
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
            $http.get('/json/' + encodeURI('musica')).then(function (response) {
                $scope.mensaje1 = response.data.mensaje;
            });
            $http.get('/json/' + encodeURI('estado')).then(function (response) {
                $scope.estado = response.data.mensaje;
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
            $http.get('/json/' + encodeURI('LoGuEo:' + usuario + ':' + Sha1.hash(usuario + contrasena))).then(function (response) {
                    switch (response.data.mensaje) {
                        case 'loguea':
                            $scope.informacionamostrar = "El usuario " + usuario + " se logueo ";
                            $scope.erroramostrar = "";
                            $scope.iniciarsesion(usuario);
                            console.log(sessionStorage.SessionMessage);
                            $location.path('/listado').replace();
                            return "logueado";
                            break;
                        default:
                            $scope.erroramostrar = "No se encuentran registros de la información ingresada";
                            $scope.informacionamostrar = "";
                            return "error";
                            break;
                    }
                },
                function (data) {
                    $scope.erroramostrar = "Error de comunicacion!";
                    $scope.informacionamostrar = "";
                    return "error";
                });
        }
        else {
            console.log("crear usuario")
            console.log('CrEaR:' + usuario + ':' + Sha1.hash(usuario + contrasena));
            $http.get('/json/' + encodeURI('CrEaR:' + usuario + ':' + Sha1.hash(usuario + contrasena))).then(function (response) {
                    if (response.data.mensaje == "existente") {
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
                },
                function (data) {
                    $scope.erroramostrar = "Hubo errores en la creacion de la cuenta  " + usuario;
                    $scope.informacionamostrar = "";
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
    $scope.miestilo = "{}";
    $scope.albumes = [];
    $scope.obteneralbumseleccionado = function () {
        return $scope.aalbumasubir;
    }

    $scope.cargaralbums = function () {
        $http.get('/json/' + encodeURI('album')).then(function (response) {
            $scope.albumes = response.data.mensajes;
        });
        $scope.cantidadseleccionadas = 0;
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
        $http.get('/json/' + encodeURI('subirarchivos:' + $scope.aalbumasubir)).then(function (response) {
            $('#mensajeaporcentaje').modal('show');
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
                    $scope.miestilo = {
                        "width": $scope.porcentajesubido + "%"
                    };
                    if ($scope.porcentajesubido == 100) {
                        $('#mensajeaporcentaje').modal('hide');
                        $scope.subiendoarchivos = false;
                        $scope.files.length = 0;
                    }
                    $scope.$apply;
                    console.log("subido :D");
                    return success.data.data;
                });
            });
        });
    }
    $scope.cargaralbums();
});

app.directive('ngFileModel', ['$parse', function ($parse) {
    console.log("controlando");
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
