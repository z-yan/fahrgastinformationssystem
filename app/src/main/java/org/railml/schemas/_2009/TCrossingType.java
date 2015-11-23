//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.06 at 11:34:29 AM CET 
//


package org.railml.schemas._2009;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tCrossingType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="tCrossingType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="simpleCrossing"/>
 *     &lt;enumeration value="simpleSwitchCrossing"/>
 *     &lt;enumeration value="doubleSwitchCrossing"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tCrossingType")
@XmlEnum
public enum TCrossingType {

    @XmlEnumValue("simpleCrossing")
    SIMPLE_CROSSING("simpleCrossing"),
    @XmlEnumValue("simpleSwitchCrossing")
    SIMPLE_SWITCH_CROSSING("simpleSwitchCrossing"),
    @XmlEnumValue("doubleSwitchCrossing")
    DOUBLE_SWITCH_CROSSING("doubleSwitchCrossing");
    private final String value;

    TCrossingType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TCrossingType fromValue(String v) {
        for (TCrossingType c: TCrossingType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}