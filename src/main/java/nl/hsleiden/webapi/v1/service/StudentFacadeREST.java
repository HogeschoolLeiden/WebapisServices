/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.hsleiden.webapi.v1.service;

import java.net.URI;
import java.sql.Timestamp;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import nl.hsleiden.webapi.exception.BadRequestError;
import nl.hsleiden.webapi.exception.NotFoundError;
import nl.hsleiden.webapi.exception.UnauthorizedError;
import nl.hsleiden.webapi.model.Student;
import nl.hsleiden.webapi.model.Students;
import nl.hsleiden.webapi.model.UpdatedGradeStudent;
import nl.hsleiden.webapi.util.Result;
import nl.hsleiden.webapi.util.ValidationException;
import nl.hsleiden.webapi.util.Validator;
import org.apache.log4j.Logger;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

/**
 *
 * @author hl
 */
@Path("students")
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
                    throw new NotFoundError("No person found for searchparam @me");
                }
            } else {
                throw new UnauthorizedError("Not allowed");
            }
        }

        Query query = em.createNamedQuery("Student.findById").setParameter("id", id);
        try {
            Student student = (Student) query.getSingleResult();
            return student;
        } catch (NoResultException nre) {
            logger.info("Er is geen resultaat voor param: " + id);
            throw new NotFoundError("No person found for searchparam " + id);
        }
    }

