/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.hsleiden.webapi.v1.service;

//import com.sun.jersey.api.NotFoundException;
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
import javax.ws.rs.NotFoundException;
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
import nl.hsleiden.webapi.model.Employee;
import nl.hsleiden.webapi.model.Employees;
import nl.hsleiden.webapi.util.Result;
import nl.hsleiden.webapi.util.ValidationException;
import nl.hsleiden.webapi.util.Validator;
import org.apache.log4j.Logger;

/**
 *
 * @author hl
 */
@Path("v1/employees")
public class EmployeeFacadeREST extends AbstractFacade<Employee> {
    private EntityManagerFactory emf;
    private Logger logger = Logger.getLogger(EmployeeFacadeREST.class.getName());

    public EmployeeFacadeREST() {
        super(Employee.class);
    }

    @Context UriInfo uriInfo;
    @Context HttpServletRequest request;
    @Context HttpServletResponse response;
    
     
    @GET
    @Path("/id/{id}")
    @Produces({"application/json", "application/xml"})
    public Employee findById(@PathParam("id") String id) {
        EntityManager em = getEntityManager();
          
        if (id.equals("@me")) {
            String uid = (String) request.getAttribute("uid");
            String organisation = (String) request.getAttribute("homeOrganisation");
            
            if (uid != null && uid.length() > 0 && organisation != null && organisation.length() > 0) {

                Query query = em.createNamedQuery("Employee.findByUID").setParameter("accountname", uid);
                query.setParameter("organisation", organisation);
                try {
                    Employee employee = (Employee) query.getSingleResult();
                    return employee;
                } catch (NoResultException ne) {
                    logger.info("Er is geen resultaat voor param: @me en accountname: " + uid);
                    throw new NotFoundError("No person found for searchparam @me");
                }
            } else {
                throw new UnauthorizedError("Not allowed");
            }
        }
        Query query = em.createNamedQuery("Employee.findById").setParameter("id", id);
        try {
            Employee employee = (Employee) query.getSingleResult();
            return employee;
        } catch(NoResultException nre) {
            logger.info("Er is geen resultaat voor param: " + id);
            throw new NotFoundException("No person found for searchparam " + id);
        }
    }
    
    @GET
    @Produces({"application/json", "application/xml"})
    public Result findAll(@QueryParam("max") String max, @QueryParam("offset") String offset, @QueryParam ("department") String department) {
        EntityManager em = getEntityManager();
        Result result = new Result();
        Query query = null;
        
        if (department != null && department.trim().length() > 0) {
            query = em.createNamedQuery("Employees.findAllForDepartment()").setParameter("department", department);
        } else {
            query = em.createNamedQuery("Employees.findAll()");
        }
        
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
            if (department != null && department.trim().length() > 0) {
                logger.debug("******* department: " + department);
                count = em.createNamedQuery("Employees.getCountAllForDepartment").setParameter("department", department);
            } else {
                department = null;
                count = em.createNamedQuery("Employees.getCountAll");
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
                if (nextOffset <= total) {
                    String next = createpagingLink(null, department, max, String.valueOf(nextOffset));
                    result.setNext(next);
                }
                if (previousOffset > -1) {
                    String previous = createpagingLink(null, department, max, String.valueOf(previousOffset));
                    result.setPrevious(previous);
                }
            } else {
                logger.info("No result found error occured ");
                throw new NotFoundError("No result found");
            }
        } else {
            logger.debug("****geen pagination");
            query.setFirstResult(0);
        }
        
