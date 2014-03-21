package application.modules;


import com.milgra.server.api.Client;
import com.milgra.server.api.WrapperList;
import com.milgra.server.api.IApplication;


public class StreamControl implements IModule
{
	
	public Client client;
	
	public StreamControl ( IApplication applicationX ) { }
	
	public void onEnter ( Client clientX , WrapperList argumentsX )
	{
		
		System.out.println( System.currentTimeMillis( ) + "StreamControl.onEnter" );
		
		client = clientX;
		client.accept( );			
		
	}	
	
	/**
	 * Closes module
	 **/
	
	public void onLeave ( Client clientX ) 
	{
		
		System.out.println( System.currentTimeMillis( ) +  " StreamControl.onClose " );
		
		
	}

}
