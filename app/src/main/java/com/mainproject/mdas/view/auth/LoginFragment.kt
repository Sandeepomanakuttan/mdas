package com.mainproject.mdas.view.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.mainproject.mdas.R
import com.mainproject.mdas.databinding.FragmentLoginBinding
import com.mainproject.mdas.model.repository.auth.AuthRepository
import com.mainproject.mdas.model.viewmodel.AuthViewModel
import com.mainproject.mdas.model.viewmodel.User


class LoginFragment : Fragment() {

    private lateinit var binding:FragmentLoginBinding
    lateinit var viewModel:AuthViewModel
    private val startTime = (30 * 1000).toLong()
    private val interval = (1 * 1000).toLong()
    var verificationId: String?=null

    private var countDownTimer: CountDownTimer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater,container,false)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= ViewModelProvider(this)[AuthViewModel()::class.java]


        AuthViewModel.userList.observe(viewLifecycleOwner){
            binding.progress.isVisible=false
            if (it != null) {
                if (it.status) {
                    if(it.message=="Success") {
                        if (it.user?.type == "Admin") {
                            Toast.makeText(requireContext(), "Admin", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_loginFragment_to_adminBaseFragment)
                        }
                    }else{
                        Toast.makeText(requireContext(), "User doesn't exits", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
            else {
                val user =User(phoneNumber = binding.phoneNumber.text.toString(), type = "Admin")

                user.phoneNumber?.let { it1 ->
                    FirebaseDatabase.getInstance().getReference("User").child(it1).setValue(user).addOnCompleteListener{
                        if (it.isSuccessful){
                            Toast.makeText(requireContext(), "Added", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(requireContext(), it.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }

        binding.btnSubmit.setOnClickListener {

            if (binding.btnSubmit.text.toString() != "LOGIN") {
                if (binding.phoneNumber.text.toString().isEmpty()) {
                    binding.phoneError.isVisible = true
                    binding.phoneError.text = "Fill The Mobile Number"
                } else if (binding.phoneNumber.text.trim().toString().length != 10) {
                    binding.phoneError.isVisible = true
                    binding.phoneError.text = "Enter the valid Mobile Number"
                } else {
                    binding.phoneError.isVisible = false

                    binding.progress.isVisible = true

                    viewModel.s=requireActivity()

                   // viewModel.sendVerificationCode(binding.phoneNumber.text.toString())

                viewModel.checkUser(binding.phoneNumber.text.trim().toString(),"")
//                    viewModel.otps?.observe(viewLifecycleOwner) {
//                        binding.progress.isVisible = false
//                        binding.otpLayout.isVisible = true
//                        countDownTimer = MyCountDownTimer(startTime, interval)
//                        binding.btnSubmit.text="LOGIN"
//                        binding.resend.isVisible = true
//                        verificationId = it.toString()
                        setupOtpInput()
                        timerControl()

                   // }
                }
            }else{
                otpVerification()
            }

        }
    }

    private fun setupOtpInput() {

        binding.code1.addTextChangedListener(GenericTextWatcher(binding.code1, binding.code2))
        binding.code2.addTextChangedListener(GenericTextWatcher(binding.code2, binding.code3))
        binding.code3.addTextChangedListener(GenericTextWatcher(binding.code3, binding.code4))
        binding.code4.addTextChangedListener(GenericTextWatcher(binding.code4, binding.code5))
        binding.code5.addTextChangedListener(GenericTextWatcher(binding.code5, binding.code6))
        binding.code6.addTextChangedListener(GenericTextWatcher(binding.code6, null))


        binding.code1.setOnKeyListener(GenericKeyEvent(binding.code1, null))
        binding.code2.setOnKeyListener(GenericKeyEvent(binding.code2, binding.code1))
        binding.code3.setOnKeyListener(GenericKeyEvent(binding.code3, binding.code2))
        binding.code4.setOnKeyListener(GenericKeyEvent(binding.code4, binding.code3))
        binding.code5.setOnKeyListener(GenericKeyEvent(binding.code5, binding.code4))
        binding.code6.setOnKeyListener(GenericKeyEvent(binding.code6, binding.code5))

    }

    private fun timerControl() {
        val startTimer = true
        if (startTimer) {
            countDownTimer?.start()

        } else {
            countDownTimer?.cancel()


        }
    }

    class GenericKeyEvent internal constructor(
        private val currentView: EditText,
        private val previousView: EditText?
    ) : View.OnKeyListener {
        override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.code1 && currentView.text.isEmpty()) {
                //If current is empty then previous EditText's number will also be deleted
                previousView!!.text = null
                previousView.requestFocus()
                return true
            }
            return false
        }


    }

    class GenericTextWatcher internal constructor(
        private val currentView: View,
        private val nextView: View?
    ) :
        TextWatcher {
        override fun afterTextChanged(editable: Editable) { // TODO Auto-generated method stub
            val text = editable.toString()
            when (currentView.id) {
                R.id.code1 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.code2 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.code3 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.code4 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.code5 -> if (text.length == 1) nextView!!.requestFocus()
                //You can use EditText4 same as above to hide the keyboard
            }
        }

        override fun beforeTextChanged(
            arg0: CharSequence,
            arg1: Int,
            arg2: Int,
            arg3: Int
        ) { // TODO Auto-generated method stub
        }

        override fun onTextChanged(
            arg0: CharSequence,
            arg1: Int,
            arg2: Int,
            arg3: Int
        ) { // TODO Auto-generated method stub
        }

    }

    inner class MyCountDownTimer(startTime: Long, interval: Long) :
        CountDownTimer(startTime, interval) {
        override fun onFinish() {
            binding.resend.isVisible = true
            binding.resend.text = getString(R.string.resend)
        }

        @SuppressLint("SetTextI18n")
        override fun onTick(millisUntilFinished: Long) {
            val currentTime = millisUntilFinished / 1000
            binding.resend.text = String.format(
                resources.getString(R.string.resend_otp_26),
                "" + currentTime / 60 + " : " + if (currentTime % 60 >= 10) currentTime % 60 else "0" + currentTime % 60
            )

        }
    }

    private fun otpVerification() {


        if (binding.code1.text.toString().trim { it <= ' ' }.isEmpty()
            || binding.code2.text.toString().trim { it <= ' ' }.isEmpty()
            || binding.code3.text.toString().trim { it <= ' ' }.isEmpty()
            || binding.code4.text.toString().trim { it <= ' ' }.isEmpty()
            || binding.code5.text.toString().trim { it <= ' ' }.isEmpty()
            || binding.code6.text.toString().trim { it <= ' ' }.isEmpty()
        ) {
            Toast.makeText(
                requireContext(),
                "Please Enter Otp",
                Toast.LENGTH_SHORT
            ).show()
            return
        }else{
            val code: String = (binding.code1.text.toString()
                    + binding.code2.text.toString() +
                    binding.code3.text.toString() +
                    binding.code4.text.toString() +
                    binding.code5.text.toString() +
                    binding.code6.text.toString())
            val phoneAuthcredential = verificationId?.let {
                PhoneAuthProvider.getCredential(
                    it, code
                )
            }
            if (phoneAuthcredential != null) {
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthcredential).addOnCompleteListener {

                    if (it.isSuccessful){

                       val id = FirebaseAuth.getInstance().currentUser.toString()
                        viewModel.checkUser(binding.phoneNumber.text.toString(),id)
                        Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show()

                    }else{

                        Toast.makeText(requireContext(), it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }





    }
}