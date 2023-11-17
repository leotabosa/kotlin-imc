package com.example.imc

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        verificarEntradaDados();
        aoClicarNoBotao();
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun verificarEntradaDados(){
        val peso = findViewById<View>(R.id.peso) as EditText
        val altura = findViewById<View>(R.id.altura) as EditText

        adicionarListener(peso);
        adicionarListener(altura);
    }

    fun aoClicarNoBotao(){
        val botao = findViewById<View>(R.id.botaocalcular) as Button
        botao.setOnClickListener {
            calcularIMC();
            botao.hideKeyboard();
        }
    }

    // Liberar botão
    fun alterarEstadoBotao(estado: Boolean) {
        val botao = findViewById<View>(R.id.botaocalcular) as Button
        botao.isEnabled = estado;
    }

    // Adicionar listeners nos inputs
    fun adicionarListener(campo: EditText) {
        val peso = findViewById<View>(R.id.peso) as EditText
        val altura = findViewById<View>(R.id.altura) as EditText


        campo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val valorPeso = peso.text.toString()
                val valorAltura = altura.text.toString()
                alterarEstadoBotao(ehValido(valorPeso) && ehValido(valorAltura));
            }
        })

        campo.setOnKeyListener(View.OnKeyListener setOnKeyListener@{ v, keyCode, event ->
            val botao = findViewById<View>(R.id.botaocalcular) as Button

            val valorPeso = peso.text.toString()
            val valorAltura = altura.text.toString()
            when {((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN) && ehValido(valorPeso) && ehValido(valorAltura)) -> {
                botao.performClick()

                //return true
                return@setOnKeyListener true
            }

                else -> false
            }
        })
    }

    fun calcularIMC() {
        val peso = findViewById<View>(R.id.peso) as EditText
        val altura = findViewById<View>(R.id.altura) as EditText
        val valorPeso = peso.text.toString().replace(",",".").toDouble();
        val valorAltura = altura.text.toString().replace(",",".").toDouble();

        val resultado = valorPeso / valorAltura.pow(2.0)
        val classificao = definirClassificao(resultado);

        val textoResultado = findViewById<View>(R.id.resultado) as TextView
        textoResultado.visibility = View.VISIBLE;
        textoResultado.text = "%.2f".format(resultado);

        val labelResultadoUm = findViewById<View>(R.id.labelresultado) as TextView
        labelResultadoUm.visibility = View.VISIBLE;
        val labelResultadoDois = findViewById<View>(R.id.labelresultado2) as TextView
        labelResultadoDois.visibility = View.VISIBLE;
        labelResultadoDois.text = classificao;
    }

    fun definirClassificao(resultado: Double): CharSequence {
        if (resultado < 18.5) {
            return "Abaixo do peso normal";
        } else if (resultado > 18.5 && resultado <= 24.9) {
            return "Peso normal";
        } else if (resultado > 24.9 && resultado <= 29.9) {
            return "Excesso de peso";
        } else if (resultado > 29.9 && resultado <= 34.9) {
            return "Obesidade classe I";
        } else if (resultado > 34.9 && resultado <= 39.9) {
            return "Obesidade classe II";
        } else return "Obesidade classe III";
    }

    fun ehValido(valor: String): Boolean{
        return valor.isNotEmpty() && valor.replace(",",".").toDouble() > 0;
    }
}