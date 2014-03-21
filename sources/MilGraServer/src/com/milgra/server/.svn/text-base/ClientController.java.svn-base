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

	ClientController class
	
	@mail milgra@milgra.hu
	@author Milan Toth
	@version 20080315

	Tasks of Clientcontroller
	
		- Client initialization
		- Invoke and RTMP message flow control

**/

import com.milgra.server.api.Client;
import com.milgra.server.api.Stream;
import com.milgra.server.api.Wrapper;
import com.milgra.server.api.WrapperMap;
import com.milgra.server.api.WrapperList;
import com.milgra.server.api.InvokeEvent;
import com.milgra.server.api.StatusEvent;
import com.milgra.server.api.IApplication;
import com.milgra.server.api.EventListener;

import com.milgra.server.encoder.Encoder;
import com.milgra.server.encoder.RtmpFactory;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import java.util.HashMap;
import java.util.ArrayList;


public class ClientController extends OProcess
{
	
	// static id to make client id creation atomic and global
	
	public static long globalId = 0;			

	// client - related client api object
	// application - related custom application instance
	
	public Client client;
	public IApplication application;

	// closed - controller is closed
	// accepted - controller is accepted
	
	public boolean closed;
	public boolean accepted;
	
	// counter - rtmo update counter
	// id - unique id
	// mode - client mode, active or passive
	
	public int counter;
	public int stepRound;
	public long id;
	public long currentTime;
	public String mode;
	
	public String ip;
	public String agent;
	public String referrer;

	public long ping;
	public long bytesIn = 0;
	public long bytesOut = 0;
	
	public double bandIn = 0;
	public double bandOut = 0;

	// readStepClient - how much bytes to receive before next read message
	// readStepServer - how much bytes to send before next read message from client

	public int readStepClient;
	public int readStepServer;						
	
	// lastRead - last read bytes
	// lastBand - last bandwidth
	// lastPing - last ping send
	// lastUpdate - last update message
	// lastPingRead - last ping received
	
	public long lastRead;
	public long lastBand;
	public long lastPing;						
	public long lastPingRead;

	// lastBytesIn - last received amount for bw calculus
	// lastBytesOut - last sent amout for bw calculus

	public long lastBytesIn;
	public long lastBytesOut;					
	
	// event listeners
	
	public EventListener streamListener;
	public EventListener invokeListener;
	public EventListener statusListener;
	
	// controller
	
	public SocketController socketController;
	public StreamController streamController;
	
	// packet containers
	
	public ArrayList < RtmpPacket > incomingList;
	public ArrayList < RtmpPacket > outgoingList;
	
	// container for incoming invoke channels and ids

	public HashMap < Double , String > incomingInvokes;
	public HashMap < Double , String > outgoingInvokes;
	
	/**
	 * ClientController constructor
	 */
	
	public ClientController (  )
	{
		
		// System.out.println( System.currentTimeMillis() + " ClientController.setApplication" );
		
		// create
		
		outgoingList = new ArrayList < RtmpPacket > ( );
		incomingList = new ArrayList < RtmpPacket > ( );

		incomingInvokes = new HashMap < Double , String > ( );
		outgoingInvokes = new HashMap < Double , String > ( );

		socketController = new SocketController( this );
		streamController = new StreamController( this , socketController );

		// set

		// id - generate unique identifier for client
		// mode - default mode is passive
		// counter - update time counter
		// closed - controller not closed by default
		// accepted - controller is not accepted yet
		
		id = globalId++;							
		mode = "passive";				
		counter = 0;
		stepRound = Math.round( 1000 / Library.STEPTIME );
		closed = false;					
		accepted = false;				
		
		// read stepping is 250000 by default
		
		readStepClient = 250000;					
		readStepServer = 250000;				
		
		// init byte counters
		
		lastRead = 0;
		lastBytesIn = 0;
		lastBytesOut = 0;
		
		// start
				
		// invoke channels must be over 0
			
		incomingInvokes.put( ( double ) 0 , null );	

	}
	
	/**
	 * Sets host application for an active client
	 * @param customX
	 */
	
