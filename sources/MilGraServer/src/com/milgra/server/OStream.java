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

	OStream outline
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080315

	Tasks of OStream
	
		- Act as an interface for streams

**/

public class OStream
{
	
	public int getId( ) { return 0; };
	public long getClientId( ) { return 0; };
	public String getName ( ) { return null; };

	public void take( RtmpPacket packetX ) { };
	public void subscribe( OStream streamX ) { };
	public void unsubscribe ( OStream streamX ) { };

}
