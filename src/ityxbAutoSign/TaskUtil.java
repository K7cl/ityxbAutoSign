package ityxbAutoSign;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

public class TaskUtil {
	private final String taskUrl = "http://stu.ityxb.com/back/bxg/my/sign/conditionList";
	private final String signUrl = "http://stu.ityxb.com/back/bxg/my/sign/number/updateSignStatus";
	
	public void process(AccountUtil obj){
		Map<String, String> classes = obj.getUserClass();
		for (Map.Entry<String, String> entry : classes.entrySet()) {
			System.out.println("Check: " + entry.getValue());
			checkTask(obj, entry.getKey(), entry.getValue());
		}
	}
	
	public void checkTask(AccountUtil obj, String id, String name){
		Map<String, String> bodyMap = new HashMap<>();
		bodyMap.put("stuSignStatus", "");
		bodyMap.put("signType", "");
		bodyMap.put("signName", "");
		bodyMap.put("pageSize", "10");
		bodyMap.put("pageNumber", "1");
		bodyMap.put("course_id", id);
		HttpUtil http = new HttpUtil();
		http.post(obj, taskUrl, bodyMap);
		http.post(obj, taskUrl, bodyMap, new HttpUtil.HttpCallback() {
            @Override
            public void onFailure(Call call, IOException e) {
            	System.out.println("Check task error:");
            	e.printStackTrace();
            }

            @Override
            public void onSuccess(Response response) throws IOException {
                String resBody = response.body().string();
                try {
                    JSONObject resJson = JSONObject.parseObject(resBody);
                    JSONArray taskList = resJson.getJSONObject("resultObject").getJSONArray("items");
                    for (int i=0;i<taskList.size();i++) {
                    	System.out.println("Course: " + name + ", have sign: " + taskList.getJSONObject(i).getString("signName"));
                    	if (taskList.getJSONObject(i).getIntValue("signStatus") == 1 && taskList.getJSONObject(i).getIntValue("stuSignStatus") == -1) {
                    		if (taskList.getJSONObject(i).getIntValue("signType") == 2) {
                    			sign(obj, taskList.getJSONObject(i).getString("signId"),taskList.getJSONObject(i).getIntValue("signNum"), name, taskList.getJSONObject(i).getString("signName"));
                    		}
                    	}
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        
	}
	
	
	private void sign(AccountUtil obj, String id, int num, String name, String signName){
		Map<String, String> bodyMap = new HashMap<>();
		bodyMap.put("sign_id", id);
		bodyMap.put("sign_num", String.valueOf(num));
		HttpUtil http = new HttpUtil();
		http.post(obj, signUrl, bodyMap, new HttpUtil.HttpCallback() {
            @Override
            public void onFailure(Call call, IOException e) {
            	System.out.println("Sign error:");
            	e.printStackTrace();
            }

            @Override
            public void onSuccess(Response response) throws IOException {
                String resBody = response.body().string();
                try {
                    JSONObject resJson = JSONObject.parseObject(resBody);
                    boolean success = resJson.getBooleanValue("success");
                    if (success) {
                    	System.out.println("Course: " + name + ", signName: " + signName + ", id: " + id + ", signNum: " + String.valueOf(num) + ", sign ityxb OK!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
	}

}
