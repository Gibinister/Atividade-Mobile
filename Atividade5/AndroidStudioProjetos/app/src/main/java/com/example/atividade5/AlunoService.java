package com.example.atividade5;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AlunoService {
    @POST("alunos")
    Call<Aluno> criarAluno(@Body Aluno aluno);

    @GET("alunos")
    Call<List<Aluno>> listarAlunos();
}