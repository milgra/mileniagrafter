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
	
	RtmpHsp class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080316

	Tasks of RtmpHsp
	
		- Realize an rtmp handshake on the client side

**/

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.milgra.server.Server;
import com.milgra.server.Library;
import com.milgra.server.OProcess;
import com.milgra.server.SocketController;


public class RtmpHsp extends OProcess
{
	
	public int state;
	
	public ByteBuffer stampBuffer;
	public ByteBuffer replyBuffer;
	public ByteBuffer firstBuffer;
	
	public SocketChannel socket;
	public SocketController controller;
	
	/**
	 * Creates a RtmpHsp instane
	 * @param controllerX the socket controller instance
	 */	
	
	public RtmpHsp ( SocketController controllerX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + controllerX.id + " RtmpHsp.construct" );

		stampBuffer = ByteBuffer.allocate( 1537 );
		replyBuffer = ByteBuffer.allocate( 3073 );
		firstBuffer = ByteBuffer.allocate( 1536 );
		
		state = 0;
		controller = controllerX;
		
	}
	
	/**
	 * Closes handshaker
	 */
	
	public void close ( )
	{

		// System.out.println( System.currentTimeMillis( ) + " " + controllerX.id + " RtmpHsp.construct" );

		stampBuffer = null;
		replyBuffer = null;
		firstBuffer = null;
		
		controller = null;
		
	}
	
	/**
	 * Step
	 */	
	
	public void step ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + controller.id + " RtmpHsp.step" );
		
		try
		{
			
			if ( state == 0 ) receiveStamp( );
			if ( state == 1 ) sendAnswer( );
			if ( state == 2 ) receiveAnswer( );
			
		}
		catch ( IOException exception ) 
		{ 
			controller.close( "Connection closed." ); 
		}
		
	}
	
	/**
	 * Receives stamp from client.
	 */	
	
	public void receiveStamp ( ) throws IOException
	{
			
		//System.out.println( System.currentTimeMillis( ) + " " + controller.id + " RtmpHsp.receiveStamp" );

		try 
		{
			
			// trying to read first stamp
			// if eof happened, close
			// if one byte is readed, check if it is the shutdown indicator
			// if all is read, step to next phase
			
			int bytes = socket.read( stampBuffer );
			if ( bytes == -1 ) throw new IOException( "Disconnected." );
			if ( bytes > 0 && stampBuffer.array( ) [ 0 ] == 0 ) Server.shutdownCheck( );
			if ( !stampBuffer.hasRemaining( ) )	
			{
				
				// create reply from stamp by duplicating it
				
				replyBuffer.put( ( byte ) 0x03 );
				replyBuffer.put( stampBuffer.array( ) , 1 , 1536 );
				replyBuffer.put( stampBuffer.array( ) , 1 , 1536 );
				replyBuffer.rewind( );
				
				state = 1;
				
			}
			
		}
		catch ( IOException exception ) { throw exception; }
		
	}
	
	/**
	 * Sends answer
	 */
	
	public void sendAnswer ( ) throws IOException
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + controller.id + " RtmpHsp.sendAnswer " + 3073 );
		
		try
		{
			
			// trying to write reply
			// if succeeded, step to next phase
				
			socket.write( replyBuffer );
			if ( !replyBuffer.hasRemaining( ) ) state = 2;
			
		}
		catch ( IOException exception ) { throw exception; }
				
	}
	
	/**
	 * Receives clients answer
	 */
	
	public void receiveAnswer ( ) throws IOException
	{
				
		try
		{
			
			// tryiing to read first packets first part
			// checking eof
			// if succeeded, starting rtmp communication
			
			int bytes = socket.read( firstBuffer );
			if ( bytes == -1 ) throw new IOException( Library.CLOSEX );
			if ( !firstBuffer.hasRemaining( ) ) controller.startCommunication( );			
			
		}
		catch ( IOException exception ) { throw exception; }
		
	}	

}
