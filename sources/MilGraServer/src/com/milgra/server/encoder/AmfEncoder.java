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
	
	AmfEncoder class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080316

	Tasks of AmfEncoder
	
		- Encode data structures into raw bytes

**/

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import com.milgra.server.api.Wrapper;
import com.milgra.server.api.WrapperList;
import com.milgra.server.api.WrapperMap;


public class AmfEncoder 
{
	
	/**
	 * Amf encoded a wrapper
	 * @param wrapperX wrapper
	 * @return byte array
	 */
	
	public static byte [ ] encode ( Wrapper wrapperX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " AmfDecoder.encode " );
		
		if ( wrapperX.type.equals( Wrapper.MAP ) ) return encodeMap( wrapperX ); else
		if ( wrapperX.type.equals( Wrapper.NULL ) ) return encodeNull( wrapperX ); else
		if ( wrapperX.type.equals( Wrapper.LIST ) ) return encodeList( wrapperX ); else
		if ( wrapperX.type.equals( Wrapper.DOUBLE ) ) return encodeDouble( wrapperX ); else
		if ( wrapperX.type.equals( Wrapper.STRING ) ) return encodeString( wrapperX ); else
		if ( wrapperX.type.equals( Wrapper.BOOLEAN ) ) return encodeBoolean( wrapperX );
		
		return new byte [ 0 ];
		
	}
	
	/**
	 * Amf encodes a null value
	 * @param wrapperX wrapper
	 * @return byte array
	 */
	
	public static byte [ ] encodeNull ( Wrapper wrapperX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " AmfDecoder.encodeNull " );

		return new byte [ ] { 0x05 };
		
	}
	
	/**
	 * Amf encodes a double
	 * @param wrapperX wrapper
	 * @return byte array
	 */
	
	public static byte [ ] encodeDouble ( Wrapper wrapperX )
	{

		// System.out.println( System.currentTimeMillis( ) + " AmfDecoder.encodeDouble " );

		ByteBuffer result = ByteBuffer.allocate( 9 );
		
		result.put( ( byte ) 0x00 );
		result.putDouble( wrapperX.doubleValue );
		
		return result.array( );
		
	}
	
	/**
	 * Amf encodes a string
	 * @param wrapperX wrapper
	 * @return byte array
	 */
	
	public static byte [ ] encodeString ( Wrapper wrapperX )
	{

		// System.out.println( System.currentTimeMillis( ) + " AmfDecoder.encodeString " );

		int summa = wrapperX.stringValue.length( );
		ByteBuffer result;
		
		if ( summa > 65535 )
		{
			
			result = ByteBuffer.allocate( summa + 5 );
			result.put( ( byte ) 0x0C );
			result.put( ( byte ) ( summa >> 24 ) );
			result.put( ( byte ) ( summa >> 26 ) );
			result.put( ( byte ) ( summa >> 8 ) );
			result.put( ( byte ) summa );
			
		}
		else
		{
			
			result = ByteBuffer.allocate( summa + 3 );
			result.put( ( byte ) 0x02 );
			result.put( ( byte ) ( summa >> 8 ) );
			result.put( ( byte ) summa );
			
		}		
		
		result.put( wrapperX.stringValue.getBytes( ) ); 
		
		return result.array( );
		
	}
	
	/**
	 * Amf encodes a boolean
	 * @param wrapperX wrapper
	 * @return byte array
	 */
	
	public static byte [ ] encodeBoolean ( Wrapper wrapperX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " AmfDecoder.encodeBoolean " );

		ByteBuffer result = ByteBuffer.allocate( 2 );
		
		result.put( ( byte ) 0x01 );
		result.put( ( byte ) ( wrapperX.booleanValue ? 0x01 : 0x00 ) );
		
		return result.array( );
		
	}
	
	/**
	 * Amf encodes an arraylist
	 * @param wrapperX
	 * @return
	 */
	
	public static byte [ ] encodeList ( Wrapper wrapperX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " AmfDecoder.encodeList " );
		
		WrapperList list = wrapperX.listValue; 
		ByteArrayOutputStream result = new ByteArrayOutputStream( );

		int summa = list.size( );

		try
		{
			
			// write type

			result.write( 0x0A );
			
			// write length
			
			result.write( summa >> 24 );
			result.write( summa >> 16 );
			result.write( summa >> 8 );
			result.write( summa );
			
			// write values
			
			for ( Wrapper wrapper : list ) result.write( encode( wrapper ) );
			
		}
		catch ( IOException exception ) { exception.printStackTrace( ); }
		
		return result.toByteArray( );
		
	}

	// amf object finish
	
	public static byte [ ] finish = new byte[ ] { 0x00 , 0x00 , 0x09 };
	
	/**
	 * Amf encodes a string
	 * @param wrapperX wrapper
	 * @return byte array
	 */
	
	public static byte [ ] encodeKey ( Wrapper wrapperX )
	{

		// System.out.println( System.currentTimeMillis( ) + " AmfDecoder.encodeString " );

		int summa = wrapperX.stringValue.length( );
		
		ByteBuffer result = ByteBuffer.allocate( summa + 2 );
		
		result.put( ( byte ) ( summa >> 8 ) );
		result.put( ( byte ) summa );
		result.put( wrapperX.stringValue.getBytes( ) ); 
		
		return result.array( );
		
	}
	/**
	 * Amf encodes a map
	 * @param wrapperX wrapper
	 * @return byte array
	 */
	
	public static byte [ ] encodeMap ( Wrapper wrapperX )
	{
		
		WrapperMap map = wrapperX.mapValue; 
		ByteArrayOutputStream result = new ByteArrayOutputStream( );
		
		try
		{
			
			// write type

			result.write( 0x03 );
			
			// write key - value pairs
			
			for ( String key : map.keySet( ) )
			{
				
				Wrapper value = map.get( key );
				
				result.write( encodeKey( new Wrapper( key ) ) );
				result.write( encode( value ) );
				
			}
			
			// write ending

			result.write( finish );

		}
		catch ( IOException exception ) { exception.printStackTrace( ); }
				
		return result.toByteArray( );
		
	}

}
