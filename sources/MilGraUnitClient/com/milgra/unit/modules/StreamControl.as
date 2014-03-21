package com.milgra.unit.modules
{

	import com.milgra.unit.CustomEvent;
	import com.milgra.unit.RTMPConnection;
	
	import com.milgra.unit.ui.Seekbar;
	import com.milgra.unit.ui.Textbox;
	import com.milgra.unit.ui.Scrollbar;
	import com.milgra.unit.ui.SimpleButton;
	import com.milgra.unit.ui.ToggleButton;
	import com.milgra.unit.skin.PlayerSkin;
	
	import flash.text.TextFormat;
	import flash.media.Video;
	import flash.display.MovieClip;
	
	import flash.net.NetStream;
	import flash.net.NetConnection;	
	
	import flash.display.Bitmap;
	import flash.display.BitmapData;
	
	import flash.events.Event;
	import flash.events.TimerEvent;
	import flash.events.NetStatusEvent;
	
	/*
		Tasks of Streamcontrol
			- let the user test every possible interaction related to a playing recorded stream
	
	*/
	
	public class StreamControl
	{
		
		// nc - connection
		// rtmp - connection handler
		// counter - init helper
		
		public var nc : NetConnection;
		public var rtmp : RTMPConnection;
		public var counter : int = 0;
		public var duration : Number;
		
		public var skin : PlayerSkin; 
		public var video : Video;
		public var bitmap : Bitmap;		
		public var stream : NetStream;
		
		public var seekbar : Seekbar;
		public var speedbar : Seekbar;
		
		public var playButton : ToggleButton;
		public var pauseButton : ToggleButton;
		
		public var audioButton : SimpleButton;
		public var videoButton : SimpleButton;
		public var snapshotButton : SimpleButton;
		
		// videos - videos
		// output - text output box
		// scroll - scrollbar for textbox
		// format - textformat

		public var output : Textbox;
		public var scroll : Scrollbar;
		public var format : TextFormat;
		
		/**
		 * CSRecorded constructor
		 * @param skinX skin
		 * @param hostX host server url
		 * @param helpX helper server url
		 **/
		
		public function StreamControl( skinX : PlayerSkin ,
									   hostX : String , 
									   helpX : String )
		{
			
			trace( "StreamControl.construct " + skinX + " " + hostX + " " + helpX );
			
			// create

			rtmp = new RTMPConnection( );
			bitmap = new Bitmap( );

			seekbar = new Seekbar( skinX.seekbarSkin0 );
			speedbar = new Seekbar( skinX.seekbarSkin1 );
			
			playButton = new ToggleButton( skinX.buttonSkin0 );			
			pauseButton = new ToggleButton( skinX.buttonSkin1 );
			
			audioButton = new SimpleButton( skinX.buttonSkin3 );
			videoButton = new SimpleButton( skinX.buttonSkin4 );
			snapshotButton = new SimpleButton( skinX.buttonSkin5 );			
			
			format = new TextFormat( "Verdana" , 12 , 0 );
			scroll = new Scrollbar( skinX.scrollbarSkin0 ); 	
			output = new Textbox( skinX.field0 , format );
			
			// set

			nc = rtmp.connection;
			nc.client = this;
			
			skin = skinX;
			video = skinX.video0;
			
			// event

			rtmp.addEventListener( CustomEvent.ACTIVATE , onActivate );
			rtmp.addEventListener( CustomEvent.DEACTIVATE , onDeactivate );
			
			seekbar.addEventListener( CustomEvent.CHANGE , onSeekPosition );
			speedbar.addEventListener( CustomEvent.CHANGE , onSpeedPosition );
			seekbar.addEventListener( CustomEvent.COMPLETE , onSeek );
			speedbar.addEventListener( CustomEvent.COMPLETE, onSpeed );
			
			playButton.addEventListener( CustomEvent.ACTIVATE , onPlay );
			playButton.addEventListener( CustomEvent.DEACTIVATE , onStop );
			
			pauseButton.addEventListener( CustomEvent.ACTIVATE , onPause );
			pauseButton.addEventListener( CustomEvent.DEACTIVATE , offPause );
			
			audioButton.addEventListener( CustomEvent.ACTIVATE , onAudio );
			audioButton.addEventListener( CustomEvent.ACTIVATE , offAudio );
			videoButton.addEventListener( CustomEvent.ACTIVATE , onVideo );
			videoButton.addEventListener( CustomEvent.ACTIVATE , offVideo );
			snapshotButton.addEventListener( CustomEvent.ACTIVATE , onShot );
			
			// start
			
			skinX.addChild( bitmap );
			seekbar.setRatio( .5 );	
			
			output.clear( );
			output.add( "Connecting..." );
			//nc.connect( "rtmp://78.47.238.19/test" );
			//duration = 12;
			nc.connect( "rtmp://" + hostX + "/milgraunit" , "StreamControl" );
			
		}
		
		/**
		 * Closes module
		 **/
		 
		public function close ( ) : void
		{
			
			trace( "StreamControl.close" );
			rtmp.connection.close( );
			
		}
		
		/**
		 * Connection established
		 * @param eventX event
		 **/
		
		public function onActivate ( eventX : CustomEvent ) : void
		{
			
			trace( "StreamControl.onActivate " + eventX );
			output.add( "Connected, playing stream..." );
			
			stream = new NetStream( nc );
			stream.client = this;
			stream.addEventListener( NetStatusEvent.NET_STATUS , onStreamStatus );
			video.attachNetStream( stream );
			
		}
		
		public function onMetaData ( infoX : Object ) : void
		{
			
			trace( "StreamControl.onMetaData " + infoX.duration );
			output.add( "MetaData arrived: " + infoX.duration );
			duration = infoX.duration;
			
		}
		
		public function onPlayStatus ( infoX : Object ) : void
		{
			
			trace( "StreamControl.onPlayStatus " );
			output.add( "PlayStatus: " + infoX );
			for ( var a : * in infoX ) trace( a + " : " + infoX[a] );
			
		}
		
		public function onStreamStatus ( eventX : NetStatusEvent ) : void
		{
			
			trace( "StreamControl.onStreamStatus " + eventX );
			output.add( "Stream NetStatus " + eventX.info.code );
			for ( var a : * in eventX.info ) trace( a + " : " + eventX.info[a] );
			
		}
		
		public function onRefresh ( eventX : Event ) : void
		{
			
			trace( "StreamControl.onRefresh " + stream.time + " " + duration );
			seekbar.setRatio( stream.time / duration );
			
		}
		
		/**
		 * Connection failed
		 * @param eventX event
		 **/
		
		public function onDeactivate ( eventX : CustomEvent ) : void
		{
		
			trace( "StreamControl.onDeactivate " + eventX );	
			output.add( "Connection failed." );
			
		}
		
		/**
		 * 
		 **/
		
		public function onSeek ( eventX : CustomEvent ) : void
		{
		
			trace( "StreamControl.onSeek " + eventX.ratio * duration );
			stream.seek( eventX.ratio * duration );	
			
		}
		
		public function onSpeed ( eventX : CustomEvent ) : void
		{
			
			trace( "StreamControl.onSpeed " + eventX );
			
			var ratio : Number = Math.abs( 0.5 - eventX.ratio ) * 2 * 10;
			
			if ( eventX.ratio < .5 ) ratio *= -1;
			
			rtmp.connection.call( "speed" , null , "phone.flv" , ratio );
			
		}
		
		public function onSeekPosition ( eventX : CustomEvent ) : void
		{
			
			skin.field1.text = eventX.ratio * duration + "";
			
		}
		
		public function onSpeedPosition ( eventX : CustomEvent ) : void
		{

			var ratio : Number = Math.abs( 0.5 - eventX.ratio ) * 2 * 10;
			
			//if ( eventX.ratio < .5 ) ratio *= -1;
			
			skin.field2.text = ratio + "";
			
		}
		
		public function onPlay ( eventX : CustomEvent ) : void
		{
		
			trace( "StreamControl.onPlay " + eventX );
			stream.play( "phone.flv" );
			
			skin.addEventListener( Event.ENTER_FRAME , onRefresh );
	
		}
		
		public function onStop ( eventX : CustomEvent ) : void
		{
			
			trace( "StreamControl.onStop " + eventX );
			stream.play( false );

			skin.removeEventListener( Event.ENTER_FRAME , onRefresh );
			
		}
		
		public function onPause ( eventX : CustomEvent ) : void
		{
			
			trace( "StreamControl.onPause " + eventX );
			stream.pause( );
			
		}
		
		public function offPause ( eventX : CustomEvent ) : void
		{
			
			trace( "StreamControl.offPause " + eventX );
			stream.resume( );
			
		}
		
		public function onAudio ( eventX : CustomEvent ) : void
		{
			
			trace( "StreamControl.onAudio " + eventX );
			stream.receiveAudio( true );
			
		}
		
		public function offAudio ( eventX : CustomEvent ) : void
		{
			
			trace( "StreamControl.offAudio " + eventX );
			stream.receiveAudio( false );
			
		}

		public function onVideo ( eventX : CustomEvent ) : void
		{
			
			trace( "StreamControl.onVideo " + eventX );
			stream.receiveVideo( true );
			
		}
		
		public function offVideo ( eventX : CustomEvent ) : void
		{
			
			trace( "StreamControl.onVideo " + eventX );
			stream.receiveVideo( false );			
			
		}
		
		public function onShot ( eventX : CustomEvent ) : void
		{
		
			trace( "StreamControl.onShot" );
			bitmap.bitmapData = new BitmapData( video.width , video.height );
			bitmap.bitmapData.draw( video );	
			
		}
		
	}
	
}