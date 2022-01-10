package id.group4.rekulapplic

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import id.group4.rekulapplic.databinding.ActivityLoginBinding
import id.group4.rekulapplic.databinding.ActivityRegistrasiBinding

class Login : AppCompatActivity() {

//    var valid= true
//    var fAuth = FirebaseAuth.getInstance()
//    var fStore = FirebaseFirestore.getInstance()

    private lateinit var binding: ActivityLoginBinding

    private lateinit var fAuth: FirebaseAuth

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Tunggu Sebentar")
        progressDialog.setCanceledOnTouchOutside(false)

        //handle click, not have account, goto register screen
        binding.goToRegistrasi.setOnClickListener {
            startActivity(Intent(this, Registrasi::class.java))
        }

        //handle click, begin login
        binding.btnLogin.setOnClickListener {
            validateData()
        }

        binding.forgotpass.setOnClickListener {
            startActivity(Intent(this,ForgotPassword::class.java))
        }




//        setContentView(R.layout.activity_login)
//        var email = findViewById(R.id.email) as EditText
//        var password = findViewById(R.id.password) as EditText
//        var btnlogin = findViewById(R.id.btn_login) as Button
//        var gotoregistrasi=findViewById(R.id.go_to_registrasi) as TextView
//
//        btnlogin.setOnClickListener {
//            checkField(email)
//            checkField(password)
//
//            if(valid){
//                fAuth.signInWithEmailAndPassword(email.text.toString(),password.text.toString())
//                    .addOnSuccessListener {
//                    Toast.makeText(this,"Berhasil Login", Toast.LENGTH_SHORT).show()
//                        checkUserAccessLevel(FirebaseAuth.getInstance().currentUser!!.uid)
//
//                }.addOnFailureListener {
//                    Toast.makeText(this,"Gagal Login", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//        }
//
//        gotoregistrasi.setOnClickListener{
//            startActivity(Intent(this,Registrasi::class.java))
//        }
    }

    private var email =""
    private var password =""

    private fun validateData() {
        //input data
        email = binding.email.text.toString().trim()
        password = binding.password.text.toString().trim()

        //Validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Invalid Email Pattern",Toast.LENGTH_SHORT).show()
        }
        else if (password.isEmpty()){
            Toast.makeText(this, "Masukkan Passwordmu",Toast.LENGTH_SHORT).show()
        }
        else{
            loginUser()
        }
    }

    private fun loginUser() {
        //login fAuth
        progressDialog.setMessage("Logging in")
        progressDialog.show()

        fAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                checkUser()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Gagal Login karena ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser() {
        progressDialog.setMessage("Checking User")

        val firebaseUser = fAuth.currentUser

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseUser!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    progressDialog.dismiss()

                    //get user roles
                    val role = snapshot.child("role").value
                    if(role == "Mahasiswa"){
                        startActivity(Intent(this@Login,HomeStudent::class.java))
                        finish()
                    }
                    else if(role == "Dosen"){
                        startActivity(Intent(this@Login,HomeTeacher::class.java))
                        finish()
                    }
                    else if(role == "Admin"){
                        startActivity(Intent(this@Login , HomeAdmin::class.java))
//                            Toast.makeText(this@Login, "Login Admin",Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }


//    private fun checkUserAccessLevel(uid: String) {
//        val df=fStore.collection("Users").document(uid)
//
//        df.get()
//            .addOnSuccessListener {documentSnapshot ->
//            Log.d("TAG", "onSuccess: "+ documentSnapshot.getData())
//
////            //IdenUser
//            if(documentSnapshot.getString("isAdmin") != null){
//                Toast.makeText(this,"Login as Admin", Toast.LENGTH_SHORT).show()
//                startActivity(Intent(this,HomeAdmin::class.java))
//            }else if(documentSnapshot.getString("isTeacher") != null){
//                Toast.makeText(this,"Login as Teacher", Toast.LENGTH_SHORT).show()
//                startActivity(Intent(this,HomeTeacher::class.java))
//            }else if(documentSnapshot.getString("isStudent") != null){
//                Toast.makeText(this,"Login as Student", Toast.LENGTH_SHORT).show()
//                startActivity(Intent(this,HomeStudent::class.java))
//            }
//
//        }
//    }

//    private fun checkField(textField: EditText): Boolean {
//
//        if(textField.text.toString().isEmpty()){
//            textField.error = "Maaf Error"
//            valid=false
//        }else{
//            valid=true
//        }
//        return valid
//    }

//    override fun onStart() {
//        super.onStart()
//        if(FirebaseAuth.getInstance().currentUser != null){
//            startActivity(Intent(this,MainActivity::class.java))
//            finish()
//        }
//    }
}