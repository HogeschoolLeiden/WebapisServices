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
@Table(name = "viewmedewerker")
@XmlRootElement(name="employees")
@NamedQueries({
    @NamedQuery(name = "Employees.findAll", query = "SELECT e FROM Employees e ORDER by e.lastname"),
    @NamedQuery(name = "Employees.findForDepartment", query = "SELECT e FROM Employees e WHERE e.department = :department ORDER by e.lastname"),
    @NamedQuery(name = "Employees.findByLastname", query = "SELECT e FROM Employees e WHERE lower(e.lastname) LIKE :lastname ORDER by e.lastname"),
    @NamedQuery(name = "Employees.findByLastnameAndDepartment", query = "SELECT e FROM Employees e WHERE lower(e.lastname) LIKE :lastname AND e.department = :department ORDER by e.lastname"),
    @NamedQuery(name = "Employees.getCountForLastname", query = "SELECT COUNT(e) FROM Employees e WHERE lower(e.lastname) LIKE :lastname"),
    @NamedQuery(name = "Employees.getCountAll", query = "SELECT COUNT(e) FROM Employees e"),
    @NamedQuery(name = "Employees.getCountForDepartment", query = "SELECT COUNT(e) FROM Employees e WHERE e.department = :department"),
    @NamedQuery(name = "Employees.getCountForLastnameAndDepartment", query = "SELECT COUNT(e) FROM Employees e WHERE e.department = :department AND lower(e.lastname) LIKE :lastname ")})
public class Employees implements Serializable {
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
    @Column(name = " afdeling")
    private String department;
    @Transient
    private String uri; 

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
     
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    

    public String getListname() {
        return listname;
    }

    public void setListname(String listname) {
        this.listname = listname;
    }

    public String getId() {
        return id;
    }
    
    

    public void setId(String id) {
        this.id = id;
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
        if (!(object instanceof Employees)) {
            return false;
        }
        Employees other = (Employees) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "nl.hsleiden.webapi.model.Name[ id=" + id + " ]";
    }
    
}
