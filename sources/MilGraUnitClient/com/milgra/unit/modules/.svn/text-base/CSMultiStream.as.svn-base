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
	
	
	public class CSMultiStream
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
		
		public function CSMultiStream( skinX : MixedSkin ,
									   hostX : String , 
									   helpX : String )
		{
			
			trace( "CSMultiStream.construct " + skinX + " " + hostX + " " + helpX );
			
			// create

			rtmp = new RTMPConnection( );
			
			fields = new Array( );
			videos = new Array( );

			streams = new Array( );
			
			format = new TextFormat( "Verdana" , 10 , 0 );
			scroll = new Scrollbar( skinX.scrollbarSkin0 ); 	
			output = new Textbox( skinX.field0 , format );
			
			while ( ++ counter < 7 ) 
			{
				videos[ counter ] = skinX[ "video" + counter ];
				fields[ counter ] = skinX[ "field" + ( counter + 1 ) ];
			}
			
			// set

			nc = rtmp.connection;
			nc.client = this;
			
			camera = Camera.getCamera( );
			microphone = Microphone.getMicrophone( );
			
			fields[ 0 ].text = "camera";
			videos[ 0 ].attachCamera( camera );
			output.setScrollbar( scroll );
			
			// event

			rtmp.addEventListener( CustomEvent.ACTIVATE , onActivate );
			rtmp.addEventListener( CustomEvent.DEACTIVATE , onDeactivate );
			
			// start			
			
			output.clear( );
			output.add( "This module tests multiple stream pushing to a client" );
			output.add( "- rtmp header page flipping" );
						
			nc.connect( "rtmp://" + hostX + "/milgraunit" , "CSData" );
			
		}
		
		/**
		 * Connection established
		 * @param eventX event
		 **/
		
		public function onActivate ( eventX : CustomEvent ) : void
		{
			
			trace( "CSMultiStream.onActivate " + eventX );
			
			output.add( "Connected" );
			
			var stream : NetStream;
			
			stream = new NetStream( nc );
			stream.attachAudio( microphone );
			stream.attachCamera( camera );
			
			stream.publish( "multi" , "live" );
			streams.push( stream );
			
			for ( var index : int = 0 ; index < 26 ; index++ )
			{
				
				output.add( "opening stream: " + index );
				
				stream = new NetStream( nc );
				stream.play( "multi" );
				streams.push( stream );
				
				if ( index < 6 )
				{
				
					fields[ index + 1 ].text = " stream " + index; 
					videos[ index + 1 ].attachNetStream( stream );
				
				}				
				
			}
			
		}
		
		/**
		 * Connection failed
		 * @param eventX event
		 **/
		
		public function onDeactivate ( eventX : CustomEvent ) : void
		{
		
			trace( "CSMultiStream.onDeactivate " + eventX );	
			output.add( "Connection failed." );
			
		}
	
	}
	
}