package com.milgra.unit.modules
{
	
	import com.milgra.unit.ui.Textbox;
	import com.milgra.unit.ui.Scrollbar;
	
	import com.milgra.unit.CustomEvent;
	import com.milgra.unit.RTMPConnection;
	import com.milgra.unit.skin.MixedSkin;
	
	import flash.net.NetStream;
	import flash.net.NetConnection;

	import flash.text.TextFormat;
	import flash.events.NetStatusEvent;
	
	/*
		Tasks of CSRecorded
			- test every possible recorded stream play related functions
	
	*/
	
	public class CSRecorded
	{
		
		// nc - connection
		// rtmp - connection handler
		// counter - init helper
		
		public var nc : NetConnection;
		public var rtmp : RTMPConnection;
		public var counter : int = -1;
		
		public var stream : NetStream;
		public var streams : Array;
		
		// videos - videos
		// output - text output box
		// scroll - scrollbar for textbox
		// format - textformat

		public var fields : Array;
		public var videos : Array;		
		public var output : Textbox;
		public var scroll : Scrollbar;
		public var format : TextFormat;
		
		/**
		 * CSRecorded constructor
		 * @param skinX skin
		 * @param hostX host server url
		 * @param helpX helper server url
		 **/
		
		public function CSRecorded( skinX : MixedSkin ,
								  	hostX : String , 
								  	helpX : String )
		{
			
			trace( "CSLive.construct " + skinX + " " + hostX + " " + helpX );
			
			// create

			rtmp = new RTMPConnection( );
			
			streams = new Array( );
			fields = new Array( );
			videos = new Array( );
			format = new TextFormat( "Verdana" , 12 , 0 );
			scroll = new Scrollbar( skinX.scrollbarSkin0 ); 	
			output = new Textbox( skinX.field0 , format );
			
			while ( ++ counter < 6 ) 
			{
				videos[ counter ] = skinX[ "video" + counter ];
				fields[ counter ] = skinX[ "field" + ( counter + 1 ) ];
			}
			
			// set

			nc = rtmp.connection;
			nc.client = this;
			
			// event

			rtmp.addEventListener( CustomEvent.ACTIVATE , onActivate );
			rtmp.addEventListener( CustomEvent.DEACTIVATE , onDeactivate );
			
			// start			
			
			output.clear( );
			output.add( "Connecting..." );
			nc.connect( "rtmp://" + hostX + "/milgraunit" , "CSRecorded" );
			
		}
		
		/**
		 * Close module
		 **/
		
		public function close ( ) : void
		{
			
			trace( "CSRecorded.close" );
			nc.close( );
			
		}
		
		/**
		 * Connection established
		 * @param eventX event
		 **/
		
		public function onActivate ( eventX : CustomEvent ) : void
		{
			
			trace( "CSRecorded.onActivate " + eventX );
			playDynamic( );
			
		}
		
		/**
		 * Connection failed
		 * @param eventX event
		 **/
		
		public function onDeactivate ( eventX : CustomEvent ) : void
		{
		
			trace( "CSRecorded.onDeactivate " + eventX );	
			output.add( "Connection failed." );
			
		}
	
		/**
		 * Playing a recorded stream only for this client ( dynamic )
		 **/
		
		public function playDynamic ( ) : void 
		{
					
			output.add( "Playing a dynamic stream" );	
			
			stream = new NetStream( nc );
			stream.client = this;
			stream.play( "phone.flv" );
			stream.addEventListener( NetStatusEvent.NET_STATUS , onDynamic );
			streams.push( stream );

			fields[ 0 ].text = "Play flv";
			videos[ 0 ].attachNetStream( stream );
			
		}
		
		/**
		 * Dynamic play status
		 * @param eventX status
		 **/
		
		public function onDynamic ( eventX : NetStatusEvent ) : void
		{
			
			trace( eventX.info.code );
			output.add( "Status: " + eventX.info.code );
			
			stream.removeEventListener( NetStatusEvent.NET_STATUS , onDynamic );
			
			if ( eventX.info.code == "NetStream.Play.Reset" ) output.add( "SUCCESS" );
			else output.add( "FAILURE" );
			
			playStatic( );
			
		}

		/**
		 * Playing a static recorded stream which is played on the server continusly
		 **/
		
		public function playStatic ( ) : void 
		{
			
			output.add( "Playing a static recorded stream ( tv stream )" );	
			
			stream = new NetStream( nc );
			stream.client = this;
			stream.play( "phone" );
			stream.addEventListener( NetStatusEvent.NET_STATUS , onStatic );
			streams.push( stream );

			fields[ 1 ].text = "Play static flv";
			videos[ 1 ].attachNetStream( stream );
			
		}
		
		/**
		 * Static stream status
		 * @param eventX event
		 **/

		public function onStatic ( eventX : NetStatusEvent ) : void
		{
			
			trace( eventX.info.code );
			output.add( "Status: " + eventX.info.code );
			
			stream.removeEventListener( NetStatusEvent.NET_STATUS , onStatic );
			
			if ( eventX.info.code == "NetStream.Play.Reset" ) output.add( "SUCCESS" );
			else output.add( "FAILURE" );
			
			output.add( "Finished tests." );
			
		}
		
		public function onMetaData ( infoX : Object ) : void
		{
			
			
		}	

	}
	
}