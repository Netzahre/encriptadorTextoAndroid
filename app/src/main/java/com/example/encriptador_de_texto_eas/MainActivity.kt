package com.example.encriptador_de_texto_eas

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}

fun encriptadoSustitucion(frase: String, clave: String): String {
    val alfabeto = "abcdefghijklmnopqrstuvwxyz"
    var fraseEncriptada = ""
    for (i in frase.indices) {
        val letra = frase[i]
        val posicion = alfabeto.indexOf(letra)
        if (posicion != -1) {
            fraseEncriptada += clave[posicion]
        } else {
            fraseEncriptada += letra
        }
    }
    return fraseEncriptada
}

fun desencriptarSustitucion(frase: String, clave: String): String {
    val alfabeto = "abcdefghijklmnopqrstuvwxyz"
    var fraseDesencriptada = ""
    for (i in frase.indices) {
        val letra = frase[i]
        val posicion = clave.indexOf(letra)
        if (posicion != -1) {
            fraseDesencriptada += alfabeto[posicion]
        } else {
            fraseDesencriptada += letra
        }
    }
    return fraseDesencriptada
}

fun encriptadoXor(frase: String, clave: String): String {
    var fraseEncriptada = ""
    for (i in frase.indices) {
        val letra = frase[i]
        val letraClave = clave[i % clave.length]
        val letraEncriptada = (letra.code xor letraClave.code).toChar()
        fraseEncriptada += letraEncriptada
    }
    return fraseEncriptada
}

fun encriptadoColumnas(frase: String, columnas: Int): String {
    val textoSinEspacios = frase.replace(" ", "*")
    val longitudTexto = textoSinEspacios.length
    val filas = ceil(longitudTexto / columnas.toDouble()).toInt()
    val matriz = Array(filas) { CharArray(columnas) }
    var contador = 0

    for (i in matriz.indices) {
        for (j in matriz[i].indices) {
            if (contador < longitudTexto) {
                matriz[i][j] = textoSinEspacios[contador]
                contador++
            } else {
                matriz[i][j] = '*'
            }
        }
    }

    var textoCifrado = ""
    for (i in matriz[0].indices) {
        for (j in matriz.indices) {
            textoCifrado += matriz[j][i]
        }
    }
    return textoCifrado
}

fun desencriptadoColumnas(frase: String, columnas: Int): String {
    val filas = round(frase.length / columnas.toDouble()).toInt()
    val matriz = Array(filas) { CharArray(columnas) }
    var contador = 0
    for (i in matriz[0].indices) {
        for (j in matriz.indices) {
            if (contador < frase.length) {
                matriz[j][i] = frase[contador]
                contador++
            }
        }
    }
    var fraseDescifrada = ""
    for (i in matriz.indices) {
        for (j in matriz[i].indices) {
            if (matriz[i][j] != '*') {
                fraseDescifrada += matriz[i][j]
            } else if (matriz[i][j] == '*') {
                fraseDescifrada += " "
            }
        }
    }
    for (i in fraseDescifrada.indices) {
        if (fraseDescifrada[i] == ' ' && fraseDescifrada[i + 1] == ' ') {
            fraseDescifrada = fraseDescifrada.substring(0, i)
            break
        }
    }
    return fraseDescifrada
}