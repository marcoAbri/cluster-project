package alchemy;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.UnknownHostException;


public class ProcessingJsonAlchemy {

    //prende un json object contenente le keywords e restituisce una lista di keywords
    public static String getKeywords(String jsonSting) {
        JSONParser parser = new JSONParser();
        String keyword = "";
        try {
            Object obj = parser.parse(jsonSting);
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray keywords = (JSONArray) jsonObject.get("keywords");
            if (keywords != null)
                for (int i = 0; i < keywords.size(); i++) {
                    JSONObject single = (JSONObject) keywords.get(i);
                    String kw = (String) single.get("text");
                    keyword += kw + "_-_";
                }
            else
                System.out.println("KEYWORDS NULL");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return keyword;
    }

    public static String getRelevances(String jsonSting) {
        JSONParser parser = new JSONParser();
        String relevances = "";
        try {
            Object obj = parser.parse(jsonSting);
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray keywords = (JSONArray) jsonObject.get("keywords");
            if (keywords != null)
                for (int i = 0; i < keywords.size(); i++) {
                    JSONObject single = (JSONObject) keywords.get(i);
                    String relevance = (String) single.get("relevance");
                    relevances += relevance + " ";
                }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return relevances;
    }

    public static String getDate(String jsonSting) {
        JSONParser parser = new JSONParser();
        String date = "";
        try {
            Object obj = parser.parse(jsonSting);
            JSONObject jsonObject = (JSONObject) obj;
            JSONObject dateArray = (JSONObject) jsonObject.get("publicationDate");
            if (dateArray != null)
                date = (String) dateArray.get("date");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getArticleDate(String jsonSting) {
        JSONParser parser = new JSONParser();
        String dataArticle = "";
        try {
            Object obj = parser.parse(jsonSting);
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray dateArray = (JSONArray) jsonObject.get("dates");
            if (dateArray != null && dateArray.size() != 0) {
                JSONObject single = (JSONObject) dateArray.get(0);
                dataArticle = (String) single.get("date");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dataArticle;
    }

    public static String getArticle(String jsonSting) {
        JSONParser parser = new JSONParser();
        String article = "";
        try {
            Object obj = parser.parse(jsonSting);
            JSONObject jsonObject = (JSONObject) obj;
            article = (String) jsonObject.get("text");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return article;
    }

    public static String getSentiment(String jsonString) {
        JSONParser parser = new JSONParser();
        String sentiment = "";
        String sentimentVal;
        try {
            Object obj = parser.parse(jsonString);
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray keywords = (JSONArray) jsonObject.get("keywords");
            if (keywords != null)
                for (int i = 0; i < keywords.size(); i++) {
                    JSONObject single = (JSONObject) keywords.get(i);
                    JSONObject sentimentObject = (JSONObject) single.get("sentiment");
                    sentimentVal = (String) sentimentObject.get("score");
                    if (sentimentVal == null)
                        sentimentVal = "0.0";
                    sentiment += sentimentVal + " ";
                }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sentiment;
    }


    public static Double[] getGeo(String entityJson) throws UnknownHostException {
        try {
            org.json.JSONObject entitiesObj = new org.json.JSONObject(entityJson);
            org.json.JSONArray entities = entitiesObj.getJSONArray("entities");
            for (int i = 0; i < entities.length(); i++) {
                Boolean location = false;
                Double lat = 0.0;
                Double lon = 0.0;
                Double geoCoord[] = new Double[2];
                org.json.JSONObject entity = entities.getJSONObject(i);
                try {
                    org.json.JSONObject disambiguated = entity.getJSONObject("disambiguated");
                    String geo = disambiguated.getString("geo");
                    String[] latLong = geo.split("\\s+");
                    lat = Double.parseDouble(latLong[0]);
                    lon = Double.parseDouble(latLong[1]);
                    location = true;
                } catch (org.json.JSONException er) {
                }
                if (location) {
                    geoCoord[0] = lat;
                    geoCoord[1] = lon;
                    return geoCoord;
                } else {
                }
            }
        } catch (org.json.JSONException e) {

        }
        return null;
    }
}
