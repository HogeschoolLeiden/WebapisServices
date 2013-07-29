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
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import nl.hsleiden.webapi.model.Student;
import nl.hsleiden.webapi.model.Students;
import nl.hsleiden.webapi.util.Result;
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
    public Result findByLastname(@PathParam("lastname") String lastname, @QueryParam("max") String max, @QueryParam("offset") String offset) {

        EntityManager em = getEntityManager();
        try {
            Validator.checkLengthLastname(lastname);
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
        Result result = new Result();
        int maxResults;
        int intOffset;
        if (max != null && offset != null) {
            logger.debug("Param max: " + max);
            logger.debug("Param offset: " + offset);
                        
            maxResults = Integer.parseInt(max);
            intOffset = Integer.parseInt(offset);
                    
            Query count = em.createNamedQuery("Students.getCount").setParameter("lastname", name);
            int total = ((Long)count.getSingleResult()).intValue();
            logger.debug("MaxResults: " + total);
            
            query.setMaxResults(maxResults);
            query.setFirstResult(intOffset);
            int nextOffset = intOffset + maxResults;
            int previousOffset = intOffset - maxResults;
            logger.debug("Previous offset: " + previousOffset);
            if (nextOffset <= total) {
                String next = createpagingLink(lastname, max, String.valueOf(nextOffset));
                result.setNext(next);
            }
            if (previousOffset > -1) {
                String previous = createpagingLink(lastname, max, String.valueOf(previousOffset));
                result.setPrevious(previous);
            }
         } else {
            query.setFirstResult(0);
        }
        
        
        List<Students> students = query.getResultList();
        if (students.isEmpty()) {
            logger.info("Er is geen resultaat voor param: " + lastname);
            throw new NotFoundException("No person found for searchparam " + lastname);
        }
        students = buildLink(students);
        result.setStudents(students);
        return result;
    }

    @Override
    protected EntityManager getEntityManager() {
        emf = Persistence.createEntityManagerFactory("apis.hsleiden.nl");
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
    
    private String createpagingLink(String param, String max, String offset) {
        String hostname = request.getServerName();
        UriBuilder ub = uriInfo.getBaseUriBuilder();
        URI userUri = ub.host(hostname).port(8080).path(StudentFacadeREST.class).path("/" + param).queryParam("max", max).queryParam("offset", offset).build();
        
        return userUri.toString();
    }
}
