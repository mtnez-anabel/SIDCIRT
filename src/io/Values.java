package io;

import model.Const;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.App;
import model.Ind;
import model.Indicator;
import model.SubIndicator;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author Anabel
 */
public class Values {

    public static void saveModel(String fileName) {
        Element model = new Element(Const.MODEL);
        Document doc = new Document(model);
        doc.setRootElement(model);

        for (Ind i : Ind.values()) {
            Element value = new Element(Const.INDICATOR);
            value.setAttribute(new Attribute(Const.NAME, i.toString()));
            SubIndicator[] sub = App.indicators.get(i).subInd;
            for (int j = 0; j < sub.length; j++) {
                Element subInd = new Element(Const.SUBINDICATOR);
                subInd.setAttribute(new Attribute(Const.VALUE, String.valueOf(sub[j].ini)));
                value.addContent(subInd);
            }
            doc.getRootElement().addContent(value);
        }
        // new XMLOutputter().output(doc, System.out);
        XMLOutputter xmlOutput = new XMLOutputter();
        // display nice nice
        xmlOutput.setFormat(Format.getPrettyFormat());
        try {
            xmlOutput.output(doc, new FileWriter(fileName));
        } catch (IOException ex) {
            Logger.getLogger(Values.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void loadModel(String fileName) {
        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(fileName);

        try {
            Document document = (Document) builder.build(xmlFile);
            Element model = document.getRootElement();

            List list = model.getChildren(Const.INDICATOR);

            for (int i = 0; i < list.size(); i++) {
                Element ind = (Element) list.get(i);                
                String name = ind.getAttributeValue(Const.NAME);
                System.out.println("Name : " + name);
              
                List subList = ind.getChildren(Const.SUBINDICATOR);
                
                for (int j = 0; j < subList.size(); j++) {
                    Element subInd = (Element) subList.get(j);
                    String value = subInd.getAttributeValue(Const.VALUE);
                     System.out.println("Value : " + value);
                     
                     App.indicators.get(Ind.valueOf(name)).subInd[j].ini = Double.parseDouble(value);
                }
            }


        } catch (IOException | JDOMException io) {
            System.out.println(io.getMessage());
        }
    }
}
