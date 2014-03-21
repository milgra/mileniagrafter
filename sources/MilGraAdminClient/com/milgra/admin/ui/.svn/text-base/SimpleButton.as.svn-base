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
	
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.events.EventDispatcher;

	import flash.display.MovieClip;
	import flash.display.DisplayObject;
	
	/*
	
		Tasks of Simplebutton
			- listen for button related mouse events
			- change view state on events
			
	*/
	
	public class SimpleButton extends EventDispatcher
	{
		
		public var skin : MovieClip;
		
		/**
		 * SimpleButton constructor
		 * @param pSkin button skin
		 **/
		
		public function SimpleButton ( pSkin : MovieClip )
		{
			
			// trace( "SimpleButton.construct " + pSkin );
			
			// set
			
			skin = pSkin;
			skin.buttonMode = true;
			skin.mouseChildren = false;
			
			// event
			
			skin.addEventListener( MouseEvent.MOUSE_DOWN, onMouse );
			skin.addEventListener( MouseEvent.MOUSE_UP, offMouse );
			
			// start
			
			skin.gotoAndStop( 1 );
			
		}
		
		/**
		 * onPress handler
		 * @param pEvent 
		 **/

		private function onMouse( eventX : MouseEvent ) : void
		{
			
			// trace( "SimpleButton.onMouse " + eventX );
			
			if ( skin.currentFrame != 2 ) 
			{

				skin.gotoAndStop( 2 );
				dispatchEvent( new CustomEvent( CustomEvent.ACTIVATE )  );
			
			}
			
		}
		
		/**
		 * onRelease handler
		 * @param pEvent 
		 **/

		private function offMouse( eventX : MouseEvent  ) : void
		{
			
			// trace( "SimpleButton.offMouse " + eventX );
			
			if ( skin.currentFrame != 1 )
			{

				skin.gotoAndStop( 1 );
				dispatchEvent( new CustomEvent( CustomEvent.DEACTIVATE )  );
			
			}
			
		}

	}

}