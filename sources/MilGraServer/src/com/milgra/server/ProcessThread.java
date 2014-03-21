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
   Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA. 
   
*/ 

package com.milgra.server;

/**

	ProcessThread class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080315

	Tasks of Processthread
	
		- Execute related processes
		- Add/remove process
		- Calculate execution time
		
**/

import java.util.ArrayList;

public class ProcessThread extends Thread
{
	
	// count - delay check counter
	// start - delay check start stamp
	// delay - execution time
	
	public int count;
	public long start;
	public long delay;		
		
	// alive - thread is running
	// change - change in Process list
	
	public boolean alive;		
	public boolean change;
	
	// plus - processes to add
	// minus - processes to remove
	// processes - process list
	
	public ArrayList < OProcess > plus;			
	public ArrayList < OProcess > minus;		
	public ArrayList < OProcess > processes;	
	
	/**
	 * Creates a new ProcessThread instance
	 */
	
	public ProcessThread ( )
	{
		
		// System.out.println( System.currentTimeMillis() + " ProcessThread.construct " );

		count = 0;
		alive = true;
		change = false;
		
		plus = new ArrayList < OProcess > ( );
		minus = new ArrayList < OProcess > ( );
		processes = new ArrayList < OProcess > ( );
		
	}
	
	/**
	 * Starts Thread
	 */
	
	public void run ( )
	{
		
		// System.out.println( System.currentTimeMillis() + " ProcessThread.run " );
		
		try
		{
							
			while ( alive )
			{
				
				// step all processes
				
				for ( OProcess process : processes ) process.step( );

				// process change

				if ( change )
				{					
					
					synchronized ( plus )
					{
						
						// add/remove new processes
	
						processes.addAll( plus );
						processes.removeAll( minus );
						
						plus.clear( );
						minus.clear( );
						
						// calculate delay
					
						if ( count == 2 ) start = System.currentTimeMillis( ); else
						if ( count == 1 ) delay = System.currentTimeMillis( ) - start - Library.STEPTIME; else
						if ( count == 0 ) change = false;
						if ( count != 0 ) count--;
	
					}
					
				}
				
				// sleep for steptime
							
				Thread.sleep( Library.STEPTIME );
			
			}
			
		}
		catch ( InterruptedException exception ) { }
		
	}
	
	/**
	 * Starts a delay check process
	 */
	
	public void update ( )
	{
		
		//System.out.println( System.currentTimeMillis() + " ProcessThread.update " );
		
		count = 2;
		change = true;
		
	}
	
	/**
	 * Adds new Process
	 * @param ProcessX process
	 */
	
	public void addProcess ( OProcess processX )
	{
		
		//System.out.println( System.currentTimeMillis() + " ProcessThread.addProcess " + processX );
		
		synchronized ( plus ) 
		{ 
			
			plus.add( processX ); 
			change = true;
			
		}		
		
	}
	
	/**
	 * Removes Process
	 * @param ProcessX process
	 */
	
	public void removeProcess ( OProcess processX )
	{
		
		//System.out.println( System.currentTimeMillis() + " ProcessThread.removeProcess " + processX );
		
		synchronized ( plus ) 
		{
			
			minus.add( processX ); 
			change = true;
			
		}
		
	}
	
}
