/*
 * Copyright 2014 Hogeschool Leiden.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
    @NamedQuery(name = "Persons.findAll", query = "SELECT p FROM Persons p ORDER by p.lastname "),
    @NamedQuery(name = "Persons.findByLastname", query = "SELECT p FROM Persons p WHERE lower(p.lastname) LIKE :lastname ORDER by p.lastname"), 
    @NamedQuery(name = "Persons.findByLastnameAndEducation", query = "SELECT p FROM Persons p WHERE lower(p.lastname) LIKE :lastname AND p.education = :education ORDER by p.lastname"), 
    @NamedQuery(name = "Persons.findByLastnameAndDepartment", query = "SELECT p FROM Persons p WHERE lower(p.lastname) LIKE :lastname AND p.department = :department ORDER by p.lastname"), 
    @NamedQuery(name = "Persons.findByEducation", query = "SELECT p FROM Persons p WHERE p.education = :education ORDER by p.lastname"), 
    @NamedQuery(name = "Persons.findByDepartment", query = "SELECT p FROM Persons p WHERE p.department = :department ORDER by p.lastname"),
    @NamedQuery(name = "Persons.getCountForLastname", query = "SELECT COUNT(p) FROM Persons p WHERE lower(p.lastname) LIKE :lastname"),
    @NamedQuery(name = "Persons.getCountForLastnameAndEducation", query = "SELECT COUNT(p) FROM Persons p WHERE lower(p.lastname) LIKE :lastname AND p.education = :education"),
    @NamedQuery(name = "Persons.getCountForLastnameAndDepartment", query = "SELECT COUNT(p) FROM Persons p WHERE lower(p.lastname) LIKE :lastname AND p.department = :department"),
    @NamedQuery(name = "Persons.getCountForEducation", query = "SELECT COUNT(p) FROM Persons p WHERE p.education = :education"),
    @NamedQuery(name = "Persons.getCountForDepartment", query = "SELECT COUNT(p) FROM Persons p WHERE p.department = :department"),
    @NamedQuery(name = "Persons.getCountAll", query = "SELECT COUNT(p) FROM Persons p")})
public class Persons implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "idpersoon")
    private String id;
    @Size(max = 15)
    @Column(name = "studentnummer")
    private String studentNumber;
    @Size(max = 50)
    @Column(name = "medewerkernummer")
    private String employeeNumber;
    @Size(max = 50)
    @Column(name = "achternaam")
    private String lastname;
    @Size(max = 50)
    @Column(name = "naam_informeel")
    private String informalname;
    @Size(max = 50)
    @Column(name = "naam_lijst")
    private String listName;
    @Size(max = 50)
    @Column(name = "afdeling")
    private String department;
    @Size(max = 50)
    @Column(name = "opleiding")
    private String education;
    @Transient
    private String uri; 

    public Persons() {
    }

    public Persons(String id) {
        this.id = id;
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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
    
    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }
    
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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
        if (!(object instanceof Persons)) {
            return false;
        }
        Persons other = (Persons) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "nl.hsleiden.webapi.model.Persons[ id=" + id + " ]";
    }
    
}
