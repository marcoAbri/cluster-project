package clustering;

import elasticsearch.ElasticFacade;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.util.*;


public class Clustering_in_Action {


    public static double setSoglia(int numeroTest) throws JSONException, IOException, ParseException, java.text.ParseException {
        double eval = 0;
        JSONArray jsonArticle = ElasticFacade.queryNewsLucene();
        double minSogliaAllCluster ;
        double countNumElementsOfATopic;
        double minCosSimFind = 100000;
        double maxCosSimFind = 0;
        double mediaCos = 0;
        boolean t;
        String ncl1 = "";
        HashMap<String, Double> map1 = new HashMap<>();
        for (int h = 0; h < numeroTest - 1; h++) { //itero sulle news
            if (h > 500) {
                countNumElementsOfATopic = 0;
                t = true;
                JSONObject singleItem = null;
                while (t && h < numeroTest - 1) {//salto le news che presentano keywords vuote
                    JSONObject single = (JSONObject) jsonArticle.get(h);
                    singleItem = (JSONObject) single.get("_source");
                    String relevances = (String) singleItem.get("relevances_Alchemy");
                    String keywords = (String) singleItem.get("keywords_Alchemy");
                    String metaKeywords = (String) singleItem.get("keywords_Metadata");
                    ncl1 = (String) singleItem.get("ncl");
                    if (!keywords.isEmpty()) {
                        String metaKey[] = metaKeywords.split(",");
                        String[] rel = relevances.split(" ");
                        String[] key = keywords.split("_-_");
                        rel = Cosine_Similarity.setRelevance(metaKey, key, rel);
                        for (int c = 0; c < rel.length - 1; c++) {
                            map1.put(key[c], Double.valueOf(rel[c]));
                        }
                        t = false;
                    } else
                        h++;
                }
                for (int j = 0; j < numeroTest - 1; j++) {
                    if (h > j) {
                        JSONObject single2 = (JSONObject) jsonArticle.get(j);
                        JSONObject singleItem2 = (JSONObject) single2.get("_source");
                        String ncl2 = (String) singleItem2.get("ncl");
                        if (ncl1.equals(ncl2)) {
                            HashMap<String, Double> map3 = new HashMap<>();
                            String relevances3 = (String) singleItem2.get("relevances_Alchemy");
                            String keywords3 = (String) singleItem2.get("keywords_Alchemy");
                            String metaKeyworfds = (String) singleItem2.get("keywords_Metadata");
                            String metaKey2[] = metaKeyworfds.split(",");
                            String[] rel2 = relevances3.split(" ");
                            String[] key2 = keywords3.split("_-_");
                            if (!keywords3.isEmpty()) {
                                rel2 = Cosine_Similarity.setRelevance(metaKey2, key2, rel2);
                                for (int m = 0; m < rel2.length - 1; m++) {
                                    map3.put(key2[m], Double.valueOf(rel2[m]));
                                }
                                double cos ;
                                if (singleItem.get("date") != null && singleItem2.get("date") != null) {
                                    cos = Cosine_Similarity.cosine_similarityWithDate(map3, map1, (String) singleItem.get("date"), (String) singleItem2.get("date"));
                                } else
                                    cos = Cosine_Similarity.cosine_similarity(map3, map1);
                                if (cos != 0.0) {
                                    countNumElementsOfATopic++;
                                    eval = eval + cos;
                                }
                            }
                        }
                    }
                }
                if (countNumElementsOfATopic != 0) {
                    mediaCos = mediaCos + (eval / (countNumElementsOfATopic));
                    minSogliaAllCluster = (eval / (countNumElementsOfATopic));
                    if (minSogliaAllCluster < minCosSimFind)
                        minCosSimFind = minSogliaAllCluster;
                    if (minSogliaAllCluster > maxCosSimFind)
                        maxCosSimFind = minSogliaAllCluster;
                    eval = 0;
                }
            }
        }
        return ((minCosSimFind * 0.7) + maxCosSimFind * 0.3) / 2;
    }


