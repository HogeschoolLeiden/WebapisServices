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
@Path("employees")
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
    public Result findByLastname(@Context HttpServletRequest request, @QueryParam("lastname") String lastname, 
                     @QueryParam("max") String max, @QueryParam("offset") String offset, @QueryParam ("department") String department) {
         
        int test = 0;
        String lastNameForQuery = null;
        if (lastname != null && lastname.trim().length() > 0 && department != null && department.trim().length() > 0) {
            test = 1;
            lastNameForQuery = formatLastname(lastname);
        } else if (department != null && department.trim().length() > 0) {
            test = 2;
        } else if (lastname != null && lastname.trim().length() > 0) {
            test = 3;
            lastNameForQuery = formatLastname(lastname);
        }
        
        Result result = new Result();
        EntityManager em = getEntityManager();
        
        //determine which query to create, based on provided parameters
        Query query = null;
        switch (test) {
            case 1: 
                checkLastname(lastname.trim());
                query = em.createNamedQuery("Employees.findByLastnameAndDepartment").setParameter("lastname", lastNameForQuery);
                query.setParameter("department", department);
                break;
            case 2:
                query = em.createNamedQuery("Employees.findForDepartment").setParameter("department", department);
                break;
            case 3: 
                checkLastname(lastname.trim());
                query = em.createNamedQuery("Employees.findByLastname").setParameter("lastname", lastNameForQuery);
                break;
            default:
                query = em.createNamedQuery("Employees.findAll");
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
                    count = em.createNamedQuery("Employees.getCountForLastnameAndDepartment").setParameter("department", department);
                    count.setParameter("lastname", lastNameForQuery);
                    break;
                case 2:
                    count = em.createNamedQuery("Employees.getCountForDepartment").setParameter("department", department);
                    break;
                case 3:
                    count = em.createNamedQuery("Employees.getCountForLastname").setParameter("lastname", lastNameForQuery);
                    break;
                default:
                    count = em.createNamedQuery("Employees.getCountAll");
            }

            int total = ((Long) count.getSingleResult()).intValue();
            if (total > 0) {
                result.setTotal(String.valueOf(total));
                query.setMaxResults(maxResults);
                query.setFirstResult(intOffset);
                int nextOffset = intOffset + maxResults;
                int previousOffset = intOffset - maxResults;
                if (nextOffset < total) {
                    String next = createpagingLink(lastname, department, max, String.valueOf(nextOffset));
                    result.setNext(next);
                }
                if (previousOffset > -1) {
                    String previous = createpagingLink(lastname, department, max, String.valueOf(previousOffset));
                    result.setPrevious(previous);
                }
            } else {
                logger.info("No result found error occured " + lastname);
                throw new NotFoundError("No result found for: " + lastname);
            }
        } else {
            logger.debug("****geen pagination");
            query.setFirstResult(0);
        }

        List<Employees> names = query.getResultList();
        if (names.size() > 0) {
            result.setResults(names);
            names = buildLink(names);
            if (result.getTotal() == null) {
                result.setTotal(String.valueOf(names.size()));
            }
        } else {
            logger.info("No result found for lastname: " + lastname + "and/or department " + department);
            throw new NotFoundError("No result found for lastname: " + lastname + "and/or department: " + department);
        }
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
            userUri = ub.host(hostname).port(443).path(EmployeeFacadeREST.class).queryParam("lastname", lastname).queryParam("department", department).queryParam("max", max).queryParam("offset", offset).build();
        } else if (lastname != null) {
            userUri = ub.host(hostname).port(443).path(EmployeeFacadeREST.class).queryParam("lastname", lastname).queryParam("max", max).queryParam("offset", offset).build();
        } else if (department != null) {
            userUri = ub.host(hostname).port(443).path(EmployeeFacadeREST.class).queryParam("department", department).queryParam("max", max).queryParam("offset", offset).build();
        } else {
            userUri = ub.host(hostname).port(443).path(EmployeeFacadeREST.class).queryParam("max", max).queryParam("offset", offset).build();

        }
        return userUri.toString();
    }
}
