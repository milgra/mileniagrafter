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

package com.milgra.server.api;

/**
	
	WrapperMap class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080316

	Tasks of WrapperMap
	
		- contain multiple wrappers as a map
 
**/

import java.util.Map;
import java.util.HashMap;

public class WrapperMap extends HashMap < String , Wrapper > 
{
	
	public static final long serialVersionUID = 0;
	
	public WrapperMap ( ) {	super( ); }
	public WrapperMap ( Map < String , Wrapper > sourceX ) { super( sourceX ); }
	public WrapperMap ( String [ ] keysX , Object [ ] valuesX ) 
	{ 
		super( );
		for ( int a = 0 ; a < keysX.length ; ++a )
		{
			
			if ( valuesX[a] != null )
			{

				if ( valuesX[a] instanceof Double )	put( keysX[a] , ( Double ) valuesX[a] ); 
				if ( valuesX[a] instanceof String )	put( keysX[a] , ( String ) valuesX[a] ); 
				if ( valuesX[a] instanceof Boolean ) put( keysX[a] , ( Boolean ) valuesX[a] ); 
				
			}
			
		}
		
	}

	public void put ( String keyX )							{ put( keyX , new Wrapper( 			) ); }
	public void put ( String keyX , String stringX 		)	{ put( keyX , new Wrapper( stringX 	) ); }	
	public void put ( String keyX , double doubleX 		)	{ put( keyX , new Wrapper( doubleX 	) ); }
	public void put ( String keyX , boolean booleanX 	) 	{ put( keyX , new Wrapper( booleanX ) ); }
	public void put ( String keyX , WrapperMap mapX 	)	{ put( keyX , new Wrapper( mapX 	) ); }
	public void put ( String keyX , WrapperList listX 	) 	{ put( keyX , new Wrapper( listX 	) ); }
	
	public String getType 		( String keyX )	{ return get( keyX ).type; 			}
	public String getString 	( String keyX )	{ return get( keyX ).stringValue; 	}
	public double getDouble 	( String keyX )	{ return get( keyX ).doubleValue; 	}
	public boolean getBoolean 	( String keyX ) { return get( keyX ).booleanValue; 	}
	public WrapperMap getMap 	( String keyX ) { return get( keyX ).mapValue;		}
	public WrapperList getList 	( String keyX ) { return get( keyX ).listValue; 	}

}
