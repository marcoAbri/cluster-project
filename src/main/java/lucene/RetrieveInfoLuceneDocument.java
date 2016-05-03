package lucene;

import elasticsearch.ElasticFacade;
import alchemy.ProcessingJsonAlchemy;
import alchemy.RESTCallString;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class RetrieveInfoLuceneDocument {


    public static void start(int i, IndexSearcher searcher) throws IOException, ParseException, java.text.ParseException {
        Document d = searcher.doc(i + 1);
        String title = getTitle(d);
        String description = getSnippet(d);
        String source = getSource(d);
        String pos = getPos(d);
        String topic = getTopic(d);
        String ncl = getNcl(d);
        String html = getHtml(d);
        String article = ProcessingJsonAlchemy.getArticle(RESTCallString.getArticleLongText(html));
        org.jsoup.nodes.Document doc = Jsoup.parse(html);
        String keywordsHtml;
        keywordsHtml = getKeywordsHTML(doc);
        String dataPublished= ProcessingJsonAlchemy.getDate(RESTCallString.getPublicationDate(html));
        String dataArticle= ProcessingJsonAlchemy.getArticleDate(RESTCallString.getArticleDate(html));
        String jsonAlchemy, geoEntities;
        jsonAlchemy = RESTCallString.getKeywordsRankedSentimentLongText(article);
        geoEntities = RESTCallString.getGeoPoint(article);
        String keywordsAlchemy = "";
        String relevances = "";
        String sentiment = "";
        Double[] geo;
        String geoString = "";
        if (!Objects.equals(jsonAlchemy, "")) {
            keywordsAlchemy = ProcessingJsonAlchemy.getKeywords(jsonAlchemy);
            relevances = ProcessingJsonAlchemy.getRelevances(jsonAlchemy);
            sentiment = ProcessingJsonAlchemy.getSentiment(jsonAlchemy);
            if (ProcessingJsonAlchemy.getGeo(geoEntities) != null) {
                geo = ProcessingJsonAlchemy.getGeo(geoEntities);
                geoString = String.valueOf(geo[0]) + "--" + String.valueOf(geo[1]);
            }
        }
        writeJSONfile(title, keywordsHtml, keywordsAlchemy, relevances, sentiment, description, article, dataPublished, source, geoString, pos, topic, ncl,dataArticle, i);
    }

    public static String getPos(Document d) {
        return d.get("pos");
    }

    public static String getTopic(Document d) {
        return d.get("topic");
    }

    public static String getTextNews(Document d) {
        return d.get("news-content");
    }

    public static String getTitle(Document d) {
        return d.get("title-indexed");
    }

    public static String getUrl(Document d) {
        return d.get("url");
    }

    public static String getSource(Document d) {
        return d.get("newspaper");
    }

    public static String getNcl(Document d) {
        return d.get("ncl");
    }

    public static String getHtml(Document d) {
        return d.get("raw-source");
    }

    public static String getSnippet(Document d) {
        return d.get("snippet-indexed");
    }


    public static String getKeywordsHTML(org.jsoup.nodes.Document doc) {
        String keywordsHtml = "";
        Elements keywordsElements = doc.select("meta[name=keywords]");
        if (!keywordsElements.isEmpty())
            keywordsHtml = keywordsElements.first().attr("content");
        return keywordsHtml;
    }

    public static void writeJSONfile(String title, String keywordsHTML, String keywordsAlchemy, String relevances, String sentiment, String snippet, String article, String dateDate, String source, String geo, String pos, String topic, String ncl,String dataArticle, int id) throws IOException {
        JSONObject obj = new JSONObject();
        double avgSentiment = ElasticFacade.avarageSentiment(sentiment);
        obj.put("title", title);
        obj.put("text", article);
        obj.put("keywords_Alchemy", keywordsAlchemy);
        obj.put("average sentiment", avgSentiment);
        obj.put("relevances_Alchemy", relevances);
        obj.put("sentimentList", sentiment);
        obj.put("source", source);
        obj.put("keywords_Metadata", keywordsHTML);
        obj.put("description", snippet);
        obj.put("article-date", dataArticle);
        obj.put("publication-date",dateDate);
        obj.put("geo.coordinates", geo);
        obj.put("pos", pos);
        obj.put("topic", topic);
        obj.put("ncl", ncl);
        FileWriter file = new FileWriter("/home/marco/Scrivania/jsonCollector/" + id + ".json");
        file.write(obj.toJSONString());
        file.flush();
        file.close();
    }
}