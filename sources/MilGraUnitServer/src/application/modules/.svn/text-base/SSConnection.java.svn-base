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

/**

	SSConnection class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080316
	
	Tasks of SSConnection 
	
		- testedClient every possible connection event on remote server

**/

import com.milgra.server.api.Client;
import com.milgra.server.api.Wrapper;
import com.milgra.server.api.StatusEvent;
import com.milgra.server.api.WrapperList;
import com.milgra.server.api.IApplication;
import com.milgra.server.api.EventListener;


public class SSConnection implements IModule
{
	
	// url - address of tested server
	// client - controller client
	// testedClient - 
	// application - mother application
	
	public String url;
	public Client client;
	public Client testedClient;
	public IApplication application;
	
	/**
	 * SSConnection constuctor
	 * @param applicationX mother application
	 */
	
	public SSConnection ( IApplication applicationX )
	{
		
		System.out.println( System.currentTimeMillis( ) + " SSConnection.construct " + applicationX );
		application = applicationX;
		
	}
	
	/**
	 * Client entering point
	 * @param clientX client
	 * @param argumentX arguments
	 */
	
	public void onEnter ( Client clientX , WrapperList argumentsX )
	{

		System.out.println( System.currentTimeMillis( ) + " SSConnection.onEnter " + clientX + " " + argumentsX );

		// set
		
		url = argumentsX.getString( 1 );
		client = clientX;
		
		// start

		client.accept( );
		invalidServer( );
		
	}
	
	/**
	 * Closes module
	 **/
	
	public void onLeave ( Client clientX ) 
	{
		
		System.out.println( System.currentTimeMillis( ) +  " SSConnection.onClose " );
		
		
	}
	
	/**
	 * Connecting to an invalid server
	 */
	
	public void invalidServer ( )
	{

		System.out.println( System.currentTimeMillis( ) + " SSConnection.invalidServer " );
		client.call( "log" , new Wrapper( "Connecting to invalid server" ) );
		
		EventListener statusListener = new EventListener ( )
		{
				
			public void onEvent ( StatusEvent eventX ) { onInvalidServer( eventX ); }
				
		};
		
		testedClient = new Client( application );
		testedClient.addStatusEventListener( statusListener );
		testedClient.connect( "rtmp://fake.fke/fake" , new Wrapper( ) );
		
	}
	
	/**
	 * Status from invalid server
	 * @param eventX
	 */
	
	public void onInvalidServer ( StatusEvent eventX )
	{
		
		System.out.println( System.currentTimeMillis( ) + " SSConnection.onInvalidServer " + eventX.code );
		client.call( "log" , new Wrapper( "Status: " + eventX.code ) );
		
		if ( eventX.code.equals( StatusEvent.FAILURE ) ) client.call( "log" , new Wrapper( "SUCCESS" ) );
		else client.call( "log" , new Wrapper( "FAILED" ) );
		
		testedClient.addStatusEventListener( null );
		invalidApplication( );	
		
	}
	
	/**
	 * Connecting to invalid application
	 */
	
	public void invalidApplication ( )
	{
		
		System.out.println( System.currentTimeMillis( ) + " SSConnection.invalidApplication " );
		client.call( "log" , new Wrapper( "Connecting to invalid application" ) );
		
		EventListener statusListener = new EventListener ( )
		{
				
			public void onEvent ( StatusEvent eventX ) { onInvalidApplication( eventX ); }
				
		};
		
		testedClient = new Client( application );
		testedClient.addStatusEventListener( statusListener );
		testedClient.connect( "rtmp://localhost/fake" , new Wrapper( ) );
		
	}
	
	/**
	 * Status for invalid application
	 * @param eventX event
	 */
	
	public void onInvalidApplication ( StatusEvent eventX )
	{
		
		System.out.println( System.currentTimeMillis( ) + " SSConnection.onInvalidApplication " + eventX.code );
		
		if ( eventX.code.equals( StatusEvent.REJECTION ) ) 
		{
		
			client.call( "log" , new Wrapper( "Status: " + eventX.code + " application: " + eventX.info.getString( "application" ) ) );
			client.call( "log" , new Wrapper( "SUCCESS" ) );
			
		}
		else 
		{

			client.call( "log" , new Wrapper( "Status: " + eventX.code ) );
			client.call( "log" , new Wrapper( "FAILED" ) );
		
		}
		
		testedClient.addStatusEventListener( null );
		rejectedConnection( );	
		
	}	
	
