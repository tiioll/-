package com.example.app_android_2_version

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.app_android_2_version.databinding.ActivityUserDescriptionBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException

class UserDescriptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDescriptionBinding
    lateinit var firebaseAuth: FirebaseAuth
    private val databaseReference = FirebaseDatabase.getInstance().getReference("Images")
    private lateinit var sharpref: SharedPreferences
    val storageReference: StorageReference = FirebaseStorage.getInstance().reference
    lateinit var imageUri: Uri




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadUserInfo()
        binding = ActivityUserDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
       ;

        binding.imageView3.setOnClickListener {
            selectImage()
        }

        binding.saveButton.setOnClickListener {
            saveUserData()
            val bundle = Bundle()
            bundle.putString("name", binding.nameVvod.getText().toString())
            bundle.putString("surname", binding.surnameVvod.getText().toString())


        }






    }
    val pickImageActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null && result.data?.data != null) {
            imageUri = result.data?.data!!

            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
                    this.contentResolver,
                    imageUri
                )
                binding.imageView3.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            uploadImage()
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        pickImageActivityResultLauncher.launch(intent)
    }

    private fun loadUserInfo() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        currentUserUid?.let { uid -> FirebaseDatabase.getInstance().getReference("users").child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val username = snapshot.child("username").getValue(String::class.java)
                val profileImage = snapshot.child("profileImage").getValue(String::class.java)
                val name = snapshot.child("name").getValue(String::class.java)
                val surname = snapshot.child("surname").getValue(String::class.java)
                val email = snapshot.child("email").getValue(String::class.java)
                binding.nameVvod.setText(name)
                binding.surnameVvod.setText(surname)
                binding.emailVvod.setText(email)





                profileImage?.let {
                    if (it.isNotEmpty()) {
                        Glide.with(this@UserDescriptionActivity).load(it).into(binding.imageView3)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Обработка ошибки
            }
        })
        }
    }


    private fun uploadImage() {
        imageUri.let { uri ->
            val uid = FirebaseAuth.getInstance().currentUser?.uid

            FirebaseStorage.getInstance().getReference().child("images/$uid")
                .putFile(uri)
                .addOnSuccessListener { taskSnapshot ->
                    Toast.makeText(this@UserDescriptionActivity, "Photo upload complete", Toast.LENGTH_SHORT).show()

                    FirebaseStorage.getInstance().getReference().child("images/$uid").downloadUrl
                        .addOnSuccessListener { downloadUri ->
                            FirebaseDatabase.getInstance().getReference().child("users").child(uid!!)
                                .child("profileImage").setValue(downloadUri.toString())
                        }
                }
        }
    }
    private fun saveUserData() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { uid ->
            val userReference = FirebaseDatabase.getInstance().getReference("users").child(uid)

            val surname3 = binding.surnameVvod.text.toString()
            val name3 = binding.nameVvod.text.toString()

            userReference.child("surname").setValue(surname3)
            userReference.child("name").setValue(name3)
        }
    }



}

