package servidorpoderoso;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorPoderoso implements Runnable {

    Thread ServThread = new Thread(this);
    String ipCliente = "";
    int contador = 1, conta = 0;

    public ServidorPoderoso() {

        // ServThread.start();
    }

    public static void main(String[] args) {
        ServidorPoderoso s = new ServidorPoderoso();
        s.ServThread.start();

    }

    @Override
    public void run() {

        System.out.println("Listening....");

        try {
            ServerSocket servidor = new ServerSocket(8080);

            while (true) {

                Socket miscocket = servidor.accept();

                ipCliente = miscocket.getInetAddress().getHostName();

                DataInputStream nombre = new DataInputStream(miscocket.getInputStream());
                String nombreRuta = nombre.readUTF();

                DataInputStream dis = new DataInputStream(miscocket.getInputStream());
                String nombreArchivo = dis.readUTF().toString();
                System.out.println("ruta: " + nombreArchivo);

                Date fecha = new Date();
                File car = new File("Origen");
                
                if (!car.exists()) {

                    car.mkdirs();
                }

                int tam = dis.readInt();
                System.out.println("Recibiendo Archivo " + nombreArchivo);
                // Creamos flujo de salida, este flujo nos sirve para
                // indicar donde guardaremos el archivo

                String c = car + "/" + nombreArchivo;
                FileOutputStream fos = new FileOutputStream(c);

                BufferedOutputStream out = new BufferedOutputStream(fos);
                BufferedInputStream in = new BufferedInputStream(miscocket.getInputStream());
                
                //codigo para leer datos del archivo.

                byte[] buffer = new byte[tam];
                
                for (int i = 0; i < buffer.length; i++) {
                    buffer[i] = (byte) in.read();
                }
                
                
                out.write(buffer);//escribir el archivo

                out.flush();
                in.close();
                out.close();
                miscocket.close();
                System.out.println("Archivo recibido " + nombreArchivo);
                //actualizacion(nombreArchivo);

            }

        } catch (IOException ex) {
            Logger.getLogger(ServidorPoderoso.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void actualizacion(String nombre) {

        try {
            String m = null;
            String s = "Actualizacion";
            m = ("\n Se realizo un respaldo en  con el archivo: " + nombre + " en la fecha de : " + new Date());

            File f = new File(s);

            FileWriter fw = new FileWriter(f, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.append(m);
            pw.close();
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(ServidorPoderoso.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void funcionesCliente(){
        //copia de las funciones de cliente de la clase main
        //deben funcionar mandando a la ip que se detectó según el el cliente que ha enviado.
    }
    
    public void comparacion(){
        //compara el arraylist de las rutas local con el que llegó
    }
    
    public void enviarComparacion(){
        //Debe enviar un arraylist al cliente para que éste haga su comparación
        
    }
}
