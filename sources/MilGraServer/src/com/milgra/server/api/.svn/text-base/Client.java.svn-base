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
   Foundataion, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA. 
   
*/ 

package com.milgra.server.api;

/**
	
	Client class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080316

	Tasks of Client
	
		- provide an API for client related functions and properties

**/

import java.util.HashMap;
import com.milgra.server.ClientController;


public class Client
{
	
	public ClientController controller;
	
	public Client ( ClientController controllerX )
	{
		
		controller = controllerX;
		controller.client = this;
		
	}
	
	public Client ( IApplication applicationX )
	{
		
		controller = new ClientController( );
		controller.client = this;
		controller.setApplication( applicationX );
		
	}
	
	// attribute getters
	
	public long getId ( ) { return controller.id; } 
	public long getPing ( ) { return controller.ping; }
	
	public long getBytesIn ( ) { return controller.bytesIn; }
	public long getBytesOut ( ) { return controller.bytesOut; };
	
	public double getBandIn ( ) { return controller.bandIn; }
	public double getBandOut ( ) { return controller.bandOut; }
		
	public String getIp ( ) { return controller.ip; } 
	public String getAgent ( ) { return controller.agent; }
	public String getReferrer ( ) { return controller.referrer; }

	// functions used in passive mode
	
	public void accept ( ) { accept( new Wrapper( ) ); }
	public void accept ( Wrapper wrapperX ) { controller.accept( wrapperX ); }
	public void reject ( Wrapper wrapperX ) { controller.reject( wrapperX ); }
	public void detach ( ) { controller.detach( ); }
	
	// functions used in active mode
	
	public void connect ( String uriX , Wrapper argumentX ) { controller.connect( uriX , argumentX ); }
	public void connect ( String uriX , WrapperList argumentsX ) { controller.connect( uriX , argumentsX ); }
	
	// common functions
	
	public void call ( String invokeID ) { controller.call( invokeID ); }	
	public void call ( String invokeID , Wrapper argumentX ) { controller.call( invokeID , argumentX ); }	
	public void call ( String invokeID , WrapperList argumentsX ) { controller.call( invokeID , argumentsX ); }	
	public void callResult ( double channelX , Wrapper argumentX ) { controller.callResult( channelX, argumentX ); }
	public void callResult ( double channelX , WrapperList argumentsX ) { controller.callResult( channelX , argumentsX ); }
	
	public void addStreamEventListener ( EventListener listenerX ) { controller.addStreamEventListener( listenerX ); }
	public void addInvokeEventListener ( EventListener listenerX ) { controller.addInvokeEventListener( listenerX ); }
	public void addStatusEventListener ( EventListener listenerX ) { controller.addStatusEventListener( listenerX ); }

	public HashMap < Integer , Stream > getPlayers ( ) { return controller.getPlayers( ); }	
	public HashMap < Integer , Stream > getRouters ( ) { return controller.getRouters( ); }

}
