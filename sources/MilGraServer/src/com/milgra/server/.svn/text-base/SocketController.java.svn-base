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

	SocketController class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080315

	Tasks of ScoketController
	
		- Initialize encoders
		- Control data flow of encoders
		
**/

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import com.milgra.server.encoder.RtmpHsa;
import com.milgra.server.encoder.RtmpHsp;
import com.milgra.server.encoder.RtmpEncoder;
import com.milgra.server.encoder.RtmpDecoder;


public class SocketController extends OProcess
{
	
	// starting read and write values ( rtmp handshake by default )
	
	public long bytesIn = 3073;
	public long bytesOut = 3073;

	// state indicators
	
	// closed - controller is closed, no more data in/out
	// closeInited - we have to close in two steps
	// closeSwitch - second close step flag
	
	public boolean closed;
	public boolean closeSwitch;
	public boolean closeInited;
	
	// encoders
	
	// hsa - active handshake encoder
	// hsp - passive handshake encoder
	
	public RtmpHsa hsa;
	public RtmpHsp hsp;
	
	public OProcess encoder;
	public OProcess decoder;
	
	public RtmpEncoder rtmpe;
	public RtmpDecoder rtmpd;
	
	public SocketChannel socket;
	public ClientController client;

	// data containers
	
	public ArrayList < RtmpPacket > flvList;
	public ArrayList < RtmpPacket > dataList;
	public ArrayList < RtmpPacket > outgoingList;
	
	/**
	 * SocketController constructor
	 * @param clientX parent clientcontroller
	 */	

	public SocketController ( ClientController clientX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + clientX.id + " SocketController.construct" );

		client = clientX;
		
		hsa = new RtmpHsa( this );
		hsp = new RtmpHsp( this );
		
		rtmpe = new RtmpEncoder( this );
		rtmpd = new RtmpDecoder( this );

		closed = false;
		closeInited = false;
		closeSwitch = false;
		
		flvList = new ArrayList < RtmpPacket > ( );
		dataList = new ArrayList < RtmpPacket > ( );
		outgoingList = new ArrayList < RtmpPacket > ( );

	}
		
	/**
	 * Closes socketController
	 * @param messageX message
	 */
	
	public void close ( String messageX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + client.id + " SocketController.close " + messageX );
		
		if ( !closed )
		{
		
			try
			{
				
				closed = true;

				if ( socket != null ) socket.close( );
				if ( !client.closed ) client.close( );
				
			}
			catch ( IOException exception ) { System.out.println( Library.CLOSEX + exception.getMessage( ) ); }

			
		}
				
	}

	/**
	 * Connects controller to a socket channel
	 * @param socketX SocketChannel
	 * @param modeX client mode
	 */
	
	public void connect ( SocketChannel socketX , String modeX )
	{

		// System.out.println( System.currentTimeMillis( ) + " " + client.id + " SocketController.connect " + socketX + " " + modeX );

		socket = socketX;
			
		hsa.socket = socketX;
		hsp.socket = socketX;
			
		rtmpe.socket = socketX;
		rtmpd.socket = socketX;
		
		// set handshake encoder
		
		encoder = modeX.equals( "active" ) ? hsa : hsp;
		decoder = modeX.equals( "active" ) ? hsa : hsp;
		
	}
	
	/**
	 * Starts rtmp communication, after handshake done
	 */
	
	public void startCommunication ( )
	{

		// System.out.println( System.currentTimeMillis( ) + " " + client.id + " SocketController.startCommunication" );

		// set encoders
		
		encoder = rtmpe;
		decoder = rtmpd;
		
		// close handshakers

		hsa.close( );
		hsp.close( );
		
		// cleanup

		hsa = null;
		hsp = null;
				
	}
	
	/**
	 * Steps one in serialization/deserialization 
	 */
	
	public void step ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + client.id + " SocketController.step " + bytesIn );
		
		// packet exchange
		
		synchronized ( flvList ) { rtmpd.giveFlvPackets( flvList ); }
		synchronized ( dataList ) { rtmpd.giveDataPackets( dataList ); }
		synchronized ( outgoingList ) { rtmpe.takePackets( outgoingList ); }
		
		// encoder stepping
				
		decoder.step( );
		encoder.step( );
		
		// do we have to close?
				
		if ( closeInited )
		{
			
			// waiting one step
			
			if ( closeSwitch ) close( "Closed by application." );
			else closeSwitch = true;
			
		}
						
	}

	
	/**
	 * Takes packets
	 * @param packetsX rtmp packet
	 */
	
	public void takePackets ( ArrayList < RtmpPacket > packetsX )
	{
		
		// System.out.println( System.currentTimeMillis() + " " + client.id + " SocketController.takePackets " + packetsX.size( ) );
		
		synchronized ( outgoingList ) 
		{ 
			
			outgoingList.addAll( packetsX );
			packetsX.clear( );
			
		}
		
	}

	/**
	 * returns flv packets 
	 * @return
	 */
	
	public void giveFlvPackets ( ArrayList < RtmpPacket > packetsX ) 
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + client.id + " SocketController.getflvPackets " + flvList.size() );
		
		synchronized ( flvList )
		{
			
			packetsX.clear( );
			packetsX.addAll( flvList );
			flvList.clear( );
			
		}
		
	}
	
	/**
	 * Returns data packets 
	 * @return
	 */
	
	public void giveDataPackets ( ArrayList < RtmpPacket > packetsX ) 
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + client.id + " SocketController.getDataPackets " + dataList.size() );

		synchronized ( dataList )
		{
			
			packetsX.clear( );
			packetsX.addAll( dataList );
			dataList.clear( );
			
		}
		
	}

}