        List<Employees> names = query.getResultList();
        if (names.size() > 0) {
            result.setResults(names);
            buildLink(names);
            if (result.getTotal() == null) {
                result.setTotal(String.valueOf(names.size()));
            }
        } 
        return result;
    }
    
    
    @GET
    @Path("{lastname}")
    @Produces({"application/json", "application/xml"})
    public Result findByLastname(@PathParam("lastname") String lastname, @QueryParam("department") String department, @QueryParam("max") String max, @QueryParam("offset") String offset) {

        try {
            Validator.checkLengthLastname(lastname);
            Validator.validateStringParameter(lastname);
        } catch (ValidationException ve) {
            logger.info("A client send a bad request: " + ve.getMessage());
            throw new BadRequestError(ve.getMessage());
        }
        
        Result result = new Result();
        EntityManager em = getEntityManager();
        String name = formatLastname(lastname);
        Query query = null;
        if (department != null && department.trim().length() > 0) {
            query = em.createNamedQuery("Employees.findByLastnameAndDepartment").setParameter("lastname", name);
            query.setParameter("department", department);
        } else {
            query = em.createNamedQuery("Employees.findByLastname").setParameter("lastname", name);
        }
        
        int maxResults = -1;
        int intOffset = -1;
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
            Query count = em.createNamedQuery("Employees.getCount").setParameter("lastname", name);

            int total = ((Long) count.getSingleResult()).intValue();
            logger.debug("Totaal: " + total);
            if (total > 0) {
                result.setTotal(String.valueOf(total));
                logger.debug("MaxResults: " + total);
                query.setMaxResults(maxResults);
                query.setFirstResult(intOffset);
                int nextOffset = intOffset + maxResults;
                int previousOffset = intOffset - maxResults;
                if (nextOffset <= total) {
                    String next = createpagingLink(lastname, department, max, String.valueOf(nextOffset));
                    result.setNext(next);
                }
                if (previousOffset > -1) {
                    String previous = createpagingLink(lastname, department,  max, String.valueOf(previousOffset));
                    result.setPrevious(previous);
                }
            } else {
                logger.info("No result found error occured " + lastname);
                throw new NotFoundError("No result found " + lastname);
            }
        } else {
            logger.debug("****geen pagination");
            query.setFirstResult(0);
        }
        List<Employees> names = query.getResultList();
        if (names.size() > 0) {
            result.setResults(names);
            names = buildLink(names);
        } else {
            logger.info("No result found for lastname: " + lastname);
            throw new NotFoundError("No result found " + lastname);
        }
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
     * @param List persons
     * @return List persons
     */
    private List<Employees> buildLink(List<Employees> names) {
        
        String hostname = request.getServerName();
        
        for (Employees n : names) {
            UriBuilder ub = uriInfo.getBaseUriBuilder();
            URI userUri = ub.host(hostname).port(443).path(EmployeeFacadeREST.class).path("/id/").path(n.getId()).build();
            n.setUri(userUri.toString());
        }
        
        return names;
    }
    
//    private String createpagingLink(String param, String max, String offset) {
//        String hostname = request.getServerName();
//        UriBuilder ub = uriInfo.getBaseUriBuilder();
//        URI userUri = ub.host(hostname).port(443).path(EmployeeFacadeREST.class).path("/" + param).queryParam("max", max).queryParam("offset", offset).build();
//        
//        return userUri.toString();
//    }
    
    private String createpagingLink(String lastname, String department, String max, String offset) {
        String hostname = request.getServerName();
        UriBuilder ub = uriInfo.getBaseUriBuilder();
        URI userUri = null;
        if (lastname != null && department != null) {
            userUri = ub.host(hostname).port(443).path(EmployeeFacadeREST.class).path("/" + lastname).queryParam("department", department).queryParam("max", max).queryParam("offset", offset).build();
        } else if (lastname != null) {
            userUri = ub.host(hostname).port(443).path(EmployeeFacadeREST.class).path("/" + lastname).queryParam("max", max).queryParam("offset", offset).build();
        } else if (department != null) {
            userUri = ub.host(hostname).port(443).path(EmployeeFacadeREST.class).queryParam("department", department).queryParam("max", max).queryParam("offset", offset).build();
        } else {
            userUri = ub.host(hostname).port(443).path(EmployeeFacadeREST.class).queryParam("max", max).queryParam("offset", offset).build();

        }
        return userUri.toString();
    }
}
