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
	import com.milgra.admin.skin.PopupSkin;
	
	import flash.text.TextField;
	import flash.events.EventDispatcher;

	/*
	
		Tasks of Popupwindow
			- show up a popupwindow with a message
			- let the user accept the message
			
	*/
	
	public class PopupWindow extends EventDispatcher
	{
		
		public var skin : PopupSkin;
		public var infoField : TextField;
		public var acceptButton : SimpleButton;
		
		/**
		 * Popupwindow constructor
		 * @param skinX skin
		 **/
		
		public function PopupWindow ( skinX : PopupSkin )
		{
			
			// trace( "PopupWindow.construct " + skinX );
			
			// create

			acceptButton = new SimpleButton( skinX.buttonSkin0 );

			// set
		
			skin = skinX;
			infoField = skinX.field0;
			
			// event

			acceptButton.addEventListener( CustomEvent.ACTIVATE , onAccept );
			
			// start
			
			skin.visible = false;
	
		}
		
		/**
		 * Shows up a message
		 * @param messageX message
		 **/
		
		public function show ( messageX : String ):void
		{
			
			// trace( "PopupWindow.show " + messageX );
			
			skin.visible = true;
			infoField.text = messageX;
			
		}
		
		/**
		 * Accept
		 * @param eventX event
		 **/
		
		public function onAccept ( eventX : CustomEvent ):void
		{
			
			// trace( "PopupWindow.onAccpet " + eventX );
			
			var event : CustomEvent = new CustomEvent( CustomEvent.ACTIVATE );
			dispatchEvent( event );
			
			skin.visible = false;
			
		}
	
	}
	
}