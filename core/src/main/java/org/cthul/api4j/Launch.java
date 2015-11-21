package org.cthul.api4j;

import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.cthul.resolve.ClassLoaderResourceResolver;
import org.cthul.resolve.CompositeResolver;
import org.cthul.resolve.FileResolver;
import org.cthul.resolve.ResourceResolver;

/**
 * Used to launch Api4J from the command line.
 */
public class Launch {
    
    public static void main(String[] argArray) throws Exception {
        if (argArray.length == 0) {
            printHelpMessage();
            System.exit(1);
        }
        Deque<String> args = new ArrayDeque<>(Arrays.asList(argArray));
        File target = new File(".");
        List<File> sources = new ArrayList<>();
        List<Path> scripts = new ArrayList<>();
        List<String> includes = new ArrayList<>();
        List<String> excludes = new ArrayList<>();
        if (hasValue(args)) {
            target = new File(args.removeFirst());
            while (hasValue(args)) {
                sources.add(new File(args.removeFirst()));
            }
        }
        while (!args.isEmpty()) {
            switch (args.removeFirst()) {
                case "-?":
                case "-h":
                case "--help":
                    printHelpMessage();
                    System.exit(0);
                    break;
                case "-x":
                case "--script":
                    while (hasValue(args)) {
                        scripts.add(Paths.get(args.removeFirst()));
                    }
                    break;
                case "-i":
                case "--include":
                    while (hasValue(args)) {
                        includes.add(args.removeFirst());
                    }
                    break;
                case "-e":
                case "--exclude":
                    while (hasValue(args)) {
                        excludes.add(args.removeFirst());
                    }
                    break;
                case "-s":
                case "--source":
                    while (hasValue(args)) {
                        sources.add(new File(args.removeFirst()));
                    }
                    break;
                case "-t":
                case "--target":
                    if (hasValue(args)) {
                        target = new File(args.removeFirst());
                    }
                    if (hasValue(args)) {
                        System.out.println("Can have only one target");
                        System.exit(1);
                    }
                    break;
            }
        }
        if (sources.isEmpty()) sources.add(new File("."));
        if (scripts.isEmpty()) scripts.add(Paths.get("."));
        if (includes.isEmpty()) includes.add(defaultInclude());
        launchApi4J(target, sources, scripts, includes, excludes);
    }
    
    private static boolean hasValue(Deque<String> deque) {
        return !deque.isEmpty() && !isOption(deque.peekFirst());
    }
    
    private static boolean isOption(String arg) {
        return arg.startsWith("-");
    }

    private static void printHelpMessage() {
        PrintStream o = System.out;
        o.println("Usage:");
        o.println("    [TARGET [SOURCE+]] [OPTION ARGUMENTS]+");
        o.println("Options:");
        LinkedHashMap<String, String> options = new LinkedHashMap<String, String>(){{
            put("-h -? --help","Shows this help message");
            put("-x --script","Adds following paths as script folders or files");
            put("-i --include","Adds include patterns for script folders (default: '**.api.{xml,groovy}')");
            put("-e --exclude","Adds exclude patterns for script folders");
            put("-s --source","Adds following paths as source folders");
            put("-t --target","Sets the following path as target folder");
        }};
        for (Map.Entry<String, String> e: options.entrySet()) {
            o.printf("% 16s    %s%n", e.getKey(), e.getValue());
        }
        o.println("By default, the script, source, and target paths are set to the current directory");
        o.println("Example:");
        o.println("    api4j target/generated-sources/api4j src/main/java -x src/main/api4j");
    }

    public static void launchApi4J(File target, List<File> sources, List<Path> scripts, List<String> includes, List<String> excludes) throws Exception {
        Api4JConfiguration api4j = createConfiguration(target, scripts);
        for (File f: sources) {
            api4j.addSourceFolder(f);
        }
        Api4JEngine engine = api4j.createEngine();
        for (Path p: scripts) {
            if (Files.isDirectory(p)) {
                engine.runFileTree(p, includes, excludes);
            } else {
                engine.runScript(p, p);
            }
        }
    }
    
    public static String defaultInclude() {
        return "**.api.{xml,groovy}";
    }
    
    public static Api4JConfiguration createConfiguration(File target, List<Path> scripts) {
        List<ResourceResolver> resolvers = new ArrayList<>(scripts.size()+1);
        resolvers.add(new ClassLoaderResourceResolver().lookupAll());
        scripts.forEach(p -> {
            resolvers.add(new FileResolver(p, p).lookupAll());
        });
        ResourceResolver res = new CompositeResolver(resolvers);
        Api4JConfiguration cfg = new Api4JConfiguration(target, res);
        return cfg;
    }
}
