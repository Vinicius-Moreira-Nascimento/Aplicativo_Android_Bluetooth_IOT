package com.example.app2

import DispositivosAdapter
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.IOException
import java.io.OutputStream
import java.util.UUID

class MainActivity : ComponentActivity(), DispositivosAdapter.OnItemClickListener {

    // variaveis do bluetooth
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private lateinit var bluetoothSocket: BluetoothSocket
    private val REQUEST_ENABLE_BT = 1
    private lateinit var outputStream: OutputStream // Declara outputStream como propriedade da classe
    private var contagem = 0;

    private var connectThread: Thread? = null
    private var isConnected = false

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.primeira_tela)

        //////////////////////////////////////////////////////////////////
        // Liga o Bluetooth //////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////
        val LigaBluetooth = findViewById<Button>(R.id.LigaBluetooth)
        LigaBluetooth.setOnClickListener {
            if (bluetoothAdapter == null) {
                Toast.makeText(
                    this,
                    "Bluetooth não suportado neste dispositivo",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
            if (!bluetoothAdapter!!.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
                Toast.makeText(this, "Ligando o Bluetooth", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Bluetooth Já está ativado", Toast.LENGTH_SHORT).show()
            }
        }

        //////////////////////////////////////////////////////////////////
        // Desliga o bluetooth ////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////
        val DesligaBluetooth = findViewById<Button>(R.id.DesligaBluetooth)
        DesligaBluetooth.setOnClickListener {
            if (bluetoothAdapter == null) {
                Toast.makeText(
                    this,
                    "Bluetooth não suportado neste dispositivo",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
            if (bluetoothAdapter?.isEnabled == true) {
                bluetoothAdapter.disable()
                Toast.makeText(this, "Bluetooth desativado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Bluetooth já está desativado", Toast.LENGTH_SHORT).show()
            }
        }


        //////////////////////////////////////////////////////////////////
        // Lista dispositivos ////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////
        val ListaBluetooth = findViewById<Button>(R.id.ListaBluetooth)
        ListaBluetooth.setOnClickListener {
            if (bluetoothAdapter?.isEnabled == true) {
                val recyclerView: RecyclerView = findViewById(R.id.listaDispositivos)
                recyclerView.layoutManager = LinearLayoutManager(this)
                val dispositivosPareados: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
                val dispositivosEncontrados = mutableListOf<BluetoothDevice>()
                val adapter = DispositivosAdapter(dispositivosEncontrados, this) // Passa o listener
                recyclerView.adapter = adapter

                // Adiciona dispositivos pareados à lista
                dispositivosPareados?.let { dispositivosEncontrados.addAll(it) }

                // Inicia a descoberta
                val receiver = object : BroadcastReceiver() {
                    override fun onReceive(context: Context, intent: Intent) {
                        when(intent.action) {
                            BluetoothDevice.ACTION_FOUND -> {
                                val device: BluetoothDevice? =
                                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                                device?.let {
                                    if (!dispositivosEncontrados.contains(it)) {
                                        dispositivosEncontrados.add(it)
                                        adapter.notifyItemInserted(dispositivosEncontrados.size - 1)
                                    }
                                }
                            }
                            BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                                // A descoberta terminou
                                context.unregisterReceiver(this)
                                // Faça algo com a lista final de dispositivos encontrados
                            }
                        }
                    }
                }

                val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
                filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
                registerReceiver(receiver, filter)

                bluetoothAdapter.startDiscovery()
            } else {
                Toast.makeText(this, "Bluetooth está desativado", Toast.LENGTH_SHORT).show()
            }
        }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }/// fim do ciclo OnCreate /////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Configura o novo Ciclo apos a Conectar com Dispositivo///////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    override fun onItemClick(device: BluetoothDevice) {
        // Inicia a conexão com o dispositivo 'device'
        conectarDispositivo(device)
    }

    @SuppressLint("MissingPermission")
    private fun conectarDispositivo(device: BluetoothDevice) {
        if (isConnected) {
            Toast.makeText(this, "Já conectado a um dispositivo", Toast.LENGTH_SHORT).show()
            return
        }
        // Mostra um ProgressDialog enquanto a conexão está em andamento
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Conectando...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        connectThread = Thread {
            try {
                val uuidString = "00001101-0000-1000-8000-00805F9B34FB"
                val socket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuidString))
                bluetoothAdapter?.cancelDiscovery()
                socket.connect()

                runOnUiThread {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Conectado com sucesso!", Toast.LENGTH_SHORT).show()
                }
                isConnected = true
                gerenciarConexao(socket)
            } catch (e: IOException) {
                runOnUiThread {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Erro ao conectar: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        connectThread?.start()
    }

    // Função para desconectar
    private fun desconectar() {
        try {
            bluetoothSocket.close()
            isConnected = false
            connectThread?.interrupt()
            connectThread = null
            Toast.makeText(this, "Desconectado", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(this, "Erro ao desconectar: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun gerenciarConexao(socket: BluetoothSocket) {
        val inputStream = socket.inputStream
        outputStream = socket.outputStream
        var isRunning = true

        // Thread para receber dados
        Thread {
            while (true) {
                try {
                    val buffer = ByteArray(1024)
                    val bytesLidos = inputStream.read(buffer)
                    if (bytesLidos > 0) {
                        val dadosRecebidos = String(buffer, 0, bytesLidos)
                        // Faça algo com os dados recebidos, por exemplo, exiba na tela
                        runOnUiThread {
                            Toast.makeText(this, "Dados recebidos: $dadosRecebidos", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: IOException) {// Lidar com erros de leitura
                    break
                }
            }
        }.start()

        // Handler para enviar dados periodicamente
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                    contagem++
                    val dadosParaEnviar = "Olá ESP32: Contagem: $contagem".toByteArray()
                    outputStream.write(dadosParaEnviar)
                    Log.d("Bluetooth", "Enviando: Olá ESP32: Contagem: $contagem")
                    handler.postDelayed(this, 1000) // Envia dados a cada 1 segundo
            }
        }
        handler.postDelayed(runnable, 1000)
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}/// fim da class main activit /////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
