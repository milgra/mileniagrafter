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
	
	SSData class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080316
	
	Tasks of sSData 
	
		- testedClient every possible data transfer mode

**/

import com.milgra.server.api.Client;
import com.milgra.server.api.Wrapper;
import com.milgra.server.api.WrapperMap;
import com.milgra.server.api.WrapperList;
import com.milgra.server.api.StatusEvent;
import com.milgra.server.api.InvokeEvent;
import com.milgra.server.api.IApplication;
import com.milgra.server.api.EventListener;


public class SSData implements IModule
{
	
	// url - url of remote server
	// client - controller client
	// testedClient - 
	// application - mother applicaiton
	
	public String url;
	public Client client;
	public Client testedClient;
	public IApplication application;
	
	// test variables
	
	public Double testedClientNumber;
	public String testedClientString;
	public WrapperMap testedClientObject;
	public WrapperList testedClientArray;
	
	/**
	 * SSData constructor
	 * @param applicationX mother application
	 */
	
	public SSData ( IApplication applicationX )
	{
		
		System.out.println( System.currentTimeMillis( ) + " SSData.construct " + applicationX );
		application = applicationX;
		
	}
	
	/**
	 * Closes module
	 **/
	
	public void onLeave ( Client clientX ) 
	{
		
		System.out.println( System.currentTimeMillis( ) +  " CSConnection.onClose " );
		
		
	}
	
	/**
	 * Client entering point
	 * @param clientX client
	 * @param argumentsX arguments
	 */
	
	public void onEnter ( Client clientX , WrapperList argumentsX )
	{

		System.out.println( System.currentTimeMillis( ) + " SSData.onEnter " + clientX + " " + argumentsX );
		
		EventListener invokeListener = new EventListener ( )
		{
				
			public void onEvent ( InvokeEvent eventX ) { onInvoke( eventX ); }
				
		};
		
		EventListener statusListener = new EventListener ( )
		{
				
			public void onEvent ( StatusEvent eventX ) { onStatus( eventX ); }
				
		};

		url = argumentsX.getString( 1 );

		client = clientX;
		client.accept( );
		client.call( "log" , new Wrapper( "Connection... " ) );
		
		// init remote client
		
		testedClient = new Client( application );
		testedClient.addStatusEventListener( statusListener );
		testedClient.addInvokeEventListener( invokeListener );
		testedClient.connect( "rtmp://" + url + "/milgraunit" , new Wrapper( "CSData" ) );

	}
	
	/**
	 * Connection status of remote client
	 * @param eventX event
	 */
	
	public void onStatus ( StatusEvent eventX )
	{
		
		System.out.println( System.currentTimeMillis( ) + " SSData.onStatus " + eventX );
		client.call( "log" , new Wrapper( "Status: " + eventX.code ) );
		
		// start with empty call
		
		if ( eventX.code.equals( StatusEvent.SUCCESS ) ) emptyCall( );
		
	}
	
	/**
	 * Empty call on remote server
	 */
	
	public void emptyCall ( )
	{
		
		System.out.println( System.currentTimeMillis( ) + " SSData.emptyCall " );
		client.call( "log" , new Wrapper( "Empty call on remote" ) );
		testedClient.call( "emptyCall" );
		
	}
	
	/**
	 * Null call on remote
	 */
	
	public void nullCall ( )
	{
		
		System.out.println( System.currentTimeMillis( ) + " SSData.nullCall " );
		client.call( "log" , new Wrapper( "Null call on remote" ) );
		testedClient.call( "nullCall" , new Wrapper( ) );
		
	}
	
	/**
	 * Number call on remote
	 */
	
	public void numberCall ( )
	{
	
		System.out.println( System.currentTimeMillis( ) + " SSData.numberCall " );

		testedClientNumber = Math.random( );
		testedClient.call( "numberCall" , new Wrapper( testedClientNumber ) );
		client.call( "log" , new Wrapper( "Number call on remote : " + testedClientNumber ) );
		
	}
	
	/**
	 * Boolean call on remote
	 */
	
	public void booleanCall ( )
	{

		System.out.println( System.currentTimeMillis( ) + " SSData.booleanCall " );

		client.call( "log" , new Wrapper( "Boolean call on remote : " + true ) );
		testedClient.call( "booleanCall" , new Wrapper( true ) );		
		
	}
	
	/**
	 * String call on remote
	 */

