package id.group4.rekulapplic

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import id.group4.rekulapplic.databinding.ActivityForgotPasswordBinding
import id.group4.rekulapplic.databinding.ActivityLoginBinding

class ForgotPassword : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    private lateinit var fAuth: FirebaseAuth

    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fAuth = FirebaseAuth.getInstance()

        //progressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Tunggu Sebentar")
        progressDialog.setCanceledOnTouchOutside(false)

        //handle forgot password
        binding.btnFPassword.setOnClickListener {
            validateData()
        }

        binding.back.setOnClickListener {
            onBackPressed()
        }
    }

    private var email =""
    private fun validateData() {
        //get data
        email = binding.fpassword.text.toString().trim()

        //validate data
        if(email.isEmpty()){
            Toast.makeText(this,"Masukkan Email",Toast.LENGTH_SHORT).show()
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Invalid Email Patterns",Toast.LENGTH_SHORT).show()
        }
        else{
            recoverPassword()
        }
    }

    private fun recoverPassword() {
        //show progress
        progressDialog.setMessage("Sedang Mengirim Instruksi Reset ke $email")
        progressDialog.show()

        fAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                Toast.makeText(this,"Instruksi Telah Dikirim ke \n $email",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {e->
                Toast.makeText(this,"Gagal Mengirim Karena ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }
}