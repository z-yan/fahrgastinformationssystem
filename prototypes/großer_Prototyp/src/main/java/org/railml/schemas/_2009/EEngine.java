//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.06 at 11:34:29 AM CET 
//


package org.railml.schemas._2009;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				engine data about a motor car or locomotive, may be used
 * 				in conjunction with 'wagon'
 * 			
 * 
 * <p>Java class for eEngine complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="eEngine">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="propulsion" type="{http://www.railml.org/schemas/2009}ePropulsion" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="monitoring" type="{http://www.railml.org/schemas/2009}eMonitoring" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "eEngine", propOrder = {
    "propulsion",
    "monitoring"
})
public class EEngine {

    protected List<EPropulsion> propulsion;
    protected EMonitoring monitoring;

    /**
     * Gets the value of the propulsion property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the propulsion property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPropulsion().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EPropulsion }
     * 
     * 
     */
    public List<EPropulsion> getPropulsion() {
        if (propulsion == null) {
            propulsion = new ArrayList<EPropulsion>();
        }
        return this.propulsion;
    }

    /**
     * Gets the value of the monitoring property.
     * 
     * @return
     *     possible object is
     *     {@link EMonitoring }
     *     
     */
    public EMonitoring getMonitoring() {
        return monitoring;
    }

    /**
     * Sets the value of the monitoring property.
     * 
     * @param value
     *     allowed object is
     *     {@link EMonitoring }
     *     
     */
    public void setMonitoring(EMonitoring value) {
        this.monitoring = value;
    }

}
