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

	ProcessGroup class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080315

	Tasks of Processgroup
	
		- Collect processes related to a specific function
		- Places new processes to the smallest group
		- Provides count and delay information

**/

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;


public class ProcessGroup 
{
	
	// name - name of group
	// counts - how many processes are there in the threads
	// threads - process threads
	// positions - where is a prcess in the count array
	
	public String name;
	public ArrayList < Integer > counts;
	public ArrayList < ProcessThread > threads;
	public HashMap < OProcess , Integer > positions;	
	
	/**
	 * ProcessGroup constructor
	 * @param nameX process group name
	 */
	
	public ProcessGroup ( String nameX )
	{
		
		// System.out.println( System.currentTimeMillis() + " " + nameX + " ProcessGroup.construct" );
		
		name = nameX;
		
		counts = new ArrayList < Integer > ( );
		threads = new ArrayList < ProcessThread > ( );
		positions = new HashMap < OProcess , Integer > ( );
			
		// one thread is needed at startup
		
		for ( int index = 0 ; index < Library.IOTHREAD ; index++ )
		{
			
			ProcessThread thread = new ProcessThread( );

			thread.start( );
			threads.add( thread );
			counts.add( 0 );
			
		}
		
	}
	
	/**
	 * Closes all threads
	 */
	
	public void close ( )
	{
		
		// System.out.println( System.currentTimeMillis() + " " + nameX + " ProcessGroup.close" );
		synchronized ( positions ) 
		{
			
			// switch them off
			
			for ( ProcessThread thread : threads ) thread.alive = false; 
			
		}
		
	}
	
	/**
	 * Returns average process delay
	 * @return
	 */
	
	public int getDelay ( ) 
	{ 
		
		// System.out.println( System.currentTimeMillis() + " " + nameX + " ProcessGroup.getDelay" );
		
		int delay = 0;
		for ( ProcessThread thread : threads ) 
		{
			
			// collect durations
			// request new duration update
			
			delay += thread.delay; 
			thread.update( );	
			
		}
		
		return ( int ) delay / threads.size( );
		
	}
	
	/**
	 * Returns process threads number
	 * @return
	 */
	
	public int getCount ( ) 
	{
		
		// System.out.println( System.currentTimeMillis() + " " + nameX + " ProcessGroup.getCount" );
		
		return threads.size( );
		
	}
	
	/**
	 * Adds a new Process
	 * @param ProcessX
	 */
	
	public void addProcess ( OProcess processX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " ProcessGroup.addProcess " + processX );
		
		if ( !positions.containsKey( processX ) )
		{

			synchronized ( positions )
			{
				
				// find smallest process group
				// get position of group
				
				int min = Collections.min( counts );
				int pos = counts.indexOf( min );
				
				// put process in thread
				
				threads.get( pos ).addProcess( processX );
				
				// refresh helper containers
				
				counts.set( pos , min + 1 );
				positions.put( processX , pos );
				
			}

		}
				
	}
	
	/**
	 * Removes a Process
	 * @param ProcessX
	 */
	
	public void removeProcess ( OProcess processX )
	{
		
		//System.out.println( System.currentTimeMillis( ) + " ProcessGroup.removeProcess " + processX );
	
		if ( positions.containsKey( processX ) )
		{
			
			synchronized ( positions )
			{
				
				// find related process group
				// get count of group
				
				int pos = positions.remove( processX );
				int cnt = counts.get( pos );
				
				threads.get( pos ).removeProcess( processX );
				
				// refresh helper
				
				counts.set( pos , cnt - 1 );
				
			}
	
		}		

	}

}
