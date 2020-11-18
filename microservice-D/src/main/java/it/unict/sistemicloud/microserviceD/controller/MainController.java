package it.unict.sistemicloud.microserviceD.controller;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.security.SecureRandom;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import it.unict.sistemicloud.microserviceD.DTO.Transazione;
import it.unict.sistemicloud.microserviceD.DTO.Tokens;
import it.unict.sistemicloud.microserviceD.DTO.Cliente;
import it.unict.sistemicloud.microserviceD.DTO.Numero;
import it.unict.sistemicloud.microserviceD.DTO.Ordine;
import it.unict.sistemicloud.microserviceD.DTO.Packet;
import it.unict.sistemicloud.microserviceD.DTO.Pizza;
import it.unict.sistemicloud.microserviceD.DTO.Richiesta;
import it.unict.sistemicloud.microserviceD.controller.Input;
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
    public String hello(){
			System.out.println("Ciao");
	    	return "Hello im microserver D";
	}
    
    
	
	
	
	boolean accesso = false;
    
    
    
    @GetMapping(value = "/getclienti", produces = MediaType.TEXT_PLAIN_VALUE)
    public String leggiclienti() throws IOException {
    	String jsonResult = doGetRequest("http://localhost:8082/TokenValid");
		Packet packet = new Gson().fromJson(jsonResult, Packet.class);
		packet.microservizio += "D";
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
		accesso = true;
    	if(accesso==true) {
			URL cg = new URL("https://bucketnunzio.s3.amazonaws.com/Clienti.txt");
			BufferedReader lettore = new BufferedReader(new InputStreamReader(cg.openStream()));
			int cont = 0;
	    	String riga = "";
	    	String ele = "";
	    	while((riga = lettore.readLine()) != null) {
	    			//riga = lettore.readLine();
	    			//System.out.println(riga);
	    			cont++;
	    			ele += riga;
	    	}
	    	//int righe = cont-1;
	    	int righe = cont;
	    	//System.out.println("Le righe sono: "+righe);
	    	//System.out.println(ele); 	
	    	String[] x = null;
	    	if(ele.contains(";")) {
	    		x = ele.split(";");
	    		for(int i=0;i<righe*2;i++) {
	        		//System.out.println(x[i]);
	        	}
	    	}
	    	Cliente[] cliente = new Cliente[righe];
	    	int j=0;
	    	for(int i=0;i<cliente.length;i++) {
	    		Cliente c = new Cliente();
	    		c.nome = x[j];
	    		c.cognome = x[j+1];
	    		cliente[i] = c;
	    		j = j+2;
	    	}
	    	for(int i=0;i<cliente.length;i++) {
	    		Cliente c = cliente[i];
	    		logger.info("Nome:"+ c.nome);
	    		logger.info("Cognome :"+ c.cognome);
	    		logger.info("");
	    		/*
	    		System.out.println("Nome:"+ c.nome);
	    		System.out.println("Cognome :"+ c.cognome);
	    		System.out.println("");
	    		 */
	    	}
	    	return "LA SEQUENZA ESEGUITA E' : "+packet.microservizio;
    	}
    	else {
    		System.out.println("Accesso Negato");
    		return "Accesso negato";
    	}
    }
    
    
    
    
    @GetMapping(value = "/getpizze", produces = MediaType.TEXT_PLAIN_VALUE)
    public String leggipizze() throws IOException {
    	String jsonResult = doGetRequest("http://localhost:8082/TokenValid");
		Packet packet = new Gson().fromJson(jsonResult, Packet.class);
		packet.microservizio += "D";
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
		accesso = true;
    	if(accesso==true) {
			URL cg = new URL("https://bucketnunzio.s3.amazonaws.com/Pizze.txt");
			BufferedReader lettore = new BufferedReader(new InputStreamReader(cg.openStream()));
			int cont = 0;
	    	String riga = "";
	    	String ele = "";
	    	while((riga = lettore.readLine()) != null) {
	    			//riga = lettore.readLine();
	    			//System.out.println(riga);
	    			cont++;
	    			ele += riga;
	    	}
	    	//int righe = cont-1;
	    	int righe = cont;
	    	//System.out.println("Le righe sono: "+righe);
	    	//System.out.println(ele); 	
	    	String[] x = null;
	    	if(ele.contains(";")) {
	    		x = ele.split(";");
	    		for(int i=0;i<righe*3;i++) {
	        		//System.out.println(x[i]);
	        	}
	    	}
	    	Pizza[] pizza = new Pizza[righe];
	    	int j=0;
	    	for(int i=0;i<pizza.length;i++) {
	    		Pizza p = new Pizza();
	    		p.nome_pizza = x[j];
	    		p.ingredienti = x[j+1];
	    		p.prezzo = x[j+2];
	    		pizza[i] = p;
	    		j = j+3;
	    	}
	    	Pizza[] p = new Pizza[righe];
	    	for(int i=0;i<pizza.length;i++) {
	    		p[i] = pizza[i];
	    		logger.info("Nome pizza :"+ p[i].nome_pizza);
	    		logger.info("Ingredienti :"+ p[i].ingredienti);
	    		logger.info("Prezzo :"+p[i].prezzo);
	    		logger.info("");
	    		/*
	    		System.out.println("Nome pizza :"+ p[i].nome_pizza);
	    		System.out.println("Ingredienti :"+ p[i].ingredienti);
	    		System.out.println("Prezzo :"+p[i].prezzo);
	    		System.out.println("");
	    		*/
	    	}
	    	return "LA SEQUENZA ESEGUITA E' : "+packet.microservizio;
    	}
    	else {
    		System.out.println("Accesso negato");
    		return "Accesso negato";
    	}
    }
    
    
   
    
    
    @GetMapping(value = "/getordine", produces = MediaType.TEXT_PLAIN_VALUE)
    public String leggiordine() throws IOException {
    	String jsonResult = doGetRequest("http://localhost:8082/TokenValid");
		Packet packet = new Gson().fromJson(jsonResult, Packet.class);
		packet.microservizio += "D";
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
    	accesso = true;
    	if(accesso==true) {
			URL cg = new URL("https://bucketnunzio.s3.amazonaws.com/Ordine.txt");
			BufferedReader lettore = new BufferedReader(new InputStreamReader(cg.openStream()));
			int cont = 0;
	    	String riga = "";
	    	String ele = "";
	    	while((riga = lettore.readLine()) != null) {
	    			//riga = lettore.readLine();
	    			//System.out.println(riga);
	    			cont++;
	    			ele += riga;
	    	}
	    	//int righe = cont-1;
	    	int righe = cont;
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
	    		logger.info("Nome:"+ o.nome);
	    		logger.info("Cognome :"+ o.cognome);
	    		logger.info("Ordine:"+ o.ordine);
	    		logger.info("");
	    		/*
	    		System.out.println("Nome:"+ o.nome);
	    		System.out.println("Cognome :"+ o.cognome);
	    		System.out.println("Ordine:"+ o.ordine);
	    		System.out.println("");
	    		*/
	    	}
	    	return "LA SEQUENZA ESEGUITA E' : "+packet.microservizio;
    	}
    	else {
    		System.out.println("Accesso negato");
    		return "Accesso negato";
    	}
    }
    
    
    
    
    @GetMapping(value = "/getconto", produces = MediaType.TEXT_PLAIN_VALUE)
    public String chiediconto() {
       try {
    	String jsonResult = doGetRequest("http://localhost:8082/getA");
   		Packet packet = new Gson().fromJson(jsonResult, Packet.class);
   		packet.microservizio += "D";
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
		String jsonResult2 = doGetRequest("http://localhost:8082/elencoordini");
		Ordine[] ordine = new Gson().fromJson(jsonResult2, Ordine[].class);
		System.out.println("La dimensione e' : "+ordine.length);
		for(int i=0;i<ordine.length;i++) {
			System.out.println(ordine[i].nome+" "+ordine[i].cognome+" "+ordine[i].ordine);
			System.out.println("");
		}
		
		double prezzo = 0;
		String nome_cliente = "";
		System.out.println("Inserisci nome cliente");
		nome_cliente = Input.readLine();
		
		
		for(int i=0;i<ordine.length;i++) {
			if(ordine[i].nome.equals(nome_cliente)) {
				System.out.println("Hai chiesto il conto di "+ordine[i].cognome);
				System.out.println("Prezzzo"+prezzo);
			}
		}
		
		String[] single_pizze;
		for(int i=0;i<ordine.length;i++) {
			if(ordine[i].nome.equals(nome_cliente)) {
				String pizze = ordine[i].ordine;
				single_pizze = pizze.split(",");
				//System.out.println("Siamo qua");
				
				System.out.println(single_pizze);
				for(int j=0;j<single_pizze.length;j++) {
					System.out.println(single_pizze[j]);
					if(single_pizze[j].equals("margherita")) {
						prezzo += 6.50;
						System.out.println(prezzo);
					}
					else if(single_pizze[j].equals("capricciosa")) {
						prezzo += 7.00;
						System.out.println(prezzo);
					}
					else if(single_pizze[j].equals("rucola")) {
						prezzo += 7.50;
						System.out.println(prezzo);
					}
					else if(single_pizze[j].equals("quattro stagioni")) {
						prezzo += 7.50;
						System.out.println(prezzo);
					}
					else if(single_pizze[j].equals("partenopea")) {
						prezzo += 8.00;
						System.out.println(prezzo);
					}
					else if(single_pizze[j].equals("romana")) {
						prezzo += 7.00;
						System.out.println(prezzo);
					}
					else if(single_pizze[j].equals("diavola")) {
						prezzo += 6.50;
						System.out.println(prezzo);
					}
					else if(single_pizze[j].equals("viennese")) {
						prezzo += 5.00;
						System.out.println(prezzo);
					}
					else if(single_pizze[j].equals("siciliana")) {
						prezzo += 9.00;
						System.out.println(prezzo);
					}
					else if(single_pizze[j].equals("texana")) {
						prezzo += 7.50;
						System.out.println(prezzo);
					}
				}
			}
		}
		return "Il prezzo da pagare e' "+String.valueOf(prezzo)+" € "+"LA SEQUENZA ESEGUITA E' : "+packet.microservizio;
       } catch (IOException e) {
    	   e.printStackTrace();
		return "Non riesco a contattare il microservice B";
       }
    }
        
    
    
    @GetMapping(value = "/getonepizza", produces = MediaType.TEXT_PLAIN_VALUE)
    public String richiedipizza() throws IOException {
    	String jsonResult = doGetRequest("http://localhost:8082/TokenValid");
		Packet packet = new Gson().fromJson(jsonResult, Packet.class);
		packet.microservizio += "D";
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
    	accesso = true;
    	if(accesso==true) {
			URL cg = new URL("https://bucketnunzio.s3.amazonaws.com/Pizze.txt");
			BufferedReader lettore = new BufferedReader(new InputStreamReader(cg.openStream()));
			int cont = 0;
	    	String riga = "";
	    	String ele = "";
	    	while((riga = lettore.readLine()) != null) {
	    			//riga = lettore.readLine();
	    			System.out.println(riga);
	    			cont++;
	    			ele += riga;
	    	}
	    	//int righe = cont-1;
	    	int righe = cont;
	    	//System.out.println("Le righe sono: "+righe);
	    	//System.out.println(ele); 	
	    	String[] x = null;
	    	if(ele.contains(";")) {
	    		x = ele.split(";");
	    		for(int i=0;i<righe*3;i++) {
	        		//System.out.println(x[i]);
	        	}
	    	}
	    	Pizza[] pizza = new Pizza[righe];
	    	int j=0;
	    	for(int i=0;i<pizza.length;i++) {
	    		Pizza p = new Pizza();
	    		p.nome_pizza = x[j];
	    		p.ingredienti = x[j+1];
	    		p.prezzo = x[j+2];
	    		pizza[i] = p;
	    		j = j+3;
	    	}
	    	for(int i=0;i<pizza.length;i++) {
	    		Pizza p = pizza[i];
	    		System.out.println("Nome pizza :"+ p.nome_pizza);
	    		System.out.println("Ingredienti :"+ p.ingredienti);
	    		System.out.println("Prezzo :"+p.prezzo);
	    		System.out.println("");
	    	}
			
			System.out.println("Inserisci il nome della pizza che vuoi cercare");
			String nome_pizza = Input.readLine();
			for(int i=0;i<pizza.length;i++) {
				Pizza p = pizza[i];
				if(nome_pizza.equals(p.nome_pizza)) {
					System.out.println(packet.microservizio);
					return "Nome pizza: "+p.nome_pizza+", Ingredienti: "+p.ingredienti+", Prezzo: "+p.prezzo;
				}
	    	}
			return "Accesso consentito";
    	}
    	else {
    		System.out.println("Accesso negato");
    		return "Accesso negato";
    	}
    }
    
    /*
    @GetMapping(value = "/getonepizza2", produces = MediaType.TEXT_PLAIN_VALUE)
    public String richiedipizza2() throws IOException {
    	String jsonResult = doGetRequest("http://localhost:8082/getA");
		Packet packet = new Gson().fromJson(jsonResult, Packet.class);
		System.out.println(packet.nome);
		System.out.println(packet.cognome);
		System.out.println(packet.id_transazione);
		System.out.println(packet.microservizio);
		System.out.println(packet.token);
    	accesso = true;
    	if(accesso==true) {
			URL cg = new URL("https://bucketnunzio.s3.amazonaws.com/Pizze.txt");
			BufferedReader lettore = new BufferedReader(new InputStreamReader(cg.openStream()));
			int cont = 0;
	    	String riga = "";
	    	String ele = "";
	    	while((riga = lettore.readLine()) != null) {
	    			//riga = lettore.readLine();
	    			System.out.println(riga);
	    			cont++;
	    			ele += riga;
	    	}
	    	//int righe = cont-1;
	    	int righe = cont;
	    	//System.out.println("Le righe sono: "+righe);
	    	//System.out.println(ele); 	
	    	String[] x = null;
	    	if(ele.contains(";")) {
	    		x = ele.split(";");
	    		for(int i=0;i<righe*3;i++) {
	        		//System.out.println(x[i]);
	        	}
	    	}
	    	Pizza[] pizza = new Pizza[righe];
	    	int j=0;
	    	for(int i=0;i<pizza.length;i++) {
	    		Pizza p = new Pizza();
	    		p.nome_pizza = x[j];
	    		p.ingredienti = x[j+1];
	    		p.prezzo = x[j+2];
	    		pizza[i] = p;
	    		j = j+3;
	    	}
	    	for(int i=0;i<pizza.length;i++) {
	    		Pizza p = pizza[i];
	    		System.out.println("Nome pizza :"+ p.nome_pizza);
	    		System.out.println("Ingredienti :"+ p.ingredienti);
	    		System.out.println("Prezzo :"+p.prezzo);
	    		System.out.println("");
	    	}
			
			System.out.println("Inserisci il nome della pizza che vuoi cercare");
			String nome_pizza = Input.readLine();
			for(int i=0;i<pizza.length;i++) {
				Pizza p = pizza[i];
				if(nome_pizza.equals(p.nome_pizza)) {
					return "Nome pizza: "+p.nome_pizza+", Ingredienti: "+p.ingredienti+", Prezzo: "+p.prezzo;
				}
	    	}
			return "Accesso consentito";
    	}
    	else {
    		System.out.println("Accesso negato");
    		return "Accesso negato";
    	}
    }
    
    
    
    @GetMapping(value = "/getconto2", produces = MediaType.TEXT_PLAIN_VALUE)
    public String chiediconto2() {
       try {
    	String jsonResult = doGetRequest("http://localhost:8082/getA");
   		Packet packet = new Gson().fromJson(jsonResult, Packet.class);
   		packet.microservizio = "D";
   		System.out.println(packet.nome);
   		System.out.println(packet.cognome);
   		System.out.println(packet.id_transazione);
   		System.out.println(packet.microservizio);
   		System.out.println(packet.token);
		String jsonResult2 = doGetRequest("http://localhost:8082/elencoordini");
		Ordine[] ordine = new Gson().fromJson(jsonResult2, Ordine[].class);
		System.out.println("La dimensione e' : "+ordine.length);
		for(int i=0;i<ordine.length;i++) {
			System.out.println(ordine[i].nome+" "+ordine[i].cognome+" "+ordine[i].ordine);
			System.out.println("");
		}
		
		double prezzo = 0;
		String nome_cliente = "";
		System.out.println("Inserisci nome cliente");
		nome_cliente = Input.readLine();
		
		
		for(int i=0;i<ordine.length;i++) {
			if(ordine[i].nome.equals(nome_cliente)) {
				System.out.println("Hai chiesto il conto di "+ordine[i].cognome);
				System.out.println("Prezzzo"+prezzo);
			}
		}
		
		String[] single_pizze;
		for(int i=0;i<ordine.length;i++) {
			if(ordine[i].nome.equals(nome_cliente)) {
				String pizze = ordine[i].ordine;
				single_pizze = pizze.split(",");
				System.out.println("Siamo qua");
				System.out.println(single_pizze);
				for(int j=0;j<single_pizze.length;j++) {
					System.out.println(single_pizze[j]);
					if(single_pizze[j].equals("margherita")) {
						prezzo += 6.50;
						System.out.println(prezzo);
					}
					else if(single_pizze[j].equals("capricciosa")) {
						prezzo += 7.00;
						System.out.println(prezzo);
					}
					else if(single_pizze[j].equals("rucola")) {
						prezzo += 7.50;
						System.out.println(prezzo);
					}
					else if(single_pizze[j].equals("quattro stagioni")) {
						prezzo += 7.50;
						System.out.println(prezzo);
					}
					else if(single_pizze[j].equals("partenopea")) {
						prezzo += 8.00;
						System.out.println(prezzo);
					}
					else if(single_pizze[j].equals("romana")) {
						prezzo += 7.00;
						System.out.println(prezzo);
					}
					else if(single_pizze[j].equals("diavola")) {
						prezzo += 6.50;
						System.out.println(prezzo);
					}
					else if(single_pizze[j].equals("viennese")) {
						prezzo += 5.00;
						System.out.println(prezzo);
					}
					else if(single_pizze[j].equals("siciliana")) {
						prezzo += 9.00;
						System.out.println(prezzo);
					}
					else if(single_pizze[j].equals("texana")) {
						prezzo += 7.50;
						System.out.println(prezzo);
					}
				}
			}
		}
		return "Il prezzo da pagare e' "+String.valueOf(prezzo)+" €";
       } catch (IOException e) {
    	   e.printStackTrace();
		return "Non riesco a contattare il microservice B";
       }
    }
    
    @GetMapping(value = "/getordine2", produces = MediaType.TEXT_PLAIN_VALUE)
    public String leggiordine2() throws IOException {
    	String jsonResult = doGetRequest("http://localhost:8082/getA");
		Packet packet = new Gson().fromJson(jsonResult, Packet.class);
		packet.microservizio = "D";
		System.out.println(packet.nome);
		System.out.println(packet.cognome);
		System.out.println(packet.id_transazione);
		System.out.println(packet.microservizio);
		System.out.println(packet.token);
    	accesso = true;
    	if(accesso==true) {
			URL cg = new URL("https://bucketnunzio.s3.amazonaws.com/Ordine.txt");
			BufferedReader lettore = new BufferedReader(new InputStreamReader(cg.openStream()));
			int cont = 0;
	    	String riga = "";
	    	String ele = "";
	    	while((riga = lettore.readLine()) != null) {
	    			//riga = lettore.readLine();
	    			//System.out.println(riga);
	    			cont++;
	    			ele += riga;
	    	}
	    	//int righe = cont-1;
	    	int righe = cont;
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
	    		System.out.println("");
	    	}
			return "Accesso consentito";
    	}
    	else {
    		System.out.println("Accesso negato");
    		return "Accesso negato";
    	}
    }
    
    @GetMapping(value = "/getpizze2", produces = MediaType.TEXT_PLAIN_VALUE)
    public String leggipizze2() throws IOException {
    	String jsonResult = doGetRequest("http://localhost:8082/getA");
		Packet packet = new Gson().fromJson(jsonResult, Packet.class);
		packet.microservizio = "D";
		System.out.println("Nome : "+packet.nome);
		System.out.println("Cognome : "+packet.cognome);
		System.out.println("Id transazione : "+packet.id_transazione);
		System.out.println("Microservizio : "+packet.microservizio);
		System.out.println("Token : "+packet.token);
		accesso = true;
    	if(accesso==true) {
			URL cg = new URL("https://bucketnunzio.s3.amazonaws.com/Pizze.txt");
			BufferedReader lettore = new BufferedReader(new InputStreamReader(cg.openStream()));
			int cont = 0;
	    	String riga = "";
	    	String ele = "";
	    	while((riga = lettore.readLine()) != null) {
	    			//riga = lettore.readLine();
	    			//System.out.println(riga);
	    			cont++;
	    			ele += riga;
	    	}
	    	//int righe = cont-1;
	    	int righe = cont;
	    	//System.out.println("Le righe sono: "+righe);
	    	//System.out.println(ele); 	
	    	String[] x = null;
	    	if(ele.contains(";")) {
	    		x = ele.split(";");
	    		for(int i=0;i<righe*3;i++) {
	        		//System.out.println(x[i]);
	        	}
	    	}
	    	Pizza[] pizza = new Pizza[righe];
	    	int j=0;
	    	for(int i=0;i<pizza.length;i++) {
	    		Pizza p = new Pizza();
	    		p.nome_pizza = x[j];
	    		p.ingredienti = x[j+1];
	    		p.prezzo = x[j+2];
	    		pizza[i] = p;
	    		j = j+3;
	    	}
	    	Pizza[] p = new Pizza[righe];
	    	for(int i=0;i<pizza.length;i++) {
	    		p[i] = pizza[i];
	    		System.out.println("Nome pizza :"+ p[i].nome_pizza);
	    		System.out.println("Ingredienti :"+ p[i].ingredienti);
	    		System.out.println("Prezzo :"+p[i].prezzo);
	    		System.out.println("");
	    	}
	    	return "Accesso consentito";
    	}
    	else {
    		System.out.println("Accesso negato");
    		return "Accesso negato";
    	}
    }
    
    @GetMapping(value = "/getclienti2", produces = MediaType.TEXT_PLAIN_VALUE)
    public String leggiclienti2() throws IOException {
    	String jsonResult = doGetRequest("http://localhost:8082/getA");
		Packet packet = new Gson().fromJson(jsonResult, Packet.class);
		packet.microservizio = "D";
		System.out.println("Nome : "+packet.nome);
		System.out.println("Cognome : "+packet.cognome);
		System.out.println("Id transazione : "+packet.id_transazione);
		System.out.println("Microservizio : "+packet.microservizio);
		System.out.println("Token : "+packet.token);
		accesso = true;
    	if(accesso==true) {
			URL cg = new URL("https://bucketnunzio.s3.amazonaws.com/Clienti.txt");
			BufferedReader lettore = new BufferedReader(new InputStreamReader(cg.openStream()));
			int cont = 0;
	    	String riga = "";
	    	String ele = "";
	    	while((riga = lettore.readLine()) != null) {
	    			//riga = lettore.readLine();
	    			//System.out.println(riga);
	    			cont++;
	    			ele += riga;
	    	}
	    	//int righe = cont-1;
	    	int righe = cont;
	    	//System.out.println("Le righe sono: "+righe);
	    	//System.out.println(ele); 	
	    	String[] x = null;
	    	if(ele.contains(";")) {
	    		x = ele.split(";");
	    		for(int i=0;i<righe*2;i++) {
	        		//System.out.println(x[i]);
	        	}
	    	}
	    	Cliente[] cliente = new Cliente[righe];
	    	int j=0;
	    	for(int i=0;i<cliente.length;i++) {
	    		Cliente c = new Cliente();
	    		c.nome = x[j];
	    		c.cognome = x[j+1];
	    		cliente[i] = c;
	    		j = j+2;
	    	}
	    	for(int i=0;i<cliente.length;i++) {
	    		Cliente c = cliente[i];
	    		System.out.println("Nome:"+ c.nome);
	    		System.out.println("Cognome :"+ c.cognome);
	    		System.out.println("");
	    	}
			return "Accesso consentito";
    	}
    	else {
    		System.out.println("Accesso Negato");
    		return "Accesso negato";
    	}
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
