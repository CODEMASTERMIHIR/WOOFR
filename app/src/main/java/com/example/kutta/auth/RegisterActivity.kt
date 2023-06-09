package com.example.kutta.auth

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.kutta.MainActivity
import com.example.kutta.R
import com.example.kutta.databinding.ActivityRegisterBinding
import com.example.kutta.model.UserModel
import com.example.kutta.utils.COnfig
import com.example.kutta.utils.COnfig.hideDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private  var imageUri : Uri? = null
    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri = it

        binding.userImage.setImageURI(imageUri)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userImage.setOnClickListener {
            selectImage.launch("image/*")

        }

        binding.saveData.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        if (binding.userName.text.toString().isEmpty()|| imageUri==null|| binding.userEmail.text.toString().isEmpty() || binding.userCity.text.toString().isEmpty()){
            Toast.makeText(this,"Please enter all Fields",Toast.LENGTH_SHORT).show()

        }else if(!binding.terms.isChecked){
            Toast.makeText(this,"PLease accept terms and condition",Toast.LENGTH_SHORT).show()
        }else{
            uploadImage()
        }
    }

    private fun uploadImage(){
        COnfig.showDialog(this)

        val storageRef = FirebaseStorage.getInstance().getReference("profile")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("loading_dog.png")

        storageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener {
                     storeData(it)
                }.addOnFailureListener{
                    hideDialog()
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show() }
            }.addOnFailureListener{
                Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
            }

    }

    private fun storeData(imageUrl: Uri?) {



        val data =UserModel(
            name = binding.userName.text.toString(),
            image = imageUrl.toString(),
            email = binding.userEmail.text.toString(),
            city =  binding.userCity.text.toString(),

        )

        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
            .setValue(data).addOnCompleteListener {
                hideDialog()
                    if(it.isSuccessful){
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                        Toast.makeText(this,"User Registered Sucesfully",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this,it.exception!!.message,Toast.LENGTH_SHORT).show()
                    }
            }

    }

}
