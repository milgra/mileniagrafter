package com.milgra.unit.modules
{

	import com.milgra.unit.CustomEvent;
	import com.milgra.unit.RTMPConnection;
	import com.milgra.unit.skin.TextSkin;
	import com.milgra.unit.skin.StressSkin;

	import com.milgra.unit.ui.Seekbar;
	import com.milgra.unit.ui.Textbox;
	import com.milgra.unit.ui.Scrollbar;
	
	import flash.net.NetStream;
	import flash.net.NetConnection;
	import flash.text.TextFormat;
	import flash.media.Video;
	import flash.media.Camera;
	import flash.media.Microphone;
	

	public class Stress
	{
		
		public var nc : NetConnection;
		public var max : Number = 5000;
		public var rtmp : RTMPConnection;
		
		public var cam : Camera;
		public var mic : Microphone;
		
		public var fields : Array;
		public var videos : Array;
		public var streams : Array;
		
		public var format : TextFormat;
		public var scroll : Scrollbar;
		public var output : Textbox;
		public var seekbar : Seekbar;
		

		public function Stress( skinX : StressSkin ,
								hostX : String , 
								helpX : String )
		{
			
			trace( "CSLive.construct " + skinX + " " + hostX + " " + helpX );
			
			// create

			rtmp = new RTMPConnection( );
			videos = new Array( );
			fields = new Array( );
			streams = new Array( );
			seekbar = new Seekbar( skinX.seekbarSkin0 );

			format = new TextFormat( "Verdana" , 12 , 0 );
			scroll = new Scrollbar( skinX.scrollbarSkin0 ); 	
			output = new Textbox( skinX.field0 , format );
			
			// set

			nc = rtmp.connection;
			nc.client = this;
			
			videos = [ skinX.video0 , skinX.video1 , skinX.video2 , skinX.video3 ];
			fields = [ skinX.field1 , skinX.field2 , skinX.field3 , skinX.field4 ];
			
			// event

			rtmp.addEventListener( CustomEvent.ACTIVATE , onActivate );
			rtmp.addEventListener( CustomEvent.DEACTIVATE , onDeactivate );
			
			seekbar.addEventListener( CustomEvent.COMPLETE , onRatio );
			
			// start			
			
			seekbar.setRatio( .1 );
			output.clear( );
			output.add( "Stress Test tests loads the tested server with a lot of connections which are subscribing" );
			output.add( "onto the live stream provided by the client. You can test how much load Milenia can handle on" );
			output.add( "your machine" );
			output.add( "Simply slide to the wanted position. The whole slider means 5000 connections." );
			output.add( "Connecting..." );
			nc.connect( "rtmp://" + helpX + "/milgraunit" , "Stress" , hostX );
			
		}
		
		public function onActivate ( eventX : CustomEvent ) : void
		{
			
			output.add( "Connected" );
			
			cam = Camera.getCamera( );
			mic = Microphone.getMicrophone( );
			
			var stream : NetStream = new NetStream( nc );
			stream.attachAudio( mic );
			stream.attachCamera( cam );
			stream.publish( "livestream" );
			
			streams.push( stream );

			trace( "Stress.onActivate" + eventX );			
			
		}
		
		public function onDeactivate ( eventX : CustomEvent ) : void
		{
		
			trace( "Stress.onDeactivate " + eventX );	
			
		}
		
		public function onRatio ( eventX : CustomEvent ) : void
		{
			
			trace( "Stress.onRatio " + max * eventX.ratio );
			
			nc.call( "setSubscribers" , null , max * eventX.ratio );

			for ( var index : int = 0 ; index < 4 ; index++ )
			{
				
				var name : String = "stream" + Math.round( Math.random( ) * max * eventX.ratio );
				var stream : NetStream = new NetStream( nc );
				stream.play( name );
				streams.push( stream );
				fields[ index ].text = name;
				videos[ index ].attachNetStream( stream );
				
			}
			
		}
		
		public function log ( logX : String ) : void
		{
			
			output.add( logX );
			
		}

	}
	
}