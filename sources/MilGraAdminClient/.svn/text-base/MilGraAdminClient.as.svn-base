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

	import com.milgra.admin.CustomEvent;
	import com.milgra.admin.GraphiconData;
	import com.milgra.admin.ApplicationData;

	import com.milgra.admin.ui.Grapher;
	import com.milgra.admin.ui.Scrollbar;
	import com.milgra.admin.ui.SimpleTable;
	import com.milgra.admin.ui.LoginWindow;
	import com.milgra.admin.ui.PopupWindow;
	import com.milgra.admin.ui.SimpleButton;

	import com.milgra.admin.skin.Skin;
	
	import flash.net.NetStream;
	import flash.net.NetConnection;
	import flash.net.ObjectEncoding;

	import flash.display.Sprite;
	import flash.display.MovieClip;
	import flash.display.StageScaleMode;
	
	import flash.events.MouseEvent;
	import flash.events.NetStatusEvent;
	
	/**

		MilGraAdminClient class
		
		@mail milgra@milgra.com
		@author Milan Toth
		@version 20080315
	
		Tasks of OProcess
		
			- Initialize admin client and related functions

	**/
	
	public class MilGraAdminClient extends Sprite
	{
		
		// skin - skin displayobject
		// host - host address of server to admin
		// state - display state, graphs or applications
		// connection - netconnection object
		// application - actual application
		
		public var skin : Skin;
		public var host : String;
		public var state : String;
		public var connection : NetConnection;
		public var application : Object;		
		public var applications : ApplicationData;
		
		// graph viewers 
		
		public var graphA : GraphiconData;
		public var graphB : GraphiconData;
		
		// loadButton - application load indicator
		// unloadButton - application unload indicator
		// refreshButton - application refresh indicator
		
		public var loadButton : SimpleButton;
		public var unloadButton : SimpleButton;
		public var refreshButton : SimpleButton;
		
		// graphsButton - graphicon state switcher
		// applicationsButton - applications state switcher
		
		public var graphsButton : SimpleButton;
		public var applicationsButton : SimpleButton;
		
		public var loginWindow : LoginWindow;
		public var popupWindow : PopupWindow;
			
		public var table : SimpleTable;
		public var scrollbar : Scrollbar;
		
		/**
		 * MilGraAdminClient constructor
		 **/
		
		public function MilGraAdminClient ( )
		{
			
			// trace( "MilGraAdmin.construct" );
			
			// create
			
			skin = new Skin( );
			connection = new NetConnection( );
			applications = new ApplicationData( );
			
			graphA = new GraphiconData( skin.adminSkin.graphiconsSkin.graphASkin );
			graphB = new GraphiconData( skin.adminSkin.graphiconsSkin.graphBSkin );
			
			loginWindow = new LoginWindow( skin.loginSkin );
			popupWindow = new PopupWindow( skin.popupSkin );
			
			table = new SimpleTable( skin.adminSkin.applicationsSkin.tableSkin0 );
			scrollbar = new Scrollbar( skin.adminSkin.applicationsSkin.scrollbarSkin0 )
			
			loadButton = new SimpleButton( skin.adminSkin.applicationsSkin.buttonSkin0 );
			unloadButton = new SimpleButton( skin.adminSkin.applicationsSkin.buttonSkin2 );
			refreshButton = new SimpleButton( skin.adminSkin.applicationsSkin.buttonSkin1 );
			
			graphsButton = new SimpleButton( skin.adminSkin.buttonSkin0 );
			applicationsButton = new SimpleButton( skin.adminSkin.buttonSkin1 );

			// set
			
			host = "";
			state = "graphs";
			
			stage.frameRate = 25;
			stage.scaleMode = StageScaleMode.NO_SCALE;
			table.setScrollbar( scrollbar );
						
			connection.client = this;
			connection.objectEncoding = ObjectEncoding.AMF0;
			
			skin.adminSkin.visible = false;
			applications.setTable( table );
			
			// eventXs
			
			graphsButton.addEventListener( CustomEvent.ACTIVATE, onGraphs );
			applicationsButton.addEventListener( CustomEvent.ACTIVATE , onApplications );
			
			loadButton.addEventListener( CustomEvent.ACTIVATE , onLoad );
			unloadButton.addEventListener( CustomEvent.ACTIVATE , onUnload );
			refreshButton.addEventListener( CustomEvent.ACTIVATE , onRefresh );

			connection.addEventListener( NetStatusEvent.NET_STATUS , onStatus );
			applications.addEventListener( CustomEvent.ACTIVATE , onSelect );
						
			loginWindow.addEventListener( CustomEvent.ACTIVATE , onLogin );
			popupWindow.addEventListener( CustomEvent.ACTIVATE , clearStripes );			
						
			// start
			
			addChild( skin );
			login( );
			
		}
		
		/**
		 * Inits login
		 **/		
		
		public function login ( ) : void
		{
			
			// trace( "MilGraAdminClient.login" );
			
			loginWindow.show( );
			skin.stripesSkin.visible = true;
			
		}
		
		/**
		 * Shows a popup
		 * @param messageX message
		 **/
		
		public function popup ( messageX : String ) : void
		{
			
			// trace( "MilGraAdminClient.popup " + messageX );
			
			popupWindow.show( messageX );
			skin.stripesSkin.visible = true;
			
		}
		
		/**
		 * Clears stripes
		 * @param eventX event
		 **/
		
		public function clearStripes ( eventX : CustomEvent ) : void
		{
			
			// trace( "MilGraAdminClient.clearStripes " + eventX );
			
			skin.stripesSkin.visible = false;
			
		}
				
		/**
		 * Sends username and password, attempts to connect
		 * @param eventXX eventX
		 **/
		 
		public function onLogin ( eventX : CustomEvent ):void
		{
			
			// trace( "MilgraChat.onLogin " + eventX.hostname + " " + eventX.username + " " + eventX.password );
			
			loginWindow.removeEventListener( CustomEvent.ACTIVATE , onLogin );
			connection.connect( "rtmp://" + eventX.host + "/milgraadmin" , eventX.user , eventX.pass );
			
		}
		
		/**
		 * Captures NetStatusEvents
		 * @param eventX event
		 **/		
		
		public function onStatus ( eventX : NetStatusEvent ):void
		{
			
			// trace( "MilGraAdmin.onStatus " + eventX.info.code );

			switch ( eventX.info.code )
			{
				
				case CustomEvent.SUCCESS :
				
					skin.stripesSkin.visible = false;
					skin.loginSkin.visible = false;
					switchState( "graphicons" );
					break;
					
				case CustomEvent.FAILURE :
				
					loginWindow.setInfo( "Failed to connect. Try again later." );
					login( );
					break;
					
				case CustomEvent.REJECTION : 
				
					loginWindow.setInfo( "Connection rejected : " + eventX.info.application );
					login( );
					break;
					
				case CustomEvent.CLOSURE :
					
					loginWindow.setInfo( "Connection closed by server" );
					skin.adminSkin.visible = false;
					skin.stripesSkin.visible = true;
					login( );
					break;
				
			}
			
		}
				
		/**
		 * Application selected
		 * @paramn eventX event
		 **/
		
		public function onSelect ( eventX : CustomEvent ):void
		{
			
			trace( "MilGraAdmin.onApplicationSelect " + eventX.item[0] );
			
			application = eventX.id;
			
		}
		
		/**
		 * Graphs button pressed
		 * @param eventX event
		 **/
		
		public function onGraphs ( eventX : CustomEvent ):void 
		{
			
			trace( "onGraphs " + eventX );
			switchState( "graphicons" );
			
		}
		
		/**
		 * Applications button pressed
		 * @param eventX event
		 **/
		
		public function onApplications ( eventX : CustomEvent ):void 
		{ 
			
			trace( "onApplications " + eventX );
			switchState( "applications" );

		}
		
		/**
		 * State switching
		 * @param stateX state
		 **/
		
		public function switchState ( stateX : String ) : void
		{
			
			trace( "MilGraAdminClient.switchState " + stateX );
			
			skin.adminSkin.visible = true;
			skin.adminSkin.applicationsSkin.visible = stateX == "applications";
			skin.adminSkin.graphiconsSkin.visible = stateX == "graphicons"; 
			
		}

		/**
		 * Application load button pressed
		 * @param eventX event
		 **/
		
		public function onLoad ( eventX : CustomEvent ):void
		{
			
			trace( "MilGraAdmin.onLoad" );
			
			connection.call( "load" , null , application );
			popupWindow.show( "Please wait for next refresh event to see changes" );
			
		}
		
		/**
		 * Applicaiton unload button pressed
		 * @param eventX event
		 **/
		 
		public function onUnload ( eventX : CustomEvent ):void
		{ 
		
			trace( "MilGraAdmin.onUnload " );
			
			connection.call( "unloadA" , null , application );
			popupWindow.show( "Please wait for next refresh event to see changes" );								
		
		}
		
		/**
		 * Application list refresh button pressed
		 * @param eventX event
		 **/
		 
		public function onRefresh ( eventX : CustomEvent ):void 
		{
			
			trace( "MilGraAdmin.onRefresh" );
			
			connection.call( "refresh" , null );
			popupWindow.show( "Please wait for next refresh event to see changes" );	
				
		}
		 
		/**
		 * Updates exchange data
		 * @param appData application related data
		 * @param graphData graph related data
		 **/
		 
		public function updateData ( applicationsX : Object , graphsX : Object ) : void
		{
		 	
		 	trace( "MilGraAdmin.updateData" );
		 	
		 	graphA.update( graphsX );
		 	graphB.update( graphsX );
		 	
		 	applications.update( applicationsX );
		 	
		}
				
	}
	
}
