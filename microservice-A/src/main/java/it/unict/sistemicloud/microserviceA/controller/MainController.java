package it.unict.sistemicloud.microserviceA.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpClient;
import java.security.SecureRandom;
import java.util.Scanner;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import it.unict.sistemicloud.microserviceA.DTO.Cliente;
import it.unict.sistemicloud.microserviceA.DTO.Numero;
import it.unict.sistemicloud.microserviceA.DTO.Packet;
import it.unict.sistemicloud.microserviceA.DTO.Pizza;
import it.unict.sistemicloud.microserviceA.DTO.Richiesta;
import it.unict.sistemicloud.microserviceA.controller.Input;
import it.unict.sistemicloud.microserviceA.DTO.Tokens;
import it.unict.sistemicloud.microserviceA.DTO.Transazione;
import it.unict.sistemicloud.microserviceA.DTO.Utente;
import okhttp3.*;
import javax.swing.*;
import java.awt.*;

import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class Input{
    
    private static BufferedReader reader = 
	new BufferedReader(new InputStreamReader(System.in));
    
    /**
       Legge una linea di input. Nell'improbabile caso di una
       IOException, il programma termina. 
       @return restituisce la linea di input che l'utente ha battuto.
    */
    public static String readLine(){  
	String inputLine = "";
	try{  
	    inputLine = reader.readLine();
	}
	catch(IOException e){  
	    System.out.println(e);
	    System.exit(1);
	}
	return inputLine;
    }
    
    /**
       Legge una linea di input e la converte in un intero.
       Eventuali spazi bianchi prima e dopo l'intero vengono ignorati.
       @return l'intero dato in input dall'utente
    */
    public static int readInt(){  
	String inputString = readLine();
	inputString = inputString.trim();
	int n = Integer.parseInt(inputString);
	return n;
    }
   
    /**
       Legge una linea di input e la converte in un numero
       in virgola mobile.  Eventuali spazi bianchi prima e
       dopo il numero vengono ignorati.
       @return il numero dato in input dall'utente 
    */
    public static double readDouble(){  
	String inputString = readLine();
	inputString = inputString.trim();
	double x = Double.parseDouble(inputString);
	return x;
    }
   
    /**
       Legge una linea di input e ne estrae il primo carattere.  
       @return il primo carattere della riga data in input dall'utente 
    */
}


@RestController

public class MainController {

    private HttpClient httpClient;
    
    Logger logger = LoggerFactory.getLogger(MainController.class);
    
    
    public String id_transazione() {
    	String lower = "abcdefghijklmnopqrstuvwxyz";
        String upper = lower.toUpperCase();
        String numeri = "0123456789";
        String perRandom = upper + lower + numeri;
        int lunghezzaRandom = 20;

        SecureRandom sr = new SecureRandom();
        StringBuilder sb = new StringBuilder(lunghezzaRandom);
        for (int i = 0; i < lunghezzaRandom; i++) {
            int randomInt = sr.nextInt(perRandom.length());
            char randomChar = perRandom.charAt(randomInt);
            sb.append(randomChar);
        }
    	return sb.toString();
	}
    
