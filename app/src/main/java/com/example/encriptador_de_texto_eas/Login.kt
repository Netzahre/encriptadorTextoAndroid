package com.example.encriptador_de_texto_eas

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val textoUser = findViewById<TextView>(R.id.escUsuario)
        val textoPassword = findViewById<TextView>(R.id.escPassword)
        val registro = findViewById<Button>(R.id.registro)
        val acceder = findViewById<Button>(R.id.acceder)
        val modificar = findViewById<Button>(R.id.modificar)
        val borrar = findViewById<Button>(R.id.borrar)
        val admin = SQLiteHelpedAdmin(this, "admin", null, 1)
        val encriptador = Intent(this, mainActivity::class.java)

        acceder.setOnClickListener {
            if (accederCuenta(textoUser.text.toString(), textoPassword.text.toString(), admin)) {
                textoUser.setText("")
                textoPassword.setText("")
                startActivity(encriptador)
            }
            textoUser.setText("")
            textoPassword.setText("")
        }
        registro.setOnClickListener {
            if (crearCuenta(textoUser.text.toString(), textoPassword.text.toString(), admin)) {
                textoUser.setText("")
                textoPassword.setText("")
                startActivity(encriptador)
            }
            textoUser.setText("")
            textoPassword.setText("")
        }
        modificar.setOnClickListener {
            modificarPassword(textoUser.text.toString(), textoPassword.text.toString(), admin)
            textoUser.setText("")
            textoPassword.setText("")
        }
        borrar.setOnClickListener {
            borrarUsuario(textoUser.text.toString(), textoPassword.text.toString(), admin)
            textoUser.setText("")
            textoPassword.setText("")
        }
    }

    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    fun accederCuenta(usuario: String, password: String, admin: SQLiteHelpedAdmin): Boolean {
        val bd = admin.writableDatabase
        if (usuario.isNotBlank() && password.isNotBlank()) {
            val comprobarUser =
                bd.rawQuery("SELECT usuario, password FROM Usuarios WHERE usuario='${usuario}'", null)
            return try {
                if (comprobarUser.moveToFirst()) {
                    val passwordBase = comprobarUser.getString(1)
                    if (passwordBase == password) {
                        mostrarToast("Acceso exitoso")
                        true;
                    } else {
                        mostrarToast("Usuario o contraseña incorrecto")
                        false;
                    }
                } else {
                    mostrarToast("Usuario o contraseña incorrecto")
                    false;
                }
            } finally {
                comprobarUser.close()
                bd.close()
            }
        } else {
            mostrarToast("Usuario y/o contraseña no pueden estar vacio")
            return false;
        }
    }

    fun crearCuenta(usuario: String, password: String, admin: SQLiteHelpedAdmin): Boolean {
        val bd = admin.writableDatabase
        if (usuario.isNotBlank() && password.isNotBlank()) {
            val comprobarUser =
                bd.rawQuery("SELECT usuario, password FROM Usuarios WHERE usuario='${usuario}'", null)
            try {
                if (comprobarUser.moveToFirst()) {
                    mostrarToast("El usuario ya existe")
                    return false
                } else if (comprobarPassword(password)) {
                    val usuarioNuevo = usuario
                    val passwordNueva = password
                    val registrar = ContentValues()
                    registrar.put("usuario", usuarioNuevo)
                    registrar.put("password", passwordNueva)
                    bd.insert("Usuarios", null, registrar)
                    bd.close()
                    mostrarToast("Registro existoso")
                    return true
                }
            } finally {
                comprobarUser.close()
                bd.close()
            }
        } else {
            mostrarToast("Usuario y/o contraseña no pueden estar vacio")
            return false;
        }
        return false
    }

    fun modificarPassword(usuario: String, password: String, admin: SQLiteHelpedAdmin) {
        val bd = admin.writableDatabase
        if (usuario.isNotBlank() && password.isNotBlank()) {
            val comprobarUser =
                bd.rawQuery("SELECT usuario, password FROM Usuarios WHERE usuario='${usuario}'", null)
            try {
                if (comprobarUser.moveToFirst()) {
                    if (comprobarPassword(password)) {
                        val passwordBase = comprobarUser.getString(1)
                        if (passwordBase == password) {
                            mostrarToast("La contraseña nueva debe ser distinta de la actual")
                            return
                        } else {
                            val modificar = ContentValues()
                            modificar.put("password", password)
                            val cant = bd.update("Usuarios", modificar, "usuario='${usuario}'", null)
                            if (cant == 1) {
                                mostrarToast("contraseña modificada con exito")
                            } else {
                                mostrarToast("Ha ocurrido un error")
                            }
                        }
                    }
                } else {
                    mostrarToast("Usuario no encontrado")
                }
            } finally {
                comprobarUser.close()
                bd.close()
            }
        } else {
            mostrarToast("Usuario y/o contraseña no pueden estar vacio")
            return;
        }

    }

    fun borrarUsuario(usuario: String, password: String, admin: SQLiteHelpedAdmin) {
        val bd = admin.writableDatabase
        if (usuario.isNotBlank() && password.isNotBlank()) {
            val comprobarUser =
                bd.rawQuery("SELECT usuario, password FROM Usuarios WHERE usuario='${usuario}'", null)
            try {
                if (comprobarUser.moveToFirst()) {
                    val passwordBase = comprobarUser.getString(1)
                    if (passwordBase == password) {
                        val cant = bd.delete("Usuarios", "usuario='${usuario}'", null)
                        if (cant == 1) {
                            mostrarToast("Usuario borrado con exito")
                        } else {
                            mostrarToast("Ha ocurrido un error")
                        }
                    } else {
                        mostrarToast("La contraseña no coincide")
                        return
                    }
                } else {
                    mostrarToast("Usuario no encontrado")
                }
            } finally {
                comprobarUser.close()
                bd.close()
            }
        } else {
            mostrarToast("Usuario y/o contraseña no pueden estar vacio")
            return;
        }
    }

    fun comprobarPassword(password: String): Boolean {
        if (password.length < 8) {
            mostrarToast("La contraseña debe contener al menos ocho caracteres")
            return false;
        }
        if (!password.any() { it.isLetter() }) {
            mostrarToast("La contraseña debe contener al menos un caracter")
            return false;
        }
        if (!password.any() { it.isDigit() }) {
            mostrarToast("La contraseña debe contener al menos un numero")
            return false;
        }
        if (!password.any { it.isUpperCase() }) {
            mostrarToast("La contraseña debe contener al menos una mayuscula")
            return false
        }
        return true
    }
}