package com.example.encriptador_de_texto_eas

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MostrarCifrado : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mostrar_cifrado)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val bundle = intent.extras
        val texto = bundle?.getString("texto")
        val mostrar = findViewById<TextView>(R.id.mostrarTexto)
        val salir = findViewById<Button>(R.id.salir)

        mostrar.setText(texto)

        salir.setOnClickListener {
            //Segun la documentacion esto no es incorrecto, al ser la activity lanzada desde otra
            //esto simplemente te devuelve a la activity original.
            finish()
        }

    }
}