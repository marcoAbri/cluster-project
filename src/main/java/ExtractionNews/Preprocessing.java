package ExtractionNews; /**
 * Created by marco on 25/12/15.
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Preprocessing {


    public static List<String> extractLinks(Reader reader) throws IOException {
        final ArrayList<String> list = new ArrayList<String>();

        ParserDelegator parserDelegator = new ParserDelegator();
        ParserCallback parserCallback = new ParserCallback() {
            public void handleStartTag(Tag tag, MutableAttributeSet attribute, int pos) {
                if (tag == Tag.A) {
                    String address = (String) attribute.getAttribute(Attribute.HREF);
                    list.add(address);
                }
            }


        };
        parserDelegator.parse(reader, parserCallback, false);
        return list;
    }

}
