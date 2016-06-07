package clustering;

import com.google.common.collect.Sets;
import lucene.IndexLuceneToEleastic;
import org.json.simple.JSONObject;

import java.text.ParseException;
import java.util.*;

public class Cosine_Similarity {

    public static double cosine_similarityWithDate(HashMap<String, Double> v1, HashMap<String, Double> v2, String data1, String data2) throws ParseException {
        boolean sameDate = false;
        boolean precDate = false;
        Date date1, date2;
        date1 = IndexLuceneToEleastic.getDateFormat1(data1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);

        date2 = IndexLuceneToEleastic.getDateFormat1(data2);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        if (calendar != null && calendar2 != null) {

            int equal = calendar.compareTo(calendar2);
            if (equal == 0)
                sameDate = true;
            if (equal > 0) {
                if (calendar.get(Calendar.YEAR) - calendar2.get(Calendar.YEAR) == 0 && calendar.get(Calendar.MONTH) - calendar2.get(Calendar.MONTH) == 0 && calendar.get(Calendar.DAY_OF_MONTH) - calendar2.get(Calendar.DAY_OF_MONTH) < 4)
                    precDate = true;
            }
            if (equal < 0) {
                if (calendar2.get(Calendar.YEAR) - calendar.get(Calendar.YEAR) == 0 && calendar2.get(Calendar.MONTH) - calendar.get(Calendar.MONTH) == 0 && calendar2.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH) > 4)
                    precDate = true;
            }
        }
        Set<String> both = Sets.newHashSet(v1.keySet());
        both.retainAll(v2.keySet());
        double sclar = 0, norm1 = 0, norm2 = 0;
        for (String k : both) {
            if (v2.get(k) != null)
                sclar += v1.get(k) * v2.get(k);
        }
        for (String k : v1.keySet())
            norm1 += v1.get(k) * v1.get(k);
        for (String k : v2.keySet())
            norm2 += v2.get(k) * v2.get(k);
        if (sameDate) {
            if (1 - sclar / Math.sqrt(norm1 * norm2) > 0.003)
                return (sclar / Math.sqrt(norm1 * norm2)) + 0.003;
        }
        if (precDate) {
            if (1 - (sclar / Math.sqrt(norm1 * norm2)) > 0.001)
                return (sclar / Math.sqrt(norm1 * norm2)) + 0.001;
        }
        return (sclar / Math.sqrt(norm1 * norm2));
    }

    public static double cosine_similarity(HashMap<String, Double> v1, HashMap<String, Double> v2) {
        Set<String> both = Sets.newHashSet(v1.keySet());
        both.retainAll(v2.keySet());
        double sclar = 0, norm1 = 0, norm2 = 0;
        for (String k : both) {
            if (v2.get(k) != null)
                sclar += v1.get(k) * v2.get(k);
        }
        for (String k : v1.keySet())
            norm1 += v1.get(k) * v1.get(k);
        for (String k : v2.keySet())
            norm2 += v2.get(k) * v2.get(k);
        return (sclar / Math.sqrt(norm1 * norm2));


    }

    public static String[] setRelevance(String[] metakeywords, String[] alchemyKeywords, String[] alchemyRelevance) {
        for (String string : metakeywords) {
            int index = 0;
            for (String string2 : alchemyKeywords) {
                if (string.contains(string2)) {
                    alchemyRelevance[index] = "0.99999999";
                }
                index++;
            }
        }
        return alchemyRelevance;
    }
}
