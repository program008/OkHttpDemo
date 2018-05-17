package com.uurobot.okhttpdemo.interceptor;

import android.util.Log;

import java.io.IOException;
import java.nio.Buffer;
import java.util.Iterator;
import java.util.Set;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author:
 * @version:
 * @package:
 * @fileName:
 * @description: URL重定向
 * @date:
 * @time:
 */
public class UrlRedirectInterceptor implements Interceptor {

	private static final String TAG = "UrlRedirectInterceptor";

	private String newHost = "127.0.0.1";

	private String path1 = "/test/upload/img";

	private String path2 = "/test/upload/voice";
	public static String requestBodyToString(RequestBody requestBody) throws IOException {
		StringBuffer buffer = new StringBuffer();
		//requestBody.writeTo(buffer);
		//return buffer.readUtf8();
		return null;
	}

	@Override
	public Response intercept(Interceptor.Chain chain) throws IOException {
		Request request = chain.request();
		Response response = chain.proceed(request);
		HttpUrl url = request.url();
		//http://127.0.0.1/test/upload/img?userName=xiaoming&userPassword=12345
		String scheme = url.scheme();//  http https
		String host = url.host();//   127.0.0.1
		String path = url.encodedPath();//  /test/upload/img
		String query = url.encodedQuery();//  userName=xiaoming&userPassword=12345
		StringBuffer sb = new StringBuffer();
		sb.append(scheme).append(newHost).append(path).append("?").append(query).toString();

		//加密查询内容
		Set<String> queryList = url.queryParameterNames();
		Iterator<String> iterator = queryList.iterator();
		for (int i = 0; i < queryList.size(); i++) {

			String queryName = iterator.next();
			sb.append(queryName).append("=");
			String queryKey = url.queryParameter(queryName);
			//对query的key进行加密
			//sb.append(CommonUtils.getMD5(queryKey));
			if (iterator.hasNext()) {
				sb.append("&");
			}
		}


		String newUrl = sb.toString();


		RequestBody body = request.body();
		String bodyToString = requestBodyToString(body);
		//TestBean testBean = GsonTools.changeGsonToBean(bodyToString, TestBean.class);
		//String userPassword = testBean.getUserPassword();
		//加密body体中的用户密码
		//testBean.setUserPassword(CommonUtils.getMD5(userPassword));

		//String testGsonString = GsonTools.createGsonString(testBean);
		//RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), testGsonString);


		Request.Builder builder = request.newBuilder()
			//.post(requestBody)
			.url(newUrl);
		//HEAD动态添加
		switch (1) {
			//case path1:
			case 1:
				builder.addHeader("token", "token");
				break;
			case 2:
			//case path2:
				builder.addHeader("token", "token");
				builder.addHeader("uid", "uid");
				break;
		}



		return chain.proceed(builder.build());
	}

	private void log(Request request,Response response) throws IOException {
		RequestBody body = request.body();
		String bodyToString = requestBodyToString(body);
		if (response != null) {
			ResponseBody responseBody = response.body();
			long contentLength = responseBody.contentLength();
			String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";

			Log.e(TAG,response.code() + ' '
				+ response.message() + ' '
				+ response.request().url()+' '
				+ bodySize
			);

			Headers headers = response.headers();
			for (int i = 0, count = headers.size(); i < count; i++) {
				Log.e(TAG,headers.name(i) + ": " + headers.value(i));
			}


		}
	}
}