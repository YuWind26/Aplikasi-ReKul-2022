package id.group4.rekulapplic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SplashScreen : AppCompatActivity() {

    private lateinit var fAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        fAuth = FirebaseAuth.getInstance()

        Handler(Looper.getMainLooper()).postDelayed({
//            startActivity(Intent(this, Login::class.java))
//            finish()
            checkUser()
        },1000)
    }

    private fun checkUser() {
        val firebaseUser = fAuth.currentUser

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        if(firebaseUser == null){
            startActivity(Intent(this, Login::class.java))
        }
        else{
            ref.child(firebaseUser!!.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        //get user roles
                        val role = snapshot.child("role").value
                        if(role == "Mahasiswa"){
                            startActivity(Intent(this@SplashScreen,HomeStudent::class.java))
                            finish()
                        }
                        else if(role == "Dosen"){
                            startActivity(Intent(this@SplashScreen,HomeTeacher::class.java))
                            finish()
                        }
                        else if(role == "Admin"){
                            startActivity(Intent(this@SplashScreen , HomeAdmin::class.java))
                            finish()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }

    }
}