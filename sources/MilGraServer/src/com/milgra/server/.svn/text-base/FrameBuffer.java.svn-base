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
  
  FrameBuffer class
  
  @mail milgra@milgra.com
  @author Milan Toth
  @version 20080315
  
   	Tasks of Streambuffer
 		- store frame properties of a specific flv
 		- buffer frames based on speed
 		- return frames by request
  
**/

import java.io.IOException;
import java.io.RandomAccessFile;

import java.util.HashMap;
import java.util.ArrayList;

import com.milgra.server.Server;
import com.milgra.server.OProcess;


public class FrameBuffer extends OProcess
{
	
	// last - last timestamp, for duration count
	// type - type of buffer content, 0x08 audio, 0x09 video
	// frames - frame count in buffer
	
	public int last;
	public int type;
	public int frames;
	
	// lastStamp - timestamp of last sent frame
	// lastPosition - position of last sent frame
	
	public int lastStamp;
	public int lastPosition;
		
	// bufferSize - size of buffer 
	// bufferLimit - buffer limits timestamp
	
	public double bufferSize;
	public double bufferLimit;
	
	// active - buffer is active
	// forward - direction of buffering
	// finished - buffer reached end
	// buffering - buffering
	
	public boolean active;
	public boolean forward;
	public boolean finished;
	public boolean buffering;
	
	// positions - container for frame file positions
	// durations - for frame durations
	// bodysizes - for frame sizes
	// flvstamps - for frame stamps
		
	public ArrayList < Long    > positions;
	public ArrayList < Integer > durations;
	public ArrayList < Integer > bodysizes;
	public ArrayList < Integer > flvstamps;
	
	// file - the streams file
	// pool - buffer pool
	
	public RandomAccessFile file;
	public HashMap < Integer , RtmpPacket > pool;
	
	/**
	 * FrameBuffer constructor
	 * @param typeX type of frames stored in this buffer
	 * @param fileX file containing the stream
	 **/	

	public FrameBuffer ( int typeX , RandomAccessFile fileX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " FrameBuffer.construct " + typeX + " " + fileX );

		// create

		pool = new HashMap < Integer , RtmpPacket > ( );
		
		positions = new ArrayList < Long    > ( );
		bodysizes = new ArrayList < Integer > ( );
		flvstamps = new ArrayList < Integer > ( );
		durations = new ArrayList < Integer > ( );
		
		// set

		type = typeX;
		file = fileX;
		
		frames = 0;
		forward = true;
		bufferSize = Library.NSBUFFER;
		bufferLimit = 0;
		
