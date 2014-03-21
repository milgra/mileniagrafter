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

package com.milgra.server;

/**
   StreamWriter class
   
   @mail milgra@milgra.com
   @author Milan Toth
   @version 20080316

   	Tasks of Streamwriter
 		- create a file for writing a stream
 		- write incoming chunks into the file

**/

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;


public class StreamWriter 
{

	public int last = 0;
	public int stamp = 0;
	public int start = -1;
	
	public BufferedOutputStream stream;
	
	/**
	 * Creates a streamwriter instance, creates the file based on streamID
	 * @param streamID String
	 */
	
	public StreamWriter ( String idX , boolean appendX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " StreamWriter.construct " + idX + " " + appendX );

		try
		{

			File file;
			FileOutputStream fileStream;
			
			// open file
			
			file = new File( Library.STREAMDIR , idX + ".flv");
			
			// if file exists and mode is not append, delete file
			
			if ( file.exists( ) && !appendX ) file.delete( );
			
			// if file doesn't exist, create file
			
			if ( !file.exists( ) ) file.createNewFile( );
			
			// init io streams

			fileStream = new FileOutputStream( file , true );
			stream = new BufferedOutputStream( fileStream  );
			
			if ( file.length( ) == 0 )
			{
				
				// write flv header
				
				stream.write( 0x46 );	// F
				stream.write( 0x4C );	// L
				stream.write( 0x56 );	// V
				
				stream.write( 0x01 );	// version
				stream.write( 0x05 );	// type : audio and video
				
				stream.write( 0 );
				stream.write( 0 );
				stream.write( 0 );
				stream.write( 0x09 );	// header length, always 9
				
				// flush data
				
				stream.flush( );
	
			}
			else
			{
				
				// !!! get last timestamp
				
			}

		}
		catch ( IOException exception ) 
		{ 
			
			System.out.println( Library.FILEEX ); 
			exception.printStackTrace( ); 
			
		}
		
	}	
	
	/**
	 * Adds new rtmp packet to the file
	 * @param packetX RtmpPacket with a chunk
	 */
	
	public void addPacket ( RtmpPacket packetX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " StreamWriter.addPacket " + packetX.flvStamp );
		
		try
		{
			
			// if first packet, get start time
			
			if ( start == -1 ) start = ( int ) System.currentTimeMillis( );
			
			// calculate stamp from beginning
			
			stamp = ( int ) ( System.currentTimeMillis( ) - start );
				
			// add previous chunk size
				
			stream.write( last >> 24 );
			stream.write( last >> 16 );
			stream.write( last >> 8 );
			stream.write( last );
			
			// create chunk header
			
			stream.write( packetX.bodyType );	// 0x09 video 0x08 audio
			
			// body size on three bytes
			
			stream.write( packetX.bodySize >> 16 );
			stream.write( packetX.bodySize >> 8 );
			stream.write( packetX.bodySize );
			
			// body stamp in milliseconds
			
			stream.write( stamp >> 16 );
			stream.write( stamp >> 8 );
			stream.write( stamp );
			
			// timestamp extension
			
			stream.write( 0 );
			
			// streamid
			
			stream.write( 0 );
			stream.write( 0 );
			stream.write( 0 );
			
			// add chunk body and flush
			
			stream.write( packetX.body );
			stream.flush( );
			
			// store chunk size without the chunk size bytes
			
			last = packetX.bodySize + 11;

		}
		catch ( IOException exception ) 
		{ 
			
			System.out.println( Library.FILEEX ); 
			exception.printStackTrace( ); 
			
		}
		
	}
	
	/**
	 * Closes the file
	 */
	
	public void close ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " StreamWriter.closeStream" );
		
		try
		{
			
			stream.flush( );
			stream.close( );
			
		}
		catch ( IOException exception ) 
		{ 
			
			System.out.println( Library.FILEEX ); 
			exception.printStackTrace( ); 
			
		}
		
	}

}
