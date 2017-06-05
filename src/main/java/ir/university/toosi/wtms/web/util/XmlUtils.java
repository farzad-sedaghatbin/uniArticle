package ir.university.toosi.wtms.web.util;


import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Iterator;
import java.util.List;


/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */
public class XmlUtils {

    public static void modify(String fileName, String parentNode, String childNode, String attribute, String oldValue, String newValue) {
        try {
            SAXReader saxReader = new SAXReader();
            InputStream in = new FileInputStream(new File(fileName));
            Document document = saxReader.read(in);
            List<Node> list = (List<Node>) document.selectNodes(parentNode);

            if (attribute == "null" && childNode != "null") {
                for (Node node : list) {
                    Element element = (Element) node;
                    List<Element> iterator = (List<Element>) element.elements(childNode);
                    for (Element elements : iterator)
                        if (elements.getText().equals(oldValue))
                            elements.setText(newValue);
                }
            }

            if (attribute == "null" && childNode == "null") {
                for (Node node : list) {
                    Attribute attr = (Attribute) node;
                    if (attr.getValue().equals(oldValue))
                        attr.setValue(newValue);
                }
            }

            modifyAccomplish(fileName, document);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private static void modifyAccomplish(String fileName, Document document) {
        try {
            XMLWriter output = new XMLWriter(new FileWriter(fileName));
            output.write(document);
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeXml(String fileName, String content) {
        try {
            SAXReader saxReader = new SAXReader();
            InputStream in = new ByteArrayInputStream(content.getBytes("UTF-8"));
            Document document = saxReader.read(in);
            XMLWriter output = new XMLWriter(new FileWriter(fileName));
            OutputFormat format = OutputFormat.createCompactFormat();
            format.setExpandEmptyElements(true);
            format.setIndent("\t");
            output.write(document);
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


    public static Object XmlToDiskMessages(String xmlFile) {
        Object object = null;
        try {
            JAXBContext jc = JAXBContext.newInstance("org.tamin.dashboard.manager.fact.disk.webservices");
            Unmarshaller unmarshal = jc.createUnmarshaller();
            object = (Object) unmarshal.unmarshal(new StreamSource(new StringReader(xmlFile)));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static Object XmlToDebitMessages(String xmlFile) {
        Object object = null;
        try {
            JAXBContext jc = JAXBContext.newInstance("org.tamin.dashboard.manager.fact.debit.webservices");
            Unmarshaller unmarshal = jc.createUnmarshaller();
            object = (Object) unmarshal.unmarshal(new StreamSource(new StringReader(xmlFile)));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static Object XmlToOrderMessages(String xmlFile) {
        Object object = null;
        try {
            JAXBContext jc = JAXBContext.newInstance("org.tamin.dashboard.manager.fact.order.webservices");
            Unmarshaller unmarshal = jc.createUnmarshaller();
            object = (Object) unmarshal.unmarshal(new StreamSource(new StringReader(xmlFile)));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static Object XmlToHistoryMessages(String xmlFile) {
        Object object = null;
        try {
            JAXBContext jc = JAXBContext.newInstance("org.tamin.dashboard.manager.fact.webservices.allhistory");
            Unmarshaller unmarshal = jc.createUnmarshaller();
            object = (Object) unmarshal.unmarshal(new StreamSource(new StringReader(xmlFile)));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static String getNodeAttributeValue(Element root, String node, String nodeAttribute) {
        for (Iterator i = root.elementIterator(node); i.hasNext(); ) {
            Element nodeElement = (Element) i.next();
            for (Iterator j = nodeElement.attributeIterator(); j.hasNext(); ) {
                Attribute attribute = (Attribute) j.next();
                if (attribute.getName().equalsIgnoreCase(nodeAttribute)) {
                    return attribute.getText().trim().replaceAll(" ", "");
                }
            }
        }

        return "";
    }
}