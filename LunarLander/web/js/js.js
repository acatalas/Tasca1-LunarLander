/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// Variables Globales
var pagina = "";
var menuVisible = true;
var aterrizadoPausado = true;
var modoDificil = false;
var fuelOut = false;
var gravedad = 4 * 1.622;
var dt = 0.016683;
var temporizador = null;
var temporizadorFuel = null;
var limiteVelocidad = 10;

// Variables Nave
var coloresNave = ["rojo", "blanco"];
var colorNave = "rojo";
var valorAltura = 5;
var valorVelocidad = 0;
var valorFuel = 100;
var aceleracion = gravedad;

// Variables Atajos Marcadores
var indicadorAltura = null;
var indicadorVelocidadD = null;
var indicadorVelocidadM = null;
var indicadorFuel = null;

// Variables Convertidas para Marcadores
var valorRotacion = 43;
var medidaRotacion = "rotate(" + valorRotacion + "deg)";
var medidaAltura = valorAltura + "%";
var medidaFuel = valorFuel + "%";

// Variables Atajos Colores
var naranja = "rgb(225, 75, 0)";
var azul = "rgb(0, 167, 208)";

leerConfigJSON();

// Eventos
window.onload = function () {
    
    //Atajos botones del menu
    btMenu = document.getElementById("btMenu");
    btAbout = document.getElementById("btAbout");
    btInstrucciones = document.getElementById("btInstrucciones");
    
    //Atajos botones de cancelar
    btCancelInstrucciones = document.getElementById("btCancelInstrucciones");
    btCancelAbout = document.getElementById("btCancelAbout");
    
    //Atajos botones de dificultad
    btFacil = document.getElementById("btFacil");
    btDificil = document.getElementById("btDificil");
    
    //Atajos popup
    ppInstrucciones = document.getElementById("instrucciones");
    ppAbout = document.getElementById("about");
    ppLoadConfig = document.getElementById("loadConfigurations");
    ppSaveConfig = document.getElementById("saveConfigurations");
    
    //Atajos botones del fin del juego
    btResPositivo = document.getElementById("btResPositivo");
    btResNegativo = document.getElementById("btResNegativo");
    
    //Atajo del boton de restart
    btRestart = document.getElementById("btRestart");
    
    // Atajos indicadores
    indicadorAltura = document.getElementById("fondoAltura");
    indicadorVelocidadD = document.getElementById("imgAguja");
    indicadorVelocidadM = document.getElementById("velM");
    indicadorFuel = document.getElementById("fondoFuel");

    //Escoge la nave
    document.getElementById("nave").src = "img/" + colorNave + ".png";

    //Comprobacion de el modo dificil
    if (modoDificil) {
        btDificil.style.backgroundColor = naranja;
        btDificil.style.color = "white";
        document.getElementById("velD").src = "img/indicadorVelocidadPcDificil.png";
        valorFuel = 50;
    } else {
        btFacil.style.backgroundColor = naranja;
        btFacil.style.color = "white";
        document.getElementById("velD").src = "img/indicadorVelocidadPcFacil.png";
        valorFuel = 100;
    }
    
    //Actualiza el indicador del fuel
    medidaFuel = valorFuel + "%";
    indicadorFuel.style.height = medidaFuel;
    indicadorFuel.style.width = medidaFuel;

    //Boton nave
    document.getElementById("nave").onclick = function () {
        let color = prompt("Elige el color de la nave", "Rojo");
        if (color !== null) {
            if (checkColor(color) === true){
                document.getElementById("nave").src = "img/" + color.toLowerCase() + ".png";
                colorNave = color.toLowerCase();
                guardarConfigJSON();
            } else {
                alert("El color introducido es invalido. Escoge uno de los siguientes colores:\n"
                        + coloresNave);
            }
        }
    };

    // Botón menú
    btMenu.onclick = function () {
        if (menuVisible) {
            document.getElementById("menu").style.display = "none";
            btMenu.src = "img/botonMenuPause.png";
            menuVisible = false;
            aterrizadoPausado = false;
            start();
        } else {
            document.getElementById("menu").style.display = "block";
            btMenu.src = "img/botonMenuPlay.png";
            menuVisible = true;
            aterrizadoPausado = true;
            stop();
        }
    };

    // Botones de reinicio
    btRestart.onclick = function () {
        restart();
    };
    btResPositivo.onclick = function () {
        restart();
    };
    btResNegativo.onclick = function () {
        restart();
    };

    // Boton de About
    btAbout.onclick = function () {
        ppAbout.style.display = "block";
    };

    //Boton de Instrucciones
    btInstrucciones.onclick = function () {
        ppInstrucciones.style.display = "block";
    };

    //Botones de "Volver atras" del menu
    btCancelAbout.onclick = function () {
        ppAbout.style.display = "none";
    };
    
    btCancelInstrucciones.onclick = function () {
        ppInstrucciones.style.display = "none";
    };


    //Botones de dificultad (dentro del menú)
    //Cambiar a modo facil al clicar en el boton de "Fàcil"
    btFacil.onclick = function () {
        modoDificil = false;
        limiteVelocidad = 10;
        btFacil.style.backgroundColor = naranja;
        btFacil.style.color = "white";
        btDificil.style.backgroundColor = azul;
        btDificil.style.color = "black";
        document.getElementById("velD").src = "img/indicadorVelocidadPcFacil.png";
        guardarConfigJSON();
        restart();
    };

    //Cambiar a modo dificil al clicar en el boton de "Dificil"
    btDificil.onclick = function () {
        modoDificil = true;
        limiteVelocidad = 5;
        btDificil.style.backgroundColor = naranja;
        btDificil.style.color = "white";
        btFacil.style.backgroundColor = azul;
        btFacil.style.color = "black";
        document.getElementById("velD").src = "img/indicadorVelocidadPcDificil.png";
        guardarConfigJSON();
        restart();
    };

    // Encender/Apagar el motor al mantener pulsada la luna (Mouse)
    document.getElementById("luna").onmousedown = motorOn;
    document.getElementById("luna").onmouseup = motorOff;

    // Encender/Apagar el motor al mantener pulsada la luna (Touch)
    document.getElementById("luna").oncontextmenu = function (e) {
        e.preventDefault();
        e.stopPropagation();
        return false;
    };
    document.getElementById("luna").addEventListener("touchstart", function (e) {
        motorOn();
    });
    document.getElementById("luna").addEventListener("touchend", function (e) {
        motorOff();
    });
};

