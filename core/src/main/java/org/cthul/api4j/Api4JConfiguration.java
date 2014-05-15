package org.cthul.api4j;

import com.thoughtworks.qdox.JavaProjectBuilder;
import freemarker.template.Configuration;
import groovy.lang.Closure;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import org.cthul.api4j.api.*;
import org.cthul.api4j.fm.FmTemplate;
import org.cthul.api4j.fm.FmTemplateLoader;
import org.cthul.api4j.gen.GeneratedClass;
import org.cthul.api4j.groovy.DslUtils;
import org.cthul.api4j.groovy.GroovyDsl;
import org.cthul.api4j.xml.XmlLoader;
import org.cthul.resolve.ResourceResolver;

public class Api4JConfiguration {
    
    private final File out;
    private final JavaProjectBuilder qdox;
    private final ScriptFinder scriptFinder;
    private final Configuration fmConfig;
    private final XmlLoader xmlLoader;
    private final ScriptContext rootContext = new ScriptContext(this);

    @SuppressWarnings({"LeakingThisInConstructor", "OverridableMethodCallInConstructor"})
    public Api4JConfiguration(File out, ResourceResolver resolver) {
        this.out = out;
        this.qdox = new JavaProjectBuilder();
        this.scriptFinder = new ScriptFinder(this, resolver);
        fmConfig = new Configuration();
        fmConfig.setTemplateLoader(new FmTemplateLoader(resolver));
        fmConfig.setDefaultEncoding("UTF-8");
        fmConfig.setLocalizedLookup(false);
        fmConfig.addAutoInclude("org/cthul/api4j/api1/lib.ftl");
        xmlLoader = new XmlLoader(this);
    }
    
    public void runScript(File f) {
        scriptFinder.resolve(f).run();
    }
    
    public void runScript(String s) {
        scriptFinder.resolve(s).run();
    }
    
    public void addSourceTree(File f) {
        qdox.addSourceTree(f);
    }

    public JavaProjectBuilder getQdox() {
        return qdox;
    }

    public ScriptContext getRootContext() {
        return rootContext;
    }

    public void runFileTree(Path dir, String... include) throws Exception {
        runFileTree(dir, Arrays.asList(include), Collections.<String>emptyList());
    }
    
    public void runFileTree(Path dir, String[] include, String[] exclude) throws Exception {
        runFileTree(dir, Arrays.asList(include), Arrays.asList(exclude));
    }
    
    public void runFileTree(final Path dir, List<String> include, List<String> exclude) throws Exception {
        final List<PathMatcher> im = toMatchers(dir.getFileSystem(), include);
        final List<PathMatcher> em = toMatchers(dir.getFileSystem(), exclude);
        DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>() {
            @Override
            public boolean accept(Path entry)  {
                if (Files.isDirectory(entry)) return true;
                entry = dir.relativize(entry);
                System.out.println("? " + entry);
                for (PathMatcher pm: em) {
                    if (pm.matches(entry)) return false;
                }
                for (PathMatcher pm: im) {
                    if (pm.matches(entry)) return true;
                }
                return false;
            }
        };
        run(dir, dir, filter);
    }
    
