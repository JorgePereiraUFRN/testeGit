/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clientbandas;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Jorge
 */
public class ClientBandas {

    /**
     * @param args the command line arguments
     */
    public static String urlBase = "http://localhost:8084/webresources/";

    public static void main(String[] args) throws MalformedURLException, IOException, JSONException {
        //GET
        recuperarBandas();
        //GET
        recuperarBanda(1);
        //PUT
        atualizarBanda(1);
        //GET
        recuperarBanda(1);
        //POST
        adicionarBanda();
        //GET
        recuperarBandas();
        //DELETE
        removerBanda(1);
        //GET
        recuperarBandas();

    }

    public static String lerInputStream(InputStream inputStream) throws IOException {

        InputStreamReader isr = new InputStreamReader(inputStream);
        BufferedReader br = new BufferedReader(isr);
        String s = br.readLine();
        while (br.ready()) {
            s += br.readLine();
        }

        return s;

    }

    private static void recuperarBandas() throws MalformedURLException, JSONException, IOException, JsonSyntaxException, ProtocolException {
        System.out.println("\nRecuperando todas as bandas");
        URL recurso = new URL(urlBase + "bandas");
        HttpURLConnection conexao = (HttpURLConnection) recurso.openConnection();
        conexao.setRequestMethod("GET");
        conexao.setDoOutput(true);
        conexao.connect();

        String res = lerInputStream(conexao.getInputStream());
        conexao.disconnect();

        JSONArray retorno = new JSONArray(res);

        for (int i = 0; i < retorno.length(); i++) {

            Banda b = new Gson().fromJson(retorno.getString(i), Banda.class);
            System.out.println("\nBanda");
            System.out.println("nome: " + b.getNome());
            System.out.println("id: " + b.getId());
            System.out.println("ano de formação: " + b.getAnoDeFormacao());

        }
    }

    private static void recuperarBanda(Integer i) throws MalformedURLException, ProtocolException, JsonSyntaxException, JSONException, IOException {
        try {
            System.out.println("\nRecuperar banda " + i.toString());
            URL recurso = new URL(urlBase + "bandas/" + i.toString());
            HttpURLConnection conexao = (HttpURLConnection) recurso.openConnection();
            conexao.setRequestMethod("GET");
            conexao.setDoOutput(true);
            conexao.connect();

            String res = lerInputStream(conexao.getInputStream());
            conexao.disconnect();


            JSONObject banda = new JSONObject(res);

            Gson g = new Gson();
            Banda b = g.fromJson(banda.getString(i.toString()), Banda.class);

            System.out.println("\nBanda");
            System.out.println("nome: " + b.getNome());
            System.out.println("id: " + b.getId());
            System.out.println("ano de formação: " + b.getAnoDeFormacao());
        } catch (IOException e) {
            System.out.println("Erro. provavelmente banda " + i + " ja foi removida");
        }
    }

    private static void atualizarBanda(Integer i) throws ProtocolException, MalformedURLException, IOException {
        try {
            System.out.println("\nAtualizando banda " + i.toString());
            URL recurso = new URL(urlBase + "bandas");
            HttpURLConnection conexao = (HttpURLConnection) recurso.openConnection();
            conexao.setRequestMethod("PUT");
            conexao.setRequestProperty("content-type", "application/json");
            conexao.setDoOutput(true);

            Banda b = new Banda();
            b.setId(i);
            b.setAnoDeFormacao(2000);
            b.setNome("Grafite");

            String envia = new Gson().toJson(b).toString();


            conexao.connect();
            conexao.getOutputStream().write(envia.getBytes(), 0, envia.length());

            String res = lerInputStream(conexao.getInputStream());
            conexao.disconnect();

            System.out.println(res);
        } catch (IOException e) {
            System.out.println("Erro provavelmente banda " + i + " já foi removida");
        }
    }

    private static void adicionarBanda() throws IOException, ProtocolException, MalformedURLException {
        System.out.println("\nAdicionando banda");
        URL recurso = new URL(urlBase + "bandas");
        HttpURLConnection conexao = (HttpURLConnection) recurso.openConnection();
        conexao.setRequestMethod("POST");
        conexao.setRequestProperty("Content-Type", "application/json");

        Banda b = new Banda();
        b.setNome("Engenheiros do hawai");
        b.setAnoDeFormacao(2004);
        conexao.setDoOutput(true);
        String envia = new Gson().toJson(b).toString();
        conexao.connect();
        conexao.getOutputStream().write(envia.getBytes(), 0, envia.length());
        String res = lerInputStream(conexao.getInputStream());
        conexao.disconnect();

        System.out.println(res);
    }

    private static void removerBanda(Integer id) throws MalformedURLException, ProtocolException, IOException {
        try {
            System.out.println("\nRemovendo banda " + id.toString());
            URL recurso = new URL(urlBase + "bandas?id=" + id.toString());

            HttpURLConnection conexao = (HttpURLConnection) recurso.openConnection();
            conexao.setRequestMethod("DELETE");
            conexao.setDoInput(true);
            conexao.connect();
            String res = lerInputStream(conexao.getInputStream());
            conexao.disconnect();

            System.out.println(res);
        } catch (IOException e) {
            System.out.println("Erro provavelmente banda " + id + " já foi removida");
        }
    }
}
