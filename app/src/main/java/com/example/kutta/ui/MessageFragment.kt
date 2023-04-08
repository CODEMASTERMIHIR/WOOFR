package com.example.kutta.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kutta.adapter.MessageUserAdapter
import com.example.kutta.databinding.FragmentMessageBinding
import com.example.kutta.ui.DatingFragment.Companion.list
import com.example.kutta.utils.COnfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MessageFragment : Fragment() {
    private lateinit var binding: FragmentMessageBinding
    private lateinit var adapter: MessageUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessageBinding.inflate(layoutInflater)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = MessageUserAdapter(requireContext(), arrayListOf(), arrayListOf())
        binding.recyclerView.adapter = adapter
        getData()
        return binding.root
    }

    private fun getData() {
        COnfig.showDialog(requireContext())
        var list = arrayListOf<String>()
        var newList = arrayListOf<String>()
        val currentId= FirebaseAuth.getInstance().currentUser!!.phoneNumber
        FirebaseDatabase.getInstance().getReference("chats")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    for(data in snapshot.children){
                        if(data.key!!.contains(currentId!!)){
                            list.add(data.key!!.replace(currentId,""))
                            newList.add(data.key!!)
                        }
                    }
                    binding.recyclerView.adapter = MessageUserAdapter(requireContext(), newList,list)
                    COnfig.hideDialog()
                }


                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(),error.message,Toast.LENGTH_SHORT).show()
                }

            })
    }
}
