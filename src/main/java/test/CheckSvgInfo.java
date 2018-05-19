package test;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yu.zuohong on 2018/4/3
 */
public class CheckSvgInfo{

    private Workbook workbook = null;
    private Sheet sheetName = null;
    private final String inPath = "C:\\Users\\yu.zuohong\\Desktop\\停车场\\svg(1)\\线上svg汇总169(3).txt";
    private final String outPath = "/Users/apple/Desktop/test/";

    private List numTextList,numPolygonList;
    private List vBoxAllList,vParkAllList,repeatAllList,gidAllList,polygonAllList,textAllList;
    private List vBoxParkNameList,vParkNameList,reParkNameList,gidParkNameList,pgParkNameList,textParkNameList;
    @BeforeClass
    public void BClass() throws Exception {
//        MEngine = new mysqlEngine("carfree_parking");

        vBoxAllList = new ArrayList();
        vParkAllList = new ArrayList();
        repeatAllList = new ArrayList();
        gidAllList = new ArrayList();
        vBoxParkNameList = new ArrayList();
        vParkNameList = new ArrayList();
        reParkNameList = new ArrayList();
        gidParkNameList = new ArrayList();
        polygonAllList = new ArrayList();
        textAllList = new ArrayList();
        pgParkNameList = new ArrayList();
        textParkNameList = new ArrayList();

        numTextList = new ArrayList();
        numPolygonList = new ArrayList();

    }

    /**
     * 本地文件校验
     * @throws Exception
     */
    @Test
    public void checkSvgInfoByLocalURL() throws Exception{
        File file = new File("/Users/apple/Desktop/svg");
        File[] list = file.listFiles();
        for (File f : list){
            if (!f.getName().contains("Store")){
                Boolean sign = checkSvg("file:///" + f.getAbsolutePath(),"",f.getName());
            }

//            if (sign == true){
//                //复制文件
//                String outPath = "C:\\Users\\yu.zuohong\\Desktop\\停车场\\无错\\" + f.getName();
//                copySVGFile(f, outPath);
//                publibs.writeFile("C:\\Users\\yu.zuohong\\Desktop\\停车场\\无错\\测试通过svg.txt",f.getName() + "\n", true);
//            }

            System.out.println(f.getName());
        }

        createExcel(vBoxParkNameList, vBoxAllList, "viewBox检查", "viewBox属性值");
        createExcel(vParkNameList, vParkAllList, "vpark检查", "vpark标签id");
        createExcel(reParkNameList, repeatAllList, "车位重复检查", "g标签id");
        createExcel(gidParkNameList, gidAllList, "g标签id规则检查", "g标签id");
        createExcel(pgParkNameList, polygonAllList, "多边形检查", "该g标签下有多边形");
        createExcel(textParkNameList, textAllList, "文本检查", "该g标签有多个text标签");
    }

    /**
     * txt读取
     * @throws Exception
     */
//    @Test
    public void checkSvgInfoBytxt() throws Exception{
        FileReader fileReader = new FileReader(inPath);
        BufferedReader reader = new BufferedReader(fileReader);
        String tempStr = null;
        while ((tempStr = reader.readLine()) != null){
            String[] strArr = tempStr.split("@");
            String parkName = strArr[0];
            String floor = strArr[1];
            String url = strArr[2];
            checkSvg(url, floor, parkName);
        }
        reader.close();
        createExcel(vBoxParkNameList, vBoxAllList, "viewBox检查", "viewBox属性值");
        createExcel(vParkNameList, vParkAllList, "vpark检查", "vpark标签id");
        createExcel(reParkNameList, repeatAllList, "车位重复检查", "g标签id");
        createExcel(gidParkNameList, gidAllList, "g标签id规则检查", "g标签id");
        createExcel(pgParkNameList, polygonAllList, "多边形检查", "该g标签下有多边形");
        createExcel(textParkNameList, textAllList, "文本检查", "该g标签有多个text标签");
    }

