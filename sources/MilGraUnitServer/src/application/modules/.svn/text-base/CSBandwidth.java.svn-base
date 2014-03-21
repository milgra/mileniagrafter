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

package application.modules;

/**
	
	CSBandwidth class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080316
	
	Tasks of CSBadnwidth 
	
		- accept a client for bandwidth testing

**/

import com.milgra.server.api.Client;
import com.milgra.server.api.WrapperList;
import com.milgra.server.api.IApplication;


public class CSBandwidth implements IModule
{
	
	/**
	 * CSBandwidth constructor
	 * @param applicationX mother application
	 */
	
	public CSBandwidth ( IApplication applicationX ) { }
	
	/**
	 * Client entering point
	 * @param clientX client
	 * @param argumentsX arguments
	 */
	
	public void onEnter ( Client clientX , WrapperList argumentsX )
	{

		System.out.println( System.currentTimeMillis( ) + " CSConnection.onEnter " + clientX + " " +argumentsX );
		clientX.accept( );
		
	}
	
	/**
	 * Closes module
	 **/
	
	public void onLeave ( Client clientX ) 
	{
		
		System.out.println( System.currentTimeMillis( ) +  " CSBandwidth.onClose " );
		
		
	}
	
}
