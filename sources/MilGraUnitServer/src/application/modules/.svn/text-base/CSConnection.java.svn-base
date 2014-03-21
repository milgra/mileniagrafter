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

	CSConnection class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080316
	
	Tasks of CSConnection 
	
		- accept or reject connecting client to test outgoing staus events

**/

import com.milgra.server.api.Client;
import com.milgra.server.api.Wrapper;
import com.milgra.server.api.WrapperList;
import com.milgra.server.api.InvokeEvent;
import com.milgra.server.api.IApplication;
import com.milgra.server.api.EventListener;


public class CSConnection implements IModule
{
		
	/**
	 * CSConnection constructor
	 * @param applicationX
	 */
	
	public CSConnection ( IApplication applicationX )
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

		System.out.println( System.currentTimeMillis( ) + " CSConnection.onEnter " + clientX + " " +argumentsX );

		// get mode
	
		String mode = argumentsX.getString( 1 );
		
		// reject
		
		if ( mode.equals( "reject" ) ) clientX.reject( new Wrapper( "Requested rejection" ) );
		
		// accept
		
		if ( mode.equals( "accept" ) )
		{
			
			// create invoke listener
			
			EventListener invokeListener = new EventListener ( )
			{
					
				public void onEvent ( InvokeEvent eventX ) { onInvoke( eventX ); }
					
			};
			
			// init

			clientX.addInvokeEventListener( invokeListener );
			clientX.accept( new Wrapper( "Requested acception" ) );
		
		}
		
	}
	
	/**
	 * Closes module
	 **/
	
	public void onLeave ( Client clientX ) 
	{
		
		System.out.println( System.currentTimeMillis( ) +  " CSConnection.onLeave " );
		
		
	}
	
	/**
	 * Invoke event listener
	 * @param eventX invoke event
	 */
	
	public void onInvoke ( InvokeEvent eventX )
	{
		
		System.out.println( System.currentTimeMillis( ) +  " CSConnection.onInvoke " + eventX.id );
		
		if ( eventX.id.equals( "disconnect" ) ) eventX.client.detach( );		
		
	}

}
