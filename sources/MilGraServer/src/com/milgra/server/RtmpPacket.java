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

	RtmpPacket class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080315

	Tasks of RtmpPacket
	
		- Hold packet related information and packet body
		- Clone packet function
		- Show packet as string if needed
		
**/


public class RtmpPacket 
{
		
	public int bodyType = 0;
	public int bodySize = 0;
	public int bodyLeft = 0;
	
	public int flvStamp = 0;
	public int flvChannel = 0;
	public int rtmpChannel = 0x03;
	
	public boolean first;
	public byte [ ] body = new byte[ 0 ];
	
	/**
	 * RtmpPacket constructor, created a default packet
	 **/
	
	public RtmpPacket ( ) { }
	
	/**
	 * Clonse a packet
	 * @param packetX
	 **/
	
	public RtmpPacket ( RtmpPacket packetX )
	{
		
		// no need to clone body, nothing modifies body array after creation
		
		bodyType = packetX.bodyType;
		bodySize = packetX.bodySize;
		bodyLeft = packetX.bodyLeft;
		
		flvStamp = packetX.flvStamp;
		flvChannel = packetX.flvChannel;
		rtmpChannel = packetX.rtmpChannel;
		
		body = packetX.body;
		first = packetX.first;
		
	}
	
	/**
	 * Returns packet information as string
	 * @return packet info as string
	 **/
	
	public String toString ( )
	{
		
		return bodyType + "|" + body.length + "|" + flvStamp + "|" + flvChannel + "|" + rtmpChannel;
		
	}

}
