/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.hsleiden.webapi.util;

import java.sql.Timestamp;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author hl
 */
public class TimestampAdapter extends XmlAdapter<String, Timestamp> {
    
      @Override
      public String marshal(Timestamp v) {
          return v.toString();
      }
      @Override
      public Timestamp unmarshal(String v) {
          return Timestamp.valueOf(v);
      }
  }
