//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.04.06 at 12:01:51 PM IRDT 
//


package ir.university.toosi.tms.model.entity.objectValue.init;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="gg" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="NodeName" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "gg"
})
@XmlRootElement(name = "ee")
public class Ee {

    @XmlElement(required = true)
    protected Gg gg;
    @XmlAttribute(name = "NodeName")
    @XmlSchemaType(name = "anySimpleType")
    protected String nodeName;

    /**
     * Gets the value of the gg property.
     *
     * @return possible object is
     * {@link Object }
     */
    public Gg getGg() {
        return gg;
    }

    /**
     * Sets the value of the gg property.
     *
     * @param value allowed object is
     *              {@link Object }
     */
    public void setGg(Gg value) {
        this.gg = value;
    }

    /**
     * Gets the value of the nodeName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * Sets the value of the nodeName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNodeName(String value) {
        this.nodeName = value;
    }

}