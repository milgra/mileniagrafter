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
   Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA. 
   
*/ 

package com.milgra.server.encoder;

/**
	
	AmfDecoder class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080316

	Tasks of Server
	
		- Decode raw byte data into data structures

**/

import java.io.IOException;
import java.nio.ByteBuffer;

import com.milgra.server.Library;
import com.milgra.server.api.Wrapper;
import com.milgra.server.api.WrapperMap;
import com.milgra.server.api.WrapperList;


public class AmfDecoder 
{
	
	/**
	 * amf decodes a bytebuffer
	 * @param bufferX the bytebuffer containing raw amf data
	 * @return wrapper containing decoded data
	 * @throws IOException decode exception
	 */
	
	public static Wrapper decode ( ByteBuffer bufferX , WrapperList referencesX ) throws IOException
	{
		
		// System.out.println( System.currentTimeMillis( ) + " AmfDecoder.decode( " + bufferX + " )");
		
		byte type = bufferX.get( );
		
		switch ( type )
		{
		
			// 0x00 - AS Number
			// 0x01 - AS Boolean
			// 0x02 - AS String
			// 0x03 - AS Object
			// 0x05 - AS null
			// 0x06 - AS undefined
			// 0x07 - AS reference
			// 0x08 - AS mixed array
			// 0x0A - AS indexed array
			// 0x0C - AS Long String
			
			case 0x00 : return new Wrapper( getDouble( bufferX ) );
			case 0x01 : return new Wrapper( getBoolean( bufferX ) );
			case 0x02 : return new Wrapper( getString( bufferX ) );
			case 0x03 : return new Wrapper( getMap( bufferX , referencesX ) );
			case 0x05 : return new Wrapper( );
			case 0x06 : return new Wrapper( );
			case 0x07 : return getReference( bufferX , referencesX );
			case 0x08 : return new Wrapper( getMixed( bufferX , referencesX ) ); 
			case 0x0A : return new Wrapper( getList( bufferX , referencesX ) );
			case 0x0C : return new Wrapper( getLongString( bufferX ) );
			default   :	throw new IOException( Library.AMFDEX + type );
			
		}
		
	}
	
	/**
	 * Decodes a 64bit double precision floating pont double
	 * @param bufferX
	 * @return double
	 */
	
