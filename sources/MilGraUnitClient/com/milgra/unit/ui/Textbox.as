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
	
	import flash.text.TextField;
	import flash.text.TextFormat;
	
	import com.milgra.unit.CustomEvent;
	
	/*
		Tasks of TextBox
			- create a textfield, stor incoming text
			- refresh related scrollbar, listen for scroll events
	
	*/
	
	public class Textbox
	{

		// active - textbox is active
		// buffer - stored lines
		// clearCount - how many chars to clear on overflow
		
		private var active : Boolean;				
		private var buffer : int = 100;
		private var clearCount : int = 300;
		
		// field - actual textfield
		// format - textformat
		// myField - our original textfield
		// scrollbar -
		
		private var field : TextField;
		private var format : TextFormat;
		private var myField : TextField;
		private var scrollbar : Scrollbar;
		
		/**
		 * Textbox constructor
		 * @param fieldX field
		 * @param formatX format
		 **/
		
		public function Textbox( fieldX : TextField , formatX : TextFormat )
		{
			
			// trace( "Textbox.construct " + fieldX + " " + formatX );

			// create

			myField = new TextField( );
			
			// set
			
			active = true;
			field = fieldX;	
			format = formatX;
			
			fieldX.defaultTextFormat = formatX;
			myField.defaultTextFormat = formatX;

		}
		
		/**
		 * Adds new text
		 * @param textX text
		 **/
		
		public function add ( textX : String ) : void
		{
			
			// trace( "Textbox.add " + textX );
			
			// storing in our master field

			myField.appendText( textX + "\n" );
			myField.scrollV = myField.maxScrollV;
			if ( myField.numLines > buffer ) myField.replaceText( 0 , clearCount , "" );
			
			if ( active )
			{
				
				// adding it to the active field also
				
				field.appendText( textX + "\n" );
				field.scrollV = field.maxScrollV;
				if ( field.numLines > buffer ) field.replaceText( 0 , clearCount , "" );
				
				// refresh scrollbar
				
				if ( scrollbar != null ) 
				{
					
					var pos : Number = myField.scrollV;
					var vis : Number = ( myField.bottomScrollV - myField.scrollV + 1 );
					var max : Number = myField.numLines;

					scrollbar.setStatus( pos , vis , max  );
					
				}
				
			}
							
		}
		
		/**
		 * Clears field
		 **/
		
		public function clear ( ) : void
		{
			
			// trace( "Textbox.clear" );
			
			field.text = "";
			
		}
		
		/**
		 * sets scrollbar
		 * @param scrollbarX scrollbar
		 **/
		
		public function setScrollbar ( scrollbarX : Scrollbar ) : void 
		{ 
			
			// trace( "Textbox.setScrollbar " + scrollbarX );

			scrollbar = scrollbarX;
			scrollbar.addEventListener( CustomEvent.CHANGE , onScroll );
			
		}
		
		/**
		 * Activates textbox
		 * @param stateX state
		 **/
		
		public function setActive ( stateX : Boolean ) : void 
		{ 
			
			// trace( "Textbox.setActive " + stateX );
			
			active = stateX;
			
			if ( active ) 
			{
				
				// copy original text
				
				field.text = myField.text.slice( 0 );
				
				// refresh scrollbar
				
				if ( scrollbar != null ) 
				{
					
					var pos : Number = myField.scrollV;
					var vis : Number = ( myField.bottomScrollV - myField.scrollV + 1 );
					var max : Number = myField.numLines;

					scrollbar.setStatus( pos , vis , max  );
					
				}
				
			}
		
		}
		
		/**
		 * Scroll event
		 * @param eventX event
		 **/
		
		private function onScroll ( eventX : CustomEvent ) : void
		{
			
			// trace( "Textbox.onScroll " + eventX );
			
			if ( active ) field.scrollV = Math.round( eventX.ratio * field.maxScrollV );			
			
		}

	}
	
}