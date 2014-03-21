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

package com.milgra.server.encoder;

/**
	
	RtmpEncoder class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080316

	Tasks of RtmpEncoder
	
		- Serialize incoming rtmp packets to byte stream
		- Distributes rtmp channels for optimal transfer

**/

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import com.milgra.server.Library;
import com.milgra.server.OProcess;
import com.milgra.server.RtmpPacket;
import com.milgra.server.SocketController;


public class RtmpEncoder extends OProcess
{

	public int writes;
	public int length;
	public int chunkSize = 128;
	
	public ByteBuffer buffer;
	public ByteBuffer actual;
	
	public SocketChannel socket;
	public SocketController controller;

	public RtmpPacket previous;
	public RtmpPacket [ ] packetList;	
	public ArrayList < ByteBuffer > bufferList;
	
	/**
	 * RtmpEncoder constuctor
	 * @param socketX
	 * @param controllerX
	 */
	
	public RtmpEncoder ( SocketController controllerX )
	{
		
		// System.out.println( System.currentTimeMillis() + " " + controllerX.client.id + " RtmpEncoder.construct" );
				
		// create
		
		packetList = new RtmpPacket[ 64 ];
		bufferList = new ArrayList < ByteBuffer > ( );
		
		for ( int index = 0 ; index < 64 ; index++ ) 
		{
			
			packetList[ index ] = new RtmpPacket( );
			packetList[ index ].rtmpChannel = index;
			packetList[ index ].flvChannel = -1;
			packetList[ index ].bodySize = 0;
			packetList[ index ].bodyType = 0;

		}
		
		// set
		
		controller = controllerX;
		
	}
	
	/**
	 * Creates a new buffer
	 */
	
	public void addBuffer ( )
	{

		// System.out.println( System.currentTimeMillis() + " " + controller.client.id + " RtmpEncoder.addBuffer " );

		buffer = ByteBuffer.allocate( Library.IOBUFFER );
		bufferList.add( buffer );
		
	}
	
	/**
	 * Gets waitingList packetList
	 * @param packetListX
	 */
	
	public void takePackets ( ArrayList < RtmpPacket > packetListX )
	{

		// System.out.println( System.currentTimeMillis() + " " + controller.client.id + " RtmpEncoder.getPAckets " );

		for ( RtmpPacket packet : packetListX ) encode( packet );
		packetListX.clear( );
		
	}
	
	// size - chunk size for buffer injection
	// remaining - remaining bytes in buffer injection
	
	public int size;
	public int remaining;
	
	// header properties
	
	public int headerSize;
	public int headerFlag;
	
	public byte [ ] header;
	public byte [ ] stumpy = new byte[ 1 ];
	
	/**
	 * Adds new packet
	 * @param packetX
	 */

	public void encode ( RtmpPacket packetX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + controller.client.id + " RtmpEncoder.encode " + packetX );
		
		// reset size

		packetX.bodySize = packetX.body.length;
				
		// getting previous

		previous = packetList[ packetX.rtmpChannel ];
		
		if ( packetX.first || previous.flvChannel != packetX.flvChannel )
		{
			
			// by default, header size is 12
			
			headerSize = 12;
			headerFlag = 0x00;
			
			// store new values
			
			packetList[ packetX.rtmpChannel ] = packetX;
			
		}
		else
		{
			
			// flv channel is the same, length is 8
			
			headerSize = 8;
			headerFlag = 0x40;
			
			if ( previous.bodyType == packetX.bodyType && previous.bodySize == packetX.bodySize )
			{
				
				// body type and size is the same, length is 4
				
				headerSize = 4;
				headerFlag = 0x80;
				
				if ( previous.flvStamp == packetX.flvStamp )
				{
					
					// flv stamp is the same, length is 1
					
					headerSize = 1;
					headerFlag = 0xC0;
					
				}
				else packetList[ packetX.rtmpChannel ] = packetX;
				
			}
			else packetList[ packetX.rtmpChannel ] = packetX;
			
		}

		// header and stumpy byte construction
		
		header = new byte [ headerSize ];
		
		header[ 0 ] = ( byte )( headerFlag | packetX.rtmpChannel & 0x3F );
		stumpy[ 0 ] = ( byte )( header[ 0 ] | 0xC0 );
					
		if ( headerSize > 1 )
		{
			
			// put flv stamp
			
			header[ 1 ] = ( byte )( packetX.flvStamp >> 16 );
			header[ 2 ] = ( byte )( packetX.flvStamp >> 8 );
			header[ 3 ] = ( byte )( packetX.flvStamp );
			
			if ( headerSize > 4 )
			{
				
				// put body size and type
				
				header[ 4 ] = ( byte )( packetX.bodySize >> 16 );
				header[ 5 ] = ( byte )( packetX.bodySize >> 8 );
				header[ 6 ] = ( byte )( packetX.bodySize );
				header[ 7 ] = ( byte )( packetX.bodyType );
					
				if ( headerSize > 8 )
				{
						
					// put flv channel
					
					header[ 8 ] = ( byte )( packetX.flvChannel >> 24 );
					header[ 9 ] = ( byte )( packetX.flvChannel >> 16 );
					header[ 10 ]= ( byte )( packetX.flvChannel >> 8 );
					header[ 11 ]= ( byte )( packetX.flvChannel );
					
				}
				
			}
		
		}
		
		// System.out.println( "header: " + Encoder.getHexa( header ) );

		// reset buffer and chunk size if needed

		if ( bufferList.isEmpty( ) ) addBuffer( );
		if ( packetX.bodyType == 0x01 ) chunkSize = Encoder.bytesToInt( packetX.body );
		
		// setting remaining

		remaining = packetX.bodySize;
		
		// injecting packet to buffer
		
		do
		{
			
			// getting size until next chunk
			
			size = remaining < chunkSize ? remaining : chunkSize;
			
			// add new buffer if needed
			
			if ( buffer.position( ) + header.length + size > buffer.capacity( ) ) addBuffer( );
			
			buffer.put( header );
			buffer.put( packetX.body , packetX.bodySize - remaining , size );
			
			// decrease remaining
			// set stumpy header for further use

			remaining -= size;
			header = stumpy;
			
		}
		while ( remaining > 0 );
		
	}

	
	/**
	 * Serializes rtmp chunks into raw binary data
	 * @param packetX The RTMP Packet
	 */
	
	public void step ( )
	{
		
		//System.out.println( System.currentTimeMillis() + " " + controller.client.id + " RtmpEncoder.step " );
		
		if ( !bufferList.isEmpty( ) )
		{
			
			try
			{
				
				// getting oldest buffer
				// getting limit
				
				actual = bufferList.get( 0 );
				actual.flip( );
				length = actual.limit( );
				
				// writing buffer
				
				writes = socket.write( actual );
				controller.bytesOut += writes;
				
				// System.out.println( "write: " + writes );
				
				// checking if it flushed completely ( low-level socket buffer was full ) 
				
				if ( writes == length ) bufferList.remove( 0 );
				else actual.compact( );
							
			}
			catch ( IOException exception )
			{
				
				// System.out.println( System.currentTimeMillis() + " " + controller.client.id + " EXCEPTION RtmpEncoder.step " +	exception.getMessage( ) );

				if ( !controller.closed ) controller.close( exception.getMessage( ) );
				
			}
			
		}	
		
	}

}
