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

package application.modules;

/*
 
 	Tasks of Stress
 		- accept conneciton of controller
 		- open remote connections to given tested server
 		- on successful connection, start playing a stream
 
 
*/

import java.util.ArrayList;

import com.milgra.server.api.Client;
import com.milgra.server.api.Stream;
import com.milgra.server.api.Wrapper;
import com.milgra.server.api.WrapperList;
import com.milgra.server.api.InvokeEvent;
import com.milgra.server.api.StatusEvent;
import com.milgra.server.api.IApplication;
import com.milgra.server.api.EventListener;


public class Stress implements IModule
{
	
	// counter - remote player counter
	// streamId - counterer for local stream creation
	
	public int counter;
	public int streamId;
	
	// client - controller client
	// testedUrl - url of tested server

	public Client client;
	public Client testedClient;
	public Stream testedStream;
	public String testedUrl;

	// application - mother application
	// players - remote clients

	public IApplication application;	
	public ArrayList < Client > players;
	
	/**
	 * Stress constructor
	 * @param applicationX mother application
	 */
	
	public Stress ( IApplication applicationX )
	{
		
		System.out.println( System.currentTimeMillis( ) + " SSLive.construct " );
		
		// create

		players = new ArrayList < Client > ( );

		// set
		
		counter = 0;
		streamId = 0;
		application = applicationX;
		
	}
	
	/**
	 * Client entering point
	 * @param clientX client
	 * @param argumentsX arguments
	 */
	
	public void onEnter ( Client clientX , WrapperList argumentsX )
	{

		System.out.println( System.currentTimeMillis( ) + " SSLive.onEnter " + clientX + " " + argumentsX );
		
		// create
		
		EventListener invokeListener = new EventListener ( )
		{
				
			public void onEvent ( InvokeEvent eventX ) { onInvoke( eventX ); }
				
		};

		// set

		testedUrl = argumentsX.getString( 1 );

		client = clientX;		
		client.addInvokeEventListener( invokeListener );
		client.accept( );
		
		connectRemote( );
		
	}
	
	public void connectRemote ( )
	{
		
		System.out.println( System.currentTimeMillis( ) + " SSLive.connectRemote " );

		client.call( "log" , new Wrapper( "Connecting to accepting application" ) );
		
		EventListener statusListener = new EventListener ( )
		{
				
			public void onEvent ( StatusEvent eventX ) { onAcceptedConnection( eventX ); }
				
		};
		
		WrapperList arguments = new WrapperList( );
		arguments.add( "StreamControl" );
		
		testedClient = new Client( application );
		testedClient.addStatusEventListener( statusListener );
		testedClient.connect( "rtmp://" + testedUrl + "/milgraunit" , arguments );
		
	}
	/**
	 * Status for accepted connection
	 * @param eventX event
	 */
	
	public void onAcceptedConnection ( StatusEvent eventX )
	{
	
		System.out.println( System.currentTimeMillis( ) + " SSConnection.onAcceptedConnection " + eventX.code );
		
		if ( eventX.code.equals( StatusEvent.SUCCESS ) ) 
		{
		
			client.call( "log" , new Wrapper( "Status: " + eventX.code + " application: " + eventX.info.getString( "application" ) ) );
			client.call( "log" , new Wrapper( "You can start stress testing." ) );
			
			testedStream = new Stream( "livestream1" , testedClient );
			testedStream.publish( "livestream" , "live" );
			
		}
		else 
		{

			client.call( "log" , new Wrapper( "Status: " + eventX.code ) );
			client.call( "log" , new Wrapper( "FAILED" ) );
		
		}
		
		testedClient.addStatusEventListener( null );
		
	}
	
	/**
	 * Closes module
	 **/
	
	public void onLeave ( Client clientX ) 
	{
		
		System.out.println( System.currentTimeMillis( ) +  " Stress.onClose " );
		
		
	}
	/**
	 * Invoke from controller
	 * @param eventX event
	 */
	
	public void onInvoke ( InvokeEvent eventX )
	{
		
		System.out.println( System.currentTimeMillis( ) + " SSLive.onInvoke " + eventX.id );
		if ( eventX.id.equals( "setSubscribers" ) ) setSubscribers( ( int ) eventX.arguments.getDouble( 0 ) );
		
	}
	
	/**
	 * Status events from players
	 * @param eventX event
	 */
	
	public void onStatus ( StatusEvent eventX )
	{
		
		System.out.println( System.currentTimeMillis( ) + " Stress.onPlayerStatus " + eventX.code );

		if ( eventX.code.equals( StatusEvent.SUCCESS ) )
		{
			
			// play stream
			
			Stream stream = new Stream( "stream" + ++ streamId , eventX.client );
			stream.play( "livestream1" );
			
		}

	}
	
	/**
	 * Sets remote subscribers
	 * @param counterX counter
	 */
	
	public void setSubscribers ( int counterX )
	{
		
		System.out.println( System.currentTimeMillis( ) + " Stress.setSubscribers " + counterX );
		
		if ( counterX != counter )
		{
			
			counter = counterX;
			
			// rearrange
			
			int difference = counter - players.size( );
			
			if ( difference > 0 ) addPlayers( difference );
			if ( difference < 0 ) removePlayers( - difference );
			
			
		}
		
	}
	
	/**
	 * Adds new remote players
	 * @param counterX counter
	 */
	
	public void addPlayers ( int counterX )
	{
		
		System.out.println( System.currentTimeMillis( ) + " Stress.addPlayers " + counterX );

		EventListener statusListener = new EventListener ( )
		{
				
			public void onEvent ( StatusEvent eventX ) { onStatus( eventX ); }
				
		};

		for ( int index = 0 ; index < counterX ; index++ )
		{
			
			// create client
			
			Client client = new Client( application );
			client.addStatusEventListener( statusListener );
			client.connect( "rtmp://" + testedUrl + "/milgraunit" , new Wrapper( "StreamControl" ) );
			
			players.add( client );
			
		}
		
	}
	
	/**
	 * Removes players
	 * @param counterX counter
	 */
	
	public void removePlayers ( int counterX )
	{
		
		System.out.println( System.currentTimeMillis( ) + " Stress.removePlayers " + counterX );

		for ( int index = 0 ; index < counterX ; index++ )
		{
			
			Client client = players.remove( 0 );
			client.detach( );
			
		}
		
	}

}
