package it.unict.sistemicloud.microserviceB.controller;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.SecureRandom;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import it.unict.sistemicloud.microserviceB.DTO.Cliente;
import it.unict.sistemicloud.microserviceB.DTO.Numero;
import it.unict.sistemicloud.microserviceB.DTO.Ordine;
import it.unict.sistemicloud.microserviceB.DTO.Pizza;
import it.unict.sistemicloud.microserviceB.DTO.Richiesta;
import it.unict.sistemicloud.microserviceB.DTO.Tokens;
import it.unict.sistemicloud.microserviceB.DTO.Transazione;
import it.unict.sistemicloud.microserviceB.controller.Input;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.util.Scanner;

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
	
	boolean accesso = false;
	
	
	@GetMapping(value = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
    public String hello(){
		System.out.println("Ciao");
    	return "Hello im microserver B";
	}  
}
