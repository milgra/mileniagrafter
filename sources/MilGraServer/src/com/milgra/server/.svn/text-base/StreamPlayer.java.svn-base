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

	StreamPlayer class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080315
	
	Tasks of Streamplayer
		- sort incoming flv rtmp packets based on their type
		- drop packets based on streamcontrollers frame dropping
		- create stream reader if flv requested

**/

import java.util.ArrayList;
import com.milgra.server.encoder.RtmpFactory;


public class StreamPlayer extends OStream 
{
	
	private int id;
	private int flvChannel;
	
	private int audioChannel;
	private int videoChannel;

	private String name;
	
	private boolean sendVideo;
	private boolean sendAudio;
	private boolean registered;
	
	private boolean paused;		
	private boolean hasFrame;
	
	private int frameType;

	private OStream injector;
	private RtmpPacket packet;
	public StreamReader reader;
	private StreamController controller;
	private ArrayList < RtmpPacket > buffer;
	
	/**
	 * StreamPlayer constructor
	 * @param idX - stream id
	 * @param flvChannelX - outgoing flv flvChannel
	 * @param videoChannelX - outgoing video packets rtmp channel
	 * @param audioChannelX - outgoing audio packets rtmp channel
	 * @param nameX - name of wanted stream
	 * @param controllerX - related stream controller
	 */
	
	public StreamPlayer ( int idX , 
						  int flvChannelX ,
						  int videoChannelX ,
						  int audioChannelX ,
						  String nameX , 
						  StreamController controllerX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + nameX + " StreamPlayer.construct " + idX );
		
		// create
	
		buffer = new ArrayList < RtmpPacket > ( );
		
		// set

		id = idX;
		name = nameX;
		controller = controllerX;	
		
		flvChannel = flvChannelX;
		videoChannel = videoChannelX;
		audioChannel = audioChannelX;

		paused = true;
		hasFrame = false;
		registered = false;
			
		sendVideo = true;
		sendAudio = true;
		
	}
	
	/**
	 * StreamPlayer destrcutor
	 **/
	
	public void destruct ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + name + " StreamPlayer.destruct " + id );
		
		paused = true;
		
		buffer = null;
		reader = null;
		injector = null;
		controller = null;
		
	}

	/**
	 * Close init
	 */
	
	public void close ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + name + " StreamPlayer.destruct " + id );
				
		if ( registered ) unregister( );
		if ( reader != null ) reader.close( );
		if ( injector != null ) injector.unsubscribe( this );
		if ( controller != null ) controller.removePlayer( this );
		
		destruct( );
		
	}
	
	/**
	 * Enables playing, constructs and sends out acces, reset and start stats messages
	 */
	
	public void enable ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + name + " StreamPlayer.enable " + id );

		paused = false;
		
		register( );
		
		if ( name.endsWith( ".flv" ) )
		{
			
			reader = new StreamReader( name , this );
			
		}
		else
		{			
			
			take( RtmpFactory.streamControl( 4 , id ) );
			take( RtmpFactory.streamControl( 0 , id ) );
			take( RtmpFactory.playReset( id , name ) );
			take( RtmpFactory.playStart( id , name ) );
			take( RtmpFactory.sampleAccess( true , true ) );
			
		}
		
	}
	
	/**
	 * Disables playing, send out play failed message
	 */
	
	public void disable ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + name + " StreamPlayer.disable " + id );

		RtmpPacket packet = RtmpFactory.playFailed( id , name );
		packet.flvChannel = flvChannel;
		
		controller.client.addOutgoingPacket( packet );
		
	}
	
	/**
	 * Subscribes player to router 
	 */
	
	public void register ( )
	{
		
		// System.out.println( System.currentTimeMillis() + " " + name + " StreamPlayer.register " );
		
		Server.registerPlayer( this );
		Server.connectPlayer( this , name );
		
		registered = true;

	}
	
	/**
	 * Subscribes player to router 
	 */
	
	public void unregister ( )
	{
		
		// System.out.println( System.currentTimeMillis() + " " + name + " StreamPlayer.unregister " );
		
		Server.unregisterPlayer( this );
		Server.disconnectPlayer( this , name );

		registered = false;

	}

	/**
	 * Enables or disables audio and video sending, bufferlength
	 * @param stateX state
	 */
	
	public void enableAudio ( boolean stateX ) { sendAudio = stateX; }
	public void enableVideo ( boolean stateX ) { sendVideo = stateX; }
	
	/**
	 * Adds new rtmp packet
	 * @param packetX rtmp pacekt
	 */
	
	public void take ( RtmpPacket packetX )
	{
		
		if ( !paused )
		{

			// System.out.println( System.currentTimeMillis() + " " + name + " StreamPlayer.takePacket " + packetX );

			synchronized ( buffer )
			{
				
				switch ( packetX.bodyType )
				{
				
					// ping messages - buffer set, buffer reset, playhead reset
				
					case 0x01 :
					case 0x04 :
						
						buffer.add( packetX );
						break;
						
					// audio data
				
					case 0x08 :
						
						if ( !controller.dropAudio && sendAudio )
						{
							
							// clone packet
							// set outgoing flvChannel
							// set incoming stamp for buffering
							// set audio state
												
							packet = new RtmpPacket( packetX );
							packet.flvChannel = flvChannel;
							packet.rtmpChannel = audioChannel;

							buffer.add( packet );
							
						}
						
						break;
						
					// video data
						
					case 0x09 :
												
						if ( sendVideo )
						{
							
							if ( packetX.body.length != 0 ) 
							{
							
								frameType = packetX.body[0 ] & 0xF0;
								
								if ( frameType == 0x30 && controller.dropDisposables ) return;
								if ( frameType == 0x20 && controller.dropInterframes ) return;
								if ( frameType == 0x10 ) { hasFrame = true;	if ( controller.dropKeyframes ) return;	}
								
							}
							
							if ( hasFrame )
							{
								
								// clone packet
								// set outgoing flvChannel
								// set incoming stamp for buffering
								// set video state
								
								packet = new RtmpPacket( packetX );
								packet.flvChannel = flvChannel;
								packet.rtmpChannel = videoChannel;
								
								buffer.add( packet );
							
							}
						
						}
						
						break;
						
					// stream metadata
						
					case 0x12 :
						
					// status messages	
					
					case 0x14 :
						

						packet = new RtmpPacket( packetX );
						packet.flvChannel = flvChannel;

						buffer.add( packet );
						
						break;						
					
				}
					
			}
			
		}
		
	}
	
	/**
	 * Places received packets into given arraylist
	 * @param inputX arraylist to put packets in
	 */
	
	public void givePackets ( ArrayList < RtmpPacket > inputX )
	{
		
		// System.out.println( System.currentTimeMillis() + " " + name + " StreamPlayer.givePackets " + buffer.size( ) );
		
		if ( !paused )
		{
			
			synchronized ( buffer )
			{
				
				inputX.addAll( buffer );
				buffer.clear( );
	
			}
		
		}

	}

	/**
	 * Returns played streams name
	 **/
	
	public int getId ( )
	{
		
		return id;
		
	}
	
	public long getClientId ( )
	{
		
		return controller.client.id;
		
	}
	
	public String getName ( )
	{
		
		return name;
		
	}
	
	public int getFlvChannel ( )
	{
		
		return flvChannel;
		
	}
	
	public int getAudioChannel ( )
	{
		
		return audioChannel;
		
	}
	
	public int getVideoChannel ( )
	{
		
		return videoChannel;
		
	}

}
