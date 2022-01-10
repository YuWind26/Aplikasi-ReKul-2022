package id.group4.rekulapplic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import id.group4.rekulapplic.databinding.ActivityAddTugasBinding
import id.group4.rekulapplic.databinding.ActivityUpdateTugasBinding
import java.util.*
import kotlin.collections.HashMap

class UpdateTugas : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateTugasBinding
    private lateinit var database: DatabaseReference
    private lateinit var fAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateTugasBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.back.setOnClickListener {
//            onBackPressed()
//        }

//        binding.btnUpdate.setOnClickListener {
//            val matkul = binding.mataKuliahTugas.text.toString()
//            val desc = binding.deskripsiTugas.text.toString()
//            val dedline = binding.deadlineTugas.text.toString()
//
//            updateData(matkul, desc, dedline)
//        }
    }

//    //stuck di update data bingung
//    private fun updateData(matkul: String, desc: String, dedline: String) {
//        Toast.makeText(this, "Anda telah Update data", Toast.LENGTH_SHORT).show()
//        val hashMap = HashMap<String,Any>()
//        hashMap["matakuliah"] = matkul
//        hashMap["deskripsi"] = desc
//        hashMap["deadline"] = dedline
//
//        val ref = FirebaseDatabase.getInstance().getReference("Users").child(fAuth.uid!!).child("Tugas")
//            .updateChildren(hashMap)
//            .addOnSuccessListener {
//                Toast.makeText(this,"Sedang Update", Toast.LENGTH_SHORT).show()
//            }.addOnFailureListener {e->
//                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
//            }
//    }
}