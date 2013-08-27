/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.hsleiden.webapi.model;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import nl.hsleiden.webapi.util.TimestampAdapter;

/**
 *
 * @author hl
 */
@Entity
@Table(name = "viewstudent")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UpdatedGradeStudent.findByUpdate", query = "SELECT s FROM UpdatedGradeStudent s WHERE s.cijfer_gesignaleerd >= :cijfer_gesignaleerd")})
public class UpdatedGradeStudent implements Serializable {
    private static final long serialVersionUID = 1L;
 
    @Id
    @Column(name = "studentnummer")
    private String studentnumber;
    @Size(max = 2147483647)
    
    @Column(name = "homeorganisation")
    private String organisation;
    
    /**
     * 
     */
    @Column(name = "cijfer_gesignaleerd")
    private Timestamp cijfer_gesignaleerd;

    @XmlJavaTypeAdapter( TimestampAdapter.class)
    public Timestamp getCijfer_gesignaleerd() {
        return cijfer_gesignaleerd;
    }

    public void setCijfer_gesignaleerd(Timestamp cijfer_gesignaleerd) {
        this.cijfer_gesignaleerd = cijfer_gesignaleerd;
    }

    public String getStudentnumber() {
        return studentnumber;
    }

    public void setStudentnumber(String studentnumber) {
        this.studentnumber = studentnumber;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }
   
    @Override
    public String toString() {
        return "nl.hsleiden.webapi.model.UpdatedGradeStudent[ studentnumber=" + studentnumber + " ]";
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UpdatedGradeStudent)) {
            return false;
        }
        UpdatedGradeStudent other = (UpdatedGradeStudent) object;
        if ((this.studentnumber == null && other.studentnumber != null) || (this.studentnumber != null && !this.studentnumber.equals(other.studentnumber))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (studentnumber != null ? studentnumber.hashCode() : 0);
        return hash;
    }

}
