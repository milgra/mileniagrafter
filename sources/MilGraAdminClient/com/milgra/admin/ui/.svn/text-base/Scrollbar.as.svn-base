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

	import flash.events.MouseEvent;
	import flash.events.EventDispatcher;
	
	import flash.display.Sprite;
	import flash.display.MovieClip;
	
	/*
	
		Tasks of Scrollbar
			- update scroller visibility, size and position based on status
			- let the user to push scroll buttons, drag scroller
	
	*/
	
	public class Scrollbar extends EventDispatcher
	{
		
		// ratio - position ratio of upper element and y coordinate of scroller
		// stepping - stepping values of scroll buttons
		// scrolling - we are under scrolling
		// sizeRatio - visibility ratio of visible content to all content and scroller height ot scrollbar height
		// dragPoint - the actual point where the user clicked the scroller button
	
		public var ratio : Number;
		public var stepping : Number;
		public var scrolling : Boolean;
		public var sizeRatio : Number;
		public var dragPoint : Number;
		
		// scrollSpace - the maximum space where the scroller can move. scrollbar height minus scroller height
		// scrollCapacity - scrolbar height
		// scrollerHeight - scroller height
		
		public var scrollSpace : Number;
		public var scrollerHeight : Number;
		
		// skin elements
		
		public var scrollPath : MovieClip;
		public var scrollButton : Sprite;
		public var scrollUpButton : MovieClip;
		public var scrollDownButton : MovieClip;
		public var scrollButtonSkin : MovieClip;
		
		/**
		 * Scrollbar constructor
		 * @param skinX skin
		 **/		
		
		public function Scrollbar( skinX : MovieClip )
		{
			
			// trace( "Scrollbar.construct " + skinX );
			
			// create holder for button resize
			
			scrollButton = new Sprite( );
			
			// set 

			ratio = 0;
			stepping = .1;
			sizeRatio = .5;
			
			scrollPath = skinX.scrollPathSkin;
			scrollUpButton = skinX.scrollUpButtonSkin;
			scrollDownButton = skinX.scrollDownButtonSkin;			
			scrollButtonSkin = skinX.scrollButtonSkin;

			scrollerHeight = scrollPath.height * sizeRatio;
									
			// button events
			
			scrollPath.buttonMode = true;
			scrollPath.mouseEnabled = false;
			scrollPath.addEventListener( MouseEvent.MOUSE_DOWN , onJump );
			
			scrollButton.buttonMode = true;
			scrollButton.mouseChildren = false;
			scrollButton.addEventListener( MouseEvent.MOUSE_DOWN , onDrag );
			
			scrollUpButton.buttonMode = true;
			scrollUpButton.mouseChildren = false;
			scrollUpButton.addEventListener( MouseEvent.CLICK , onScrollUp );

			scrollDownButton.buttonMode = true;
			scrollDownButton.mouseChildren = false;
			scrollDownButton.addEventListener( MouseEvent.CLICK , onScrollDown );
			
			// start
			
			skinX.addChild( scrollButton );
			scrollButton.addChild( scrollButtonSkin );
			
			scrollButtonSkin.y = 0;
			scrollButtonSkin.height = scrollerHeight;
			
		}
		
		/**
		 * Scrollpath clicked, jump to positon
		 * @param eventX mouse event
		 **/
		
		public function onJump ( eventX : MouseEvent ) : void
		{
			
			// trace( "Scrollbar.onJump " + eventX.localY );
						
			var position : Number = eventX.localY;
								
			if ( position < 0 ) position = 0; else
			if ( position > scrollSpace ) position = scrollSpace;

			setRatio( position / scrollSpace );
			
			var event : CustomEvent = new CustomEvent( CustomEvent.CHANGE );
			event.ratio = ratio;
			dispatchEvent( event );
			
		}
		
		/**
		 * Scroller clicked, drag
		 * @param eventX mouse event
		 **/
		
		public function onDrag ( eventX : MouseEvent ) : void
		{
			
			// trace( "Scrollbar.onDrag " + eventX.localY );
			
			scrolling = true;
			dragPoint = scrollButton.mouseY;
			
			// events
			
			scrollButton.stage.addEventListener( MouseEvent.MOUSE_MOVE , onMove );
			scrollButton.stage.addEventListener( MouseEvent.MOUSE_UP , onDrop );
			
		}
		
		/**
		 * Scroller released, end drag
		 * @param eventX mouse event
		 **/
		
		public function onDrop ( eventX : MouseEvent ) : void
		{
			
			// trace( "Scrollbar.onDrop " + eventX );
			
			scrolling = false;
			scrollButton.stage.removeEventListener( MouseEvent.MOUSE_MOVE , onMove );
			scrollButton.stage.removeEventListener( MouseEvent.MOUSE_UP , onDrop );
		
			// dispatch position
		
			var event : CustomEvent = new CustomEvent( CustomEvent.CHANGE );
			event.ratio = ratio;
			dispatchEvent( event );	
				
		}
		
		/**
		 * Scroler moved
		 * @param eventX mouse event
		 **/
		
		public function onMove ( eventX : MouseEvent ) : void
		{
			
			// trace( "Scrollbar.onMove" );
			
			// get position
			
			var position : Number = scrollPath.mouseY - dragPoint;
						
			if ( position < 0 ) position = 0; else
			if ( position > scrollSpace ) position = scrollSpace;
			
			// set position

			setRatio( position / scrollSpace );
			
			var event : CustomEvent = new CustomEvent( CustomEvent.CHANGE );
			event.ratio = ratio;
			dispatchEvent( event );
			
		}

		/**
		 * Scroll up pressed
		 * @param eventX mouse event
		 **/
		
		public function onScrollUp ( eventX : MouseEvent ) : void
		{
			
			// trace( "Scrollbar.onScrollUp " + eventX );
			
			ratio -= stepping;
			
			if ( ratio > 1 ) ratio = 1; else
			if ( ratio < 0 ) ratio = 0;
			
			// set position

			setRatio( ratio );			
						
			var event : CustomEvent = new CustomEvent( CustomEvent.CHANGE );
			event.ratio = ratio;
			
			dispatchEvent( event );

		}
		
		/**
		 * Scroll down pressed
		 * @param eventX mouse event
		 **/
		
		public function onScrollDown ( eventX : MouseEvent ) : void
		{
			
			// trace( "Scrollbar.onScrollDown " + eventX );
			
			ratio += stepping;
			if ( ratio > 1 ) ratio = 1; else
			if ( ratio < 0 ) ratio = 0;
			
			// set position

			setRatio( ratio );	
						
			var event : CustomEvent = new CustomEvent( CustomEvent.CHANGE );
			event.ratio = ratio;
			
			dispatchEvent( event );
			
		}
		
		/**
		 * Sets scroll ratio
		 * @param pRatio ratio 
		 **/
		
		private function setRatio ( pRatio : Number ) : void
		{
			
			// trace( "Scrollbar.setRatio" );
			
			ratio = pRatio;
			scrollButton.y = scrollPath.y + scrollSpace * ratio;; 			
			
		}
		
		/**
		 * Sets status of scrollbar
		 * @param indexX top position
		 * @param visibleX visible size
		 * @param allX overall size
		 **/

		public function setStatus ( indexX : Number , visibleX : Number , allX : Number ) : void
		{
			
			// trace( "Scrollbar.setStatus " + indexX + " " + visibleX + " " + allX );
			
			// calculate top position ratio
			
			ratio = indexX / ( allX - visibleX );
			
			// calcualte scroller size
			
			scrollerHeight = scrollPath.height * ( visibleX / allX );
			
			// get scrolling space
			
			scrollSpace = scrollPath.height - scrollerHeight;
			
			// check need
			
			scrollButton.visible = scrollUpButton.enabled = scrollDownButton.enabled = scrollerHeight >= scrollPath.height;
			
			// set finals
			
			if ( scrollButton.visible ) scrollButtonSkin.height = scrollerHeight;	
			if ( !scrolling ) setRatio( ratio );
			
		}

	}
	
}