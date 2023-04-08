package com.example.kutta.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kutta.adapter.MessageAdapter
import com.example.kutta.databinding.ActivityMessageBinding
import com.example.kutta.model.MessageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class MessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        verifyChatId()

        binding.send.setOnClickListener {
            if (binding.messagechat.text!!.isEmpty()){
                Toast.makeText(this,"Please enter your message",Toast.LENGTH_SHORT).show()
            }else{
                storeData(binding.messagechat.text.toString())
            }
        }
    }
    private var senderId : String?=null
    private var receiverId : String?=null
    private var chatid : String? =null

    private fun verifyChatId() {

        senderId = FirebaseAuth.getInstance().currentUser!!.phoneNumber
        receiverId = FirebaseAuth.getInstance().currentUser!!.phoneNumber
        chatid = senderId + receiverId
        val reverseChatId = receiverId + senderId

        val reference = FirebaseDatabase.getInstance().getReference("chats")

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild(chatid!!)) {
                    getData(chatid)
                } else if (snapshot.hasChild(reverseChatId)) {
                    chatid = reverseChatId
                    getData(chatid)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MessageActivity,"Something went wrong",Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getData(chatId: String?) {
        FirebaseDatabase.getInstance().getReference("chats")
            .child(chatId!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = arrayListOf<MessageModel>()

                    for (show in snapshot.children) {
                        list.add(show.getValue(MessageModel::class.java)!!)


                    }

                    binding.recyclerView3.adapter = MessageAdapter(this@MessageActivity, list)
                    binding.recyclerView2.adapter = MessageAdapter(this@MessageActivity, list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MessageActivity, error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }


    private fun storeData(msg: String) {
        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())
        val map = hashMapOf<String,String>()
        map["message"]=msg
        map["sender"] =senderId!!
        map["receiver"] = receiverId!!
        map["currentTime"]=currentTime
        map["currentDate"]=currentDate
        val reference = FirebaseDatabase.getInstance().getReference("chats").child(chatid!!)

        reference.child(reference.push().key!!).setValue(map).addOnCompleteListener {
            if (it.isSuccessful){
                binding.messagechat.text = null
                Toast.makeText(this,"Message sent",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show()
            }
        }
    }
}