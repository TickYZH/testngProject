package util;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpClientUtil {



    public HttpClientUtil(){

    }



    /**
     * Get 请求
     */
    public String httpGetRequest(String url){
        String reponse = "";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                reponse = EntityUtils.toString(httpResponse.getEntity());
            }

            httpResponse.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }
        return reponse;
    }

    /**
     * Post 请求
     */
    public String httpPostRequest(String url, Map bodyMap){
        String reponse = "";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost();
        Set<Map.Entry<String, String>> entrySet = bodyMap.entrySet();
        List params = new ArrayList();

        for (Map.Entry<String, String> e : entrySet){
            NameValuePair pair = new BasicNameValuePair(e.getKey(), e.getValue());
            params.add(pair);
        }

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                reponse = EntityUtils.toString(httpResponse.getEntity());
            }

            httpResponse.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }
        return reponse;
    }
}
