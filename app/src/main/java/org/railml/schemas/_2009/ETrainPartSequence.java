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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for eTrainPartSequence complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="eTrainPartSequence">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.railml.org/schemas/2009}tTrainPartSequence">
 *       &lt;sequence>
 *         &lt;element name="trainPartRef" type="{http://www.railml.org/schemas/2009}tTrainPartRef" maxOccurs="unbounded"/>
 *         &lt;element name="equipmentUsage" type="{http://www.railml.org/schemas/2009}eEquipmentUsage" minOccurs="0"/>
 *         &lt;element name="brakeUsage" type="{http://www.railml.org/schemas/2009}eBrakeUsage" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "eTrainPartSequence", propOrder = {
    "trainPartRef",
    "equipmentUsage",
    "brakeUsage"
})
public class ETrainPartSequence
    extends TTrainPartSequence
{

    @XmlElement(required = true)
    protected List<TTrainPartRef> trainPartRef;
    protected EEquipmentUsage equipmentUsage;
    protected EBrakeUsage brakeUsage;

    /**
     * Gets the value of the trainPartRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the trainPartRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTrainPartRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TTrainPartRef }
     * 
     * 
     */
    public List<TTrainPartRef> getTrainPartRef() {
        if (trainPartRef == null) {
            trainPartRef = new ArrayList<TTrainPartRef>();
        }
        return this.trainPartRef;
    }

    /**
     * Gets the value of the equipmentUsage property.
     * 
     * @return
     *     possible object is
     *     {@link EEquipmentUsage }
     *     
     */
    public EEquipmentUsage getEquipmentUsage() {
        return equipmentUsage;
    }

    /**
     * Sets the value of the equipmentUsage property.
     * 
     * @param value
     *     allowed object is
     *     {@link EEquipmentUsage }
     *     
     */
    public void setEquipmentUsage(EEquipmentUsage value) {
        this.equipmentUsage = value;
    }

    /**
     * Gets the value of the brakeUsage property.
     * 
     * @return
     *     possible object is
     *     {@link EBrakeUsage }
     *     
     */
    public EBrakeUsage getBrakeUsage() {
        return brakeUsage;
    }

    /**
     * Sets the value of the brakeUsage property.
     * 
     * @param value
     *     allowed object is
     *     {@link EBrakeUsage }
     *     
     */
    public void setBrakeUsage(EBrakeUsage value) {
        this.brakeUsage = value;
    }

}
