import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app2.R // Substitua pelo nome correto do seu pacote

class DispositivosAdapter(private val dispositivos: List<BluetoothDevice> , private val listener: OnItemClickListener) :
    RecyclerView.Adapter<DispositivosAdapter.DispositivoViewHolder>() {

    class DispositivoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeDispositivo: TextView = itemView.findViewById(R.id.nomeDispositivo)
        val enderecoMac: TextView = itemView.findViewById(R.id.enderecoMac)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DispositivoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dispositivo, parent, false)
        return DispositivoViewHolder(itemView)
    }

    interface OnItemClickListener {
        fun onItemClick(device: BluetoothDevice)
    }

    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: DispositivoViewHolder, position: Int) {
        val dispositivo = dispositivos[position]
        holder.nomeDispositivo.text = dispositivo.name ?: "Nome desconhecido"
        holder.enderecoMac.text = dispositivo.address

        holder.itemView.setOnClickListener {
            listener.onItemClick(dispositivos[position])
        }
    }

    override fun getItemCount(): Int = dispositivos.size
}