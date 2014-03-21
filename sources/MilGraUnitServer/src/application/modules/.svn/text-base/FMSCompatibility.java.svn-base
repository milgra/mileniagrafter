package application.modules;

import com.milgra.server.api.Client;
import com.milgra.server.api.EventListener;
import com.milgra.server.api.IApplication;
import com.milgra.server.api.StatusEvent;
import com.milgra.server.api.Stream;
import com.milgra.server.api.Wrapper;
import com.milgra.server.api.WrapperList;

public class FMSCompatibility 
{
	public String url;
	public Stream stream;
	public Client client;
	public Client testClient;
	public IApplication application;
	
	/**
	 * FMSCompatibility constructor
	 * @param applicationX mother application
	 */
	
	public FMSCompatibility ( IApplication applicationX )
	{
		
		System.out.println( System.currentTimeMillis( ) + " FMSCompatibility.construct " );
		application = applicationX;
		
	}
	
	/**
	 * Client entering point
	 * @param clientX client
	 * @param argumentsX arguments
	 */
	
	public void onEnter ( Client clientX , WrapperList argumentsX )
	{

		System.out.println( System.currentTimeMillis( ) + " FMSCompatibility.onEnter " + clientX + " " + argumentsX );

		url = argumentsX.getString( 1 );

		client = clientX;
		client.accept( );

		client.call( "log" , new Wrapper( "Connection... " ) );
		
		// create remote client

		EventListener statusListener = new EventListener ( )
		{
				
			public void onEvent ( StatusEvent eventX ) { onStatus( eventX ); }
				
		};

		testClient = new Client( application );
		testClient.addStatusEventListener( statusListener );
		testClient.connect( "rtmp://" + url + "/milgraunit" , new Wrapper( ) );
		
	}
	
	/**
	 * Closes module
	 **/
	
	public void onLeave ( Client clientX ) 
	{
		
		System.out.println( System.currentTimeMillis( ) +  " FMSCompatibility.onClose " );		
		
	}	
	
	/**
	 * Remote client status
	 * @param eventX statusevent
	 */
	
	public void onStatus ( StatusEvent eventX )
	{
		
		System.out.println( System.currentTimeMillis( ) + " FMSCompatibility.onStatus " + eventX.code );
		client.call( "log" , new Wrapper( "Status: " + eventX.code ) );
		
		// connection status

		if ( eventX.code.equals( StatusEvent.SUCCESS ) )
		{
			
			stream = new Stream( "fmspulled" , testClient );
			stream.publish( "fmslive" , "live" );
			
			stream = new Stream( "fmspulled" , testClient );
			stream.play( "fmspulled" );
			
			stream = new Stream( "fmsdemand" , testClient );
			stream.play( "milgra.flv" );

		}
	
		
	}
	
	/**
	 * Playing normalClone stream from remote server
	 */
	
	public void normalPlay ( )
	{

		System.out.println( System.currentTimeMillis( ) + " FMSCompatibility.normalPlay " );
		client.call( "log" , new Wrapper( "Playing normal stream " ) );
		
		stream = new Stream( "normalRemoteClone" , testClient );
		stream.play( "normalRemote" );
		
	}
	
	public void normalPublish ( )
	{
		
		System.out.println( System.currentTimeMillis( ) + " FMSCompatibility.normalPublish " );

		client.call( "log" , new Wrapper( "Publishing normal stream " ) );
		
		stream = new Stream( "normalRemote" , testClient );
		stream.publish( "normal" , "live" );
		
	}
}
