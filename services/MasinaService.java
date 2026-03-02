package com.serviceauto.services;

import com.serviceauto.models.Masina;
import com.serviceauto.database.BazaDate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MasinaService {

    // Adaugă o mașină în DB și returnează ID-ul generat
    public static int adaugaMasinaInDB(Masina masina) {
        String sql = "INSERT INTO Masina (marca, model, proprietarId , numar_inmatriculare) VALUES (?, ?, ?, ?)";

        try (Connection con = BazaDate.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, masina.getMarca());
            stmt.setString(2, masina.getModel());
            stmt.setInt(3, masina.getProprietarId());
            stmt.setString(4, masina.getNR());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                masina.setId(id);
                return id;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // dacă nu s-a putut adăuga
    }

    // Preia toate mașinile din DB
    public static List<Masina> getToateMasinile() {
        List<Masina> masini = new ArrayList<>();
        String sql = "SELECT * FROM Masina";

        try (Connection con = BazaDate.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String marca = rs.getString("marca");
                String model = rs.getString("model");
                int proprietarId = rs.getInt("proprietarId");
                String nr = rs.getString("numar_inmatriculare");

                Masina m = new Masina(id,marca, model, proprietarId, nr);
                masini.add(m);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return masini;
    }

    // Afișează toate mașinile din DB
    public static void afiseazaMasini() {
        String sql = "SELECT * FROM Masina";

        try (Connection con = BazaDate.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Marca: " + rs.getString("marca") +
                        ", Model: " + rs.getString("model") +
                        ", ProprietarID: " + rs.getInt("proprietarId") +
                        ", Nr: " + rs.getString("nr"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
