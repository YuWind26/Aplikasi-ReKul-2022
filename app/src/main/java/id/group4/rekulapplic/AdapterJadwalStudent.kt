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

class AdapterJadwalStudent (val mCtx: Context, val layoutResid : Int, val jadwalList : List<JadwalUser>) : ArrayAdapter<JadwalUser>(mCtx,layoutResid,jadwalList) {
    private lateinit var fAuth: FirebaseAuth
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)

        fAuth = FirebaseAuth.getInstance()

        var view: View = layoutInflater.inflate(layoutResid, null)

        val tvMatkul: TextView = view.findViewById(R.id.tvMataKuliah)
        val tvDosen: TextView = view.findViewById(R.id.tvDosen)
        val tvTopik: TextView = view.findViewById(R.id.tvTopik)
        val tvWaktu: TextView = view.findViewById(R.id.tvWaktu)

        val jadwal = jadwalList[position]

        tvMatkul.text = jadwal.matakuliah
        tvDosen.text = jadwal.dosen
        tvTopik.text = jadwal.topik
        tvWaktu.text = jadwal.waktu

        return view

    }
}