//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.06 at 11:34:29 AM CET 
//


package org.railml.schemas._2009;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tOperationModeChange complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tOperationModeChange">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.railml.org/schemas/2009}tOrientedElement">
 *       &lt;attGroup ref="{http://www.railml.org/schemas/2009}aOperationMode"/>
 *       &lt;anyAttribute namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tOperationModeChange")
public class TOperationModeChange
    extends TOrientedElement
{

    @XmlAttribute(name = "modeLegislative", required = true)
    protected String modeLegislative;
    @XmlAttribute(name = "modeExecutive", required = true)
    protected String modeExecutive;
    @XmlAttribute(name = "clearanceManaging")
    protected TClearanceManaging clearanceManaging;

    /**
     * Gets the value of the modeLegislative property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModeLegislative() {
        return modeLegislative;
    }

    /**
     * Sets the value of the modeLegislative property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModeLegislative(String value) {
        this.modeLegislative = value;
    }

    /**
     * Gets the value of the modeExecutive property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModeExecutive() {
        return modeExecutive;
    }

    /**
     * Sets the value of the modeExecutive property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModeExecutive(String value) {
        this.modeExecutive = value;
    }

    /**
     * Gets the value of the clearanceManaging property.
     * 
     * @return
     *     possible object is
     *     {@link TClearanceManaging }
     *     
     */
    public TClearanceManaging getClearanceManaging() {
        return clearanceManaging;
    }

    /**
     * Sets the value of the clearanceManaging property.
     * 
     * @param value
     *     allowed object is
     *     {@link TClearanceManaging }
     *     
     */
    public void setClearanceManaging(TClearanceManaging value) {
        this.clearanceManaging = value;
    }

}