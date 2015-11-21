package org.cthul.api4j.groovy;

import groovy.util.ResourceConnector;
import groovy.util.ResourceException;
import java.net.URLConnection;
import org.cthul.resolve.ObjectResolver;
import org.cthul.resolve.RResponse;
import org.cthul.resolve.RResult;
import org.cthul.resolve.ResolvingException;
import org.cthul.resolve.ResourceResolver;

public class ScriptConnector extends ObjectResolver<URLConnection, ResourceException> implements ResourceConnector {

    public ScriptConnector(ResourceResolver resolver) {
        super(resolver);
    }

    public ScriptConnector(ResourceResolver... resolver) {
        super(resolver);
    }

    @Override
    protected URLConnection result(RResult result) throws ResourceException {
        try {
            return result.asURLConnection();
        } catch (ResolvingException e) {
            throw new ResourceException(e.getMessage(), e.getResolvingCause());
        }
    }
    
    protected URLConnection noResult(RResponse res) throws ResourceException {
        throw new ResourceException(res.getRequest().getUriOrId());
    }

    @Override
    public URLConnection getResourceConnection(String name) throws ResourceException {
        return resolve(name);
    }
}
