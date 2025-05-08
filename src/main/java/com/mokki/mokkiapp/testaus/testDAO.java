package com.mokki.mokkiapp.testaus;

import com.mokki.mokkiapp.dao.*;
import com.mokki.mokkiapp.model.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Tällä testataan DAO luokkien toimivuus
 */
public class testDAO {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LaskuDAO laskuDAO = new LaskuDAO();
        AsiakasDAO asiakasDAO = new AsiakasDAO();
        VarausDAO varausDAO = new VarausDAO();
        MokkiDAO mokkiDAO = new MokkiDAO();

        while (true) {
            System.out.println("\n=== DAO Testivalikko ===");
            System.out.println("1: Näytä maksamattomat laskut");
            System.out.println("2: Näytä maksetut laskut");
            System.out.println("3: Merkitse lasku maksetuksi");
            System.out.println("4: Näytä yksityishenkilön asiakasprofiili");
            System.out.println("5: Näytä yritysasiakkaan profiili");
            System.out.println("6: Näytä kaikki varaukset");
            System.out.println("7: Näytä mökki tiedot");
            System.out.println("0: Poistu");
            System.out.println("11: Kaikki");
            System.out.print("Valinta: ");

            int valinta = Integer.parseInt(scanner.nextLine());

            switch (valinta) {
                case 1 -> {
                    List<Lasku> maksamattomat = laskuDAO.haeMaksamattomatLaskut();
                    maksamattomat.forEach(System.out::println);
                }
                case 2 -> {
                    List<Lasku> maksetut = laskuDAO.haeMaksetutLaskut();
                    maksetut.forEach(System.out::println);
                }
                case 3 -> {
                    System.out.print("Anna laskun ID: ");

                    int laskuId = Integer.parseInt(scanner.nextLine());
                    laskuDAO.merkitseMaksetuksi(laskuId);
                    System.out.println("Lasku " + laskuId + " merkitty maksetuksi.");
                }
                case 4 -> {
                    System.out.print("Anna asiakas ID (yksityinen): ");

                    int id = Integer.parseInt(scanner.nextLine());
                    Yksityishenkilo y = asiakasDAO.haeYksityishenkilo(id);
                    System.out.println(y);
                }
                case 5 -> {
                    System.out.print("Anna asiakas ID (yritys): ");

                    int id = Integer.parseInt(scanner.nextLine());
                    Yritys y = asiakasDAO.haeYritys(id);
                    System.out.println(y);
                }
                case 6 -> {
                    List<Varaus> varaukset = varausDAO.haeKaikkiVaraukset();
                    varaukset.forEach(System.out::println);
                }
                case 7 -> {
                    System.out.print("Anna mökin ID: ");
                    int mokkiId = Integer.parseInt(scanner.nextLine());
                    Mokki m = mokkiDAO.haeMokki(mokkiId);
                    System.out.println(m);
                }
                case 11 -> {
                    // LaskuDAO
                    testaaLaskuDAO();

                    // AsiakasDAO
                    testaaAsiakasDAO(1);
                    testaaYritysDAO(2);

                    // MokkiDAO
                    testaaMokkiDAO();

                    // VarausDAO
                    testaaVarausDAO();

                }
                case 0 -> {
                    System.out.println("Ohjelma sulkeutuu.");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Virheellinen valinta.");
            }
        }
    }



    private static void testaaLaskuDAO() {
        LaskuDAO dao = new LaskuDAO();

        System.out.println("== Maksamattomat laskut ==");

        List<Lasku> maksamattomat = dao.haeMaksamattomatLaskut();
        for (Lasku l : maksamattomat) {
            System.out.println("Lasku ID: " + l.getLaskuId() + ", Summa: " + l.getSumma() + ", Maksettu: " + l.isMaksettu());
        }

        if (!maksamattomat.isEmpty()) {
            int testiLaskuId = maksamattomat.get(0).getLaskuId();
            System.out.println("Merkitään lasku " + testiLaskuId + " maksetuksi...");
            dao.merkitseMaksetuksi(testiLaskuId);
        }
    }


    private static void testaaAsiakasDAO(int id) {
        AsiakasDAO dao = new AsiakasDAO();

        System.out.println("== Haetaan yksityishenkilö ID:llä " + id + " ==");
        Yksityishenkilo y = dao.haeYksityishenkilo(101);
        if (y != null)
            System.out.println(y.getEtunimi() + " " + y.getSukunimi() + " (" + y.getEmail() + ")");
    }

    private static void testaaYritysDAO(int id) {
        AsiakasDAO dao = new AsiakasDAO();

        System.out.println("== Haetaan yritys ID:llä " + id + " ==");
        Yritys yritys = dao.haeYritys(id);
        if (yritys != null)
            System.out.println(yritys.getYrityksenNimi() + " (" + yritys.getYtunnus() + ")");
    }


    private static void testaaVarausDAO() {
        VarausDAO dao = new VarausDAO();

        System.out.println("== Kaikki varaukset ==");
        for (Varaus v : dao.haeKaikkiVaraukset()) {
            System.out.println("Varaus ID: " + v.getVarausId() + ", Asiakas ID: " + v.getAsiakasId() + ", Alkaa: " + v.getAlku());
        }

        System.out.println("== Lisää varaus ==");
        // JDBC vaatii java.sql.Date
        // joten localDate -> Date
        Varaus uusiVaraus = new Varaus(
                1112,                                       // varaus_id (valitse uniikki ID)
                1,                                                  // asiakas_id
                101,                                                // mokki_id
                Date.valueOf(LocalDate.of(2024, 10, 1)),   // alkupvm
                Date.valueOf(LocalDate.of(2025, 10, 5)),   // loppupvm
                Date.valueOf(LocalDate.now())                                     // varaus_pvm
        );
        dao.lisaaVaraus(uusiVaraus);

    }


    private static void testaaMokkiDAO() {
        MokkiDAO dao = new MokkiDAO();

        System.out.println("== Haetaan mökki ID:llä 1 ==");
        Mokki m = dao.haeMokki(1);
        if (m != null)
            System.out.println(m.getNimi() + ", " + m.getKatuosoite() + " (" + m.getHinta() + " €/yö)");
    }



}