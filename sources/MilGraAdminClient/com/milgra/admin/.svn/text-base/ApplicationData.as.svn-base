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

	import com.milgra.admin.ui.SimpleTable;
	
	import flash.utils.describeType;
	import flash.events.EventDispatcher;
	
	/*
	
		Tasks of application data
			- collect incoming application data
			- generate new items
			- delete removed items
			- update data
	
	*/
	
	public class ApplicationData extends EventDispatcher
	{
		
		public var items : Object;
		public var table : SimpleTable;
		public var actual : ApplicationItem;
		
		/**
		 * Application data constructor
		 **/
		
		public function ApplicationData ( )
		{
			
			// trace( "ApplicationData.construct" );
		
			items = new Object( );	

		}
		
		/**
		 * Sets the displaying table component
		 * @param tableX table
		 **/
		
		public function setTable ( tableX : SimpleTable ) : void
		{
			
			// trace( "ApplicationData.setTable " + tableX );
			
			table = tableX;
			
		}
		
		/**
		 * An item is selected
		 * @param eventX event
		 **/
		
		public function onSelect ( eventX : CustomEvent ) : void
		{
			
			// trace( "ApplicationData.onSelect " + eventX );
			
			// deactivate previously activated items
			
			if ( actual != null ) actual.deactivate( );
			actual = eventX.item;
			
			var event : CustomEvent = new CustomEvent( CustomEvent.SELECT );
			event.id = eventX.id;
			
			dispatchEvent( event );
			
		}
		
		/**
		 * Updates items
		 * @param listX new states
		 **/
		
		public function update ( listX : Object ) : void
		{
			
			// trace( "ApplicationDataProvider.update " + listX );
			
			var key : String;
			
			// create new items
			
			for ( key in listX )
				if ( items[ key ] == null )
				{
					items[ key ] = new ApplicationItem( );
					items[ key ].addEventListener( CustomEvent.SELECT , onSelect );
					table.addItem( items[ key ] );
				}
				
			// delete disappeared items
					
			for ( key in items )
				if ( listX[ key ] == null )
				{
					table.removeItem( items[ key ] );
					items[ key ].removeEventListener( CustomEvent.SELECT , onSelect );
					items[ key ] = null;
				}
				
			// update info

			for ( key in listX ) 
				if ( items[ key ] != null )
					items[ key ].update( listX[ key ] );		
			
		}		

	}
	
}