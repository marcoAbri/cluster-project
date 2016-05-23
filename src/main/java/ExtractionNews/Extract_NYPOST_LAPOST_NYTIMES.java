package ExtractionNews;

import alchemy.ProcessingJsonAlchemy;
import alchemy.RESTCallString;
import elasticsearch.ElasticFacade;
import org.codehaus.jettison.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Extract_NYPOST_LAPOST_NYTIMES {
    public static void Analyzer(String link) throws IOException, JSONException, ParseException, InterruptedException {
        Document document;
        String title = "";
        document = Jsoup.connect(link).userAgent("Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:42.0) Gecko/20100101 Firefox/42.0").ignoreHttpErrors(true).timeout(10 * 1000).get();
        String html = document.toString();
        Document doc = Jsoup.parse(html);
        String description = "";
        String data = "";
        String article = "";
        String section = "";
        String keywordsHtml = "";
        String source = "";
        Elements descriptionElements;
        Elements dataElements;
        Elements articleElements;
        Elements titleElements;
        Elements keywordsElements;
        Elements sourceElements;
        Elements sectionElements;
        Date dateDate = null;
        boolean copy = false;
        //New York Times articolo !="" 20160110351204
        if (document.location().contains("nytimes")) {
            descriptionElements = doc.select("meta[name=description]");
            if (!descriptionElements.isEmpty())
                description = descriptionElements.first().attr("content");

            dataElements = doc.select("meta[name=ptime]");
            if (!dataElements.isEmpty()) {
                data = dataElements.first().attr("content");
                StringBuilder myData = new StringBuilder(data);
                for (int index = 8; index < data.length(); index++)
                    myData.setCharAt(index, ' ');
                data = myData.toString();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd      ", Locale.ENGLISH);

                try {
                    dateDate = formatter.parse(data);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }

            }

            articleElements = doc.select("p.story-body-text.story-content");
            if (articleElements.text().length() > 10)
                article = articleElements.text();

            titleElements = doc.select("meta[property=og:title]");
            if (!titleElements.isEmpty())
                title = titleElements.first().attr("content");

            sectionElements = doc.select("meta[property=article:section]");
            if (!sectionElements.isEmpty())
                section = sectionElements.first().attr("content");

            keywordsElements = doc.select("meta[name=keywords]");
            if (!keywordsElements.isEmpty())
                keywordsHtml = keywordsElements.first().attr("content");

            sourceElements = doc.select("meta[name=cre]");
            if (!sourceElements.isEmpty()) {
                source = sourceElements.first().attr("content");

            }
        }


        //New York POST titolo != "" 2016-01-13T10:15:02+00:00

        if (document.location().contains("nypost")) {
            articleElements = doc.select("div.entry-content");
            if (articleElements.text().length() > 10)
                article = articleElements.text();

            dataElements = doc.select("meta[property=article:published_time]");
            if (!dataElements.isEmpty()) {
                data = dataElements.first().attr("content");

                StringBuilder myData = new StringBuilder(data);
                for (int index = 10; index < data.length(); index++) {
                    if (index == 10)
                        myData.setCharAt(index, ' ');
                    if (index > 18)
                        myData.setCharAt(index, ' ');
                }

                data = myData.toString();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss     ", Locale.ENGLISH);
                try {
                    dateDate = formatter.parse(data);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            }

            titleElements = doc.select("meta[property=og:title]");
            if (!titleElements.isEmpty())
                title = titleElements.first().attr("content");

            sourceElements = doc.select("meta[property=og:site_name]");
            if (!sourceElements.isEmpty())
                source = sourceElements.first().attr("content");

            descriptionElements = doc.select("meta[property=og:description]");
            if (!descriptionElements.isEmpty())
                description = descriptionElements.first().attr("content");

            sectionElements = doc.select("meta[name=nypost-section]");
            if (!sectionElements.isEmpty()) {
                section = sectionElements.first().attr("content");
            }
            keywordsElements = doc.select("meta[name=sailthru.tags]");
            if (!keywordsElements.isEmpty())
                keywordsHtml = keywordsElements.first().attr("content");
        }

        //Los Angeles Post 2016-01-12T21:24:00-0800
        if (document.location().contains("latimes")) {
            descriptionElements = doc.select("meta[property=og:description]");
            if (!descriptionElements.isEmpty())
                description = descriptionElements.first().attr("content");

            dataElements = doc.select("meta[itemprop=datePublished]");
            if (!dataElements.isEmpty()) {
                data = dataElements.first().attr("content");
                StringBuilder myData = new StringBuilder(data);
                for (int index = 10; index < data.length(); index++) {
                    if (index == 10)
                        myData.setCharAt(index, ' ');
                    if (index > 18)
                        myData.setCharAt(index, ' ');
                }
                data = myData.toString();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss     ", Locale.ENGLISH);

                try {
                    dateDate = formatter.parse(data);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            }
            titleElements = doc.select("meta[name=fb_title]");
            if (!titleElements.isEmpty())
                title = titleElements.first().attr("content");

            sectionElements = doc.select("a.trb_ar_bc_a");
            if (!sectionElements.isEmpty())
                section = sectionElements.text();

            keywordsElements = doc.select("meta[name=keywords]");
            if (!keywordsElements.isEmpty())
                keywordsHtml = keywordsElements.first().attr("content");

            sourceElements = doc.select("meta[name=author]");
            if (!sourceElements.isEmpty())
                source = sourceElements.first().attr("content");

            articleElements = doc.select("div.trb_ar_page");
            if (articleElements.text().length() > 10)
                article = articleElements.text();
        }
        String jsonAlchemy, geoEntities = null;
        JSONArray jsonArticle = ElasticFacade.queryNews();

        if (jsonArticle != null) {
            for (int i = 0; i < jsonArticle.size() - 1; i++) {
                JSONObject single = (JSONObject) jsonArticle.get(i);
                JSONObject singleItem = (JSONObject) single.get("_source");
                if (title.equals(singleItem.get("title").toString()) && source.equals(singleItem.get("source").toString())) {
                    copy = true;
                    break;
                }
            }
        }

        if (!copy) {

            if (dateDate != null && article.length() > 10 && keywordsHtml != "" && section != "" && source != "") {
                jsonAlchemy = RESTCallString.getKeywordsRankedSentimentLongText(article);
                geoEntities = RESTCallString.getGeoPoint(article);
                if (jsonAlchemy != "") {
                    String keywords = ProcessingJsonAlchemy.getKeywords(jsonAlchemy);
                    String relevances = ProcessingJsonAlchemy.getRelevances(jsonAlchemy);
                    String sentiment = ProcessingJsonAlchemy.getSentiment(jsonAlchemy);
                    if (ProcessingJsonAlchemy.getGeo(geoEntities) != null) {
                        Double[] geo = ProcessingJsonAlchemy.getGeo(geoEntities);
                        Double lat = geo[0];
                        Double lon = geo[1];
                       // ElasticFacade.indexNewsInfo(dateDate, article, title, source, keywords, relevances, sentiment, lat, lon, keywordsHtml, description, section);

                    }
                }
            }
        }

    }
}
