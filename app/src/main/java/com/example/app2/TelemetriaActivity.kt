package com.example.app2

import BluetoothSocketWrapper
import android.annotation.SuppressLint
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class TelemetriaActivity : AppCompatActivity() {

    private var bluetoothSocket: BluetoothSocket? = null
    private lateinit var tvDadosRecebidos: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_telemetria)

        tvDadosRecebidos = findViewById(R.id.tvDadosRecebidos)

        // Obtém o BluetoothSocketWrapper do Intent
        val bluetoothSocketWrapper = intent.getSerializableExtra("bluetoothSocket") as? BluetoothSocketWrapper
        bluetoothSocket =bluetoothSocketWrapper?.bluetoothSocket

        // Verifica se o BluetoothSocket foi obtido com sucesso
        if (bluetoothSocket == null) {
            // Lide com o caso em que o BluetoothSocket não foi passado corretamente
            Toast.makeText(this, "Erro ao obter o socket Bluetooth", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Inicia uma thread para receber dados da ESP32
        Thread {
            try {
                val inputStream = bluetoothSocket!!.inputStream
                while (true) {
                    val buffer = ByteArray(1024)
                    val bytesLidos = inputStream.read(buffer)
                    if (bytesLidos > 0) {
                        val dadosRecebidos = String(buffer, 0, bytesLidos)
                        runOnUiThread {
                            tvDadosRecebidos.text = dadosRecebidos
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Lide com a exceção, como fechar a conexão ou exibir uma mensagem de erro
            }
        }.start()
    }
}