/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.hsleiden.webapi.util;

import java.util.List;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import nl.hsleiden.webapi.model.Employees;
import nl.hsleiden.webapi.model.Students;
import nl.hsleiden.webapi.model.UpdatedGradeStudent;

/**
 *
 * @author hl
 */
@XmlRootElement(name="results")
@XmlSeeAlso({Students.class, Employees.class, UpdatedGradeStudent.class})
public class Result<T> {
    
    private List<T> results;
    private String previous;
    private String next;
    private String total;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }


    @XmlMixed
    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        
        this.results = results;
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
