/*

   Milenia Grafter Server
  
   Copyright (c) 2007-2008 by Milan Toth. All rights reserved.
  
   This program is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public License
   of the License, or (at your option) any later version.
  
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
  
   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundataion, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA. 
   
*/ 

package com.milgra.server.api;

/**
	
	EventListener class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080316

	Tasks of EventListener
	
		- provide an outline for milenia events

**/

public class EventListener 
{
	
	public void onEvent ( ) { }
	public void onEvent ( InvokeEvent eventX ) { }
	public void onEvent ( StatusEvent eventX ) { }
	public void onEvent ( StreamEvent eventX ) { }

}
