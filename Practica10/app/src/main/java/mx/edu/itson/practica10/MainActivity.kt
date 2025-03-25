package mx.edu.itson.practica10

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import mx.edu.itson.practica10.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        val email:EditText = findViewById(R.id.editTextTextEmailAddress)
        val password:EditText = findViewById(R.id.editTextTextPassword)
        val tvError:TextView = findViewById(R.id.tvError)

        val btnLogIn:Button = findViewById(R.id.btEntrar)
        val btnSignIn:Button = findViewById(R.id.btRegistrar)

        tvError.visibility = View.INVISIBLE

        btnLogIn.setOnClickListener {
            logIn(email.text.toString(), password.text.toString())
        }

        btnSignIn.setOnClickListener {
            val intent = Intent(this, Registro::class.java)
            startActivity(intent)
        }

        /**
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        */
    }

    fun goToMain(user: FirebaseUser) {
        val intent = Intent(this, Usuario::class.java)
        intent.putExtra("user", user.email)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun showError(text:String = "", visible:Boolean) {
        val tvError:TextView = findViewById(R.id.tvError)
        tvError.text = text
        tvError.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            goToMain(currentUser)
        }
    }

    fun logIn(email:String, password:String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
                task ->
            if  (task.isSuccessful) {
                val user = auth.currentUser
                showError(visible = false)
                goToMain(user!!)
            } else {
                showError("Usuario y/o contraseña equivocados", true)
            }
        }
    }
}