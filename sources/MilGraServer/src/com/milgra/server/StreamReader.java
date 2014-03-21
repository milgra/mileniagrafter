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

	StreamReader class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080315

**/

import com.milgra.server.encoder.RtmpFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/*
 
  	Tasks of Streamreader
  		- Analyze flv stream, add frame info to buffers
  		- Set position on request
  		- Set speed on request
  		- Dispatch frames on steps 
 
 */

public class StreamReader extends OProcess
{
	
	// name - stream/file name
	// subscriber - player/router
	// streamFile - streamfile

	public String name;
	public OStream subscriber;
	public RandomAccessFile streamFile;
	
	// measures for stepping

	public long parttime;
	public long lasttime;
	
	// seektime - last seek time
	// multiply - speed multiplier
	// position - playhead position
	// duration - stream duration
	
	public double seektime;
	public double multiply;
	public double position;
	public double duration;
	
	public double newPosition;
	public double newMultiply;
		
	// close - reader is closing
	// paused - paused

	public boolean close;
	public boolean paused;
	public boolean change;
	
	// frame buffers

	public FrameBuffer audioBuffer;
	public FrameBuffer videoBuffer;
	
	// packet containers
	
	public ArrayList < RtmpPacket > audioPackets;
	public ArrayList < RtmpPacket > videoPackets;
	
	/**
	 * Steam Reader constructor
	 * @param fileNameX
	 * @param subscriberX
	 **/		
	
	public StreamReader ( String fileNameX , OStream subscriberX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " StreamReader.construct " + fileNameX + " " + subscriberX );

		// create

		try
		{			

			streamFile = new RandomAccessFile( new File( Library.STREAMDIR , fileNameX ) , "r" );

		}
		catch ( IOException exception ) { exception.printStackTrace( ); }
			
		audioBuffer = new FrameBuffer( 0x08 , streamFile );
		videoBuffer = new FrameBuffer( 0x09 , streamFile );
			
		audioPackets = new ArrayList < RtmpPacket > ( );
		videoPackets = new ArrayList < RtmpPacket > ( );
			
		// set
			
		name = fileNameX;
		subscriber = subscriberX;
		
		parttime = 0;
		lasttime = 0;

		seektime = 0;
		position = 0;
		duration = 0;
		multiply = 1;
			
		// start
		
