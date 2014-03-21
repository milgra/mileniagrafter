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
 
   StreamRouter class
   
   @mail milgra@milgra.hu
   @author Milan Toth
   @version 20080315

 	Tasks of Streamrouter
 	
 		- route incoming stream packets to subscribers
 		- create stream writer if needed
 		- create stream reader if needed 
  
**/

import java.util.ArrayList;
import com.milgra.server.encoder.RtmpFactory;


public class StreamRouter extends OStream
{
	
	public int id;
	public int clientId;
	public int flvChannel;
	
	public String name;
	public String mode;
	
	public boolean record;
	public boolean append;
	public boolean registered;
	
	// change - subscriber list changed
	// paused - stream router is paused
		
	public boolean change;
	public boolean paused;
	
	public StreamWriter writer;
	public StreamController controller;
		
	// plus - subscribers to add at next step
	// minus - subscribers to remove at next step
	// subscribers - subscribers

	public ArrayList < OStream > plus;
	public ArrayList < OStream > minus;
	public ArrayList < OStream > subscribers;
	
	/**
	 * Creates new StreamRouter instance
	 **/
	
	public StreamRouter ( int idX , 
						  int flvChannelX , 
						  String nameX , 
						  String modeX , 
						  StreamController controllerX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + id + " StreamRouter.construct " + nameX );

		// create
		
		plus = new ArrayList < OStream > ( );
		minus = new ArrayList < OStream > ( );
		subscribers = new ArrayList < OStream > ( );
		
		// set

		id = idX;
		name = nameX;
		mode = modeX;
		flvChannel = flvChannelX;
		controller = controllerX;
				
		paused = true;
		clientId = controller == null ? 0 : ( int ) controller.client.id; 

		record = mode.equals( "record" );
		append = mode.equals( "append" );
		
	}
	
	/**
	 * StreamRouter destructor
	 **/
	
	public void close ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + id + " StreamRouter.destruct " + name );

		if ( writer != null ) writer.close( );

		paused = true;
		
		/*plus = null;
		minus = null;
		subscribers = null;
		
		writer = null;
		controller = null;*/
		
	}

	/**
	 * Enables publishing
	 **/
	
	public void enable ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + name + " StreamRouter.enable " + id + " controller: " + controller );

		paused = false;
		
		register( );
		
		RtmpPacket start = RtmpFactory.publishStart( clientId , name );
		start.flvChannel = flvChannel;
		
		if ( controller != null ) controller.client.addOutgoingPacket( start );		
		if ( record || append ) writer = new StreamWriter( name , append );
		
		
	}
	
	/**
	 * Disables publishing
	 **/
	
	public void disable ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + name + " StreamRouter.disable " + id );
		
		RtmpPacket failed = RtmpFactory.noAccess( clientId , name );
		failed.flvChannel = flvChannel;
		
		if ( controller != null ) controller.client.addOutgoingPacket( failed );		

	}
	
	/**
	 * Registers router in server
	 **/
	
	public void register ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + name + " StreamRouter.register " + id );

		registered = true;
		
		Server.registerRouter( this );
		Server.connectRouter( this , name );
		
	}
	
	/**
	 * Unregisters router in server
	 **/
	
	public void unregister ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + name + " StreamRouter.register " + id );

		registered = false;
		
		Server.unregisterRouter( this );
		Server.disconnectRouter( this , name );
		
	}

	/**
	 * Adds a subscriber
	 * @param controllerX StreamController
	 */
	
	public void subscribe ( OStream subscriberX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + id + " StreamRouter.subscribe " + subscriberX );
		
		synchronized ( subscribers ) 
		{ 
		
			plus.add( subscriberX ); 
			change = true;

		}
		
	}
	
	/**
	 * Removes a subscriber 
	 * @param controllerX
	 */
	
	public void unsubscribe ( OStream subscriberX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + id + " StreamRouter.unsubscribe " + subscriberX );
		
		synchronized ( subscribers ) 
		{ 

			minus.add( subscriberX ); 
			change = true;
		
		}

	}
	
	/**
	 * Adds new rtmp packets
	 * @param packetX
	 */
	
	public void take ( RtmpPacket packetX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + name + " StreamRouter.take " + packetX );
		
		if ( !paused )
		{
		
			// if there is a change in subscribers
			
			if ( change )
			{
				
				synchronized ( subscribers )
				{
					
					subscribers.addAll( plus );
					subscribers.removeAll( minus );
					
					plus.clear( );
					minus.clear( );
	
					change = false;
					
				}
				
			}
	
			// dispatch to subscribers
			
			for ( OStream stream : subscribers ) stream.take( packetX );
			
			// send to writer
			
			if ( writer != null ) writer.addPacket( packetX );
			
		}
					
	}

	
	// getters
	
	public int getId ( ) 
	{ 
		
		return id; 
		
	}
	
	public long getClientId ( )
	{
		
		return clientId;
		
	}
	
	public String getName ( ) 
	{	
		
		return name; 
		
	}
	
	public int getFlvChannel ( ) 
	{ 
		
		return flvChannel; 
		
	}
	
}