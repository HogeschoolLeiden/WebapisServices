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
package nl.hsleiden.webapi.v1.service.test;


import com.sun.jersey.test.framework.JerseyTest;
import org.apache.log4j.Logger;

public class StudentFacadeTest extends JerseyTest{
     
    private Logger logger = Logger.getLogger(StudentFacadeTest.class.getName());
    
    public StudentFacadeTest() throws Exception {
        super("nl.hsleiden.webapi.v1");
    }
    
//    @Test
//    public void testFindStudentsByIdWithXML() throws JSONException{
//        
//        WebResource webResource = client().resource("http://localhost:8080/WebapisServices/webresources/v1/student/id");
//        GenericType<Collection<Student>> genericType = new GenericType<Collection<Student>>(){};
//        WebResource r = webResource.queryParam("id", "1");
//        List<Student> students = (List<Student>) r.accept(MediaType.APPLICATION_XML).get(genericType);
//        
//        Iterator it = students.iterator();
//        while(it.hasNext()) {
//            Student student = (Student)it.next();
//            assertEquals("Geronimo", student.getGivenname());
//        }    
//    }
//    
//    @Test
//    public void testFindStudentsByIdWithJSON() throws JSONException{
//       
//        WebResource webResource = client().resource("http://localhost:8080/WebapisServices/webresources/v1/student/id");
//        GenericType<Collection<Student>> genericType = new GenericType<Collection<Student>>(){};
//        WebResource r = webResource.queryParam("id", "1");
//        List<Student> students = (List<Student>) r.accept(MediaType.APPLICATION_JSON).get(genericType);
//        
//        Iterator it = students.iterator();
//        while(it.hasNext()) {
//            Student student = (Student)it.next();
//            assertEquals("Geronimo", student.getGivenname());
//        }    
//    }
}
