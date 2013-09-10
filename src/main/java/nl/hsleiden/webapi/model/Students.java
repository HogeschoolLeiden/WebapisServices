/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.hsleiden.webapi.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hl
 */
@Entity
@Table(name = "viewstudent")
@XmlRootElement(name="students")
@NamedQueries({
    @NamedQuery(name = "Students.findAll()", query = "SELECT s FROM Students s ORDER by s.lastname"),
    @NamedQuery(name = "Students.findAllForEducation()", query = "SELECT s FROM Students s WHERE s.education = :education ORDER by s.lastname"),
    @NamedQuery(name = "Students.findByLastname", query = "SELECT s FROM Students s WHERE lower(s.lastname) LIKE :lastname ORDER by s.lastname"),
    @NamedQuery(name = "Students.findByLastnameAndEducation", query = "SELECT s FROM Students s WHERE lower(s.lastname) LIKE :lastname AND s.education = :education ORDER by s.lastname"),
    @NamedQuery(name = "Students.getCount", query = "SELECT COUNT(s) FROM Students s WHERE lower(s.lastname) LIKE :lastname"),
    @NamedQuery(name = "Students.getCountAll", query = "SELECT COUNT(s) FROM Students s"),
    @NamedQuery(name = "Students.getCountAllForEducation", query = "SELECT COUNT(s) FROM Students s WHERE s.education LIKE :education")})
public class Students implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idpersoon")
    private String id;
    @Size(max = 15)
    @Column(name = "naamlijst")
    private String listname;
    @Size(max = 15)
    @Column(name = "achternaam")
    private String lastname;
    @Column(name = "opleiding")
    private String education;
    @Transient
    private String uri; 

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getListname() {
        return listname;
    }

    public void setListname(String listname) {
        this.listname = listname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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
        if (!(object instanceof Students)) {
            return false;
        }
        Students other = (Students) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "nl.hsleiden.webapi.model.Students[ id=" + id + " ]";
    }
    
}
