package id.group4.rekulapplic

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import id.group4.rekulapplic.databinding.ActivityAddTugasBinding
import id.group4.rekulapplic.databinding.ActivityForgotPasswordBinding
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

class AddTugas : AppCompatActivity(), View.OnClickListener {
    //    var Fstore = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivityAddTugasBinding
//
    private lateinit var fAuth: FirebaseAuth

    private lateinit var mataKuliahTugas : EditText
    private lateinit var deskripsiTugas : EditText
    private lateinit var deadlineTugas : EditText
    private lateinit var pickupDateBtn : Button
    private lateinit var tambahBtn : Button

//
//    var mataKuliah = binding.mataKuliahTugas
//    var deskripsi = binding.deskripsiTugas
//    var deadlineTugas = binding.deadlineTugas
//    var pickupDateBtn = binding.pickDate
//
//    var tambahBtn = binding.btnTambahkan


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTugasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mataKuliahTugas = binding.mataKuliahTugas
        deskripsiTugas = binding.deskripsiTugas
        deadlineTugas = binding.deadlineTugas
        pickupDateBtn = binding.pickDate
        tambahBtn = binding.btnTambahkan

        binding.back.setOnClickListener {
            onBackPressed()
        }

        fAuth = FirebaseAuth.getInstance()


//        var mataKuliah: EditText = findViewById(R.id.mataKuliah_Tugas)
//        var deskripsi: EditText = findViewById(R.id.deskripsi_tugas)
//        var pickupDateBtn: Button = findViewById(R.id.pick_date)
//        var deadlineTugas: EditText = findViewById(R.id.deadline_tugas)
//        var tambahBtn: Button = findViewById(R.id.btn_tambahkan)

//        tambahBtn.setOnClickListener {
//            var matKul: String = mataKuliah.text.toString().trim()
//            var desc: String = deskripsi.text.toString().trim()
//            var dedline: String = deadlineTugas.text.toString().trim()
//
//            uploadData(matKul,desc,dedline)
//
//            startActivity(Intent(this, Tugas::class.java))
//        }

        var c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)

        var datePicker = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                deadlineTugas.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year)
            },
            year,
            month,
            day
        )

        pickupDateBtn.setOnClickListener {
            datePicker.show()
        }

        tambahBtn.setOnClickListener(this)

        loadUserInfo()

    }

    private fun loadUserInfo() {
        //db reference to load user info
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(fAuth.uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get user.info
                    val imageProfil = "${snapshot.child("profileImage").value}"
                    //set image
                    try{
                        Glide.with(this@AddTugas)
                            .load(imageProfil)
                            .placeholder(R.drawable.account)
                            .into(binding.profileImage)
                    }
                    catch (e: Exception){

                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })}

//    private fun uploadData(matKul: String, desc: String, dedline: String) {
//        Toast.makeText(this@AddTugas, "Anda telah tambah data",Toast.LENGTH_SHORT).show()
//        val timestamp = System.currentTimeMillis()
//        val id = UUID.randomUUID().toString()
//        val hashMap = HashMap<String,Any>()
//        hashMap["id"] = id
//        hashMap["matakuliah"] = matKul
//        hashMap["deskripsi"] = desc
//        hashMap["deadline"] = dedline
//
//        val ref = FirebaseDatabase.getInstance().getReference("Users")
//        ref.child(fAuth.uid!!).child("Tugas").child(id)
//            .setValue(hashMap)
//            .addOnSuccessListener {
//                Toast.makeText(this,"Sedang Upload",Toast.LENGTH_SHORT).show()
//            }.addOnFailureListener {e->
//                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
//            }
//    }

    override fun onClick(p0: View?) {
        saveData()
    }

    private fun saveData() {
        val matakuliah = mataKuliahTugas.text.toString().trim()
        val desc = deskripsiTugas.text.toString().trim()
        val deadline = deadlineTugas.text.toString().trim()

        if (matakuliah.isEmpty()){
            mataKuliahTugas.error = "MataKuliah masih kosong"
        }

        if (desc.isEmpty()){
            deskripsiTugas.error = "Deskripsi masih kosong"
        }

        if (deadline.isEmpty()){
            deadlineTugas.error = "MataKuliah masih kosong"
        }



        val ref = FirebaseDatabase.getInstance().getReference("Users").child(fAuth.uid!!).child("Tugas")

        val userId = ref.push().key

        val user = User(userId,matakuliah,desc,deadline)

        ref.child(userId!!).setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            }
    }
}

//    private fun uploadData(matKul: String, desc: String, dedline: String) {
//        Toast.makeText(this@AddTugas, "Anda telah tambah data",Toast.LENGTH_SHORT).show()
//        val timestamp = System.currentTimeMillis()
//        val id = UUID.randomUUID().toString()
//        val hashMap = HashMap<String,Any>()
//        hashMap["id"] = id
//        hashMap["matakuliah"] = matKul
//        hashMap["deskripsi"] = desc
//        hashMap["deadline"] = dedline
//
////        Toast.makeText(this,"Anda telah tambah data",Toast.LENGTH_SHORT).show()
////        var dataInfo= mutableMapOf<String,String>()
////        dataInfo.put("id",id)
////        dataInfo.put("matakuliah",matKul)
////        dataInfo.put("deskripsi",desc)
////        dataInfo.put("deadline",dedline)
//
//        val ref = FirebaseDatabase.getInstance().getReference("Users")
//        ref.child(fAuth.uid!!)
//
//        Fstore.collection("Data").document(id).set(dataInfo)
//            .addOnSuccessListener {
//                Toast.makeText(this,"Sedang Upload",Toast.LENGTH_SHORT).show()
//            }.addOnFailureListener {e->
//                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

//Create Data (Done)
//Read Data
//Update Data
//Delete data


