package id.group4.rekulapplic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val userList:ArrayList<User>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return  MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {
        val user : User = userList[position]
        holder.matkul.text = user.matakuliah
        holder.description.text = user.deskripsi
        holder.deadline.text = user.deadline
    }




    override fun getItemCount(): Int {
        return userList.size
    }

    public class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val matkul : TextView = itemView.findViewById(R.id.tvMataKuliah)
        val description : TextView = itemView.findViewById(R.id.tvDescription)
        val deadline : TextView = itemView.findViewById(R.id.tvDeadline)
    }
}