package net.whydah.token.application;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.apache.ApacheHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.net.URI;

/**
 * Created by baardl on 22.03.14.
 */
public class ApplicationAuthenticatorImpl implements ApplicationAuthenticator {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationAuthenticatorImpl.class);


    @Inject
    @Named("useridbackendUri")
    private URI useridbackendUri;
    private final Client restClient;

    public ApplicationAuthenticatorImpl() {
        restClient = ApacheHttpClient.create();
    }

    @Override
    public ApplicationToken logonApplication(ApplicationCredential applicationCredential) {
        ApplicationToken token = null;
        logger.trace("logonApplication - Calling UserIdentityBackend at " + useridbackendUri);
        if (applicationCredential != null) {
            WebResource webResource = restClient.resource(useridbackendUri).path("logon");
            ClientResponse response = webResource.type(MediaType.APPLICATION_XML).post(ClientResponse.class, applicationCredential.toXML());
            token = buildApplictionToken(applicationCredential, response);
            logger.trace("Logged on Applicaton {}", token.toXML());
        }

        return token;
    }

    private ApplicationToken buildApplictionToken(ApplicationCredential applicationCredential, ClientResponse response) {
        return new ApplicationToken();
    }

    @Override
    public boolean validateApplication(ApplicationToken applicationToken) {
        return false;
    }
}
