package com.mainproject.mdas

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.mainproject.mdas.databinding.FragmentRegistrationBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.model.response.ResponseClass
import com.mainproject.mdas.model.viewmodel.AuthViewModel
import com.mainproject.mdas.model.viewmodel.AuthViewModel.Companion.personAddResponse
import com.mainproject.mdas.utils.disableArrays
import com.mainproject.mdas.utils.panchayatArray


class RegistrationFragment :
    BaseFragments<FragmentRegistrationBinding>(FragmentRegistrationBinding::inflate) {

    private var flag = true
    private var imgProfile: Uri? = null
    private var imgGuardianAdhaar: Uri? = null
    private var imgDisability: Uri? = null
    private var imgDisabilityAdhaar: Uri? = null
    lateinit var viewModel: AuthViewModel
    private val startTime = (30 * 1000).toLong()
    private val interval = (1 * 1000).toLong()
    var verificationId: String? = null
    lateinit var personClass: ResponseClass.Person
    private var countDownTimer: CountDownTimer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        binding.scrollable.isVisible = true
        viewModel.s = requireActivity()

        observers()

        binding.profileImage.setOnClickListener {
            imageChooser(100)
        }

        binding.imgGuardian.setOnClickListener {
            imageChooser(101)
        }

        binding.imgDisability.setOnClickListener {
            imageChooser(102)
        }

        binding.imgadhar.setOnClickListener {
            imageChooser(103)
        }

        val spinner = ArrayAdapter(
            requireContext(),
            com.bumptech.glide.R.layout.support_simple_spinner_dropdown_item,
            panchayatArray
        )

        spinner.setDropDownViewResource(
            android.R.layout
                .simple_spinner_dropdown_item
        )

        val spinner1 = ArrayAdapter(
            requireContext(),
            com.bumptech.glide.R.layout.support_simple_spinner_dropdown_item,
            disableArrays
        )

        spinner.setDropDownViewResource(
            android.R.layout
                .simple_spinner_dropdown_item
        )

        binding.disability.adapter = spinner1




        binding.submit.setOnClickListener {
            val personName = binding.name.text.trim().toString()
            val houseName = binding.houseName.text.trim().toString()
            val phone = binding.phone.text.trim().toString()
            val district = binding.district.text
            val panchayath = binding.panchayath.text
            val guardian = binding.guardian.text.trim().toString()
            val disability = binding.disability.selectedItem
            val percentage = binding.percentage.text.trim().toString()
            val uid = binding.aadhar.text.trim().toString()


            when {
                imgProfile == null -> showToast("Add Your Image")
                personName == "" -> showToast("Enter Your Name")
                houseName == "" -> showToast("Enter Your house Name")
                phone == "" -> showToast("Enter Your phone Number")
                district.equals("Select District") -> showToast("Select Your Valid District")
                district != "Eranakulam" -> showToast("This App Available only Eranakulam Peoples")
                panchayath.equals(panchayatArray[0]) -> showToast("Select Your Valid Panchayath")
                panchayath != panchayatArray[13] -> showToast("This App Available only Paingottoor Panchayath")
                guardian == "" -> showToast("Enter Your Name of Guardian")
                imgGuardianAdhaar == null -> showToast("Add Your Guardian Image")
                disability.equals(disableArrays[0]) -> showToast("Select Your Ability")
                imgDisability == null -> showToast("Add Your  Image")
                percentage == "" -> showToast("Enter Valid Percentage")
                uid == "" -> showToast("Enter UID Number")
                imgDisabilityAdhaar == null -> showToast("Add Your  Image")
                else -> {
                    personClass = ResponseClass.Person(
                        personName = personName,
                        houseName = houseName,
                        district = district.toString(),
                        panchayath = panchayath.toString(),
                        img = imgProfile.toString(),
                        disability = disability.toString(),
                        guardian = guardian,
                        imgGuardianAdhar = imgGuardianAdhaar.toString(),
                        percentage = percentage,
                        imgDisability = imgDisability.toString(),
                        adhar = uid,
                        imgAdhar = imgDisabilityAdhaar.toString(),
                        status = "apply",
                        phone = phone,
                        type = "User"
                    )

                    progress.isVisible = true
                    viewModel.userCheck(personClass)

                }
            }

        }

        binding.btnSubmit.setOnClickListener {
            otpVerification()
            progress.isVisible = true
        }
    }

    private fun observers() {

        viewModel.otps!!.observe(viewLifecycleOwner) {
            progress.isVisible = false
            binding.otpLayoutMain.isVisible = true
            binding.scrollable.isVisible = false
            binding.txt.text =
                String.format(binding.phone.text.toString(), R.string.code_has_been_send_to_91_84_61)
            countDownTimer = MyCountDownTimer(startTime, interval)
            binding.resend.isVisible = true
            verificationId = it.toString()
            setupOtpInput()
            timerControl()

        }


            AuthViewModel.userList.observe(viewLifecycleOwner) {
                Log.e("callsss", "called2")
                progress.isVisible = false
                if (it!!.status) {
                    viewModel.s = requireActivity()
                    viewModel.sendVerificationCode(binding.phone.text.toString())
                    progress.isVisible = true

                } else {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }



        personAddResponse.observe(viewLifecycleOwner) {
            progress.isVisible = false
            if (it.status) {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                     findNavController().popBackStack()
                }
             else {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
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
        } else {
            progress.isVisible = true
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
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthcredential)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
//                            progress.isVisible = false
                            flag = false
                            viewModel.addUser(personClass)

                        } else {
                            progress.isVisible = false

                            Toast.makeText(
                                requireContext(),
                                "invalid Otp",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }

        }
        binding.resend.setOnClickListener {
            viewModel.sendVerificationCode(binding.phone.text.toString())
        }

    }

    private fun imageChooser(i: Int) {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, i)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                100 -> {
                    imgProfile = data?.data!!
                    binding.profileImage.setImageURI(imgProfile)
                }
                101 -> {
                    imgGuardianAdhaar = data?.data!!
                    binding.imgGuardian.setImageURI(imgGuardianAdhaar)
                }
                102 -> {
                    imgDisability = data?.data!!
                    binding.imgDisability.setImageURI(imgDisability)
                }
                103 -> {
                    imgDisabilityAdhaar = data?.data!!
                    binding.imgadhar.setImageURI(imgDisabilityAdhaar)
                }
            }

        }
    }
}