    public static HashMap<String, LinkedList<JSONObject>> clusteringTest(double sogliaCos) throws JSONException, IOException, ParseException, java.text.ParseException {

        JSONArray jsonArticle = ElasticFacade.queryNewsLucene();
        HashMap<String, LinkedList<JSONObject>> clustering = new HashMap<>();
        int num = 0;
        for (int i = 0; i < jsonArticle.size() - 1; i++) {-
            if (i < 500) {
                HashMap<String, Double> map1 = new HashMap<>();
                JSONObject single = (JSONObject) jsonArticle.get(i);
                JSONObject singleItem = (JSONObject) single.get("_source");
                String relevances = (String) singleItem.get("relevances_Alchemy");
                String keywords = (String) singleItem.get("keywords_Alchemy");
                String metaKeywords = (String) singleItem.get("keywords_Metadata");
                if (!keywords.isEmpty()) {
                    String metaKey[] = metaKeywords.split(",");
                    String[] rel = relevances.split(" ");
                    String[] key = keywords.split("_-_");
                    rel = Cosine_Similarity.setRelevance(metaKey, key, rel);
                    if (clustering.size() == 0) {
                        LinkedList<JSONObject> l = new LinkedList<>();
                        l.add(singleItem);
                        clustering.put((String) singleItem.get("ncl"), l);
                    } else {
                        for (int c = 0; c < rel.length - 1; c++) {
                            map1.put(key[c], Double.valueOf(rel[c]));
                        }
                        boolean t = false;
                        Iterator it = clustering.entrySet().iterator();
                        String numCluster = "";
                        double maxSoglia = 0.0;
                        while (it.hasNext()) {// && !t) {
                            Map.Entry pair = (Map.Entry) it.next();
                            LinkedList<JSONObject> k = (LinkedList<JSONObject>) pair.getValue();
                            for (JSONObject jso : k) {
                                HashMap<String, Double> map3 = new HashMap<>();
                                String relevances3 = (String) jso.get("relevances_Alchemy");
                                String keywords3 = (String) jso.get("keywords_Alchemy");
                                String metaKeyworfds = (String) jso.get("keywords_Metadata");
                                String metaKey2[] = metaKeyworfds.split(",");
                                String[] rel2 = relevances3.split(" ");
                                String[] key2 = keywords3.split("_-_");
                                //System.out.println("vuoto 2");
                                rel2 = Cosine_Similarity.setRelevance(metaKey2, key2, rel2);
                                for (int m = 0; m < rel2.length - 1; m++) {
                                    map3.put(key2[m], Double.valueOf(rel2[m]));
                                }
                                double cos;
                                if (jso.get("date") != null && singleItem.get("date") != null)
                                    cos = Cosine_Similarity.cosine_similarityWithDate(map3, map1, (String) jso.get("date"), (String) singleItem.get("date"));
                                else
                                    cos = Cosine_Similarity.cosine_similarity(map3, map1);
                                if (cos >= sogliaCos) {
                                    if (cos >= maxSoglia && cos != 1) {
                                        maxSoglia = cos;
                                        t = true;
                                        numCluster = (String) pair.getKey();
                                    }
                                }
                            }
                        }
                        if (t) {
                            clustering.get(numCluster).add(singleItem);
                        } else {
                            LinkedList<JSONObject> l1 = new LinkedList<>();
                            l1.add(singleItem);
                            if (clustering.get(singleItem.get("ncl")) != null) {
                                clustering.put(singleItem.get("ncl") + "tag" + num, l1);
                                num++;
                            } else {
                                clustering.put((String) singleItem.get("ncl"), l1);
                            }
                        }
                    }
                }
            }
        }
        return clustering;
    }

    public static void indexLucene(HashMap<String, LinkedList<JSONObject>> cluster) throws IOException {
        for (Object o : cluster.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            LinkedList<JSONObject> op = (LinkedList<JSONObject>) pair.getValue();
            for (JSONObject jk : op) {
                elasticsearch.ElasticFacade.indexNewsClusterMyCosSim(jk, (String) pair.getKey());

            }
        }
    }
}
