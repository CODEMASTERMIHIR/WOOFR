package com.example.kutta.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.kutta.R
import com.example.kutta.activity.EditProfileActivity
import com.example.kutta.auth.LoginActivity
import com.example.kutta.databinding.FragmentProfileBinding
import com.example.kutta.model.UserModel
import com.example.kutta.utils.COnfig
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class ProfileFragment : Fragment() {

        private lateinit var binding:FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        COnfig.showDialog(requireContext())
        binding=FragmentProfileBinding.inflate(layoutInflater)
        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!).get()
            .addOnSuccessListener {
                if(it.exists()){
                    val data = it.getValue((UserModel::class.java))
                    binding.name.setText(data!!.name.toString())
                    binding.city.setText(data.city.toString())
                    binding.email.setText(data.email.toString())


                   Glide.with(requireContext()).load(data.image).placeholder(R.drawable.profile_dog).into(binding.userImage)
                    COnfig.hideDialog()
                }
                }

        binding.logout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(),LoginActivity::class.java))
            requireActivity().finish()
        }

        binding.editprofile.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }
        return binding.root
    }
    }



