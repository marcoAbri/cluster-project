package lucene;

import kibana.GeoHash;
import elasticsearch.ElasticClient;
import elasticsearch.ElasticFacade;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class IndexLuceneToEleastic {
    public static void main(String[] args) throws IOException, JSONException, org.json.simple.parser.ParseException, ParseException {
        TransportClient client = ElasticClient.getInstance().getClient();
        for (int i = 0; i <= 10448; i++) {
            Date articleDate,publicationDate;
            JSONParser parser = new JSONParser();
            FileInputStream file = new FileInputStream("/home/marco/Scrivania/jsonCollector/" + i + ".json");
            JSONObject jsonObject = (JSONObject) parser.parse(new InputStreamReader(file));
            articleDate = getDateFormat1((String) jsonObject.get("article-date"));
            publicationDate=getDateFormat1((String) jsonObject.get("publication-date"));
            //media sentiment
            String sentiment = (String) jsonObject.get("sentimentList");
            double avgSentiment = ElasticFacade.avarageSentiment(sentiment);
            String hash;
            GeoPoint geoPoint = null;
            String geoString = (String) (jsonObject.get("geo.coordinates"));
            Double[] geoD = {null, null};
            if (!geoString.isEmpty()) {
                String geo[] = geoString.split("--");
                geoD[0] = Double.parseDouble(geo[0]);
                geoD[1] = Double.parseDouble(geo[1]);
                hash = GeoHash.geoHashStringWithCharacterPrecision(geoD[0], geoD[1], 12);
                geoPoint = new GeoPoint(hash);
            }
            ElasticFacade.indexNews(jsonObject, avgSentiment, client, articleDate, publicationDate, geoPoint);
            if (i % 100 == 0)
                System.out.println(i);
        }
    }

    public static Date getDateFormat1(String data) {
        if(data!=null) {
            if (data.length() != 0) {
                String shortData = data.substring(0, 8);
                Date dateDate;
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
                try {
                    dateDate = formatter.parse(shortData);
                } catch (java.text.ParseException e) {
                    return null;
                }
                return dateDate;
            }
        }
        return null;
    }
}
