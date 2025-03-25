package mx.edu.itson.practica10

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class Registro : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)

        auth = Firebase.auth

        val email:EditText = findViewById(R.id.editTextTextEmailAddress)
        val password:EditText = findViewById(R.id.editTextTextPassword)
        val confirmPassword:EditText = findViewById(R.id.editTextTextConfirmPassword)
        val tvError:TextView = findViewById(R.id.tvError)

        val button:Button = findViewById(R.id.btRegistrar)

        tvError.visibility = View.INVISIBLE

        button.setOnClickListener {
            if (email.text.isBlank() || password.text.isBlank()  || confirmPassword.text.isBlank()) {
                tvError.text = "Asegúrese de llenar todos los campos"
                tvError.visibility = View.VISIBLE
            } else if (!password.text.toString().equals(confirmPassword.text.toString())) {
                tvError.text = "Asegúrese de que ambas contraseñas coincidan"
                tvError.visibility = View.VISIBLE
            } else {
                tvError.visibility = View.INVISIBLE
                signIn(email.text.toString(), password.text.toString())
            }
        }

        /**
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        */
    }

    fun signIn(email:String, password:String) {
        Log.d("INFO", "email: ${email}, password: ${password}")
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d("INFO", "signInWithEmail:success")
                val user = auth.currentUser
                val intent = Intent(this, Usuario::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } else {
                Log.w("ERROR", "signInWithEmail:failure", task.exception)
                Toast.makeText(baseContext, "El registro falló", Toast.LENGTH_SHORT).show()
            }
        }
    }

}