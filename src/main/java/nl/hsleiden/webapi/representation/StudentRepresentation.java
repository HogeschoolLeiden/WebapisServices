/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.hsleiden.webapi.representation;

import com.sun.jersey.server.linking.Binding;
import com.sun.jersey.server.linking.Link;
import com.sun.jersey.server.linking.Links;
import com.sun.jersey.server.linking.Ref;
import com.sun.jersey.server.linking.Ref.Style;
import java.net.URI;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import nl.hsleiden.webapi.v1.service.StudentFacadeREST;


/**
 *
 * @author fadministrator
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="item")
@Links({
    @Link(
        value=@Ref(
            resource=StudentFacadeREST.class,
            style = Style.ABSOLUTE,
            condition="${resource.next}",
            bindings=@Binding(name="id", value="${resource.nextId}")
        ),
        rel="next"
    ),
    @Link(
        value=@Ref(
            resource=StudentFacadeREST.class,
            style = Style.ABSOLUTE,
            condition="${resource.prev}",
            bindings=@Binding(name="id", value="${resource.prevId}")
        ),
        rel="prev"
    )
})
public class StudentRepresentation {
    @XmlElement
    private String name;

    @Ref(
        resource=StudentFacadeREST.class,
        style = Style.ABSOLUTE,
        bindings=@Binding(name="id", value="${resource.id}")
    )
    @XmlElement
    URI self;

    @Ref(
        resource=StudentFacadeREST.class,
        style = Style.ABSOLUTE,
        condition="${resource.next}",
        bindings=@Binding(name="id", value="${resource.nextId}")
    )
    @XmlElement
    URI next;

    @Ref(
        resource=StudentFacadeREST.class,
        style = Style.ABSOLUTE,
        condition="${resource.prev}",
        bindings=@Binding(name="id", value="${resource.prevId}")
    )
    @XmlElement
    URI prev;

    public StudentRepresentation() {
        this.name = "";
    }

    public StudentRepresentation(String name) {
        this.name = name;
    }
 
}
