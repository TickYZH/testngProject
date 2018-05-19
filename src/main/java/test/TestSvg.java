package test;

import util.HttpClientUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestSvg {


    public static void main(String[] args){
         HttpClientUtil httpRequest = new HttpClientUtil();

         String res = httpRequest.httpGetRequest("https://img.carfree.net/car-park/42/c4/qkwMi2wf0qSnUGu.svg");

//        System.out.println(res);

        String reg1 = "<g(\\sid=\"park)[\\s\\S]*?>";
        String reg2 = "<text[^>]*?>([^<]+)</text>";
        String reg3 = ">.*<";
        String regId = "park";

        /**
         * 校验g标签id
         */
        Pattern p0 = Pattern.compile(reg1);
        Matcher m0 = p0.matcher(res);
        while (m0.find()){
            System.out.println(m0.group());
            Pattern pId = Pattern.compile(regId);
            Matcher mId = pId.matcher(m0.group());

            System.out.println(mId.group());
        }

        /**
         * 校验文本标签
         */
        Pattern p1 = Pattern.compile(reg2);
        Matcher m1 = p1.matcher(res);
        List<String> textList = new ArrayList();
        while (m1.find()){
            Pattern p2 = Pattern.compile(reg3);
            Matcher m2 = p2.matcher(m1.group());

            if (m2.find()){
                String targetStr = m2.group();

                String textStr = targetStr.substring(1, targetStr.length() - 1);
                textList.add(textStr);
            }else {

            }
        }

        TestSvg t = new TestSvg();

//        t.checkRepeat(textList);
    }

    /**
     * 判重
     */
    public Boolean checkRepeat(List list){
        Boolean flag = true;
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size();j++){
                if (list.get(i).equals(list.get(j))){
                    System.out.println("有重复标签:" + list.get(i).toString());
                    flag = false;
                }
            }
        }

        return flag;
    }

}
