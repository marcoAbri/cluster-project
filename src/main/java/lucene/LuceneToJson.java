package lucene;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;


public class LuceneToJson {

    public static void main(String[] args) throws IOException, ParseException, NullPointerException, java.text.ParseException {
/*
        Query q = new MatchAllDocsQuery();
        int hitsPerPage = 10;
        File indexdir = new File("/home/marco/Scrivania/index/index"); // location of my index
        IndexReader reader = IndexReader.open(FSDirectory.open(indexdir));
        IndexSearcher searcher = new IndexSearcher(reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
        searcher.search(q, collector);
        int i;
        System.out.println( collector.getTotalHits());
        for (i =10448; i < collector.getTotalHits(); ++i) {
            RetrieveInfoLuceneDocument.start(i, searcher);
            if (i % 100 == 0)
                System.out.println(i);
        }
        reader.close();
*/
    }
}