package skontrumPackage;

import java.io.File;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

public class FileSender {
/*
 plikoZapisywacz.println("@echo off");
 plikoZapisywacz.println("if not exist y: net use y: \\someIP\someShare /user:someUser somePass");
 plikoZapisywacz.println("\r\n");
 plikoZapisywacz.println("copy *.txt y:");*/	
	
	public  void TimeToBuildAndATimeToDestroy(){
		try {
			PrintWriter plikoZapisywacz = new PrintWriter("FileSender.cmd");			
				plikoZapisywacz.println("@echo off");
				plikoZapisywacz.println("if not exist y: net use y: \\\\\\someIP\\someShare /user:someUser somePass");
				plikoZapisywacz.println("\r\n");
				plikoZapisywacz.println("copy *.txt y:");
				plikoZapisywacz.println("net use y: /d /y");
				plikoZapisywacz.close();
		    //Process runBatch = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "FileSender.cmd");//use the protoclhandler
			Process runBatch = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "FileSender.cmd");
			synchronized (runBatch) {
				//Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "FileSender.cmd");
				//runBatch.waitFor();//Waits the process to terminate
				runBatch.wait(5000); //wat 'bout 5 secs
			    if (runBatch.exitValue() == 0){			        
			        JOptionPane.showMessageDialog(null, "Files copied to a NAS server!");
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
				System.out.println("Batch deleted:)");				
			else
				System.out.println("Batch couldn't be deleted:[!");
		}catch(Exception fileErr){
			System.out.println("Smth went wrong O_o");
		}		
	};
}
