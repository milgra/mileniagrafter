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

package com.milgra.server.encoder;

/**
	
	RtmpFactory class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080316

	Tasks of RtmpFactory
	
		- Create and fill up rtmp flow related packages

**/

import com.milgra.server.Library;
import com.milgra.server.RtmpPacket;
import com.milgra.server.api.Wrapper;
import com.milgra.server.api.WrapperList;
import com.milgra.server.api.WrapperMap;


public class RtmpFactory 
{
	
	/**
	 * Creates an rtmp chunk size changer packet
	 * @param sizeX chunk size
	 * @return packet
	 **/
	
	public static RtmpPacket chunk ( int sizeX )
	{

		// System.out.println( System.currentTimeMillis() + " RtmpFactry.chunk " + sizeX );

		RtmpPacket packet = new RtmpPacket( );
		
		packet.body = Encoder.intToBytes( sizeX , 4 );
		packet.first = true;
		packet.bodyType = 0x01;
		packet.rtmpChannel = 0x02;
		
		return packet;		
		
	}
	
	/**
	 * Create a ping packet for pinging
	 * @param typeX ping type - 0x06 or 0x07
	 * @param part1X
	 * @param part2X
	 * @param part3X
	 * @return packet
	 */
	
	public static RtmpPacket ping ( int typeX , int part1X , int part2X , int part3X )
	{
		
		// System.out.println( System.currentTimeMillis() + " RtmpFactory.ping " + typeX + " " + part1X + " " + part2X + " " + part3X );

		RtmpPacket packet = new RtmpPacket( );
		
		byte [ ] type = Encoder.intToBytes( typeX , 2 );
		byte [ ] part1 = Encoder.intToBytes( part1X , 4 );
		byte [ ] part2 = Encoder.intToBytes( part2X , 4 );
		byte [ ] part3 = Encoder.intToBytes( part3X , 4 );

		packet.body = Encoder.concatenate( type , part1 , part2 , part3 );
		packet.first = true;
		packet.bodyType = 0x04;
		packet.rtmpChannel = 0x02;
		
		return packet;		
		
	}
	
	/**
	 * Creates a stream control packet
	 * @param typeX type of message
	 * @param idX stream id
	 * @return packet
	 */
	
	public static RtmpPacket streamControl ( int typeX , int idX )
	{
	
		// System.out.println( System.currentTimeMillis() + " RtmpFactory.streamControl " + typeX + " " + idX );

		RtmpPacket packet = new RtmpPacket( );
		
		byte [ ] type = Encoder.intToBytes( typeX , 2 );
		byte [ ] body = Encoder.intToBytes( idX , 4 );

		packet.body = Encoder.concatenate( type , body );
		packet.first = true;
		packet.bodyType = 0x04;
		packet.rtmpChannel = 0x02;
		
		return packet;		
		
	}
	
	/**
	 * Creates a buffer length set packet
	 * @param streamIdX stream id
	 * @param lengthX buffer length
	 * @return packet
	 */
		
	public static RtmpPacket bufferLength ( int streamIdX , int lengthX )
	{
		
		// System.out.println( System.currentTimeMillis() + " RtmpFactory.bufferLength " + streamIdX + " " + lengthX );
		
		RtmpPacket packet = new RtmpPacket( );
		
		byte [ ] type = Encoder.intToBytes( 3 , 2 );
		byte [ ] body = Encoder.intToBytes( streamIdX , 4 );
		byte [ ] size = Encoder.intToBytes( lengthX , 4 );

		packet.body = Encoder.concatenate( type , body , size );
		packet.first = true;
		packet.bodyType = 0x04;
		packet.rtmpChannel = 0x02;
		
		return packet;
		
	}
	
	/**
	 * Creates a data start invoke packet
	 * @return packet
	 */
	
