package testeSFTP;

import java.io.File;
import java.util.Vector;
 
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class testeDownlad {
 
    public static void main(String[] args)  {
    	ChannelSftp channelSftp = null;
        Session session = null;
        Channel channel = null;
        String pathSeparator = "/";
        String host = "10.0.0.1"; // SFTP Host Name or SFTP Host IP Address
        String port = "22"; // SFTP Port Number
        String user = "test"; // User Name
        String password = "passwd"; // Password
        String remoteDir = "/home/test/javatest"; // Source Directory on SFTP server
        String localDir = "D:\\Java\\eclipse\\eclipse-workspace\\testeSFTP\\a"; // Local Target Directory
        int contador = 0;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            session.setPassword(password);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect(); // Create SFTP Session
            channel = session.openChannel("sftp"); // Open SFTP Channel
            channel.connect();
            channelSftp = (ChannelSftp) channel;
            channelSftp.cd(remoteDir); // Change Directory on SFTP Server
            
            @SuppressWarnings("unchecked")
			Vector<ChannelSftp.LsEntry> fileAndFolderList = channelSftp.ls(remoteDir); // Let list of folder content
            //Iterate through list of folder content
            for (ChannelSftp.LsEntry item : fileAndFolderList) {
            	if (!item.getAttrs().isDir()) { // Check if it is a file (not a directory).
            		try {
            			new File(localDir + pathSeparator + item.getFilename());
            			channelSftp.get(remoteDir + pathSeparator + item.getFilename(),
                                localDir + pathSeparator + item.getFilename()); // Download file from source (source filename, destination filename).
                                System.out.println("Download do arquivo " + item.getFilename() + " realizado com sucesso.");    
                                contador++;
            		} catch (SftpException e) {
            			e.printStackTrace();
            		}
            	}   	
            }
            System.out.println("Número de arquivos baixados: " + contador );
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (channelSftp != null)
                channelSftp.disconnect();
            if (channel != null)
                channel.disconnect();
            if (session != null)
                session.disconnect();
 
        }
 
    }
}