	public static double getDouble ( ByteBuffer bufferX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " AmfDecoder.getDouble( " + bufferX + " )");
		return bufferX.getDouble( );
		
	}
	
	/**
	 * Decodes a boolean 
	 * @param bufferX
	 * @return
	 */
	
	public static boolean getBoolean ( ByteBuffer bufferX )
	{

		// System.out.println( System.currentTimeMillis( ) + " AmfDecoder.getBoolean( " + bufferX + " )");
		return ( bufferX.get( ) & 0xFF ) == 0 ? false : true;
		
	}
	
	/**
	 * Decodes a string 
	 * @param bufferX bytebuffer
	 * @return the string
	 */
	
	public static String getString ( ByteBuffer bufferX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " AmfDecoder.getString( " + bufferX + " )");
		
		int size = ( bufferX.get( ) & 0xFF ) << 8 | 
				   ( bufferX.get( ) & 0xFF );
		
		byte [ ] bytes = new byte [ size ];
		bufferX.get( bytes );

		return new String( bytes );

	}
	
	/**
	 * Decodes a string 
	 * @param bufferX bytebuffer
	 * @return the string
	 */
	
	public static String getLongString ( ByteBuffer bufferX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " AmfDecoder.getString( " + bufferX + " )");
		
		int size = ( bufferX.get( ) & 0xFF ) << 24 | 
				   ( bufferX.get( ) & 0xFF ) << 16 |
				   ( bufferX.get( ) & 0xFF ) << 8 | 
				   ( bufferX.get( ) & 0xFF );
		
		byte [ ] bytes = new byte [ size ];
		bufferX.get( bytes );

		return new String( bytes );

	}
	
	/**
	 * Checks for object finish byte string
	 * @param bufferX bytebuffer
	 * @return boolean
	 */
	
	public static boolean getFinish ( ByteBuffer bufferX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " AmfDecoder.getFinish( " + bufferX + " )");

		int summa = ( bufferX.get( ) & 0xFF ) << 16 | 
					( bufferX.get( ) & 0xFF ) << 8 | 
					( bufferX.get( ) & 0xFF );
		
		if ( summa == 9 ) return true;
		
		bufferX.position( bufferX.position( ) - 3 );
		return false;
		
	}
	
	/**
	 * Decodes an object map
	 * @param bufferX bytebuffer
	 * @return wrappermap
	 * @throws IOException
	 */
	
	public static WrapperMap getMap ( ByteBuffer bufferX , WrapperList referencesX ) throws IOException
	{
		
		// System.out.println( System.currentTimeMillis( ) + " AmfDecoder.getMap( " + bufferX + " )");
		
		try
		{
				
			WrapperMap result = new WrapperMap( );
			
			// check for finish
			
			while ( !getFinish( bufferX ) )
			{
				
				// key - object property name
				// value - value related to property
				
				String key = getString( bufferX );
				Wrapper value = decode( bufferX , referencesX );
				
				result.put( key , value );
				
			}

			referencesX.add( new Wrapper( result ) );
			
			return result;
			
		}
		catch ( IOException exception ) { throw ( exception );	}
		
	}
	
	/**
	 * Decodes a wrapperlist
	 * @param bufferX bytebuffer
	 * @return wrapperlist
	 * @throws IOException
	 */	
	
	public static WrapperList getList ( ByteBuffer bufferX , WrapperList referencesX ) throws IOException
	{
			
		// System.out.println( System.currentTimeMillis( ) + " AmfDecoder.getList( " + bufferX + " )");

		try
		{
			
			// index - actual position
			// summa - list length

			int index = 0;
			int summa = ( bufferX.get( ) & 0xFF ) << 24 | 
			   		    ( bufferX.get( ) & 0xFF ) << 16 | 
			   		    ( bufferX.get( ) & 0xFF ) << 8 | 
			   		    ( bufferX.get( ) & 0xFF );
			
			WrapperList result = new WrapperList( );
			
			while ( index < summa )
			{
				
				Wrapper value = decode( bufferX , referencesX );
				result.add( value );
				index++;
				
			}
			
			referencesX.add( new Wrapper( result ) );

			return result;

		}
		catch ( IOException exception ) { throw ( exception ); }
		
	}
	
	/**
	 * Decodes a mixed array from bytebuffer
	 * @param bufferX bytebuffer
	 * @return wrappermap
	 * @throws IOException
	 */
	
	public static WrapperList getMixed ( ByteBuffer bufferX , WrapperList referencesX ) throws IOException
	{
		
		// System.out.println( System.currentTimeMillis( ) + " AmfDecoder.getMixed( " + bufferX + " )");
		
		try 
		{
			
			// index - actual position
			// summa - list length

			int index = 0;
			int summa = ( bufferX.get( ) & 0xFF ) << 24 | 
			   		    ( bufferX.get( ) & 0xFF ) << 16 | 
			   		    ( bufferX.get( ) & 0xFF ) << 8 | 
			   		    ( bufferX.get( ) & 0xFF );
			
			WrapperList result = new WrapperList( );

			while ( !getFinish( bufferX ) && index < summa )
			{
				
				// read key
				// read value
				
				getString( bufferX );
				Wrapper value = decode( bufferX , referencesX );
				
				result.add( value );
				index++;
				
			}
			
			referencesX.add( new Wrapper( result ) );

			return result;

		}
		catch ( IOException exception ) { throw ( exception ); }

	}
	
	/**
	 * Returns referenced object
	 * @return Wrapper containing referenced object
	 */
	
	public static Wrapper getReference ( ByteBuffer bufferX , WrapperList referencesX ) throws IOException
	{
	
		// System.out.println( System.currentTimeMillis( ) + " AmfDecoder.getReference " + referencesX.size( ) );
		
		int index = ( bufferX.get( ) & 0xFF ) << 8 | 
		   			( bufferX.get( ) & 0xFF );
		
		if ( referencesX.size( ) == 0 ) throw new IOException( Library.AMFDEX + "ref" );
		if ( referencesX.size( ) >= index ) return referencesX.get( index - 1 ); 
		else throw new IOException( Library.AMFDEX + "ref" );
		
	}

}
