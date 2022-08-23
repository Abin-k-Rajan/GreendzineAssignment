package com.example.assignment.Interface;

import com.example.assignment.model.PageInfo;
import com.example.assignment.model.UserInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JSONPlaceholder {
    @GET("users?page=2")
    Call<PageInfo> getUserInfo();
}
