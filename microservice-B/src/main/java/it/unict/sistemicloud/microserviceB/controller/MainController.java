package it.unict.sistemicloud.microserviceB.controller;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.http.HttpClient;
import java.security.SecureRandom;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import it.unict.sistemicloud.microserviceB.DTO.Utente;
import it.unict.sistemicloud.microserviceB.DTO.Cliente;
import it.unict.sistemicloud.microserviceB.DTO.Numero;
import it.unict.sistemicloud.microserviceB.DTO.Ordine;
import it.unict.sistemicloud.microserviceB.DTO.Packet;
import it.unict.sistemicloud.microserviceB.DTO.Pizza;
import it.unict.sistemicloud.microserviceB.DTO.Richiesta;
import it.unict.sistemicloud.microserviceB.DTO.Tokens;
import it.unict.sistemicloud.microserviceB.DTO.Transazione;
import it.unict.sistemicloud.microserviceB.controller.Input;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.util.Scanner;

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
	
	@GetMapping(value = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
    public String hello(){
		System.out.println("Ciao");
    	return "Hello im microserver B";
	}
	
	
	@GetMapping(value = "/getA", produces = MediaType.APPLICATION_JSON_VALUE)
    public Packet ricevi() throws IOException{
			String jsonResult = doGetRequest("http://localhost:8081/associate");
			Packet packet = new Gson().fromJson(jsonResult, Packet.class);
			packet.microservizio +="B";
			/*
			System.out.println("Nome : "+packet.nome);
			System.out.println("Cognome : "+packet.cognome);
			System.out.println("Id transazione : "+packet.id_transazione);
			System.out.println("Microservizio : "+packet.microservizio);
			System.out.println("Token : "+packet.token);
			*/
			logger.info("Nome : "+packet.nome);
			logger.info("Cognome : "+packet.cognome);
			logger.info("Id transazione : "+packet.id_transazione);
			logger.info("Microservizio : "+packet.microservizio);
			logger.info("Token : "+packet.token);
			return packet;
	}
	
	@GetMapping(value = "/TokenValid", produces = MediaType.APPLICATION_JSON_VALUE)
    public Packet riceciC() throws IOException{
			String jsonResult = doGetRequest("http://localhost:8083/requestValidToken");
			Packet packet = new Gson().fromJson(jsonResult, Packet.class);
			packet.microservizio +="B";
			/*
			System.out.println("Nome : "+packet.nome);
			System.out.println("Cognome : "+packet.cognome);
			System.out.println("Id transazione : "+packet.id_transazione);
			System.out.println("Microservizio : "+packet.microservizio);
			System.out.println("Token : "+packet.token);
			*/
			logger.info("Nome : "+packet.nome);
			logger.info("Cognome : "+packet.cognome);
			logger.info("Id transazione : "+packet.id_transazione);
			logger.info("Microservizio : "+packet.microservizio);
			logger.info("Token : "+packet.token);
			return packet;
	}
	
	
    @GetMapping(value = "/elencoordini", produces = MediaType.APPLICATION_JSON_VALUE)
    public Ordine[] elencoordini() throws IOException {
	    	System.out.println("Elenco Ordini");
	    	FileReader file = new FileReader("Ordine.txt");
	    	BufferedReader lettore = new BufferedReader(file);
	    	int cont = 0;
	    	String riga = "";
	    	String ele = "";
	    	while(riga != null) {
	    			riga = lettore.readLine();
	    			//System.out.println(riga);
	    			cont++;
	    			ele += riga;
	    	}
	    	int righe = cont-1;
	    	//System.out.println("Le righe sono: "+righe);
	    	//System.out.println(ele); 	
	    	String[] x = null;
	    	if(ele.contains(";")) {
	    		x = ele.split(";");
	    		for(int i=0;i<righe*3;i++) {
	        		//System.out.println(x[i]);
	        	}
	    	}
	    	Ordine[] ordine = new Ordine[righe];
	    	int j=0;
	    	for(int i=0;i<ordine.length;i++) {
	    		Ordine o = new Ordine();
	    		o.nome = x[j];
	    		o.cognome = x[j+1];
	    		o.ordine = x[j+2];
	    		ordine[i] = o;
	    		j = j+3;
	    	}
	    	for(int i=0;i<ordine.length;i++) {
	    		Ordine o = ordine[i];
	    		System.out.println("Nome:"+ o.nome);
	    		System.out.println("Cognome :"+ o.cognome);
	    		System.out.println("Ordine:"+ o.ordine);
	    	}
	    	
	    	file.close();
	    	
	    	return ordine;
    }
	
	
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
