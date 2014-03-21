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

	SocketConnector class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080315

	Tasks of SocketConnector
	
		- Initialize and open server socket
		- Listen for incoming connections
		- Initialize and connect client sockets
		
**/

import java.io.IOException;
import java.net.InetSocketAddress;

import java.nio.channels.SocketChannel;
import java.nio.channels.ServerSocketChannel;

import java.util.HashMap;
import java.util.ArrayList;

import com.milgra.server.Library;
import com.milgra.server.OProcess;
import com.milgra.server.api.Client;


public class SocketConnector extends OProcess
{
	
	// listener - server socket
	// sockets - active sockets under connection
	
	public ServerSocketChannel listener;
	public HashMap < SocketChannel , ClientController > sockets;
	
	// change - an outgoing socketchannel appeared
	// accepted - actual accepted channels
	// rejected - actual rejected channels
	
	public boolean change;
	public ArrayList < SocketChannel > accepted;
	public ArrayList < SocketChannel > rejected;
	
	/**
	 * SocketConnector constructor
	 **/
	
	public SocketConnector ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " SocketConnector.construct" );
		
		sockets = new HashMap < SocketChannel , ClientController > ( );
		
		accepted = new ArrayList < SocketChannel > ( );
		rejected = new ArrayList < SocketChannel > ( );
		
		try
		{
			
			listener = ServerSocketChannel.open( );
			listener.configureBlocking( false );
			listener.socket( ).bind( new InetSocketAddress( Library.PORT ) );
			
		}
		catch ( IOException exception ) { System.out.println( Library.OPENEX + exception.getMessage( ) ); }
		
	}
	
	/**
	 * Checks for connected sockets all the time
	 **/
	
	public void step ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " SocketConnector.step" );
		
		try
		{
			
			// checking for connected socket
			
			SocketChannel socket = listener.accept( );
			
			if ( socket != null )
			{
				
				// create clientcontroller for socket
				            
				ClientController controller = new ClientController( );
				controller.client = new Client( controller );
				socket.configureBlocking( false );
				controller.connect( socket );
				
			}
			
		}
		catch ( IOException exception ) 
		{ 
			System.out.println( Library.OPENEX + exception.getMessage( ) ); 
		}
		
		// if we have channels to connect
		
		if ( change )
		{
			
			// no incoming channels during check
			
			synchronized ( sockets )
			{
			
				for ( SocketChannel socket : sockets.keySet() )
				{
					
					// checking for accepted socket
					
					try 
					{
						
						// checking channel state
						
						if ( socket.finishConnect( ) ) accepted.add( socket ); 
						
					}
					catch ( IOException exception )	
					{
						
						System.out.println( Library.OPENEX + exception.getMessage( ) );
						rejected.add( socket );
						
					}
					
				}
				
				// dispatch results
				
				for ( SocketChannel socket : accepted ) sockets.get( socket ).connect( socket );
				for ( SocketChannel socket : rejected ) sockets.get( socket ).connectFailed(  );
				
				// remove finished connections
				
				sockets.keySet( ).removeAll( accepted );
				sockets.keySet( ).removeAll( rejected );
				
				// clear containers
				
				accepted.clear( );
				rejected.clear( );
				
				if ( sockets.isEmpty( ) ) change = false;
				
			}			
		
		}

	}
	
	/**
	 * Creates a socket and connects it to specific host
	 * @param hostX url of server
	 * @param controllerX clientcontroller assigned to the request
	 */
	
	public void connect ( String hostX , ClientController controllerX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " SocketConnector.connect " + hostX + " " + controllerX );
		
		try
		{
			
			// getting default port
			
			int port = Library.PORT;
			String [ ] ports = hostX.split( ":" );
			SocketChannel socket = SocketChannel.open( );
			
			// if port is in url, then use that
			
			if ( ports.length > 1 ) port = new Integer( ports[ ports.length - 1 ] );
	        InetSocketAddress address = new InetSocketAddress( ports[0] , port );
	        
	        socket.configureBlocking( false );
	        socket.connect( address );

	        synchronized ( sockets ) 
	        {
	        	
	        	sockets.put( socket , controllerX );
	        	change = true;
	        	
	        }
			
		}
		catch ( Exception exception )	
		{
			
			System.out.println( Library.OPENEX + exception.getMessage( ) );
			controllerX.connectFailed( );
			
		}
		
	}

}
