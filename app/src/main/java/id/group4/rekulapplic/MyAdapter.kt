package id.group4.rekulapplic

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class MyAdapter (val mCtx: Context, val layoutResid : Int, val userList : List<User>) : ArrayAdapter<User>(mCtx,layoutResid,userList) {
    private lateinit var fAuth: FirebaseAuth
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)

        fAuth = FirebaseAuth.getInstance()

        var view:View = layoutInflater.inflate(layoutResid, null)

        val tvMatkul: TextView = view.findViewById(R.id.tvMataKuliah)
        val tvDeskripsi: TextView = view.findViewById(R.id.tvDescription)
        val tvDeadline: TextView = view.findViewById(R.id.tvDeadline)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)

        val user = userList[position]


        btnEdit.setOnClickListener {

            UpdateData(user)
        }

        tvMatkul.text = user.matakuliah
        tvDeskripsi.text = user.deskripsi
        tvDeadline.text = user.deadline

        return view

    }

    private fun UpdateData(user: User) {
        val builder = AlertDialog.Builder(mCtx)
////
        val inflater = LayoutInflater.from(mCtx)
        val view = inflater.inflate(R.layout.update_dialog, null)
//
        val matkul = view.findViewById<EditText>(R.id.edit_matkul)
        val desc = view.findViewById<EditText>(R.id.edit_deskripsi)
        val dedline = view.findViewById<EditText>(R.id.edit_deadline)
            var pickupDateBtn = view.findViewById<Button>(R.id.btn_pick_date)
////
////
            var c = Calendar.getInstance()
            var year = c.get(Calendar.YEAR)
            var month = c.get(Calendar.MONTH)
            var day = c.get(Calendar.DAY_OF_MONTH)

            var datePicker = DatePickerDialog(
                mCtx,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    dedline.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year)
                },
                year,
                month,
                day
            )
//
            pickupDateBtn.setOnClickListener {
                datePicker.show()
            }


//
        matkul.setText(user.matakuliah)
        desc.setText(user.deskripsi)
        dedline.setText(user.deadline)
//
        builder.setView(view)
//
        builder.setPositiveButton("Update") { p0, p1 ->

            val dbUser = FirebaseDatabase.getInstance().getReference("Users").child(fAuth.uid!!).child("Tugas")
            val matkulDb = matkul.text.toString().trim()
            val descDb = desc.text.toString().trim()
            val dedlineDb = dedline.text.toString().trim()

            if (matkulDb.isEmpty()){
                matkul.error = "Nama masih kosong"
                matkul.requestFocus()
                return@setPositiveButton
            }

            if (descDb.isEmpty()){
                desc.error = "Deskripsi masih kosong"
                desc.requestFocus()
                return@setPositiveButton
            }
//
            if (dedlineDb.isEmpty()){
                dedline.error = "Deadline masih kosong"
                dedline.requestFocus()
                return@setPositiveButton
            }

            val user = User(user.id,matkulDb,descDb,dedlineDb)

            dbUser.child(user.id!!).setValue(user)

            Toast.makeText(mCtx,"Data Berhasil Diupdate", Toast.LENGTH_SHORT).show()
            return@setPositiveButton
        }

        builder.setNegativeButton("Hapus"){
            p0,p1->
            val dbUser = FirebaseDatabase.getInstance().getReference("Users").child(fAuth.uid!!).child("Tugas").child(user.id.toString())
            dbUser.removeValue()

            Toast.makeText(mCtx,"Data berhasil dihapus",Toast.LENGTH_SHORT).show()

            return@setNegativeButton
//
        }
//
        builder.setNeutralButton("Batal"){
            p0,p1->
        }
//
        val alert = builder.create()
        alert.show()




        }
//sudah jadi si tapi update e bingun
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val itemView =
//            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
//        return MyViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val currentItem = userList[position]
//        holder.itemId
//        holder.matkul.text = currentItem.matakuliah
//        holder.desc.text = currentItem.deskripsi
//        holder.dedline.text = currentItem.deadline
//
//    }
//
//    override fun getItemCount(): Int {
//        return userList.size
//    }
//
//    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val matkul: TextView = itemView.findViewById(R.id.tvMataKuliah)
//        val desc: TextView = itemView.findViewById(R.id.tvDescription)
//        val dedline: TextView = itemView.findViewById(R.id.tvDeadline)
//        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
//        val btnHapus: TextView = itemView.findViewById(R.id.btnHapus)
//    }
    }



//    (private val userList:ArrayList<User>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
//        return  MyViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {
//        val user : User = userList[position]
//        holder.matkul.text = user.matakuliah
//        holder.description.text = user.deskripsi
//        holder.deadline.text = user.deadline
//    }
//
//
//
//
//    override fun getItemCount(): Int {
//        return userList.size
//    }
//
//    public class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
//        val matkul : TextView = itemView.findViewById(R.id.tvMataKuliah)
//        val description : TextView = itemView.findViewById(R.id.tvDescription)
//        val deadline : TextView = itemView.findViewById(R.id.tvDeadline)
//    }
