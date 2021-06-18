/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 *
 * @author Dani
 */
public class FtpClient {
    private String server="ftp.site4now.net";
    private int port=21;
    private String user="danielike-0022";
    private String password="@incrad12345";
    private FTPClient ftp;
    
    public void open() throws IOException{
        ftp = new FTPClient();
        ftp.setControlEncoding("UTF-8");
        ftp.setDefaultTimeout(1000000);
        ftp.setDataTimeout(1000000);
        ftp.connect(server, port);
        int reply = ftp.getReplyCode();
        if(ftp.login(user, password)){
            System.out.println(ftp.getReplyString());
            //se obtiene la respuesta del server y se accede
            if(FTPReply.isPositiveCompletion(reply)){
                           System.out.println("Conectado a ftpserver");
            }else{
                System.err.println("Error al conectar a FTP server");
            }
        }
    }
    public void close() throws IOException{
        ftp.logout();
        ftp.disconnect();
        
    }
    /**
     * sube un fichero al server
     * @param path Direccion del fichero + archivo (c:/users/archivo.jpg)
     * @param fichero Nombre del fichero y su extension (imagen.jpg)
     * @return true: todo correcto // false: hay algún error
     */
    public boolean subirFichero(String path, String fichero){
        //InputStream is;
        boolean ficheroSubido = false;
        try {
            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.setFileTransferMode(FTP.BINARY_FILE_TYPE);
        } catch (IOException ex) {
            Logger.getLogger(FtpClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        try (InputStream is = new BufferedInputStream(new FileInputStream(path))){
            //cogemos el fichero de su ruta
            //subimos el fichero
            ficheroSubido = ftp.storeFile(fichero, is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ficheroSubido;
    }
    
}
