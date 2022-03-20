package pl.lodz.p.it.dk.security.etag;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
@EtagFilterBinding
public class EtagFilter implements ContainerRequestFilter {

    @Inject
    private Verifier verifier;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String etagHeader = requestContext.getHeaderString("If-Match");

        if (etagHeader == null || etagHeader.isEmpty() || !verifier.verify(etagHeader)) {
            requestContext.abortWith(Response.status(Response.Status.BAD_REQUEST).build());
        }
    }
}
