package id.group4.rekulapplic

//import android.content.Intent
//import android.content.pm.PackageManager
//import android.content.pm.PackageManager.NameNotFoundException
//import android.net.Uri
//import android.os.Bundle
//import android.view.View
//import android.widget.Button
//import android.widget.EditText
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.hbb20.CountryCodePicker
//
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.group4.rekulapplic.databinding.ActivityKontakBinding
import java.lang.Exception

class MyKontak : AppCompatActivity() {

    private lateinit var fAuth: FirebaseAuth

    private lateinit var binding: ActivityKontakBinding

    //RecyclerView
//    private lateinit var newRecyclerView: RecyclerView
//    private var list: ArrayList<DataPesan> = arrayListOf()
//
//    private lateinit var waUrl : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKontakBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        newRecyclerView = findViewById(R.id.rvPesan)
//        newRecyclerView.setHasFixedSize(true)
//
//        list.addAll(ListPesan.listData)

//        binding.btnKontak.setOnClickListener {
//            Toast.makeText(this,etNomor,Toast.LENGTH_SHORT).show()
//            binding.etNo.setText("")
//            binding.etNama.setText(etNomor)
//        }


//        val myViewHolder = AdapterPesan(list,object :AdapterPesan.OnAdapterListener{
//            override fun onclick(pesan: String) {
//                Toast.makeText(applicationContext,pesan, Toast.LENGTH_SHORT).show()
//                waUrl = "https://wa.me/6287841930598?text="+pesan
//                val uri = Uri.parse(waUrl)
//                val intent = Intent(Intent.ACTION_VIEW,uri)
//                startActivity(intent)
//            }
//
//        })
//
////        val myViewHolder = AdapterPesan(this,list)
//        newRecyclerView.adapter = myViewHolder
//
//
//        newRecyclerView.layoutManager = LinearLayoutManager(this)

        fAuth = FirebaseAuth.getInstance()

        binding.back.setOnClickListener {
            onBackPressed()
        }

        binding.etNo.setOnClickListener {
            var i = Intent(Intent.ACTION_PICK)
            i.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
            startActivityForResult(i, 111)
        }

        binding.btnKirimData.setOnClickListener {
            callActivity()
        }

        loadUserInfo()


    }

    private fun callActivity() {
        val etNo = binding.etNo.text.toString()

        val intent = Intent(this, Pesan::class.java).also {
            it.putExtra("EXTRA_NUMBER", etNo)
            startActivity(it)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == Activity.RESULT_OK) {
            var contactUri: Uri = data?.data ?: return
            var cols: Array<String> = arrayOf(
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            )
            var rs: Cursor? = contentResolver.query(contactUri, cols, null, null, null)
            if (rs?.moveToFirst()!!) {
                binding.etNo.setText(rs.getString(0))
                binding.etNama.setText(rs.getString(1))
            }
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
                    //set image
                    try {
                        Glide.with(this@MyKontak)
                            .load(imageProfil)
                            .placeholder(R.drawable.account)
                            .into(binding.profileImage)
                    } catch (e: Exception) {

                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}
//}

//
//class MyKontak : AppCompatActivity() {
//    private lateinit var countryCodePicker: CountryCodePicker
//    private lateinit var  phone: EditText
//    private lateinit var message: EditText
//    private lateinit var sendbtn: Button
//    var messagestr: String? = null
//    var phonestr : String? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_kontak)
//        countryCodePicker = findViewById(R.id.countryCode)
//        phone = findViewById(R.id.phoneNo)
//        message = findViewById(R.id.message)
//        sendbtn = findViewById(R.id.sendbtn)
//        sendbtn.setOnClickListener(View.OnClickListener {
//            messagestr = message.getText().toString()
//            phonestr = phone.getText().toString()
//            if (messagestr!!.isEmpty() && phonestr!!.isEmpty()) {
//                countryCodePicker.registerCarrierNumberEditText(phone)
//                phonestr = countryCodePicker.fullNumber
//                if (isWhatappInstalled) {
//                    val i = Intent(
//                        Intent.ACTION_VIEW, Uri.parse(
//                            "https://api.whatsapp.com/send?phone=" + phonestr +
//                                    "&text=" + messagestr
//                        )
//                    )
//                    startActivity(i)
//                    message.setText("")
//                    phone.setText("")
//                } else {
//                    Toast.makeText(
//                        this,
//                        "Whatsapp is not installed",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            } else {
//                Toast.makeText(
//                    this,
//                    "Please fill in the Phone no. and message it can't be empty",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//        })
//    }
//
//    private val isWhatappInstalled: Boolean
//        private get() {
//            val packageManager = packageManager
//            val whatsappInstalled: Boolean
//            whatsappInstalled = try {
//                packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
//                true
//            } catch (e: PackageManager.NameNotFoundException) {
//                false
//            }
//            return whatsappInstalled
//        }