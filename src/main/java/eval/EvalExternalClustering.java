package eval;

/**
 * Created by marco on 23/02/16.
 */

import org.json.simple.JSONObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class EvalExternalClustering {

    public static HashMap<String, LinkedList<JSONObject>> getP(HashMap<String, LinkedList<JSONObject>> Clist){
        HashMap<String, LinkedList<JSONObject>> Plist= new HashMap<>();
        for (Object o : Clist.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            LinkedList<JSONObject> op = (LinkedList<JSONObject>) pair.getValue();
            for (JSONObject jk : op) {
                if (!Plist.containsKey(jk.get("ncl")) ){
                    LinkedList<JSONObject> listJson= new LinkedList<>();
                    listJson.add(jk);
                    Plist.put((String) jk.get("ncl"),listJson);
                }else{
                    Plist.get( jk.get("ncl")).add(jk);
                }
            }
        }
        return Plist;
    }

    public static int getA(HashMap<String, LinkedList<JSONObject>> cList,HashMap<String, LinkedList<JSONObject>> pList){
        int a=0;
        for (Object o : cList.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            LinkedList<JSONObject> op = (LinkedList<JSONObject>) pair.getValue();
            for (JSONObject jsc : op) {
                for (Object o2 : pList.entrySet()) {
                    Map.Entry pair2 = (Map.Entry) o2;
                    LinkedList<JSONObject> op2 = (LinkedList<JSONObject>) pair2.getValue();
                    for (JSONObject jsp : op2) {
                        if(op2.contains(jsc)) {//magari metti il testo non il titolo
                            if(!(jsp.get("title").equals(jsc.get("title")))){
                                if(op.contains(jsp)){
                                    a++;
                                }
                            }
                        }
                    }
                }
            }
        }
        return a;
    }

    public static int getB(HashMap<String, LinkedList<JSONObject>> cList,HashMap<String, LinkedList<JSONObject>> pList){
        int b=0;
        for (Object o : cList.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            LinkedList<JSONObject> op = (LinkedList<JSONObject>) pair.getValue();
            for (JSONObject jsc : op) {
                for (Object o2 : pList.entrySet()) {
                    Map.Entry pair2 = (Map.Entry) o2;
                    LinkedList<JSONObject> op2 = (LinkedList<JSONObject>) pair2.getValue();
                    for (JSONObject jsp : op2) {
                        if(op2.contains(jsc)) {//magari metti il testo non il titolo
                            if(!(jsp.get("title").equals(jsc.get("title")))){
                                if(!op.contains(jsp)){
                                    b++;
                                }
                            }
                        }
                    }
                }
            }
        }
        return b;
    }

    public static int getC(HashMap<String, LinkedList<JSONObject>> cList,HashMap<String, LinkedList<JSONObject>> pList){
        int c=0;
        for (Object o : cList.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            LinkedList<JSONObject> op = (LinkedList<JSONObject>) pair.getValue();
            for (JSONObject jsc : op) {

                for (Object o2 : pList.entrySet()) {
                    Map.Entry pair2 = (Map.Entry) o2;
                    LinkedList<JSONObject> op2 = (LinkedList<JSONObject>) pair2.getValue();
                    for (JSONObject jsp : op2) {
                        if(!op2.contains(jsc)) {//magari metti il testo non il titolo
                                if(op.contains(jsp)){
                                    c++;
                            }
                        }
                    }
                }
            }
        }
        return c;
    }

    public static int getD(HashMap<String, LinkedList<JSONObject>> cList,HashMap<String, LinkedList<JSONObject>> pList){
        int d=0;
        for (Object o : cList.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            LinkedList<JSONObject> op = (LinkedList<JSONObject>) pair.getValue();
            for (JSONObject jsc : op) {

                for (Object o2 : pList.entrySet()) {
                    Map.Entry pair2 = (Map.Entry) o2;
                    LinkedList<JSONObject> op2 = (LinkedList<JSONObject>) pair2.getValue();
                    for (JSONObject jsp : op2) {
                        if(!op2.contains(jsc)) {//magari metti il testo non il titolo
                            if(!op.contains(jsp)){
                                d++;
                            }
                        }
                    }
                }
            }
        }
        return d;
    }

}