	public void setApplication ( IApplication customX )
	{
		
		// System.out.println( System.currentTimeMillis() + " " + id + " ClientController.setApplication" );
		
		// mode swithces to active
		
		mode = "active";							
		
		// store host application
		
		application = customX;						
		
		// pair client with application
		
		Server.registerClient( customX , this );	
			
	}
	
	/**
	 * Connects controller to an opened socket passed by ServerSocketConnector
	 * @param socketX
	 */
	
	public void connect ( SocketChannel channelX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + id + " ClientController.connect " +	channelX + " " + closed);
	
		if ( !closed )
		{
			
			// connecting socketcontroller to our socketchannel
		
			socketController.connect( channelX , mode );				
			
			// registering processes
			
			Server.registerProcess( this , "client" );					
			Server.registerProcess( socketController , "socket" );
			Server.registerProcess( streamController , "stream" );
			
		}
		
	}
	
	/**
	 * Connects controller to a remote server with a simple argument
	 * @param urlX remote server's url
	 * @param argumentsX connection arguments
	 */
	
	public void connect ( String urlX , Wrapper argumentX ) 
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + id + " ClientController.connect " + urlX );
		
		connect( urlX , new WrapperList( argumentX ) ); 
	
	}

	/**
	 * Connects controller to a remote server with wrapperlist argument
	 * @param urlX remote server's url
	 * @param argumentsX connection arguments
	 */
	
	public void connect ( String urlX , WrapperList argumentsX )
	{
	
		// System.out.println( System.currentTimeMillis( ) + " " + id + " ClientController.connect " + urlX );
			
		if ( !closed )
		{
			
			// url -  part after rtmp://
			// host - host address
			// appid - application id
			
			String url = urlX.substring( 7 );
			String host = url.split( "/" )[0 ];
			String appid = url.split( "/" )[1 ];				

			// invokeChannel - new inckeChannel
			// info - invoke info message
			// message - invoke rtmp packet
			
			double invokeChannel = incomingInvokes.size( );		
			WrapperMap info = new WrapperMap( Library.CONNECTKEYS , Library.MACPLAYERARR );
			RtmpPacket message = new RtmpPacket( );
			WrapperList arguments = new WrapperList( );
			
			// store application and tcUrl
			
			info.put( "app" , appid );
			info.put( "tcUrl" , urlX );
			
			// message creation, no null needed after invokechannel at connect

			arguments.add( new Wrapper( "connect" ) );
			arguments.add( new Wrapper( invokeChannel ) );
			arguments.add( new Wrapper( info ) );
			arguments.addAll( argumentsX );
			
			// rtmp message
			
			message.bodyType = 0x14;
			message.rtmpChannel = 0x03;
			message.body = Encoder.encode( arguments );
			
			// put packet on outgoing list 
			
			addOutgoingPacket( message );
			
			// invokeChannel 1 is reserved for connect
			
			incomingInvokes.put( invokeChannel , "connect" );
			
			// start connecting
			
			Server.socketConnector.connect( host , this );
		
		}
		
	}
		
	/**
	 * Channel connection failed
	 */
	
	public void connectFailed ( )
	{
		
		// System.out.println( System.currentTimeMillis() + " " + id + " ClientController.connectFailed " );
		
		// status info
		
		WrapperMap info = new WrapperMap( Library.STATUSKEYS , Library.FAILUREARR );
		
		// dispatch status event
		
		if ( statusListener != null ) statusListener.onEvent( new StatusEvent( info , client ) );
		
		// close controller
		
		close( );
		
	}
	
	/**
	 * Closes clientcontroller, cleanup
	 */
	
	public synchronized void close ( )
	{
		
		// System.out.println( System.currentTimeMillis() + " " + id + " ClientController.close " );
		
		// we are closed, no more invokes allowed
		
		closed = true;														
		
		// if streamcontroller isn't closed
		// if close isn't triggered by socketcontroller
		
		if ( !streamController.closed ) streamController.close( );			
		if ( !socketController.closed ) socketController.close( "detach" );	
		
		// if we had an application
		
		if ( application != null )
		{
			
			// if we were passive, telling application that we left
			// if we were active, dispatch event to status listener
			
			if ( mode.equals( "passive" ) ) dispatchLeave( );						
			if ( mode.equals( "active" ) ) dispatchClosure( );			
			
		}

		// unpair client with application
		
		Server.unregisterClient( application , this );						

		// unregister processes
		
		Server.unregisterProcess( this , "client" );						
		Server.unregisterProcess( socketController , "socket" );
		Server.unregisterProcess( streamController , "stream" );
	
	}
	
	/**
	 * Dispathes leave event to paired application
	 */
	
	public void dispatchLeave ( )
	{
		
		// System.out.println( System.currentTimeMillis() + " " + id + " ClientController.dispatchLeave " );
		
		// Syncing is needed, multiple clients from multiple threads can disconnect at the same time
		
		synchronized ( application ) 
		{ 
			
			// notify application
			
			application.onLeave( client );	
			
		} 
		
	}
	
	/**
	 * Dispatches closure vent to status listeners
	 */
	
	public void dispatchClosure ( )
	{
		
		// System.out.println( System.currentTimeMillis() + " " + id + " ClientController.dispatchClosure " );
		
		// create status object
		
		WrapperMap info = new WrapperMap( Library.STATUSKEYS , Library.CLOSUREARR );
		
		// if listener exist dispatch 
		
		if ( statusListener != null ) statusListener.onEvent( new StatusEvent( info , client ) );
	
	}
	/**
	 * Synchronized packet pushing, multiple client threads can trigger this function
	 * @param packetX RtmpPacket
	 */
	
	public void addOutgoingPacket ( RtmpPacket packetX ) 
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + id + " ClientController.addOutgoingPacket " + packetX );
		
		synchronized ( outgoingList ) 
		{ 
			
			outgoingList.add( packetX );
			
		}
		
	}

	/**
	 * Execution step
	 * syncing is needed from previous function
	 **/
	
	public void step ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + id + " ClientController.step " + outgoingList.size() );
		
		synchronized ( outgoingList )
		{ 
			
			// packets exchange
				
			socketController.takePackets( outgoingList ); 
			socketController.giveDataPackets( incomingList );

			try 
			{
				
				// decode packets
				
				for ( RtmpPacket packet : incomingList ) receivePacket( packet );	
				
			}
			catch ( Exception exception ) 
			{
				
				// notify user on standard output about the error
				
				System.out.println( Library.CODEEX ); 
				exception.printStackTrace( );	
				
			}
				
		}
		
		// update rtmp message every one second
		
		if ( ++counter > stepRound )
		{
			
			update( );
			counter = 0;
			
		}
		
	}
	
	/**
	 * updates RTMP flow related events
	 **/
	
	public void update ( )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + id + " ClientController.update" );

		currentTime = System.currentTimeMillis( );
		
		if ( accepted )
		{
			
			// sending out ping
			
			if ( currentTime - lastPing > Library.PINGTIME ) sendPing( 6 , ( int ) lastPing & 0xffffffff , -1 , -1 );
			
			// updating bandwidth information
			
			if ( currentTime - lastBand > Library.BANDTIME ) updateBand( currentTime - lastBand );
						
		}
		
	}
	
	/**
	 * Updates bandwidth data
	 * @param delayX time delay since last band update
	 **/
	
	public void updateBand ( long delayX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + id + " ClientController.updateBand " + socketController.bytesIn );

		lastBand = System.currentTimeMillis( );

		bytesIn = socketController.bytesIn;
		bytesOut = socketController.bytesOut;

		bandIn = ( bytesIn - lastBytesIn ) / ( delayX / 1000 );
		bandOut = ( bytesOut - lastBytesOut ) / ( delayX / 1000 );

		lastBytesIn = bytesIn;
		lastBytesOut = bytesOut;
		
		// if we exceeded incoming byte step, send new read

		if ( bytesIn - lastRead > readStepClient ) sendRead( );

	}
	
	/**
	 * Sorts incoming data packets
	 * @param packetX RtmpPacket packet to sort
	 **/
	
	public void receivePacket ( RtmpPacket packetX )
	{
		
		// System.out.println( System.currentTimeMillis() + " " + id + " ClientController.receivePacket " + packetX );
		
		switch ( packetX.bodyType )
		{
		
			// 0x03 - Read amount
			// 0x04 - Ping received
			// 0x05 - Server or donwloading bandwidth
			// 0x06 - Clint or uploading bandwidth
			// 0x12 - Stream metadata
			// 0x14 - Invoke
		
			case 0x03 : receiveRead( packetX ); break;
			case 0x04 : receivePing( packetX ); break;
			case 0x05 : receiveReadStepClient( packetX ); break;
			case 0x06 : receiveReadStepServer( packetX ); break;
			case 0x12 : receiveInvoke( packetX ); break;
			case 0x14 : receiveInvoke( packetX ); break;
			default   : receiveUnknown( packetX ); break;
		
		}
		
	}
	
	/**
	 * Sends read bytes amount to client
	 **/	
	
	public void sendRead ( )
	{

		// System.out.println( System.currentTimeMillis( ) + " " + id + " ClientController.sendRead " + bytesIn );

		RtmpPacket packet = new RtmpPacket( );
		
		// sending received bytes amount as a 4 byte plain integer

		packet.rtmpChannel = 0x02;
		packet.bodyType = 0x03;
		packet.body = Encoder.concatenate( Encoder.intToBytes( bytesIn , 8 ) );

		// System.out.println( System.currentTimeMillis( ) + " " + id + " ClientController.sendRead " + Encoder.getHexa( packet.body ) );

		lastRead = bytesIn;
		addOutgoingPacket( packet ); 
		
	}
	
	/**
	 * Receives read notification from client, have to compare with our write amount, if there is a big
	 * difference, we have to decrease data output
	 * @param packetX RtmpPacket incoming packet
	 **/
	
	public void receiveRead ( RtmpPacket packetX )
	{
	
		// System.out.println( System.currentTimeMillis( ) + " " + id + " ClientController.receiveRead " + packetX );
		
		long clientRead = Encoder.bytesToInt( packetX.body );

		// System.out.println( System.currentTimeMillis() + " clientRead: " + clientRead + " difference: " + difference );

		// if difference is big, drop frames
		
		if ( clientRead < ( bytesOut - 10000 ) ) streamController.addDropping( );
		
		// if difference is small, remove dropping
		
		if ( clientRead >= ( bytesOut - 10000 ) ) streamController.removeDropping( );
		
	}
	
	/**
	 * Sends a ping message
	 * @param typeX type of ping
	 * @param p1X...p3X ping parts 
	 **/	
		
	public void sendPing ( int typeX , int p1X , int p2X , int p3X )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + id + " ClientController.sendPing " + typeX + " " + p1X + " " +p2X + " " + p3X );
		
		// checking ping timeout
					
		if ( ping > Library.PINGTIMEOUT ) detach( );
		else 
		{
			
			addOutgoingPacket( RtmpFactory.ping( typeX , p1X , p2X , p3X ) );
			lastPing = System.currentTimeMillis( );
		
		}
			
	}
	
	/**
	 * Receives ping from client
	 * receivePing : rtmp flow control ping uzenetek. elso ket byte a tipus. 00 - stream reset , 01 - stream buffer clear, 
	 * 03 - stream buffer meret beallitas , 06 - ping , 07 -pong, 08 - first ping talan
	 * @param packetX
	 **/
	
	public void receivePing ( RtmpPacket packetX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + id + " RTMPFlowController.receivePing " + packetX );
		
		byte [ ] body = packetX.body;
		
		// extracting parts from body
		
		int type = Encoder.bytesToInt( new byte [ ] { body[ 0 ] , body[ 1 ] } );
		int part1 = Encoder.bytesToInt( new byte [ ] { body[ 2 ] , body[ 3 ] , body[ 4 ] , body[ 5 ] } );
		int part2 = body.length > 6 ? Encoder.bytesToInt( new byte [ ] { body[ 6 ] , body[ 7 ] , body[ 8 ] , body[ 9 ] } ) : 0;
		int part3 = body.length > 10 ? Encoder.bytesToInt( new byte [ ] { body[ 10 ] , body[ 11 ] , body[ 12 ] , body[13]} ) : 0;
		
		switch ( type )
		{

			// stream buffer length, sending it to router, and sending buffer clear ping message
			// part1 is the flv channel this case
				
			case 3 : streamController.setBufferLength( part1 , part2 );	break;
			
			// normal ping request, sending pong
			
			case 6 : sendPing( 7 , part1 , part2 , part3 );	break;
				
			// normal pong, checking roundtrip delay

			case 7 : ping = System.currentTimeMillis() - lastPing; break;
				
			// first ping ?

			case 8 : break;
			
			// unknown
				
			// default : System.out.println( "Ping: " + Encoder.getHexa( packetX.body ) );	break;
		
		}
		
	}
	
	/**
	 * Sends server ( server side download ) bandwidth in passive connection
	 * @param packetX
	 **/
	
	public void sendReadStepServer ( int stepX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + id + " ClientController.sendReadStepServer " );
		
		RtmpPacket packet = new RtmpPacket ( );
		packet.bodyType = 0x05;
		packet.rtmpChannel = 0x02;
		packet.body = Encoder.intToBytes( stepX , 4 );
		
		addOutgoingPacket( packet );
		
	}
	
	/**
	 * Receives server ( server side download ) bandwidth in active connection
	 * @param packetX
	 */
	
	public void receiveReadStepClient ( RtmpPacket packetX )
	{
		
		// System.out.println( System.currentTimeMillis() + " " + id + " ClientController.receiveReadStepClient " + packetX ); 
		// System.out.println( Encoder.bytesToInt( packetX.body ) );

	}
	
	/**
	 * Sends client ( client side download ) bandwidth in passive connection
	 * @param packetX
	 **/
	
	public void sendReadStepClient ( int multiX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + id + " ClientController.sendReadStepClient " );

		RtmpPacket packet = new RtmpPacket ( );
		packet.bodyType = 0x06;
		packet.rtmpChannel = 0x02;
 		packet.body = Encoder.concatenate( Encoder.intToBytes( multiX , 4 ) , new byte [ ] { 0x02 } );
		
		addOutgoingPacket( packet );
		
	}

	/**
	 * Receives client ( client side download ) bandwidth in active connection
	 * @param packetX
	 **/
	
	public void receiveReadStepServer ( RtmpPacket packetX )
	{
		
		// System.out.println( System.currentTimeMillis() + " " + id + " RTMPFlowController.receiveReadStepServer " + packetX ); 
		// System.out.println( Encoder.bytesToInt( packetX.body ) );

	}
	
	/**
	 * Unknown packet type
	 * @param packet
	 */
	
	public void receiveUnknown ( RtmpPacket packetX )
	{
		
		// System.out.println( System.currentTimeMillis() + " " + id + "Unknown RtmpPacket " + packetX.toString( ) );
		
	}

	/**
	 * Receives invoke
	 * @param packetX rtmp packet
	 **/
	
	public void receiveInvoke ( RtmpPacket packetX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + id + " ClientController.receiveInvoke " );
		
		try 
		{ 
			
			// decode packet
			
			WrapperList arguments = Encoder.decode( packetX.body ); 
			String invokeId = arguments.getString( 0 );
			
			// System.out.println( "invoke: " + invokeId );
			
			if ( invokeId.equals( "receiveAudio" ) ) streamController.onAudioReceiveState	( arguments , packetX ); else
			if ( invokeId.equals( "receiveVideo" ) ) streamController.onVideoReceiveState	( arguments , packetX ); else
			if ( invokeId.equals( "createStream" ) ) streamController.onStreamCreateRequest	( arguments , packetX ); else
			if ( invokeId.equals( "deleteStream" ) ) streamController.onStreamDeleteRequest	( arguments , packetX ); else
			if ( invokeId.equals( "closeStream"  ) ) streamController.onStreamCloseRequest	( arguments , packetX ); else
			if ( invokeId.equals( "play" 		 ) ) streamController.onStreamPlayRequest	( arguments , packetX ); else
			if ( invokeId.equals( "pause" 		 ) ) streamController.onStreamPauseRequest	( arguments , packetX ); else
			if ( invokeId.equals( "seek" 		 ) ) streamController.onStreamSeekRequest	( arguments , packetX ); else
			if ( invokeId.equals( "speed" 		 ) ) streamController.onStreamSpeedRequest	( arguments , packetX ); else
			if ( invokeId.equals( "publish" 	 ) ) streamController.onStreamPublishRequest( arguments , packetX ); else
			if ( invokeId.equals( "measure"  	 ) ) onMeasure								( arguments , packetX ); else
			if ( invokeId.equals( "_error"  	 ) ) onResult								( arguments , packetX ); else
			if ( invokeId.equals( "_result" 	 ) ) onResult								( arguments , packetX ); else
			if ( invokeId.equals( "onStatus"	 ) ) onStatus								( arguments , packetX ); else
			if ( invokeId.equals( "connect"		 ) ) onConnect								( arguments ); 			 else
													 onInvoke								( arguments );
			
		}
		catch ( IOException exception ) 
		{ 
			
			// decode exception happened, trace it
			
			System.out.println( Library.CODEEX ); 
			exception.printStackTrace( );
			
			// create status info
			
			WrapperMap status = new WrapperMap( Library.STATUSKEYS , Library.AMFERRORARR );
			status.put( "description" , exception.getMessage( ) );
			
			call( "onStatus" , new Wrapper( status ) );

		}
		
	}

	
	/**
	 * Connection object from client, connection initialization
	 * @param argumentsX Conneciton arguments
	 **/
	
	public void onConnect ( WrapperList argumentsX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + id + " ClientController.onConnect " + argumentsX.size( ) );

		// get info object, which is the third part after invoke id and channel
		
		WrapperMap info = argumentsX.getMap( 2 );
		String customId = info.get( "app" ).stringValue;
		
		// get agent info

		ip = socketController.socket.socket( ).getInetAddress( ).getHostName( );
		agent = info.get( "flashVer" ).stringValue;
		referrer = info.get( "swfUrl" ).stringValue;		
		application = Server.getApplication( customId );
		
		if ( application != null )
		{
						
			// setting read notify stepping
			
			sendReadStepServer( readStepServer );
			sendReadStepClient( readStepClient );

			// first ping event
			
			//sendPing( 8 , 0 , -1 , -1 );
			sendPing( 0 , 0 , -1 , -1 );

			// remove unnecessary arguments
			
			argumentsX.remove( 0 );
			argumentsX.remove( 0 );
			argumentsX.remove( 0 );

			// enter client, sync is needed because multiple client threads can enter an application
			
			synchronized ( application ) { application.onEnter( client , argumentsX ); }
			
		}
		else reject( new Wrapper( Library.NOINST + customId ) );	
		
	}

	/**
	 * Normal invoke from client
	 * @param argumentsX arguments
	 **/
	
	public void onInvoke ( WrapperList argumentsX )
	{
					
		if ( invokeListener != null ) 
		{
						
			String invokeId = argumentsX.getString( 0 );
			double invokeChannel = argumentsX.getDouble( 1 );

			// System.out.println( System.currentTimeMillis( ) + " " + id + " ClientController.onInvoke " + invokeId );

			// remove id, channel and null
			
			argumentsX.remove( 0 );
			argumentsX.remove( 0 );
			argumentsX.remove( 0 );
			
			// store invoke id for further response, dispatch event
			
			if ( invokeChannel != 0 ) outgoingInvokes.put( invokeChannel , invokeId );
			invokeListener.onEvent( new InvokeEvent( invokeId , invokeChannel , client , argumentsX ) );
			
		}
		
	}

	/**
	 * Invoke result from client.
	 * @param argumentsX arguments
	 **/

	public void onResult ( WrapperList argumentsX , RtmpPacket packetX )
	{
		
		// get channel and id
		
		double invokeChannel = argumentsX.getDouble( 1 );
		String invokeId = incomingInvokes.remove( invokeChannel );
		
		// System.out.println( System.currentTimeMillis( ) + " " + id + " ClientController.onResult " + invokeChannel + " " + invokeId );
		
		if ( invokeId.equals( "connect" ) ) 
		{
			
			// get result info
			
			WrapperMap info = argumentsX.getMap( 3 );
			String code = info.getString( "code" );
			
			// following invoke channels must be over 1
			
			incomingInvokes.put( ( double ) 1 , invokeId );
			
			// if success, set accepted true, else close
			
			if ( code.equals( StatusEvent.SUCCESS ) ) 
			{
				
				accepted = true;
				
				// dispatch success
				
				if ( statusListener != null ) statusListener.onEvent( new StatusEvent( info , client ) );
				
			}
			else 
			{
				
				// first dispatch close
				
				if ( statusListener != null ) statusListener.onEvent( new StatusEvent( info , client ) );
				
				// then close
				
				close( );	
				
			}
			
		}
		else
		if ( invokeId.equals( "createStream" ) ) 
		{
			
			// invoke result for createStream
			
			streamController.onCreateStream( argumentsX.getDouble( 3 ) );
			
		}
		
	}
	
	/**
	 * Status message from client
	 * @param argumentsX arguments info
	 * @param packetX rtmp packet
	 */
	
	public void onStatus ( WrapperList argumentsX , RtmpPacket packetX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + id + " ClientController.onStatus " + argumentsX );

		//for ( Wrapper wrapper : argumentsX ) System.out.println( wrapper );
		
		if ( packetX.bodyType == 0x12 ) 
		{
			WrapperMap info = argumentsX.getMap( 1 );
			for ( String key : info.keySet() ) System.out.println( key + " " + info.get( key ) );
			return;
		}
		WrapperMap info = argumentsX.getMap( 3 );
		if ( statusListener != null ) statusListener.onEvent( new StatusEvent( info , client ) );

	}
	
	/**
	 * Bandwidth meause request from client, storing read time and create string if needed
	 * @param argumentsX arguments
	 * @param packetX packet
	 **/
	
	public void onMeasure ( WrapperList argumentsX , RtmpPacket packetX )
	{

		// System.out.println( System.currentTimeMillis( ) + " " + id + " ClientController.onMeasure " );
		
		// store read time immediately
		// extract chanel

		double read = System.currentTimeMillis( );
		double size = argumentsX.getMap( 3 ).getDouble( "size" );
		double channel = argumentsX.getDouble( 1 );
		String value;
		String oldvalue = argumentsX.getMap( 3 ).getString( "value" );
		
		// create string with desired length - desired bytes
		
		//System.out.println( "size: " + size + " " + oldvalue.length( ) );
		
		char [ ] chars = new char [ ( int ) size ];
		value = new String( chars );
		
		// create message

		WrapperMap message = new WrapperMap( );
		message.put( "read" , read );
		message.put( "send" , System.currentTimeMillis( ) );
		message.put( "value" , value );
		
		//System.out.println( "read: " + (long)message.getDouble( "read" ) );
		//System.out.println( "send: " + (long)message.getDouble( "send" ) );
		//System.out.println( "value: " + message.getString( "value" ).length( ) );
		
		// send back message with values

		invoke( "_result" , channel , 0 , new WrapperList( new Wrapper( message ) ) );
		
	}
		
	/**
	 * Invoke calls to client
	 * @param idX Invoke Id
	 * @param argumentsX Invoke arguments
	 */
	
	public void call ( String callIdX ) { invoke( callIdX , 0 , 0 , new WrapperList( ) ); }
	public void call ( String callIdX , Wrapper argumentX ) { invoke( callIdX , 0 , 0 , new WrapperList( argumentX ) ); }
	public void call ( String callIdX , Wrapper argumentX , int flvChannelX ) { invoke( callIdX , 0 , flvChannelX , new WrapperList( argumentX ) ); }
	public void call ( String callIdX , WrapperList argumentsX ) { invoke( callIdX , 0 , 0 , argumentsX ); }
	public void call ( String callIdX , WrapperList argumentsX , int flvChannelX ) { invoke( callIdX , 0 , flvChannelX , argumentsX ); }
	public void callResult ( double channelX , Wrapper argumentX ) { invoke( "_result" , channelX , 0 , new WrapperList( argumentX ) ); }
	public void callResult ( double channelX , WrapperList argumentsX )	{ invoke( "_result" , channelX ,  0 , argumentsX ); }
	public void callResponse ( String callIdX , WrapperList argumentsX , int flvChannelX ) 
	{ 
		
		double invokeChannel = incomingInvokes.size( );
		incomingInvokes.put( invokeChannel , callIdX );
		invoke( callIdX , invokeChannel , flvChannelX , argumentsX );
		
	}	

	/**
	 * Invoke to client
	 * @param channelX invoke channel
	 * @param argumentsX arguments
	 **/
	
	public void invoke ( String callIdX , double invokeChannelX , int flvChannelX , WrapperList argumentsX )
	{
		
		// System.out.println( System.currentTimeMillis() + " ClientController.invoke " + callIdX + " " + invokeChannelX );

		// create invoke packet
		
		RtmpPacket call = new RtmpPacket( );
		WrapperList arguments = new WrapperList( );

		// create invoke object
				
		arguments.add( 0 , new Wrapper( callIdX ) );
		arguments.add( 1 , new Wrapper( invokeChannelX ) );
		arguments.add( 2 , new Wrapper( ) );
		arguments.addAll( argumentsX );
		
		// fill up packet

		call.flvChannel = flvChannelX;
		call.bodyType = 0x14;
		call.body = Encoder.encode( arguments );
		
		// send
		
		if ( !closed ) addOutgoingPacket( call );		
		
	}

	/**
	 * Accepts the client 
	 * @param wrapperX - an information object passed to the client
	 **/
	
	public void accept ( Wrapper wrapperX )
	{

		// System.out.println( System.currentTimeMillis( ) + " " + id + " ClientController.accept" + closed + " " + mode );
		
		if ( !closed && mode.equals( "passive" ) ) 
		{
			
			// create acception status
			
			WrapperMap info = new WrapperMap( Library.STATUSKEYS , Library.SUCCESSARR );
			info.put( "application" , wrapperX );
			
			RtmpPacket call = new RtmpPacket( );
			WrapperList message = new WrapperList( );
			
			// create invoke object
			
			message.add( new Wrapper( "_result" ) );
			message.add( new Wrapper( 1 ) );
			message.add( new Wrapper(   ) );
			message.add( new Wrapper( info ) );
			
			// fill up packet

			call.flvChannel = 0;
			call.bodyType = 0x14;
			call.body = Encoder.encode( message );
			
			// send back as result, register app
			
			addOutgoingPacket( call );
			Server.registerClient( application , this );			
			accepted = true;
		
		}
		
	}
	
	/**
	 * Rejects the client, called from a custom applications. Sends a standard rejection object as onStatus
	 * @param wrapperX - an information object passed to the client
	 */
	
	public void reject ( Wrapper wrapperX )
	{
		
		// System.out.println( System.currentTimeMillis( ) + " " + id + " ClientController.reject closed: " + closed + " mode: " + mode );
		
		if ( !closed && mode.equals( "passive" )) 
		{
			
			// create rejection status

			WrapperMap info = new WrapperMap( Library.STATUSKEYS , Library.REJECTIONARR );

			RtmpPacket call = new RtmpPacket( );
			WrapperList message = new WrapperList( );
				
			info.put( "application" , wrapperX );
			
			// send back as result
			
			// create invoke object
			
			message.add( new Wrapper( "_result" ) );
			message.add( new Wrapper( 1 ) );
			message.add( new Wrapper(   ) );
			message.add( new Wrapper( info ) );
			
			// fill up packet

			call.flvChannel = 0;
			call.bodyType = 0x14;
			call.body = Encoder.encode( message );
			
			// send back as result, register app
			
			addOutgoingPacket( call );
			
			socketController.takePackets( outgoingList );
			socketController.closeInited = true;
		
		}
		
	}
	
	/**
	 * Other methods
	 */
	
	public void detach ( ) { socketController.close( "detach" ); }
	
	public void addStreamEventListener ( EventListener streamListenerX ) { streamListener = streamListenerX; }
	public void addInvokeEventListener ( EventListener invokeListenerX ) { invokeListener = invokeListenerX; }
	public void addStatusEventListener ( EventListener statusListenerX ) { statusListener = statusListenerX; }
	
	public HashMap < Integer , Stream > getPlayers ( ) { if ( !closed ) return streamController.getPlayers( ); else return null; }	
	public HashMap < Integer , Stream > getRouters ( ) { if ( !closed ) return streamController.getRouters( ); else return null; }


}
