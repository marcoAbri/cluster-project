package alchemy;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;

public class RESTCallString {

    static final String API_KEY = "#####################################";
    static final String OUTPUT = "json";
    static final String USER_AGENT = "Mozilla/5.0";


    public static String getArticleDate(String content) throws IOException {
        final String TEXT = URLEncoder.encode(content, "UTF-8");
        String url = "http://access.alchemyapi.com/calls/html/HTMLExtractDates";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        String urlParameters = "apikey=" + API_KEY +"&html=" + TEXT  +"&outputMode=" + OUTPUT;
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine + "\n");
        }
        in.close();
        return new String(response.toString());

    }

    public static String getKeywordsRankedSentimentLongText(String content) throws IOException {
        final String TEXT = URLEncoder.encode(content, "UTF-8");
        String url = "http://access.alchemyapi.com/calls/text/TextGetRankedKeywords";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Accept-encoding", "gzip");
        String urlParameters = "apikey=" + API_KEY +"&text=" + TEXT  +"&sentiment=1" +"&outputMode=" + OUTPUT;
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new GZIPInputStream(con.getInputStream())));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine + "\n");
            }
            in.close();
            return new String(response.toString());
        }catch(ZipException e){
          return getKeywordsRankedSentimentShortText(content);
        }
    }

    public static String getPublicationDate(String content) throws IOException {
        final String TEXT = URLEncoder.encode(content, "UTF-8");
        String url = "http://access.alchemyapi.com/calls/html/HTMLGetPubDate";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        String urlParameters = "apikey=" + API_KEY +"&html=" + TEXT  +"&outputMode=" + OUTPUT;
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine + "\n");
            }
            in.close();
            return new String(response.toString());

    }

    public static String getKeywordsRankedSentimentShortText(String content) throws IOException {
        try {
            final String TEXT = URLEncoder.encode(content, "UTF-8");
            String url = "http://access.alchemyapi.com/calls/text/TextGetRankedKeywords";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            String urlParameters = "apikey=" + API_KEY + "&text=" + TEXT + "&sentiment=1" + "&outputMode=" + OUTPUT;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine + "\n");
            }
            in.close();
            return new String(response.toString());
        }catch (Exception e){
            return "";
        }

    }

    public static String getGeoPoint(String content) throws IOException {
        final String TEXT = URLEncoder.encode(content, "UTF-8");
        String url = "http://access.alchemyapi.com/calls/text/TextGetRankedNamedEntities";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Accept-encoding", "gzip");
        String urlParameters = "apikey=" + API_KEY +"&text=" + TEXT  +"&outputMode=" + OUTPUT;
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new GZIPInputStream(con.getInputStream())));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine + "\n");
            }
            in.close();
            return new String(response.toString());
        }catch(ZipException e){
            return "";
        }
    }

    public static String getArticleLongText(String content) throws IOException {
        final String TEXT = URLEncoder.encode(content, "UTF-8");
        String url = "http://access.alchemyapi.com/calls/html/HTMLGetText";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Accept-encoding", "gzip");
        String urlParameters = "apikey=" + API_KEY +"&html=" + TEXT  +"&outputMode=" + OUTPUT;
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new GZIPInputStream(con.getInputStream())));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine + "\n");
            }
            in.close();
            return new String(response.toString());
        }catch(ZipException e){
            return getArticleShortText(content);
        }
    }

    public static String getArticleShortText(String content) throws IOException {
        try {
            final String TEXT = URLEncoder.encode(content, "UTF-8");
            String url = "http://access.alchemyapi.com/calls/html/HTMLGetText";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            String urlParameters = "apikey=" + API_KEY + "&html=" + TEXT + "&outputMode=" + OUTPUT;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine + "\n");
            }
            in.close();
            System.out.println("Short buon fine");
            System.out.println();
            return new String(response.toString());
        }catch (Exception e){
            System.out.println("Short inutile");
            System.out.println();
            return "";
        }

    }
}