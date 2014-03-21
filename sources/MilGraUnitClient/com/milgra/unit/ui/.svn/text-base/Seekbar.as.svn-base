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
	
	import flash.events.MouseEvent;
	import flash.events.EventDispatcher;
	import flash.display.MovieClip;
	
	/*
	
		Tasks of Seekbar
			- let the user select an arbitrary position on bar
	
	*/
	
	public class Seekbar extends EventDispatcher
	{
		
		// ratio - position ratio
		
		public var ratio : Number;
		
		// skin elements
		
		public var skin : MovieClip;
		public var seekPath : MovieClip;
		public var seekButton : MovieClip;
		
		/**
		 * Seekbar constructor
		 * @param skinX skin
		 **/
		
		public function Seekbar ( skinX : MovieClip )
		{
			
			trace( "Seekbar.construct " + skinX );
			
			seekPath = skinX.seekPathSkin;
			seekButton = skinX.seekButtonSkin;
			
			skin = skinX;
			seekPath.buttonMode = true;
			seekButton.buttonMode = true;
			
			seekPath.addEventListener( MouseEvent.MOUSE_DOWN , onDown );
			seekButton.addEventListener( MouseEvent.MOUSE_DOWN , onDown );
			
		}
		
		/**
		 * Mouse down event
		 * @param eventX event
		 **/
		
		public function onDown ( eventX : MouseEvent ) : void
		{
			
			// trace( "Seekbar.onDown " + eventX );

			skin.stage.addEventListener( MouseEvent.MOUSE_MOVE , onMove );
			skin.stage.addEventListener( MouseEvent.MOUSE_UP , onUp );
			
			ratio = seekPath.mouseX / seekPath.width;
			
			var event : CustomEvent = new CustomEvent( CustomEvent.COMPLETE );
			event.ratio = ratio;
			
			//dispatchEvent( event );
			update( );
			
		}
		
		/**
		 * Mouse move event
		 * @param eventX event
		 **/
		
		public function onMove ( eventX : MouseEvent ) : void
		{
			
			// trace( "Seekbar.onMove " + eventX );
			
			ratio = seekPath.mouseX / seekPath.width;
			update( );			
			
		}
		
		/**
		 * Mouse up event
		 * @param eventX mouse event
		 **/
		
		public function onUp ( eventX : MouseEvent ) : void
		{
			
			// trace( "Seekbar.onUp " + eventX );
			
			skin.stage.removeEventListener( MouseEvent.MOUSE_MOVE , onMove );
			skin.stage.removeEventListener( MouseEvent.MOUSE_UP , onUp );
			
			ratio = seekPath.mouseX / seekPath.width;
			
			var event : CustomEvent = new CustomEvent( CustomEvent.COMPLETE );
			event.ratio = ratio;
			
			dispatchEvent( event );
			update( );
			
			
		}
		
		/**
		 * Sets base ratio 
		 **/
		
		public function setRatio ( ratioX : Number ) : void
		{
			
			//trace( "Seekbar.setRatio " + ratioX );
			
			ratio = ratioX;
			update( );
			
		}
		
		/**
		 * updates skin 
		 **/
		
		public function update ( ) : void
		{
						
			seekButton.x = ratio * seekPath.width;

			// trace( "Seekbar.update " + ratio + " " + seekPath.width );
;
			
		}

	}
	
}