package
{
	
	import flash.net.NetStream;
	import flash.net.NetConnection;
	import flash.net.ObjectEncoding;
	
	import flash.media.Camera;
	import flash.media.Microphone;
	import flash.events.NetStatusEvent;
	
	import flash.display.Sprite;
	import flash.display.StageScaleMode;
	
	
	public class Publisher extends Sprite
	{
		
		public var camera : Camera;
		public var stream : NetStream;
		public var connection : NetConnection;
		
		public function Publisher ( )
		{
			
			connection = new NetConnection( );
			
			stage.scaleMode = StageScaleMode.NO_SCALE;			
			connection.objectEncoding = ObjectEncoding.AMF0;
			connection.addEventListener( NetStatusEvent.NET_STATUS , onStatus );
			connection.connect( "rtmp://localhost/milgraunit" , "control" );
			//connection.connect( "rtmp://78.47.238.19/maketv" );
		}
		
		public function onStatus ( eventX : NetStatusEvent ) : void
		{
			
			camera = Camera.getCamera( );
			camera.setMode( 320 , 240 , 20 );
			camera.setQuality( 20000 , 0 );
			
			stream = new NetStream( connection );
			stream.attachAudio( Microphone.getMicrophone( ) );
			stream.attachCamera( Camera.getCamera( ) );
			stream.publish( "test" , "live" );
			
		}

	}
	
}