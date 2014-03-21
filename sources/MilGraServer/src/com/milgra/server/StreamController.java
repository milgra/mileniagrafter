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
  
  StreamController class
  
  @mail milgra@milgra.com
  @author Milan Toth
  @version 20080315
  
   	Tasks of Streamcontroller
 		- control stream creation, deletion
 		- control play, publish request
 		- receive and dispatch stream packets
  
**/

import java.util.HashMap;
import java.util.ArrayList;

import com.milgra.server.api.Stream;
import com.milgra.server.api.Wrapper;
import com.milgra.server.api.WrapperList;
import com.milgra.server.api.StreamEvent;


public class StreamController extends OProcess 
{

	// closed - controller is closed
	
	public boolean closed;
	
	// flvId - flv channel counter for outgoing streams
	// streamId - stream id counter for incoming streams
	// channnelIndex - rtmp channel counter 
	
	public int flvId;
	public int streamId;
	public int channelIndex;
	
	// hasAudio - do we received audio data
	// hasVideo - do we received video data
	
	public boolean hasAudio;
	public boolean hasVideo;
	
	// frame drop control booleans
	
	public boolean dropAudio;
	public boolean dropKeyframes;
	public boolean dropInterframes;
	public boolean dropDisposables;
	
	// controllers
	
	public SocketController socket;
	public ClientController client;
	
	// request waiting lists
	
	public HashMap < Integer , Integer > channelToId;
	public ArrayList < Integer > channels;
	public ArrayList < Integer > idSequence;
	public ArrayList < Stream > createRequests;
	
	// RTMP packet waiting list
	
	public ArrayList < RtmpPacket > incomingList;
	public ArrayList < RtmpPacket > outgoingList;

	// stream containers
	
	// players - standard stream players subscribing to a router
	// routers - stream routers related to our client
	// remotes - stream players pushing stream to a remote client after acception
	// address - stream router addresses to routers to make sorting more simple
	
	public HashMap < Integer , StreamPlayer > players;
	public HashMap < Integer , StreamRouter > routers;
	public HashMap < Integer , StreamPlayer > remotes;
	public HashMap < Integer , StreamRouter > address;
	
	/**
	 * StreamController constructor
	 * @param clientX client controller
	 * @param socketX socket controller
	 */
	
	public StreamController ( ClientController clientX , SocketController socketX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + clientX.id + " StreamController.construct" );
		
		// create
		
		players = new HashMap < Integer , StreamPlayer > ( );
		routers = new HashMap < Integer , StreamRouter > ( );
		remotes = new HashMap < Integer , StreamPlayer > ( );
		address = new HashMap < Integer , StreamRouter > ( );		
		
		channels = new ArrayList < Integer > ( );
		idSequence = new ArrayList < Integer > ( );
		channelToId = new HashMap < Integer , Integer > ( );
		createRequests = new ArrayList < Stream > ( );
		
		// set
		
		// controllers
		
		client = clientX;
		socket = socketX;
		
		// starting channel and id
		
		// flvchannels starting from 01 00 00 00
		// streamids starting from 1
		// channelIndex starts from the first
		
		flvId = 1 << 24;
		streamId = 1;
		channelIndex = 0;

		// frame drop controls
		
		dropAudio = false;
		dropKeyframes = false;
		dropInterframes = false;
		dropDisposables = false;
		
		// packet containers

		incomingList = new ArrayList < RtmpPacket > ( );
		outgoingList = new ArrayList < RtmpPacket > ( );
		
		// start
		
		// fill up rtmp channels 
		
