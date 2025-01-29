package com.example.loteriamexicana


import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.*

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var imv: ImageView
    private lateinit var b1: Button
    private lateinit var b2: Button
    private lateinit var bBarajear: Button
    private lateinit var nombre: TextView
    private lateinit var tts: TextToSpeech // Para el Text-to-Speech

    // Arreglo con las cartas de la lotería (54 en total)
    private val recursos = arrayOf(
        R.mipmap.gallo, R.mipmap.diablito, R.mipmap.dama, R.mipmap.catrin, R.mipmap.paraguas,
        R.mipmap.sirena, R.mipmap.escalera, R.mipmap.botella, R.mipmap.barril, R.mipmap.arbol, R.mipmap.melon,
        R.mipmap.valiente, R.mipmap.gorro, R.mipmap.muerte, R.mipmap.pera, R.mipmap.bandera, R.mipmap.bandolon, R.mipmap.violoncello,
        R.mipmap.garza, R.mipmap.pajaro, R.mipmap.mano, R.mipmap.bota, R.mipmap.luna, R.mipmap.cotorro,
        R.mipmap.borracho, R.mipmap.negro, R.mipmap.corazon, R.mipmap.sandia, R.mipmap.tambor, R.mipmap.camaron,
        R.mipmap.jaras, R.mipmap.musico, R.mipmap.arana, R.mipmap.soldado, R.mipmap.estrella, R.mipmap.cazo,
        R.mipmap.mundo, R.mipmap.apache, R.mipmap.nopal, R.mipmap.alacran, R.mipmap.rosa, R.mipmap.calavera,
        R.mipmap.campana, R.mipmap.cantarito, R.mipmap.venado, R.mipmap.sol, R.mipmap.corona, R.mipmap.chalupa, R.mipmap.pino, R.mipmap.pescado, R.mipmap.palma, R.mipmap.maceta, R.mipmap.arpa
    )

    private val nombres = arrayOf(
        "El Gallo",
        "El Diablito",
        "La Dama",
        "El Catrín",
        "El Paraguas",
        "La Sirena",
        "La Escalera",
        "La Botella",
        "El Barril",
        "El Árbol",
        "El Melón",
        "El Valiente",
        "El Gorrito",
        "La Muerte",
        "La Pera",
        "La Bandera",
        "El Bandolón",
        "El Violonchelo",
        "La Garza",
        "El Pájaro",
        "La Mano",
        "La Bota",
        "La Luna",
        "El Cotorro",
        "El Borracho",
        "El Negrito",
        "El Corazón",
        "La Sandía",
        "El Tambor",
        "El Camarón",
        "Las Jaras",
        "El Músico",
        "La Araña",
        "El Soldado",
        "La Estrella",
        "El Cazo",
        "El Mundo",
        "El Apache",
        "El Nopal",
        "El Alacrán",
        "La Rosa",
        "La Calavera",
        "La Campana",
        "El Cantarito",
        "El Venado",
        "El Sol",
        "La Corona",
        "La Chalupa",
        "El Pino",
        "El Pescado",
        "La Palma",
        "La Maceta",
        "El Arpa"
    )

    private var actual = 0
    private var ordenBarajado = recursos.indices.toList().shuffled().toMutableList() // Mezclar las cartas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imv = findViewById(R.id.ivFoto)
        b1 = findViewById(R.id.bAnterior)
        b2 = findViewById(R.id.bSiguiente)
        bBarajear = findViewById(R.id.btBarajear)
        nombre = findViewById(R.id.tvNombre)

        // Inicializar Text-to-Speech
        tts = TextToSpeech(this, this)

        // Inicializar la UI
        updateUI()

        // Deshabilitar el botón anterior al inicio
        b1.isEnabled = false

        // Asignar acción al botón de barajear
        bBarajear.setOnClickListener { barajearCartas() }
    }

    // Método que se llama cuando TextToSpeech está listo para usarse
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Configurar el idioma para TTS
            val result = tts.setLanguage(Locale("es", "MX")) // Español de México
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Manejar error si el idioma no está soportado o falta
                println("El idioma seleccionado no está soportado")
            }
        } else {
            println("Error al inicializar TextToSpeech")
        }
    }

    // Actualiza la interfaz de usuario con la carta actual y realiza la lectura del nombre
    private fun updateUI() {
        imv.setImageResource(recursos[ordenBarajado[actual]])
        val nombreCarta = nombres[ordenBarajado[actual]]
        nombre.text = nombreCarta
        speak(nombreCarta) // Leer el nombre de la carta en voz alta
    }

    // Función para avanzar a la siguiente carta
    fun siguiente(view: View) {
        if (actual < ordenBarajado.size - 1) {
            actual++
            updateUI()
            b1.isEnabled = true
            if (actual == ordenBarajado.size - 1) {
                b2.isEnabled = false
            }
        }
    }

    // Función para retroceder a la carta anterior
    fun anterior(view: View) {
        if (actual > 0) {
            actual--
            updateUI()
            b2.isEnabled = true
            if (actual == 0) {
                b1.isEnabled = false
            }
        }
    }

    // Función para mezclar las cartas y reiniciar el índice actual
    private fun barajearCartas() {
        ordenBarajado = recursos.indices.toList().shuffled().toMutableList()
        actual = 0 // Reiniciar al inicio de la baraja
        updateUI()

        // Habilitar los botones
        b1.isEnabled = false
        b2.isEnabled = true
    }

    // Función para hablar el nombre de la carta
    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    // Asegurarse de liberar el recurso de TextToSpeech cuando se destruya la actividad
    override fun onDestroy() {
        if (tts != null) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
