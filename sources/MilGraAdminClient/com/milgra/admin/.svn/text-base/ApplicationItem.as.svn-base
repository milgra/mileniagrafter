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

package com.milgra.admin
{

	import com.milgra.admin.skin.ItemSkin;

	import flash.text.TextField;

	import flash.display.Sprite;
	import flash.display.MovieClip;

	import flash.events.MouseEvent;
	import flash.events.EventDispatcher;
	
	/*
		Tasks of Applicationitem
			- create a new item skin
			- update fields with infos
			- let the user select the item
	
	*/
	
	public class ApplicationItem extends Sprite
	{
		
		public var skin : ItemSkin;
		public var indicatorClip : MovieClip;
		
		public var idField : TextField;
		public var stateField : TextField;
		public var bandinField : TextField;
		public var bandoutField : TextField;
		public var clientsField : TextField;
		
		/**
		 * ApplicationItem constructor
		 **/
		
		public function ApplicationItem (  )
		{
			
			// trace( "ApplicationItem.construct" );
			
			// create
			
			skin = new ItemSkin( );
			
			// set
			
			idField = skin.field0;
			stateField = skin.field1;
			bandinField = skin.field2;
			bandoutField = skin.field3;
			clientsField = skin.field4;
			indicatorClip = skin.clip0;
			
			// event
			
			skin.addEventListener( MouseEvent.CLICK , onSelect );
			
			// start
			
			addChild( skin );
			deactivate( );
			
		}
		
		/**
		 * Updates content
		 * @param dataX update data
		 **/
		
		public function update ( dataX : Object ) : void
		{
			
			// trace( "ApplicationItem.update " + dataX );
			
			idField.text = dataX.id;
			stateField.text = dataX.state;
			bandinField.text = dataX.bandin;
			bandoutField.text = dataX.bandout;
			clientsField.text = dataX.clients;
			
		}
		
		/**
		 * Activator functions
		 **/
		
		public function activate ( ) : void { indicatorClip.visible = true; }
		public function deactivate ( ) : void { indicatorClip.visible = false; }
		
		/**
		 * Select event
		 * @param eventX event
		 **/
		
		public function onSelect ( eventX : MouseEvent ) : void
		{
			
			// trace( "ApplicationItem.onSelect" );
			
			var event : CustomEvent = new CustomEvent( CustomEvent.SELECT );
			event.item = this;
			event.id = idField.text;
			
			dispatchEvent( event );
			activate( );
						
		}

	}
	
}