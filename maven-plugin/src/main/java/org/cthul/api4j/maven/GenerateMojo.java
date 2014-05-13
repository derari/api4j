package org.cthul.api4j.maven;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.cthul.api4j.api.Generator;
import org.cthul.resolve.ClassLoaderResourceResolver;
import org.cthul.resolve.CompositeResolver;
import org.cthul.resolve.FileResolver;
import org.cthul.resolve.ResourceResolver;

@Mojo(name="generate-api", defaultPhase=LifecyclePhase.GENERATE_SOURCES,
        threadSafe=true)
public class GenerateMojo extends AbstractMojo {
    
    @Parameter(property = "api4j.source", defaultValue = "src/main/api4j")
    private String source;
    
    @Parameter(property = "api4j.target", defaultValue = "target/generated-sources/api4j")
    private String target;
    
    @Parameter(property = "api4j.includes", defaultValue = "--default--")
    private String[] includes;
    
    @Parameter(property = "api4j.excludes")
    private String[] excludes;
    
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    public GenerateMojo() {
    }

    @Override
    public void execute() throws MojoExecutionException {
        Generator g = createGenerator();
        for (String src: project.getCompileSourceRoots()) {
            g.addSourceTree(new File(src));
        }
        project.addCompileSourceRoot(target);
        if (includes.length == 1 && includes[0].equals("--default--")) {
            includes = new String[]{"**.api.{xml,groovy}"};
        }
        try {
            Path p = Paths.get(source);
            g.runFileTree(p, includes, excludes);
        } catch (Exception e) {
            throw new MojoExecutionException("", e);
        }
    }
    
    protected Generator createGenerator() {
        File base = new File(source);
        ResourceResolver res = new CompositeResolver(
                new ClassLoaderResourceResolver().lookupAll(),
                new FileResolver(base, base).lookupAll());
        Generator g = new Generator(new File(target),res);
        return g;
    }
}
