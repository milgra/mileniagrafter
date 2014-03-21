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

	CSLive class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080316
	
	Tasks of CSLive 
	
		- enable/disable stream requests to test streaming/stream events

**/

import com.milgra.server.api.Client;
import com.milgra.server.api.InvokeEvent;
import com.milgra.server.api.StreamEvent;
import com.milgra.server.api.WrapperList;
import com.milgra.server.api.IApplication;
import com.milgra.server.api.EventListener;


public class CSLive implements IModule
{
	
	// testing client
	
	public Client client;
	
	/**
	 * CSLive constructor
	 * @param applicationX mother application
	 */
	
	public CSLive ( IApplication applicationX ) { }
	
	/**
	 * Client entering point
	 * @param clientX client
	 * @param argumentsX arguments
	 */
	
	public void onEnter ( Client clientX , WrapperList argumentsX )
	{
		
		System.out.println( System.currentTimeMillis( ) + " CSLive.onEnter " );
		
		// create

		EventListener invokeListener = new EventListener ( )
		{
				
			public void onEvent ( InvokeEvent eventX ) { onInvoke( eventX ); }
				
		};
		
		// set

		client = clientX;
		
		// start
		
		client.addInvokeEventListener( invokeListener );
		client.accept( );		
		
	}
	
	/**
	 * Closes module
	 **/
	
	public void onLeave ( Client clientX ) 
	{
		
		System.out.println( System.currentTimeMillis( ) +  " CSLive.onClose " );
		
		
	}	
	/**
	 * Invoke event listener
	 * @param eventX event
	 */
	
	public void onInvoke ( InvokeEvent eventX )
	{
		
		System.out.println( System.currentTimeMillis( ) + " CSLive.onInvoke " + eventX.id );

		if ( eventX.id.equals( "addStreamListeners" ) )
		{
			
			// adding stream event listener
			
			EventListener streamListener = new EventListener ( )
			{
				public void onEvent ( StreamEvent event ) { onStreamEvent( event ); };
			};
			
			client.addStreamEventListener( streamListener );
			
		}		
		
	}
	
	/**
	 * Stream event
	 * @param eventX event
	 */
	
	public void onStreamEvent ( StreamEvent eventX )
	{
		
		System.out.println( System.currentTimeMillis( ) + " CSLive.onStreamEvent " + eventX.stream.name );

		if ( eventX.stream.name.equals( "disabled" ) ) eventX.stream.disable( );
		if ( eventX.stream.name.equals( "enabled" ) ) eventX.stream.enable( );
		
	}

}
