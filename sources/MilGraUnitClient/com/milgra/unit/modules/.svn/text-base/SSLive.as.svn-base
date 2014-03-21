package com.milgra.unit.modules
{
	import com.milgra.unit.skin.TextSkin;
	import com.milgra.unit.CustomEvent;
	import com.milgra.unit.skin.MixedSkin;
	import com.milgra.unit.RTMPConnection;
	import flash.text.TextFormat;
	import com.milgra.unit.ui.Scrollbar;
	import com.milgra.unit.ui.Textbox;
	import flash.media.Camera;
	import flash.media.Microphone;
	import flash.net.NetStream;
	import flash.net.NetConnection;
	
	
	public class SSLive
	{
		
		// nc - connection
		// rtmp - connection handler
		// counter - init helper
		
		public var nc : NetConnection;
		public var rtmp : RTMPConnection;
		public var counter : int = -1;
		
		public var streamIn : NetStream;
		public var streamOut : NetStream;
		
		public var camera : Camera;
		public var microphone : Microphone;
		
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
		 * SSLive constructor
		 * @param skinX skin
		 * @param hostX host server url
		 * @param helpX helper server url
		 **/
		
		public function SSLive( skinX : MixedSkin ,
								hostX : String , 
								helpX : String )
		{
			
			trace( "SSLive.construct " + skinX + " " + hostX + " " + helpX );
			
			// create

			rtmp = new RTMPConnection( );

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

			camera = Camera.getCamera( );
			microphone = Microphone.getMicrophone( );
			
			camera.setQuality( 80000 , 0 );
			microphone.rate = 44;
			
			output.setScrollbar( scroll );
			
			// event

			rtmp.addEventListener( CustomEvent.ACTIVATE , onActivate );
			rtmp.addEventListener( CustomEvent.DEACTIVATE , onDeactivate );
			
			// start			
			
			output.clear( );
			output.add( "Connecting..." );
			nc.connect( "rtmp://" + hostX + "/milgraunit" , "SSLive" , helpX );
			
		}
		
		/**
		 * Closes module
		 **/
		 
		public function close ( ) : void
		{
			
			trace( "SSLive.close" );
			rtmp.connection.close( );
			
		}
		
		/**
		 * Connection established
		 * @param eventX event
		 **/
		
		public function onActivate ( eventX : CustomEvent ) : void
		{
			
			trace( "SSLive.onActivate " + eventX );

			streamOut = new NetStream( nc );
			streamOut.attachAudio( microphone );
			streamOut.attachCamera( camera );
			streamOut.publish( "normal" , "live" );
			
			streamIn = new NetStream( nc );
			streamIn.bufferTime = .1;
			streamIn.play( "normalRemoteClone" );
			
			videos[ 0 ].attachCamera( camera );
			videos[ 1 ].attachNetStream( streamIn );
			
			fields[ 0 ].text = "camera";
			fields[ 1 ].text = "Proxied stream"; 
			
		}
		
		/**
		 * Connection failed
		 * @param eventX event
		 **/
		
		public function onDeactivate ( eventX : CustomEvent ) : void
		{
		
			trace( "SSLive.onDeactivate " + eventX );	
			output.add( "Connection failed." );
			
		}
		
		public function log ( logX : String ) : void
		{
			
			output.add( logX );
			
		}

	}
	
}