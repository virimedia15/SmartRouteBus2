package mx.tecnm.cdhidalgo.smartroutebus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegistroActivity : AppCompatActivity() {
 //Declaracion de variables
    private lateinit var nombre:EditText
    private lateinit var email:EditText
    private lateinit var pass:EditText
    private lateinit var tel:EditText
    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        //Llamar Vistas
        nombre=findViewById(R.id.etNombre)
        email=findViewById(R.id.email)
        pass=findViewById(R.id.pass)
        tel=findViewById(R.id.tel)

        //Instanciar BD y autentificacion
        database= FirebaseDatabase.getInstance()
        auth=FirebaseAuth.getInstance()

        //Ref para leer o escribir en una ubicacion}
        dbReference=database.reference.child("User")

    }
    //Metodo onclick
    fun btn_guardar(view: View){
        //llamar metodo para ejecutar monmento de presionar guardar
        createNewAccount()

    }
    //obtener valores caja de texto
    private fun createNewAccount(){
        val name:String=nombre.text.toString()
        val email:String=email.text.toString()
        val pass:String=pass.text.toString()
        val tel:String=tel.text.toString()

        //validar campos
        if(!TextUtils.isEmpty(name) &&!TextUtils.isEmpty(email) &&!TextUtils.isEmpty(pass)
            &&!TextUtils.isEmpty(tel)){

            //dar de alta user y pass
            auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this){
                    //verificar que el registro se realizo correctamente
                    task ->

                    if(task.isComplete){
                        val user:FirebaseUser?=auth.currentUser
                        //enviar el correo
                        verificacionEmail(user)
                        //otra ubicacion donde se encontrara nombre
                        val userBD=dbReference.child(user?.uid!!)

                        userBD.child("nombre").setValue(nombre)
                        userBD.child("tel").setValue(tel)
                        acccion()

                    }


                }
        }
    }

    //nuevo metodo accion si se realiza correctamente
    private fun acccion(){
        //mandar vista login
        startActivity(Intent(this, LoginActivity::class.java))

    }
    //? llamada segura
    //enviar email a usuario que se registro correctamente
    private fun verificacionEmail(user: FirebaseUser?){
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this){
                task ->
                //se realizo la tarea complegtamente
                if(task.isComplete){
                    Toast.makeText(this,"Email enviado",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,"Error al enviar el email",Toast.LENGTH_SHORT).show()

                }
            }

    }
}