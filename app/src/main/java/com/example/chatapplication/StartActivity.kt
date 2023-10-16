package com.example.chatapplication

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.chatapplication.activity.LoginActivity
import com.example.chatapplication.databinding.ActivityStartBinding
import com.example.chatapplication.dto.UserInfo
import com.example.chatapplication.dto.ValidUserInfo
import com.google.android.material.chip.Chip
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class StartActivity : AppCompatActivity() {

    private val binding: ActivityStartBinding by lazy {
        ActivityStartBinding.inflate(layoutInflater)
    }

    private val users = LoginActivity.users
    private var imageUri: String? = null

    companion object {
       // lateinit var myInfo:UserInfo
    }

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
            this.contentResolver.takePersistableUriPermission(uri, flag)

            binding.imageView.setImageURI(uri)
            imageUri = uri.toString()

            Log.d("PhotoPicker", "Selected URI: $uri")
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        bindView()
    }

    private fun initView() {
//        database = FirebaseDatabase.getInstance()
//        users = database.getReference("users")

    }


    private fun bindView() {

        binding.submitButton.setOnClickListener {
            saveData()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.imageView.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


    }

    private fun getBirthDate(): String {

        val year = binding.datePicker.year.toString()
        val month = binding.datePicker.month.toString()
        val day = binding.datePicker.dayOfMonth.toString()
        val birthDate = year + "." + month + "." + day

        return birthDate
    }

    private fun getPersonality(): String {
        val checkedChipIds = binding.chipGroup.checkedChipIds
        var personality = ""

        for (id in checkedChipIds) {
            val checkedChip: Chip = findViewById(id)
            personality += checkedChip.text.toString()+" "
        }

        return personality

    }

    private fun getSex(): String {
        val id = binding.sexChipGroup.checkedChipId
        val checkedChip: Chip = findViewById(id)
        val sex = checkedChip.text.toString()

        return sex
    }

    private fun saveData() {
        val userName = binding.nickNameEditText.getTextValue
        val introduce = binding.introduceEditText.getTextValue
        val state = binding.stateEditText.getTextValue
        val birthDate = getBirthDate()
        val personality = getPersonality()
        val sex = getSex()




        //카카오 로그인
        LoginActivity.kakaoCurrentUser?.let {
            val userId = it.id.toString()
            val title = "profile"
            val myInfo = UserInfo(userId,userName, introduce,state, birthDate, personality, sex, imageUri)
            LoginActivity.users.child("isVaildUser").child(myInfo.userId).setValue(ValidUserInfo(true))
            LoginActivity.myInfo = myInfo
            users.child(userId).child(title).push().setValue(myInfo)


        }

        //구글 로그인
        LoginActivity.googleCurrentUser?.let {
            val userId = it.id.toString()
            val title = "profile"
            val myInfo = UserInfo(userId,userName, introduce,state, birthDate, personality, sex, imageUri)
            LoginActivity.users.child("isVaildUser").child(myInfo.userId).setValue(ValidUserInfo(true))
            LoginActivity.myInfo = myInfo
            users.child(userId).child(title).push().setValue(myInfo)


        }

    }


}