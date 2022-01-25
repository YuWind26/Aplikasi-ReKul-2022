package id.group4.rekulapplic

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import id.group4.rekulapplic.databinding.ActivityEditProfilBinding
import id.group4.rekulapplic.databinding.ActivityProfilBinding
import java.lang.Exception

class EditProfil : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfilBinding

    private lateinit var fAuth: FirebaseAuth

    //image uri(which we will pick)
    private var imageUri:Uri ?= null

    //progress dialog
    private lateinit var progressDialog:ProgressDialog

//    var fAuth = FirebaseAuth.getInstance()
//    private var fStore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setup progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        fAuth = FirebaseAuth.getInstance()
        loadUserInfo()

        binding.back.setOnClickListener {
            onBackPressed()
        }

        //handle update image
        binding.profileImage.setOnClickListener {
            showImageAttachedMenu()
        }

        binding.updateBtn.setOnClickListener {
            validateData()
        }
    }

    private var username = ""
    private var kelas = ""
    private fun validateData() {
        //get data
        username = binding.username.text.toString().trim()
        kelas = binding.kelas.text.toString().trim()

        //validate data
        if (username.isEmpty()){
            //name not entered
            Toast.makeText(this,"Masukkan Username",Toast.LENGTH_SHORT).show()
        }
        else if (kelas.isEmpty()){
            //Kelas not entered
            Toast.makeText(this,"Masukkan Kelas",Toast.LENGTH_SHORT).show()
        }else{
            //username dan kelas entered

            if (imageUri == null){
                //need to update without image
                updateProfile("")
            }
            else{
                //need to update with image
                uploadImage()
            }
        }
    }

    private fun uploadImage() {
        progressDialog.setMessage("Uploading Profile Image")
        progressDialog.show()

        //image path and name, use uid to replace previous
        val filePathAndName = "ProfileImage/"+fAuth.uid

        //storage reference
        val reference = FirebaseStorage.getInstance().getReference(filePathAndName)
        reference.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                //image uploaded, get url of uploaded image
                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while(!uriTask.isSuccessful);
                val uploadedImageUrl = "${uriTask.result}"

                updateProfile(uploadedImageUrl)
            }
            .addOnFailureListener { e->
                //failed to upload image
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to upload image due to ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateProfile(uploadedImageUrl: String) {
        progressDialog.setMessage("Updating Profile...")

        //setup info to update to db
        val hashMap: HashMap<String,Any> = HashMap()
        hashMap["username"] = "$username"
        hashMap["kelas"] = "$kelas"
        if (imageUri != null){
            hashMap["profileImage"] = uploadedImageUrl
        }

        //update to db
        val reference = FirebaseDatabase.getInstance().getReference("Users")
        reference.child(fAuth.uid!!)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                //profile updated
                progressDialog.dismiss()
                Toast.makeText(this, "Profile Updated",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e->
                //failed to upload image
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to Update image due to ${e.message}",Toast.LENGTH_SHORT).show()
            }

    }

    private fun showImageAttachedMenu() {
        //show popup menu with options Camera, galery to pick images

        //setup popup menu
        val popupMenu = PopupMenu(this,binding.profileImage)
//        popupMenu.menu.add(Menu.NONE,0,0,"Camera")
        popupMenu.menu.add(Menu.NONE,1,1,"Gallery")
        popupMenu.show()

        //handle popum menu item click
        popupMenu.setOnMenuItemClickListener { item ->
            //get id of clicked item
            val id = item.itemId
//            if (id == 0){
//                //camera clicked
//                pickImageCamera()
//            }
//            else if (id == 1){
//                //gallery clicked
//                pickImageGallery()
//            }

            pickImageGallery()

            true
        }
    }

    private fun pickImageGallery() {
        //intent to pick image from gallery
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        GalleryActivityResultLauncher.launch(intent)
    }

    private fun pickImageCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE,"Temp_Title")
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp_Description")

        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
        cameraActivityResultLauncher.launch(intent)
    }

    private val cameraActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback <ActivityResult> { result ->
            //get uri of image
            if (result.resultCode == Activity.RESULT_OK){
                val data = result.data
                //imageUri = data!!.data no need we already have image in imageUri in camera case

                //set to imageview
                binding.profileImage.setImageURI(imageUri)
            }
            else{
                //canceled
                Toast.makeText(this, "Canceled",Toast.LENGTH_SHORT).show()
            }
        }
    )

    private val GalleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback <ActivityResult> { result ->
            //get uri of image
            if (result.resultCode == Activity.RESULT_OK){
                val data = result.data
                imageUri = data!!.data

                //set to imageview
                binding.profileImage.setImageURI(imageUri)
            }
            else{
                //canceled
                Toast.makeText(this, "Canceled",Toast.LENGTH_SHORT).show()
            }
        }
    )

    private fun loadUserInfo() {
        //db reference to load user info
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(fAuth.uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get user.info
                    val imageProfil = "${snapshot.child("profileImage").value}"
                    val username = "${snapshot.child("username").value}"
                    val kelas = "${snapshot.child("kelas").value}"

                    //set data
                    binding.username.setText(username)
                    binding.kelas.setText(kelas)

                    //set image
                    try{
                        Glide.with(this@EditProfil)
                            .load(imageProfil)
                            .placeholder(R.drawable.account)
                            .into(binding.profileImage)
                    }
                    catch (e: Exception){

                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

}
