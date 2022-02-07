package id.group4.rekulapplic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.group4.rekulapplic.databinding.ActivityHomeStudentBinding
import id.group4.rekulapplic.databinding.ActivityHomeTeacherBinding
import java.lang.Exception

class HomeTeacher : AppCompatActivity() {
    private lateinit var fAuth: FirebaseAuth

    private lateinit var binding: ActivityHomeTeacherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnJadwalDosen.setOnClickListener {
            startActivity(Intent(applicationContext, Jadwal::class.java))
        }

        fAuth = FirebaseAuth.getInstance()

        var btnAccount = findViewById(R.id.btn_account) as View

        binding.btnKontakDosen.setOnClickListener {
            startActivity(Intent(this,MyKontak::class.java))
        }

        btnAccount.setOnClickListener {
            startActivity(Intent(applicationContext,Profil::class.java))
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
                        Glide.with(this@HomeTeacher)
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