		lastStamp = 0;
		lastPosition = 0;
		
	}
	
	/**
	 * FrameBuffer destructor
	 **/
	
	public void destruct ( )
	{

		// System.out.println( System.currentTimeMillis( ) + " FrameBuffer.destruct " + type + " " + file );

		pool = null;
		
		positions = null;
		bodysizes = null;
		flvstamps = null;
		durations = null;
		
	}
	
	/**
	 * Closes buffer, unregisters process
	 **/
	
	public void close ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + type + " FrameBuffer.close " );

		Server.unregisterProcess( this , "io" );
		destruct( );
		
	}
	
	/**
	 * Registers a frame related to the stream
	 * @param positionX frame position in file
	 * @param stampX frames timestamp
	 * @param sizeX frames body size
	 **/
	
	public void registerFrame ( long positionX , int stampX , int sizeX )
	{

		// System.out.println( System.currentTimeMillis( ) + " " + type + " FrameBuffer.registerFrame " + positionX + " " + stampX + " " + sizeX );

		bodysizes.add( sizeX );
		flvstamps.add( stampX );
		positions.add( positionX );
		durations.add( stampX - last );
		
		frames++;
		active = true;
		
		if ( stampX != last ) last = stampX;
		
	}	
	
	/**
	 * Loads a specific frame
	 * @param indexX frame index
	 **/
	
	public void loadFrame ( int indexX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + type + " FrameBuffer.loadFrame " + indexX );
		
		try
		{
			
			// create packet and body container
				
			RtmpPacket framePacket = new RtmpPacket( );
			framePacket.body = new byte[ bodysizes.get( indexX ) ];
			
			// read up body
				
			file.seek( positions.get( indexX ) );
			file.read( framePacket.body );
			
			framePacket.bodyType = type;
			framePacket.flvStamp = durations.get( indexX );
			
			pool.put( indexX , framePacket );

		} 
		catch ( IOException exception ) { exception.printStackTrace( ); }
		
	}
	
	/**
	 * Starts buffer checker process
	 */
	
	public void start ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + type + " FrameBuffer.start " );		
		Server.registerProcess( this , "io" );
		
	}
	
	/**
	 * Stops buffer checker process
	 */
	
	public void stop ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + type + " FrameBuffer.stop " );		
		Server.unregisterProcess( this , "io" );
		
	}

	/**
	 * Sets speed multiplier, needed for forward and buffer length
	 * @param multiplierX
	 **/
	
	public void speed ( double multiplierX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + type + " FrameBuffer.setSpeed " + multiplierX );
		
		forward = multiplierX < 0 ? false : true;
		bufferSize = multiplierX * Library.NSBUFFER;
		
	}
	
	/**
	 * Seeks position to wanted timestamp from actual position
	 * @param stampX timestamp
	 **/
	
	public void seek ( double stampX )
	{

		// System.out.println( System.currentTimeMillis( ) + " " + type + " FrameBuffer.seek " + stampX + " " + lastPosition );
		
		if ( active )
		{
			
			// checking endpoints

			int timeStamp;
			
			if ( lastPosition == -1 ) lastPosition += 1;
			if ( lastPosition == frames ) lastPosition -= 1;
			
			if ( stampX < lastStamp )
			{				
				
				do
				{
					
					timeStamp = flvstamps.get( lastPosition );
					if ( stampX < timeStamp ) -- lastPosition;
					
				}
				while ( stampX < timeStamp && lastPosition > 0 );
				
			}
			else
			{
				
				do
				{
					
					timeStamp = flvstamps.get( lastPosition );
					if ( stampX > timeStamp ) ++ lastPosition;
					
				}
				while ( stampX > timeStamp && lastPosition < frames );			
				
			}
			
			if ( lastPosition == frames ) lastPosition -= 1;
			if ( lastPosition == -1 ) lastPosition += 1;

			// set limit, clear buffer
			
			bufferLimit = flvstamps.get( lastPosition );
			finished = false;
			pool.clear( );	
			
		}		
				
	}
	
	/**
	 * Buffers a specific number of frames from actual position.	 * 
	 * @param limitX last stamp to buffer
	 **/
	
	public void bufferFrames ( double limitX )
	{
	
		// System.out.println( System.currentTimeMillis( ) + " " + type + " FrameBuffer.bufferFrames " + limitX );
		
		if ( !active ) return;
		
		if ( lastPosition == frames ) lastPosition -= 1;
		if ( lastPosition == -1 ) lastPosition += 1;
		
		int flvstamp = 0;
		int position = lastPosition;
		
		if ( forward )
		{
			
			do
			{
				
				flvstamp = flvstamps.get( position );
				
				if ( flvstamp <= limitX ) 
					if ( !pool.containsKey( position ) ) loadFrame( position );

				position ++;

			}
			while ( flvstamp <= limitX && position < frames );
			
		}
		else
		{
			
			do
			{
				
				flvstamp = flvstamps.get( position );
								
				if ( flvstamp >= limitX ) 
					if ( !pool.containsKey( position ) ) loadFrame( position );

				position --;

			}
			while ( flvstamp >= limitX && position > -1 );
			
		}	
		
	}
	
	/**
	 * Calculates frame index related to a timestamp
	 * @param stampX timestamp
	 * @return index of frame related to timestamp
	 */
	
	public void giveFrames ( double stampX , ArrayList < RtmpPacket > framesX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + type + " FrameBuffer.giveFrames " + stampX + " finished: " + finished );
		
		if ( active && !finished )
		{
			
			if ( forward )
			{
				
				// getting frames from pool
				
				do
				{
					
					lastStamp = flvstamps.get( lastPosition );
					
					if ( lastStamp <= stampX )
					{
						
						RtmpPacket packet = pool.remove( lastPosition++ );
						if ( packet != null ) framesX.add( packet );
						//else System.out.println( "No packet in buffer pool: " + ( lastPosition - 1 ) );
						
					}
					if ( lastPosition == frames ) finished = true;
					
				}
				while ( lastStamp <= stampX && !finished );
				
			}
			else
			{
				
				// getting frames from pool
				
				do
				{

					lastStamp = flvstamps.get( lastPosition );

					if ( lastStamp >= stampX )
					{
						
						RtmpPacket packet = pool.remove( lastPosition-- );
						if ( packet != null ) framesX.add( packet );
						//else System.out.println( "No packet in buffer pool: " + ( lastPosition + 1 ) );
						
					}
					if ( lastPosition == -1 ) finished = true;
					
				}
				while ( lastStamp >= stampX && !finished );
				
			}
			
		}
		
	}
	
	/**
	 * Checkign buffer underrun, if needed, buffer
	 **/
	
	public void step ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + type + " FrameBuffer.step " );

		if ( forward )
		{
			
			if ( lastStamp >= bufferLimit - 1000 )
			{

				bufferLimit = lastStamp + bufferSize;
				bufferFrames( bufferLimit );
				
			}
			
		}				
		else
		{
				
			if ( lastStamp <= bufferLimit + 1000 )
			{

				bufferLimit = lastStamp + bufferSize;
				bufferFrames( bufferLimit );
					
			}
			
		}
		
	}

}
