package com.example.sunopodcast

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
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
import com.example.sunopodcast.dataclasestorecievefromapi.loginclass
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

lateinit var  pref:SharedPreferences
@AndroidEntryPoint
class loginfragment (): Fragment() {
    val viewModel :MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pref = androidx.preference.PreferenceManager.getDefaultSharedPreferences(requireContext())
        arguments?.let {

        }
        if (::pref.isInitialized) {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val token = pref.getString("jwt", "")
                    try {
//                        Log.e("hwl", "onCreate: $token", )
                        val z = viewModel.authenticate("Bearer $token")
                        if (z.isSuccessful) {
                            if (z.code() == HttpStatusCode.OK.value) {

                                withContext(Dispatchers.Main){
                                currenbtuser.login = true
                                    findNavController().navigate(R.id.action_loginfragment_to_listOfPodcast)

                                }

                            }
                        }
                    }catch (ex: Exception){
                        withContext(Dispatchers.Main){
                            Toast.makeText(context, "Failed to establish connection to the server", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loginfragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val login = view.findViewById<Button>(R.id.login)
        val email = view.findViewById<TextView>(R.id.email)
        val pass = view.findViewById<TextView>(R.id.password)
        val register = view.findViewById<TextView>(R.id.register)
        register.setOnClickListener {
            findNavController().navigate(R.id.action_loginfragment_to_registerationfragment)
        }

        login.setOnClickListener {
            if (email.text.isBlank() ||pass.text.isBlank()){
                Toast.makeText(context, "Please fill both the required fields", Toast.LENGTH_SHORT).show()
            }else{
               try {
                   lifecycleScope.launch {
                       try {
                           val logdata = viewModel.logintheuser(loginclass(
                               email = email.text.toString(),
                               password = pass.text.toString()
                           ))
                           if (logdata.isSuccessful){
                               if (logdata.code()== HttpStatusCode.OK.value){
                                   withContext(Dispatchers.Main){
//                                Log.e("TAG", "onViewCreated: ${logdata.body()?.message}", )
                                       pref.apply {
                                           this.edit().putString("jwt", logdata.body()?.message).apply()
                                       }
                                       findNavController().navigate(R.id.action_loginfragment_to_listOfPodcast)
                                       currenbtuser.login = true
                                   }

                               }
                           }else if (logdata.code() == HttpStatusCode.Conflict.value){
                               pass.setError("incorrect password")
                               Toast.makeText(context, "Incorrect Password", Toast.LENGTH_SHORT).show()
                           }else{
                               Toast.makeText(context, "Please Check Your Email", Toast.LENGTH_SHORT).show()
                           }
                       }catch (ex:Exception){
                           Toast.makeText(context, "Failed To Connect", Toast.LENGTH_SHORT).show()
                       }

                   }
               }catch (ex:Exception){
                   Toast.makeText(context, "Exception:$ex", Toast.LENGTH_SHORT).show()
               }

            }
        }


    }

}