	/**
	 * Connecting to rejecting application
	 */
	
	public void rejectedConnection ( )
	{
		
		System.out.println( System.currentTimeMillis( ) + " SSConnection.rejectedConnection " );
		client.call( "log" , new Wrapper( "Connecting to rejecting application" ) );
		
		EventListener statusListener = new EventListener ( )
		{
				
			public void onEvent ( StatusEvent eventX ) { onRejectedConnection( eventX ); }
				
		};
				
		WrapperList arguments = new WrapperList( );
		arguments.add( "CSConnection" );
		arguments.add( "reject" );

		testedClient = new Client( application );
		testedClient.addStatusEventListener( statusListener );
		testedClient.connect( "rtmp://" + url + "/fake" , arguments );
			
	}
	
	/**
	 * Status for rejected connnection
	 * @param eventX event
	 */
	
	public void onRejectedConnection ( StatusEvent eventX )
	{
		
		System.out.println( System.currentTimeMillis( ) + " SSConnection.onRejectedConnection " + eventX.code );
		
		if ( eventX.code.equals( StatusEvent.REJECTION ) ) 
		{
		
			client.call( "log" , new Wrapper( "Status: " + eventX.code + " application: " + eventX.info.getString( "application" ) ) );
			client.call( "log" , new Wrapper( "SUCCESS" ) );
			
		}
		else 
		{

			client.call( "log" , new Wrapper( "Status: " + eventX.code ) );
			client.call( "log" , new Wrapper( "FAILED" ) );
		
		}
		
		testedClient.addStatusEventListener( null );
		acceptedConnection( );			
		
	}
	
	/**
	 * Connecting to accepting application
	 */
	
	public void acceptedConnection ( )
	{
		
		System.out.println( System.currentTimeMillis( ) + " SSConnection.acceptedConnection " );

		client.call( "log" , new Wrapper( "Connecting to accepting application" ) );
		
		EventListener statusListener = new EventListener ( )
		{
				
			public void onEvent ( StatusEvent eventX ) { onAcceptedConnection( eventX ); }
				
		};
		
		WrapperList arguments = new WrapperList( );
		arguments.add( "CSConnection" );
		arguments.add( "accept" );
		
		testedClient = new Client( application );
		testedClient.addStatusEventListener( statusListener );
		testedClient.connect( "rtmp://" + url + "/milgraunit" , arguments );
		
		
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
			client.call( "log" , new Wrapper( "SUCCESS" ) );
			
		}
		else 
		{

			client.call( "log" , new Wrapper( "Status: " + eventX.code ) );
			client.call( "log" , new Wrapper( "FAILED" ) );
		
		}
		
		testedClient.addStatusEventListener( null );
		closedConnection( );		
		
	}
	
	/**
	 * Closing connecting
	 */
	
	public void closedConnection ( )
	{
		
		System.out.println( System.currentTimeMillis( ) + " SSConnection.closedConnection " );
		
		EventListener statusListener = new EventListener ( )
		{
				
			public void onEvent ( StatusEvent eventX ) { onClosedConnection( eventX ); }
				
		};
		
		client.call( "log" , new Wrapper( "Closing connection" ) );
		testedClient.addStatusEventListener( statusListener );
		testedClient.call( "disconnect" );
		
	}
	
	/**
	 * Status for closed connection
	 * @param eventX
	 */
	
	public void onClosedConnection ( StatusEvent eventX )
	{
		
		System.out.println( System.currentTimeMillis( ) + " SSConnection.onClosedConnection " + eventX.code );
		
		client.call( "log" , new Wrapper( "Status: " + eventX.code ) );
		
		if ( eventX.code.equals( StatusEvent.CLOSURE ) ) client.call( "log" , new Wrapper( "SUCCESS" ) );
		else client.call( "log" , new Wrapper( "FAILED" ) );

		testedClient.addStatusEventListener( null );
		client.call( "log" , new Wrapper( "Finished testedClienting." ) );

	}
	
}
