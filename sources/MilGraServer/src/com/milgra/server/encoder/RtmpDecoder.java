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
	
	RtmpDecoder class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080316

	Tasks of RtmpDecoder
	
		- Deserialize incoming byte stream to rtmp packetBufferA

**/

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import com.milgra.server.Library;
import com.milgra.server.OProcess;
import com.milgra.server.RtmpPacket;
import com.milgra.server.SocketController;


public class RtmpDecoder extends OProcess
{
	
	// read - read count
	// next - read amount for next chunk
	// nextSize - header size modifiers size register
	// chunkSize - actual chunk size for decoder
	
	public int read;
	public int next;
	public int nextSize = -1;
	public int chunkSize = 128;
	
	// header decoding helpers
	
	public int headerFlag;
	public int headerSize;
	public int headerChannel;
	
	// headerSizes - header size containers
	// end - deserialization helper

	public byte [ ] headerSizes; 
	public boolean end;
	
	// packet buffers

	public RtmpPacket [ ] activeBuffer;
	public RtmpPacket [ ] packetBufferA;
	public RtmpPacket [ ] packetBufferB;
	
	// packet containers

	public ArrayList < RtmpPacket > flvList;
	public ArrayList < RtmpPacket > dataList;
	
	//
	
	public RtmpPacket packet;
	public ByteBuffer buffer;
	public SocketChannel socket;
	public SocketController controller;	
				
	/**
	 * RtmpDecoder constructor
	 * @param controllerX SocketController
	 */
	
	public RtmpDecoder ( SocketController controllerX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " RtmpDecoder.construct" );
		
		// construct
		
		buffer = ByteBuffer.allocate( Library.IOBUFFER );
		headerSizes = new byte [ 4 ];
		
		flvList = new ArrayList < RtmpPacket > ( );
		dataList = new ArrayList < RtmpPacket > ( );
		
		packetBufferA = new RtmpPacket [ 64 ];
		packetBufferB = new RtmpPacket [ 64 ];
				
		// set

		controller = controllerX;
		activeBuffer = packetBufferA;
		
		// fillup
		
		headerSizes[ 3 ] = 1;
		headerSizes[ 2 ] = 4;
		headerSizes[ 1 ] = 8;					
		headerSizes[ 0 ] = 12;

