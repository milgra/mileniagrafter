package com.milgra.unit.modules
{
	
	import flash.net.NetStream;
	import flash.net.NetConnection;
	import flash.media.Camera;
	import flash.media.Microphone;
	
	import flash.text.TextFormat;
	import flash.events.NetStatusEvent;

	import com.milgra.unit.CustomEvent;	
	import com.milgra.unit.RTMPConnection;
	import com.milgra.unit.ui.Textbox;
	import com.milgra.unit.ui.Scrollbar;
	import com.milgra.unit.skin.MixedSkin;
	
	/*
		Tasks of CSLive
			- test every possible live stream related functions
	
	*/
	
	public class CSLive
	{
		
		// nc - connection
		// rtmp - connection handler
		// counter - init helper
		
		public var nc : NetConnection;
		public var rtmp : RTMPConnection;
		public var counter : int = -1;
		
		public var stream : NetStream;
		public var streams : Array;
		
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
		 * CSLive constructor
		 * @param skinX skin
		 * @param hostX host server url
		 * @param helpX helper server url
		 **/
		
		public function CSLive( skinX : MixedSkin ,
								hostX : String , 
								helpX : String )
		{
			
			trace( "CSLive.construct " + skinX + " " + hostX + " " + helpX );
			
			// create

			rtmp = new RTMPConnection( );
			
			streams = new Array( );
			fields = new Array( );
			videos = new Array( );
			format = new TextFormat( "Verdana" , 10 , 0 );
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
			
			//camera.setQuality( 80000 , 0 );
			//microphone.rate = 44;
			
			output.setScrollbar( scroll );
			
			// event

			rtmp.addEventListener( CustomEvent.ACTIVATE , onActivate );
			rtmp.addEventListener( CustomEvent.DEACTIVATE , onDeactivate );
			
			// start			
			
			output.clear( );
			output.add( "This module tests live streaming mechaninsms of milenia grafter" );
			output.add( "- stream related invokes\n- stream control messages\n- stream packet delivery\n" );
						
			nc.connect( "rtmp://" + hostX + "/milgraunit" , "CSLive" );
			
		}
		
		/**
		 * Closes module
		 **/
		
		public function close ( ) : void
		{
			
			trace( "CSLive.close" );
			nc.close( );
			
		}
		
		/**
		 * Connection established
		 * @param eventX event
		 **/
		
		public function onActivate ( eventX : CustomEvent ) : void
		{
			
			trace( "CSLive.onActivate " + eventX );
			normalPlay( );
			
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
	
		/**
		 * Playing a non-existing stream from the server without restrictions
		 **/
		
		public function normalPlay ( ) : void
		{
			
			output.add( "Playing the stream from the server" );
			
			stream = new NetStream( nc );
			stream.addEventListener( NetStatusEvent.NET_STATUS , onNormalPlayStatus );
			stream.play( "normal" );
			
			streams.push( stream );
			
			fields[ 0 ].text = "Normal play";
			videos[ 0 ].attachNetStream( stream );
			
		}
		
		/**
		 * Normal play status
		 * @param eventX event
		 **/
		
		public function onNormalPlayStatus ( eventX : NetStatusEvent ) : void
		{
			
			stream.removeEventListener( NetStatusEvent.NET_STATUS , onNormalPlayStatus );
			
			trace( eventX.info.code );
			output.add( "Status: " + eventX.info.code );
			
			if ( eventX.info.code == "NetStream.Play.Reset" ) output.add( "SUCCESS" );
			else output.add( "FAILURE" );
			
			normalPublish( );
			
		}

		/**
		 * Publishing a non-existing stream to the server without restrictions
		 **/		
		
		public function normalPublish ( ) : void
		{
			
			output.add( "Publishing a stream to server live" );
			
			stream = new NetStream( nc );
			stream.attachAudio( microphone );
			stream.attachCamera( camera );
			stream.addEventListener( NetStatusEvent.NET_STATUS , onNormalPublishStatus );
			
			streams.push( stream );
			
			stream.publish( "normal" , "live" );
			
		}
		
		/**
		 * Normal publish tatus
		 * @param eventX event
		 **/
		
		public function onNormalPublishStatus ( eventX : NetStatusEvent ) : void
		{
			
			stream.removeEventListener( NetStatusEvent.NET_STATUS , onNormalPublishStatus );
			
			trace( eventX.info.code );
			output.add( "Status: " + eventX.info.code );
			
			if ( eventX.info.code == "NetStream.Publish.Start" ) output.add( "SUCCESS" );
			else output.add( "FAILURE" );
			
			disabledStreamPublish( );
			
		}
		
		/**
		 * Publishing a stream what is disabled
		 **/
	
		public function disabledStreamPublish ( ) : void 
		{
			
			// adding stream listeners on server
			
			nc.call( "addStreamListeners" , null );
		
			output.add( "Publishing a disabled stream to server" );
					
			stream = new NetStream( nc );
			stream.attachAudio( microphone );
			stream.attachCamera( camera );
			stream.addEventListener( NetStatusEvent.NET_STATUS , onDisabledStreamPublish );

			stream.publish( "disabled" , "live" );
			streams.push( stream );
			
		}
		
		/**
		 * Disabled publish status
		 * @param eventX event
		 **/
		
		public function onDisabledStreamPublish ( eventX : NetStatusEvent ) : void
		{
			
			stream.removeEventListener( NetStatusEvent.NET_STATUS , onDisabledStreamPublish );
			
			trace( eventX.info.code );
			output.add( "Status: " + eventX.info.code );
					
			if ( eventX.info.code == "NetStream.Record.NoAccess" ) output.add( "SUCCESS" );
			else output.add( "FAILURE" );

			disabledStreamPlay( );
	
		}
		
		/**
		 * Playing a disabled stream
		 **/
		
		public function disabledStreamPlay ( ) : void 
		{ 
		
			output.add( "Playing a disabled stream from server" );
					
			stream = new NetStream( nc );
			stream.addEventListener( NetStatusEvent.NET_STATUS , onDisabledStreamPlay );
			
			stream.play( "disabled" );
			streams.push( stream );

			fields[ 1 ].text = "Disabled play";
			videos[ 1 ].attachNetStream( stream );			
			
		}
		
		/**
		 * Disabled play status
		 * @param eventX event
		 **/
		
		public function onDisabledStreamPlay ( eventX : NetStatusEvent ) : void
		{
			
			stream.removeEventListener( NetStatusEvent.NET_STATUS , onDisabledStreamPlay );
			
			trace( eventX.info.code );
			output.add( "onDisabledStreamPlay Status: " + eventX.info.code );
			
			if ( eventX.info.code == "NetStream.Play.Failed" ) output.add( "SUCCESS" );
			else output.add( "FAILURE" );
			
			enabledPublish( );
			
		}
		
		/**
		 * Publishing an enabled stream in record mode
		 **/
		
		public function enabledPublish ( ) : void 
		{
		
			output.add( "Publishing an enabled recorded stream to server" );	
			
			stream = new NetStream( nc );
			stream.attachAudio( microphone );
			stream.attachCamera( camera );
			stream.addEventListener( NetStatusEvent.NET_STATUS , onEnabledPublish );
			
			stream.publish( "enabled" , "record" );
			streams.push( stream );

		}
		
		/**
		 * Enabled publish event
		 * @param eventX event
		 **/
		
		public function onEnabledPublish ( eventX : NetStatusEvent ) : void
		{
			
			stream.removeEventListener( NetStatusEvent.NET_STATUS , onEnabledPublish );
			
			trace( eventX.info.code );
			output.add( "Status: " + eventX.info.code );
			
			if( eventX.info.code == "NetStream.Publish.Start" ) output.add( "SUCCESS" );
			else output.add( "FAILURE" );
			
			enabledPlay( );
			
		}
		
		/**
		 * Enabled stream play
		 **/
		
		public function enabledPlay ( ) : void 
		{
			
			output.add( "Playing live stream from server" );
					
			stream = new NetStream( nc );
			stream.addEventListener( NetStatusEvent.NET_STATUS , onEnabledPlay );
			stream.play( "enabled" );
			
			streams.push( stream );
			
			fields[ 2 ].text = "Enabled play";
			videos[ 2 ].attachNetStream( stream );
			
		}
		
		/**
		 * Enabled play status
		 * @param eventX event
		 **/
		
		public function onEnabledPlay ( eventX : NetStatusEvent ) : void
		{
			
			trace( eventX.info.code );
			output.add( "Status: " + eventX.info.code );

			stream.removeEventListener( NetStatusEvent.NET_STATUS , onEnabledPlay );
			
			if ( eventX.info.code == "NetStream.Play.Reset" ) output.add( "SUCCESS" );
			else output.add( "FAILURE" );
			
			onCloseStream( );
			
		}
		
		public function onCloseStream ( ) : void
		{
			
			output.add( "onCloseStream." );
			output.add( "Finished testing." );
			
		}

	}
	
}