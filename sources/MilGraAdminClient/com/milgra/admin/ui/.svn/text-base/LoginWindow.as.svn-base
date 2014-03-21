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

package com.milgra.admin.ui
{
	
	import com.milgra.admin.CustomEvent;
	import com.milgra.admin.ui.SimpleButton;
	import com.milgra.admin.skin.LoginSkin;
	
	import flash.ui.Keyboard;
	import flash.net.SharedObject;
	import flash.text.TextField;
	
	import flash.events.MouseEvent;
	import flash.events.KeyboardEvent;
	import flash.events.EventDispatcher;
	
	/*
	
		Tasks of LoginWindow
		 	- let user type in host, username and password
		 	- on enter jump to next inputfield, or dispatch start event
	
	*/	
	
	public class LoginWindow extends EventDispatcher
	{

		public var hostField : TextField;
		public var userField : TextField;
		public var passField : TextField;
		public var infoField : TextField;
				
		public var skin : LoginSkin;
		public var shared : SharedObject;
		public var loginButton : SimpleButton;
		
		/**
		 * Creates a new loginwindow instance
		 * @param skinX skin
		 **/
		
		public function LoginWindow ( skinX : LoginSkin )
		{
		
			// trace( "LoginWindow.construct " + clipX + " " + clipX.stage );
			
			// create
			
			loginButton = new SimpleButton( skinX.buttonSkin0 );
			
			// set
			
			hostField = skinX.field0;
			userField = skinX.field1;
			passField = skinX.field2;
			infoField = skinX.field3;

			infoField.text = "Login";
			infoField.mouseEnabled = false;
			
			skin = skinX;
			shared = SharedObject.getLocal( "milgraadmin" );
			
			// event
			
			hostField.addEventListener( KeyboardEvent.KEY_DOWN , onKeyDown );
			userField.addEventListener( KeyboardEvent.KEY_DOWN , onKeyDown );
			passField.addEventListener( KeyboardEvent.KEY_DOWN , onKeyDown );
			
			loginButton.addEventListener( CustomEvent.ACTIVATE , onStart );
				
		}
		
		/**
		 * Sets info text
		 * @param infoX info
		 **/
		
		public function setInfo ( infoX : String ) : void
		{
			
			// trace( "LoginWindow.setInfo " + infoX );
			
			infoField.text = infoX;
			
		}
		
		/**
		 * Shows up login
		 **/
		
		public function show ( ):void
		{
			
			// trace( "LoginWindow.init " );
						
			if ( shared.data.url == null ) shared.data.url = "Type server IP or address here";
						
			userField.text = "username";
			passField.text = "password";
			hostField.text = shared.data.url;

			hostField.addEventListener( MouseEvent.MOUSE_DOWN , selectHost );
			userField.addEventListener( MouseEvent.MOUSE_DOWN , selectUser );
			passField.addEventListener( MouseEvent.MOUSE_DOWN , selectPass );

			selectHost( );			
			skin.visible = true;
			
		}
		
		/**
		 * Inits host input
		 * @param eventX event
		 **/
				
		public function selectHost ( eventX : MouseEvent = null ):void
		{
			
			// trace( "LoginWindow.selectURI" );
			
			skin.stage.focus = hostField;			
			hostField.setSelection( 0 , hostField.text.length );
			
		}
		
		/**
		 * Inits username selection
		 * @param eventX event
		 **/
		
		public function selectUser ( eventX : MouseEvent = null ):void
		{
			
			// trace( "LoginWindow.selectUsername" );
			
			skin.stage.focus = userField;			
			userField.setSelection( 0 , userField.text.length );
			
		}
		
		/**
		 * Inits password selection
		 * @param eventX event
		 **/
		
		public function selectPass ( eventX : MouseEvent = null ):void
		{
			
			// trace( "LoginWindow.selectUsername" );
			
			skin.stage.focus = passField;			
			passField.setSelection( 0 , passField.text.length );
			
		}		
		/**
		 * Key pressed event
		 * @param eventX event
		 **/
		
		public function onKeyDown( eventX : KeyboardEvent ):void
		{

			// trace( "LoginWindow.onKeyDown " + event.keyCode );			
			
			if ( eventX.keyCode == Keyboard.ENTER )
			{
				
				infoField.text = "Login";
									
				if ( skin.stage.focus == hostField ) selectUser(  ); else
				if ( skin.stage.focus == userField ) selectPass(  ); else
				if ( skin.stage.focus == passField ) onStart(  );
				
			}
			
		}
		
		/**
		 * Dispatches login event
		 * @param eventX event
		 **/
		
		public function onStart ( eventX : CustomEvent = null  ):void
		{
			
			// trace( "LoginWindow.dispatchLogin" );
			
			shared.data.url = hostField.text;
			shared.flush( );

			var passEvent : CustomEvent = new CustomEvent( CustomEvent.ACTIVATE );
			passEvent.host = hostField.text;
			passEvent.user = userField.text;
			passEvent.pass = passField.text;
									
			dispatchEvent( passEvent );
			infoField.text = "Connecting...";
					
		}

	}
	
}