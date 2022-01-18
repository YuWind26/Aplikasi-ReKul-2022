package id.group4.rekulapplic

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class AdapterJadwal (val mCtx: Context, val layoutResid : Int, val jadwalList : List<JadwalUser>) : ArrayAdapter<JadwalUser>(mCtx,layoutResid,jadwalList) {
    private lateinit var fAuth: FirebaseAuth
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)

        fAuth = FirebaseAuth.getInstance()

        var view: View = layoutInflater.inflate(layoutResid, null)

        val tvMatkul: TextView = view.findViewById(R.id.tvMataKuliah)
        val tvDosen: TextView = view.findViewById(R.id.tvDosen)
        val tvTopik: TextView = view.findViewById(R.id.tvTopik)
        val tvWaktu: TextView = view.findViewById(R.id.tvWaktu)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)

        val jadwal = jadwalList[position]


        btnEdit.setOnClickListener {

            UpdateData(jadwal)
        }

        tvMatkul.text = jadwal.matakuliah
        tvDosen.text = jadwal.dosen
        tvTopik.text = jadwal.topik
        tvWaktu.text = jadwal.waktu

        return view

    }

    private fun UpdateData(jadwal : JadwalUser) {
        val builder = AlertDialog.Builder(mCtx)
        builder.setTitle("Edit Jadwal")
////
        val inflater = LayoutInflater.from(mCtx)
        val view = inflater.inflate(R.layout.update_dialog_jadwal, null)
//
        val matkul = view.findViewById<EditText>(R.id.edit_matkul)
        val dosen = view.findViewById<EditText>(R.id.edit_dosen)
        val topik = view.findViewById<EditText>(R.id.edit_topik)
        val waktu = view.findViewById<EditText>(R.id.edit_waktu)
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
                waktu.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year)
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
        matkul.setText(jadwal.matakuliah)
        dosen.setText(jadwal.dosen)
        topik.setText(jadwal.topik)
        waktu.setText(jadwal.waktu)

//
        builder.setView(view)
//
        builder.setPositiveButton("Update") { p0, p1 ->

            val dbUser = FirebaseDatabase.getInstance().getReference("Users").child("Jadwal")
            val matkulDb = matkul.text.toString().trim()
            val dosenDb = dosen.text.toString().trim()
            val topikDb = topik.text.toString().trim()
            val waktuDb = waktu.text.toString().trim()

            if (matkulDb.isEmpty()){
                matkul.error = "Mata Kuliah masih kosong"
                matkul.requestFocus()
                return@setPositiveButton
            }

            if (dosenDb.isEmpty()){
                dosen.error = "Dosen masih kosong"
                dosen.requestFocus()
                return@setPositiveButton
            }
//
            if (topikDb.isEmpty()){
                topik.error = "Topik masih kosong"
                topik.requestFocus()
                return@setPositiveButton
            }

            if (waktuDb.isEmpty()){
                waktu.error = "Waktu masih kosong"
                waktu.requestFocus()
                return@setPositiveButton
            }

            val jadwal = JadwalUser(jadwal.id,matkulDb,dosenDb,topikDb,waktuDb)

            dbUser.child(jadwal.id!!).setValue(jadwal)

            Toast.makeText(mCtx,"Data Berhasil Diupdate", Toast.LENGTH_SHORT).show()
            return@setPositiveButton
        }

        builder.setNegativeButton("Hapus"){
                p0,p1->
            val dbUser = FirebaseDatabase.getInstance().getReference("Users").child("Jadwal").child(jadwal.id.toString())
            dbUser.removeValue()

            Toast.makeText(mCtx,"Data berhasil dihapus", Toast.LENGTH_SHORT).show()

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
}