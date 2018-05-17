package com.uurobot.okhttpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.uurobot.okhttpdemo.interceptor.UrlRedirectInterceptor;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		OkHttpClient build = new OkHttpClient.Builder()
			//.addInterceptor(new UrlRedirectInterceptor())
			.build();
	}
}
