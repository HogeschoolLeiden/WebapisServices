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