	@GetMapping(value = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
    public String hello() {
		System.out.println("Ciao");
		logger.info("Ciao");
    	return "Hello im microserver A. This is API GATEWAY";
    }
	
	@GetMapping(value = "/utente", produces = MediaType.APPLICATION_JSON_VALUE)
    public Utente registrautente() {
		Utente utente = new Utente();
		utente.nome = "Nunzio";
		utente.cognome = "Pedalino";
		return utente;
    }
	
	
	@GetMapping(value = "/associate", produces = MediaType.APPLICATION_JSON_VALUE)
    public Packet packet() throws IOException {
				String jsonResult = doGetRequest("http://localhost:8081/utente");
				Utente utente = new Gson().fromJson(jsonResult, Utente.class);
				/*
				System.out.println("Nome: "+ utente.nome);
				System.out.println("Cognome: "+utente.cognome);
				*/
				logger.info("Nome: "+ utente.nome);
				logger.info("Cognome: "+utente.cognome);
				
				String jsonResult1 = doGetRequest("http://localhost:8081/createTrackid");
				Transazione transazione = new Gson().fromJson(jsonResult1, Transazione.class);
				/*
				System.out.println("Id transazione : "+transazione.id_transazione);
				System.out.println("Microservizio : "+transazione.microservizio);
				*/
				logger.info("Id transazione : "+transazione.id_transazione);
				logger.info("Microservizio : "+transazione.microservizio);
				
				String jsonResult2 = doGetRequest("http://localhost:8081/createToken");
				Tokens token = new Gson().fromJson(jsonResult2, Tokens.class);
				//System.out.println("token : "+token.token_id);
				logger.info("token : "+token.token_id);
				
				Packet packet = new Packet();
				packet.nome = utente.nome;
				packet.cognome = utente.cognome;
				packet.id_transazione = transazione.id_transazione;
				packet.microservizio = transazione.microservizio;
				packet.token = token.token_id;
				return packet;
    }
	
	
	
	@GetMapping(value = "/createTrackid", produces = MediaType.APPLICATION_JSON_VALUE)
    public Transazione track_id(String sb) {
		Transazione transazione = new Transazione();
    	transazione.id_transazione = id_transazione();
    	transazione.microservizio = "A";
    	return transazione;
    }
	
	
	@GetMapping(value = "/createToken", produces = MediaType.APPLICATION_JSON_VALUE)
    public Tokens create() {
		Tokens token = new Tokens();
		token.token_id = "TheTokenStar2020";
		return token;
	}
	
	
	
	@GetMapping(value = "/getDonepizza", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDonepizza() throws IOException {
				String jsonResult = doGetRequest("http://localhost:8084/getonepizza");
				String a = jsonResult;
				return a;
	}
	
	
	
	
	
	@GetMapping(value = "/getDconto", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDconto() throws IOException {
				String jsonResult = doGetRequest("http://localhost:8084/getconto");
				String a = jsonResult;
				return a;
	}
	
	
	
	
	
	@GetMapping(value = "/getDclienti", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDclienti() throws IOException {
				String jsonResult = doGetRequest("http://localhost:8084/getclienti");
				return jsonResult;
	}
	
	
	
	@GetMapping(value = "/getDpizze", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDpizze() throws IOException {
				String jsonResult = doGetRequest("http://localhost:8084/getpizze");
				return jsonResult;
	}
	
	
	
	
	@GetMapping(value = "/getDordini", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDordine() throws IOException {
				String jsonResult = doGetRequest("http://localhost:8084/getordine");
				return jsonResult;
	}
	
	
	
	/*
	@GetMapping(value = "/getDclienti2", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDclienti2() throws IOException {
				String jsonResult = doGetRequest("http://localhost:8084/getclienti2");
				return jsonResult;
	}
	
	
	@GetMapping(value = "/getDpizze2", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDpizze2() throws IOException {
				String jsonResult = doGetRequest("http://localhost:8084/getpizze2");
				return jsonResult;
	}
	
	
	@GetMapping(value = "/getDordini2", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDordine2() throws IOException {
				String jsonResult = doGetRequest("http://localhost:8084/getordine2");
				return jsonResult;
	}
	
	@GetMapping(value = "/getDconto2", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDconto2() throws IOException {
				String jsonResult = doGetRequest("http://localhost:8084/getconto2");
				//String a = new Gson().fromJson(jsonResult, String.class);
				String a = jsonResult;
				return a;
	}
	
	
	@GetMapping(value = "/getDonepizza2", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDonepizza2() throws IOException {
				String jsonResult = doGetRequest("http://localhost:8084/getonepizza2");
				//String a = new Gson().fromJson(jsonResult, String.class);
				String a = jsonResult;
				return a;
	}
	 */
	
	
	
	OkHttpClient client = new OkHttpClient();
    // code request code here
    String doGetRequest(String url) throws IOException {
      Request request = new Request.Builder()
          .url(url)
          .build();

      Response response = client.newCall(request).execute();
      return response.body().string();
    }

}