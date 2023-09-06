package com.example.sunopodcast

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.sunopodcast.Viewmodel.MyViewModel
import com.example.sunopodcast.dataclasestorecievefromapi.registeruser

import dagger.hilt.android.AndroidEntryPoint
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.launch

@AndroidEntryPoint
class registerationfragment() : Fragment() {

    val viewmodel : MyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registerationfragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fullname = view.findViewById<TextView>(R.id.editTextText2)
        val email = view.findViewById<TextView>(R.id.editTextTextEmailAddress)
        val pass = view.findViewById<TextView>(R.id.editTextTextPassword)
        val pass2 = view.findViewById<TextView>(R.id.editTextTextPassword2)
        val registerbutton = view.findViewById<Button>(R.id.button2)
        registerbutton.setOnClickListener {
            if (fullname.text.isBlank()||email.text.isBlank()
                ||pass.text.isBlank()||pass2.text.isBlank()){
                if (fullname.text.isBlank()){
                    fullname.setError("RequiredFeild")
                }
                if (email.text.isBlank()){
                    email.setError("RequiredFeild")
                }
                if (pass.text.isBlank()){
                    pass.setError("RequiredFeild")
                }
                if (pass2.text.isBlank()){
                    pass2.setError("RequiredFeild")
                }
            }else{
                if (pass2.text.length > 8){
                    pass2.setError("Password Should be of 8 digits")
                }else if (pass2.text.length<8){
                    pass2.setError("Password Should be of 8 digits")
                }else if (pass2.text.length == 8){
                    if (pass.text != pass.text){
                        pass.setError("Not Equal to password entered in above field")
                    }else{
                        if (validateEmail(email.text.toString())){
                            lifecycleScope.launch {
                              val o=   viewmodel.registersentusertorepo(registeruser(
                                    email = email.text.toString(),
                                    password = pass.text.toString(),
                                    fullname = fullname.text.toString()
                                ))
                                if (o != null) {
                                    Toast.makeText(context, "${o.body()?.message}", Toast.LENGTH_SHORT).show()
                                    if (o.code() == HttpStatusCode.OK.value){

                                        findNavController().navigate(R.id.action_registerationfragment_to_loginfragment)
                                    }
                                }

                            }



                        }else{
                            email.setError("Only Gmail Yahoo or Outlook Email Adress Allowed")
                        }
                    }
                }
            }
        }

    }
    private fun validateEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]+$".toRegex()

        if (!email.matches(emailRegex)) {
            return false // Invalid email format
        }

        val domain = email.substringAfterLast("@")

        return when (domain) {
            "gmail.com", "yahoo.com", "outlook.com" -> true // Email belongs to supported domains
            else -> false // Email does not belong to supported domains
        }
    }

}