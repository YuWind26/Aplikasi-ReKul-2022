package id.group4.rekulapplic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*

class Tugas : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<User>
    private lateinit var myAdapter: MyAdapter
    private lateinit var Fstore: FirebaseFirestore
    private lateinit var back:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tugas)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        userArrayList = arrayListOf()

        myAdapter = MyAdapter(userArrayList)

        recyclerView.adapter = myAdapter

        EventChangeListener()
//
//        back.setOnClickListener {
//            startActivity(Intent(this,HomeAdmin::class.java))
//        }

        var addTugas:ImageView = findViewById(R.id.plus)
        addTugas.setOnClickListener {
            startActivity(Intent(this,AddTugas::class.java))
            finish()
        }
    }

    private fun EventChangeListener() {
        Fstore = FirebaseFirestore.getInstance()
        Fstore.collection("Data")
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null){
                        Toast.makeText(this@Tugas, "Error" + error.message,Toast.LENGTH_SHORT).show()
                        return
                    }

                    for (dc : DocumentChange in value?.documentChanges!!){
                        if(dc.type == DocumentChange.Type.ADDED){
                            userArrayList.add(dc.document.toObject(User::class.java))
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                }

            })
    }


}

