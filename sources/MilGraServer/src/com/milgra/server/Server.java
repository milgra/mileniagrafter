/*

	Milenia Grafter Server
  
	Copyright (c) 2007-2008 by Milan Toth. All rights reserved.
  
	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
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
	
	Server class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080316

	Tasks of Server
	
		- process command line parameters
		- create a new instance if needed
		- init closing on an old instance if needed
		- initialize containers
		- load and initialize custom applications
		- register applications
		- register processes
		- register clients
		- register streams

**/

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import java.net.URL;
import java.net.Socket;
import java.net.URLClassLoader;

import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;

import com.milgra.server.api.Client;
import com.milgra.server.api.IApplication;


public class Server 
{
	
	public static SocketConnector socketConnector;
	
	// players - stream players
	// routers - stream routers

	public static ArrayList < OStream > players;
	public static ArrayList < OStream > routers;
	
	// jars - container for existing jar files
	// states - container for application states
	// pools - container for process groups
	// customs - container for custom application instances
	// clients - container for clients
	
	public static HashMap < String , File > jars;
	public static HashMap < String , Boolean > states;
	public static HashMap < String , ProcessGroup > pools;
	public static HashMap < String , IApplication > customs;
	public static HashMap < String , ArrayList < Client > > clients;
	
	/**
	 * Server constructor
	 **/
	
	public Server ( )
	{
		
		// System.out.println( System.currentTimeMillis() + " Server.construct" );
		
		// construct
		
		players = new ArrayList < OStream > ( );
		routers = new ArrayList < OStream > ( );
		
		jars = new HashMap < String , File > ( );
		pools = new HashMap < String , ProcessGroup > ( );
		states = new HashMap < String , Boolean > ( );
		customs = new HashMap < String , IApplication > ( );
		clients = new HashMap < String , ArrayList < Client > > ( );
		
		// register socket connector

		socketConnector = new SocketConnector( );
		registerProcess( socketConnector , "sockets" );
		
		// start
		
		readApplications( );
		loadApplications( );
		
	}
	
	/**
	 * Reads up application jars
	 **/
	
	public static void readApplications ( )
	{

		// System.out.println( System.currentTimeMillis( ) + " Server.readApplications" );

		File directory = new File( Library.CUSTOMDIR );
		
		if ( directory.exists( ) ) 
		{
			
			// reset jars
			
			jars.clear( );
			
			// get list
			
			File [ ] files = directory.listFiles( );
			
			for ( File file : files )
			{
				
				String fileID = file.getName( );
				
				if ( fileID.endsWith( ".jar" ) )
				{
					
					// extract application name

					String customID = fileID.substring( 0 , fileID.length( ) - 4 );
					
					// store file
					
					jars.put( customID , file );

				}
				
			}
		
		} 
		else System.out.println( Library.NOAPPS );
				
	}
	
	/**
	 * Loads all available applications on startup
	 **/
	
	public static void loadApplications ( )
	{

		// System.out.println( System.currentTimeMillis() + " Server.loadApplications" );
		for ( String id : jars.keySet() ) loadApplication( id , id );

	}
	
	/**
	 * Loads an application based on the file name of the class
	 * @param idX custom application id
	 **/
	
	public static void loadApplication ( String jarX , String idX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " Server.loadApplication " + idX );
		
		// check if application is already loaded
		
