rpackage com.milgra.unit.modules
{
	public class CSAdmin
	{
		public function CSAdmin()
		{
		}
			public function closeApplication ( ) : void
		{
		
			textList.addText( "Closing application: " );
			
			connections[0].call( "closeVoidApplication" , null );	
			
		}
		
		public function onCloseApplication ( resultX : Object ) : void
		{
		
			textList.addText( "Result: " + resultX );
			textList.addText( "Connecting to closed application" );
			
			connectApplication( );
			
		}
				
	
		public function reloadApplication ( ) : void
		{
			
			textList.addText( "Reloading application..." );
			
			connections[0].call( "reloadVoidApplication" , null );
			
			connectApplication( );
			
		}
		
		public function getClientInfo ( ) : void { }
		public function getStreamInfo ( ) : void { }
		public function getServerInfo ( ) : void { }
		public function fmsCompatibility ( ) : void { }
		public function bandCheckerTest ( ) : void { }
		public function streamDropTest ( ) : void { }
		public function rtmpFlowTest ( ) : void { }
		public function remoteDataTest ( ) : void { }

	}
}