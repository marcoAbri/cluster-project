package eval;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import clustering.*;

public class EvalClustering {
    public static void main(String[] args) throws IOException, ParseException, java.text.ParseException {
        start();

    }

    public static void start() throws IOException, ParseException, java.text.ParseException {
        System.out.println("------------------------------------");
        System.out.println(" . . .wait , I've been training the system . .");
        double sogliaCos =Clustering_in_Action.setSoglia(2500);
        System.out.println("the system is ready to test");
        //System.out.println("Threshold value is: "+sogliaCos);
        HashMap<String, LinkedList<JSONObject>> C = Clustering_in_Action.clusteringTest(sogliaCos);
        //Clustering_in_Action.indexLucene(C);
        HashMap<String, LinkedList<JSONObject>> P = EvalExternalClustering.getP(C);
        double a= EvalExternalClustering.getA(C,P);
        //System.out.println("calcolato a "+a);
        double b=EvalExternalClustering.getB(C,P);
        //System.out.println("calcolato b "+b);
        double c = EvalExternalClustering.getC(C,P);
        //System.out.println("calcolato c "+c);
        double d= EvalExternalClustering.getD(C,P);
       // System.out.println("calcolato d "+d);
        getRand(a,b,c,d);
        getJaccard(a,b,c,d);
        getFowlkes(a,b,c,d);
        getΓstatistics(a,b,c,d);
        System.out.println("------------------------------------");
    }


    public static double getRand( double a,double b, double c,double d){
        double rand= ((a+d)/(a+b+c+d));
        System.out.println("RAND: "+rand);
        return rand;
    }

    public static double getJaccard (double a,double b, double c,double d){
        double jaccard= (a/(a+b+c));
        System.out.println("Jaccard: "+jaccard);
        return jaccard;
    }

    public static double getFowlkes (double a,double b, double c,double d){
        double jaccard= Math.sqrt((a/(a+b))*(a/(a+c)));
        System.out.println("Fowlkes: "+jaccard);
        return jaccard;
    }

    public static double getΓstatistics (double a,double b, double c,double d){
        double M=a+b+c+d;
        double m1= a+b;
        double m2=a+c;
        double jaccard=(M*a-m1*m2)/(Math.sqrt(m1*m2*(M-m1)*(M-m2)));
        System.out.println("Γ statistics: "+jaccard);
        return jaccard;
    }
}