    public Boolean checkSvg (String svgUrl, String floor, String parkName) throws Exception{
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(svgUrl);
        Element root = document.getDocumentElement();



        //获取g标签id
        List gIdArr = getSvgGid(document);
        //打印标签
        Boolean flag = true;

        List viewBoxList = checkViewBox(document);
        if (viewBoxList.size() != 0){
            flag = false;
            vBoxAllList.addAll(viewBoxList);
            vBoxAllList.add("stop");
            vBoxParkNameList.add(parkName + floor);
        }

        List vparkList = checkVparkInVarea(document);
        if (vparkList.size() != 0){
            flag = false;
            vParkAllList.addAll(vparkList);
            vParkAllList.add("stop");
            vParkNameList.add(parkName + floor);
        }

        List repeatList = checkRepeat(gIdArr);
        if (repeatList.size() != 0){
            flag = false;
            repeatAllList.addAll(repeatList);
            repeatAllList.add("stop");
            reParkNameList.add(parkName + floor);
        }

        List gidRuleList = checkGidRule(gIdArr);
        if (gidRuleList.size() != 0){
            flag = false;
            gidAllList.addAll(gidRuleList);
            gidAllList.add("stop");
            gidParkNameList.add(parkName + floor);
        }

        checkParkPolygon(root);
        if (numPolygonList.size() != 0){
            flag = false;
            polygonAllList.addAll(numPolygonList);
            polygonAllList.add("stop");
            pgParkNameList.add(parkName + floor);
            numPolygonList.clear();
        }

        checkTextNode(root);
        if (numTextList.size() != 0){
            flag = false;
            textAllList.addAll(numTextList);
            textAllList.add("stop");
            textParkNameList.add(parkName + floor);
            numTextList.clear();
        }
        if (flag == false){
            System.out.println(svgUrl);
            System.out.println(parkName + "  " + floor + "\n");
        }
        return flag;
    }

    /**
     * 检查svg标签 viewBox数值
     */
    public List checkViewBox(Document document){
        Boolean flag = true;
        List viewBoxList = new ArrayList();
        NodeList svgNodeList = document.getElementsByTagName("svg");
        for (int i = 0; i < svgNodeList.getLength(); i++) {
            Node viewBoxNode = svgNodeList.item(i);
            NamedNodeMap svgAtt = viewBoxNode.getAttributes();

            if (svgAtt.getNamedItem("viewBox") != null){
                String viewBoxValue = svgAtt.getNamedItem("viewBox").getNodeValue();
                String[] valueArr = viewBoxValue.split(" ");
                if ("0".equals(valueArr[0]) && "0".equals(valueArr[1])){
                    flag = true;
                }else {
                    //前两位不为0
                    flag = false;
                    viewBoxList.add(svgAtt.getNamedItem("viewBox"));
                    System.out.println(svgAtt.getNamedItem("viewBox"));
                }
            }else {
                System.out.println("缺少viewBox属性");
            }
        }
        return viewBoxList;
    }
    /**
     * 循环获取g标签id， id包含park
     */
    public List getSvgGid(Document document){
        List gIdArr = new ArrayList();
        //循环获取g标签
        NodeList gList = document.getElementsByTagName("g");
        for (int i = 0; i < gList.getLength(); i++) {
            Node g = gList.item(i);
            //获取g标签属性
            NamedNodeMap attrs = g.getAttributes();
            //筛选g id标签
            for (int j = 0; j < attrs.getLength(); j++) {
                //通过item(index)方法获取g节点的id
                Node attr = attrs.item(j);
                if (attr.getNodeValue().contains("park") && "id".equals(attr.getNodeName()) && !attr.getNodeValue().contains("pid")){
                    //获取属性值
                    gIdArr.add(attr.getNodeValue());
                }
            }
        }
        return gIdArr;
    }
    /**
     * 判断g标签id非法
     */
    public List checkGidRule(List list){
        List erroList = new ArrayList();
        Boolean flag = true;
        String str,checkStr = "",firstStr = "";
        for (int i = 0; i < list.size(); i++) {
            str = list.get(i).toString();
            checkStr = str.substring(str.length() - 3, str.length());
            firstStr = str.substring(0,1);
            //检验末三位字符
            if (checkStr.matches("_[0-9]_")){
                System.out.println(str);
                flag = false;
                erroList.add(str);
            }
            //检验首位字符
            if ("_".equals(firstStr)){
                System.out.println(str);
                flag = false;
                erroList.add(str);
            }
            //检查是否为三个下划线，过滤_x5F，规范如:vpark_no_002,park_no_34
            String repalceStr = str.replace("_x5F","");
            String[] lineArr = repalceStr.split("_");
            if (lineArr.length != 3){
                System.out.println(str);
                flag = false;
                erroList.add(str);
            }
        }
        return erroList;
    }

