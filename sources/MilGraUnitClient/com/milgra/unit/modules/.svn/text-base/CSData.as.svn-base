package com.milgra.unit.modules
{
	
	import com.milgra.unit.CustomEvent;
	import com.milgra.unit.RTMPConnection;
	import com.milgra.unit.ui.Scrollbar;
	import com.milgra.unit.ui.Textbox;
	import com.milgra.unit.skin.TextSkin;

	import flash.net.Responder;
	import flash.net.NetConnection;
	import flash.text.TextFormat;
	
	/*
		Tasks of CSData
			- teste every possible data exchange situations between client and server
	
	*/	
	
	public class CSData
	{
		
		// nc - connection
		// rtmp - connection handler
		
		public var nc : NetConnection;
		public var rtmp : RTMPConnection;
		
		public var testNumber : Number;
		public var testObject : Object;
		public var testString : String;
		public var testArray : Array;
		
		// output - text output box
		// scroll - scrollbar for textbox
		// format - textformat
		
		public var output : Textbox;
		public var scroll : Scrollbar;
		public var format : TextFormat;
		
		/**
		 * CSData constructor
		 * @param skinX skin
		 * @param hostX host server url
		 * @param helpX helper server url
		 **/
		
		public function CSData ( skinX : TextSkin ,
								 hostX : String , 
								 helpX : String )
		{
			
			trace( "CSData.construct " + skinX + " " + hostX + " " + helpX );
			
			// create

			rtmp = new RTMPConnection( );
			
			format = new TextFormat( "Verdana" , 10 , 0 );
			scroll = new Scrollbar( skinX.scrollbarSkin0 ); 	
			output = new Textbox( skinX.field0 , format );
			
			// set

			nc = rtmp.connection;
			nc.client = this;
			
			// event

			rtmp.addEventListener( CustomEvent.ACTIVATE , onActivate );
			rtmp.addEventListener( CustomEvent.DEACTIVATE , onDeactivate );
			
			// start			

			output.clear( );
			output.add( "This module tests all possible AMF0 related messaging mechanism." );
			output.add( "- amf deserializer\n" );
			
			output.add( "Connecting..." );
			nc.connect( "rtmp://" + hostX + "/milgraunit" , "CSData" );
			
		}
		
		/**
		 * Closes module
		 **/
		
		public function close ( ) : void
		{
			
			trace( "CSData.close" );
			nc.close( );
			
		}
		
		/**
		 * Connection established
		 * @param eventX event
		 **/
		
		public function onActivate ( eventX : CustomEvent ) : void
		{
			
			trace( "CSData.onActivate " + eventX );
			emptyCall( );
			
		}
		
		/**
		 * Connection failed
		 * @param eventX event
		 **/
		
		public function onDeactivate ( eventX : CustomEvent ) : void
		{
		
			trace( "CSData.onDeactivate " + eventX );	
			output.add( "Connection failed." );
			
		}
	
		/**
		 * Empty call on server
		 **/
		
		public function emptyCall ( ) : void
		{
			
			output.add( "Empty on server" );
			nc.call( "emptyCall" , null );
			
		}
		
		/**
		 * Empty call on client
		 **/
		
		public function onEmptyCall ( ) : void
		{
		
			output.add( "Empty on client" );
			output.add( "SUCCESS" );
			nullCall( );			
			
		}
				
		/**
		 * Calling null on server
		 **/
		
		public function nullCall ( ) : void
		{
			
			output.add( "Null call on server " + null );
			nc.call( "nullCall" , null , null );
			
		}
		
		/**
		 * Calling null on client
		 * @param resultX result
		 **/
		
		public function onNullCall ( resultX : Object ) : void
		{
		
			output.add( "Null call on client " + resultX );
			
			if ( resultX == null ) output.add( "SUCCESS" );
			else output.add( "FAILURE" );
			
			numberCall( );
						
		}
		
		/**
		 * Calling number on server
		 **/
		
		public function numberCall ( ) : void 
		{
						
			testNumber = Math.random( );
			output.add( "Number call on server " + testNumber );
			nc.call( "numberCall" , null , testNumber );
			
		}
		
		/**
		 * Calling number on client
		 * @param resultX 
		 **/
		
		public function onNumberCall ( resultX : Number ) : void
		{
		
			output.add( "Number call on client " + resultX );

			if ( resultX == testNumber ) output.add( "SUCCESS" );
			else output.add( "FAILURE" );
			
			booleanCall( );
			
		}
		
		/**
		 * Calling a boolean on server
		 **/
		
		public function booleanCall ( ) : void 
		{
			
			output.add( "Boolean call on server " + true );
			
			nc.call( "booleanCall" , null , true );
			
		}
		
		/**
		 * Calling boolean on client
		 * @param resultX result
		 **/
		
		public function onBooleanCall ( resultX : Boolean ) : void
		{
		
			output.add( "Boolean call on client " + resultX );
			if ( resultX == true ) output.add( "SUCCESS" );
			else output.add( "FAILURE" );
			
			stringCall( );
			
		}
		
		/**
		 * Calling a string on server
		 **/
		
		public function stringCall ( ) : void 
		{
						
			testString = "azipafaipapipipapapifapipa";
			output.add( "String call on server " + testString );
			nc.call( "stringCall" , null , testString );
			
		}
		
		/**
		 * Calling a string on client
		 * @param resultX result
		 **/
		
		public function onStringCall ( resultX : String ) : void
		{
		
			output.add( "String call on client " + resultX );
			
			if ( resultX == testString ) output.add( "SUCCESS" );
			else output.add( "FAILURE" );
			
			arrayCall( );
			
		}
	
		/**
		 * Calling an array on server
		 **/
		
		public function arrayCall ( ) : void 
		{
			
			testArray = [ 23 , 45 , 77.6 , 45.3 ];
			output.add( "Array call on server " + testArray );
			
			nc.call( "arrayCall" , null , testArray );
			
		}
		
		/**
		 * Calling an array on client
		 * @param resultX result
		 **/
		
		public function onArrayCall ( resultX : Array ) : void
		{
		
			output.add( "Array call on client : " + resultX );
			
			var failed : Boolean = false;
			for ( var a : int = 0 ; a < testArray.length ; ++a ) if ( resultX[a] != testArray[a] ) failed = true; 
				
			if ( !failed ) output.add( "SUCCESS" );
			else output.add( "FAILURE" );
			
			objectCall( );
			
		}
		  
		/**
		 * Calling object on server
		 **/
		
		public function objectCall ( ) : void 
		{
						
			testObject = { id : 234 , name : "object" , valid : true };
			output.add( "Object call on server " + testObject );

			nc.call( "objectCall" , null , testObject );
			
		}
		
		/**
		 * Calling object on client
		 * @param resultX result
		 **/
		
		public function onObjectCall ( resultX : Object ) : void
		{
		
			output.add( "Object call on client " + resultX );

			var failed : Boolean = false;
			for ( var a : String in testObject ) if ( resultX[a] != testObject[a] ) failed = true; 
				
			if ( !failed ) output.add( "SUCCESS" );
			else output.add( "FAILURE" );
			
			listenedCall( );
			
		}

		/**
		 * Calling function on server with result listening
		 **/
		
		public function listenedCall ( ) : void 
		{
			
			output.add( "Listened call on server " );
			
			var listener : Responder = new Responder( onListenedCallResult , onListenedCallResult );
			nc.call( "listenedCall" , listener , testString );
			
		}
		
		/**
		 * Function result
		 * @param resultX result 
		 **/
		
		public function onListenedCallResult ( resultX : Object ) : void
		{
			
			output.add( "Result: " + resultX );
			
			if ( resultX == testString ) output.add( "SUCCESS" );
			else output.add( "FAILURE" );
			
			nc.call( "callListened" , null );
			
			output.add( "Tests finished." );
			
		}
		 
	}
	
}