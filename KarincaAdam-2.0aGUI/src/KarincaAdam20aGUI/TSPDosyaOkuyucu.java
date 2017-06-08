/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KarincaAdam20aGUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mustafa.helvaci
 */
public class TSPDosyaOkuyucu extends Arayuz {
    
    public static final double[][] uzaklikMatrisiOlustur(File dosya) throws IOException {

        //final List<Koordinat> koordinatlar = new ArrayList<>();
        FileReader dosyaOku = new FileReader(dosya);
        try (final BufferedReader tampon = new BufferedReader(dosyaOku)) {

            boolean koordinatBaslangici = false;
            String satir;
            while ((satir = tampon.readLine()) != null) {
                //String satirSonu = satir.replaceAll("\\s+", "");
                satir = satir.replaceAll("^\\s+", ""); // Satır başındaki boşluğu siler.
                if (satir.equals("EOF")) {
                    break;
                }
                if (koordinatBaslangici) {
                    String kelimeler[] = satir.split(" ");
                    koordinatlar.add(new Koordinat(Double.parseDouble(kelimeler[1].trim()), Double.parseDouble(kelimeler[2].trim())));
                }
                if (satir.equals("NODE_COORD_SECTION")) {
                    koordinatBaslangici = true;
                }
            } 
        }

        final double[][] uzaklikMatrisi = new double[koordinatlar.size()][koordinatlar.size()];

        int satirIndisi = 0;
        for (Koordinat koordinat1 : koordinatlar) {
            int sutunIndisi = 0;
            for (Koordinat koordinat2 : koordinatlar) {
                uzaklikMatrisi[satirIndisi][sutunIndisi] = oklidDistanceHesapla(koordinat1.x, koordinat1.y, koordinat2.x, koordinat2.y);
                sutunIndisi++;
            }
            satirIndisi++;
        }
        return uzaklikMatrisi;
    }

    private static final double oklidDistanceHesapla(double x1, double y1, double x2, double y2) {
        final double xLerFarki = x2 - x1;
        final double yLerFarki = y2 - y1;
        return (Math.abs((Math.sqrt((xLerFarki * xLerFarki) + (yLerFarki * yLerFarki)))) + 1); // 0 uzunluklu kenar olmaması için 1 ekliyoruz. (sonradan çıkarılacak)
    }
    
}
