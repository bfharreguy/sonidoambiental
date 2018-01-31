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
function loguear(){
    alert("anda");
    var usuario = document.getElementById('Usuario');  
    var contrasena = document.getElementById('contrasena');  
    alert(sha1(usuario + contrasena));  
};
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
app.controller("InicioController", function ($scope, $interval) {
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
    $scope.detenerreproductor=function(){
        console.log("!");
        preguntar('pausar-reproducir');   
        yaesta=true;
    };
    $scope.StartTimer();    
    // $scope.mensaje1 = "¡¡Hola Mundo!!";*/
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
function SHA1(msg) {
    function rotate_left(n,s) {
        var t4 = ( n<<s ) | (n>>>(32-s));
        return t4;
    };
    function lsb_hex(val) {
        var str="";
        var i;
        var vh;
        var vl;
        for( i=0; i<=6; i+=2 ) {
            vh = (val>>>(i*4+4))&0x0f;
            vl = (val>>>(i*4))&0x0f;
            str += vh.toString(16) + vl.toString(16);
        }
        return str;
    };
    function cvt_hex(val) {
        var str="";
        var i;
        var v;
        for( i=7; i>=0; i-- ) {
            v = (val>>>(i*4))&0x0f;
            str += v.toString(16);
        }
        return str;
    };
    function Utf8Encode(string) {
        string = string.replace(/\r\n/g,"\n");
        var utftext = "";
        for (var n = 0; n < string.length; n++) {
            var c = string.charCodeAt(n);
            if (c < 128) {
                utftext += String.fromCharCode(c);
            }
            else if((c > 127) && (c < 2048)) {
                utftext += String.fromCharCode((c >> 6) | 192);
                utftext += String.fromCharCode((c & 63) | 128);
            }
            else {
                utftext += String.fromCharCode((c >> 12) | 224);
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                utftext += String.fromCharCode((c & 63) | 128);
            }
        }
        return utftext;
    };
    var blockstart;
    var i, j;
    var W = new Array(80);
    var H0 = 0x67452301;
    var H1 = 0xEFCDAB89;
    var H2 = 0x98BADCFE;
    var H3 = 0x10325476;
    var H4 = 0xC3D2E1F0;
    var A, B, C, D, E;
    var temp;
    msg = Utf8Encode(msg);
    var msg_len = msg.length;
    var word_array = new Array();
    for( i=0; i<msg_len-3; i+=4 ) {
        j = msg.charCodeAt(i)<<24 | msg.charCodeAt(i+1)<<16 |
                msg.charCodeAt(i+2)<<8 | msg.charCodeAt(i+3);
        word_array.push( j );
    }
    switch( msg_len % 4 ) {
        case 0:
            i = 0x080000000;
            break;
        case 1:
            i = msg.charCodeAt(msg_len-1)<<24 | 0x0800000;
            break;
        case 2:
            i = msg.charCodeAt(msg_len-2)<<24 | msg.charCodeAt(msg_len-1)<<16 | 0x08000;
            break;
        case 3:
            i = msg.charCodeAt(msg_len-3)<<24 | msg.charCodeAt(msg_len-2)<<16 | msg.charCodeAt(msg_len-1)<<8  | 0x80;
            break;
    }
    word_array.push( i );
    while( (word_array.length % 16) != 14 ) word_array.push( 0 );
    word_array.push( msg_len>>>29 );
    word_array.push( (msg_len<<3)&0x0ffffffff );
    for ( blockstart=0; blockstart<word_array.length; blockstart+=16 ) {
        for( i=0; i<16; i++ ) W[i] = word_array[blockstart+i];
        for( i=16; i<=79; i++ ) W[i] = rotate_left(W[i-3] ^ W[i-8] ^ W[i-14] ^ W[i-16], 1);
        A = H0;
        B = H1;
        C = H2;
        D = H3;
        E = H4;
        for( i= 0; i<=19; i++ ) {
            temp = (rotate_left(A,5) + ((B&C) | (~B&D)) + E + W[i] + 0x5A827999) & 0x0ffffffff;
            E = D;
            D = C;
            C = rotate_left(B,30);
            B = A;
            A = temp;
        }
        for( i=20; i<=39; i++ ) {
            temp = (rotate_left(A,5) + (B ^ C ^ D) + E + W[i] + 0x6ED9EBA1) & 0x0ffffffff;
            E = D;
            D = C;
            C = rotate_left(B,30);
            B = A;
            A = temp;
        }
        for( i=40; i<=59; i++ ) {
            temp = (rotate_left(A,5) + ((B&C) | (B&D) | (C&D)) + E + W[i] + 0x8F1BBCDC) & 0x0ffffffff;
            E = D;
            D = C;
            C = rotate_left(B,30);
            B = A;
            A = temp;
        }
        for( i=60; i<=79; i++ ) {
            temp = (rotate_left(A,5) + (B ^ C ^ D) + E + W[i] + 0xCA62C1D6) & 0x0ffffffff;
            E = D;
            D = C;
            C = rotate_left(B,30);
            B = A;
            A = temp;
        }
        H0 = (H0 + A) & 0x0ffffffff;
        H1 = (H1 + B) & 0x0ffffffff;
        H2 = (H2 + C) & 0x0ffffffff;
        H3 = (H3 + D) & 0x0ffffffff;
        H4 = (H4 + E) & 0x0ffffffff;
    }
    var temp = cvt_hex(H0) + cvt_hex(H1) + cvt_hex(H2) + cvt_hex(H3) + cvt_hex(H4);
    
    return temp.toLowerCase();
}