/*
JView2 a command line client for MyWebProjct SOAP Web Service
Copyright (C) 2017-2018 Davide Sestili

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package it.dsestili.jview;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

import it.dsestili.mywebproject.ws.FileInfo;
import it.dsestili.mywebproject.ws.GenerateAndDownloadHashWSLoginServiceLocator;
import it.dsestili.mywebproject.ws.GenerateAndDownloadHashWSLoginSoapBindingStub;
import it.dsestili.mywebproject.ws.Result;

public class Main
{
	public static void main(String[] args)
	{
		if(args.length != 7)
		{
			System.out.println("Usage: param 1: endpoint, param 2: remote folder, param 3: algorithm, param 4: modeParam, param 5: userName, param 6: password, param 7: outputFile");
		}
		else
		{
			try 
			{
				String endpoint = args[0];
				String remoteFolder = args[1];
				String algorithm = args[2];
				String modeParam = args[3];
				String userName = args[4];
				String password = args[5];
				String outputFilePath = args[6];
				
				File outputFile = new File(outputFilePath);
				if(outputFile.exists())
				{
					System.out.println(outputFile.getAbsolutePath() + " already exist");
					return;
				}

				FileOutputStream fos = new FileOutputStream(outputFile);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				
				GenerateAndDownloadHashWSLoginServiceLocator serviceLocator = new GenerateAndDownloadHashWSLoginServiceLocator();
				GenerateAndDownloadHashWSLoginSoapBindingStub stub = new GenerateAndDownloadHashWSLoginSoapBindingStub(new URL(endpoint), serviceLocator);
				((org.apache.axis.client.Stub)stub).setTimeout(10800000);

				Result r = stub.generateAndDownloadHashLogin(remoteFolder, algorithm, modeParam, userName, password);
				if(r != null)
				{
					FileInfo[] infos = r.getResult();

					if(infos != null)
					{
						for(FileInfo info : infos)
						{
							String lineOfText = info.getHashCode() + " *" + info.getFileName() + "\n";
							bos.write(lineOfText.getBytes());
							System.out.print(lineOfText);
						}
					}
					else
					{
						System.out.println(infos);
					}
				}
				else
				{
					System.out.println(r);
				}
				
				bos.close();
			} 
			catch(Exception e) 
			{
				e.printStackTrace();
			} 
		}
	}
}
