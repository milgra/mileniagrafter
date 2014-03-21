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

package com.milgra.server.api;

/**

	Stream class
	
	@mail milgra@milgra.hu
	@author Milan Toth
	@version 20080315
	
		Tasks of Streambase
			- handle server-side created stream creation and functions

**/

import java.util.ArrayList;

import com.milgra.server.api.Stream;

import com.milgra.server.Server;
import com.milgra.server.StreamPlayer;
import com.milgra.server.StreamReader;
import com.milgra.server.StreamRouter;
import com.milgra.server.ClientController;


public class Stream
{
	
	// remoteId - if stream is a remote stream, this is the remote stream id
	// remoteType - if stream is a remote stream, it can be player or publisher
	// remoteName - if stream is a remote stream, the remote name
		
	public int remoteId;
	public int remoteChannel;
	
	public String remoteType;
	public String remoteName;
	
	// router - stream router, used in passive modes, and in remote play
	// reader - stream reader, for passive stream reading
	// palyer - stream player for passive mode, and for active publishing
	
	private StreamRouter router;
	private StreamPlayer player;
	private StreamReader reader;
	
	// remote client's controller

	public ClientController client;
	
	public String type;
	public String name;
	public String mode;

	public static final String PLAYER = "player";
	public static final String ROUTER = "router";
	
	/**
	 * Stream constructor for player
	 * @param nameX name
	 */

	public Stream ( StreamPlayer playerX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " Stream.construct " + playerX );
		
