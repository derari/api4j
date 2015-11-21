package org.cthul.api4j.gen;

import java.util.List;

/**
 * A list of strings that parses (and possibly splits) the input before accepts it.
 */
public abstract class AutoParsingStringList extends AutoParsingListBase<String> {

    public AutoParsingStringList() {
    }

    public AutoParsingStringList(List<String> list) {
        super(list);
    }
}
