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

package com.milgra.unit.ui
{
	
	import com.milgra.unit.CustomEvent;
	import com.milgra.unit.ui.SimpleButton;
	import com.milgra.unit.skin.LoginSkin;
	
	import flash.ui.Keyboard;
	import flash.net.SharedObject;
	import flash.text.TextField;
	
	import flash.events.MouseEvent;
	import flash.events.KeyboardEvent;
	import flash.events.EventDispatcher;
	
	/*
	
		Tasks of LoginWindow
		 	- let user type in tested and tester host
		 	- on enter jump to next inputfield, or dispatch start event
	
	*/	
	
	public class LoginWindow extends EventDispatcher
	{

		public var testedField : TextField;
		public var testerField : TextField;
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
		
			trace( "LoginWindow.construct " );
			
			// create
			
			loginButton = new SimpleButton( skinX.buttonSkin0 );
			
			// set
			
			testedField = skinX.field0;
			testerField = skinX.field1;
			infoField = skinX.field2;

			infoField.text = "Login";
			infoField.mouseEnabled = false;
			
			skin = skinX;
			shared = SharedObject.getLocal( "milgraunit1" );
			
			// event
			
			testedField.addEventListener( KeyboardEvent.KEY_DOWN , onKeyDown );
			testerField.addEventListener( KeyboardEvent.KEY_DOWN , onKeyDown );
			
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
			
			trace( "LoginWindow.init " + shared.data.tested );
						
			if ( shared.data.tested == null ) shared.data.tested = "Type tested server IP or address here";
			if ( shared.data.tester == null ) shared.data.tester = "Type helper server IP or address here";
						
			testedField.text = shared.data.tested;
			testerField.text = shared.data.tester;

			testedField.addEventListener( MouseEvent.MOUSE_DOWN , selectTested );
			testerField.addEventListener( MouseEvent.MOUSE_DOWN , selectTester );

			selectTested( );			
			skin.visible = true;
			
		}
		
		/**
		 * Inits host input
		 * @param eventX event
		 **/
				
		public function selectTested ( eventX : MouseEvent = null ):void
		{
			
			// trace( "LoginWindow.selectURI" );
			
			skin.stage.focus = testedField;			
			testedField.setSelection( 0 , testedField.text.length );
			
		}
		
		/**
		 * Inits username selection
		 * @param eventX event
		 **/
		
		public function selectTester ( eventX : MouseEvent = null ):void
		{
			
			// trace( "LoginWindow.selectUsername" );
			
			skin.stage.focus = testerField;			
			testerField.setSelection( 0 , testerField.text.length );
			
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
									
				if ( skin.stage.focus == testedField ) selectTester(  ); else
				if ( skin.stage.focus == testerField ) onStart(  );
				
			}
			
		}
		
		/**
		 * Dispatches login event
		 * @param eventX event
		 **/
		
		public function onStart ( eventX : CustomEvent = null  ):void
		{
			
			// trace( "LoginWindow.dispatchLogin" );
			
			shared.data.tested = testedField.text;
			shared.data.tester = testedField.text;
			shared.flush( );

			var passEvent : CustomEvent = new CustomEvent( CustomEvent.ACTIVATE );
			passEvent.testedUrl = testedField.text;
			passEvent.testerUrl = testerField.text;
									
			dispatchEvent( passEvent );
			infoField.text = "Connecting...";
					
		}

	}
	
}