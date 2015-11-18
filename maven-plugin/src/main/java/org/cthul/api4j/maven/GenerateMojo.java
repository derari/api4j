package org.cthul.api4j.maven;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.cthul.api4j.Api4JConfiguration;
import org.cthul.api4j.Api4JEngine;
import org.cthul.api4j.Launch;
import org.cthul.resolve.ClassLoaderResourceResolver;
import org.cthul.resolve.CompositeResolver;
import org.cthul.resolve.FileResolver;
import org.cthul.resolve.ResourceResolver;

@Mojo(name="generate-api", defaultPhase=LifecyclePhase.GENERATE_SOURCES,
        threadSafe=true)
public class GenerateMojo extends AbstractMojo {
    
    @Parameter(property = "api4j.source", defaultValue = "${basedir}/src/main/api4j")
    private String source;
    
    @Parameter(property = "api4j.target", defaultValue = "${project.build.directory}/generated-sources/api4j")
    private String target;
    
    @Parameter(property = "api4j.includes", defaultValue = "--default--")
    private String[] includes;
    
    @Parameter(property = "api4j.excludes")
    private String[] excludes;
    
    @Parameter(property = "api4j.recreate", defaultValue = "false")
    private boolean recreate;
    
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    public GenerateMojo() {
    }

    @Override
    public void execute() throws MojoExecutionException {
        Api4JConfiguration cfg = createConfiguration();
        for (String src: project.getCompileSourceRoots()) {
            cfg.addSourceFolder(new File(src));
        }
        
        Api4JEngine engine = cfg.createEngine();
        project.addCompileSourceRoot(target);
        project.addTestCompileSourceRoot(target);
        if (includes.length == 1 && includes[0].equals("--default--")) {
            includes = new String[]{Launch.defaultInclude()};
        }
        try {
            Path p = Paths.get(source);
            engine.runFileTree(p, includes, excludes);
        } catch (Exception e) {
            throw new MojoExecutionException("", e);
        }
    }
    
    protected Api4JConfiguration createConfiguration() {
        return Launch.createConfiguration(new File(target), Arrays.asList(Paths.get(source)));
//        File base = new File(source);
//        ResourceResolver res = new CompositeResolver(
//                new ClassLoaderResourceResolver().lookupAll(),
//                new FileResolver(base, base).lookupAll());
//        Api4JConfiguration cfg = new Api4JConfiguration(new File(target),res);
//        return cfg;
    }
}
