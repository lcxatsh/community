package life.majing.community.provider;

import com.alibaba.fastjson.JSON;
import life.majing.community.dto.AccessTokenDTO;
import life.majing.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

/**
 * Created by liangchunxiao on 2020/5/7.
 */
@Component
public class GithubProvider {

    public String getAccessToken(AccessTokenDTO dto){
        MediaType mediaType= MediaType.get("application/json; charset=utf-8");
        System.out.println("=====" + dto.toString());
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType,JSON.toJSONString(dto));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {

            String str = response.body().string();
            System.out.println(str);
            String accessToken = str.split("&")[0].split("=")[1];
            System.out.println(accessToken);
            return accessToken;
        }catch (IOException e){

        }
        return "";
    }

    public GithubUser getUser(String access_token){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" +access_token)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String str = response.body().string();
            System.out.println("getUser==="+str);
            GithubUser githubUser = JSON.parseObject(str, GithubUser.class);
            return githubUser;
        }catch (IOException e){
            System.out.println("getUser===报异常了");
            e.printStackTrace();
        }
        return null;
    }
}
