package com.manipur.khannasi.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.manipur.khannasi.MainActivity
import com.manipur.khannasi.databinding.LayoutSignInBinding
import com.manipur.khannasi.repository.UserDetailsRepository

class SignInFragment : Fragment() {

    private var _binding: LayoutSignInBinding? = null
    private val binding get() = _binding!!
    private val userDetailsRepository = UserDetailsRepository()
    private val gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUp.setOnClickListener {
            val fragment = SignUpFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(com.manipur.khannasi.R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.signIn.setOnClickListener {
            val username = binding.username.text.toString()
            val passwordHash = binding.password.text.toString()

            if (username.isNotEmpty() && passwordHash.isNotEmpty()) {
                userDetailsRepository.getUserDetailsByUsernameAndPassword(username, passwordHash) { userDetails ->
                    if (userDetails != null) {
                        val sharedPreferences = activity?.getSharedPreferences("UserPreferences", AppCompatActivity.MODE_PRIVATE)
                        val editor = sharedPreferences?.edit()
                        editor?.putBoolean("isLoggedIn", true)
                        val userDetailsJson = gson.toJson(userDetails)
                        editor?.putString("UserDetails", userDetailsJson)
                        editor?.apply()

                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                        activity?.finish()

                        Toast.makeText(context, "Sign In Successful", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Sign In Failed", Toast.LENGTH_SHORT).show()
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