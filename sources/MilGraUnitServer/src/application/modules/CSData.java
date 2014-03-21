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

	CSData class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080316
	
	Tasks of CSData 
	
		- pass back arguments of incoming invokes

**/

import com.milgra.server.api.Client;
import com.milgra.server.api.Wrapper;
import com.milgra.server.api.WrapperList;
import com.milgra.server.api.InvokeEvent;
import com.milgra.server.api.IApplication;
import com.milgra.server.api.EventListener;


public class CSData implements IModule
{
	
	// tester client
	
	public Client client;
	
	/**
	 * CSData constructor
	 * @param applicationX application
	 */
	
	public CSData ( IApplication applicationX ) 
	{
		
		System.out.println( System.currentTimeMillis( ) + " CSConnection.construct " + applicationX );

	}
	
	/**
	 * Client entering point
	 * @param clientX client
	 * @param argumentsX arguments
	 */
	
	public void onEnter ( Client clientX , WrapperList argumentsX )
	{
		
		System.out.println( System.currentTimeMillis( ) + " CSConnection.onEnter " + clientX + " " + argumentsX );

		// create
		
		EventListener invokeListener = new EventListener ( )
		{
				
			public void onEvent ( InvokeEvent eventX ) { onInvoke( eventX ); }
				
		};
		
		// set

		client = clientX;
		
		// init
		
		clientX.addInvokeEventListener( invokeListener );
		clientX.accept( );
		
	}
	
	/**
	 * Closes module
	 **/
	
	public void onLeave ( Client clientX ) 
	{
		
		System.out.println( System.currentTimeMillis( ) +  " CSData.onClose " );
		
		
	}
	
	/**
	 * Incoming invoke
	 * @param eventX invoke event
	 */
	
	public void onInvoke ( InvokeEvent eventX )
	{
		
		System.out.println( System.currentTimeMillis( ) + " CSConnection.onInvoke " + eventX.id );
		
		if ( eventX.id.equals( "emptyCall" ) ) client.call( "onEmptyCall" );
		if ( eventX.id.equals( "nullCall" ) ) client.call( "onNullCall" , new Wrapper( ) );
		if ( eventX.id.equals( "numberCall" ) ) client.call( "onNumberCall" , new Wrapper( eventX.arguments.getDouble( 0 ) ) );
		if ( eventX.id.equals( "booleanCall" ) ) client.call( "onBooleanCall" , new Wrapper( eventX.arguments.getBoolean( 0 ) ) );
		if ( eventX.id.equals( "stringCall" ) ) client.call( "onStringCall" , new Wrapper( eventX.arguments.getString( 0 ) ) );
		if ( eventX.id.equals( "arrayCall" ) ) client.call( "onArrayCall" , new Wrapper( eventX.arguments.getList( 0 ) ) );
		if ( eventX.id.equals( "objectCall" ) ) client.call( "onObjectCall" , new Wrapper( eventX.arguments.getMap( 0 ) ) );
		if ( eventX.id.equals( "mixedCall" ) ) client.call( "onMixedCall" , new Wrapper( eventX.arguments.getMap( 0 ) ) );
		if ( eventX.id.equals( "listenedCall" ) ) client.callResult( eventX.channel , new WrapperList( new Wrapper( eventX.arguments.getString( 0 ) ) ) );
		
	}

}