    private void run(Path root, Path current, DirectoryStream.Filter<Path> filter) throws Exception {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(current, filter)) {
            for (Path file: stream) {
                System.out.println("! " + file);
                if (Files.isDirectory(file)) {
                    run(root, file, filter);
                } else {
                    Path rel = root.relativize(file);
                    String fileName = rel.toString();
                    if (fileName.endsWith(".xml")) {
                        System.out.println("Processing " + fileName);
                        xmlLoader.load(fileName, file.toFile());
                    } else {
                        System.out.println("Running " + fileName);
                        runScript(fileName);
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

    public GeneratedClass generateClass(String name) {
        File f = new File(out, name.replace('.', '/') + ".java");
        return new GeneratedClass(getQdox(), f, name.replace('/', '.'));
    }
    
    public Object generateClass(String name, GroovyDsl dsl, Closure<?> c) throws IOException {
        try (GeneratedClass cg = generateClass(name)) {
            return DslUtils.configure(dsl, cg, c);
        }
    }

    public Template fmTemplate(String name) {
        try {
            return new FmTemplate(fmConfig.getTemplate(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
//=======
//package org.cthul.api4j.api;
//
//import com.thoughtworks.qdox.JavaDocBuilder;
//import freemarker.template.Configuration;
//import groovy.lang.Closure;
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.DirectoryStream;
//import java.nio.file.FileSystem;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.PathMatcher;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import org.cthul.api4j.fm.DslObjectWrapper;
//import org.cthul.api4j.fm.FmTemplate;
//import org.cthul.api4j.fm.FmTemplateLoader;
//import org.cthul.api4j.gen.ClassGenerator;
//import org.cthul.api4j.gen.FileGenerator;
//import org.cthul.api4j.groovy.GroovyDsl;
//import org.cthul.api4j.xml.XmlLoader;
//import org.cthul.resolve.ResourceResolver;
//
//public class Api4JConfiguration {
//    
//    private final File out;
//    private final JavaDocBuilder qdox;
//    private final ScriptFinder scriptFinder;
//    private final Configuration fmConfig;
//    private final Templates templates = new Templates();
//    private final XmlLoader xmlLoader;
//
//    @SuppressWarnings({"LeakingThisInConstructor", "OverridableMethodCallInConstructor"})
//    public Api4JConfiguration(File out, ResourceResolver resolver) {
//        this.out = out;
//        this.qdox = new JavaDocBuilder();
//        this.scriptFinder = new ScriptFinder(this, resolver);
//        this.xmlLoader = new XmlLoader(this);
//        fmConfig = new Configuration();
//        fmConfig.setTemplateLoader(new FmTemplateLoader(resolver));
//        fmConfig.setDefaultEncoding("UTF-8");
//        fmConfig.setLocalizedLookup(false);
//        fmConfig.addAutoInclude("org/cthul/api4j/api1/lib.ftl");
//        fmConfig.setObjectWrapper(new DslObjectWrapper());
//    }
//    
//    public void runScript(File f) {
//        ApiScript script = scriptFinder.resolve(f);
//        if (script == null) throw new IllegalArgumentException(
//                f + " no found");
//        script.run();
//    }
//    
//    public void runScript(String s) {
//        ApiScript script = scriptFinder.resolve(s);
//        if (script == null) throw new IllegalArgumentException(
//                s + " no found");
//        script.run();
//    }
//    
//    public void runFileTree(Path dir, String... include) throws Exception {
//        runFileTree(dir, Arrays.asList(include), Collections.<String>emptyList());
//    }
//    
//    public void runFileTree(Path dir, String[] include, String[] exclude) throws Exception {
//        runFileTree(dir, Arrays.asList(include), Arrays.asList(exclude));
//    }
//    
//    public void runFileTree(final Path dir, List<String> include, List<String> exclude) throws Exception {
//        final List<PathMatcher> im = toMatchers(dir.getFileSystem(), include);
//        final List<PathMatcher> em = toMatchers(dir.getFileSystem(), exclude);
//        DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>() {
//            @Override
//            public boolean accept(Path entry)  {
//                if (Files.isDirectory(entry)) return true;
//                entry = dir.relativize(entry);
//                System.out.println("? " + entry);
//                for (PathMatcher pm: em) {
//                    if (pm.matches(entry)) return false;
//                }
//                for (PathMatcher pm: im) {
//                    if (pm.matches(entry)) return true;
//                }
//                return false;
//            }
//        };
//        run(dir, dir, filter);
//    }
//    
//    private void run(Path root, Path current, DirectoryStream.Filter<Path> filter) throws Exception {
//        try (DirectoryStream<Path> stream = Files.newDirectoryStream(current, filter)) {
//            for (Path file: stream) {
//                System.out.println("! " + file);
//                if (Files.isDirectory(file)) {
//                    run(root, file, filter);
//                } else {
//                    Path rel = root.relativize(file);
//                    String fileName = rel.toString();
//                    if (fileName.endsWith(".xml")) {
//                        System.out.println("Processing " + fileName);
//                        xmlLoader.load(fileName, file.toFile());
//                    } else {
//                        System.out.println("Running " + fileName);
//                        runScript(fileName);
//                    }
//                }
//            }
//        }
//    }
//    
//    private List<PathMatcher> toMatchers(FileSystem fs, List<String> patterns) {
//        List<PathMatcher> result = new ArrayList<>(patterns.size());
//        for (String s: patterns) {
//            try {
//                if (s.indexOf(':') < 0) s = "glob:" + s;
//                result.add(fs.getPathMatcher(s));
//            } catch (IllegalArgumentException e) {
//                throw new IllegalArgumentException(s, e);
//            }
//        }
//        return result;
//    }
//    
//    public void addSourceTree(File f) {
//        qdox.addSourceTree(f);
//    }
//
//    public JavaDocBuilder getQdox() {
//        return qdox;
//    }
//    
//    public ClassGenerator generateClass(GroovyDsl dsl, String name) {
//        return new ClassGenerator(dsl, name);
//    }
//    
//    public Object generateClass(GroovyDsl dsl, String name, Closure<?> c) throws IOException {
//        ClassGenerator cg =  generateClass(dsl, name);
//        try {
//            return cg.configure(c);
//        } finally {
//            writeJavaFile(cg);
//        }
//    }
//    
//    public void writeJavaFile(ClassGenerator cg) throws IOException {
//        try (FileGenerator f = new FileGenerator(cg.getDsl(), cg.getFile(out))) {
//            f.body(cg);
//        }
//    }
//
//    public Templates getTemplates() {
//        return templates;
//    }
//    
//    public Template fmTemplate(String name) {
//        try {
//            return new FmTemplate(fmConfig.getTemplate(name));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    
//    public Template fm(String template) {
//        try {
//            return new FmTemplate(
//                    new freemarker.template.Template("t", template, fmConfig));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
//>>>>>>> ab04573103cb1c0605321368b157bdb1e583e6ff
