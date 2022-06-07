package id.group4.rekulapplic

import android.app.Activity
import android.content.Context
import android.util.Log
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.ContextCompat.startActivity

import android.net.Uri

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import androidx.core.content.ContextCompat


class AdapterPesan(
//                    private val context: Context,
                   private val pesanList : ArrayList<DataPesan>, private val listener : OnAdapterListener) : RecyclerView.Adapter<AdapterPesan.MyViewHolder> (){

    private lateinit var waUrl : String

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val tvPesan : TextView = itemView.findViewById(R.id.tvPesan)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView : View = LayoutInflater.from(parent.context).inflate(R.layout.list_pesan,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = pesanList[position]
        val pesan = currentItem.pesan
        holder.tvPesan.text = pesan
        holder.tvPesan.setOnClickListener {
//            Toast.makeText(context,pesan, Toast.LENGTH_SHORT).show()
            listener.onclick(pesan)
//            waUrl = "https://wa.me/+6287841930598?text=Hi,apa ada orang?"
//            val intent = Intent(Intent.ACTION_VIEW)
//            intent.data = Uri.parse(waUrl)
//            startActivity(intent)
        }

//        val activity =  holder.itemView.context as Activity
//        holder.itemView.setOnClickListener {
//            waUrl = "https://wa.me/+6287841930598?text="+pesan
//            val uri = Uri.parse(waUrl)
//            val intent = Intent(Intent.ACTION_VIEW,uri)
//            activity.startActivity(intent)
//        }
    }

    override fun getItemCount() = pesanList.size


    interface OnAdapterListener{
        fun onclick(pesan : String)
    }




}