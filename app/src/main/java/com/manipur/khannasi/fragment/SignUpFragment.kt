package com.manipur.khannasi.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.manipur.khannasi.MainActivity
import com.manipur.khannasi.databinding.LayoutSignUpBinding
import com.manipur.khannasi.dto.UserBasics
import com.manipur.khannasi.dto.UserDetails
import com.manipur.khannasi.util.SharedPreferencesRetriever
import com.manipur.khannasi.repository.UserDetailsRepository

class SignUpFragment : Fragment() {

    private var _binding: LayoutSignUpBinding? = null
    private val binding get() = _binding!!
    private val userDetailsRepository = UserDetailsRepository()
    private val gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.country.setOnItemClickListener { parent, _, position, _ ->
            val selectedCountry = parent.getItemAtPosition(position).toString()
            if (selectedCountry != "India") {
                binding.state.visibility = View.GONE
            } else binding.state.visibility = View.VISIBLE
        }

        binding.signUp.setOnClickListener {
            val firstName = binding.firstName.text.toString()
            val lastName = binding.lastName.text.toString()
            val username = binding.username.text.toString()
            val email = binding.email.text.toString()
            val contactNo = binding.contact.text.toString()
            val address = binding.address.text.toString()
            val state = binding.state.text.toString()
            val country = binding.country.text.toString()
            val passwordHash = binding.password.text.toString()

            if (firstName.isNotEmpty() && username.isNotEmpty() && email.isNotEmpty() && passwordHash.isNotEmpty()) {
                val userBasics = UserBasics(
                    firstName = firstName,
                    lastName = lastName,
                    username = username
                )

                val userDetails = UserDetails(
                    userBasics = userBasics,
                    email = email,
                    contactNo = contactNo,
                    address = address,
                    state = state,
                    country = country,
                    passwordHash = passwordHash
                )

                userDetailsRepository.postUserDetails(userDetails) { result ->
                    if (result != null) {
                        val sharedPreferences = activity?.getSharedPreferences("UserPreferences", AppCompatActivity.MODE_PRIVATE)
                        val editor = sharedPreferences?.edit()
                        editor?.putBoolean("isLoggedIn", true)
                        val userDetailsJson = gson.toJson(result)
                        editor?.putString("UserDetails", userDetailsJson)
                        editor?.apply()

                        val savedUserDetails: UserDetails? =
                            SharedPreferencesRetriever.getDetails<UserDetails>(requireContext(), "UserDetails")
                        Log.d("SignUpFragment", "onViewCreated: $savedUserDetails")

                        if(savedUserDetails != null) {
                            Log.d("SignUpFragment", "onViewCreated: ${savedUserDetails.userBasics.username}")
                            val intent = Intent(activity, MainActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        }

                        Toast.makeText(context, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Sign Up Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}