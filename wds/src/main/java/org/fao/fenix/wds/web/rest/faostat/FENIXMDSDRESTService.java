package org.fao.fenix.wds.web.rest.faostat;

import org.fao.fenix.d3s.mdsd.MDSDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * */
@Component
@Path("/mdsd")
public class FENIXMDSDRESTService {

    @Autowired
    MDSDGenerator mdsdGenerator;

    private String mdsd;

    @GET
    public String mdsd() {
        return mdsd == null ? mdsd = mdsdGenerator.generate() : mdsd;
    }

}