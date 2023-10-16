package com.example.chatapplication.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.chatapplication.MainActivity
import com.example.chatapplication.R
import com.example.chatapplication.StartActivity
import com.example.chatapplication.databinding.ActivityLoginBinding
import com.example.chatapplication.dto.UserInfo
import com.example.chatapplication.dto.ValidUserInfo
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission

class LoginActivity : AppCompatActivity() {

    companion object {
        var googleCurrentUser: GoogleSignInAccount? = null
        var kakaoCurrentUser: User? = null
        var myInfo: UserInfo = UserInfo("", "", "", "", "", "", "", "")
        lateinit var database: FirebaseDatabase
        lateinit var users: DatabaseReference

        val REQUEST_CODE = 1010
    }

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private val RC_SIGN_IN = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        requestPermission()
        initView()
        bindView()
    }


    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)

    }

    override fun onDestroy() {
        super.onDestroy()
        googleSignOut()
        kakaoSignout()
    }


    private fun initView() {
        database = FirebaseDatabase.getInstance()
        users = database.getReference("users")
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        oneTapClient = Identity.getSignInClient(this)

    }

    private fun bindView() {
        binding.googleButton.setOnClickListener {
            startGoogleLogin()
        }

        binding.kakaotalkButton.setOnClickListener {
            startKakaotalkLogin()
        }
    }


    private fun requestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_MEDIA_VIDEO
                    ) == PackageManager.PERMISSION_GRANTED

            -> {

                //GRANTED

            }
