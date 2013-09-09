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
@Table(name = "viewmedewerker")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Employee.findById", query = "SELECT e FROM Employee e WHERE e.id = :id "),
    @NamedQuery(name = "Employee.findByUID", query = "SELECT e FROM Employee e WHERE e.accountname = :accountname AND e.organisation = :organisation")})
public class Employee implements Serializable {
   
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "idpersoon")
    private String id;
    @Size(max = 15)
    @Column(name = "achternaam")
    private String lastname;
    @Size(max = 15)
    @Column(name = "roepnaam")
    private String givenname;
    @Size(max = 15)
    @Column(name = "naamlijst")
    private String listname;
    @Size(max = 15)
    @Column(name = "geslacht")
    private String gender;
    @Size(max = 15)
    @Column(name = "tussenvoegsel")
    private String inserts;
    @Size(max = 15)
    @Column(name = "emailhogeschool")
    private String emailaddress;
    @Size(max = 15)
    @Column(name = "telefoonvast")
    private String phonenumberHl;
    @Size(max = 15)
    @Column(name = "mobielhogeschool")
    private String mobileHl;
    @Size(max = 15)
    @Column(name = "locatie")
    private String location;
    @Size(max = 15)
    @Column(name = "functienaam")
    private String functionDescription;
    @Size(max = 15)
    @Column(name = "medewerkernummer")
    private String employeeNumber;
    @Column(name = "afdeling")
    private String department;
    @Column(name = "homeorganisation")
    private String organisation;
    @Column(name = "accountnaam")
    private String accountname;


    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }
    
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
    
    @Transient
    private String uri;

    
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
    
    

    public Employee() {
    }

    public Employee(String employeenumber) {
        this.employeeNumber = employeenumber;
    }

   public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGivenname() {
        return givenname;
    }

    public void setGivenname(String givenname) {
        this.givenname = givenname;
    }

    public String getListname() {
        return listname;
    }

    public void setListname(String listname) {
        this.listname = listname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getInserts() {
        return inserts;
    }

    public void setInserts(String inserts) {
        this.inserts = inserts;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getPhonenumberHl() {
        return phonenumberHl;
    }

    public void setPhonenumberHl(String phonenumberhl) {
        this.phonenumberHl = phonenumberhl;
    }

    public String getMobileHl() {
        return mobileHl;
    }

    public void setMobileHl(String mobilehl) {
        this.mobileHl = mobilehl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFunctionDescription() {
        return functionDescription;
    }

    public void setFunctionDescription(String functiondescription) {
        this.functionDescription = functiondescription;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeenumber) {
        this.employeeNumber = employeenumber;
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
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "nl.hsleiden.webapi.model.Medewerker[ id=" + id + " ]";
    }

   
}
