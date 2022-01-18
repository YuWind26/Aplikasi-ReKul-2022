package id.group4.rekulapplic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import id.group4.rekulapplic.databinding.ActivityJadwalBinding
import id.group4.rekulapplic.databinding.ActivityJadwalStudentBinding
import java.lang.Exception

class JadwalStudent : AppCompatActivity() {
    private lateinit var fAuth: FirebaseAuth

    private lateinit var listJadwal : ListView
    private  lateinit var jadwalList : MutableList<JadwalUser>

    private lateinit var dbRef : DatabaseReference
//    private lateinit var userRecyclerView: RecyclerView
//    private lateinit var userArrayList: ArrayList<User>

    private lateinit var binding: ActivityJadwalStudentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJadwalStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)


        listJadwal = binding.listViewJadwalStudent

        fAuth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("Users").child("Jadwal")


        binding.back.setOnClickListener {
            onBackPressed()
        }


        jadwalList = mutableListOf()
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    jadwalList.clear()
                    for(i in snapshot.children){
                        val jadwal = i.getValue(JadwalUser::class.java)
                        if(jadwal != null){
                            jadwalList.add(jadwal)
                        }
                    }
                    val adapter = AdapterJadwalStudent(this@JadwalStudent, R.layout.list_jadwal_student,jadwalList)
                    listJadwal.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })



//        userRecyclerView = binding.recyclerView
//        userRecyclerView.layoutManager = LinearLayoutManager(this)
//        userRecyclerView.setHasFixedSize(true)
//
//        userArrayList = arrayListOf<User>()
//
//
//
//        getUserData()

//        recyclerView = findViewById(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.setHasFixedSize(true)
//
//        userArrayList = arrayListOf()
//
//        myAdapter = MyAdapter(userArrayList)
//
//        recyclerView.adapter = myAdapter
//
//        EventChangeListener()

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
                        Glide.with(this@JadwalStudent)
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