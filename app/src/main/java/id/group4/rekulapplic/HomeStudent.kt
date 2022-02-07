package id.group4.rekulapplic

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.group4.rekulapplic.databinding.ActivityHomeAdminBinding
import id.group4.rekulapplic.databinding.ActivityHomeStudentBinding
import id.group4.rekulapplic.databinding.ActivityProfilBinding
import java.lang.Exception

class HomeStudent : AppCompatActivity() {
    private lateinit var fAuth: FirebaseAuth

    private lateinit var binding: ActivityHomeStudentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fAuth = FirebaseAuth.getInstance()

        binding.btnHome.setOnClickListener {
            startActivity(Intent(this, HomeStudent::class.java))
        }

        binding.profileImage.setOnClickListener {
            startActivity(Intent(this,Profil::class.java ))
        }

        binding.btnTugas.setOnClickListener {
            startActivity(Intent(this,Tugas::class.java))
        }

        binding.btnAccount.setOnClickListener {
            startActivity(Intent(this,Profil::class.java))
        }

        binding.btnKontak.setOnClickListener {
            startActivity(Intent(this,MyKontak::class.java))
        }

        binding.btnJadwalStudent.setOnClickListener {
            startActivity(Intent(this, JadwalStudent::class.java))
        }

        getuserName()
        loadUserInfo()



    }


    private fun getuserName() {
        val user = FirebaseDatabase.getInstance().getReference("Users").child(fAuth.uid!!)
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userName = "${snapshot.child("username").value}"
                    binding.tvNamaUser.text =userName
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
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
                        Glide.with(this@HomeStudent)
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
}