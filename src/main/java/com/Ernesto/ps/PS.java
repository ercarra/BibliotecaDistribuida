package com.Ernesto.ps;

import java.io.File;
import java.util.Scanner;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

public class PS {
    public static void sendMessage(String msg) {

        try {
            String ipComputadora1="*";
            String ipComputadora2="*";
            ZContext context = new ZContext();
            ZMQ.Socket socket1 = context.createSocket(SocketType.REQ);
            socket1.connect("tcp://"+ipComputadora1+":5553");
            socket1.setReceiveTimeOut(2000);
            System.out.println("enviando...");
            socket1.send(msg.getBytes(ZMQ.CHARSET),0);

            byte[] recv = socket1.recv(0);
            if (recv != null) {
                System.out.println( new String(recv) );
            }else{
                ZMQ.Socket socket2 = context.createSocket(SocketType.REQ);
                socket2.connect("tcp://"+ipComputadora2+":5555");
                socket2.setReceiveTimeOut(6000);
                socket2.send(msg.getBytes(ZMQ.CHARSET),0);
    
                byte[] recv2 = socket2.recv(0);
                if (recv2 != null) {
                    System.out.println( new String(recv2) );
                }else{
                    System.out.println( "sistema caido");
                }
                socket2.close();
            }
            context.close();
            socket1.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            String solicitud;
            System.out.print("Ingrese requerimientos ");
            solicitud = sc.nextLine();
            Scanner input = new Scanner(new File(solicitud));
            long tiempoTotal = System.currentTimeMillis();
            while (input.hasNextLine()) {
                String line = input.nextLine();
                System.out.println(line);
                long startTime = System.currentTimeMillis();
                sendMessage(line);
                System.out.println(System.currentTimeMillis()-startTime );
            }
            System.out.println(System.currentTimeMillis()-tiempoTotal );
            input.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        
        // while(true){
        //     System.out.print("Ingrese peticion ");
        //     solicitud = sc.nextLine();
        //     sendMessage(solicitud);
            
        // }

    }
}