package it.unict.sistemicloud.microserviceA.DTO;

import java.io.Serializable;

public class Packet implements Serializable{
	public String nome;
	public String cognome;
	public String id_transazione;
	public String microservizio;
	public String token;
}