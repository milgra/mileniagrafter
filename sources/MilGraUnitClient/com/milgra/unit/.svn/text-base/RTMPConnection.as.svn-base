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

package com.milgra.unit
{
	
	import com.milgra.unit.CustomEvent;
	
	import flash.net.NetConnection;
	import flash.net.ObjectEncoding;
	
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.events.NetStatusEvent;
	import flash.events.AsyncErrorEvent;
	import flash.events.EventDispatcher;
	import flash.events.SecurityErrorEvent;
	
	/*
	
		Tasks of Milgraunitclient
			- init unit tester client
			- check server availability
			- initialize new tests
	
	*/
	
	public class RTMPConnection extends EventDispatcher
	{
		
		public static var ACCEPTION : String = "NetConnection.Connect.Success";
		public static var REJECTION : String = "NetConnection.Connect.Rejected";
		
		public static var FAILURE : String = "NetConnection.Connect.Failed";
		public static var CLOSURE : String = "NetConnection.Connect.Closed";
		
		public var activated : Boolean;
		public var connection : NetConnection;
		
		/**
		 * RTMPConnection constructor
		 **/
		
		public function RTMPConnection ( )
		{
			
			trace( "RTMPConnection.construct" );

			connection = new NetConnection( );
			connection.objectEncoding = ObjectEncoding.AMF0;
			connection.addEventListener( IOErrorEvent.IO_ERROR , onIOError );
			connection.addEventListener( NetStatusEvent.NET_STATUS , onStatus );
			connection.addEventListener( AsyncErrorEvent.ASYNC_ERROR , onAsyncError );
			connection.addEventListener( SecurityErrorEvent.SECURITY_ERROR , onSecurityError );

		}
		
		/**
		 * netStatus event listener
		 * @param pEvent netstatus
		 **/
		
		public function onStatus ( pEvent : NetStatusEvent ) : void
		{
			
			trace( "RTMPConnection.onStatus " + pEvent.info.code );
			
			switch ( pEvent.info.code )
			{
				
				case ACCEPTION :
				
					activated = true;
					dispatchEvent( new CustomEvent( CustomEvent.ACTIVATE ) );
					break;
					
				case REJECTION :
				
					dispatchEvent( new CustomEvent( CustomEvent.DEACTIVATE ) );
					break;
					
				case FAILURE : 
				
					dispatchEvent( new CustomEvent( CustomEvent.DEACTIVATE ) );
					break;
					
				case CLOSURE :
				
					activated = false;
					dispatchEvent( new CustomEvent( CustomEvent.DEACTIVATE ) );
					break;
				
				
			}
			
		}
		
		/**
		 * Error events
		 * @param pEvent error event
		 **/
		
		public function onIOError ( pEvent : IOErrorEvent ) : void
		{
			trace( "AMFConnection.onIOError : " + pEvent );
		}
		
		public function onAsyncError ( pEvent : AsyncErrorEvent ) : void
		{
			trace( "AMFConnection.onAsyncError : " + pEvent );	
		}
		
		public function onSecurityError ( pEvent : SecurityErrorEvent ) : void
		{
			trace( "AMFConnection.onSecurityError : " + pEvent );	
		}

	}
	
}