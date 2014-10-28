package org.cthul.api4j.api;

import com.thoughtworks.qdox.Searcher;
import com.thoughtworks.qdox.model.JavaClass;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternSearcher implements Searcher, Predicate<JavaClass> {
    
    private static final Pattern SYNTAX = Pattern.compile("[.]{1,2}|[/]{1,2}|[*]{1,2}");
    
    public static Pattern compile(String pattern) {
        StringBuilder sb = new StringBuilder();
        Matcher m = SYNTAX.matcher(pattern);
        int p = 0;
        while (m.find()) {
            sb.append(pattern, p, m.start());
            switch (m.group()) {
                case "..":
                case "//":
                case "**":
                    if (m.start() > 0) sb.append("\\.");
                    sb.append("(.*\\.)?");
                    break;
                case ".":
                    if (m.start() == 0) sb.append("(.*\\.)?");
                    else sb.append("\\.");
                    break;
                case "/":
                    if (m.start() > 0) sb.append("\\.");
                    break;
                case "*":
                    sb.append("[^.]*");
                    break;
                default:
                    throw new AssertionError(pattern + ": " + m.group());
            }
            p = m.end();
        }
        sb.append(pattern, p, pattern.length());
        return Pattern.compile(sb.toString());
    }
    
    public static PatternSearcher forPatterns(String... patterns) {
        Pattern[] regexes = new Pattern[patterns.length];
        for (int i = 0; i < regexes.length; i++) {
            regexes[i] = compile(patterns[i]);
        }
        return new PatternSearcher(regexes);
    }
    
    private final Pattern[] regexes;

    public PatternSearcher(Pattern... regexes) {
        this.regexes = regexes;
    }

    @Override
    public boolean test(JavaClass cls) {
        return eval(cls);
    }

    @Override
    public boolean eval(JavaClass cls) {
        for (Pattern p: regexes) {
            if (p.matcher(cls.getFullyQualifiedName()).matches()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return Arrays.toString(regexes);
    }
}