		if ( !customs.containsKey( idX ) )
		{
			
			try
			{
				
				// create classloader
				
				File file = jars.get( jarX );
				URL [ ] urls = new URL[ ]{ file.toURL() };
				ClassLoader classLoader = new URLClassLoader( urls );
				
				// instantiate
				
				Class <?> customClass = classLoader.loadClass( "application.Application" );
				IApplication customInstance = ( IApplication ) customClass.newInstance( );
				customInstance.onStart( idX );
				
				// refresh containers
								
				states.put( idX , true );
				customs.put( idX , customInstance );
				clients.put( idX , new ArrayList < Client > ( ) );

			}
			catch ( Exception exception ) { exception.printStackTrace( ); }
			
		}
		
	}

	/**
	 * Unloads an application, cleans up resources
	 * @param idX custom application id
	 **/
	
	public static void unloadApplication ( String idX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " Server.unloadApplication " + idX );
		
		if ( states.containsKey( idX ) )
		{
			
			boolean state = states.get( idX );
			
			if ( state )
			{
				
				IApplication customInstance;
				ArrayList < Client > clientList;
							
				synchronized ( clients )
				{
					
					// remove client registry
					// get custom application instance
					
					clientList = clients.remove( idX );					
					customInstance = customs.get( idX );			
					
				}
				
				// detaching clients

				for ( Client client : clientList ) client.detach( );
				
				// custom application cleanup trigger
				
				customInstance.onClose( );							
				states.put( idX , false );

				// if custom app jar is deleted we cannot reload
				
				if ( !jars.containsKey( idX ) )	
				{
					
					// cleanup containers
					
					states.remove( idX );
					customs.remove( idX );
					
				}
				
			}
			
		}
		
	}
	
	/**
	 * Returns application instance based on id
	 * @param idX custom application id
	 * @return IApplication custom application instance
	 **/
	
	public static IApplication getApplication ( String idX )
	{
	
		// System.out.println( System.currentTimeMillis( ) + " Server.getInstance " + idX );
		
		if ( customs.containsKey( idX ) )
		{
			
			boolean state = states.get( idX );
			if ( state ) return customs.get( idX );
			
		}
		else
		{
			
			String [ ] parts = idX.split( "/" );
			if ( jars.containsKey( parts[ 0 ] ) ) loadApplication( parts[ 0 ] , idX );
			
			return customs.get( idX );
			
		}
		
		return null;
		
	}

	/**
	 * Adds process to process group
	 * @param processX process
	 * @param groupX process group identifier
	 **/
	
	public static void registerProcess ( OProcess processX , String groupX )
	{
		
		// System.out.println( System.currentTimeMillis() + " Server.registerProcess " + processX + " " + groupX );

		if ( !pools.containsKey( groupX ) )	pools.put( groupX , new ProcessGroup( groupX ) );
		pools.get( groupX ).addProcess( processX );
		
	}
	
	/**
	 * Removes process from process group
	 * @param processX process
	 * @param groupX process group identifier
	 **/
	
	public static void unregisterProcess ( OProcess processX , String groupX )
	{
		
		// System.out.println( System.currentTimeMillis() + " Server.unregisterProcess " + processX + " " + groupX );
		
		if ( !pools.containsKey( groupX ) ) return;
		pools.get( groupX ).removeProcess( processX );
		
	}
	
	
	/**
	 * Pairs a client with an application
	 * @param clientX ClientController instance
	 * @param applicationIDX client's application
	 **/
	
	public static void registerClient ( IApplication customX , ClientController clientX )
	{
	
		//System.out.println( System.currentTimeMillis( ) + " Server.registerClient " + clientX.id + " " + customX );
	
		synchronized ( clients )
		{
			
			// searching for application instance
			
			for ( String id : customs.keySet( ) )	
			{
				
				// if got it, store client
				
				if ( customs.get( id ) == customX )
				{
			
					ArrayList < Client > list = clients.get( id );
					
					// if custom application is not unloaded
					
					if ( list != null ) list.add( clientX.client );
					
					return;
						
				}
				
			}
			
		}
		
	}
	
	/**
	 * Unpairs client with application
	 * @param clientX ClientController instance
	 * @param applicationIDX client's application
	 **/
	
	public static void unregisterClient ( IApplication customX , ClientController clientX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " Server.unregisterClient " + clientX.id + " " + customX );

		synchronized ( clients )
		{
			
			// searching for application instance
			
			for ( String id : customs.keySet( ) )
			{
				
				// if got it, delete it
				
				if ( customs.get( id ) == customX )
				{
			
					ArrayList < Client > list = clients.get( id );
					
					// if custom application is not unloaded
					
					if ( list != null ) list.remove( clientX.client );
					
					return;
						
				}
				
			}
			
		}
		
	}
		
	/**
	 * Registers a stream router
	 * @param nameX stream name
	 * @param routerX router instance
	 **/
	
	public static void registerRouter ( OStream routerX )
	{

		// System.out.println( System.currentTimeMillis() + " Server.registerRouter " + routerX );
		synchronized ( routers ) { routers.add( routerX ); }
		
	}
	
	/**
	 * Unregisters a stream router
	 * @param nameX stream name
	 * @param routerX router instance
	 **/
	
	public static void unregisterRouter ( OStream routerX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " Server.unregisterRouter " + nameX + " " + routerX );
		synchronized ( routers ) { routers.remove( routerX ); }
		
	}
	
	/**
	 * Registers a stream player
	 * @param playerX player instance 
	 **/
	
	public static void registerPlayer ( OStream playerX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " Server.registerPlayer " + playerX );
		synchronized ( players ) { players.add( playerX ); }
		
	}
	
	/**
	 * Unregisters a stream player
	 * @param playerX player istance
	 **/
	
	public static void unregisterPlayer ( OStream playerX )
	{

		// System.out.println( System.currentTimeMillis() + " Server.unregisterPlayer " + playerX );
		synchronized ( players ) { players.remove( playerX ); }		
		
	}
	
	/**
	 * Subscribes player to available router
	 * @param playerX player instance
	 * @param nameX stream name
	 **/
	
	public static void connectPlayer ( OStream playerX , String nameX ) 
	{
	
		// System.out.println( System.currentTimeMillis() + " Server.connectPlayer " + playerX + " " + nameX );

		synchronized ( routers )
		{
			
			// search for router
			
			for ( OStream router : routers ) 
				if ( router.getName( ).equals( nameX ) ) router.subscribe( playerX );
			
		}
		
	}
	
	/**
	 * UNSubscribes player from available router
	 * @param playerX player instance
	 * @param nameX stream name
	 **/
	
	public static void disconnectPlayer ( OStream playerX , String nameX ) 
	{
	
		// System.out.println( System.currentTimeMillis() + " Server.connectPlayer " + playerX + " " + nameX );

		synchronized ( routers )
		{
			
			// search for router
			
			for ( OStream router : routers ) 
				if ( router.getName( ).equals( nameX ) ) router.unsubscribe( playerX );
			
		}
		
	}
	
	/**
	 * Subscribe players to router
	 * @param playerX player instance
	 * @param nameX stream name
	 */
	
	public static void connectRouter ( OStream routerX , String nameX ) 
	{
		
		// System.out.println( System.currentTimeMillis() + " Server.connectRouter " + routerX + " " + nameX );

		synchronized ( players )
		{
			
			// search for players
			
			for ( OStream player : players ) 
				if ( player.getName( ).equals( nameX ) ) routerX.subscribe( player );
			
		}
		
	}

	/**
	 * Subscribe players to router
	 * @param playerX player instance
	 * @param nameX stream name
	 */
	
	public static void disconnectRouter ( OStream routerX , String nameX ) 
	{
		
		// System.out.println( System.currentTimeMillis() + " Server.connectRouter " + routerX + " " + nameX );

		synchronized ( players )
		{
			
			// search for players
			
			for ( OStream player : players ) 
				if ( player.getName( ).equals( nameX ) ) routerX.unsubscribe( player );
			
		}
		
	}
	
	/**
	 * Returns stream router names
	 * @return copy of actual stream router names
	 **/
	
	public static ArrayList < String > getStreamNames ( )
	{

		// System.out.println( System.currentTimeMillis() + " Server.getStreamRouters" );
		
		ArrayList < String > names = new ArrayList < String > ( );
		for ( OStream router : routers ) names.add( router.getName( ) );
		
		return names;
		
	}

	/**
	 * Creates a new Server instance
	 * @param args command line arguments
	 **/
	
	public static void main ( String [ ] argumentsX )
	{
		
		// print console greetings
		
		System.out.println( Library.SALUTE );
		
		int index;
		List < String > arguments = Arrays.asList( argumentsX );

		if ( argumentsX.length > 0 )
		{

			index = arguments.indexOf( "port" );
			if ( index != -1 ) Library.PORT = new Integer( arguments.get( index + 1 ) );
			index = arguments.indexOf( "iostep" );
			if ( index != -1 ) Library.STEPTIME = new Integer( arguments.get( index + 1 ) );
			index = arguments.indexOf( "iobuffer" );
			if ( index != -1 ) Library.IOBUFFER = new Integer( arguments.get( index + 1 ) );
			index = arguments.indexOf( "iothreads" );
			if ( index != -1 ) Library.IOTHREAD = new Integer( arguments.get( index + 1 ) );
			index = arguments.indexOf( "streams" );
			if ( index != -1 ) Library.STREAMDIR = new String( arguments.get( index + 1 ) );
			index = arguments.indexOf( "applications" );
			if ( index != -1 ) Library.CUSTOMDIR = new String( arguments.get( index + 1 ) );
			
			if ( arguments.get( 0 ).equals( "start" ) )	new Server( ); 
			else closeRequest( );
			
		}
		else System.out.println( Library.PARAMS );
		
	}
	
	/**
	 * Shuts down server instance
	 **/
	
	public static void shutdown ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " Server.shutdown" );

		// stop listening to new connections
		
		unregisterProcess( socketConnector , "sockets" );	
		
		// clone all applications container to avoid concurrencies

		ArrayList < String > ids = new ArrayList < String > ( customs.keySet( ) );
		
		// close all applications
		
		for ( String id : ids ) unloadApplication( id );
		
		// close process groups
		
		for ( ProcessGroup pool : pools.values( ) ) pool.close( );

	}
	
	/**
	 * Attempts to close a Milenia instance attached to a specific port
	 * Creates a temporary file, then sends a zero byte to the selected port. The decoder
	 * recognizes the attempt, since an rtmp handshake should start with 0x03
	 **/
	
	public static void closeRequest ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " Server.closeRequest" );
		
		try
		{
			
			// create temporary file and socket
				
			File trigger = new File( Library.CLOSEFILE );
			Socket socket = new Socket( "localhost" , Library.PORT );
			OutputStream stream = socket.getOutputStream( );
			
			// trigger

			trigger.createNewFile( );
			stream.write( 0 );
			
			// cleanup
			
			stream.close( );
			socket.close( );
		
		}
		catch ( IOException exception )	{ System.out.println( Library.NOPORT ); }
		
	}
	
	/**
	 * Checks if shutdown request is valid by checking the existence of the temporary file 
	 **/
	
	public static void shutdownCheck ( )
	{
	
		// System.out.println( System.currentTimeMillis( ) + " Server.shutCheck" );
		
		File trigger = new File( Library.CLOSEFILE );
		if ( trigger.exists( ) ) { trigger.delete( ); shutdown( ); }
		
	}

}
