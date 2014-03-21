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
	
	import flash.net.Responder;
	import flash.net.NetConnection;
	import flash.events.EventDispatcher;

	/*
		Tasks of Rtmpbandwidth 
			- measure incoming and outgoind bandwidth between client and server
	
	*/
	
	public class RTMPBandwidth extends EventDispatcher
	{
		
		public static var EVENT_COMPLETE : String = "rtmp_complete";
		public static var EVENT_PROGRESS : String = "rtmp_progress";
		
		// connection - netconnection
		// upResponder - responder for upload checking
		// downResponder - responder for download checking
		
		public var connection : NetConnection;
		public var upResponder : Responder;
		public var downResponder : Responder;
		
		// timeout - maximum check time
		// startTime - check start time
		// baseString - default string section for testing
		// testString - tester string
		
		public var timeout : Number = 5000;
		public var startTime : Number;
		public var baseString : String;
		public var testString : String;
		
		// containers for timestamps

		public var sendTimes : Array = [ ];
		public var readTimes : Array = [ ];
		
		// bandIndex - index of actual tested value
		// bandValues - tested bandwidths, multiplied 16K
		
		public var bandIndex : int = 0;
		public var bandValues : Array = [ 0 , 0 , 16384 , 32768 , 49152 , 65536 , 81920 , 98304 , 114688 , 131072 , 147456 ,
										  163840 , 180224 , 196608 , 212992 ];
		
		/**
		 * RTMPBandwidth constructor
		 * @param pConnection connection
		 **/
		
		public function RTMPBandwidth ( pConnection : NetConnection )
		{
			
			trace( "RTMPBandwidth " + pConnection );
			
			// create
			
			upResponder = new Responder( onUploadCheck );
			downResponder = new Responder( onDownloadCheck );
			
			// set

			baseString = "";
			connection = pConnection;
			for ( var index : int = 0 ; index < 16384 ; index++ ) baseString += "a";
			
		}
		
		public function uploadCheck ( ) : void
		{
			
			trace( "uploadCheck " );
			
			startTime = new Date( ).time;
			bandIndex = 0;
			
			sendTimes = [ ];
			readTimes = [ ];
			testString = "";
			
			sendUploadPacket( );
			
		}
		
		public function sendUploadPacket ( ) : void
		{
			
			trace( "sendUploadPacket" );
			
			// create
		
    		var index : int;
    		var message : Object;
    		
    		// set
    		
    		message = new Object( );
			message.size = 0;	
    		
    		if ( bandIndex > 1 ) testString += baseString;
    		
    		message.value = testString;
    		    		
    		// store sendtime and send
    		
    		++bandIndex;
    		connection.call( "measure" , upResponder , message );				
    		sendTimes.push( new Date( ).time );
			
		}
		
		public function onUploadCheck ( pMessage : Object ) : void
		{
			
			trace( "onUploadCheck " + pMessage.read + " " + pMessage.send );
			
			readTimes.push( pMessage.read );
						
			if ( bandIndex == bandValues.length || ( new Date( ).time - startTime ) > timeout )
    		{
    			
    			var empty1 : Number = readTimes[ 0 ] - sendTimes[ 0 ];
    			var empty2 : Number = readTimes[ 1 ] - sendTimes[ 1 ];
    			
    			var overall : Number = 0;
    			var difference : Number = ( empty1 + empty2 ) / 2;
    			
    			trace( empty1 + " " + empty2 + "difference: " + difference );
    			
    			for ( var index : int = 2 ; index < readTimes.length ; index++ )
    			{
    				
    				trace( "read: " + readTimes[ index ] + " difference: " + difference + " send: " + sendTimes[ index ] );
    				
    				var delay : Number = ( readTimes[ index ] + difference ) - sendTimes[ index ];
    				
    				trace( "delay: " + delay + " for: " + bandValues[ index ] );
    				
    				//string length + amf object + amf number ( size ) + object keys + rtmp header + tcp header 
    				
    				var value : Number = bandValues[ index ] + 4 + 9 + 13 + 12 + 32;
    				overall += value / ( delay / 1000 );
    				
    			}
    			
    			var band : Number = Math.round( overall / ( readTimes.length - 2 ) );
    			
    			trace( "bandwidth: " + band + " bytes/sec" );

				var event : CustomEvent = new CustomEvent( EVENT_COMPLETE );
				event.bandwidth = band;
				dispatchEvent( event );
    			    			
    		}
    		else sendUploadPacket( );
    		
    		var event1 : CustomEvent = new CustomEvent( EVENT_PROGRESS );
			event1.ratio = bandIndex / bandValues.length;
			
			dispatchEvent( event1 );	
			
		}
		
		public function downloadCheck ( ) : void
		{
			
			trace( "downloadCheck " );
			
			startTime = new Date( ).time;
			bandIndex = 0;
			
			sendTimes = [ ];
			readTimes = [ ];
			
			sendDownloadPacket( );
			
		}
			
		public function sendDownloadPacket ( ) : void
		{
			
			trace( "sendDownloadPacket" );
			
			// create
		
    		var index : int;
    		var message : Object;
    		
    		// set
    		
    		message = new Object( );
			message.size = bandValues[ bandIndex ];	
    		message.value = "";
    		
    		// store sendtime and send
    		
    		++bandIndex;
    		connection.call( "measure" , downResponder , message );				
			
		}
		
		public function onDownloadCheck ( pMessage : Object ) : void
		{
			
			trace( "onDownloadCheck " + pMessage.read + " " + pMessage.send );
			
			sendTimes.push( pMessage.send );
			readTimes.push( new Date( ).time );
			
			if ( bandIndex == bandValues.length || ( new Date( ).time - startTime ) > timeout )
    		{
    			
    			var empty1 : Number = readTimes[ 0 ] - sendTimes[ 0 ];
    			var empty2 : Number = readTimes[ 1 ] - sendTimes[ 1 ];
    			
    			var overall : Number = 0;
    			var difference : Number = ( empty1 + empty2 ) / 2;
    			    			
    			trace( empty1 + " " + empty2 + "difference: " + difference );

    			for ( var index : int = 2 ; index < readTimes.length ; index++ )
    			{
    				
    				trace( "read: " + readTimes[ index ] + " difference: " + difference + " send: " + sendTimes[ index ] );
    				
    				var delay : Number = readTimes[ index ] - ( sendTimes[ index ] + difference );
    				
    				trace( "delay: " + delay + " for: " + bandValues[ index ] );
    				
    				// string length + amf object + amf number ( size ) + object keys + rtmp header + tcp header 
    				
    				var value : Number = bandValues[ index ] + 4 + 9 + 13 + 12 + 32;
    				overall += value / ( delay / 1000 );
    				
    			}
    			
    			var band : Number = Math.round( overall / ( readTimes.length - 2 ) );
    			
    			trace( "bandwidth: " + band + " bytes/sec" );

				var event : CustomEvent = new CustomEvent( EVENT_COMPLETE );
				event.bandwidth = band;
				dispatchEvent( event );
    			    			
    		}
    		else sendDownloadPacket( );
    		
    		var event1 : CustomEvent = new CustomEvent( EVENT_PROGRESS );
			event1.ratio = bandIndex / bandValues.length;	
    		
   			dispatchEvent( event1 );	
    		
		}

	}
	
}