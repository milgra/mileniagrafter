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

package application;

/**

	Configreader class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080316
	
	Tasks of Configreader
	
		- read up config xml
		- store values

**/

public class ConfigReader 
{
	
	// ips - allowed ips
	// userName - username
	// passWord - password
	
	public String [ ] ips;
	public String userName;
	public String passWord;
	
	// accesLogging enabled
	// graphsLogging enabled

	public boolean accessLogging;
	public boolean graphsLogging;
	
	/**
	 * ConfigReader constructor
	 * @param urlX url
	 */
	
	public ConfigReader ( String urlX )
	{
		
		// System.out.println( System.currentTimeMillis() + " ConfigReader.construct" );

		XmlParser parser = new XmlParser( urlX );
	
		accessLogging = parser.getValue( "accesslogging" , 0 ).equals( "true" );
		graphsLogging = parser.getValue( "graphslogging" , 0 ).equals( "true" );
		
		userName = parser.getValue( "username" , 0 );
		passWord = parser.getValue( "password" , 0 );
		
		int ipnum = ( int ) parser.getCount( "enabled.ip" );
		ips = new String [ ipnum ];
		
		for ( int a = 0 ; a < ipnum ; a++ ) ips[a ] = parser.getValue( "enabled.ip" , a );

	}
	
	/**
	 * Tells if there is any ip node in config xml
	 * @return boolean
	 */
	
	public boolean hasIp ( )
	{

		// System.out.println( System.currentTimeMillis() + " ConfigReader.hasIp" );

		return ips.length == 0;
		
	}
	
	/**
	 * Tells if given ip is among allowed ips
	 * @param ipX ip
	 * @return boolean
	 */
	
	public boolean hasIp ( String ipX )
	{
		
		// System.out.println( System.currentTimeMillis() + " ConfigReader.hasIp " + ipX );

		for ( String ip : ips )
			if ( ipX.indexOf( ip ) > -1 ) return true;
		
		return false;
		
	}
	
}
