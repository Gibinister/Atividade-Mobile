package com.example.atividade5;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CadastroActivity extends AppCompatActivity {
    private EditText etRa, etNome, etCep, etLogradouro, etComplemento, etBairro, etCidade, etUf;
    private Button btnSalvar;
    private ViaCEPService viaCEPService;
    private AlunoService alunoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        etRa = findViewById(R.id.etRa);
        etNome = findViewById(R.id.etNome);
        etCep = findViewById(R.id.etCep);
        etLogradouro = findViewById(R.id.etLogradouro);
        etComplemento = findViewById(R.id.etComplemento);
        etBairro = findViewById(R.id.etBairro);
        etCidade = findViewById(R.id.etCidade);
        etUf = findViewById(R.id.etUf);
        btnSalvar = findViewById(R.id.btnSalvar);

        Retrofit retrofitViaCep = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        viaCEPService = retrofitViaCep.create(ViaCEPService.class);

        Retrofit retrofitMockApi = new Retrofit.Builder()
                .baseUrl("https://651c9a7135bd4107e372f930.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        alunoService = retrofitMockApi.create(AlunoService.class);

        etCep.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                buscarEndereco(etCep.getText().toString());
            }
        });

        btnSalvar.setOnClickListener(v -> {
            Aluno aluno = new Aluno(
                    Integer.parseInt(etRa.getText().toString()),
                    etNome.getText().toString(),
                    etCep.getText().toString(),
                    etLogradouro.getText().toString(),
                    etComplemento.getText().toString(),
                    etBairro.getText().toString(),
                    etCidade.getText().toString(),
                    etUf.getText().toString()
            );
            salvarAluno(aluno);
        });
    }

    private void buscarEndereco(String cep) {
        viaCEPService.getEndereco(cep).enqueue(new Callback<Endereco>() {
            @Override
            public void onResponse(Call<Endereco> call, Response<Endereco> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Endereco endereco = response.body();
                    etLogradouro.setText(endereco.getLogradouro());
                    etComplemento.setText(endereco.getComplemento());
                    etBairro.setText(endereco.getBairro());
                    etCidade.setText(endereco.getLocalidade());
                    etUf.setText(endereco.getUf());
                }
            }

            @Override
            public void onFailure(Call<Endereco> call, Throwable t) {
                Toast.makeText(CadastroActivity.this, "Erro ao buscar endereço", Toast.LENGTH_SHORT).show();
                Log.e("CadastroActivity", "Erro ao buscar endereço", t);
            }
        });
    }

    private void salvarAluno(Aluno aluno) {
        alunoService.criarAluno(aluno).enqueue(new Callback<Aluno>() {
            @Override
            public void onResponse(Call<Aluno> call, Response<Aluno> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(CadastroActivity.this, "Aluno salvo com sucesso!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(CadastroActivity.this, "Erro ao salvar aluno", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Aluno> call, Throwable t) {
                Toast.makeText(CadastroActivity.this, "Erro ao salvar aluno", Toast.LENGTH_SHORT).show();
                Log.e("CadastroActivity", "Erro ao salvar aluno", t);
            }
        });
    }
}