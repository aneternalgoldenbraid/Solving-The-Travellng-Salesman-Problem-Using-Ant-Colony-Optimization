// GUI eklenmiş versiyon.
// 0 uzunlukluklu kenar ve progress bar problemi giderildi.
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KarincaAdam20aGUI;


import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author mustafa.helvaci
 */
public class KarincaAdam20aGUI {

    /**
     * @param args the command line arguments
     */
    public static Graphics grafik;
    public double c = 1.0;
    public double Q = 500;
    
    public static double uzaklikMatrisi[][] = null;
    public double feromonIzi[][] = null;
    public double olasiliklar[] = null;
    public Karinca karincalar[] = null;
    public int turIndisi = 0;
    public static int sehirSayisi = 0;
    public int karincaSayisi = 0;
    private Random rassal = new Random();
    public int[] enIyiTur;
    public double enIyiTurUzunlugu;

    private class Karinca { // TSP dosyası okunduktan sonra şehir sayısı ve karıncaYüzdesine göre karınca kolonisi oluşturulur.
        public int tur[] = new int[uzaklikMatrisi.length];
        public boolean ziyaretEdildiMi[] = new boolean[uzaklikMatrisi.length];
        
        public void sehirZiyaretEt(int sehir) {
            tur[turIndisi + 1] = sehir;
            ziyaretEdildiMi[sehir] = true;
        }
        
        public boolean ziyaretEdildiMi(int sehir) {
            return ziyaretEdildiMi[sehir];
        }
        
        public double turUzunlugu() {
            double uzunluk = uzaklikMatrisi[tur[sehirSayisi - 1]][tur[0]]; // Son şehirden ilk şehire olan uzaklık.
            for (int i = 0; i < sehirSayisi - 1; i++)
                uzunluk = uzunluk + uzaklikMatrisi[tur[i]][tur[i + 1]];
            return uzunluk;
        }
        
        public void sifirla() {
            for (int i = 0; i < sehirSayisi; i++)
                ziyaretEdildiMi[i] = false;
        }
        
    } 
    
