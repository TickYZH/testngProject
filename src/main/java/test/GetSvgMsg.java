package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetSvgMsg {


    public static void main(String[] args){


        String svgStr ="<g id=\"park_x5F_no_x5F_1\">\n" +
                "\t<g id=\"XMLID_1453_\">\n" +
                "\t\t\n" +
                "\t\t\t<rect id=\"XMLID_2037_\" x=\"416.6\" y=\"25.8\" transform=\"matrix(0.9846 -0.1748 0.1748 0.9846 0.2969 74.1496)\" class=\"st0\" width=\"9\" height=\"19.2\"/>\n" +
                "\t</g>\n" +
                "\t<g id=\"XMLID_1304_\">\n" +
                "\t\t<text id=\"XMLID_532_\" transform=\"matrix(0.1714 0.9852 -0.9852 0.1714 418.8044 34.739)\" class=\"st1 st2 st3\">1</text>\n" +
                "\t</g>\n" +
                "</g>\n" +
                "<g id=\"park_x5F_no_x5F_67_1_\">\n" +
                "\t<g id=\"XMLID_1307_\">\n" +
                "\t\t\n" +
                "\t\t\t<rect id=\"XMLID_2046_\" x=\"637.1\" y=\"3.9\" transform=\"matrix(-0.1124 -0.9937 0.9937 -0.1124 700.3182 652.6087)\" class=\"st0\" width=\"9\" height=\"19.2\"/>\n" +
                "\t</g>\n" +
                "\t<g id=\"XMLID_1301_\">\n" +
                "\t\t<text id=\"XMLID_530_\" transform=\"matrix(0.9959 -9.028000e-02 9.028000e-02 0.9959 638.3386 15.9491)\" class=\"st1 st2 st3\">67</text>\n" +
                "\t</g>\n" +
                "</g>\n" +
                "<g id=\"park_x5F_no_x5F_68_1_\">\n" +
                "\t<g id=\"XMLID_1309_\">\n" +
                "\t\t\n" +
                "\t\t\t<rect id=\"XMLID_2047_\" x=\"618.1\" y=\"6.1\" transform=\"matrix(-0.1124 -0.9937 0.9937 -0.1124 676.9647 636.1512)\" class=\"st0\" width=\"9\" height=\"19.2\"/>\n" +
                "\t</g>\n" +
                "\t<g id=\"XMLID_1298_\">\n" +
                "\t\t<text id=\"XMLID_528_\" transform=\"matrix(0.9959 -9.028000e-02 9.028000e-02 0.9959 619.7552 18.1777)\" class=\"st1 st2 st3\">68</text>\n" +
                "\t</g>\n" +
                "</g>";

        String reg1 = "<g(\\s|\\S)id=('|\")park[\\s\\S]*>[\\s\\S]*<\\/g>";
        String reg2 = "<text[^>]*>([^<]+)</text>";
        String reg3 = ">[0-9]*<";



        Pattern p1 = Pattern.compile(reg1);
        Matcher m1 = p1.matcher(svgStr);


        while (m1.find()){
            String s2 = m1.group();
            Pattern p2 = Pattern.compile(reg2);
            Matcher m2 = p2.matcher(s2);

            System.out.println(s2);
            while (m2.find()){
                String s3 = m2.group();
                Pattern p3 = Pattern.compile(reg3);
                Matcher m3 = p3.matcher(s3);

                System.out.println(s3);
                while (m3.find()){
                    String s4 = m3.group(0);

                    System.out.println(s4);
                }
            }
        }



    }
}
