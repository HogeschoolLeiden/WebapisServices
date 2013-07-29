/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.hsleiden.webapi.util;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import nl.hsleiden.webapi.model.Students;

/**
 *
 * @author hl
 */
@XmlRootElement
public class Result {
    
    private List<Students> students;
    private String previous;
    private String next;

    public List<Students> getStudents() {
        return students;
    }

    public void setStudents(List<Students> students) {
        this.students = students;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }
    
}