	public void stringCall ( )
	{
		
		System.out.println( System.currentTimeMillis( ) + " SSData.numberCall " );

		testedClientString = "azipafaipapipipapapifapipa";
		testedClient.call( "stringCall" , new Wrapper( testedClientString ) );		
		client.call( "log" , new Wrapper( "String call on remote : " + testedClientString ) );
		
	}
	
	/**
	 * Array call on remote
	 */
	
	public void arrayCall ( ) 
	{

		System.out.println( System.currentTimeMillis( ) + " SSData.arrayCall " );
	
		testedClientArray = new WrapperList( );
		testedClientArray.add( true );
		testedClientArray.add( 23423.2 );

		testedClient.call( "arrayCall" , new Wrapper( testedClientArray ) );		
		client.call( "log" , new Wrapper( "Array call on remote : " + testedClientArray ) );

	}

	/**
	 * Object call on remote
	 */
	
	public void objectCall ( ) 
	{

		System.out.println( System.currentTimeMillis( ) + " SSData.objectCall " );

		testedClientObject = new WrapperMap( );
		testedClientObject.put( "bool" , true );
		testedClientObject.put( "num" , 23423.2 );

		client.call( "log" , new Wrapper( "Array call on remote : " + testedClientObject ) );
		testedClient.call( "objectCall" , new Wrapper( testedClientObject ) );		

	}
	
	/**
	 * Invoke from remote server
	 * @param eventX invoke event
	 */
	
	public void onInvoke ( InvokeEvent eventX )
	{

		System.out.println( System.currentTimeMillis( ) + " SSData.onInvoke " + eventX.id );

		if ( eventX.id.equals( "onEmptyCall" ) )
		{
			
			client.call( "log" , new Wrapper( "Empty call on local " ) );
			client.call( "log" , new Wrapper( "SUCCESS" ) );
			nullCall( );
			
		}

		if ( eventX.id.equals( "onNullCall" ) )
		{
			
			client.call( "log" , new Wrapper( "Null call on local " + eventX.arguments.get( 0 ).type ) );

			if ( eventX.arguments.get( 0 ).type == Wrapper.NULL ) client.call( "log" , new Wrapper( "SUCCESS" ) );
			else client.call( "log" , new Wrapper( "FAILURE" ) );

			numberCall( );
			
		}
		
		if ( eventX.id.equals( "onNumberCall" ) )
		{
			client.call( "log" , new Wrapper( "Number call on local " + eventX.arguments.getDouble( 0 ) ) );
			
			if ( eventX.arguments.getDouble( 0 ) == testedClientNumber ) client.call( "log" , new Wrapper( "SUCCESS" ) );
			else client.call( "log" , new Wrapper( "FAILURE" ) );
			
			booleanCall( );
			
		}

		if ( eventX.id.equals( "onBooleanCall" ) )
		{
		
			client.call( "log" , new Wrapper( "Boolean call on local " + eventX.arguments.getBoolean( 0 ) ) );

			if ( eventX.arguments.getBoolean( 0 ) == true ) client.call( "log" , new Wrapper( "SUCCESS" ) );
			else client.call( "log" , new Wrapper( "FAILURE" ) );
			
			stringCall( );
			
		}

		if ( eventX.id.equals( "onStringCall" ) )
		{
		
			client.call( "log" , new Wrapper( "String call on local " + eventX.arguments.getString( 0 )) );

			if ( eventX.arguments.getString( 0 ).equals( testedClientString ) ) client.call( "log" , new Wrapper( "SUCCESS" ) );
			else client.call( "log" , new Wrapper( "FAILURE" ) );
			
			arrayCall( );
			
		}
		
		if ( eventX.id.equals( "onArrayCall" ) )
		{
			
			client.call( "log" , new Wrapper( "Array call on local " + eventX.arguments.getList( 0 ).size( ) ) );

			if ( eventX.arguments.getList( 0 ).size( ) == testedClientArray.size( ) ) client.call( "log" , new Wrapper( "SUCCESS" ) );
			else client.call( "log" , new Wrapper( "FAILURE" ) );
			
			objectCall( );
				
		}
		
		if ( eventX.id.equals( "onObjectCall" ) )
		{
			
			client.call( "log" , new Wrapper( "Object call on local " + eventX.arguments.getMap( 0 ).size( ) ) );

			if ( eventX.arguments.getMap( 0 ).size( ) == testedClientObject.size( ) ) client.call( "log" , new Wrapper( "SUCCESS" ) );
			else client.call( "log" , new Wrapper( "FAILURE" ) );
			
			client.call( "log" , new Wrapper( "Finished testing." ) );
				
		}

	}

}
