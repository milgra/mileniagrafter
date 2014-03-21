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
	
	import flash.display.MovieClip;
	import flash.display.DisplayObject;
	
	/*
	
		Tasks of Simpletable
			- show up assigned displayobject as items
			- in case of a scrollbar, change scroll position of items
			
	*/	
	
	public class SimpleTable
	{
		
		// index - index of the upper element
		// items - contained items
		
		// rows - skin row clips
		// scrollbar - scrollbar	
		
		public var index : int;
		public var items : Array;		
		
		public var rows : Array;
		public var scrollbar : Scrollbar;
		
		/**
		 * SimpleTable constructor
		 * @param skinX skin
		 **/		
		
		public function SimpleTable ( skinX : MovieClip )
		{
			
			// trace( "SimpleTable.construct " + skinX );

			// create
					
			rows = new Array( );
			items = new Array( );
			
			// set
			
			index = 0;
			while ( skinX[ "rowSkin" + index ] != null ) rows.push( skinX[ "rowSkin" + index ++ ] );
			index = 0;
						
		}
		
		/**
		 * Sets related scrollbar
		 * @param scrollbarX scrollbar
		 **/
		 
		public function setScrollbar ( scrollbarX : Scrollbar ) : void
		{
			
			// trace( "SimpleTable.setScrollBar " + scrollbarX );
			
			scrollbar = scrollbarX;
			scrollbar.addEventListener( CustomEvent.CHANGE , onScroll );
			
		}
		
		/**
		 * Add item
		 * @param itemX item
		 **/
		
		public function addItem ( itemX : DisplayObject ) : void
		{
			
			// trace( "SimpleTable.addItem " + itemX );

			items.push( itemX );			
			update( );
			
		}
		
		/**
		 * Remove item
		 * @param itemX item
		 **/
		
		public function removeItem ( itemX : DisplayObject ) : void
		{
			
			// trace( "SimpleTable.addItem " + itemX );

			items.splice( items.indexOf( itemX ) , 1 );		
			update( );
			
		}
		
		/**
		 * Updates table state
		 * @param scrollerX flag indicating scroller update
		 **/
		
		public function update ( scrollerX : Boolean = false ) : void
		{
			
			// trace( "SimpleTable.update " + rows.length + " " + items.length );

			for ( var row : int = 0 ; row < rows.length ; row ++ )
			{
				
				if ( rows[ row ].numChildren > 1 ) rows[ row ].removeChildAt( 1 );
				if ( index + row < items.length ) rows[ row ].addChild( items[ index + row ] );

			}
			
			if ( scrollbar != null && !scrollerX ) scrollbar.setStatus( index , rows.length , items.length );
			
		}
		
		/**
		 * Scroll event
		 * eventX event
		 **/
		
		public function onScroll ( eventX : CustomEvent ) : void
		{
			
			// trace( "SimpleTable.onScroll " + eventX );
			
			index = Math.round( eventX.ratio * ( items.length - rows.length ) );
			update( true );
			
		}		

	}
	
}