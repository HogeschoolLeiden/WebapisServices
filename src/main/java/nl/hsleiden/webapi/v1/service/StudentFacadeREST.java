/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.hsleiden.webapi.v1.service;

import com.sun.jersey.api.NotFoundException;
import java.net.URI;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import nl.hsleiden.webapi.model.Student;
import nl.hsleiden.webapi.model.Students;
import nl.hsleiden.webapi.util.ValidationException;
import nl.hsleiden.webapi.util.Validator;
import org.apache.log4j.Logger;

/**
 *
 * @author hl
 */
@Path("v1/students")
public class StudentFacadeREST extends AbstractFacade<Student> {

    private EntityManagerFactory emf;
    private Logger logger = Logger.getLogger(StudentFacadeREST.class.getName());
    
    public StudentFacadeREST() {
        super(Student.class);
    }
    @Context
    UriInfo uriInfo;
    @Context
    HttpServletRequest request;
    @Context
    HttpServletResponse response;

    @GET
    @Path("/id/{id}")
    @Produces({"application/json", "application/xml"})
    public Student findById(@PathParam("id") String id) {

        EntityManager em = getEntityManager();
        
        if (id.equals("@me")) {
            String uid = (String) request.getAttribute("uid");
            String organisation = (String) request.getAttribute(uid);
            
            if (uid != null && uid.length() > 0 && organisation != null && organisation.length() > 0) {

                Query query = em.createNamedQuery("Student.findByStudentnumber").setParameter("studentnumber", uid);
                query.setParameter("organisation", organisation);
                try {
                    Student student = (Student) query.getSingleResult();
                    return student;
                } catch (NoResultException ne) {
                    logger.info("Er is geen resultaat voor param: @me en studentnummer: " + uid);
                    throw new NotFoundException("No person found for searchparam @me");
                }
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.UNAUTHORIZED);
                builder.entity("Not allowed.");
                builder.type(MediaType.TEXT_PLAIN);
                Response res = builder.build();
                throw new WebApplicationException(res);
            }
        }

        Query query = em.createNamedQuery("Student.findById").setParameter("id", id);
        try {
            Student student = (Student) query.getSingleResult();
            return student;
        } catch (NoResultException nre) {
            logger.info("Er is geen resultaat voor param: " + id);
            throw new NotFoundException("No person found for searchparam " + id);
        }
    }

    @GET
    @Path("{lastname}")
    @Produces({"application/json", "application/xml"})
    public List<Students> findByLastname(@PathParam("lastname") String lastname) {

        EntityManager em = getEntityManager();
        try {
            Validator.checkLengthLastname(lastname);
        } catch (ValidationException ve) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            builder.entity(ve.getMessage());
            builder.type(MediaType.TEXT_PLAIN);
            Response res = builder.build();
            throw new WebApplicationException(res);
        }

        try {
            Validator.validateStringParameter(lastname);
        } catch (ValidationException ve) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            builder.entity(ve.getMessage());
            builder.type(MediaType.TEXT_PLAIN);
            Response res = builder.build();
            throw new WebApplicationException(res);
        }

        String name = formatLastname(lastname);
        Query query = em.createNamedQuery("Students.findByLastname").setParameter("lastname", name);

        List<Students> students = query.getResultList();
        if (students.isEmpty()) {
            logger.info("Er is geen resultaat voor param: " + lastname);
            throw new NotFoundException("No person found for searchparam " + lastname);
        }
        students = buildLink(students);
        response.setHeader("NumberOfResults", String.valueOf(students.size()));
        return students;
    }

    @Override
    protected EntityManager getEntityManager() {
        emf = Persistence.createEntityManagerFactory("postgresif");
        return emf.createEntityManager();
    }

    /**
     * Formats lastname for a case insensitive search
     *
     * @param lastname
     * @return
     */
    private String formatLastname(String lastname) {
        StringBuilder sb = new StringBuilder();
        sb.append(lastname.toLowerCase());
        sb.append("%");
        return sb.toString();
    }

    /**
     * Creates an uri for linking to more information. Person.id is used.
     *
     * @param List persons
     * @return List persons
     */
    private List<Students> buildLink(List<Students> students) {
        String hostname = request.getServerName();

        for (Students s : students) {
            UriBuilder ub = uriInfo.getBaseUriBuilder();
            URI userUri = ub.host(hostname).port(443).path(StudentFacadeREST.class).path("/id/").path(s.getId()).build();
            s.setUri(userUri.toString());
        }
        return students;
    }
}
