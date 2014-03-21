package com.milgra.unit.modules
{
	
	
	import com.milgra.unit.CustomEvent;
	import com.milgra.unit.ui.Textbox;
	import com.milgra.unit.ui.Scrollbar;
	import com.milgra.unit.skin.TextSkin;
	import com.milgra.unit.RTMPConnection;
	
	import flash.net.NetConnection;
	import flash.net.ObjectEncoding;
	
	import flash.text.TextFormat;
	import flash.events.NetStatusEvent;
	import flash.events.EventDispatcher;
	
	
	public class SSData
	{

		// skin - module skin
		
		public var skin : TextSkin;
		
		// hostUrl - url of host application
		// helpUrl - url of helper application
		// connection - connection
		
		public var host : String;
		public var help : String;
		
		public var rtmp : RTMPConnection;
		public var connection : NetConnection;
		
		// output - textbox
		// scroll - scroller for textbox
		// format - textformat for box
		
		public var output : Textbox;
		public var scroll : Scrollbar;
		public var format : TextFormat;
		
		/**
		 * SSConnection construct
		 * @param skinX skin
		 * @param hostUrlX host url
		 * @param helpUrlX helper url
		 **/
		
		public function SSData ( skinX : TextSkin ,
								 hostX : String , 
								 helpX : String )
		{

			trace( "CSConnection.construct " + skinX + " " + hostX + " " + help );
			
			// create
			
			rtmp = new RTMPConnection( );
			
			format = new TextFormat( "Verdana" , 12 , 0 );
			scroll = new Scrollbar( skinX.scrollbarSkin0 ); 	
			output = new Textbox( skinX.field0 , format );
			
			// set
			
			host = hostX;
			help = helpX;
			
			skin = skinX;
			
			output.setScrollbar( scroll );
			
			rtmp.connection.client = this;
			rtmp.addEventListener( CustomEvent.ACTIVATE , onActivate );
			rtmp.addEventListener( CustomEvent.DEACTIVATE , onDeactivate );
			
			// start
			
			rtmp.connection.connect( "rtmp://" + host + "/milgraunit" , "SSData" , help );
			
		}
		
		/**
		 * Closes module
		 **/
		 
		public function close ( ) : void
		{
			
			trace( "SSData.close" );
			rtmp.connection.close( );
			
		}
		
		
		/**
		 * Connection established
		 * @param eventX event
		 **/
		
		public function onActivate ( eventX : CustomEvent ) : void
		{
			
			trace( "CSLive.onActivate " + eventX );
			
		}
		
		/**
		 * Connection failed
		 * @param eventX event
		 **/
		
		public function onDeactivate ( eventX : CustomEvent ) : void
		{
		
			trace( "CSLive.onDeactivate " + eventX );	
			output.add( "Connection failed." );
			
		}	
			
		public function log ( logX : String ) : void
		{
			
			output.add( logX );
			
		}

	}
	
}