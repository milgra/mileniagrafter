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
	
	Tasks of Unit Tester Application 
	
		- create and pass incoming clients to tester classes

**/

import application.modules.*;

import com.milgra.server.api.Client;
import com.milgra.server.api.WrapperList;
import com.milgra.server.api.IApplication;

import java.util.HashMap;


public class Application implements IApplication
{

	// permament tv stream
	
	public HashMap < Client , IModule > clients;
	
	/**
	 * Application constructor
	 **/
	
	public Application ( )
	{
		
		System.out.println( System.currentTimeMillis( ) + " MilGra Unit Tester 1.0 " );
		
		clients = new HashMap < Client , IModule > ( );
		
	}
	
	public void onStart ( String nameX )
	{
		
	}
	
	/**
	 * Shutdown event
	 **/
	
	public void onClose ( )
	{
		
		System.out.println( System.currentTimeMillis( ) + "MilGraUnit.onClose" );
		
	}
	
	/**
	 * Client entering point
	 * @param clientX client
	 * @param argumentsX arguments
	 **/
	
	public void onEnter ( Client clientX , WrapperList argumentsX )
	{
		
		System.out.println( System.currentTimeMillis( ) + " MilGraUnit.onEnter " + clientX.getIp( ) + " " + argumentsX.getString( 0 ) );
		
		if ( argumentsX.size( ) > 0 )
		{
			
			if ( argumentsX.getString( 0 ).equals( "control" ) ) clientX.accept( );
			
			if ( argumentsX.getString( 0 ).equals( "CSConnection" ) ) 
			{
				CSConnection csConnection = new CSConnection( this );
				csConnection.onEnter( clientX , argumentsX );
				clients.put( clientX , csConnection );
			}

			if ( argumentsX.getString( 0 ).equals( "CSData" ) ) 
			{
				CSData csData = new CSData( this );
				csData.onEnter( clientX , argumentsX );
				clients.put( clientX , csData );
			}

			if ( argumentsX.getString( 0 ).equals( "CSLive" ) ) 
			{
				CSLive csLive = new CSLive( this );
				csLive.onEnter( clientX , argumentsX );
				clients.put( clientX , csLive );
			}

			if ( argumentsX.getString( 0 ).equals( "CSRecorded" ) ) 
			{
				CSRecorded csRecorded = new CSRecorded( this );
				csRecorded.onEnter( clientX , argumentsX );
				clients.put( clientX , csRecorded );
			}

			if ( argumentsX.getString( 0 ).equals( "CSBandwidth" ) ) 
			{
				CSBandwidth csBandwidth = new CSBandwidth( this );
				csBandwidth.onEnter( clientX , argumentsX );
				clients.put( clientX , csBandwidth );
			}

			if ( argumentsX.getString( 0 ).equals( "Stress" ) ) 
			{
				Stress stress = new Stress( this );
				stress.onEnter( clientX , argumentsX );
				clients.put( clientX , stress );
			}
			
			if ( argumentsX.getString( 0 ).equals( "SSConnection" ) ) 
			{
				SSConnection ssConnection = new SSConnection( this );
				ssConnection.onEnter( clientX , argumentsX );
				clients.put( clientX , ssConnection );
			}

			if ( argumentsX.getString( 0 ).equals( "SSData" ) ) 
			{
				SSData ssData = new SSData( this );
				ssData.onEnter( clientX , argumentsX );
				clients.put( clientX , ssData );
			}

			if ( argumentsX.getString( 0 ).equals( "SSLive" ) ) 
			{
				SSLive ssLive = new SSLive( this );
				ssLive.onEnter( clientX , argumentsX );
				clients.put( clientX , ssLive );
			}

			if ( argumentsX.getString( 0 ).equals( "SSRecord" ) ) 
			{
				CSRecorded csRecorded = new CSRecorded( this );
				csRecorded.onEnter( clientX , argumentsX );
				clients.put( clientX , csRecorded );
			}

			if ( argumentsX.getString( 0 ).equals( "SSStress" ) ) 
			{
				Stress ssStress = new Stress( this );
				ssStress.onEnter( clientX , argumentsX );
				clients.put( clientX , ssStress );
			}

			if ( argumentsX.getString( 0 ).equals( "StreamControl" ) ) 
			{
				StreamControl streamControl = new StreamControl( this );
				streamControl.onEnter( clientX , argumentsX );
				clients.put( clientX , streamControl );
			}

		}
		
	}
	
	/**
	 * Client leaving point
	 * @param clientX client 
	 **/
	
	public void onLeave ( Client clientX )
	{
		
		System.out.println( System.currentTimeMillis( ) + " MilGraUnit.onLeave " + clientX.getIp( ) );
		
		IModule module = clients.remove( clientX );
		if ( module != null ) module.onLeave( clientX );
		
	}	

}