//            shouldShowRequestPermissionRationale() -> {
//
//            }
            else -> requestPermissions(
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
                ), REQUEST_CODE
            )



        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            REQUEST_CODE -> {
                if(grantResults.isNotEmpty()) {
                    for(result in grantResults) {
                        if(result != PackageManager.PERMISSION_GRANTED)
                            finish()

                    }
                    //GRANTED
                }
                else {
                    finish()
                }

            }
            else -> {


            }



        }

    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        moveToNextActivity(completedTask)
    }

    private fun moveToNextActivity(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            googleCurrentUser = account

            val myUserId = account.id.toString()
            //Toast.makeText(this, "${account.displayName}님 환영합니다", Toast.LENGTH_SHORT).show()

            //val shared = getSharedPreferences("userId",Activity.MODE_PRIVATE)
            //val result = shared.getString("myUserId","null")
            Toast.makeText(this, "${account.displayName}님 로그인 되었습니다", Toast.LENGTH_SHORT).show()

            getUserProfile(myUserId)
            updateProfile(myUserId)


//            val path = users.child(myUserId).child("profile").push()
//            val key = path.key
//            path.setValue(myInfo)


//            if(!myInfo.isExist){
////                with(shared.edit()){
////                    putString("myUserId",myUserId)
////                    commit()
////                }
//
//                updateProfile(myUserId)
//                getUserProfile(myUserId)
//                users.child(myUserId).child("profile").removeValue()
//            }


            //Toast.makeText(this,"${myInfo.imageUri}",Toast.LENGTH_LONG).show()
//
//            if (!myInfo.isExist) {
//                //프로필이 존재하지 않는 경우
//
//                //myInfo.isExist = true
//                //updateProfile(myInfo.userId)
//
//
//                val intent = Intent(this, StartActivity::class.java)
//                startActivity(intent)
//            } else {
//                //프로필이 존재하는 경우
//
//                //Toast.makeText(this, "${myInfo.imageUri}", Toast.LENGTH_LONG).show()
//
//
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//            }
        } catch (e: ApiException) {
            Log.w("failed", "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun startGoogleLogin() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }


    private fun getUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("FAILED", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                kakaoCurrentUser = user
                getUserProfile(user.id.toString())

                Toast.makeText(
                    this,
                    "${user.kakaoAccount?.profile?.nickname}님 로그인 되었습니다",
                    Toast.LENGTH_SHORT
                ).show()
                val myUserId = kakaoCurrentUser?.id.toString()
                updateProfile(myUserId)

                Log.i(
                    "SUCCESS", "사용자 정보 요청 성공" +
                            "\n회원번호: ${user.id}" +
                            "\n이메일: ${user.kakaoAccount?.email}" +
                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                            "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                )
            }
        }
    }


    private fun startKakaotalkLogin() {


        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("FAILED", "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                getUserInfo()


//                val shared = getSharedPreferences("kakaoUserId", Activity.MODE_PRIVATE)
//                val result = shared.getString("myUserId", "null")


//
//                if (!myInfo.isExist) {
//                    //프로필이 존재하지 않는 경우
////                    with(shared.edit()) {
////                        putString("myUserId", myUserId)
////                        commit()
////                    }
//
//                    myInfo.isExist = true
//
//                    val intent = Intent(this, StartActivity::class.java)
//                    startActivity(intent)
//                } else {
//                    //프로필이 존재허는 경우
//
//
//
//                    val intent = Intent(this, MainActivity::class.java)
//                    startActivity(intent)
//                }

                Log.i("SUCCESS", "카카오계정으로 로그인 성공 ${token.accessToken}")
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e("FAILED", "카카오톡으로 로그인 실패", error)

                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {


                    getUserInfo()

//                    val myUserId = kakaoCurrentUser?.id.toString()
//                    Toast.makeText(this, "${kakaoCurrentUser?.kakaoAccount?.profile?.nickname}님 로그인 되었습니다", Toast.LENGTH_SHORT).show()
//
//
////                    val shared = getSharedPreferences("kakaoUserId", Activity.MODE_PRIVATE)
////                    val result = shared.getString("myUserId", "null")
//
//                    Toast.makeText(this, "${kakaoCurrentUser?.kakaoAccount?.profile?.nickname}님 로그인 되었습니다", Toast.LENGTH_SHORT).show()
//                    updateProfile(myUserId)
                    Log.i("SUCCESS", "카카오계정으로 로그인 성공 ${token.accessToken}")
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

    private fun updateProfile(myUserId: String) {

        val ref = users.child("isVaildUser").child(myUserId)
        //val key = ref.key.toString()
        ref.get().addOnCompleteListener {
            if (it.isComplete) {
                val result = it.result.value
                if (result == null) {
                    users.child("isVaildUser").child(myUserId).setValue(ValidUserInfo(false))
                    //Toast.makeText(this, "${kakaoCurrentUser?.kakaoAccount?.profile?.nickname}님 로그인 되었습니다", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, StartActivity::class.java)
                    startActivity(intent)
                } else {
                    val validUserInfo = result as Map<*, *>
                    val isValid = validUserInfo.get("valid").toString().toBoolean()

                    if (isValid) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this, StartActivity::class.java)
                        startActivity(intent)

                    }

                    // Toast.makeText(this, "${isValid}", Toast.LENGTH_LONG).show()

                }

            }

            //Toast.makeText(this,"${it.result.value}",Toast.LENGTH_LONG).show()
        }


    }


//
//
//
//        users.child(myUserId).child("profile").removeValue()
//        users.child(myUserId).child("profile").push().setValue(myInfo)


    private fun googleSignOut() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        mGoogleSignInClient.signOut()
            .addOnCompleteListener {
                Toast.makeText(this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
            }

    }

    private fun kakaoSignout() {

        // 로그아웃
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e("FAILURE", "로그아웃 실패. SDK에서 토큰 삭제됨", error)

            } else {
                Log.i("SUCCESS", "로그아웃 성공. SDK에서 토큰 삭제됨")
            }
        }
    }

    private fun getUserProfile(userId: String) {

        users.child(userId).child("profile")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        val userInfo = data.value as Map<*, *>
                        val userId = userInfo.get("userId").toString()
                        val userName = userInfo.get("userName").toString()
                        val introduce = userInfo.get("introduce").toString()
                        val state = userInfo.get("state").toString()
                        val birthDate = userInfo.get("birthDate").toString()
                        val personality = userInfo.get("personality").toString()
                        val sex = userInfo.get("sex").toString()
                        val imageUri = userInfo.get("imageUri").toString()
                        val storyCount = userInfo.get("storyCount").toString().toInt()
                        val followingCount = userInfo.get("followingCount").toString().toInt()
                        val followerCount = userInfo.get("followerCount").toString().toInt()


                        myInfo = UserInfo(
                            userId,
                            userName,
                            introduce,
                            state,
                            birthDate,
                            personality,
                            sex,
                            imageUri,
                            storyCount,
                            followingCount,
                            followerCount
                        )


                        //Toast.makeText(this@LoginActivity,"${myInfo.isExist}",Toast.LENGTH_LONG).show()

//
//                    if (!myInfo.isExist) {
//                        //프로필이 존재하지 않는 경우
//
//
//
//                        val intent = Intent(this@LoginActivity, StartActivity::class.java)
//                        startActivity(intent)
//                    } else {
//                        //프로필이 존재하는 경우
//
//                        //Toast.makeText(this, "${myInfo.imageUri}", Toast.LENGTH_LONG).show()
//
//
//                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
//                        startActivity(intent)
//                    }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }


            })


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        when (requestCode) {
            RC_SIGN_IN -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            }


        }


    }
}