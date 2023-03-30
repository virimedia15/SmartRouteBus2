package mx.tecnm.cdhidalgo.smartroutebus

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgotPassActivity : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)
        email=findViewById(R.id.email)
        //crear instancia
        auth=FirebaseAuth.getInstance()
    }
    fun send(view: View){
        //que el cuadro de texto no este vacio
        val email=email.text.toString()

        if(!TextUtils.isEmpty(email)){
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this){
                    task ->

                    if(task.isSuccessful){
                        startActivity(Intent(this, LoginActivity::class.java))
            }else{
                        Toast.makeText(this,"Error al enviar el email", Toast.LENGTH_SHORT).show()
            }
            }
        }

    }
}