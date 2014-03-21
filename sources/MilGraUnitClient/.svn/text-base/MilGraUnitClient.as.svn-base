/*

	Milenia Grafter Server
	
	Copyright (c) 2007-2008 by Milan Toth. All rights reserved.
	
	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

*/

package 
{
	
	import com.milgra.unit.modules.*;
	import com.milgra.unit.CustomEvent;
	import com.milgra.unit.RTMPConnection;
	
	import com.milgra.unit.ui.LoginWindow;
	import com.milgra.unit.ui.PopupWindow;
	import com.milgra.unit.ui.SimpleButton;
	import com.milgra.unit.skin.UnitSkin;

	import flash.net.NetConnection;
	import flash.net.ObjectEncoding;
	import flash.events.NetStatusEvent;
	
	import flash.display.Sprite;
	import flash.display.StageAlign;
	import flash.display.StageScaleMode;
		
	/*
		Tasks of Milgraunitclient
			- init unit tester client
			- check server availability
			- initialize new tests
	
	*/

	public class MilGraUnitClient extends Sprite
	{
		
		// skin - application skin
		// back - back button for tests
		// buttons - test selector buttons
		// counter - loop counter
		
		public var skin : UnitSkin;
		public var back : SimpleButton;
		public var buttons : Array;
		public var counter : int = -1;
		
		// server urls
		
		public var testedUrl : String;
		public var testerUrl : String;
		
		// popups
	
		public var loginWindow : LoginWindow;
		public var popupWindow : PopupWindow;
		
		// tester - actual test
		// states - avaliable test states
		
		public var tester : *;
		public var states : Array = [ "CSConnection" , "CSData" , "CSLive" , "CSRecorded" , "CSRecorder" ,
					  				  "SSConnection" , "SSData" , "SSLive" , "SSRecorded" ,
					  				  "Multistream" , "FMSCompatibility" ,
					   				  "BandwidthCheck" , "StreamControl" , "Stress" ];
		
		/**
		 * Constructor
		 **/
		
		public function MilGraUnitClient ( )
		{
			
			trace( "construct" );	
			
			// create
			
			skin = new UnitSkin( );
			back = new SimpleButton( skin.buttonSkin0 );
			buttons = new Array( );
			
			loginWindow = new LoginWindow( skin.loginSkin );
			popupWindow = new PopupWindow( skin.popupSkin );
			
			while ( ++counter < 14 ) buttons[ counter ] = new SimpleButton( skin.testsSkin.selectorSkin[ "buttonSkin" + counter ] );
			
			// set

			skin.buttonSkin0.visible = false;
			skin.testsSkin.visible = false;

			stage.align = StageAlign.TOP_LEFT;
			stage.frameRate = 25;
			stage.scaleMode = StageScaleMode.NO_SCALE;
									
			// event
			
			back.addEventListener( CustomEvent.ACTIVATE , onState );
			loginWindow.addEventListener( CustomEvent.ACTIVATE , onLogin );
			popupWindow.addEventListener( CustomEvent.ACTIVATE , clearStripes );
			
			counter = -1;
			while ( ++counter < 14 ) buttons[ counter ].addEventListener( CustomEvent.ACTIVATE , onState );
			
			// start
			
			addChild( skin );
			loginWindow.show( );
			skin.stripesSkin.visible = true;
						
		}
		
		/**
		 * Shows a popup
		 * @param messageX message
		 **/
		
		public function popup ( messageX : String ) : void
		{
			
			popupWindow.show( messageX );
			skin.stripesSkin.visible = true;
			
		}
		
		/**
		 * Clears stripes
		 * @param eventX event
		 **/
		
		public function clearStripes ( eventX : CustomEvent ) : void
		{
			
			skin.stripesSkin.visible = false;
			
		}
				
		/**
		 * Tests servers
		 * @param eventXX eventX
		 **/
		 
		public function onLogin ( eventX : CustomEvent ):void
		{
			
			loginWindow.removeEventListener( CustomEvent.ACTIVATE , onLogin );
			
			testedUrl = eventX.testedUrl;
			testerUrl = eventX.testerUrl;
			
			skin.testsSkin.visible = true;
			skin.loginSkin.visible = false;
			skin.stripesSkin.visible = false;

			skin.testsSkin.textSkin.visible = false;
			skin.testsSkin.mixedSkin.visible = false;
			skin.testsSkin.stressSkin.visible = false;
			skin.testsSkin.playerSkin.visible = false;
			skin.testsSkin.recorderSkin.visible = false;
			skin.testsSkin.selectorSkin.visible = true;
			
		}
				
		/**
		 * Switches view
		 * @param stateX view stae
		 **/
		
		public function onState ( eventX : CustomEvent = null ) : void
		{
									
			var index : int = buttons.indexOf( eventX.root );
			if ( tester != null ) tester.close( );

			skin.buttonSkin0.visible = true;
			skin.testsSkin.textSkin.visible = false;
			skin.testsSkin.mixedSkin.visible = false;
			skin.testsSkin.stressSkin.visible = false;
			skin.testsSkin.playerSkin.visible = false;
			skin.testsSkin.recorderSkin.visible = false;
			skin.testsSkin.selectorSkin.visible = false;

			switch ( states[ index ] )
			{
				
				//
				// Client Server Related
				//
				
				case "CSConnection" :
				
					// create new client-server connection test
				
					tester = new CSConnection( skin.testsSkin.textSkin , testedUrl , testerUrl );
					skin.testsSkin.textSkin.visible = true;
				
					break;	
					
				case "CSData" :

					// create new client-server data exchange test
				
					tester = new CSData( skin.testsSkin.textSkin , testedUrl , testerUrl );
					skin.testsSkin.textSkin.visible = true;
				
					break;	

				case "CSLive" :

					// create new client-server live stream test
				
					tester = new CSLive( skin.testsSkin.mixedSkin , testedUrl , testerUrl );
					skin.testsSkin.mixedSkin.visible = true;
				
					break;
					
				case "CSRecorded" :
				
					// create new client-server recorded stream test
				
					tester = new CSRecorded( skin.testsSkin.mixedSkin , testedUrl , testerUrl );
					skin.testsSkin.mixedSkin.visible = true;
				
					break;
					
				case "CSRecorder" :
				
					// create new client-server recorded stream test
				
					tester = new CSRecorder( skin.testsSkin.recorderSkin , testedUrl , testerUrl );
					skin.testsSkin.recorderSkin.visible = true;
				
					break;
				
				//
				// Server - Server
				//
					
				case "SSConnection" :

					// create new server-server connection test
				
					tester = new SSConnection( skin.testsSkin.textSkin , testedUrl , testerUrl );
					skin.testsSkin.textSkin.visible = true;
				
					break;
					
				case "SSData" :

					// create new server-server data exchange test
				
					tester = new SSData( skin.testsSkin.textSkin , testedUrl , testerUrl );
					skin.testsSkin.textSkin.visible = true;
				
					break;
					
				case "SSLive" :

					// create new server-server live stream test
				
					tester = new SSLive( skin.testsSkin.mixedSkin , testedUrl , testerUrl );
					skin.testsSkin.mixedSkin.visible = true;
				
					break;		
					
				case "SSRecorded" :

					// create new server-server recorded stream test
				
					tester = new SSRecorded( skin.testsSkin.mixedSkin , testedUrl , testerUrl );
					skin.testsSkin.mixedSkin.visible = true;
				
					break;
				
				//
				// Rtmp tricks
				//

				case "Multistream" :
				
					// create new client-server recorded stream test
				
					tester = new CSMultiStream( skin.testsSkin.mixedSkin , testedUrl , testerUrl );
					skin.testsSkin.mixedSkin.visible = true;
				
					break;

				case "FMSCompatibility" :
				
					// create new client-server recorded stream test
				
					tester = new FMSCompatibility( skin.testsSkin.mixedSkin , testedUrl , testerUrl );
					skin.testsSkin.mixedSkin.visible = true;
				
					break;
					
				//
				// Features
				// 

				case "BandwidthCheck" :

					// create new stream control test

					tester = new CSBandwidth( skin.testsSkin.textSkin , testedUrl , testerUrl );
					skin.testsSkin.textSkin.visible = true;
					
					break;
					
				case "StreamControl" :

					// create new stream control test

					tester = new StreamControl( skin.testsSkin.playerSkin , testedUrl , testerUrl );
					skin.testsSkin.playerSkin.visible = true;
					
					break;
					
				case "Stress" :
				
					// create new stress test				

					tester = new Stress( skin.testsSkin.stressSkin , testedUrl , testerUrl );
					skin.testsSkin.stressSkin.visible = true;
					
					break;
					
				//
				// Selector
				//
					
				default :
				
					// show test selector panel
				
					skin.testsSkin.selectorSkin.visible = true;
					skin.buttonSkin0.visible = false;
					
					break;				
					
			}			
			
		}
		
	}
	
}