	public static RtmpPacket dataStart ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " RtmpFactory.dataStart " );

		WrapperMap data = new WrapperMap( );
		RtmpPacket packet = new RtmpPacket( );
		WrapperList arguments = new WrapperList( );
		
		data.put( "code" , Library.DATASTART );

		// create invoke object
				
		arguments.add( new Wrapper( "onStatus" ) );
		arguments.add( data );
		
		// fill up packet

		packet.rtmpChannel = 0x05;
		packet.bodyType = 0x12;
		packet.body = Encoder.encode( arguments );
		
		return packet;
		
	}
	
	/**
	 * Creates a sample access packet
	 * @param audioX audio access state
	 * @param videoX video access state
	 * @return
	 */
	
	public static RtmpPacket sampleAccess ( boolean audioX , boolean videoX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " RtmpFactory.sampleAccess " );

		RtmpPacket packet = new RtmpPacket( );
		WrapperList arguments = new WrapperList( );
		
		// fill up sample packet message arguments
		
		arguments.add( "|RtmpSampleAccess" );
		arguments.add( audioX );
		arguments.add( videoX );
		
		// acces packet
		
		packet.rtmpChannel = 0x05;
		packet.bodyType = 0x12;
		packet.first = true;
		packet.body = Encoder.encode( arguments );
		
		return packet;
		
	}
	
	/**
	 * Creates a seek notify status invoke
	 * @param positionX
	 * @param streamIdX
	 * @param streamNameX
	 * @return
	 */
	
	public static RtmpPacket seekNotify ( int positionX , int streamIdX , double clientIdX , String streamNameX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " RtmpFactory.seekNotify " + positionX + " " + streamIdX );
		
		WrapperMap seek = new WrapperMap( Library.STATUSKEYS , Library.SEEKARR );
		RtmpPacket packet = new RtmpPacket( );
		WrapperList arguments = invokeRoot( "onStatus" );
		
		// create status object

		seek.put( "description" , seek.getString( "description" ) + ( int ) positionX + " (stream ID: " + streamIdX + ")" );
		seek.put( "clientid" , clientIdX );
		seek.put( "details" , streamNameX );
		
		arguments.add( seek );
		
		// fill up packet

		packet.body = Encoder.encode( arguments );
		packet.first = true;
		packet.bodyType = 0x14;
		packet.flvStamp = positionX;
		packet.rtmpChannel = 0x05;
		
		return packet;
		
	}
	
	/**
	 * Creates a play stop message
	 * @param streamIdX
	 * @param streamNameX
	 * @return
	 */
	
	public static RtmpPacket playStop ( double clientIdX , String streamNameX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " RtmpFactory.playStop " + streamIdX + " " + streamNameX );
		
		WrapperMap stop = new WrapperMap( Library.STATUSKEYS , Library.PLAYSTOPARR );
		RtmpPacket packet = new RtmpPacket( );
		WrapperList arguments = invokeRoot( "onStatus" );

		stop.put( "description" , stop.getString( "description" ) + streamNameX );
		stop.put( "clientid" , clientIdX );
		
		arguments.add( stop );
		
		// fill up packet

		packet.body = Encoder.encode( arguments );
		packet.bodyType = 0x14;
		packet.rtmpChannel = 0x05;
		
		return packet;
		
	}
	
	/**
	 * Creates a play start status invoke
	 * @param streamIdX
	 * @param streamNameX
	 * @return
	 */
	
	public static RtmpPacket playStart ( double clientIdX , String streamNameX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " RtmpFactory.playStart " + streamIdX + " " + streamNameX );

		WrapperMap start = new WrapperMap( Library.STATUSKEYS , Library.PLAYSTARTARR );
		RtmpPacket packet = new RtmpPacket( );
		WrapperList arguments = invokeRoot( "onStatus" );

		start.put( "description" , start.getString( "description" ) + streamNameX );
		start.put( "clientid" , clientIdX );		

		arguments.add( start );
		
		// fill up packet
		
		packet.rtmpChannel = 0x05;
		packet.bodyType = 0x14;
		packet.body = Encoder.encode( arguments );
		
		return packet;
		
	}
	
	/**
	 * Creates a play reset status invoke
	 * @param streamIdX
	 * @param streamNameX
	 * @return
	 */
	
	public static RtmpPacket playReset ( double clientIdX , String streamNameX )
	{		
		
		// System.out.println( System.currentTimeMillis( ) + " RtmpFactory.playReset " + streamIdX + " " + streamNameX );

		WrapperMap reset = new WrapperMap( Library.STATUSKEYS , Library.PLAYRESETARR );
		RtmpPacket packet = new RtmpPacket( );
		WrapperList arguments = invokeRoot( "onStatus" );
		
		reset.put( "description" , reset.get( "description" ) + streamNameX );
		reset.put( "clientid" , clientIdX );
		reset.put( "details" , streamNameX );
		
		arguments.add( reset );
		
		// fill up packet
		
		packet.rtmpChannel = 0x05;
		packet.bodyType = 0x14;
		packet.body = Encoder.encode( arguments );

		return packet;
		
	}
	
	/**
	 * Creates a play failed status invoke
	 * @param streamIdX
	 * @param streamNameX
	 * @return
	 */
	
	public static RtmpPacket playFailed ( double clientIdX , String streamNameX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " RtmpFactory.playFailed " + streamIdX + " " + streamNameX );

		WrapperMap failed = new WrapperMap( Library.STATUSKEYS , Library.PLAYFAILEDARR );		
		RtmpPacket packet = new RtmpPacket( );
		WrapperList arguments = invokeRoot( "onStatus" );
		
		failed.put( "description" , failed.get( "description" ) + streamNameX );
		failed.put( "clientid" , clientIdX );
		failed.put( "details" , streamNameX );
		
		arguments.add( failed );
		
		// fill up packet
		
		packet.rtmpChannel = 0x05;
		packet.bodyType = 0x14;
		packet.body = Encoder.encode( arguments );

		return packet;		
		
	}
	
	/**
	 * Creates a play failed status invoke
	 * @param streamIdX
	 * @param streamNameX
	 * @return
	 */
	
	public static RtmpPacket publishStart ( double clientIdX , String streamNameX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " RtmpFactory.playFailed " + clientIdX + " " + streamNameX );

		WrapperMap start = new WrapperMap( Library.STATUSKEYS , Library.PUBLISHSTARTARR );
		RtmpPacket packet = new RtmpPacket( );
		WrapperList arguments = invokeRoot( "onStatus" );
		
		start.put( "description" , streamNameX + start.getString( "description" ) );
		start.put( "clientid" , clientIdX );
		start.put( "details" , streamNameX );
		
		arguments.add( start );
		
		// fill up packet
		
		packet.rtmpChannel = 0x05;
		packet.bodyType = 0x14;
		packet.body = Encoder.encode( arguments );

		return packet;		
		
	}	
	
	/**
	 * Creates a play failed status invoke
	 * @param streamIdX
	 * @param streamNameX
	 * @return
	 */
	
	public static RtmpPacket noAccess ( double clientIdX , String streamNameX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " RtmpFactory.noAccess " + clientIdX + " " + streamNameX );

		WrapperMap noaccess = new WrapperMap( Library.STATUSKEYS , Library.RECORDNOACCESSARR );
		RtmpPacket packet = new RtmpPacket( );
		WrapperList arguments = invokeRoot( "onStatus" );
		
		noaccess.put( "description" , noaccess.getString( "description" ) + streamNameX + "." );
		noaccess.put( "clientid" , clientIdX );
		
		arguments.add( noaccess );
		
		// fill up packet
		
		packet.rtmpChannel = 0x05;
		packet.bodyType = 0x14;
		packet.body = Encoder.encode( arguments );

		return packet;		
		
	}	
	
	/**
	 * Creates a play failed status invoke
	 * @param streamIdX
	 * @param streamNameX
	 * @return
	 */
	
	public static RtmpPacket publishStop ( double clientIdX , String streamNameX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " RtmpFactory.publishStop " + clientIdX + " " + streamNameX );

		WrapperMap unpublish = new WrapperMap( Library.STATUSKEYS , Library.UNPUBLISHSUCCESSARR );
		RtmpPacket packet = new RtmpPacket( );
		WrapperList arguments = invokeRoot( "onStatus" );

		unpublish.put( "description" , streamNameX + unpublish.getString( "description" ) );
		unpublish.put( "clientid" , clientIdX );
		
		arguments.add( unpublish );
		
		// fill up packet
		
		packet.rtmpChannel = 0x05;
		packet.bodyType = 0x14;
		packet.body = Encoder.encode( arguments );

		return packet;		
		
	}
	
	/**
	 * Creates an empty flv packet
	 * @param typeX
	 * @param stampX
	 * @return
	 */
	
	public static RtmpPacket emptyFrame ( int typeX , int stampX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " RtmpFactory.emptyFrame " + typeX + " " + stampX );
		
		RtmpPacket packet = new RtmpPacket( );
		packet.flvStamp = stampX;
		packet.bodyType = typeX;
		packet.body = new byte [ 0 ];
		packet.first = true;
		
		return packet;
		
	}
	
	/**
	 * Creates a stream duration metadata invoke
	 * @param durationX
	 * @return
	 */
	
	public static RtmpPacket streamDuration ( double durationX )
	{

		// System.out.println( System.currentTimeMillis( ) + " RtmpFactory.streamDuration " + durationX );
		
		RtmpPacket packet = new RtmpPacket( );
		WrapperMap metadata = new WrapperMap( );
		WrapperList arguments = new WrapperList( );

		// create invoke object

		metadata.put( "duration" , durationX / 1000 );
		
		arguments.add( new Wrapper( "onMetaData" ) );
		arguments.add( metadata );
		
		// fill up packet

		packet.body = Encoder.encode( arguments );
		packet.bodyType = 0x12;
		packet.rtmpChannel = 0x05;
		
		// send
		
		return packet;
		
	}
	
	/**
	 * Creates a root invoke argument list
	 * @param idX inovke id
	 * @return
	 */
	
	public static WrapperList invokeRoot ( String idX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " RtmpFactory.invokeRoot " + idX );

		WrapperList arguments = new WrapperList( );
		
		arguments.add( new Wrapper( idX ) );
		arguments.add( new Wrapper( 0 ) );
		arguments.add( new Wrapper( ) );
		
		return arguments;
		
	}

}
