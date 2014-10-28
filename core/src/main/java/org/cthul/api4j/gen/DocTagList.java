package org.cthul.api4j.gen;

import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.impl.DefaultDocletTag;

/**
 *
 */
public class DocTagList extends AutoParsingList<DocletTag> {

    private final boolean removeReturnTags;

    public DocTagList() {
        this.removeReturnTags = false;
    }

    public DocTagList(boolean removeReturnTags) {
        this.removeReturnTags = removeReturnTags;
    }

    @Override
    protected void plainAdd(int index, DocletTag element) {
        if (removeReturnTags && "return".equals(element.getName())) {
            return;
        }
        super.plainAdd(index, element);
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
}
