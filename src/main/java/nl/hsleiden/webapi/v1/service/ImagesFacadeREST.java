/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.hsleiden.webapi.v1.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import nl.hsleiden.webapi.model.Images;
import org.apache.log4j.Logger;

/**
 *
 * @author hl
 */
@Path("v1/images")
public class ImagesFacadeREST extends AbstractFacade<Images> {
    
    private EntityManagerFactory emf;
    private Logger logger = Logger.getLogger(ImagesFacadeREST.class.getName());

    @PersistenceContext(unitName = "postgresif")
    private EntityManager em;

    public ImagesFacadeREST() {
        super(Images.class);
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Images find(@PathParam("id") String id) {
        logger.debug("In de methode find images" + id);
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Images.findByStudentnumber").setParameter("studentnumber", id);
        Images image = (Images)query.getSingleResult();
        logger.debug("resultaat: " + image.getStudentnumber());
        logger.debug("resultaat: " + image.getImage());
        
        return image;
        //return super.find(id);
    }

    
    @Override
    protected EntityManager getEntityManager() {
        emf = Persistence.createEntityManagerFactory("postgresif");
        return emf.createEntityManager();
    }
    
}
