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
	
	Encoder class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080316

	Tasks of Encoder
	
		- Generic encode/decode tasks

**/

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import com.milgra.server.Library;
import com.milgra.server.api.Wrapper;
import com.milgra.server.api.WrapperList;


public class Encoder 
{
		
	/**
	 * Decodes an invoke
	 * @param bytesX byte array
	 * @return wrapperlist
	 */
	
	public static WrapperList decode ( byte [ ] bytesX ) throws IOException
	{

		// System.out.println( System.currentTimeMillis( ) + "Encoder.decode " + bytesX.length );
		
		WrapperList result = new WrapperList( );

		try
		{

			WrapperList references = new WrapperList( );
			ByteBuffer buffer = ByteBuffer.wrap( bytesX );
			while ( buffer.hasRemaining( ) ) result.add( AmfDecoder.decode( buffer , references ) );

		}
		catch ( IOException exception ) { throw ( exception ); }
		
		return result;
		
	}
	
	/**
	 * Encodes an invoke
	 * @param listX wrapperlist
	 * @return byte array
	 */
	
	public static byte [ ] encode ( WrapperList listX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + "Encoder.encode " );

		ByteArrayOutputStream result = new ByteArrayOutputStream( );
		
		try
		{

			for ( Wrapper wrapper : listX )
			{
				
				byte [ ] byteValue = AmfEncoder.encode( wrapper );
				result.write( byteValue );
				
			}

		}
		catch ( IOException exception ) 
		{ 
			
			System.out.println( Library.AMFDEX ); 
			exception.printStackTrace( ); 
			
		}
		
		return result.toByteArray( );
		
	}
	
	/**
	 * Concatenates byte arrays into one byte array
	 * @param arraysX byte arrays
	 * @return byte array
	 */
	
	public static byte [ ] concatenate ( byte [ ] ... arraysX )
	{
	
		// System.out.println( System.currentTimeMillis( ) + "Encoder.concatenate " );

		ByteArrayOutputStream result = new ByteArrayOutputStream( );
		
		try
		{

			for ( byte [ ] array : arraysX ) result.write( array );
			
		}
		catch ( IOException exception ) { exception.printStackTrace( ); }
		
		return result.toByteArray( );
		
	}
	
	/**
	 * Converts bytes to an unsigned integer
	 * @param bytesX
	 * @return
	 */
	
	public static int bytesToInt ( byte [ ] bytesX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + "Encoder.bytesToInt " + bytesX );
		
		int result = 0;
		int actual = 0;
		int offset = bytesX.length * 8;
		
		do
		{
		
			offset -= 8;
			result += ( bytesX[ actual ] & 0xFF ) << offset;
			actual ++;
			
		}
		while ( offset > 0 );
		
		return result;
		
	}
	
	/**
	 * Converts unsigned integer to bytes on given length
	 * @param valueX integer value
	 * @param lengthX byte array length
	 * @return byte array
	 */
	
	public static byte [ ] intToBytes ( long valueX , int lengthX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + "Encoder.intToBytes " + valueX + " " + lengthX );
		
		byte [ ] result = new byte [ lengthX ];
		int offset = lengthX * 8;
		int actual = 0;
		
		do
		{
			
			offset -= 8;
			result[ actual ] = ( byte ) ( valueX >> offset );
			actual ++ ;
			
		}
		while ( offset > 0 );
		
		return result;
		
	}
	
	/**
	 * Prints a byte array as hexa digits
	 * @param bytes byte array
	 * @return string representing hexa view
	 */
	
	public static String getHexa ( byte [ ] bytesX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + "Encoder.getHexa " + bytesX );

		int counter = 1;
		String result = " ";
		
		for ( byte actual : bytesX )
		{
			
			result += getHexaBits( ( actual & 0xF0 ) >> 4 ) + getHexaBits( actual & 0x0F ) + " ";
			if ( counter % 8 == 0 ) result += "  ";
			if ( counter % 16 == 0 ) result += "\n";
			++counter;
			
		}
		
		return result;

	}
	
	/**
	 * Returns hexa value of an int
	 * @param valueX int value under 16
	 * @return hexa string
	 */
	
	public static String getHexaBits ( int valueX )
	{

		// System.out.println( System.currentTimeMillis( ) + "Encoder.getHexaBits " + valueX );

		switch ( valueX )
		{
		
			case 15 : return "F";
			case 14 : return "E";
			case 13 : return "D";
			case 12 : return "C";
			case 11 : return "B";
			case 10 : return "A";
			default : return "" + valueX;
		
		}
		
	}

}
