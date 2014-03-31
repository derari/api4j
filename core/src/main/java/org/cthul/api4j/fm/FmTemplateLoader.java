package org.cthul.api4j.fm;

import freemarker.cache.TemplateLoader;
import java.io.IOException;
import java.io.Reader;
import org.cthul.resolve.ObjectResolver;
import org.cthul.resolve.RResult;
import org.cthul.resolve.ResourceResolver;

public class FmTemplateLoader extends ObjectResolver<RResult, RuntimeException> implements TemplateLoader {

    public FmTemplateLoader(ResourceResolver resolver) {
        super(resolver);
    }

    public FmTemplateLoader(ResourceResolver... resolver) {
        super(resolver);
    }

    @Override
    protected RResult result(RResult result) throws RuntimeException {
        return result;
    }
    
    @Override
    public Object findTemplateSource(String name) throws IOException {
        return resolve(name);
    }

    @Override
    public long getLastModified(Object templateSource) {
        return 0;
    }

    @Override
    public Reader getReader(Object templateSource, String encoding) throws IOException {
        RResult result = (RResult) templateSource;
        result.setDefaultEncoding(encoding);
        return result.asReader();
    }

    @Override
    public void closeTemplateSource(Object templateSource) throws IOException {
    }
}
