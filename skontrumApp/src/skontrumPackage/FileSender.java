package skontrumPackage;

import java.io.File;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

public class FileSender {
/*
 plikoZapisywacz.println("@echo off");
 plikoZapisywacz.println("if not exist y: net use y: \\192.168.0.252\\skontrum /user:skontrum skontrum");
 plikoZapisywacz.println("\r\n");
 plikoZapisywacz.println("copy *.txt y:");*/	
	
	public  void TimeToBuildAndATimeToDestroy(){
		try {
			PrintWriter plikoZapisywacz = new PrintWriter("FileSender.cmd");			
				plikoZapisywacz.println("@echo off");
				plikoZapisywacz.println("if not exist y: net use y: \\\\192.168.0.252\\mak\\skontrum /user:skontrum skontrum");
				plikoZapisywacz.println("\r\n");
				plikoZapisywacz.println("copy *.txt y:");
				plikoZapisywacz.println("net use y: /d /y");
				plikoZapisywacz.close();
		    //Process runBatch = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "FileSender.cmd");//use the protoclhandler
			Process runBatch = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "FileSender.cmd");
			synchronized (runBatch) {
				//Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "FileSender.cmd");
				//runBatch.waitFor();//Waits the process to terminate
				runBatch.wait(5000); //czekaj 5 sekund
			    if (runBatch.exitValue() == 0){			        
			        JOptionPane.showMessageDialog(null, "Pliki skopiowane na serwer!:-)");
			        usunBatch();
			    }
			}
		}catch(Exception e) {//Process.waitFor() can throw InterruptedException
			e.printStackTrace();
		} 
}; 

	public void usunBatch(){
		try{
			File batch = new File("FileSender.cmd");
			if(batch.delete())
				System.out.println("Batch usuniety!:)");				
			else
				System.out.println("Nie udalo sie usunac batcha!");
		}catch(Exception fileErr){
			System.out.println("Cos poszlo nie tak...");
		}		
	};
}
