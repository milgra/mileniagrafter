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
	
	Timer class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080316

	Tasks of Timer
	
		- timing
 
**/

public class Timer extends Thread 
{
	
	public long delay;
	public long after;
	public long before;
	public long worktime;
	public long newdelay;
	
	public int steps;
	public int counter;
	
	public boolean running;
	public EventListener event;
	
	/**
	 * Timer constructor
	 * @param delayX delay between events
	 * @param eventX the destination event
	 */

	
	public Timer ( int delayX , EventListener eventX )
	{

		delay = delayX;
		event = eventX;
		
		steps = 0;
		counter = 0;
		newdelay = delay;
		
		running = true;
		
	}

	public Timer ( int delayX , int countX , EventListener eventX )
	{

		delay = delayX;
		event = eventX;
		
		steps = countX;
		counter = 0;
		newdelay = delay;
		
		running = true;
		
	}
	
	/**
	 * Start
	 */
	
	public void run ( )
	{
		
		while ( running ) 
		{
			
			try 
			{
				Thread.sleep ( newdelay );
			} 
			catch ( InterruptedException exception ) { exception.printStackTrace( ); } 
			
			before = System.currentTimeMillis();
			event.onEvent( );
			after = System.currentTimeMillis();

			worktime = after - before;
			newdelay = delay - worktime;

			if ( newdelay < 0 ) newdelay = 1;
			
			if ( steps > 0 )
			{

				++counter;
				if ( counter == steps ) running = false;
				
			}

		}
		
	}
	
	/**
	 * Finishes thread
	 */
	
	public void finish ( )
	{
		
		running = false;
		
	}

}