//    @GET
//    @Produces({"application/json", "application/xml"})
//    public Result findAll(@QueryParam("max") String max, @QueryParam("offset") String offset, @QueryParam ("education") String education) {
//        EntityManager em = getEntityManager();
//        Result result = new Result();
//        Query query = null;
//        
//        if (education != null && education.trim().length() > 0) {
//            query = em.createNamedQuery("Students.findAllForEducation()").setParameter("education", education);
//        } else {
//            query = em.createNamedQuery("Students.findAll()");
//        }
//        
//        int maxResults;
//        int intOffset;
//        if (max != null && max.trim().length() > 0 && offset != null && offset.trim().length() > 0) {
//            try {
//                maxResults = Integer.parseInt(max);
//                intOffset = Integer.parseInt(offset);
//            } catch (NumberFormatException n) {
//                logger.info("Parameters max and/or offset are not a number. Max =  " + max + ", offset = " + offset);
//                throw new BadRequestError("Parameters max and/or offset are not a number. Max =  " + max + ", offset = " + offset);
//            }
//            if (maxResults < 0 || intOffset < 0) {
//                logger.info("A negative number is provided for offset or max: " + "max = " + max + ", offset = " + offset);
//                throw new BadRequestError("A negative number is provided for offset or max: " + "max = " + max + ", offset = " + offset);
//            }
//            
//            Query count = null;
//            if (education != null && education.trim().length() > 0) {
//                logger.debug("******* education: " + education);
//                count = em.createNamedQuery("Students.getCountAllForEducation").setParameter("education", education);
//            } else {
//                education = null;
//                count = em.createNamedQuery("Students.getCountAll");
//            }
//            int total = ((Long) count.getSingleResult()).intValue();
//            logger.debug("Totaal: " + total);
//            if (total > 0) {
//                result.setTotal(String.valueOf(total));
//                logger.debug("MaxResults: " + total);
//                query.setMaxResults(maxResults);
//                query.setFirstResult(intOffset);
//                int nextOffset = intOffset + maxResults;
//                int previousOffset = intOffset - maxResults;
//                if (nextOffset <= total) {
//                    String next = createpagingLink(null, education, max, String.valueOf(nextOffset));
//                    result.setNext(next);
//                }
//                if (previousOffset > -1) {
//                    String previous = createpagingLink(null, education, max, String.valueOf(previousOffset));
//                    result.setPrevious(previous);
//                }
//            } else {
//                logger.info("No result found error occured ");
//                throw new NotFoundError("No result found");
//            }
//        } else {
//            logger.debug("****geen pagination");
//            query.setFirstResult(0);
//        }
//        
//        List<Students> names = query.getResultList();
//        if (names.size() > 0) {
//            result.setResults(names);
//            buildLink(names);
//            if (result.getTotal() == null) {
//                result.setTotal(String.valueOf(names.size()));
//            }
//        } 
//        return result;
//    }
    
    @GET
    //@Path("{lastname}")
    @Produces({"application/json", "application/xml"})
    public Result findByLastname(@Context HttpServletRequest request, @QueryParam("lastname") String lastname, 
                     @QueryParam("max") String max, @QueryParam("offset") String offset, @QueryParam ("education") String education) throws OAuthSystemException {
        logger.debug("In methode");
        //String name = null;
        
        int test = 1;
        
        if (lastname != null && lastname.trim().length() > 0 && education != null && education.trim().length() > 0) {
            test = 2;
        } else if (education != null && education.trim().length() > 0) {
            test = 3;
        } else if (lastname != null && lastname.trim().length() > 0) {
            test = 4;
        }
        
        Result result = new Result();
        EntityManager em = getEntityManager();
        
        //determine which query to create, based on provided parameters
        Query query = null;
        switch (test) {
            case 1:
                query = em.createNamedQuery("Students.findAll");
            case 2: 
                checkLastname(lastname);
                lastname = formatLastname(lastname);
                query = em.createNamedQuery("Students.findByLastnameAndEducation").setParameter("lastname", lastname);
                query.setParameter("education", education);
            case 3:
                query = em.createNamedQuery("Students.findAllForEducation").setParameter("education", education);
            case 4: 
                checkLastname(lastname);
                lastname = formatLastname(lastname);
                query = em.createNamedQuery("Students.findByLastname").setParameter("lastname", lastname);
        }
 
        //determine is pagination is asked for
        int maxResults;
        int intOffset;
        if (max != null && max.trim().length() > 0 && offset != null && offset.trim().length() > 0) {
            try {
                maxResults = Integer.parseInt(max);
                intOffset = Integer.parseInt(offset);
            } catch (NumberFormatException n) {
                logger.info("Parameters max and/or offset are not a number. Max =  " + max + ", offset = " + offset);
                throw new BadRequestError("Parameters max and/or offset are not a number. Max =  " + max + ", offset = " + offset);
            }
            if (maxResults < 0 || intOffset < 0) {
                logger.info("A negative number is provided for offset or max: " + "max = " + max + ", offset = " + offset);
                throw new BadRequestError("A negative number is provided for offset or max: " + "max = " + max + ", offset = " + offset);
            }
            Query count = null;

            switch (test) {
                case 1:
                    count = em.createNamedQuery("Students.countAll");
                case 2:
                    count = em.createNamedQuery("Students.getCountForNameAndEducation").setParameter("education", education);
                    count.setParameter("lastname", lastname);
                case 3:
                    count = em.createNamedQuery("Students.getCountAllForEducation").setParameter("education", education);
                case 4:
                    em.createNamedQuery("Students.getCount").setParameter("lastname", lastname);
            }

            int total = ((Long) count.getSingleResult()).intValue();
            logger.debug("Totaal: " + total);
            if (total > 0) {
                result.setTotal(String.valueOf(total));
                logger.debug("MaxResults: " + total);
                query.setMaxResults(maxResults);
                query.setFirstResult(intOffset);
                int nextOffset = intOffset + maxResults;
                int previousOffset = intOffset - maxResults;
                if (nextOffset < total) {
                    String next = createpagingLink(lastname, education, max, String.valueOf(nextOffset));
                    result.setNext(next);
                }
                if (previousOffset > -1) {
                    String previous = createpagingLink(lastname, education, max, String.valueOf(previousOffset));
                    result.setPrevious(previous);
                }
            } else {
                logger.info("No result found error occured " + lastname);
                throw new NotFoundError("No result found");
            }
        } else {
            logger.debug("****geen pagination");
            query.setFirstResult(0);
        }

        List<Students> names = query.getResultList();
        if (names.size() > 0) {
            result.setResults(names);
            names = buildLink(names);
            if (result.getTotal() == null) {
                result.setTotal(String.valueOf(names.size()));
            }
        } else {
            logger.info("No result found for lastname: " + lastname + "and/or education " + education);
            throw new NotFoundError("No result found " + lastname + "and/or education " + education);
        }
        return result;

    }

    @GET
    @Path("/grades/{date}")
    @Produces({"application/json"})
    public Result findByLastUpdate(@PathParam("date") String date) {
        logger.debug("In method findbylastupdate: " + date);
        Result result = new Result();
        Timestamp ts = Timestamp.valueOf(date);
        
        EntityManager em = getEntityManager();
        logger.debug("timestamp: " + ts);
        Query query = em.createNamedQuery("UpdatedGradeStudent.findByUpdate").setParameter("cijfer_gesignaleerd", ts);
        List<UpdatedGradeStudent> students = query.getResultList();
           
        result.setResults(students);
        result.setTotal(String.valueOf(students.size()));
        return result;
    }
    
    @Override
    protected EntityManager getEntityManager() {
        emf = Persistence.createEntityManagerFactory("apis.hsleiden.nl");
        return emf.createEntityManager();
    }

    private void checkLastname(String lastname) {
        try {
            Validator.checkLengthLastname(lastname);
            Validator.validateStringParameter(lastname);
            
        } catch (ValidationException ve) {
            logger.info("A client send a bad request: " + ve.getMessage());
            throw new BadRequestError(ve.getMessage());
        }
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
    
    private String createpagingLink(String lastname, String education, String max, String offset) {
        String hostname = request.getServerName();
        UriBuilder ub = uriInfo.getBaseUriBuilder();
        URI userUri = null;
        if (lastname != null && education != null) {
            userUri = ub.host(hostname).port(443).path(StudentFacadeREST.class).path("/" + lastname).queryParam("education", education).queryParam("max", max).queryParam("offset", offset).build();
        } else if (lastname != null) {
            userUri = ub.host(hostname).port(443).path(StudentFacadeREST.class).path("/" + lastname).queryParam("max", max).queryParam("offset", offset).build();
        } else if (education != null) {
            userUri = ub.host(hostname).port(443).path(StudentFacadeREST.class).queryParam("education", education).queryParam("max", max).queryParam("offset", offset).build();
        } else {
            userUri = ub.host(hostname).port(443).path(StudentFacadeREST.class).queryParam("max", max).queryParam("offset", offset).build();

        }
        return userUri.toString();
    }

    private Exception BadRequestError(String missing_authorization_header) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
