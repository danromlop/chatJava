package ChatJava;


import java.awt.*;
import java.io.*;

import javax.swing.*;

import java.net.*;

public class ServidorChat  {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MarcoServidorChat mimarco=new MarcoServidorChat();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
	}	
}

class MarcoServidorChat extends JFrame implements Runnable{
	
	public MarcoServidorChat(){
		
		setBounds(1200,300,280,350);				
			
		JPanel milamina= new JPanel();
		
		milamina.setLayout(new BorderLayout());
		
		areatexto=new JTextArea();
		
		milamina.add(areatexto,BorderLayout.CENTER);
		
		add(milamina);
		
		setVisible(true);	
		//comenzamos hilo
		Thread miHilo = new Thread(this);
		
		miHilo.start();
	
		
		}	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ServerSocket miServidor = new ServerSocket(9998); //puerto a la escucha
			
			String nick,ip,mensaje;
			
			EnvioPaqueteDatos paqueteRecibido;
		
			//bucle infinito para recibir continuamente paquetes
			while (true) {
				Socket miSocket = miServidor.accept(); //acepta todas las conexiones que viajan por el socket
				
				ObjectInputStream flujoDatosEntrada = new ObjectInputStream(miSocket.getInputStream());
				
				paqueteRecibido = (EnvioPaqueteDatos) flujoDatosEntrada.readObject();				
				
				nick = paqueteRecibido.getNick();
				
				ip = paqueteRecibido.getIp();
				
				mensaje = paqueteRecibido.getTextoCliente();
				
				areatexto.append("\n" + "nick: " + nick + " Mensaje: " + mensaje + " IP: " + ip);
				
				Socket reenvioDestinatario = new Socket(ip, 9998);
				
				ObjectOutputStream paqueteReenvio = new ObjectOutputStream(reenvioDestinatario.getOutputStream());
				
				paqueteReenvio.writeObject(paqueteRecibido);
				
				reenvioDestinatario.close();
				
				miSocket.close();
				/*
				//flujo de entrada de datos
				DataInputStream flujoEntrada = new DataInputStream(miSocket.getInputStream());
				String mensajeTexto = flujoEntrada.readUTF();
				areatexto.append(mensajeTexto + "\n");*/
				
				
				
			}						
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private	JTextArea areatexto;

	
	
}
