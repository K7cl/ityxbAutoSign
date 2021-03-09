package ityxbAutoSign;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {
	OkHttpClient client = new OkHttpClient();
	
	public String post(AccountUtil obj, String url, Map<String, String> bodyMap){
		FormBody.Builder builder = new FormBody.Builder();
    	if (bodyMap != null) {
    		for (Map.Entry<String, String> entry : bodyMap.entrySet()) {
    			builder.add(entry.getKey(), entry.getValue());
    		}
    	}
    	RequestBody formBody = builder.build();
    	
    	Request.Builder reqBuilder = new Request.Builder();
    	List<Cookie> cookies = obj.getCookies();
    	if (cookies != null) {
    		StringBuilder cookieStr = new StringBuilder();
        	for(Cookie cookie : cookies){
    			cookieStr.append(cookie.name()).append("=").append(cookie.value()+";");
    		}
        	reqBuilder.addHeader("Cookie", cookieStr.toString());
    	}
		reqBuilder.url(url);
		reqBuilder.post(formBody);
		Request request = reqBuilder.build();
		try (Response response = client.newCall(request).execute()) {
			Headers headers = response.headers();
			HttpUrl loginUrl = request.url();
	        cookies = Cookie.parseAll(loginUrl, headers);
	        obj.setCookies(cookies);
			return response.body().string();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

    public void post(AccountUtil obj, String url, Map<String, String> bodyMap, final HttpCallback cb) {
    	FormBody.Builder builder = new FormBody.Builder();
    	if (bodyMap != null) {
    		for (Map.Entry<String, String> entry : bodyMap.entrySet()) {
    			builder.add(entry.getKey(), entry.getValue());
    		}
    	}
    	RequestBody formBody = builder.build();
    	Request.Builder reqBuilder = new Request.Builder();
    	List<Cookie> cookies = obj.getCookies();
    	if (cookies != null) {
    		StringBuilder cookieStr = new StringBuilder();
        	for(Cookie cookie : cookies){
    			cookieStr.append(cookie.name()).append("=").append(cookie.value()+";");
    		}
        	reqBuilder.addHeader("Cookie", cookieStr.toString());
    	}
		reqBuilder.url(url);
		reqBuilder.post(formBody);
		Request request = reqBuilder.build();
        
        client.newCall(request).enqueue( new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                cb.onFailure(null, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    cb.onFailure(call, null);
                    return;
                }
                cb.onSuccess(response);
            }
        });
    }
    
    public void get(String url, final HttpCallback cb) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        
        client.newCall(request).enqueue( new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                cb.onFailure(null, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    cb.onFailure(call, null);
                    return;
                }
                cb.onSuccess(response);
            }
        });
    }
	
	public interface HttpCallback  {
        public void onFailure(Call call, IOException e);
        public void onSuccess(Response responset) throws IOException;
    }

}