// Funciones
function recordatorioDificultad() {
    if (modoDificil) {
        document.getElementById("dificultad").innerHTML = "Difícil";
    } else {
        document.getElementById("dificultad").innerHTML = "Fácil";
    }
    setTimeout(function () {
        document.getElementById("dificultad").innerHTML = "&nbsp;";
    }, 1000);
}

function start() {
    recordatorioDificultad();
    temporizador = setInterval(function () {
        moverNave();
    }, dt * 1000);
}

function guardarConfigJSON() {
    var url = "SaveConfigFile";
    var emess = "Error desconocido";
    console.log(colorNave);
    $.ajax({
        method: "POST",
        url: url,
        data: {modoDificil: modoDificil, nave: colorNave},
        success: function (u) {
            alert("Configuracion guardada con exito");
        },
        error: function (e) {
            if (e["responseJSON"] === undefined) {
                alert(emess);
            } else {
                alert(e["responseJSON"]["error"]);
            }
        }
    });
}

function leerConfigJSON() {
    var url = "ReadConfigFile";
    var emess = "Error desconocido";
    $.ajax({
        url: url,
        dataType: 'json',
        success: function (jsn) {
            modoDificil = jsn.modoDificil;
            colorNave = jsn.nave;
        },
        error: function (e) {
            if (e["responseJSON"] === undefined)
                alert(emess);
            else
                alert(e["responseJSON"]["error"]);
        }
    });
}

function stop() {
    clearInterval(temporizador);
}

