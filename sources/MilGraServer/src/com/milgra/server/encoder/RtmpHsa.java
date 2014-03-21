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
	
	RtmpHsa class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080316

	Tasks of RtmpHsa
	
		- Realize an rtmp handshake on the host side

**/

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.channels.NotYetConnectedException;

import com.milgra.server.Library;
import com.milgra.server.OProcess;
import com.milgra.server.SocketController;


public class RtmpHsa extends OProcess
{
	
	// state - handshake state
	
	public int state = 0;
	
	public ByteBuffer stampBuffer;
	public ByteBuffer replyBuffer;
	public ByteBuffer firstBuffer;
	
	public SocketChannel socket;
	public SocketController controller;
		
	/**
	 * Creates a RtmpHsa instance
	 * @param controllerX SocketController
	 * @param socketX SocketChanne;
	 */
	
	public RtmpHsa ( SocketController controllerX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + controllerX.id + " RtmpHsa.construct" );
		
		stampBuffer = ByteBuffer.allocate( 1537 );
		replyBuffer = ByteBuffer.allocate( 3073 );
		firstBuffer = ByteBuffer.allocate( 1536 );
		
		controller = controllerX;
		
		// active handshake starts with 0x03
		
		stampBuffer.put( ( byte ) 0x03 );
		
		for ( int index = 0 ; index < 1536 ; index++ )
		{
			
			String hex = Library.HSHASH.substring( index * 2 , index * 2 + 2 );
			int hexa = Integer.parseInt( hex , 16 );
			
			stampBuffer.put( ( byte ) hexa );
			
		}

		stampBuffer.rewind( );

	}
	
	/**
	 * Closes handshaker
	 */
	
	public void close ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + controllerX.id + " RtmpHsa.construct" );

		stampBuffer = null;
		replyBuffer = null;
		firstBuffer = null;
		
		controller = null;
		
	}
	
	/**
	 * Steps one in handshake.
	 */
	
	public void step ( ) 
	{
		
		//System.out.println( System.currentTimeMillis( ) + " " + controller.id + " RtmpHsa.step" );
		
		try
		{
						
			if ( state == 0 ) sendStamp( );
			if ( state == 1 ) receiveAnswer( );
			if ( state == 2 ) sendConnection( );
			
		}
		catch ( IOException exception ) { controller.close( "Connection closed." ); }
		
	}
	
	/**
	 * Sends first handshake chunk - 0x03 followed by 1536 bytes of random data, in this case zeros
	 * @throws IOException
	 */
	
	public void sendStamp ( ) throws IOException
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + controller.id + " RtmpHsa.sendStamp " + 1537 );

		try
		{
			
			// trying to write stamp buffer
			// if all is written, step to next phase
			
			socket.write( stampBuffer );
			if ( !stampBuffer.hasRemaining( ) )	state = 1;
			
		}
		catch ( NotYetConnectedException exception ) { exception.printStackTrace( ); }
		catch ( IOException exception ) { throw exception; }
		
	}
	
	/**
	 * Receives server's answer, 0x03 followed by 1536 bytes from previous step followed by 1536 random bytes from server
	 * @throws IOException
	 */
	
	public void receiveAnswer ( ) throws IOException
	{

		// System.out.println( System.currentTimeMillis( ) + " " + controller.id + " RtmpHsa.receiveAnswer " );
		
		try
		{
			
			// trying to read reply
			// if eof happened, close
			// if all is read, step to next phase
			
			int bytes = socket.read( replyBuffer );	
			if ( bytes == -1 ) throw new IOException( "Disconnected at handshake" );
			
			if ( !replyBuffer.hasRemaining( ) ) 
			{
			
				// positioning reply to client's stamp

				replyBuffer.position( 1 );
				byte [ ] temp = new byte[1536];
				
				replyBuffer.get( temp );
				
				firstBuffer.put( temp );
				firstBuffer.rewind( );

				state = 2;

			}
			
		}
		catch ( IOException exception ) { throw exception; }
		
	}
	
	/**
	 * Last step, send back second 1536 bytes, then tells ClientController to send connection invoke
	 * @throws IOException
	 */
	
	public void sendConnection ( ) throws IOException
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + controller.id + " RtmpHsa.sendConnection " + 1536 );

		try
		{
			
			// trying to write first packet's first part
			// if success, start rtmp communication
			
			socket.write( firstBuffer );
			if ( !firstBuffer.hasRemaining( ) )	controller.startCommunication( );
			
		}
		catch ( IOException exception ) { throw exception; }
		
	}

}