    /**
     * 判重
     */
    public List checkRepeat(List list){
        List errorList = new ArrayList();
        Boolean flag = true;
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size();j++){
                if (list.get(i).equals(list.get(j))){
                    System.out.println(list.get(i).toString());
                    flag = false;
                    errorList.add(list.get(i).toString());
                }
            }
        }
        return errorList;
    }

    /**
     * 判断虚拟区域是否包含varea_name
     */
    public Boolean checkVareaName(Document document){
        Boolean flag = true;

        NodeList vareaList = document.getElementsByTagName("g");
        for (int i = 0; i < vareaList.getLength(); i++) {

            Boolean nameFlag = true;
            Node varea = vareaList.item(i);
            NodeList childList = varea.getChildNodes();
            //子节点
            for (int j = 0; j < childList.getLength(); j++) {
                //判断是否为区域节点
                if (varea.getAttributes().getNamedItem("id") == null){
                    break;
                }
                if (!varea.getAttributes().getNamedItem("id").toString().contains("varea_x5F_no_x5F")){
                    break;
                }
                Node childeNode = childList.item(j);
                //筛选节点
                if (childeNode.getNodeType() == Node.ELEMENT_NODE){
                    //子节点g标签
                    if ("g".equals(childeNode.getNodeName())){
                        if (childeNode.getAttributes().getNamedItem("id") != null){
                            String nodeValue = childeNode.getAttributes().getNamedItem("id").toString();
                            //判断虚拟区域名字
                            if (nodeValue.contains("varea_x5F_name")){
                                nameFlag = true;
                            }else {
                                nameFlag = false;
                            }
                        }

                    }
                }
            }
            if (nameFlag == false){
                System.out.println("缺少区域名字");
                System.out.println(varea.getNodeName() + "--" + varea.getAttributes().getNamedItem("id"));
            }

        }
        return flag;
    }

    /**
     * 复制svg文件
     */
    public void copySVGFile(File fromFile, String toFilePath) throws IOException{
        File toFile = new File(toFilePath);
        FileInputStream ins = new FileInputStream(fromFile);
        FileOutputStream out = new FileOutputStream(toFile);
        byte[] b = new byte[1024];
        int n=0;
        while((n=ins.read(b))!=-1){
            out.write(b, 0, n);
        }
        ins.close();
        out.close();
    }

    /**
     * 检查虚拟车位是否在虚拟区域标签下
     */
    public List checkVparkInVarea(Document document){
        Boolean flag = true;
        List vparkList = new ArrayList();
        List vareaParkList = new ArrayList();
        NodeList vgList = document.getElementsByTagName("g");

        for (int i = 0; i < vgList.getLength(); i++) {
            Node vparkNode = vgList.item(i);
            NamedNodeMap vparkAtt = vparkNode.getAttributes();

            if (vparkAtt.getNamedItem("id") != null){
                //获取所有虚拟车位id属性
                String nodeValue = vparkAtt.getNamedItem("id").getNodeValue();
                //获取虚拟车位
                if (nodeValue.contains("vpark")){
                    vparkList.add(nodeValue);
                }
                //获取虚拟区域包裹下的虚拟车位
                if (nodeValue.contains("varea_x5F") && !nodeValue.equals("varea_x5F_name")){
                    //获取子节点
                    NodeList vareaChild = vparkNode.getChildNodes();
                    for (int j = 0; j < vareaChild.getLength(); j++) {
                        Node vparkChildNode = vareaChild.item(j);
                        if (vparkChildNode.getNodeType() == Node.ELEMENT_NODE){
                            //获取g标签子节点
                            if ("g".equals(vparkChildNode.getNodeName())){
                                NamedNodeMap childAtt = vparkChildNode.getAttributes();
                                if (childAtt.getNamedItem("id") != null){
                                    //属性包含vpark
                                    if (childAtt.getNamedItem("id").toString().contains("vpark")){
                                        vareaParkList.add(childAtt.getNamedItem("id").getNodeValue());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //
        List errorVparkList = new ArrayList();
        //对比数组 - 获取未在区域下的虚拟车位
        for (int i = 0; i < vparkList.size(); i++) {
            Boolean flag2 = true;
            for (int j = 0; j < vareaParkList.size(); j++) {
                String str1 = vareaParkList.get(j).toString();
                String str2 = vparkList.get(i).toString();
                if (str1.equals(str2)){
                    flag2 = true;
                    break;
                }else {
                    flag2 = false;
                }
            }
            if (flag2 == false){
                flag = false;
                System.out.println(vparkList.get(i));
                errorVparkList.add(vparkList.get(i));
            }
        }
        return errorVparkList;
    }

    /**
     * 判断车位下是否包含多个<text>标签
     */
    public int checkTextNode(Element root) {
        int num = 0;
        NodeList childList = root.getChildNodes();
        if (childList != null){
            for (int i = 0; i < childList.getLength(); i++) {
                Node childNode = childList.item(i);
                if (childNode.getNodeType() == Node.ELEMENT_NODE){
//                    System.out.println("---------    " + childNode.getNodeName());
                    if (childNode.getNodeName().equals("text")){
                        num++;
                    }
                    num += checkTextNode((Element)childNode);

                }

            }
        }
        if (root.getNodeName().equals("g") && root.getAttribute("id").contains("park") && !root.getAttribute("id").contains("pid")
                && num != 1){
            System.out.println("<" + root.getNodeName() + ">" + root.getAttribute("id") + "---" + num + "个文本标签");
            numTextList.add(root.getAttribute("id"));
        }
        return num;
    }

    /**
     * 判断车位下是否包含多边形标签
     */
    public int checkParkPolygon(Element root){
        NodeList childList = root.getChildNodes();
        int num = 0;
        if (childList != null){
            for (int i = 0; i < childList.getLength(); i++) {
                Node childNode = childList.item(i);
                if (childNode.getNodeType() == Node.ELEMENT_NODE){
                    if (childNode.getNodeName().equals("polygon")){
                        num++;
                    }
                    num += checkParkPolygon((Element)childNode);
                }

            }
        }
        if (root.getNodeName().equals("g") && root.getAttribute("id").contains("park") && !root.getAttribute("id").contains("pid")
                && num == 1){
            System.out.println("<" + root.getNodeName() + ">" + root.getAttribute("id") + "--多边形");
            numPolygonList.add(root.getAttribute("id"));
        }

        return num;
    }

    /**
     * 传值svgUrl，parkName,floor,newList,向新的excel插入数据
     */
    public void createExcel(List parkNameList, List valueList, String excelName, String checkName) throws Exception{
        if (parkNameList.size() == 0){
            return;
        }
        workbook = new HSSFWorkbook();
        sheetName = workbook.createSheet("SVG");
        FileOutputStream fos = new FileOutputStream(outPath + excelName + ".xls");

        int sum = 1;
        int sign = 0;

        Row firstRow = sheetName.createRow(0);
        firstRow.createCell(0).setCellValue("停车场名称");
        firstRow.createCell(1).setCellValue(checkName);
        for (int i = 0; i < parkNameList.size(); i++) {
            String parkName = parkNameList.get(i).toString();
            for (int j = sign; j < valueList.size(); j++) {
                String value = valueList.get(j).toString();
                if (value.equals("stop")){
                    sign = j + 1;
                    break;
                }else {
                    Row row = sheetName.createRow(sum);
                    Cell c1 = row.createCell(0);
                    Cell c2 = row.createCell(1);
                    c1.setCellValue(parkName);
                    c2.setCellValue(value);
                    sum++;
                }
            }
        }
        sheetName.autoSizeColumn(0);
        sheetName.autoSizeColumn(1);
        workbook.write(fos);
        fos.close();
    }

}
