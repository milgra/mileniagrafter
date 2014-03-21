package application.modules;

import com.milgra.server.api.Client;
import com.milgra.server.api.WrapperList;

public interface IModule 
{
	
	void onEnter ( Client clientX , WrapperList argumentsX );
	void onLeave ( Client clientX );

}
