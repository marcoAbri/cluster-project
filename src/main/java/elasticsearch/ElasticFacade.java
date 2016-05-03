package elasticsearch;
import org.elasticsearch.common.geo.GeoPoint;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.elasticsearch.client.transport.TransportClient;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.LinkedList;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class ElasticFacade {

    static final String LOGIN = "";
    static final String URL = "http://localhost:9200/sii-news-cluster/sii-cluster-project/_search?q=title:*?&size=100000";
    //static final String NOCOPY = "http://localhost:9200/sii-project/unique-info-news/_search?q=title:*?&size=100000";
    //static final String QUERY = "http://localhost:9200/sii-project/cluster/_search?q=clusterID:*&size=1000";
    static final String URLucene = "http://localhost:9200/sistemi-intelligenti-internet/articoli-cluster/_search?q=title:*?&size=3000";

    /*
    public static JSONArray queryCluster() throws IOException, org.json.simple.parser.ParseException {
        URL url = new URL(QUERY);
        String encoded = new sun.misc.BASE64Encoder().encode(LOGIN.getBytes());
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("Authorization", "Basic " + encoded);
        String response = org.apache.commons.io.IOUtils.toString(new BufferedReader(new InputStreamReader(conn.getInputStream())));
        JSONParser jsonParser2 = new JSONParser();
        return (JSONArray) ((org.json.simple.JSONObject) ((org.json.simple.JSONObject) jsonParser2.parse(response)).get("hits")).get("hits");

    }
    */

    //Query: ritorna tutte le news indicizzate in sii-project/info-news
    public static JSONArray queryNews() throws IOException, org.json.simple.parser.ParseException {
        URL url = new URL(URL);
        String encoded = new sun.misc.BASE64Encoder().encode(LOGIN.getBytes());
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("Authorization", "Basic " + encoded);
        String response = org.apache.commons.io.IOUtils.toString(new BufferedReader(new InputStreamReader(conn.getInputStream())));
        JSONParser jsonParser2 = new JSONParser();
        return (JSONArray) ((org.json.simple.JSONObject) ((org.json.simple.JSONObject) jsonParser2.parse(response)).get("hits")).get("hits");
    }

    //Query: ritorna tutte le news indicizzate in sii-project/lucene-news
    public static JSONArray queryNewsLucene() throws IOException, org.json.simple.parser.ParseException {
        URL url = new URL(URLucene);
        String encoded = new sun.misc.BASE64Encoder().encode(LOGIN.getBytes());
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("Authorization", "Basic " + encoded);
        String response = org.apache.commons.io.IOUtils.toString(new BufferedReader(new InputStreamReader(conn.getInputStream())));
        JSONParser jsonParser2 = new JSONParser();
        return (JSONArray) ((org.json.simple.JSONObject) ((org.json.simple.JSONObject) jsonParser2.parse(response)).get("hits")).get("hits");
    }

/*
    public static JSONArray queryNewsUnique() throws IOException, org.json.simple.parser.ParseException {
        URL url = new URL(NOCOPY);
        String encoded = new sun.misc.BASE64Encoder().encode(LOGIN.getBytes());
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("Authorization", "Basic " + encoded);
        String response = org.apache.commons.io.IOUtils.toString(new BufferedReader(new InputStreamReader(conn.getInputStream())));
        JSONParser jsonParser2 = new JSONParser();
        return (JSONArray) ((org.json.simple.JSONObject) ((org.json.simple.JSONObject) jsonParser2.parse(response)).get("hits")).get("hits");
    }

    //crea nuovo indice
    public static void indexNewsInfo(Date dateDate, String text, String titolo, String fonte, String keywords, String relevances, String sentiment, Double lat, Double lon, String keywordsMeta, String descrizione, String section) throws IOException, JSONException {
        double avgSentiment = avarageSentiment(sentiment);
        TransportClient client = ElasticClient.getInstance().getClient();
        try {
            client.prepareIndex("news", "inAction")
                    .setSource(jsonBuilder().startObject()
                            .field("title", titolo)
                            .field("text", text)
                            .field("keywords_Alchemy", keywords)
                            .field("relevances_Alchemy", relevances)
                            .field("average sentiment", avgSentiment)
                            .field("sentimentList", sentiment)
                            .field("source", fonte)
                            .field("date", dateDate)
                            //.field("geo.coordinates", new GeoPoint(lon, lat))
                            .field("keywords_Metadata", keywordsMeta)
                            .field("description", descrizione)
                            .field("section", section)
                            .endObject()
                    ).execute().actionGet();
            System.out.println("");
            System.out.println("");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("");
        }
        //}
    }
*/

    public static void indexNews(JSONObject jsonObject,double avgSentiment,TransportClient client,Date articleDate,Date publicationDate,GeoPoint geoPoint) throws IOException {
        jsonObject.put("sentiment", avgSentiment);
            client.prepareIndex("sistemi-intelligenti-internet", "articoli-cluster").setSource(
                    jsonBuilder().startObject()
                            .field("title", jsonObject.get("title"))
                            .field("text", jsonObject.get("text"))
                            .field("keywords_Alchemy", jsonObject.get("keywords_Alchemy"))
                            .field("relevances_Alchemy", jsonObject.get("relevances_Alchemy"))
                            .field("average sentiment", avgSentiment)
                            .field("sentimentList", jsonObject.get("sentimentList"))
                            .field("source", jsonObject.get("source"))
                            .field("article-date", articleDate)
                            .field("publication-date", publicationDate)
                            .field("geo_point", geoPoint)
                            .field("keywords_Metadata", jsonObject.get("keywords_Metadata"))
                            .field("description", jsonObject.get("description"))
                            .field("ncl", jsonObject.get("ncl"))
                            .endObject())
                    .execute().actionGet();
    }

    public static void indexNewsClusterMyCosSim(JSONObject json, String topic) throws IOException, JSONException {
        TransportClient client = ElasticClient.getInstance().getClient();
        try {
            json.put("topic", topic);
            client.prepareIndex("my-cluster", "topic")
            .setSource(json).execute().actionGet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/*
    public static void indexNoCouple(JSONObject json) throws IOException, JSONException {
        TransportClient client = ElasticClient.getInstance().getClient();
        try {
            client.prepareIndex("sii-project", "unique-info-news")
                    .setSource(json).execute().actionGet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

    public static double avarageSentiment(String sentiment) {
            String[] sentimentList = sentiment.split(" ");
            double sentimentDouble = 0.0;
            double avg = 0;
            for (String s : sentimentList) {
                if (!s.isEmpty())
                    sentimentDouble = Double.parseDouble(s);
                avg += sentimentDouble;
            }
            avg = avg / sentimentList.length;
            return avg;
    }
/*
    //prendi un intero indice e reindicizza tutto eliminando gli eventuali duplicati
    public static void deleteCoupleArticle() throws IOException, ParseException {
        LinkedList<String> list = new LinkedList<String>();
        JSONArray jsonArticle = queryNews();
        //carico l'indice attuale
        JSONArray jsonArticleNoCouple = queryNewsUnique();
        for (int i = 0; i < jsonArticleNoCouple.size() - 1; i++) {
            JSONObject single = (JSONObject) jsonArticleNoCouple.get(i);
            JSONObject singleItem = (JSONObject) single.get("_source");
            list.add ((String) singleItem.get("title"));
        }
        if (jsonArticle != null) {
            for (int i = 0; i < jsonArticle.size() - 1; i++) {
                JSONObject single = (JSONObject) jsonArticle.get(i);
                JSONObject singleItem = (JSONObject) single.get("_source");
                String check = (String) singleItem.get("title");
                if (!list.contains(check)) {
                    list.add(check);
                    ElasticFacade.indexNoCouple(singleItem);
                }

            }
        }

    }
    */

}
