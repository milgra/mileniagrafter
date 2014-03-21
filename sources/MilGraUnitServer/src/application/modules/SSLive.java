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
	
	SSLive class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080316
	
	Tasks of SSLive 
	
		- push and pull a stream to and from a remote server

**/

import com.milgra.server.api.Client;
import com.milgra.server.api.Stream;
import com.milgra.server.api.Wrapper;
import com.milgra.server.api.StatusEvent;
import com.milgra.server.api.WrapperList;
import com.milgra.server.api.IApplication;
import com.milgra.server.api.EventListener;


public class SSLive implements IModule
{
	
	public String url;
	public Stream stream;
	public Client client;
	public Client testClient;
	public IApplication application;
	
	/**
	 * SSLive constructor
	 * @param applicationX mother application
	 */
	
	public SSLive ( IApplication applicationX )
	{
		
		System.out.println( System.currentTimeMillis( ) + " SSLive.construct " );
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

		url = argumentsX.getString( 1 );

		client = clientX;
		client.accept( );

		client.call( "log" , new Wrapper( "Connection... " ) );
		
		// create remote client

		EventListener statusListener = new EventListener ( )
		{
				
			public void onEvent ( StatusEvent eventX ) { onStatus( eventX ); }
				
		};

		WrapperList arguments = new WrapperList( );
		arguments.add( "CSLive" );

		testClient = new Client( application );
		testClient.addStatusEventListener( statusListener );
		testClient.connect( "rtmp://" + url + "/milgraunit" , arguments );
		
	}
	
	/**
	 * Closes module
	 **/
	
	public void onLeave ( Client clientX ) 
	{
		
		System.out.println( System.currentTimeMillis( ) +  " SSLive.onClose " );		
		
	}	
	
	/**
	 * Remote client status
	 * @param eventX statusevent
	 */
	
	public void onStatus ( StatusEvent eventX )
	{
		
		System.out.println( System.currentTimeMillis( ) + " SSLive.onStatus " + eventX.code );
		client.call( "log" , new Wrapper( "Status: " + eventX.code ) );
		
		// connection status

		if ( eventX.code.equals( StatusEvent.SUCCESS ) ) normalPublish( );
		
		if ( !eventX.info.containsKey( "details" ) ) return;
		
		// stream status
		
		if ( eventX.info.getString( "details" ).equals( "normalRemote" ) )
		{

			// publish start event came
			
			if ( eventX.code.equals( StatusEvent.PUBLISHSTART ) )
			{
				
				// publishing is started, we can start playing
				
				client.call( "log" , new Wrapper( "SUCCESS" ) );
				normalPlay( );
				
			}
			
			if ( eventX.code.equals( StatusEvent.PLAYRESET ) ) 
			{
				
				// playing is started
				
				client.call( "log" , new Wrapper( "SUCCESS" ) );
				client.call( "log" , new Wrapper( "Finished testClients." ) );
				
			}

		}
		
	}
	
	/**
	 * Playing normalClone stream from remote server
	 */
	
	public void normalPlay ( )
	{

		System.out.println( System.currentTimeMillis( ) + " SSLive.normalPlay " );
		client.call( "log" , new Wrapper( "Playing normal stream " ) );
		
		stream = new Stream( "normalRemoteClone" , testClient );
		stream.play( "normalRemote" );
		
	}
	
	public void normalPublish ( )
	{
		
		System.out.println( System.currentTimeMillis( ) + " SSLive.normalPublish " );

		client.call( "log" , new Wrapper( "Publishing normal stream " ) );
		
		stream = new Stream( "normalRemote" , testClient );
		stream.publish( "normal" , "live" );
		
	}

}
