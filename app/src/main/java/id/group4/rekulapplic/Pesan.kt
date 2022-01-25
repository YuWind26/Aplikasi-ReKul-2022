package id.group4.rekulapplic

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import id.group4.rekulapplic.databinding.ActivityPesanBinding
import java.lang.Exception

class Pesan : AppCompatActivity() {

    private lateinit var fAuth: FirebaseAuth

    private lateinit var binding : ActivityPesanBinding

    //RecyclerView
    private lateinit var newRecyclerView: RecyclerView
    private var list: ArrayList<DataPesan> = arrayListOf()

    private lateinit var waUrl : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPesanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fAuth = FirebaseAuth.getInstance()

        binding.back.setOnClickListener {
            onBackPressed()
        }

        val number = intent.getStringExtra("EXTRA_NUMBER")

        newRecyclerView = findViewById(R.id.rvPesan)
        newRecyclerView.setHasFixedSize(true)

        list.addAll(ListPesan.listData)

        val myViewHolder = AdapterPesan(list,object :AdapterPesan.OnAdapterListener{
            override fun onclick(pesan: String) {
                waUrl = "https://wa.me/"+number+"?text="+pesan
                val uri = Uri.parse(waUrl)
                val intent = Intent(Intent.ACTION_VIEW,uri)
                startActivity(intent)
            }

        })

        newRecyclerView.adapter = myViewHolder


        newRecyclerView.layoutManager = LinearLayoutManager(this)

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
                    try {
                        Glide.with(this@Pesan)
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