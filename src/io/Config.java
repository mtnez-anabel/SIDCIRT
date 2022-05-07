package io;

import model.Const;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import model.Ind;
import model.Indicator;
import model.SubIndicator;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class Config {

    private static Indicator read(String fileName) {
        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(fileName);
        Indicator ind = null;
        SubIndicator[] subInd;
        try {
            Document document = (Document) builder.build(xmlFile);
            Element rootNode = document.getRootElement();
            String rootName = rootNode.getAttributeValue(Const.NAME);
//            System.out.println("Root Name : " + rootName);
            
            List list = rootNode.getChildren(Const.SUBINDICATOR);
            subInd = new SubIndicator[list.size()];
            
            for (int i = 0; i < list.size(); i++) {
                Element node = (Element) list.get(i);
                String name = node.getChildText(Const.NAME);
                String comment = node.getChildText(Const.COMMENT);
                String min = node.getChildText(Const.MIN);
                String max = node.getChildText(Const.MAX);
                String ini = node.getChildText(Const.INI);

//                System.out.println("Name : " + name);
//                System.out.println("Comment : " + comment);
//                System.out.println("Min : " + min);
//                System.out.println("Max : " + max);
//                System.out.println("Ini : " + ini);
                
                subInd[i] = new SubIndicator(name, comment,  Double.parseDouble(min),  Double.parseDouble(max),  Double.parseDouble(ini));
            }
            
//            System.out.println("************************************************************************************");
            ind = new Indicator(rootName, subInd);

        } catch (IOException | JDOMException io) {
            System.out.println(io.getMessage());
        }
        return ind;
    }
    
    public static Indicator load(Ind indicator){
        return read(Const.CONFIG_DIR + indicator + ".xml");
    }
}