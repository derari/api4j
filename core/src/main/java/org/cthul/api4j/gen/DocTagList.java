package org.cthul.api4j.gen;

import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.impl.DefaultDocletTag;
import java.util.List;

/**
 *
 */
public class DocTagList extends AutoParsingList<DocletTag> {
    
    public static DocTagList wrap(List<DocletTag> list) {
        if (list instanceof DocTagList) {
            return (DocTagList) list;
        }
        return wrap(list, DocTagList::new);
    }

    private final boolean removeReturnTags;

    public DocTagList() {
        this(false);
    }

    public DocTagList(boolean removeReturnTags) {
        this.removeReturnTags = removeReturnTags;
    }

    public DocTagList(boolean removeReturnTags, List<DocletTag> list) {
        super(list);
        this.removeReturnTags = removeReturnTags;
    }

    public DocTagList(List<DocletTag> list) {
        this(false, list);
    }

    @Override
    protected boolean plainAdd(int index, DocletTag element) {
        if (removeReturnTags && "return".equals(element.getName())) {
            return false;
        }
        return super.plainAdd(index, element);
    }
    
    @Override
    protected DocletTag parse(String s) {
        int iSpace = s.indexOf(' ');
        if (iSpace < 0) {
            return new DefaultDocletTag(s, "");
        } else {
            return new DefaultDocletTag(s.substring(0, iSpace), s.substring(iSpace+1));
        }
    }

    @Override
    public int indexOf(String s) {
        int i = super.indexOf(s);
        if (i >= 0) return i;
        i = 0;
        for (DocletTag t: this) {
            if (t.getName().equals(s)) {
                return i;
            }
            i++;
        }
        return -1;
    }
}
