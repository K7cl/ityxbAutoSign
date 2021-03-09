package ityxbAutoSign;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import okhttp3.Cookie;


public class AccountUtil {
	private final String loginUrl = "http://stu.ityxb.com/back/bxg_anon/login";
	public boolean isLogin = false;
	private final String classUrl = "http://stu.ityxb.com/back/bxg/course/getHaveList";
	private Map<String, String> classes = new HashMap<>();
	private List<Cookie> cookies;
	
	public List<Cookie> getCookies(){return cookies;}
	
	public void setCookies(List<Cookie> cookie) {
		if (cookies == null) {
			cookies = cookie;
		}else {
			for (int i=0;i<cookie.size();i++) {
				
				cookies.add(cookie.get(i));
			}
		}
	}
	
	public boolean isLogin() {return isLogin;}
	
	public void login(String username, String password){
		Map<String, String> bodyMap = new HashMap<>();
		bodyMap.put("automaticLogon", "false");
		bodyMap.put("username", username);
		bodyMap.put("password", password);
		HttpUtil http = new HttpUtil();
		String resBody = http.post(this, loginUrl, bodyMap);
        try {
            JSONObject resJson = JSONObject.parseObject(resBody);
            isLogin = resJson.getBoolean("success");
            if (isLogin()) {
            	setClass();
            }else {
            	System.out.println("Login fial!");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
	}
	
	public Map<String, String> getUserClass() {return classes;}
	
	private void setClass(){
		Map<String, String> bodyMap = new HashMap<>();
		bodyMap.put("type", "1");
		bodyMap.put("pageNumber", "1");
		bodyMap.put("pageSize", "100");
		HttpUtil http = new HttpUtil();
		String resBody = http.post(this, classUrl, bodyMap);
        try {
            JSONObject resJson = JSONObject.parseObject(resBody);
            JSONArray classList = resJson.getJSONObject("resultObject").getJSONArray("items");
            for (int i=0;i<classList.size();i++) {
            	classes.put(classList.getJSONObject(i).getString("id"),classList.getJSONObject(i).getString("name"));
            }
            getUserClass();
        } catch (JSONException e) {
            e.printStackTrace();
        }
	}

}
