import java.util.ArrayList;
import java.util.List;

public class App {  
    static Graf<String, Spoj<String>> graf = new Graf<>();   // hlavní graf
    public static void main(String[] args) { 
        PridatJednotkyVahy();        // ng, mg, g, dkg, kg, t
        PridatJednotkyVzdalenosti(); // pm, nm, mm, dm, cm, m, km
        PridatJednotkyCasu();        // ps, ns, ms, s                     všechny prvky jednotek jsou přidané do stejného grafu        

        graf.VypsatVsechnyPropojeni();                 // vypsání všech propojení všech prvků
        System.out.printf("----------------------------\n");


        double prevod1 = PrevodJednotek(6, "cm", "km");      // převod 6 cm na km
        double prevod2 = PrevodJednotek(5, "km", "cm");
        double prevod3 = PrevodJednotek(4, "m", "cm");
        double prevod4 = PrevodJednotek(3, "dm", "m");
        double prevod5 = PrevodJednotek(2, "mm", "nm");

        double prevod6 = PrevodJednotek(2, "ps", "ns");      // převod 2 ps na ns
        double prevod7 = PrevodJednotek(3, "s", "ms");
        double prevod8 = PrevodJednotek(4, "s", "ns");

        double prevod9 = PrevodJednotek(5, "g", "ng");      // převod 5 g na ng
        double prevod10 = PrevodJednotek(6, "t", "kg");
        double prevod11 = PrevodJednotek(7, "dkg", "g");


        System.out.printf("Převody délky: " + "\n" +
                          "  1. Převod: " + prevod1 + " km" +"\n" +
                          "  2. Převod: " + prevod2 + " cm" +"\n" +
                          "  3. Převod: " + prevod3 + " cm" +"\n" +
                          "  4. Převod: " + prevod4 + " m" +"\n" +
                          "  5. Převod: " + prevod5 + " nm" +"\n" +
                          "Převody času: " +"\n" +
                          "  6. Převod: " + prevod6 + " ns" +"\n" +
                          "  7. Převod: " + prevod7 + " ms" +"\n" +
                          "  8. Převod: " + prevod8 + " ns" +"\n" +
                          "Převody váhy: " + "\n" +
                          "  9. Převod: " + prevod9 + " ng" +"\n" +
                          "  10. Převod: " + prevod10 + " kg" +"\n" +
                          "  11. Převod: " + prevod11 + " g" +"\n" 
                         );

    }
    /** 
    Vypočítá převod ze zadané jednotky na druhou zadanou jednotku O(n)
    @return vrátí výsledek převodu
    */
    public static double PrevodJednotek(float hodnota, String puvodniJednotka, String jednotkaNaPrevedeni) {
        return hodnota * PrevodJednotekRecursive(1d, puvodniJednotka, jednotkaNaPrevedeni, new ArrayList<String>());
    }
    /** 
    Rekursivní procházení prvkami v grafu a výpočet převodu 
    @return vrátí poměr mezi první danou jednotkou a druhou
    */
    public static double PrevodJednotekRecursive(Double hodnota, String aktualniJednotka, String jednotkaNaPrevedeni, List<String> navstivene) {
        if (jednotkaNaPrevedeni.equals(aktualniJednotka)) {
            return hodnota;
        }
        List<String> spoje = graf.GetPropojeniPrvku(aktualniJednotka);
        List<String> navstivenePrvky = navstivene;
        for (String spoj : spoje) {
            if (navstivenePrvky.contains(spoj))
                continue;
            else
                navstivenePrvky.add(spoj);

            Spoj<String> spojString = graf.GetSpoj(aktualniJednotka, spoj);
            double nasobek = hodnota;
            if (spojString.getSmer().equals(spoj)) 
                nasobek *= spojString.getVaha();
            else 
                nasobek *= (1d / spojString.getVaha());
            double doubl = PrevodJednotekRecursive(nasobek, spoj, jednotkaNaPrevedeni, navstivenePrvky);
            if (doubl != 0d) 
                return doubl;
        }
        return 0d;
    }
    public static void PridatJednotkyVahy() {
        graf.VLozitPrvek("ng");     
        graf.VLozitPrvek("mg");     
        graf.VLozitPrvek("g"); 
        graf.VLozitPrvek("dkg");
        graf.VLozitPrvek("kg");
        graf.VLozitPrvek("t");       // přidání prvků (vertices)
        
        graf.PridatPropojeni("ng", "mg", new Spoj<String>("ng", 1_000_000));
        graf.PridatPropojeni("dkg", "g", new Spoj<String>("g", 10));
        graf.PridatPropojeni("dkg", "mg", new Spoj<String>("mg", 10_000));

        graf.PridatPropojeni("g", "kg", new Spoj<String>("g", 1_000));
        graf.PridatPropojeni("dkg", "t", new Spoj<String>("dkg", 100_000));             // přidání propojení (edges)          
    }
    public static void PridatJednotkyVzdalenosti() {
        graf.VLozitPrvek("pm"); //    // pikometr 1 000 = 1 nm
        graf.VLozitPrvek("nm"); //    // nanometr 1 000 000 = 1 mm
        graf.VLozitPrvek("mm"); 
        graf.VLozitPrvek("cm");
        graf.VLozitPrvek("dm");
        graf.VLozitPrvek("m");
        graf.VLozitPrvek("km");       // přidání prvků (vertices)
        
        graf.PridatPropojeni("pm", "nm", new Spoj<String>("pm", 1_000));
        graf.PridatPropojeni("nm", "mm", new Spoj<String>("nm", 1_000_000));
        graf.PridatPropojeni("mm", "cm", new Spoj<String>("mm", 10));

        graf.PridatPropojeni("nm", "dm", new Spoj<String>("nm", 100_000_000));
        graf.PridatPropojeni("mm", "m", new Spoj<String>("mm", 1_000));
        graf.PridatPropojeni("m", "km", new Spoj<String>("m", 1_000));            // přidání propojení (edges)
    }
    public static void PridatJednotkyCasu() {
        graf.VLozitPrvek("s"); 
        graf.VLozitPrvek("ms"); 
        graf.VLozitPrvek("ns"); 
        graf.VLozitPrvek("ps");    // přidání prvků (vertices)

        graf.PridatPropojeni("s", "ns", new Spoj<String>("ns", 1_000_000_000));
        graf.PridatPropojeni("ns", "ms", new Spoj<String>("ns", 1_000_000));
        graf.PridatPropojeni("ps", "ns", new Spoj<String>("ps", 1_000));       // přidání propojení (edges) 
    }
}