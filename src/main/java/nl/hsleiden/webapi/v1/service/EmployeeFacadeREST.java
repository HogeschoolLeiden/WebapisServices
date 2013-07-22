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
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import nl.hsleiden.webapi.model.Employee;
import nl.hsleiden.webapi.model.Employees;
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
    @Path("{lastname}")
    @Produces({"application/json","application/xml"}) 
    public List<Employees> findByLastname(@PathParam("lastname") String lastname) {
        logger.debug("In methode ");
        try {
            Validator.checkLengthLastname(lastname);
        } catch (ValidationException ve) {
            ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            builder.entity(ve.getMessage());
            builder.type(MediaType.TEXT_PLAIN);
            Response res = builder.build();
            throw new WebApplicationException(res);
        }      
       
        try {
            Validator.validateStringParameter(lastname);
        } catch (ValidationException ve) {
            ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            builder.entity(ve.getMessage() );
            builder.type(MediaType.TEXT_PLAIN);
            Response res = builder.build();
            throw new WebApplicationException(res);
        }
        
        EntityManager em = getEntityManager();
        String name = formatLastname(lastname);
        
        Query query = em.createNamedQuery("Employees.findByLastname").setParameter("lastname", name);
        List<Employees> names = query.getResultList();
        
        if (names.isEmpty()) {
            logger.info("Er is geen resultaat voor param: " + lastname);
            throw new NotFoundException("No person found for searchparam " + lastname);
        }
        names = buildLink(names);
        return names;
    }

    @Override
    protected EntityManager getEntityManager() {
        emf = Persistence.createEntityManagerFactory("postgresif");
        return emf.createEntityManager();
    }
    
    /**
     * Formats lastname for a case insensitive search
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
        UriBuilder ub = uriInfo.getBaseUriBuilder();
        for (Employees n : names) {
            URI userUri = ub.host(hostname).port(443).path(EmployeeFacadeREST.class).path("/id/").path(n.getId()).build();
            n.setUri(userUri.toString());
        }
        return names;
    }
}
