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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import nl.hsleiden.webapi.exception.NotFoundError;
import nl.hsleiden.webapi.model.Images;
import org.apache.log4j.Logger;

/**
 *
 * @author hl
 */
@Path("images")
public class ImagesFacadeREST extends AbstractFacade<Images> {
    
    private EntityManagerFactory emf;
    private Logger logger = Logger.getLogger(ImagesFacadeREST.class.getName());

    @PersistenceContext(unitName = "postgresif")
    private EntityManager em;

    public ImagesFacadeREST() {
        super(Images.class);
    }

    @GET
    @Path("/id/{id}")
    @Produces({"application/xml", "application/json"})
    public Images find(@PathParam("id") String id) {

        EntityManager em = getEntityManager();
        Images image = null;
        try {
            Query query = em.createNamedQuery("Images.findByStudentnumber").setParameter("studentnumber", id);
            image = (Images) query.getSingleResult();
        } catch (NoResultException ne) {
            logger.info("Er is geen resultaat voor studentnummer: " + id);
            throw new NotFoundError("No person found for searchparam " + id);
        }
        return image;
    }

    
    @Override
    protected EntityManager getEntityManager() {
        emf = Persistence.createEntityManagerFactory("apis.hsleiden.nl");
        return emf.createEntityManager();
    }
    
}
