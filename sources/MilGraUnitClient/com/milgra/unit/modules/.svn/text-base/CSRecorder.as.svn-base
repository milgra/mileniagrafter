package com.milgra.unit.modules
{
	
	import com.milgra.unit.ui.Textbox;
	import com.milgra.unit.ui.Scrollbar;
	import com.milgra.unit.ui.SimpleButton;
	
	import com.milgra.unit.CustomEvent;
	import com.milgra.unit.RTMPConnection;
	import com.milgra.unit.skin.MixedSkin;
	import com.milgra.unit.skin.RecorderSkin;
	
	import flash.net.NetStream;
	import flash.net.NetConnection;

	import flash.text.TextFormat;
	import flash.events.NetStatusEvent;
	import flash.text.TextField;
	import flash.media.Camera;
	import flash.media.Microphone;
	import flash.media.Video;
	
	/*
		Tasks of CSRecorded
			- test every possible recorded stream play related functions
	
	*/
	
	public class CSRecorder
	{
		
		public var nc : NetConnection;
		public var rtmp : RTMPConnection;
		
		public var cam : Camera;
		public var mic : Microphone;
		
		public var videoIn : Video;
		public var videoOut : Video;
		
		public var streamIn : NetStream;
		public var streamOut : NetStream;
		
		public var label : TextField;
		public var output : Textbox;
		public var scroll : Scrollbar;
		public var format : TextFormat;
		public var button : SimpleButton;
		
		/**
		 * CSRecorded constructor
		 * @param skinX skin
		 * @param hostX host server url
		 * @param helpX helper server url
		 **/
		
		public function CSRecorder( skinX : RecorderSkin ,
									hostX : String , 
									helpX : String )
		{
			
			trace( "CSLive.construct " + skinX + " " + hostX + " " + helpX );
			
			// create
			
			cam = Camera.getCamera( );
			mic = Microphone.getMicrophone( );

			rtmp = new RTMPConnection( );
			
			format = new TextFormat( "Verdana" , 12 , 0 );
			scroll = new Scrollbar( skinX.scrollbarSkin0 ); 	
			output = new Textbox( skinX.field0 , format );
			button = new SimpleButton( skinX.buttonSkin0 );
			
			// set

			nc = rtmp.connection;
			nc.client = this;
			
			label = skinX.field1;
			label.text = "Record";
			label.mouseEnabled = false;
			
			videoIn = skinX.video1;
			videoOut = skinX.video0;
			
			// event

			rtmp.addEventListener( CustomEvent.ACTIVATE , onActivate );
			rtmp.addEventListener( CustomEvent.DEACTIVATE , onDeactivate );
			
			button.addEventListener( CustomEvent.ACTIVATE , onButton );
			
			// start			
			
			videoOut.attachCamera( cam );
			output.clear( );
			output.add( "Connecting..." );
			nc.connect( "rtmp://" + hostX + "/milgraunit" , "StreamControl" );
			
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
			output.add( "Connected" );
			
			streamIn = new NetStream( nc );
			streamOut = new NetStream( nc );
			
			streamIn.client = this;
			streamOut.client = this;
			
			streamIn.addEventListener( NetStatusEvent.NET_STATUS , onStreamStatus );
			streamOut.addEventListener( NetStatusEvent.NET_STATUS , onStreamStatus );
		
			videoIn.attachNetStream( streamIn );
			
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
		
		public function onButton ( eventX : CustomEvent ) : void
		{
			
			trace( "CSRecorder.onButton " + eventX );
			
			if ( label.text == "Record" )
			{
				
				streamIn.play( false );
				streamOut.attachAudio( mic );
				streamOut.attachCamera( cam );
				streamOut.publish( "recorded" , "record" );
				
				label.text = "Play";
				
			}
			else
			if ( label.text == "Play" )
			{
				
				streamOut.publish( null );
				streamIn.play( "recorded.flv" );
				
				label.text = "Record";
				
			}
			
		}
		
		public function onMetaData ( infoX : Object ) : void
		{
			
			trace( "CSRecorder.onMetaData " + infoX.duration );
			output.add( "MetaData arrived: " + infoX.duration );
			
		}
		
		public function onPlayStatus ( infoX : Object ) : void
		{
			
			trace( "CSRecorder.onPlayStatus " );
			output.add( "PlayStatus: " + infoX );
			for ( var a : * in infoX ) trace( a + " : " + infoX[a] );
			
		}
		
		public function onStreamStatus ( eventX : NetStatusEvent ) : void
		{
			
			trace( "CSRecorder.onStreamStatus " + eventX );
			output.add( "Stream NetStatus " + eventX.info.code );
			for ( var a : * in eventX.info ) trace( a + " : " + eventX.info[a] );
			
		}
		
	
	}
	
}