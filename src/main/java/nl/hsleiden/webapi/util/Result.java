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
package nl.hsleiden.webapi.util;

import java.util.List;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import nl.hsleiden.webapi.model.Employees;
import nl.hsleiden.webapi.model.Persons;
import nl.hsleiden.webapi.model.Students;
import nl.hsleiden.webapi.model.UpdatedGradeStudent;

/**
 *
 * @author hl
 */
@XmlRootElement(name="results")
@XmlSeeAlso({Students.class, Employees.class, UpdatedGradeStudent.class, Persons.class})
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
