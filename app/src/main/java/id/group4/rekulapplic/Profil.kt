package id.group4.rekulapplic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.group4.rekulapplic.databinding.ActivityProfilBinding
import java.lang.Exception

class Profil : AppCompatActivity() {

    private lateinit var binding: ActivityProfilBinding

    private lateinit var fAuth: FirebaseAuth

    private var firebaseDB = FirebaseDatabase.getInstance()

//    var fAuth = FirebaseAuth.getInstance()
//    private var fStore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fAuth = FirebaseAuth.getInstance()
        checkUser()

        loadUserInfo()

        binding.btnEdit.setOnClickListener {
            startActivity(Intent(this,EditProfil::class.java))
        }

//        setContentView(R.layout.activity_profil)
//
//        var username: EditText = findViewById(R.id.username)
//        var email: EditText = findViewById(R.id.email)
//        var role: EditText = findViewById(R.id.role)
//        var logout: Button = findViewById(R.id.logoutBtn)
//        var back :ImageView = findViewById(R.id.back)
//
        binding.back.setOnClickListener {
            onBackPressed()
        }
//
//        var userID = fAuth.currentUser?.uid
//        userID?.let { fStore.collection("Users").document(it) }
//            ?.addSnapshotListener { snapshot, _ ->
//                username.setText(snapshot!!.getString("FullName"))
//                email.setText(snapshot!!.getString("Email"))
//                when {
//                    snapshot!!.getString("isAdmin") == "1" -> {
//                        role.setText("Admin")
//                    }
//                    snapshot!!.getString("isStudent") == "1" -> {
//                        role.setText("Mahasiswa")
//                    }
//                    snapshot!!.getString("isTeacher") == "1" -> {
//                        role.setText("Dosen")
//                    }
//                }
//            }
//
        binding.logoutBtn.setOnClickListener {
            val firebaseUser = fAuth.currentUser
            val email = firebaseUser?.email
            fAuth.signOut()
            checkUser()
            Toast.makeText(this,"Anda Telah Keluar dari Akun ${email}", Toast.LENGTH_SHORT).show()
        }
//    }
    }

    private fun checkUser() {
        //get current user
        val firebaseUser = fAuth.currentUser
        if (firebaseUser == null){
//            not logged in, goto main screen
            val email = firebaseUser?.email
            startActivity(Intent(this,Login::class.java))
            finish()
        }
    }

    private fun loadUserInfo() {
        //db reference to load user info
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(fAuth.uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get user.info
                    val imageProfil = "${snapshot.child("profileImage").value}"
                    val username = "${snapshot.child("username").value}"
                    val email = "${snapshot.child("email").value}"
                    val kelas = "${snapshot.child("kelas").value}"
                    val role = "${snapshot.child("role").value}"

                    //set data
                    binding.username.text = username
                    binding.email.text = email
                    binding.kelas.text = kelas
                    binding.role.text = role

                    //set image
                    try{
                        Glide.with(this@Profil)
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
