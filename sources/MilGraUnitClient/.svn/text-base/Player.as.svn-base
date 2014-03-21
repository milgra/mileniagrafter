package
{
	
	import flash.net.NetStream;
	import flash.net.NetConnection;
	import flash.net.ObjectEncoding;

	import flash.media.Video;
	import flash.events.NetStatusEvent;
	import flash.display.Sprite;
	import flash.display.StageScaleMode;
	import flash.display.StageAlign;
	import flash.media.Camera;
	import flash.media.Microphone;
	import flash.events.MouseEvent;
	import flash.net.Responder;
	import flash.utils.Timer;
	import flash.events.TimerEvent;
	import flash.events.Event;
	
	
	public class Player extends Sprite
	{
		
		public var video : Video;
		public var stream : NetStream;
		public var video1 : Video;
		public var stream1 : NetStream;
		public var video2 : Video;
		public var stream2 : NetStream;
		public var connection : NetConnection;
		public var check : Boolean;
		
		public function Player( )
		{
			
			video = new Video( );
			video1 = new Video( );
			video2 = new Video( );
			connection = new NetConnection( );

			connection.objectEncoding = ObjectEncoding.AMF0;
			connection.addEventListener( NetStatusEvent.NET_STATUS , onStatus );
			connection.client = this;
			//connection.connect( "rtmp://mdn511-v2.interlake.net/sdnl_maketv01" );
			connection.connect( "rtmp://78.47.238.19/test" );
			//connection.connect( "rtmp://192.168.1.101/test" );
			//connection.connect( "rtmp://localhost/fmstest" );

			addChild( video );
			addChild( video1 );
			addChild( video2 );
			
			video1.x = 320;
			video2.x = 640;
			
			stage.scaleMode = StageScaleMode.NO_SCALE;
			stage.align = StageAlign.TOP_LEFT;
			
			stage.addEventListener( MouseEvent.CLICK , onClick );
			stage.addEventListener( Event.ENTER_FRAME , onFrame );
			
		}
		
		public function onBWDone ( )
		{
			
			return 0;
			
		}
		
		public function onStatus ( eventX : NetStatusEvent ) : void
		{
			
			trace( "***" + eventX.info.code );
			
			if ( eventX.info.code != "NetConnection.Connect.Success" ) return;
			
			var responder : Responder = new Responder( new Function( ) , new Function( ) );
			
			var mixedArrayValue:Array = [];
			var arrayValue:Array = [];
			var objectValue:Object = {};

			objectValue["key"] = "milgra" ;
			objectValue["num"] = 234235;
			//objectValue["array"] = arrayValue;
			
			mixedArrayValue[0] = "dasfdasg";
			mixedArrayValue[1] = 234235;
			mixedArrayValue[2] = 223235.2351235;
			mixedArrayValue[3] = true;
			mixedArrayValue[4] = objectValue;
			mixedArrayValue[7] = false;
			mixedArrayValue[44] = objectValue;
			
			for(var o:* in mixedArrayValue) {
				arrayValue = arrayValue.concat(o);
			}
			
			//arrayValue = arrayValue.concat("Test");

		  // connection.call( "receiveArray" , null , arrayValue );
		  //connection.call( "receiveMixedArray" , null , mixedArrayValue );
		  // connection.call( "receiveObject" , null , objectValue );
		   	//for ( var i : int = 0 ; i < 20 ; i++ ) { var stream = new NetStream( connection ); stream.play( "phone" ); }
			
			stream = new NetStream( connection );
			stream.bufferTime = 0;
			//stream.play( "stream" );
			
			//stream.close( );
			//stream.attachCamera( Camera.getCamera( ) );
			//stream.attachAudio( Microphone.getMicrophone( ) );
			stream.client = this;
			stream.addEventListener( NetStatusEvent.NET_STATUS , onStreamStatus );			
			//stream.play( "5634_5634_2_0" );
			stream.play( "mp4:mpegtest" );
			//stream.pause();
			//stream.resume();
			//stream.publish( "test" , "live" );
			video.attachNetStream( stream );
			//video.attachCamera( Camera.getCamera( ) );
			
/*
			stream1 = new NetStream( connection );
			stream1.addEventListener( NetStatusEvent.NET_STATUS , onStreamStatus );			
			stream1.play( "test" );
			video1.attachNetStream( stream1 );
/*
			stream2 = new NetStream( connection );
			stream2.play( "test" );
			video2.attachNetStream( stream2 );*/
		
		}
		
		public function onPlayStatus ( objectX : Object ) : void
		{
			
			trace( "onPlayStatus" );

			for ( var a : String in objectX.info ) trace( a + " " + objectX.info[a] );
			
		}
		
		public function onStreamStatus ( objectX : Object ) : void
		{
			
			trace( "onStatus" );
				
			for ( var a : String in objectX.info ) trace( a + " " + objectX.info[a] );
				
		}
		
		public function onMetaData ( objectX : Object ) : void
		{
			
			trace( "onMetaData" );
				
			for ( var a : String in objectX ) trace( a + " " + objectX[a] );
				
		}
		
		public function onClick ( event : MouseEvent ) : void
		{
			
			trace( "click" );
			
			//stream.publish( "false" );
			stream.seek( 1 );
			//stream.play( "phone.flv" );
			
			//stream.togglePause( );
			
			//connection.close( );
			
		}
		
		public function onFrame ( eventX : Event ) : void
		{
			
			if ( stream1 != null ) trace( stream1.time );
			
		}
			
	}
	
}