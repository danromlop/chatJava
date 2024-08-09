package ChatJava;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import javax.swing.*;

public class ClienteChat {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MarcoClienteChat mimarco=new MarcoClienteChat();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}


class MarcoClienteChat extends JFrame{
	
	public MarcoClienteChat(){
		
		setBounds(600,300,280,350);
				
		LaminaClienteChat milamina=new LaminaClienteChat();
		
		add(milamina);
		
		setVisible(true);		
		
		}	
	
}

//runnable para que pueda ejecutar continuamente la funcion para estar en escucha
class LaminaClienteChat extends JPanel implements Runnable {
	
	public LaminaClienteChat(){	
		
		nick = new JTextField(5);
		
		add(nick);
						
		JLabel cliente=new JLabel("--  CHAT  --");
		
		add(cliente);
		
		ip = new JTextField(5);
		
		add(ip);
		
		areaChat = new JTextArea(12,20);
		
		add(areaChat);
		
		campo1=new JTextField(20);
	
		add(campo1);		
	
		miboton=new JButton("Enviar");	
	
		miboton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//System.out.println(campo1.getText());

				try {
					
					// Creacion de socket (via de comunicacion)
					Socket miSocket = new Socket("127.0.0.1", 9998);
					
					EnvioPaqueteDatos datos = new EnvioPaqueteDatos();
					
					datos.setNick(nick.getText());
					datos.setIp(ip.getText());
					datos.setTextoCliente(campo1.getText());
					
					ObjectOutputStream flujoSalidaPaquete = new ObjectOutputStream(miSocket.getOutputStream());
					
					//enviamos por el flujo de salida nuestros datos
					flujoSalidaPaquete.writeObject(datos);
					
					miSocket.close();
										
									
										
					
					 
					//Creacion del flujo de datos
					/*DataOutputStream flujoSalida = new DataOutputStream(miSocket.getOutputStream());
					
					//
					flujoSalida.writeUTF(campo1.getText());
					
					flujoSalida.close();*/
					
					
					
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		
		add(miboton);	
		
		//comenzamos hilo
		Thread miHilo = new Thread(this);
				
		miHilo.start();
			
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ServerSocket escuchaCliente = new ServerSocket(9998);
			
			Socket cliente;
			
			EnvioPaqueteDatos paqueteRecibido;
			
			while(true) {
				
				cliente = escuchaCliente.accept();
				
				ObjectInputStream flujoEntrada = new ObjectInputStream(cliente.getInputStream());
				
				paqueteRecibido = (EnvioPaqueteDatos) flujoEntrada.readObject();
				
				areaChat.append("\n" + paqueteRecibido.getNick() + ": " + paqueteRecibido.getTextoCliente());
				
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private JTextField campo1;	
	
	private JTextField nick;	
	
	private JTextField ip;	
	
	private JButton miboton;
	
	private JTextArea areaChat;


	
}

	
// al implementar serializable la clase es susceptible de serializarse
class EnvioPaqueteDatos implements Serializable{
	
	
	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getTextoCliente() {
		return textoCliente;
	}

	public void setTextoCliente(String textoCliente) {
		this.textoCliente = textoCliente;
	}

	private String nick, ip, textoCliente;
}



