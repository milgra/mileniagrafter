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

package application;

/**

	Application class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080316
	
	Tasks of Admincontroller
	
		- listen for invokes
		- dispatch server state to client

**/

import com.milgra.server.api.Client;
import com.milgra.server.api.WrapperList;
import com.milgra.server.api.InvokeEvent;
import com.milgra.server.api.EventListener;


public class AdminController 
{
	
	public Client client;
	public Application parent;
	public EventListener invokeListener;
	
	/**
	 * Creates a new AdminController instance
	 * @param clientX client
	 * @param parentX parent
	 */
	
	public AdminController ( Client clientX , Application parentX )
	{
		
		// System.out.println( System.currentTimeMillis() + " " + clientX.id + " AdminController.construct" );

		invokeListener = new EventListener ( ) { public void onEvent ( InvokeEvent eventX ) { onInvoke ( eventX ); } };

		parent = parentX;
		client = clientX;
				
		client.addInvokeEventListener( invokeListener );
		
	}
	
	/**
	 * Invoke from client
	 * @param eventX invoke event
	 */
	
	public void onInvoke ( InvokeEvent eventX )
	{
			
		// System.out.println( System.currentTimeMillis() + " " + client.getId( ) + " AdminController.onInvoke " + eventX.id );
		
		if ( eventX.id.equals( "load" ) ) parent.loadApplication( eventX.arguments.getString( 0 ) ); else
		if ( eventX.id.equals( "unload" ) ) parent.loadApplication( eventX.arguments.getString( 0 ) ); else
		if ( eventX.id.equals( "refresh" ) ) parent.refreshApplications( );
		
	}
	
	/**
	 * Updates exchange data
	 * @param classX
	 * @param dataX
	 */
	
	public void updateData ( WrapperList dataX )
	{
	
		// System.out.println( System.currentTimeMillis() + " " + client.getId( ) + " AdminController.updateData" );
		
		client.call( "updateData" , dataX );
		
	}
	
}
