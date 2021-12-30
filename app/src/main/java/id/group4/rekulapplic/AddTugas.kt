package id.group4.rekulapplic

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AddTugas : AppCompatActivity() {
    var Fstore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tugas)

        var mataKuliah:EditText = findViewById(R.id.mataKuliah_Tugas)
        var deskripsi:EditText = findViewById(R.id.deskripsi_tugas)
        var pickupDateBtn: Button = findViewById(R.id.pick_date)
        var deadlineTugas:EditText = findViewById(R.id.deadline_tugas)
        var tambahBtn:Button = findViewById(R.id.btn_tambahkan)

        tambahBtn.setOnClickListener {
            var matKul:String = mataKuliah.text.toString().trim()
            var desc:String = deskripsi.text.toString().trim()
            var dedline:String = deadlineTugas.text.toString().trim()

            uploadData(matKul,desc,dedline)

            startActivity(Intent(this, Tugas::class.java))
        }

        var c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)

        var datePicker = DatePickerDialog(this,DatePickerDialog.OnDateSetListener { view, year, monthOfYear,dayOfMonth  ->
            deadlineTugas.setText(""+dayOfMonth+"-"+(monthOfYear+1)+"-"+year)
        },year,month,day)

        pickupDateBtn.setOnClickListener {
            datePicker.show()
        }
    }

    private fun uploadData(matKul: String, desc: String, dedline: String) {
        Toast.makeText(this,"Anda telah tambah data",Toast.LENGTH_SHORT).show()
        var id:String = UUID.randomUUID().toString()
        var dataInfo= mutableMapOf<String,String>()
        dataInfo.put("id",id)
        dataInfo.put("matakuliah",matKul)
        dataInfo.put("deskripsi",desc)
        dataInfo.put("deadline",dedline)

        Fstore.collection("Data").document(id).set(dataInfo)
            .addOnSuccessListener {
                Toast.makeText(this,"Sedang Upload",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {e->
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

//Create Data (Done)
//Read Data
//Update Data
//Delete data


