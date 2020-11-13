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
import it.unict.sistemicloud.microserviceA.DTO.Richiesta;
import it.unict.sistemicloud.microserviceA.controller.Input;
import it.unict.sistemicloud.microserviceA.DTO.Transazione;
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
    	return "Hello im microserver A. This is API GATEWAY";
    }
	
	String tracciamento="";
	@GetMapping(value = "/createTrackid", produces = MediaType.APPLICATION_JSON_VALUE)
    public Transazione track_id(String sb) {
		Transazione transazione = new Transazione();
    	transazione.id_transazione = id_transazione();
    	transazione.microservizio = "A";
    	transazione.tracciamento = "["+transazione.id_transazione+" , "+transazione.microservizio+" ]";
    	tracciamento += transazione.tracciamento+","+'\n';
    	System.out.println(tracciamento);
    	return transazione;
    }

}