		type = PLAYER;
		name = playerX.getName( );
		player = playerX;
		
	}

	/**
	 * Stream constructor for passive router
	 * @param nameX name
	 */

	public Stream ( StreamRouter routerX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " Stream.construct " + routerX );
		
		type = ROUTER;
		name = routerX.name;
		router = routerX;
		
	}

	/**
	 * Stream constructor for passive streams
	 * @param nameX name
	 */

	public Stream ( String nameX )
	{

		// System.out.println( System.currentTimeMillis( ) + " Stream.construct " + nameX );
		
		type = ROUTER;
		name = nameX;
		
	}
	
	/**
	 * Stream constructor for active streams
	 * @param nameX name
	 * @param clientX client
	 */
	
	public Stream ( String nameX , Client clientX )
	{
	
		// System.out.println( System.currentTimeMillis( ) + " Stream.construct " + nameX + " " + clientX );
		
		type = ROUTER;
		name = nameX;
		remoteName = nameX;
		client = clientX.controller;
		
	}
	
	/**
	 * Plays a local stream called nameX under the name given in constructor ( cloning ) or
	 * Plays the nameX flv from the streams directory under the name given in constructor
	 * Plays a remote stream called nameX under the name given in cosntructor
	 */
	
	public void play ( String nameX )
	{

		// System.out.println( System.currentTimeMillis( ) + " Stream.play " + nameX + " name: " + name + " client: " + client );

		if ( client != null )
		{
			
			// if we are remote stream, set name and type and try to start playing
			 
			remoteName = nameX;
			remoteType = "play";
			
			// maybe remotId isn't arrived yet
			
			client.streamController.createStream( this );
			
		}
		else
		{
			
			// if we are local stream
			
			if ( nameX.endsWith( ".flv" ) )
			{
				
				// if it is an flv request
				// create reader, and assign it as routers injector
				
				router = new StreamRouter( -1 , -1 , name , "live" , null );
				reader = new StreamReader( nameX , router );
				
				reader.subscriber = router;
				reader.start( );
				router.enable( );
				
			}
			else
			{
				
				// if its a normal play request
				// create router
				
				router = new StreamRouter( -1 , -1 , name , "live" , null );
				router.enable( );
				
				// set it as player and router also
				// !!!! how will it work with the player?!?!?!
				
				Server.registerPlayer( router );
				Server.connectPlayer( router , nameX );
				
			}
			
		}
		
	}
	
	/**
	 * Publishes a local stream called nameX to a remote server under the name given in the constructor
	 * @param nameX name
	 * @param modeX mode
	 */
	
	public void publish ( String nameX , String modeX )
	{
	
		// System.out.println( System.currentTimeMillis( ) + " Stream.publish " + nameX + " mode: " + modeX + " name: " + name + " client: " + client );

		if ( client != null )
		{
			
			mode = modeX;
			
			// if we are remote stream, set name and type
			
			name = nameX;
			remoteType = "publish";
			
			// maybe remote id isn't arrived yet
			
			client.streamController.createStream( this );
		
		}
		
	}
	
	/**
	 * Deletes the stream
	 */
	
	public void delete ( ) 
	{
		
		// System.out.println( System.currentTimeMillis( ) + " Stream.delete " + name );

		if ( client != null ) client.streamController.deleteStream( remoteId , this );
		if ( reader != null ) reader.close( );
		if ( player != null ) player.close( );
		if ( router != null ) router.close( );
		
	}
	
	/**
	 * Enables a client defined stream
	 */
	
	public void enable ( ) 
	{
	
		// System.out.println( System.currentTimeMillis( ) + " Stream.enable " + name );

		if ( router != null ) router.enable( );
		if ( player != null ) player.enable( );
		
	}
	
	/**
	 * Disables a client defined stream
	 */
	
	public void disable ( ) 
	{
		
		System.out.println( System.currentTimeMillis( ) + " Stream.disable " + name );

		if ( router != null ) router.disable( );
		if ( player != null ) player.disable( );

	}
	
	/**
	 * 
	 * @param stateX
	 */
	
	public void enableAudio ( boolean stateX ) { }
	
	/**
	 * 
	 * @param stateX
	 */
	
	public void enableVideo ( boolean stateX ) { }
	
	/**
	 * Pauses reader
	 */
	
	public void pause ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " Stream.pause " + name );
		if ( reader != null ) reader.stop( );
		
	}
	
	/**
	 * Starts or stops the recording of this stream
	 * @param stateX record state
	 **/
	
	public void record ( boolean stateX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + id + " StreamRouter.record " + stateX );
		/*
		if ( stateX && writer == null ) writer = new StreamWriter( name , false );
		if ( !stateX && writer != null ) writer.close( ); 
		if ( !stateX ) writer = null;
		*/
				
	}

	
	/**
	 * Seeks in reader
	 */
	
	public void seek ( int positionX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " Stream.seek " + name + " " + positionX );
		if ( reader != null ) reader.setPosition( positionX );
		if ( client != null ) client.call( "seek" , new WrapperList( new Wrapper( positionX ) ) , remoteChannel );

		
	}
	
	
	public void speed ( double multiplierX )
	{
		
		if ( reader != null ) reader.setSpeed( multiplierX );
		
	}
	
	/**
	 * Returns the actual speed of stream
	 */
	
	public double getSpeed ( ) 
	{
		
		if ( reader != null ) return reader.getSpeed( );
		else return 1;
		
	}
	
	/**
	 * Return the actual position
	 */
	
	public double getPosition ( ) 
	{
		
		if ( reader != null ) return reader.getPosition( );
		else return 0;
		
	}

	/**
	 * Returns duration of stream
	 * @return duration
	 */

	public double getDuration ( ) 
	{
		
		if ( reader != null ) return reader.getDuration( );
		else return 0;		
			
	}
	
	/**
	 * Returns type of stream
	 * @return
	 */

	public String getType ( ) 
	{ 
		
		return type; 
		
	}
	
	/**
	 * Returns name of stream 
	 * @return
	 */
	
	public String getName ( ) 
	{ 
		
		return name; 
		
	}
	
	/**
	 * Returns mode of stream
	 * @return
	 */
	
	public String getMode ( ) 
	{ 
		
		return mode; 
		
	}
	
	/**
	 * Returns available streams on server
	 * @return
	 */
	
	public static ArrayList < String > getStreamNames ( ) 
	{ 
		
		return Server.getStreamNames( ); 
		
	}

}
