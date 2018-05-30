package com.job.github.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.job.github.api.App;
import com.job.github.pojo.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeModel implements HomeContractModel {
    @Override
    public void loadUser(String token, final OnLoadUser onLoadUser) {
        App.getGitHubApi().getUser(token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() == null) {
                    onLoadUser.onError();
                } else {
                    onLoadUser.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                onLoadUser.onError();
            }
        });

    }

    @Override
    public void loadAvatar(String avatarUrl, final OnDownloadAvatar onDownloadAvatar) {
        App.getGitHubApi().loadAvatar(avatarUrl).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Bitmap bm = BitmapFactory.decodeStream(response.body().byteStream());
                        onDownloadAvatar.onSuccess(bm);
                        return;
                    }
                }
                onDownloadAvatar.onError();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                onDownloadAvatar.onError();
            }
        });
    }
}