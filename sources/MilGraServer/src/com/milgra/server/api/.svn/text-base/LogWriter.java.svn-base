/*

   Milenia Grafter Server
  
   Copyright (c) 2007-2008 by Milan Toth. All rights reserved.
  
   This program is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public License
   of the License, or (at your option) any later version.
  
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
  
   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundataion, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA. 
   
*/ 

package com.milgra.server.api;

/**
	
	LogWriter class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080316

	Tasks of LogWriter
	
		- creates a file, and appends incoming texts to it

**/

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;


public class LogWriter 
{
	
	public BufferedWriter writer;
	public boolean active;
	
	/**
	 * LogWriter constructor
	 * @param fileNameX
	 */
	
	public LogWriter ( String fileNameX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " LogWriter.construct " + fileNameX );
		
		try
		{
			
			File file = new File( fileNameX );
			if ( !file.exists( ) ) file.createNewFile( );
			writer = new BufferedWriter( new FileWriter( file ) );
			active = true;
			
		}
		catch ( IOException exception ) { exception.printStackTrace( ); }
		
	}
	
	/**
	 * Writes a log string
	 * @param textX
	 */
	
	public void log ( String textX )
	{
		
		// System.out.println( "Logwriter.log " + textX );
		
		if ( active )
		{
		
			try
			{
				
				writer.append( textX + "\n" );
				writer.flush( );
				
			}
			catch ( IOException exception ) { exception.printStackTrace( ); }
		
		}
		
	}
	
	/**
	 * Closes writer
	 */
	
	public void close ( )
	{
		
		try
		{
			
			writer.close( );
			
		}
		catch ( IOException exception ) { exception.printStackTrace( ); }
		
	}

}
