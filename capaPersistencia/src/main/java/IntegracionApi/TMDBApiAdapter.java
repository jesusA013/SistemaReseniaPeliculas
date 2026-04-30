/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package IntegracionApi;

/**
 *
 * @author golea 80eda34110b2249c17d68b51ff161a5d
 */
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TMDBApiAdapter {

    private final String API_KEY = "80eda34110b2249c17d68b51ff161a5d";
    private final String BASE_URL = "https://api.themoviedb.org/3/movie/";

    public TMDBResponse buscarPeliculaDetalle(int tmdbId) {
        try {
            // Construir URL: https://api.themoviedb.org/3/movie/550?api_key=XXX&language=es-ES
            String urlString = BASE_URL + tmdbId + "?api_key=" + API_KEY + "&language=es-ES";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                TMDBResponse response = new Gson().fromJson(in, TMDBResponse.class);
                in.close();
                return response;
            }
        } catch (Exception e) {
            System.err.println("Error al conectar con TMDB: " + e.getMessage());
        }
        return null;
    }

    public List<TMDBResponse> buscarPeliculasPorNombre(String nombre) {
        try {
            // Codificar el nombre para la URL (maneja espacios y caracteres especiales)
            String query = URLEncoder.encode(nombre, StandardCharsets.UTF_8.toString());
            String urlString = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&query=" + query + "&language=es-ES";

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                TMDBSearchResult searchResult = new Gson().fromJson(in, TMDBSearchResult.class);
                in.close();
                return searchResult.results;
            }
        } catch (Exception e) {
            System.err.println("Error en búsqueda: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    private class TMDBSearchResult {

        List<TMDBResponse> results;
    }

    public ActorTMDB buscarActor(String nombre) {
        try {
            String urlString = "https://api.themoviedb.org/3/search/person?api_key=" + API_KEY
                    + "&query=" + java.net.URLEncoder.encode(nombre, "UTF-8");
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                ActorSearchResponse search = new Gson().fromJson(in, ActorSearchResponse.class);
                return (search.results != null && !search.results.isEmpty()) ? search.results.get(0) : null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public ActorTMDB obtenerDetalleActor(int actorId) {
        try {
            String urlString = "https://api.themoviedb.org/3/person/" + actorId + "?api_key=" + API_KEY + "&language=es-ES";
            java.net.URL url = new java.net.URL(urlString);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();

            if (conn.getResponseCode() == 200) {
                java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream()));
                ActorTMDB detalle = new com.google.gson.Gson().fromJson(in, ActorTMDB.class);
                in.close();
                return detalle;
            }
        } catch (Exception e) {
            System.err.println("Error al obtener detalle del actor: " + e.getMessage());
        }
        return null;
    
}
}
