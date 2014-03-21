/*

	Milenia Grafter Server
	
	Copyright (c) 2007-2008 by Milan Toth. All rights reserved.
	
	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

*/

package application;

/**
	
	ConfigWriter class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080316
	
	Tasks of Configwriter 
	
		- write a default config xml

**/

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigWriter 
{
	
	/**
	 * Creates admin dir and admin.xml if not exists
	 * @param urlX file name
	 */
	
	public ConfigWriter ( String nameX )
	{
		
		//System.out.println( System.currentTimeMillis() + " MilGraAdmin Application.createConfig" );
	
		File adminDir = new File( "admin" );
		if ( !adminDir.exists( ) )
		{
			
			adminDir.mkdir( );
			File adminXML = new File( nameX );
			
			try
			{
				
				adminXML.createNewFile( );
				
				FileWriter writer = new FileWriter( adminXML );
				writer.append( "<!DOCTYPE root>\n<root>\n" );
				writer.append( "<enabled>\n<ip>*</ip>\n</enabled>\n" );
				writer.append( "<accesslogging>true</accesslogging>\n" );
				writer.append( "<graphslogging>true</graphslogging>\n" );
				writer.append( "<username>admin</username>\n" );
				writer.append( "<password>admin</password>\n" );
				writer.append( "</root>" );
						
				writer.flush( );
				writer.close( );
				
			}
			catch ( IOException exception ) { exception.printStackTrace( );	}
			
		}
		
	}

}