function moverNave() {

    // Cambian los Valores de Velocidad y Altura
    valorVelocidad += aceleracion * dt;
    valorAltura += valorVelocidad * dt;

    // Se actualiza la Velocidad en Escritorio
    valorRotacion = 43 + Math.abs(valorVelocidad * 9.25);
    if (valorRotacion > 312) {
        valorRotacion = 312;
    }
    medidaRotacion = "rotate(" + valorRotacion + "deg)";
    indicadorVelocidadD.style.transform = medidaRotacion;

    // Se actualiza la Velocidad en Movil
    if (Math.abs(valorVelocidad) <= 5) {
        document.getElementById("velM").src = "img/velocidadVerde.png";
    } else if (Math.abs(valorVelocidad) <= 10) {
        document.getElementById("velM").src = "img/velocidadAmarillo.png";
    } else {
        document.getElementById("velM").src = "img/velocidadRojo.png";
    }

    // Se actualiza la Altura
    if (valorAltura > 0) {
        medidaAltura = ((97 * valorAltura) / 70) + "%";
    } else {
        medidaAltura = "1%";
    }
    indicadorAltura.style.top = medidaAltura;

    // Aterrizar la Nave al llegar a la luna
    if (valorAltura < 70) {
        document.getElementById("divCentral").style.top = valorAltura + "%";
    } else {
        if (valorVelocidad > limiteVelocidad) {
            document.getElementById("nave").src = "img/" + colorNave + "Explosion.png";
            var cambiar = document.getElementById("textResNegativo").innerHTML.replace("xxx", Math.round(valorVelocidad));
            document.getElementById("textResNegativo").innerHTML = cambiar;
            document.getElementById("resNegativo").style.display = "block";
        } else {
            document.getElementById("nave").src = "img/" + colorNave + ".png";
            var cambiar = document.getElementById("textResPositivo").innerHTML.replace("xxx", Math.round(valorVelocidad));
            document.getElementById("textResPositivo").innerHTML = cambiar;
            document.getElementById("resPositivo").style.display = "block";
        }
        aterrizadoPausado = true;
        stop();
    }
}
function checkColor(color) {
    for (i = 0; i < coloresNave.length; i++) {
        if (coloresNave[i] === color.toLowerCase()) {
            return true;
        }
    }
    return false;
}

function motorOn() {
    if (aterrizadoPausado || fuelOut) {
        motorOff();
    } else {
        aceleracion = -gravedad;
        if (temporizadorFuel === null) {
            temporizadorFuel = setInterval(function () {
                actualizarFuel();
            }, 10);
            document.getElementById("nave").src = "img/" + colorNave + "FuegoGif.gif";
        }
    }
}

function motorOff() {
    aceleracion = gravedad;
    clearInterval(temporizadorFuel);
    temporizadorFuel = null;
    if (!aterrizadoPausado) {
        document.getElementById("nave").src = "img/" + colorNave + ".png";
    }
}

function actualizarFuel() {
    valorFuel -= 0.1;
    if (valorFuel < 0) {
        valorFuel = 0;
        fuelOut = true;
        motorOff();
    }
    medidaFuel = valorFuel + "%";
    indicadorFuel.style.height = medidaFuel;
    indicadorFuel.style.width = medidaFuel;
}

function restart() {
    clearInterval(temporizador);
    // Reseteo de imágenes y variables
    document.getElementById("resPositivo").style.display = "none";
    document.getElementById("resNegativo").style.display = "none";
    document.getElementById("menu").style.display = "none";
    document.getElementById("btMenu").src = "img/botonMenuPause.png";
    document.getElementById("nave").src = "img/" + colorNave + ".png";
    menuVisible = false;
    aterrizadoPausado = false;
    fuelOut = false;
    temporizador = null;
    temporizadorFuel = null;
    aceleracion = gravedad;
    // Reseteo de marcadores
    valorAltura = 5;
    valorVelocidad = 0;
    if (modoDificil) {
        valorFuel = 50;
    } else {
        valorFuel = 100;
    }
    // Actualización de marcadores
    document.getElementById("divCentral").style.top = valorAltura + "%";
    medidaAltura = ((97 * valorAltura) / 70) + "%";
    indicadorAltura.style.top = medidaAltura;
    valorRotacion = 43 + Math.abs(valorVelocidad * 9.25);
    medidaRotacion = "rotate(" + valorRotacion + "deg)";
    indicadorVelocidadD.style.transform = medidaRotacion;
    document.getElementById("velM").src = "img/velocidadVerde.png";
    medidaFuel = valorFuel + "%";
    indicadorFuel.style.height = medidaFuel;
    indicadorFuel.style.width = medidaFuel;
    // Empezamos de nuevo
    start();
}