		for ( int index = 0 ; index < 64 ; index ++ )
		{
			
			packetBufferA[ index ] = new RtmpPacket( );
			packetBufferB[ index ] = new RtmpPacket( );
			packetBufferA[ index ].rtmpChannel = index;
			packetBufferB[ index ].rtmpChannel = index;

		}

	}
	
	/**
	 * Gives flv packets to the given arraylist
	 * @param packetsX container for packets
	 */
	
	public void giveFlvPackets ( ArrayList < RtmpPacket > packetsX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " RtmpDecoder.giveFlcPackets " + packetBufferAX );
		
		if ( flvList.size( ) > 0 )
		{

			packetsX.addAll( flvList );
			flvList.clear( );

		}
		
	}
	
	/**
	 * Gives dataa packets to the given arraylist
	 * @param packetsX container for packets
	 */

	public void giveDataPackets ( ArrayList < RtmpPacket > packetsX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " RtmpDecoder.giveDataPackets " + packetsX + " " + dataList.size() );
		
		if ( dataList.size( ) > 0 )
		{
			
			packetsX.addAll( dataList );
			dataList.clear( );
			
		}
		
	}
	/**
	 * Fills up buffer for next deserialization
	 **/
	
	public void step ( )
	{
		
		try
		{
			
			read = socket.read( buffer );
			controller.bytesIn += read;

			// System.out.println( System.currentTimeMillis() + " RtmpDecoder step read: " + read );

			if ( read > 0 ) getChunks( ); else 
			if ( read == -1 ) controller.close( Library.CLOSURE );
		
		}
		catch ( IOException exception ) { controller.close( exception.getMessage( ) ); }
		
	}

	/**
	 * Deserializes rtmp chunks and creates packet
	 * @throws DecodeException at incorrect header sizes
	 */
	
	public void getChunks ( )
	{
		
		// System.out.println( System.currentTimeMillis() + " " + controller.client.id + " RtmpDecoder.getChunks " + buffer );
		
		buffer.flip( );
		
		do
		{
			
			// if a header flag is available

			if ( buffer.hasRemaining( ) )
			{
				
				// read header flag
				
				headerFlag = buffer.get( );
				headerSize = headerSizes[ ( headerFlag & 0xC0 ) >> 6 ];
				headerChannel = headerFlag & 0x3F;
				
				// check for page switch
				
				// !!! buffer page flipping is still annoying, has to do something with it
				
				if ( headerChannel == 0 && nextSize == -1 ) 
				{
					
					// switch buffer
					
					nextSize = headerSize;
					activeBuffer = packetBufferB;

				}
				else
				{
				
					// if we have a nextSize, use that
					
					packet = activeBuffer[ headerChannel ];
					if ( nextSize != -1 ) headerSize = nextSize;

					// trying to read header
					
					if ( buffer.remaining( ) >= headerSize )
					{ 
						
						if ( headerSize > 1 )
						{
							
							// extract flv timestamp
							
							packet.flvStamp = buffer.getShort( ) << 8 | buffer.get( ) & 0xFF;
						
							if ( headerSize > 4 )
							{
								
								// extract body size and type
								
								packet.bodySize = buffer.getShort( ) << 8 | buffer.get( ) & 0xFF;
								packet.bodyType = buffer.get( ) & 0xFF;
					
								if ( headerSize > 8 )
								{
									
									// extract flv channel
									
									packet.flvChannel = buffer.getInt( );
									
								}
								
								packet.body = new byte[ packet.bodySize ];
								packet.bodyLeft = packet.bodySize;
								
							}
							
						}
					
						// if chunk size is bigger than remaining, use remaining
											
						next = chunkSize < packet.bodyLeft ? chunkSize : packet.bodyLeft;
						
						// trying to read actual chunk
						
						/* header debug
						 
						byte [ ] header = new byte[ headerSize ];
						buffer.position( buffer.position() - headerSize );
						buffer.get( header );
						
						System.out.println( "header : " + Encoder.getHexa( header ) );
						System.out.println( "remaining : " + buffer.remaining() + " next: " + next );
						
						//*/
						
						if ( buffer.remaining( ) >= next )
						{
							
							// reset nextSize
							
							nextSize = -1;
							activeBuffer = packetBufferA;
							
							buffer.get( packet.body , packet.bodySize - packet.bodyLeft , next );

							// modify position and remaining
							
							packet.bodyLeft -= next;
							
							// packet is complete
	
							if ( packet.bodyLeft == 0 ) 
							{
								
								// packet is chunk size changer
								
								if ( packet.bodyType == 0x01 ) 
								{
									
									chunkSize = Encoder.bytesToInt( packet.body ); 
									
								}
								else
								{
									
									// clone packet
									
									RtmpPacket newPacket = new RtmpPacket( packet );
									
									// System.out.println( "packet decode: " + packet );
									
									switch ( packet.bodyType )
									{
									
										case 0x08 :
										case 0x09 : flvList.add( newPacket ); break;
										default	  : dataList.add( newPacket ); break;										
								
									}
																	
								}
								
								packet.body = new byte [ packet.bodySize ];
								packet.bodyLeft = packet.bodySize;

							}
						
						} 
						else
						{
							
							end = true;
							buffer.position( buffer.position( ) - headerSize );
							
						}
						
						// no data for chunk
						
					}
					else 
					{
						
						end = true;
						buffer.position( buffer.position( ) - 1 );
						
					}
					
					// no data for header
					
				}
				
				// go further

			}
			else end = true;
							
			// no data left in buffer
			
		} 
		while ( !end );
		
		end = false;
		buffer.compact( );
		
	}
	
}
