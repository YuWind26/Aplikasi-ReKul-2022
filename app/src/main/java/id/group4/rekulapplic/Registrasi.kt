package id.group4.rekulapplic

import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
//import com.google.firebase.firestore.FirebaseFirestore
import id.group4.rekulapplic.databinding.ActivityRegistrasiBinding

class Registrasi : AppCompatActivity() {

//    var valid= true
    private lateinit var binding:ActivityRegistrasiBinding

    private lateinit var fAuth: FirebaseAuth

    private lateinit var progressDialog: ProgressDialog

    private lateinit var cbMahasiswa: CheckBox
    private lateinit var cbDosen: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Menggunakan binding daripada findviewbyid
        binding = ActivityRegistrasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fAuth = FirebaseAuth.getInstance()

        cbMahasiswa = binding.cbStudent
        cbDosen = binding.cbTeacher

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Tunggu Sebentar")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btnRegistrasi.setOnClickListener {
            validateData()
        }


//        var fStore = FirebaseFirestore.getInstance()
//
//
//        var username = findViewById(R.id.username) as EditText
//        var email = findViewById(R.id.email) as EditText
//        var password = findViewById(R.id.password) as EditText
////        var btnregistrasi = findViewById(R.id.btn_registrasi) as Button
////        var gotologin=findViewById(R.id.go_to_login) as TextView
//        var cbStudent = findViewById(R.id.cb_student) as CheckBox
//        var cbTeacher = findViewById(R.id.cb_teacher) as CheckBox
//
//
//
//        binding.btnRegistrasi.setOnClickListener {
//            checkField(username)
//            checkField(email)
//            checkField(password)
//
//            if(valid){
//                fAuth.createUserWithEmailAndPassword(email.text.toString(),password.text.toString())
//                    .addOnSuccessListener {
//                        var user=fAuth.currentUser
//                        Toast.makeText(this,"Akun Dibuat", Toast.LENGTH_SHORT).show()
//                        var df=fStore.collection("Users").document(user!!.uid)
//                        var userInfo= mutableMapOf<String,String>()
//                        userInfo.put("FullName",username.text.toString())
//                        userInfo.put("Email",email.text.toString())
////                        userInfo.put("Password",password.text.toString())
//                        //specify User
//                        if(cbStudent.isChecked){
//                            userInfo.put("isStudent","1")
//                        }else if(cbTeacher.isChecked){
//                            userInfo.put("isTeacher","1")
//                        }else{
//                            userInfo.put("isAdmin","1")
//                        }
//
//                        df.set(userInfo)
//
//                        startActivity(Intent(this,Login::class.java))
//                        finish()
//                    }.addOnFailureListener {
//                        Toast.makeText(this,"Gagal Buat Akun", Toast.LENGTH_SHORT).show()
//                    }
//            }
//        }

        binding.goToLogin.setOnClickListener{
            startActivity(Intent(this,Login::class.java))
        }

    }




    private var username = ""
    private var email = ""
    private var kelas = ""
    private var password = ""
    private var cPassword = ""

    private fun validateData() {
        //Input Data
        username = binding.username.text.toString().trim()
        email = binding.email.text.toString().trim()
        kelas = binding.kelas.text.toString().trim()
        password = binding.password.text.toString().trim()
        cPassword = binding.confirmPassword.text.toString().trim()

        //validate data
        if (username.isEmpty()){
            Toast.makeText(this, "Masukkan Namamu",Toast.LENGTH_SHORT).show()
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Invalid Email Pattern",Toast.LENGTH_SHORT).show()
        }
        else if (kelas.isEmpty()){
            Toast.makeText(this, "Masukkan Kelasmu",Toast.LENGTH_SHORT).show()
        }
        else if (password.isEmpty()){
            Toast.makeText(this, "Masukkan Passwordmu",Toast.LENGTH_SHORT).show()
        }
        else if (cPassword.isEmpty()){
            Toast.makeText(this, "Confirm Password",Toast.LENGTH_SHORT).show()
        }
        else if (password != cPassword){
            Toast.makeText(this, "Password tidak cocok",Toast.LENGTH_SHORT).show()
        }
        else{
            createUserAccount()
        }
    }

    private fun createUserAccount() {
        // Create Account FAuth

        //show Progress
        progressDialog.setMessage("Membuat Akun")
        progressDialog.show()

        //create user with fAuth
        fAuth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                //account dibuat
                //                Firebase Cloud Messaging notification
                FirebaseMessaging.getInstance().subscribeToTopic("mahasiswa")

                val token = FirebaseInstanceId.getInstance().token
                    if (token != null) {

                        updateUserInfo(token)
                    }
                //                Firebase Cloud Messaging notification
            }
            .addOnFailureListener { e ->
                //gagal buat account
                progressDialog.dismiss()
                Toast.makeText(this, "Gagal Buat akun karena ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUserInfo(token :String) {
        //save user info -> FRealtimeDB
        progressDialog.setMessage("Saving User Info")

        //timestamp
        val timestamp = System.currentTimeMillis()

        //get current user uid, since user is registered so we can get it now
        val uid = fAuth.uid

        //setup data to add in db
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["uid"] = uid
        hashMap["email"] = email
        hashMap["kelas"] = kelas
        hashMap["username"] = username
        hashMap["profileImage"] = "" // add empty, will do in profile edit
        if (cbMahasiswa.isChecked) {
            hashMap["role"] = "Mahasiswa"
        } else if (cbDosen.isChecked) {
            hashMap["role"] = "Dosen"
        } else if (!cbMahasiswa.isChecked && !cbDosen.isChecked){
            Toast.makeText(this, "Tolong Pilih Rolemu",Toast.LENGTH_SHORT).show()
        }
        hashMap["timestamp"] = timestamp
        hashMap["token"] = token

        //set data to db
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(uid!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Account Dibuat",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@Registrasi,Login::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Gagal Menyimpan user info karena ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }

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
}