    public static void dosyaOku(File tspDosyaAdi) {
        try {
            uzaklikMatrisi = TSPDosyaOkuyucu.uzaklikMatrisiOlustur(tspDosyaAdi);
        } catch (IOException ex) {
            Logger.getLogger(KarincaAdam20aGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    public void TSPDosyasiOku(File tspDosyaAdi, double karincaYuzdesi) throws IOException {
        
        dosyaOku(tspDosyaAdi) ;
        sehirSayisi = uzaklikMatrisi.length;
        karincaSayisi = (int)(karincaYuzdesi * sehirSayisi);
        feromonIzi = new double[sehirSayisi][sehirSayisi];
        olasiliklar = new double[sehirSayisi];
        karincalar = new Karinca[karincaSayisi];
        for (int k = 0; k < karincaSayisi; k++) 
            karincalar[k] = new Karinca();
    }
    
    private void karincaAyarla() {
        turIndisi = -1;
        for (int i = 0; i < karincaSayisi; i++) {
            karincalar[i].sifirla();
            karincalar[i].sehirZiyaretEt(rassal.nextInt(sehirSayisi));
        }
        turIndisi++; //sehirZiyaretEt metodu içinde turIndisi+1 değerine atama yapıldığından burada önce turIndisi'ne -1 atayıp sonra 1 arttırıyoruz. 
        //Yani karincaAyarla metodunun çıkışında turIndisi değişkeni 0 oluyor.
    }
    
    private void karincaYurut(double alfa, double beta, double rassallikFaktoru) { //Her bir karınca turunu tamamlayana kadar devam eder.
        while ( turIndisi < (sehirSayisi - 1) ) {
            //Arayuz.jTextArea1.append(String.valueOf(turIndisi+1) + ". iterasyon : ");
            for (int i = 0; i < karincaSayisi; i++) {
                karincalar[i].sehirZiyaretEt(siradakiSehriSec(karincalar[i], alfa, beta, rassallikFaktoru));
            }
            turIndisi++;
        }
    }
    
    private int siradakiSehriSec(Karinca karinca, double alfa, double beta, double rassallikFaktoru) {
        if (rassal.nextDouble() < rassallikFaktoru) {
            int r = rassal.nextInt(sehirSayisi - turIndisi); //Gezilmemiş şehir sayısına kadar bir rasgele sayı üret.
            int j = -1;
            for (int i = 0; i < sehirSayisi; i++) { //Gezilmemiş şehirlerin bu sayıncı indisi sıradaki gezilecek şehir olarak seç.
                if (!karinca.ziyaretEdildiMi(i))
                    j++;
                if (j == r)
                    return i;
            }   
        }
        return olasilikHesapla(karinca, alfa, beta);
    }
    
    private int olasilikHesapla (Karinca karinca, double alfa, double beta) { 
        int i = karinca.tur[turIndisi];
        int maksOlasilikIndisi = 0; // Ziyaret edilmesi en karlı şehrin indisi.
        double maksOlasilik = 0.0;
        double olasiliklarToplami = 0.0;
        for (int j = 0; j < sehirSayisi; j++)
            if (!karinca.ziyaretEdildiMi(j))
                olasiliklarToplami +=  Math.pow(feromonIzi[i][j], alfa) * Math.pow(1.0 / uzaklikMatrisi[i][j], beta);
        
        for (int j = 0; j < sehirSayisi; j++) {
            if (karinca.ziyaretEdildiMi(j)) {
                olasiliklar[j] = 0.0; 
            } else {
                double pay = Math.pow(feromonIzi[i][j], alfa) * Math.pow(1.0 / uzaklikMatrisi[i][j], beta);
                olasiliklar[j] = pay / olasiliklarToplami;
                if (olasiliklar[j] > maksOlasilik) {
                    maksOlasilik = olasiliklar[j];
                    maksOlasilikIndisi = j;
                }
            }
        }
        return maksOlasilikIndisi;
    }
    
    private void feromonGuncelle(double buharlasmaFaktoru) {
        for (int i = 0; i < sehirSayisi; i++)
            for (int j = 0; j < sehirSayisi; j++)
                feromonIzi[i][j] = feromonIzi[i][j] * buharlasmaFaktoru;
        
        for (int i = 0; i < karincaSayisi; i++) {
            double karincaKatkisi = Q / karincalar[i].turUzunlugu();
            for (int j = 0; j < sehirSayisi - 1; j++)
                feromonIzi[karincalar[i].tur[j]][karincalar[i].tur[j + 1]] += karincaKatkisi;
            feromonIzi[karincalar[i].tur[sehirSayisi - 1]][karincalar[i].tur[0]] += karincaKatkisi; //Turun son şehiri ile ilk şehiri arası feromon izi güncelle.  
        }       
    }
    
    private void enIyiTurBelirle() {
        if (enIyiTur == null) {
            enIyiTur = karincalar[0].tur;
            enIyiTurUzunlugu = karincalar[0].turUzunlugu();
        }
        for (int i = 1; i < karincaSayisi; i++) {
            if (karincalar[i].turUzunlugu() < enIyiTurUzunlugu) {
                enIyiTurUzunlugu = karincalar[i].turUzunlugu();
                enIyiTur = karincalar[i].tur.clone(); 
            }
        }
    }
    
    public void hesapla(int iterasyon, double karincaYuzdesi, double alfa, double beta, double rassallikFaktoru, double buharlasmaFaktoru) {
        double oncekiEnIyiTurUzunlugu = Double.MAX_VALUE;
        for (int i = 0; i < sehirSayisi; i++)
            for (int j = 0; j < sehirSayisi; j++)
                feromonIzi[i][j] = c;
        for (int i = 0; i < iterasyon; i++) {
            
            karincaAyarla(); //Karınca ayarlarını sıfırlayıp, her karıncayı rasgele bir şehire yerleştir.
            karincaYurut(alfa, beta, rassallikFaktoru);
            feromonGuncelle(buharlasmaFaktoru);
            enIyiTurBelirle();
            
            if (Arayuz.karincaSistemiCalistir == false) {
                System.out.println("\n" + (i + 1) + ". iterasyon : ");
                System.out.println("En iyi tur uzunlugu : " + (enIyiTurUzunlugu - sehirSayisi)); // 0 uzunluklu mesafenin önüne geçmek için 1 eklemştik, şimdi her şehir için 1 çıkarıyoruz.
                for (int j = 0; j < uzaklikMatrisi.length; j++) 
                    System.out.print((enIyiTur[j] + 1) + "-");
                System.out.println();
            } else if (Arayuz.karincaSistemiCalistir == true) { 
                Arayuz.jTextArea1.append(String.valueOf(i + 1) + ". iterasyon : ");
                for (int j = 0; j < enIyiTur.length; j++) 
                    Arayuz.jTextArea1.append(Integer.toString(enIyiTur[j] + 1) + "-");
                    //Arayuz.jTextArea1.update(Arayuz.jTextArea1.getGraphics());
                if (enIyiTurUzunlugu < oncekiEnIyiTurUzunlugu) {
                    Arayuz.kenarCiz(grafik, enIyiTur);
                    oncekiEnIyiTurUzunlugu = enIyiTurUzunlugu;
                }    
                Arayuz.jTextArea1.append("\nEn iyi tur uzunluğu : " + Double.toString(enIyiTurUzunlugu - sehirSayisi));
                Arayuz.jTextArea1.append("\n\n");
                Arayuz.jProgressBar1.setValue((int)((100 * i) / iterasyon) + 1);
                Arayuz.jProgressBar1.update(Arayuz.jProgressBar1.getGraphics()); 
                if ((i == (iterasyon - 1)) && (Arayuz.jProgressBar1.getValue() != 100))
                    Arayuz.jProgressBar1.setValue(100);
            }  
        }
        if (Arayuz.karincaSistemiCalistir == true) {
            for (int i = 0; i < enIyiTur.length; i++) 
                Arayuz.jTextArea2.append(Integer.toString(enIyiTur[i] + 1) + "-");
            Arayuz.jTextField7.setText(Double.toString(enIyiTurUzunlugu - sehirSayisi) + " birim");
        }
    }
    
    
    public static void main(String[] args) { // java -jar KarincaAdam-2.0aGUI.jar berlin52.tsp
        // TODO code application logic here
        KarincaAdam20aGUI karincaTSP = new KarincaAdam20aGUI();
        
        Scanner okuyucu = new Scanner(System.in);
        int iterasyon;
        char cevap;
        double karincaYuzdesi = 0.8;
        double alfa = 1.0;
        double beta = 5.0;
        double rassallikFaktoru = 0.01;
        double buharlasmaFaktoru = 0.5;
        
        if (Arayuz.karincaSistemiCalistir == false) {
            do {
                System.out.printf("Algoritma parametrelerini el ile girmek ister misiniz? (e/h) : ");
                cevap = okuyucu.next().charAt(0);
            } while( (cevap !='e') && (cevap !='h'));
            if (cevap == 'e') {
                do {
                    System.out.printf("Karınca yüzdesi giriniz (0.1 - 2.0) : ");
                    karincaYuzdesi = okuyucu.nextDouble();
                } while( (karincaYuzdesi < 0.1) || (karincaYuzdesi > 2.0) );
                do {
                    System.out.printf("Alfa sabiti giriniz (1 - 10) : ");
                    alfa = okuyucu.nextDouble();
                } while( (alfa < 1) || (alfa > 10) );
                do {
                    System.out.printf("Beta sabiti giriniz (1 - 10) : ");
                    beta = okuyucu.nextDouble();
                } while( (beta < 1) || (beta > 10) );
                do {
                    System.out.printf("Rassallik faktoru giriniz (0.001 - 0.99) : ");
                    rassallikFaktoru = okuyucu.nextDouble();
                } while( (rassallikFaktoru < 0.001) || (rassallikFaktoru > 0.99) );
                do {
                    System.out.printf("Buharlaşma faktoru giriniz (0.1 - 0.9) : ");
                    buharlasmaFaktoru = okuyucu.nextDouble();
                } while( (buharlasmaFaktoru < 0.1) || (buharlasmaFaktoru > 0.9) );
            }
        }
        
        if (Arayuz.karincaSistemiCalistir == false) {
            try {
                karincaTSP.TSPDosyasiOku(new File(args[0]), karincaYuzdesi);
            } catch (IOException e) {
                System.err.println("TSP dosyasi bulunamadi.");
                return;
            } 
        } else if (Arayuz.karincaSistemiCalistir == true) {
            try {
                karincaYuzdesi = Double.parseDouble(Arayuz.jTextField1.getText());
                alfa = Double.parseDouble(Arayuz.jTextField2.getText());
                beta = Double.parseDouble(Arayuz.jTextField3.getText());
                rassallikFaktoru = Double.parseDouble(Arayuz.jTextField4.getText());
                buharlasmaFaktoru = Double.parseDouble(Arayuz.jTextField5.getText()); 
                Arayuz.jTextArea1.getText();
                karincaTSP.TSPDosyasiOku(Arayuz.dosya, karincaYuzdesi);
                Arayuz.koordinatNormalizeEt();
            } catch (IOException ex) {
                Logger.getLogger(KarincaAdam20aGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (Arayuz.karincaSistemiCalistir == false) {
            System.out.printf("Tekrar sayisiniz giriniz : ");
            iterasyon = okuyucu.nextInt();
        } else
            iterasyon = Integer.parseInt(Arayuz.jTextField6.getText());
        
        karincaTSP.hesapla(iterasyon, karincaYuzdesi, alfa, beta, rassallikFaktoru, buharlasmaFaktoru); 
    }
    
}
