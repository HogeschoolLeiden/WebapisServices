/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.hsleiden.webapi.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hl
 */
@Entity
@Table(name = "viewpersoon")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Person.findByLastname", query = "SELECT p FROM Person p WHERE lower(p.lastname) LIKE :lastname ORDER by p.lastname"), 
    @NamedQuery(name = "Person.getCount", query = "SELECT COUNT(p) FROM Person p WHERE lower(p.lastname) LIKE :lastname")})
public class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "id")
    private String id;
    @Size(max = 15)
    @Column(name = "studentnummer")
    private String studentNumber;
    @Size(max = 50)
    @Column(name = "medewerkernummer")
    private String employeeNumber;
    @Size(max = 15)
    @Column(name = "roepnaam")
    private String firstname;
    @Size(max = 50)
    @Column(name = "achternaam")
    private String lastname;
    @Size(max = 50)
    @Column(name = "naam_voluit")
    private String fullname;
    @Size(max = 50)
    @Column(name = "naam_informeel")
    private String informalname;
    @Size(max = 50)
    @Column(name = "naam_lijst")
    private String listName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sys_actief")
    private boolean sysActive;
    @Transient
    private String uri; 

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
    
    public Person() {
    }

    public Person(String id) {
        this.id = id;
    }

    public Person(String id, boolean sysActief) {
        this.id = id;
        this.sysActive = sysActief;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getInformalname() {
        return informalname;
    }

    public void setInformalname(String informalname) {
        this.informalname = informalname;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public boolean getSysActive() {
        return sysActive;
    }

    public void setSysActive(boolean sysActief) {
        this.sysActive = sysActief;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Person)) {
            return false;
        }
        Person other = (Person) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "nl.hsleiden.webapi.model.Persoon[ id=" + id + " ]";
    }
    
}
