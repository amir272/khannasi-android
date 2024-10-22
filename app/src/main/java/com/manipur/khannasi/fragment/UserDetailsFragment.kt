package com.manipur.khannasi.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.manipur.khannasi.HomeActivity
import com.manipur.khannasi.R
import com.manipur.khannasi.databinding.FragmentUserDetailsBinding
import com.manipur.khannasi.dto.UserDetails
import com.manipur.khannasi.util.LoadingSpinner.Companion.showLoadingSpinner
import com.manipur.khannasi.util.SharedPreferencesRetriever

class UserDetailsFragment : Fragment() {

    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireContext()

        val userDetails: UserDetails? =
            SharedPreferencesRetriever.getDetails<UserDetails>(requireContext(), "UserDetails")
        userDetails?.let {
            binding.firstName.text = context.getString(R.string.first_name_value, it.userBasics.firstName)
            binding.lastName.text = context.getString(R.string.last_name_value, it.userBasics.lastName)
            binding.username.text = context.getString(R.string.username_value, it.userBasics.username)
            binding.email.text = context.getString(R.string.email_value, it.email)
            binding.contactNo.text = context.getString(R.string.contact_no_value, it.contactNo)
            binding.address.text = context.getString(R.string.address_value, it.address)
            binding.state.text = context.getString(R.string.state_value, it.state)
            binding.country.text = context.getString(R.string.country_value, it.country)
        }

        binding.logout.setOnClickListener {
            showLoadingSpinner(this, requireContext())
            val sharedPreferences = activity?.getSharedPreferences("UserPreferences", 0)
            val editor = sharedPreferences?.edit()
            editor?.putBoolean("isLoggedIn", false)
            editor?.apply()
            editor?.remove("UserDetails")

            val intent = Intent(activity, HomeActivity::class.java)
            startActivity(intent)
            activity?.finish() // Finish MainActivity
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}