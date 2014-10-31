package org.cthul.api4j;

import groovy.util.GroovyScriptEngine;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.cthul.api4j.api.Api4JScriptContext;
import org.cthul.api4j.groovy.DSLEngine;

public class Api4JEngine extends DSLEngine {
    
    private final Api4JConfiguration config;

    public Api4JEngine(Api4JConfiguration config) {
        super(config.getResourceResolver());
        this.config = config;
        getEngine().getConfig().getScriptExtensions().add(".api.groovy");
        
        ImportCustomizer ic = new ImportCustomizer();
        ic.addStaticStars(
                "org.cthul.api4j.api1.GeneralImports",
                "org.cthul.strings.JavaNames",
                "org.cthul.strings.Romans",
                "org.cthul.strings.AlphaIndex");
        getEngine().getConfig().addCompilationCustomizers(ic);
    }

    public Api4JConfiguration getConfiguration() {
        return config;
    }

    public GroovyScriptEngine getScriptEngine() {
        return getEngine();
    }
    
    private static String cleanUriString(String uri) {
        return uri.replace('\\', '/');
    }
    
    public Object runScript(String root, String script) {
        return runScript(URI.create(cleanUriString(root)), script);
    }
    
    public Object runScript(Path root, Path script) {
        return runScript(root.toUri(), script.toString());
    }
    
    public Object runScript(Path root, String script) {
        return runScript(root, script, true);
    }
    
    public Object runScript(URI root, String script) {
        return runScript(root, script, true);
    }
    
    public Object runScript(Path root, String script, boolean recreate) {
        return runScript(root.toUri(), script, recreate);
    }
    
    public Object runScript(URI root, String script, boolean recreate) {
        script = cleanUriString(script);
        Api4JScriptContext ctx = getConfiguration().getRootContext()
                .newContext(this, root, script);
        ctx.setRecreate(recreate);
        String scriptName = root.resolve(script).toString();
        return run(ctx, scriptName);
    }
    
    public void runFileTree(Path dir, String... include) throws Exception {
        runFileTree(dir, true, include);
    }
    
    public void runFileTree(Path dir, String[] include, String[] exclude) throws Exception {
        runFileTree(dir, include, exclude, true);
    }
    
    public void runFileTree(final Path dir, List<String> include, List<String> exclude) throws Exception {
        runFileTree(dir, include, exclude, true);
    }
    
    public void runFileTree(Path dir, boolean recreate, String... include) throws Exception {
        runFileTree(dir, Arrays.asList(include), Collections.<String>emptyList(), recreate);
    }
    
    public void runFileTree(Path dir, String[] include, String[] exclude, boolean recreate) throws Exception {
        runFileTree(dir, Arrays.asList(include), Arrays.asList(exclude), recreate);
    }
    
    public void runFileTree(final Path dir, List<String> include, List<String> exclude, boolean recreate) throws Exception {
        final List<PathMatcher> im = toMatchers(dir.getFileSystem(), include);
        final List<PathMatcher> em = toMatchers(dir.getFileSystem(), exclude);
        DirectoryStream.Filter<Path> filter = entry -> {
            if (Files.isDirectory(entry)) return true;
            entry = dir.relativize(entry);
//            System.out.println("? " + entry);
            for (PathMatcher pm: em) {
                if (pm.matches(entry)) {
//                    System.out.println(" - " + pm);
                    return false;
                }
            }
            for (PathMatcher pm: im) {
                if (pm.matches(entry)) {
//                    System.out.println(" + " + pm);
                    return true;
                }
            }
            return false;
        };
        run(dir, dir, filter, recreate);
    }
    
    private void run(Path root, Path current, DirectoryStream.Filter<Path> filter, boolean recreate) throws Exception {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(current, filter)) {
            for (Path file: stream) {
//                System.out.println("! " + file);
                if (Files.isDirectory(file)) {
                    run(root, file, filter, recreate);
                } else {
                    Path rel = root.relativize(file);
                    String fileName = rel.toString();
                    if (fileName.endsWith(".xml")) {
                        System.out.println("Processing " + fileName);
                        getConfiguration().getXmlLoader().load(fileName, file.toFile());
                    } else {
                        System.out.println("Running " + fileName);
                        runScript(root, fileName);
                    }
                }
            }
        }
    }

    private List<PathMatcher> toMatchers(FileSystem fs, List<String> patterns) {
        List<PathMatcher> result = new ArrayList<>(patterns.size());
        for (String s: patterns) {
            try {
                if (s.indexOf(':') < 0) s = "glob:" + s;
                result.add(fs.getPathMatcher(s));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(s, e);
            }
        }
        return result;
    }
}
