package id.group4.rekulapplic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.*
import id.group4.rekulapplic.databinding.ActivityHomeStudentBinding
import id.group4.rekulapplic.databinding.ActivityTugasBinding
import java.lang.Exception

class Tugas : AppCompatActivity() {

//    private lateinit var recyclerView: RecyclerView
//    private lateinit var userArrayList: ArrayList<User>
//    private lateinit var myAdapter: MyAdapter
//    private lateinit var Fstore: FirebaseFirestore
//    private lateinit var back:ImageView

    private lateinit var fAuth:FirebaseAuth

    private lateinit var listUser : ListView
    private  lateinit var userList : MutableList<User>

    private lateinit var dbRef : DatabaseReference
//    private lateinit var userRecyclerView: RecyclerView
//    private lateinit var userArrayList: ArrayList<User>

    private lateinit var binding: ActivityTugasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTugasBinding.inflate(layoutInflater)
        setContentView(binding.root)


        listUser = binding.listView

        fAuth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("Users").child(fAuth.uid!!).child("Tugas")


        binding.back.setOnClickListener {
            onBackPressed()
        }

        binding.plus.setOnClickListener {
            startActivity(Intent(this, AddTugas::class.java))
        }

        userList = mutableListOf()
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    userList.clear()
                    for(h in snapshot.children){
                        val user = h.getValue(User::class.java)
                        if(user != null){
                            userList.add(user)
                        }
                    }
                    val adapter = MyAdapter(this@Tugas, R.layout.list_item,userList)
                    listUser.adapter = adapter
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
                        Glide.with(this@Tugas)
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

    //bisa si tapi update e bingung
//    private fun getUserData() {
//        dbRef = FirebaseDatabase.getInstance().getReference("Users").child(fAuth.uid!!).child("Tugas")
//
//        dbRef.addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//                if (snapshot.exists()){
//                    for (userSnapshot in snapshot.children){
//
//                        val user  = userSnapshot.getValue(User::class.java)
//                        userArrayList.add(user!!)
//                    }
//                    userRecyclerView.adapter = MyAdapter(userArrayList )
//                }
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })
//    }

//    private fun EventChangeListener() {
//        Fstore = FirebaseFirestore.getInstance()
//        Fstore.collection("Data")
//            .addSnapshotListener(object : EventListener<QuerySnapshot>{
//                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
//                    if (error != null){
//                        Toast.makeText(this@Tugas, "Error" + error.message,Toast.LENGTH_SHORT).show()
//                        return
//                    }
//
//                    for (dc : DocumentChange in value?.documentChanges!!){
//                        if(dc.type == DocumentChange.Type.ADDED){
//                            userArrayList.add(dc.document.toObject(User::class.java))
//                        }
//                    }
//                    myAdapter.notifyDataSetChanged()
//                }
//
//            })
//    }


}

