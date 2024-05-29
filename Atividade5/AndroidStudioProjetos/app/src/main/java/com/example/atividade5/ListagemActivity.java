package com.example.atividade5;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListagemActivity extends AppCompatActivity {
    private ListView lvAlunos;
    private List<Aluno> listaAlunos;
    private ArrayAdapter<Aluno> adapter;
    private AlunoService alunoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem);

        lvAlunos = findViewById(R.id.lvAlunos);
        listaAlunos = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaAlunos);
        lvAlunos.setAdapter(adapter);

        Retrofit retrofitMockApi = new Retrofit.Builder()
                .baseUrl("https://651c9a7135bd4107e372f930.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        alunoService = retrofitMockApi.create(AlunoService.class);

        listarAlunos();
    }

    private void listarAlunos() {
        alunoService.listarAlunos().enqueue(new Callback<List<Aluno>>() {
            @Override
            public void onResponse(Call<List<Aluno>> call, Response<List<Aluno>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaAlunos.clear();
                    listaAlunos.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ListagemActivity.this, "Erro ao listar alunos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Aluno>> call, Throwable t) {
                Toast.makeText(ListagemActivity.this, "Erro ao listar alunos", Toast.LENGTH_SHORT).show();
                Log.e("ListagemActivity", "Erro ao listar alunos", t);
            }
        });
    }
}
