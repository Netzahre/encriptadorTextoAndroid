package com.example.encriptador_de_texto_eas

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.ceil

class mainActivity

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
        val spinner = findViewById<Spinner>(R.id.tiposCifrado)
        val opcionesEncriptado = arrayOf("Sustitucion", "XOR", "Columna")
        val adaptador = ArrayAdapter<String>(this, R.layout.spinner, opcionesEncriptado)
        spinner.adapter = adaptador
        val cifrar = findViewById<Button>(R.id.cifrar)
        val descifrar = findViewById<Button>(R.id.descifrar)
        val capturarTexto = findViewById<TextView>(R.id.capturarTexto)
        val actividad2 = Intent(this, MostrarCifrado::class.java)
        cifrar.setOnClickListener {
            if (spinner.selectedItem.toString() == "Sustitucion") {
                val textoEncriptado = encriptadoSustitucion(capturarTexto.text.toString())
                actividad2.putExtra("texto", textoEncriptado)
                startActivity(actividad2)
            }
            if (spinner.selectedItem.toString() == "XOR") {
                val textoEncriptado = encriptadoXor(capturarTexto.text.toString())
                actividad2.putExtra("texto", textoEncriptado)
                startActivity(actividad2)
            }
            if (spinner.selectedItem.toString() == "Columna") {
                val textoEncriptado = encriptadoColumnas(capturarTexto.text.toString())
                actividad2.putExtra("texto", textoEncriptado)
                startActivity(actividad2)
            }
        }
        descifrar.setOnClickListener {
            if (spinner.selectedItem.toString() == "Sustitucion") {
                val textoDesencriptado = desencriptarSustitucion(capturarTexto.text.toString())
                actividad2.putExtra("texto", textoDesencriptado)
                startActivity(actividad2)
            }
            if (spinner.selectedItem.toString() == "XOR") {
                val textoDesencriptado = desencriptadoXor(capturarTexto.text.toString())
                actividad2.putExtra("texto", textoDesencriptado)
                startActivity(actividad2)
            }
            if (spinner.selectedItem.toString() == "Columna") {
                val textoDesencriptado = desencriptadoColumnas(capturarTexto.text.toString())
                actividad2.putExtra("texto", textoDesencriptado)
                startActivity(actividad2)
            }
        }
    }
}

fun encriptadoSustitucion(frase: String): String {
    val clave = "rèvyábôslf¿gíüujxò!êkhdqúz¡âónwañcùpà?métieìo"
    val alfabeto = "abcdefghijklmñnopqrstuvwxyzáàâéèêíìóòôúùü!¿?¡"
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

fun desencriptarSustitucion(frase: String): String {
    val clave = "rèvyábôslf¿gíüujxò!êkhdqúz¡âónwañcùpà?métieìo"
    val alfabeto = "abcdefghijklmñnopqrstuvwxyzáàâéèêíìóòôúùü!¿?¡"
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

fun encriptadoXor(frase: String): String {
    val clave = "4L3j4nDr0CoRrEa"
    var fraseEncriptada = ""
    for (i in frase.indices) {
        val letra = frase[i]
        val letraClave = clave[i % clave.length]
        // Add an offset to ensure the result is within a printable range
        val letraEncriptada = (letra.code xor letraClave.code + 128).toChar()
        fraseEncriptada += letraEncriptada
    }
    return fraseEncriptada
}

fun desencriptadoXor(fraseEncriptada: String): String {
    val clave = "4L3j4nDr0CoRrEa"
    var fraseDesencriptada = ""
    for (i in fraseEncriptada.indices) {
        val letraEncriptada = fraseEncriptada[i]
        val letraClave = clave[i % clave.length]
        // Reverse the offset applied during encryption
        val letraOriginal = (letraEncriptada.code xor letraClave.code - 128).toChar()
        fraseDesencriptada += letraOriginal
    }
    return fraseDesencriptada
}

fun encriptadoColumnas(frase: String): String {
    val textoSinEspacios = frase.replace(" ", "*")
    val longitudTexto = textoSinEspacios.length
    val columnas = textoSinEspacios.length / 2
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

fun desencriptadoColumnas(frase: String): String {
    val columnas = frase.length / 2
    val filas = ceil(frase.length / columnas.toDouble()).toInt()
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