		for ( int index = 4 ; index < 64 ; index++ ) channels.add( index );
		
	}
	
	/**
	 * @return new rtmp channel for a stream
	 **/
	
	public int getChannel ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + clientX.id + " StreamController.getChannel" );
		
		// for better header scattering, streams should vary avaliable rtmp channels

		int channel = channels.remove( channelIndex++ );
		if ( channelIndex == channels.size( ) ) channelIndex = 0;
		
		return channel;
		
	}
	
	/**
	 * Closes StreamController
	 **/
	
	public void close ( )
	{

		// System.out.println( System.currentTimeMillis( ) + " " + client.id + " StreamController.close" );
		
		closed = true;
				
		ArrayList < StreamPlayer > playersCopy = new ArrayList < StreamPlayer > ( players.values( ) );
		ArrayList < StreamRouter > routersCopy = new ArrayList < StreamRouter > ( routers.values( ) );

		for ( StreamPlayer player : playersCopy ) player.close( );
		for ( StreamRouter router : routersCopy ) router.close( );
		
	}
	
	/**
	 * Removes stream player from registers
	 * @param playerX stream player
	 **/
		
	public void removePlayer( StreamPlayer playerX ) 
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + client.id + " StreamController.removePlayer " + playerX );
		
		players.remove( playerX.getId( ) );
		
		channels.add( playerX.getAudioChannel( ) );
		channels.add( playerX.getVideoChannel( ) );
		
	}

	/**
	 * Removes stream router from registers
	 * @param playerX stream router
	 */
		
	public void removeRouter( StreamRouter routerX ) 
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + client.id + " StreamController.removeRouter " + routerX );
		
		routers.remove( routerX.getId( ) );
		address.remove( routerX.getFlvChannel( ) );
		
	}
	
	/**
	 * Creates new stream, passing back a newly generated stream id
	 * @param argumentsX arguments
	 * @param packetX rtmp packet
	 */
	
	public void onStreamCreateRequest( WrapperList argumentsX , RtmpPacket packetX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + client.id + " StreamController.onStreamCreateRequest " + argumentsX.get( 1 ).doubleValue );
		
		int actualId;
		double invokeId;
		
		// get invoke channel of request
		// generate new streamId
		
		invokeId = argumentsX.getDouble( 1 );
		actualId = streamId++;
		
		// store id in the sequence container
		// call result on client
		
		idSequence.add( actualId );
		client.callResult( invokeId , new Wrapper( actualId ) );

	}
	
	/**
	 * Deletes a stream by id, called by custom application or client
	 * @param argumentsX arguments
	 * @param packetX rtmp packet
	 */

	public void onStreamDeleteRequest( WrapperList argumentsX , RtmpPacket packetX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + client.id + " StreamController.onStreamDeleteRequest " + argumentsX );
		
		// get stream id from request
		
		double wantedId = argumentsX.getDouble( 3 );
		
		// delete related stuff
		
		if ( players.containsKey( wantedId ) )
		{
			
			channelToId.remove( players.get( wantedId ).getFlvChannel( ) );
			players.get( wantedId ).close( );
			
		}
		
		if ( routers.containsKey( wantedId ) ) 
		{
			
			channelToId.remove( routers.get( wantedId ).getFlvChannel( ) );
			routers.get( wantedId ).close( );
			
		}
		
	}
	
	/**
	 * Closes a stream
	 * @param argumentsX arguments
	 * @param packetX rtmp packet
	 */
	
	public void onStreamCloseRequest( ArrayList < Wrapper > argumentsX , RtmpPacket packetX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + client.id + " StreamController.onStreamDeleteRequest " + argumentsX );
		
		// creating clone lists, because request only contains the flv channel of closable stream
		
		ArrayList < StreamPlayer > playersCopy = new ArrayList < StreamPlayer > ( players.values( ) );
		ArrayList < StreamRouter > routersCopy = new ArrayList < StreamRouter > ( routers.values( ) );
		
		// searching for streams to close
		
		for ( StreamPlayer player : playersCopy ) if ( player.getFlvChannel( ) == packetX.flvChannel ) 
		{
			
			channelToId.remove( player.getFlvChannel( ) );
			player.close( );
			
		}
		
		for ( StreamRouter router : routersCopy ) if ( router.getFlvChannel( ) == packetX.flvChannel ) 
		{
			
			channelToId.remove( router.getFlvChannel( ) );
			router.close( );
			
		}
		
	}
	
	/**
	 * Stream play request from client
	 * @param argumentsX the arguments
	 * @param packetX rtmp packet
	 */
	
	public void onStreamPlayRequest( WrapperList argumentsX , RtmpPacket packetX )
	{

		// System.out.println( System.currentTimeMillis( ) + " StreamController.onStreamPlayRequest " + argumentsX.getString( 3 ) );

		int id;
		String name = argumentsX.getString( 3 );
		
		// close old player
		
		if ( channelToId.containsKey( packetX.flvChannel ) )
		{
			
			id = channelToId.get( packetX.flvChannel );
			StreamPlayer player = players.get( id );
			if ( player != null ) player.close( );
			
		}
		else
		{
			
			// get id if needed
			
			id = idSequence.remove( 0 );
			
		}

		// store id to channel
		channelToId.put( packetX.flvChannel , id );

		// create new player
		if ( name != null )
		{
			
			int flvChannel = packetX.flvChannel;
			int videoChannel = getChannel( );
			int audioChannel = getChannel( );
			
			StreamPlayer player = new StreamPlayer( id , 
													flvChannel , 
													videoChannel , 
													audioChannel , 
													name , 
													this );

			players.put( id , player );
			
			if ( client.streamListener == null ) player.enable( ); 
			else client.streamListener.onEvent( new StreamEvent( new Stream( player ) , client.client ) );
			
		}
				
	}
	
	/**
	 * Stream publish request from client
	 * @param argumentsX the arguments
	 * @param packetX rtmp packet
	 */
	
	public void onStreamPublishRequest( WrapperList argumentsX , RtmpPacket packetX )
	{

		// System.out.println( System.currentTimeMillis( ) + " StreamController.onStreamPublishRequest " + argumentsX.getString( 3 ) );

		int id;
		String name = argumentsX.getString( 3 );
		
		// close old router
		
		if ( channelToId.containsKey( packetX.flvChannel ) )
		{
			
			id = channelToId.get( packetX.flvChannel );
			StreamRouter router = routers.get( id );
			
			router.close( );
			
		}
		else
		{
			
			// get new id if needed
			
			id = idSequence.remove( 0 );
			
		}

		// store id
		
		channelToId.put( packetX.flvChannel , id );
		
		// create new player
		
		if ( name != null )
		{
		
			int flvChannel = packetX.flvChannel;
			String mode = argumentsX.size( ) > 4 ? argumentsX.getString( 4 ) : "live";
	
			StreamRouter router = new StreamRouter( id , flvChannel , name , mode , this );
			
			routers.put( id , router );
			address.put( flvChannel , router );

			if ( client.streamListener == null ) router.enable( );
			else 
			{
				
				Stream stream = new Stream( router );
				client.streamListener.onEvent( new StreamEvent( stream , client.client ) );
	
			}
		
		}
		
	}

	/**
	 * Remote stream create request from a remote stream instance
	 * @param streamX stream instance
	 */
	
	public void createStream ( Stream streamX )
	{

		// System.out.println( System.currentTimeMillis() + " " + client.id + " StreamController.createStream " + streamX );

		createRequests.add( streamX );
		client.callResponse( "createStream" , new WrapperList( ) , 0 );
		
	}
	
	/**
	 * Deletes a remote stream
	 * @param streamIdX stream id
	 * @param streamX stream instance
	 */
	
	public void deleteStream ( double streamIdX , Stream streamX )
	{

		// System.out.println( System.currentTimeMillis() + " " + client.id + " StreamController.deleteStream " + streamIdX + " " + streamX );

		if ( streamX.remoteType.equals( "play" ) ) stopRemote( ( int ) streamIdX );
		if ( streamX.remoteType.equals( "publish" ) ) unpublishRemote( ( int ) streamIdX );
		
		client.call( "deleteStream" , new Wrapper( streamIdX ) );

	}
	
	/**
	 * Create stream response from remote connection
	 * @param streamIdX stream id
	 */
	
	public void onCreateStream ( double streamIdX )
	{
		
		// System.out.println( System.currentTimeMillis() + " " + client.id + " StreamController.onCreateStream " + streamIdX );

		// getting oldest waiting stream instance
		// setting remote id
		
		Stream stream = createRequests.remove( 0 );
		stream.remoteId = ( int ) streamIdX;
		stream.remoteChannel = flvId--;
		
		// if stream is a remote player, create a related router for incoming stream
		// if stream is a remote publisher, create a player for utgoing stream boradcasting
		
		if ( stream.remoteType.equals( "play" ) ) playRemote( ( int ) streamIdX , stream.remoteChannel , stream.remoteName , stream.name );
		if ( stream.remoteType.equals( "publish" ) ) publishRemote( ( int ) streamIdX , stream.remoteChannel , stream.remoteName , stream.name , stream.mode );
		
	}
	
	/**
	 * Creates a router for incoming stream
	 * @param idX
	 * @param nameX
	 */

	public void playRemote ( int idX , int channelX , String remoteNameX , String localNameX )
	{
		
		// System.out.println( System.currentTimeMillis() + " " + client.id + " StreamController.playRemote " + idX + " " + remoteNameX + " " + localNameX );

		// generate new id and channel
		
		int newId = streamId++;
		
		// create router
		
		StreamRouter router = new StreamRouter( newId , channelX , localNameX , "live" , this );
		
		router.enable( );
		
		// register router
		
		routers.put( newId , router );
		address.put( channelX , router );
		
		// send play request

		client.call( "play" , new WrapperList( new Wrapper( remoteNameX ) ) , channelX );

	}
	
	/**
	 * Stops a remote player
	 * @param idX stream id
	 */
	
	public void stopRemote ( int idX )
	{
		
		// System.out.println( System.currentTimeMillis() + " " + client.id + " StreamController.stopRemote " + idX );
		
		// getting router
		
		StreamRouter router = routers.get( idX );
		
		// unregister
		
		routers.remove( idX );
		address.remove( router.getFlvChannel( ) );
		
		// close here and there
		
		router.close( );
		
	}
	
	/**
	 * Creates a player for outgoing stream
	 * @param idX stream id
	 * @param nameX local name of stream
	 * @param modeX mode of publishing
	 */
	
	public void publishRemote ( int idX , int channelX , String remoteNameX , String localNameX , String modeX )
	{

		// System.out.println( System.currentTimeMillis() + " " + client.id + " StreamController.publishRemote " + idX + " " + remoteNameX + " " + localNameX );

		// generate new id and channel
		
		int newId = streamId++;
		int videoChannel = getChannel( );
		int audioChannel = getChannel( );
		
		// create player
		
		StreamPlayer player = new StreamPlayer( newId , 
												channelX , 
												videoChannel , 
												audioChannel , 
												localNameX , 
												this );
		
		player.enable( );
		
		// register player
		
		remotes.put( idX , player );
		players.put( newId , player );
		
		// send publish request
		
		WrapperList arguments = new WrapperList( );
		arguments.add( remoteNameX );
		arguments.add( modeX );
		
		client.call( "publish" , arguments , channelX );
		
	}
	
	/**
	 * Unpublishes a remote stream
	 * @param idX stream id
	 */
	
	public void unpublishRemote ( int idX )
	{
		
		// System.out.println( System.currentTimeMillis() + " " + client.id + " StreamController.publishRemote " + idX );

		// getting player
		
		StreamPlayer player = remotes.get( idX );
		
		// unregister
		
		remotes.remove( idX );
		players.remove( player.getId( ) );
		
		// close here and there
			
		player.close( );
			
	}

	
	/**
	 * Sets audio sending state
	 * @param argumentsX arguments
	 * @param packetX rtmp packet
	 */
	
	public void onAudioReceiveState ( WrapperList argumentsX , RtmpPacket packetX )
	{
		
		// System.out.println( System.currentTimeMillis() + " " + client.id + " StreamController.onAudioReceiveState " );
		
		boolean state = argumentsX.getBoolean( 3 );
		for ( StreamPlayer player : players.values( ) ) 
			if ( player.getFlvChannel( ) == packetX.flvChannel ) player.enableAudio( state );
		
	}
	
	/**
	 * Sets video sending state
	 * @param argumentsX arguments
	 * @param packetX rtmp packet
	 */	
	
	public void onVideoReceiveState ( WrapperList argumentsX , RtmpPacket packetX )
	{
		
		// System.out.println( System.currentTimeMillis() + " " + client.id + " StreamController.onVideoReceiveState " );
		
		boolean state = argumentsX.getBoolean( 3 );
		for ( StreamPlayer player : players.values( ) ) 
			if ( player.getFlvChannel( ) == packetX.flvChannel ) player.enableVideo( state );		
		
	}
	/**
	 * Seek request from client
	 * @param argumentsX arguments
	 * @param packetX rtmp packet
	 */
	
	public void onStreamSeekRequest( WrapperList argumentsX , RtmpPacket packetX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + client.id + 
		//					"StreamController.onStreamSeekRequest" + packetX.toString( ) + " " + argumentsX.getDouble( 3 ) );
		
		for ( StreamPlayer player : players.values( ) ) 
			if ( player.getFlvChannel( ) == packetX.flvChannel )
				player.reader.setPosition( argumentsX.getDouble( 3 ) );
				
	}
	
	/**
	 * Speed request from client
	 * @param argumentsX arguments
	 * @param packetX rtmp packet
	 */
	
	public void onStreamSpeedRequest ( WrapperList argumentsX , RtmpPacket packetX )
	{
		
		//System.out.println( System.currentTimeMillis( ) + " " + client.id + 
		//					"StreamController.onStreamSpeedRequest" + packetX.toString( ) + " " + argumentsX.getString( 3 )+ " " + argumentsX.getDouble( 4 ) );
	
		for ( StreamPlayer player : players.values( ) ) 
				if ( player.getName( ).equals( argumentsX.getString( 3 ) ) )
					player.reader.setSpeed( new Double( argumentsX.getDouble( 4 ) ) );
		
	}
	
	/**
	 * Pause request from client
	 * @param argumentsX arguments
	 * @param packetX rtmp packet
	 */
	
	public void onStreamPauseRequest ( WrapperList argumentsX , RtmpPacket packetX )
	{
		
		//System.out.println( System.currentTimeMillis( ) + " " + client.id + 
		//					"StreamController.onStreamSpeedRequest" + packetX.toString( ) + " " + argumentsX.getDouble( 3 ) );
	
		for ( StreamPlayer player : players.values( ) ) 
				if ( player.getFlvChannel( ) == packetX.flvChannel )
					player.reader.pause( );
		
	}

	/**
	 * Sets the buffer length for the routers on the specified channel
	 * @param idX stream id
	 * @param bufferLengthX buffer size
	 */
	
	public void setBufferLength ( double streamIdX , int bufferLengthX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + client.id + " StreamController.setBufferLength " + streamIdX + " " + bufferLengthX );
		
		//if ( routers.containsKey( streamIdX ) )
		//	routers.get( streamIdX ).setBufferLength( bufferLengthX );
		
	}
	
	/**
	 * Steps one in packet multiplexing
	 */
	
	public void step ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + client.id + " StreamController.step ogList: " + outgoingList.size( ) );
		
		// get incoming flv packets
		
		socket.giveFlvPackets( incomingList );
		
		// sort them
						
		for ( RtmpPacket packet : incomingList )
			if ( address.containsKey( packet.flvChannel ) ) 
				 address.get( packet.flvChannel ).take( packet );
		
		// get sorted packets

		for ( StreamPlayer player : players.values( ) ) player.givePackets( outgoingList );
		
		// give available packets

		socket.takePackets( outgoingList );
		incomingList.clear( );
		
	}
	
	/**
	 * Sets dropping to a higher level
	 */
	
	public void addDropping ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + client.id + " StreamController.addDropping" );

		if ( !dropDisposables ) dropDisposables = true; else
		if ( !dropInterframes ) dropInterframes = true; else
		if ( !dropKeyframes ) dropKeyframes = true; else
		if ( !dropAudio ) dropAudio = true;
		
	}
	
	/**
	 * Sets dropping to a lower level
	 */
	
	public void removeDropping ( )
	{

		//System.out.println( System.currentTimeMillis( ) + " " + client.id + " StreamController.removeDropping" );

		if ( dropAudio ) dropAudio = false; else
		if ( dropKeyframes ) dropKeyframes = false; else
		if ( dropInterframes ) dropInterframes = false; else
		if ( dropDisposables ) dropDisposables = false;
		
	}
	
	/**
	 * Returns played stream list
	 * @return hashmap containing players
	 */
	
	public HashMap < Integer , Stream > getPlayers ( ) 
	{
		
		// System.out.println( System.currentTimeMillis() + " " + client.id + " StreamController.getPlayedStreams" );
		
		HashMap < Integer , Stream > result = new HashMap < Integer , Stream > ( );
		//for ( Stream player : players.values( ) ) result.put( player.id , player );
		
		return result;
		
	}
	
	/**
	 * Returns published stream list
	 * @return hashmap containing routers
	 */
	
	public HashMap < Integer , Stream > getRouters ( ) 
	{
		
		//System.out.println( System.currentTimeMillis() + " " + client.id + " StreamController.getPublishedStreams " );

		HashMap < Integer , Stream > result = new HashMap < Integer , Stream > ( );
		//for ( Stream router : routers.values( ) ) result.put( router.id , router );
		
		return result;
		
	}

}
