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

import java.net.URI;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.UriBuilder;
import nl.hsleiden.webapi.exception.BadRequestError;
import nl.hsleiden.webapi.exception.NotFoundError;
import nl.hsleiden.webapi.model.Persons;
import nl.hsleiden.webapi.util.Result;
import nl.hsleiden.webapi.util.ValidationException;
import nl.hsleiden.webapi.util.Validator;
import org.apache.log4j.Logger;

/**
 * REST Web Service
 *
 * @author hl
 */
@Path("persons")
public class PersonFacadeREST {

    private EntityManagerFactory emf;
    private Logger logger = Logger.getLogger(StudentFacadeREST.class.getName());
    
    @Context
    UriInfo uriInfo;
    @Context
    HttpServletRequest request;
    @Context
    HttpServletResponse response;
    
    
    /**
     * Creates a new instance of PersonFacadeREST
     */
    public PersonFacadeREST() {
    }


    
    /**
     * Retrieves representation of an instance of nl.hsleiden.webapi.v1.service.PersonFacadeREST
     * @return an instance of java.lang.String
     */
    @GET
    @Produces({"application/json", "application/xml"})
    public Result findPersonsByLastName(@QueryParam("lastname") String lastname, @QueryParam("education") String education, @QueryParam("department") String department, @QueryParam("max") String max, @QueryParam("offset") String offset) {
    
        Result result = new Result();
        EntityManager em = getEntityManager();
        Query query = null;
                
        String nameForQuery = null;
        int test = 0;
        if (lastname != null && lastname.trim().length() > 0 && education != null && education.trim().length() > 0) {
            test = 1;
            nameForQuery = formatLastname(lastname);
        } else if (lastname != null && lastname.trim().length() > 0 && department != null && department.trim().length() > 0) { 
            test = 2;
            nameForQuery = formatLastname(lastname);
        } else if (lastname != null && lastname.trim().length() > 0 ) {
            test = 3;
            nameForQuery = formatLastname(lastname);
        } else if (education != null && education.trim().length() > 0) {
            test = 4;
        } else if (department != null && department.trim().length() > 0) {
            test = 5;
        }
        
        switch (test) {
            case 1:
                checkLastname(lastname);
                query = em.createNamedQuery("Persons.findByLastnameAndEducation").setParameter("lastname", nameForQuery);
                query.setParameter("education", education);
                break;
            case 2:
                checkLastname(lastname);
                query = em.createNamedQuery("Persons.findByLastnameAndDepartment").setParameter("lastname", nameForQuery);
                query.setParameter("department", department);
                break;
            case 3:
                checkLastname(lastname);
                query = em.createNamedQuery("Persons.findByLastname").setParameter("lastname", nameForQuery);
                education = null; //for building correct paging links this must be null
                break;
            case 4:
                query = em.createNamedQuery("Persons.findByEducation").setParameter("education", education);
                lastname = null; //for building correct paging links this must be null
                break;
            case 5:
                query = em.createNamedQuery("Persons.findByDepartment").setParameter("department", department);
                lastname = null; //for building correct paging links this must be null
                break;
            default:
                lastname = null; //for building correct paging links this must be null
                education = null;
                department = null;
                query = em.createNamedQuery("Persons.findAll"); 
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
            switch (test){
            case 1:
                count = em.createNamedQuery("Persons.getCountForLastnameAndEducation").setParameter("lastname", nameForQuery);
                count.setParameter("education", education);
                break;
            case 2:
                count = em.createNamedQuery("Persons.getCountForLastnameAndDepartment").setParameter("lastname", nameForQuery);
                count.setParameter("department", department);
                break;
            case 3:
                count = em.createNamedQuery("Persons.getCountForLastname").setParameter("lastname", nameForQuery);
                break;
            case 4:
                count = em.createNamedQuery("Persons.getCountForEducation").setParameter("education", education);
                break;
            case 5:
                count = em.createNamedQuery("Persons.getCountForDepartment").setParameter("department", department);
                break;
            default:
                count = em.createNamedQuery("Persons.getCountAll"); 
            }
            
            int total = ((Long) count.getSingleResult()).intValue();
            logger.debug("Totaal: " + total);
            if (total > 0) {
                result.setTotal(String.valueOf(total));
                query.setMaxResults(maxResults);
                query.setFirstResult(intOffset);
                int nextOffset = intOffset + maxResults;
                int previousOffset = intOffset - maxResults;
                if (nextOffset < total) {
                    String next = createpagingLink(lastname, education, department, max, String.valueOf(nextOffset));
                    result.setNext(next);
                }
                if (previousOffset > -1) {
                    String previous = createpagingLink(lastname, education, department, max, String.valueOf(previousOffset));
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
        List<Persons> names = query.getResultList();
        if (names.size() > 0) {
            result.setResults(names);
            names = buildLink(names);
            if (result.getTotal() == null) {
                result.setTotal(String.valueOf(names.size()));
            }
        } else {
            logger.info("No result found for lastname: " + lastname);
            throw new NotFoundError("No result found " + lastname);
        }
        return result;
    }

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
     * Creates an uri for linking to more information. Persons.id is used.
     *
     * @param List persons
     * @return List persons
     */
    private List<Persons> buildLink(List<Persons> persons) {
        String hostname = request.getServerName();

        for (Persons p : persons) {
            UriBuilder ub = uriInfo.getBaseUriBuilder();
            URI userUri = null;
            if (p.getStudentNumber() != null) {
                userUri = ub.host(hostname).port(443).path(StudentFacadeREST.class).path("/id/").path(p.getId()).build();
            } else if (p.getEmployeeNumber() != null) {
                userUri = ub.host(hostname).port(443).path(EmployeeFacadeREST.class).path("/id/").path(p.getId()).build();
            }
            p.setUri(userUri.toString());
        }
        return persons;
    }

    private String createpagingLink(String lastname, String education, String department, String max, String offset) {
        String hostname = request.getServerName();
        UriBuilder ub = uriInfo.getBaseUriBuilder();
        URI userUri = null;
        if (lastname != null && education != null) {
            userUri = ub.host(hostname).port(443).path(PersonFacadeREST.class).queryParam("lastname", lastname).queryParam("education", education).queryParam("max", max).queryParam("offset", offset).build();
        } else if (lastname != null && department != null) { 
            userUri = ub.host(hostname).port(443).path(PersonFacadeREST.class).queryParam("lastname", lastname).queryParam("department", department).queryParam("max", max).queryParam("offset", offset).build();
        } else if (lastname != null) {
            userUri = ub.host(hostname).port(443).path(PersonFacadeREST.class).queryParam("lastname", lastname).queryParam("max", max).queryParam("offset", offset).build();
        } else if (education != null) {
            userUri = ub.host(hostname).port(443).path(PersonFacadeREST.class).queryParam("education", education).queryParam("max", max).queryParam("offset", offset).build();
        } else if (department != null) {
            userUri = ub.host(hostname).port(443).path(PersonFacadeREST.class).queryParam("department", department).queryParam("max", max).queryParam("offset", offset).build();
        } else {
            userUri = ub.host(hostname).port(443).path(PersonFacadeREST.class).queryParam("max", max).queryParam("offset", offset).build();

        }
        return userUri.toString();
    }
}
