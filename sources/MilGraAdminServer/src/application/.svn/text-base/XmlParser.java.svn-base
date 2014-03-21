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
	
	XmlParser class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080316
	
	Tasks of XmlParser 
	
		- parse a simplified xml

**/

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class XmlParser 
{

	public ArrayList < String > nodes;
	public ArrayList < String > texts;
	
	/**
	 * Creates a new ConfigReader instance, and loads the given file
	 * @param fileNameX the xml's name
	 */
	
	public XmlParser ( String fileNameX )
	{
		
		// System.out.println( System.currentTimeMillis() + " ConfigReader.construct " + fileNameX );
		
		try
		{

			File file = new File( fileNameX );
			FileReader fileReader = new FileReader( file );
			BufferedReader reader = new BufferedReader( fileReader );
			
			parseFile( reader );
			
		}
		catch ( FileNotFoundException exception ) { exception.printStackTrace( ); }		
	}
	
	/**
	 * Parses the file
	 * @param readerX BufferedReader
	 */
	
	public void parseFile ( BufferedReader readerX )
	{
		
		// System.out.println( System.currentTimeMillis() + " ConfigReader.parseFile " + readerX );
		
		texts = new ArrayList < String > ( );
		nodes = new ArrayList < String > ( );
		
		int code;
		String value = "";
		
		try
		{
			
			do
			{
			
				code = readerX.read( );
				
				switch ( code )
				{
				
					// <
					// >
				
					case 60 : texts.add( value ); value = ""; break;
					case 62 : nodes.add( value ); value = ""; break;
					default : value += ( char ) code; break;
				
				}
				
			}
			while ( code != -1 );
			
		}
		catch ( IOException exception ) { exception.printStackTrace( ); }
		
		texts.remove( 0 );
		texts.add( null );
		
	}
	
	/**
	 * Returns the wanted node value
	 * @param pathX path in xml
	 * @param indexX index of the wanted node
	 * @return String
	 */
	
	public String getValue ( String pathX , int indexX )
	{
		
		// System.out.println( System.currentTimeMillis() + " ConfigReader.getValue " + pathX + " " + indexX );

		int count = 0;
		int depth = 0;
		int index = 0;
		
		String [ ] parts = pathX.split( "\\." );
		
		// going through nodes
		
		while ( count < nodes.size( ) )
		{
			
			String node = nodes.get( count );
			
			if ( node.equals( parts[depth ] ) ) 
			{
				
				if ( depth == ( parts.length - 1 ) )
				{
				
					if ( index == indexX ) return texts.get( count );
					else ++index;
					
				} 
				else ++depth;
				
			}
			
			++count;
			
		}

		return null;
		
	}
	
	/**
	 * Returns node count
	 * @param pathX path in xml
	 * @return count
	 */
	
	public int getCount ( String pathX )
	{
		
		// System.out.println( System.currentTimeMillis() + " ConfigReader.getCount " + pathX );

		int count = 0;
		int depth = 0;
		String [ ] parts = pathX.split( "\\." );
		
		// going through nodes
		
		while ( count < nodes.size( ) )
		{
			
			String node = nodes.get( count );
			
			if ( node.equals( parts[depth ] ) ) 
			{
				
				if ( depth == ( parts.length - 1 ) )
				{
					
					return calculateCount( count );
					
				} 
				else ++depth;
				
			}
			
			++count;
			
		}

		return 0;
	}
	
	/**
	 * Calculates child count
	 * @param indexX starting index
	 * @return child count
	 */
	
	public int calculateCount ( int indexX )
	{
		
		// System.out.println( System.currentTimeMillis() + " ConfigReader.calculateCount " + indexX );
		
		int depth = 0;
		int count = indexX;
		int child = 0;
		
		do
		{
			
			String actNode = nodes.get( count );
			
			if ( actNode.charAt( 0 ) == "/".charAt( 0 ) )
			{
				
				--depth;
				
			}
			else 
			if ( actNode.charAt( actNode.length() - 1 ) != "/".charAt( 0 ) )
			{
				
				++depth;
				++child;
				
			}
			
			++count;
			
		}
		while ( count < nodes.size( ) && depth > -1 );
		
		return child;
			
	}

}
