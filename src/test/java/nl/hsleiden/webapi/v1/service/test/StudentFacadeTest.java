package nl.hsleiden.webapi.v1.service.test;


import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.ws.rs.core.MediaType;
import nl.hsleiden.webapi.model.Student;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

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