		analyze( );
		start( );

	}
	
	/**
	 * StreamReader destructor
	 **/
		
	public void destruct ( ) 
	{
		
		// System.out.println( System.currentTimeMillis( ) + " StreamReader.destruct" );

		try
		{
			
			// close file
			
			streamFile.close( );
			
			// close buffers
			
			audioBuffer.close( );
			videoBuffer.close( );
			
			Server.unregisterProcess( this , "stream" );
			
		}
		catch ( IOException exception ) { exception.printStackTrace( ); }
		
	}
	
	/**
	 * StreamReader.close
	 **/
	
	public void close ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " StreamReader.close" );
		
		close = true;
		
	}
	
	/**
	 * Analyzes stream, inits buffers
	 **/
	
	public void analyze ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " StreamReader.createIndex " );
		
		try
		{
			
			int bodyType;
			int bodySize;
			int flvStamp;

			// skip starting "FLV" and type
			
			long position = 9;
			
			do
			{
				
				// skin previous size
												
				position += 4;
				streamFile.seek( position );
				
				// reading first chunk and flv type
				
				bodyType = streamFile.read( );
				
				// if not end of file
				
				if ( bodyType != -1 )
				{
					
					// read size and timestamp
					
					bodySize = streamFile.read( ) << 16 | streamFile.read( ) << 8 | streamFile.read( );
					flvStamp = streamFile.read( ) << 16 | streamFile.read( ) << 8 | streamFile.read( );

					// skip size, stamp and streamid
					
					position += 11;
					
					// fill up buffer
					
					if ( bodyType == 0x08 )	audioBuffer.registerFrame( position , flvStamp , bodySize);
					if ( bodyType == 0x09 )	videoBuffer.registerFrame( position , flvStamp , bodySize);
					
					// skip body
					
					position += bodySize;
					duration = flvStamp;
				
				}
				
			}
			while ( bodyType != -1 );
			
		}
		catch ( IOException exception ) { exception.printStackTrace( ); }

	}
	
	/**
	 * Reader step
	 **/
	
	public void step ( )
	{	
		
		// System.out.println( System.currentTimeMillis( ) + " StreamReader.step " + position );
		
		if ( change )
		{
			
			multiply = newMultiply;
			position = newPosition;
			seektime = newPosition;
						
			audioBuffer.speed( multiply );
			videoBuffer.speed( multiply );

			audioBuffer.seek( position );
			videoBuffer.seek( position );
			
			audioBuffer.step( );
			videoBuffer.step( );

			change = false;
			sendSeek( );
			
		}
		
		// calculate elapsed time
		
		parttime = System.currentTimeMillis( ) - lasttime;
		lasttime = System.currentTimeMillis( );
		position = position + parttime * multiply;

		if ( position <= duration && position >= 0 )
		{
			
			// get packets
			
			audioBuffer.giveFrames( position , audioPackets );
			videoBuffer.giveFrames( position , videoPackets );
			
			for ( RtmpPacket packet : audioPackets )
			{

					packet.flvStamp /= multiply;
					subscriber.take( packet );
					if ( Math.abs( multiply ) > 1 ) break; 
						
			}				
				
			for ( RtmpPacket packet : videoPackets )
			{
					
				if ( seektime != 0 ) 
				{ 
				
					packet.first = true;
					packet.flvStamp = ( int ) seektime;
					
					// set it to keyframe
					
					packet.body[ 0 ] = ( byte ) ( packet.body[ 0 ] & 0x0F );
					packet.body[ 0 ] = ( byte ) ( packet.body[ 0 ] | 0x10 );
					
					seektime = 0; 		
					subscriber.take( packet );

				} 
				else 
				{

					packet.flvStamp /= multiply;
					subscriber.take( packet );
				
					if ( Math.abs( multiply ) > 1 ) break; 

				}

			}
				
			audioPackets.clear( );
			videoPackets.clear( );
			
		}
		else stop( );

		if ( close ) destruct( );

	}

	/**
	 * Starts playing
	 **/
	
	public void start ( )
	{

		// System.out.println( System.currentTimeMillis( ) + " StreamReader.start" );

		lasttime = System.currentTimeMillis( );

		audioBuffer.step( );
		videoBuffer.step( );

		audioBuffer.start( );
		videoBuffer.start( );
		
		sendStart( );

		Server.registerProcess( this , "stream" );

	}
	
	/**
	 * Stops playing
	 **/
	
	public void stop ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " StreamReader.stop" );

		audioBuffer.stop( );
		videoBuffer.stop( );
		
		sendStop( );

		Server.unregisterProcess( this , "stream" );

	}
	
	/**
	 * Pauses reading
	 */
	
	public void pause ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " StreamReader.pause" );
		
		if ( paused ) sendUnpause( );
		if ( paused ) Server.registerProcess( this , "stream" );
		else Server.unregisterProcess( this , "stream" );
		
		paused = !paused;
		
	}	
	
	/**
	 * Sets reading speed
	 * @param multiplierX speed multiplier
	 **/
	
	public void setSpeed ( double multiplyX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " StreamReader.setSpeed " + multiplyX );
		
		if ( multiplyX >  5 ) multiplyX =  5; else
		if ( multiplyX < -5 ) multiplyX = -5; else
		if ( multiplyX == 0 ) multiplyX =  1;

		newMultiply = multiplyX;
		newPosition = position;
		
		change = true;
		
	}
	
	/**
	 * Sets playhead position
	 * @param positionX
	 */
	
	public void setPosition ( double positionX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " StreamReader.setPosition " + positionX );

		if ( positionX < 0 		  ) positionX = 0; else
		if ( positionX > duration ) positionX = duration;
		
		newMultiply = multiply;
		newPosition = positionX;
		
		change = true;
		
	}
	
	/**
	 * Sends start related rtmp messages
	 */
	
	public void sendStart ( )
	{

		// System.out.println( System.currentTimeMillis( ) + " StreamReader.sendStart " );

		// reset stream
		subscriber.take( RtmpFactory.streamControl( 4 , subscriber.getId( ) ) );

		// reset playhead
		subscriber.take( RtmpFactory.streamControl( 0 , subscriber.getId( ) ) );
		
		// reset status
		subscriber.take( RtmpFactory.playReset( subscriber.getClientId( ) , name ) );
		
		// start status
		subscriber.take( RtmpFactory.playStart( subscriber.getClientId( ) , name ) );

		// sample access
		subscriber.take( RtmpFactory.sampleAccess( true , true ) );

		// data start for playhead
		subscriber.take( RtmpFactory.dataStart( ) );

		// duration
		subscriber.take( RtmpFactory.streamDuration( duration / Math.abs( multiply ) ) );
		
	}
	
	/**
	 * Send stop related rtmp message
	 */
	
	public void sendStop ( )
	{

		// System.out.println( System.currentTimeMillis( ) + " StreamReader.sendStop " );

		subscriber.take( RtmpFactory.playStop( subscriber.getClientId( ) , name ));

	}
	
	/**
	 * Sends unpause related messages
	 */
	
	public void sendUnpause ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " StreamReader.sendUnpause " );

		subscriber.take( RtmpFactory.playStart( subscriber.getClientId( ) , name ) );
		subscriber.take( RtmpFactory.sampleAccess( true , true ) );
		subscriber.take( RtmpFactory.dataStart( ) );
		
	}
	
	/**
	 * Sends seek related rtmp messages
	 */
	
	public void sendSeek ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " StreamReader.sendSeek " );
		
		// clear buffer
		subscriber.take( RtmpFactory.streamControl( 1 , subscriber.getId( ) ) );

		// chunk size
		subscriber.take( RtmpFactory.chunk( 4096 ) );

		// reset stream
		subscriber.take( RtmpFactory.streamControl( 4 , subscriber.getId( ) ) );

		// reset playhead
		subscriber.take( RtmpFactory.streamControl( 0 , subscriber.getId( ) ) );
		
		// seek notify
		subscriber.take( RtmpFactory.seekNotify( ( int ) position , subscriber.getId( ) , subscriber.getClientId( ) , name ) );
		
		// start notify
		subscriber.take( RtmpFactory.playStart( subscriber.getId( ) , name ) );
		
		// sample access before data
		subscriber.take( RtmpFactory.sampleAccess( true , true ) );
		
		// data start for playhead
		subscriber.take( RtmpFactory.dataStart( ) );
		
		// empty audio frame for playhead sync
		subscriber.take( RtmpFactory.emptyFrame( 0x08 , ( int ) position ) );
		
	}
	
	/**
	 * Returns reading speed
	 * @return speed multiplier
	 **/
	
	public double getSpeed ( )
	{

		// System.out.println( System.currentTimeMillis( ) + " StreamReader.getSpeed" );

		return multiply;
		
	}
	
	/**
	 * Returns playhead position in seconds
	 * @return
	 */
	
	public double getPosition ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " StreamReader.getPosition " );
		
		return position / 1000 * multiply;
		
	}
	
	/**
	 * Returns duration
	 * @return duration
	 **/
	
	public double getDuration ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " StreamReader.duration" );
		
		return duration / 1000;
		
	}

	// non used interface functions
	
	public void subscribe ( ) { }
	public void unsubscribe ( ) { }

	
}
