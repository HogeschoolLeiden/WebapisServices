/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.hsleiden.webapi.util;


import org.apache.log4j.Logger;

/**
 *
 * @author hl
 */
public final class Validator {
    private static Logger logger = Logger.getLogger(Validator.class.getName());
    
    
    /**
     * Checks if the minimal 2 characters are provided.
     * @param lastname
     * @throws WebServiceException
     */
    public static void checkLengthLastname(String lastname) throws ValidationException{
        logger.info("Ingevoerde naam: " + lastname);
        if (lastname.length() < 2) {
            logger.info("Er zijn te weinig tekens ingevoerd: " + lastname);
            throw new ValidationException("Not enough characters are inserted. At least 2 characters should be provided. Provided parameter: " + lastname);
        }
    }

    /**
     * Checks if the input only contains letters (Unicode) or minus (-).
     * @param parameter
     * @throws WebServiceException
     */
    public static void validateStringParameter(String parameter) throws ValidationException{
        logger.info("Ingevoerde string: " + parameter);
        boolean hasNonAlpha = parameter.matches("[\\p{L}||[\\p{Lu}]||-]*");
        if (hasNonAlpha != true) {
            logger.info("Er zijn ongewenste tekens ingevoerd: " + parameter);
            throw new ValidationException("There are unaccepted tokens inserted. Only UTF-8 characters are accepted. Provided parameter: " + parameter);
        }
    }   
}
