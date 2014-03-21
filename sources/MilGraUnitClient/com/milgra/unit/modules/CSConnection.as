package com.milgra.unit.modules
{

	import com.milgra.unit.CustomEvent;
	import com.milgra.unit.ui.Textbox;
	import com.milgra.unit.ui.Scrollbar;
	import com.milgra.unit.skin.TextSkin;
	
	import flash.net.NetConnection;
	import flash.net.ObjectEncoding;
	
	import flash.text.TextFormat;
	import flash.events.NetStatusEvent;
	import flash.events.EventDispatcher;
	
	/*
		Tasks of CSConnection
			- test every possible connection situations, status and error events
	
	*/	
	
	public class CSConnection extends EventDispatcher
	{
		
		// skin - module skin
		
		public var skin : TextSkin;
		
		// hostUrl - url of host application
		// helpUrl - url of helper application
		// connection - connection
		
		public var hostUrl : String;
		public var helpUrl : String;
		public var connection : NetConnection;
		
		// output - textbox
		// scroll - scroller for textbox
		// format - textformat for box
		
		public var output : Textbox;
		public var scroll : Scrollbar;
		public var format : TextFormat;
		
		/**
		 * CSConnection construct
		 * @param skinX skin
		 * @param hostUrlX host url
		 * @param helpUrlX helper url
		 **/
		
		public function CSConnection ( skinX : TextSkin ,
									   hostUrlX : String , 
									   helpUrlX : String )
		{

			trace( "CSConnection.construct " + skinX + " " + hostUrl + " " + helpUrl );
			
			// create
			
			format = new TextFormat( "Verdana" , 10 , 0 );
			scroll = new Scrollbar( skinX.scrollbarSkin0 ); 	
			output = new Textbox( skinX.field0 , format );
			
			// set
			
			hostUrl = hostUrlX;
			helpUrl = helpUrlX;
			
			skin = skinX;
			
			// start
			
			output.add( "This module tests all possible connection related mechanism." );
			output.add( "- rtmp handshake\n- connection invoke\n- connection result\n- application property of response\n" );
			
			invalidServer( );
			
		}
		
		/**
		 * Closes module
		 **/
		
		public function close ( ) : void
		{
			
			trace( "CSConnection.close" );
			connection.close( );
			
		}
		
		/**
		 * Connecting to an invalid url
		 **/

		public function invalidServer ( ) : void
		{

			output.add( "Connecting to unavailable server... " );
			
			connection = new NetConnection( );
			connection.objectEncoding = ObjectEncoding.AMF0;
			connection.addEventListener( NetStatusEvent.NET_STATUS , onInvalidServerStatus );
			connection.connect( "rtmp://fake.fke/fake" );
			
		}
		
		/**
		 * Invalid connection status
		 * @param eventX event
		 **/
	
		public function onInvalidServerStatus ( eventX : NetStatusEvent ) : void
		{

			output.add( "Status: " + eventX.info.code );

			connection.removeEventListener( NetStatusEvent.NET_STATUS , onInvalidServerStatus );
						
			if ( eventX.info.code == CustomEvent.FAILURE ) output.add( "SUCCESS" );
			else output.add( "FAILED" );
			
			invalidApplication( );	
			
		}
		
		/**
		 * Connecting to an invalid application
		 **/	

		public function invalidApplication ( ) : void
		{
			
			output.add( "Connecting to invalid application... " );
			
			connection = new NetConnection( );
			connection.objectEncoding = ObjectEncoding.AMF0;
			connection.addEventListener( NetStatusEvent.NET_STATUS , onInvalidApplicationStatus );
			connection.connect( "rtmp://" + hostUrl + "/fake" );
		 	
		}
		
		/**
		 * Invalid application status
		 * @param eventX event
		 **/
		
		public function onInvalidApplicationStatus ( eventX : NetStatusEvent ) : void
		{

			connection.removeEventListener( NetStatusEvent.NET_STATUS , onInvalidApplicationStatus );
						
			if ( eventX.info.code == CustomEvent.REJECTION )
			{
				
				output.add( "Status: " + eventX.info.code + " application: " + eventX.info.application );
				output.add( "SUCCESS" );
				
			}
			else 
			{
				
				output.add( "Status: " + eventX.info.code );
				output.add( "FAILED" );
				
			}
			
			rejectedConnection( );					
			
		}

		/**
		 * Connecting to a rejecting application
		 **/
		
		public function rejectedConnection ( ) : void
		{
			
			output.add( "Connecting to valid application, application rejects connection " );
			
			connection = new NetConnection( );
			connection.objectEncoding = ObjectEncoding.AMF0;
			connection.addEventListener( NetStatusEvent.NET_STATUS , onRejectedConnectionStatus );
			connection.connect( "rtmp://" + hostUrl + "/milgraunit" , "CSConnection" , "reject" );
			
			
		}
		
		/**
		 * Rejected application status
		 * @param eventX event
		 **/
		
		public function onRejectedConnectionStatus ( eventX : NetStatusEvent ) : void
		{

			connection.removeEventListener( NetStatusEvent.NET_STATUS , onRejectedConnectionStatus );
			
			if ( eventX.info.code == CustomEvent.REJECTION )
			{
				
				output.add( "Status: " + eventX.info.code + " application: " + eventX.info.application );
				output.add( "SUCCESS" );
				
			}
			else 
			{
				
				output.add( "Status: " + eventX.info.code );
				output.add( "FAILED" );
				
			}
			
			acceptedConnection( );
			
		}
		
		/**
		 * Connecting to an accepting application
		 **/

		public function acceptedConnection ( ) : void
		{
			
			output.add( "Connecting to valid application, application accepts connection " );
			
			connection = new NetConnection( );
			connection.objectEncoding = ObjectEncoding.AMF0;
			connection.addEventListener( NetStatusEvent.NET_STATUS , onAcceptedConnectionStatus );
			connection.connect( "rtmp://" + hostUrl + "/milgraunit" , "CSConnection" , "accept" );
			
		}
		
		/**
		 * Accepted connection status
		 * @param eventX event 
		 **/
		 
		public function onAcceptedConnectionStatus ( eventX : NetStatusEvent ) : void
		{
			
			connection.removeEventListener( NetStatusEvent.NET_STATUS , onAcceptedConnectionStatus );
			
			if ( eventX.info.code == CustomEvent.SUCCESS )
			{

				output.add( "Status: " + eventX.info.code + " application: " + eventX.info.application );
				output.add( "SUCCESS" );		
				
			}
			else
			{

				output.add( "Status: " + eventX.info.code );
				output.add( "FAILED" );			
				
			}
			
			closedConnection( );
			
		}
		
		/**
		 * Checking close event status
		 **/
		
		public function closedConnection ( ) : void
		{
			
			output.add( "Telling server to disconnect previous client..." );
			
			connection.addEventListener( NetStatusEvent.NET_STATUS , onClosedConnectionStatus );			
			connection.call( "disconnect" , null );			
			
		}
		
		/**
		 * Closed connection event
		 * @param eventX event
		 **/
		
		public function onClosedConnectionStatus ( eventX : NetStatusEvent ) : void
		{

			connection.removeEventListener( NetStatusEvent.NET_STATUS , onClosedConnectionStatus );
		
			output.add( "Status: " + eventX.info.code );
			
			if ( eventX.info.code == CustomEvent.CLOSURE ) output.add( "SUCCSESS" );
			else output.add( "FAILED" );
			
			output.add( "Tests finished." );
			
		}
		
	}
	
}